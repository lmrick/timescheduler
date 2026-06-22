package com.lmrick.timescheduler.config;

import com.lmrick.timescheduler.security.JwtFilter;
import com.lmrick.timescheduler.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final JwtFilter jwtFilter;
	private final CustomUserDetailsService service;
	
	public SecurityConfig(
					JwtFilter jwtFilter,
					CustomUserDetailsService service
	) {
		this.jwtFilter = jwtFilter;
		this.service = service;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
						.csrf(AbstractHttpConfigurer::disable)
						.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
						.sessionManagement(sm ->
										sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						)
						.authorizeHttpRequests(auth -> auth
										// Swagger
										.requestMatchers(
														"/v3/api-docs/**",
														"/swagger-ui/**",
														"/swagger-ui.html"
										).permitAll()
										
										.requestMatchers("/h2-console/**").permitAll()
										
										.requestMatchers("/auth/**").permitAll()
										.requestMatchers("/admin/**").hasRole("ADMIN")
										.anyRequest().authenticated()
						)
						.userDetailsService(service)
						.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
}