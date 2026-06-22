package com.lmrick.timescheduler.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String SECRET;
	
	@Value("${jwt.expiration}")
	private long EXPIRATION;
	
	@Value("${jwt.refresh-expiration}")
	private long REFRESH_EXPIRATION;
	
	private Key getKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
						.setSigningKey(getKey())
						.build()
						.parseClaimsJws(token)
						.getBody();
	}
	
	public String generateToken(String username, Long version) {
		return Jwts.builder()
						.setSubject(username)
						.claim("version", version)
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
						.signWith(getKey(), SignatureAlgorithm.HS256)
						.compact();
	}
	
	public String generateRefreshToken(String username, Long version) {
		return Jwts.builder()
						.setSubject(username)
						.claim("version", version)
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
						.signWith(getKey(), SignatureAlgorithm.HS256)
						.compact();
	}
	
	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}
	
	public Long getTokenVersion(String token) {
		return extractAllClaims(token).get("version", Long.class);
	}
	
	public boolean isTokenExpired(String token) {
		return extractAllClaims(token)
						.getExpiration()
						.before(new Date());
	}
	
	public boolean validateToken(String token, Long currentVersion) {
		try {
			Claims claims = extractAllClaims(token);
			
			String username = claims.getSubject();
			Long tokenVersion = claims.get("version", Long.class);
			Date expiration = claims.getExpiration();
			
			return username != null
						 && tokenVersion.equals(currentVersion)
						 && expiration.after(new Date());
			
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean validateRefreshToken(String token) {
		try {
			Claims claims = extractAllClaims(token);
			
			return claims.getSubject() != null
						 && claims.getExpiration().after(new Date());
			
		} catch (Exception e) {
			return false;
		}
	}
	
}