package com.lmrick.timescheduler.security;

import com.lmrick.timescheduler.infrastructure.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String SECRET;
	
	@Value("${jwt.expiration}")
	private long EXPIRATION;
	
	@Value("${jwt.refresh-expiration}")
	private long REFRESH_EXPIRATION;
	
	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}
	
	public JwtTokenInfo parseToken(String token) {
		Claims claims = extractClaims(token);
		String role = claims.get("role", String.class);
		
		return new JwtTokenInfo(
						claims.getSubject(),
						claims.get("version", Long.class),
						role != null ? Role.valueOf(role) : null,
						claims.getExpiration()
		);
	}
	
	private Claims extractClaims(String token) {
		return Jwts.parser()
						.verifyWith(getKey())
						.build()
						.parseSignedClaims(token)
						.getPayload();
	}
	
	public String generateToken(String username, Long version, String role) {
		return Jwts.builder()
						.subject(username)
						.claim("version", version)
						.claim("role", role)
						.issuedAt(new Date())
						.expiration(new Date(System.currentTimeMillis() + EXPIRATION))
						.signWith(getKey())
						.compact();
	}
	
	public String generateRefreshToken(
					String username,
					Long version
	) {
		return Jwts.builder()
						.subject(username)
						.claim("version", version)
						.issuedAt(new Date())
						.expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
						.signWith(getKey())
						.compact();
	}

	public boolean validateToken(
					JwtTokenInfo tokenInfo,
					Long currentVersion
	) {
		return tokenInfo.username() != null
					 && tokenInfo.version().equals(currentVersion)
					 && tokenInfo.expiration().after(new Date());
	}
	
	public boolean validateRefreshToken(
					String token,
					Long currentVersion
	) {
		try {
			Claims claims = extractClaims(token);
			
			Long tokenVersion = claims.get("version", Long.class);
			
			return claims.getSubject() != null
						 && tokenVersion.equals(currentVersion)
						 && claims.getExpiration().after(new Date());
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean validateRefreshToken(
					JwtTokenInfo tokenInfo,
					Long currentVersion
	) {
		return tokenInfo.username() != null
					 && tokenInfo.version().equals(currentVersion)
					 && tokenInfo.expiration().after(new Date());
	}
	
}