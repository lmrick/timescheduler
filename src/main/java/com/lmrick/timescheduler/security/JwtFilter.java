package com.lmrick.timescheduler.security;

import com.lmrick.timescheduler.infrastructure.entity.UserEntity;
import com.lmrick.timescheduler.infrastructure.exceptions.UserNotFoundException;
import com.lmrick.timescheduler.infrastructure.repository.UserRepository;
import com.lmrick.timescheduler.services.CustomUserDetailsService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final CustomUserDetailsService userDetailsService;
	
	public JwtFilter(
					JwtUtil jwtUtil,
					UserRepository userRepository,
					CustomUserDetailsService userDetailsService
	) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(
					@NonNull HttpServletRequest request,
					@Nonnull HttpServletResponse response,
					@Nonnull FilterChain filterChain
	) throws ServletException, IOException {
		String path = request.getRequestURI();
		
		// libera endpoints públicos
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
			
			String username;
			try {
				username = jwtUtil.extractUsername(token);
			} catch (Exception e) {
				log.warn("Invalid JWT signature/structure: {}", e.getMessage());
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				UserEntity user = userRepository.findByUsername(username)
								.orElseThrow(() -> new UserNotFoundException("User not found"));
				
				if (!jwtUtil.validateToken(token, user.getTokenVersion())) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
				
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
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