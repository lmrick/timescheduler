package com.lmrick.timescheduler.security;

import com.lmrick.timescheduler.infrastructure.entity.UserEntity;
import com.lmrick.timescheduler.infrastructure.exceptions.ResourceNotFoundException;
import com.lmrick.timescheduler.infrastructure.repository.UserRepository;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	
	public JwtFilter(
					JwtUtil jwtUtil,
					UserRepository userRepository
	) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
	}
	
	@Override
	protected void doFilterInternal(
					@Nonnull HttpServletRequest request,
					@Nonnull HttpServletResponse response,
					@Nonnull FilterChain filterChain
	) throws ServletException, IOException {
		
		String path = request.getRequestURI();
		
		if (path.startsWith("/auth") || path.startsWith("/swagger") || path.startsWith("/v3")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			String authHeader = request.getHeader("Authorization");
			
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			
			String token = authHeader.substring(7);
			
			JwtTokenInfo tokenInfo;
			
			try {
				tokenInfo = jwtUtil.parseToken(token);
			} catch (Exception e) {
				log.warn("Invalid JWT signature/structure: {}", e.getMessage());
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			
			if (tokenInfo.username() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				UserEntity user = userRepository.findByUsername(tokenInfo.username())
								.orElseThrow(() -> new ResourceNotFoundException("User not found"));
				
				if (!jwtUtil.validateToken(tokenInfo, user.getTokenVersion())) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
				
				UserDetails userDetails = User.builder()
								.username(user.getUsername())
								.password("")
								.roles(user.getRole().name())
								.build();
				
				UsernamePasswordAuthenticationToken auth =
								new UsernamePasswordAuthenticationToken(
												userDetails,
												null,
												userDetails.getAuthorities()
								);
				
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			
			filterChain.doFilter(request, response);
			
		} catch (Exception e) {
			log.warn("JWT filter error: {}", e.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
}