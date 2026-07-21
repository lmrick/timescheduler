package com.lmrick.timescheduler.services;

import com.lmrick.timescheduler.infrastructure.entity.UserEntity;
import com.lmrick.timescheduler.infrastructure.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository repository;
	
	public CustomUserDetailsService(UserRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public @NonNull UserDetails loadUserByUsername(@NonNull String username) {
		UserEntity user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		return User.builder()
						.username(user.getUsername())
						.password(user.getPassword())
						.roles(user.getRole() != null ? user.getRole().name() : "USER")
						.build();
	}
	
}