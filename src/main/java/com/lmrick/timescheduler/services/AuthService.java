package com.lmrick.timescheduler.services;

import com.lmrick.timescheduler.infrastructure.dto.CreateUserDTO;
import com.lmrick.timescheduler.infrastructure.dto.UserResponseDTO;
import com.lmrick.timescheduler.infrastructure.entity.UserEntity;
import com.lmrick.timescheduler.infrastructure.exceptions.ConflictException;
import com.lmrick.timescheduler.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public AuthService(UserRepository userRepository,
					PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public UserResponseDTO createUser(CreateUserDTO dto) {
		if (userRepository.findByUsername(dto.username()).isPresent()) {
			throw new ConflictException("Username already exists");
		}
		
		UserEntity user = new UserEntity();
		user.setUsername(dto.username());
		user.setPassword(passwordEncoder.encode(dto.password()));
		user.setRole(dto.role());
		user.setTokenVersion(0L);
		
		UserEntity saved = userRepository.save(user);
		
		return new UserResponseDTO(
						saved.getUsername(),
						saved.getRole().name()
		);
	}
	
}
