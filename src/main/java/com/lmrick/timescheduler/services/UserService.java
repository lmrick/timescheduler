package com.lmrick.timescheduler.services;

import com.lmrick.timescheduler.infrastructure.dto.AuthResponseDTO;
import com.lmrick.timescheduler.infrastructure.dto.UserResponseDTO;
import com.lmrick.timescheduler.infrastructure.entity.UserEntity;
import com.lmrick.timescheduler.infrastructure.exceptions.InvalidTokenException;
import com.lmrick.timescheduler.infrastructure.exceptions.RefreshTokenRevokedException;
import com.lmrick.timescheduler.infrastructure.exceptions.ResourceNotFoundException;
import com.lmrick.timescheduler.infrastructure.repository.UserRepository;
import com.lmrick.timescheduler.security.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	
	public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}
	
	public UserEntity getEntityByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}
	
	public UserResponseDTO getByUsername(String username) {
		UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return new UserResponseDTO(user.getUsername(), user.getRole().name());
	}
	
	public UserResponseDTO getById(Long id) {
		UserEntity user = userRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		return new UserResponseDTO(
						user.getUsername(),
						user.getRole().name()
		);
	}
	
	public Page<UserResponseDTO> getAll(Pageable pageable) {
		return userRepository.findAll(pageable)
						.map(user -> new UserResponseDTO(
										user.getUsername(),
										user.getRole().name()
						));
	}
	
	public AuthResponseDTO login(UserEntity user) {
		String accessToken = jwtUtil.generateToken(
						user.getUsername(),
						user.getTokenVersion()
		);
		
		String refreshToken = jwtUtil.generateRefreshToken(
						user.getUsername(),
						user.getTokenVersion()
		);
		
		user.setRefreshTokenHash(hash(refreshToken));
		userRepository.save(user);
		
		return new AuthResponseDTO(accessToken, refreshToken, new UserResponseDTO(user.getUsername(), user.getRole().name()));
	}
	
	public AuthResponseDTO refresh(String refreshToken) {
		
		if (!jwtUtil.validateRefreshToken(refreshToken)) {
			throw new InvalidTokenException("Invalid refresh token");
		}
		
		String username = jwtUtil.extractUsername(refreshToken);
		UserEntity user = getEntityByUsername(username);
		
		if (!user.getRefreshTokenHash().equals(hash(refreshToken))) {
			throw new RefreshTokenRevokedException("Refresh token reused or revoked");
		}
		
		String newAccessToken = jwtUtil.generateToken(
						user.getUsername(),
						user.getTokenVersion()
		);
		
		String newRefreshToken = jwtUtil.generateRefreshToken(
						user.getUsername(),
						user.getTokenVersion()
		);
		
		user.setRefreshTokenHash(hash(newRefreshToken));
		userRepository.save(user);
		
		return new AuthResponseDTO(
						newAccessToken,
						newRefreshToken,
						new UserResponseDTO(
										user.getUsername(),
										user.getRole().name()
						)
		);
	}
	
	public void logout(String token) {
		String username = jwtUtil.extractUsername(token);
		
		UserEntity user = getEntityByUsername(username);
		
		user.setTokenVersion(user.getTokenVersion() + 1);
		user.setRefreshTokenHash(null);
		
		userRepository.save(user);
	}
	
	private String hash(String token) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(hash);
	}
	
}