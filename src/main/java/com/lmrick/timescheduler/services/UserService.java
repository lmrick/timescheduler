package com.lmrick.timescheduler.services;

import com.lmrick.timescheduler.infrastructure.dto.AuthResponseDTO;
import com.lmrick.timescheduler.infrastructure.dto.UserResponseDTO;
import com.lmrick.timescheduler.infrastructure.entity.Role;
import com.lmrick.timescheduler.infrastructure.entity.UserEntity;
import com.lmrick.timescheduler.infrastructure.exceptions.InvalidTokenException;
import com.lmrick.timescheduler.infrastructure.exceptions.RefreshTokenRevokedException;
import com.lmrick.timescheduler.infrastructure.exceptions.ResourceNotFoundException;
import com.lmrick.timescheduler.infrastructure.repository.UserRepository;
import com.lmrick.timescheduler.security.JwtTokenInfo;
import com.lmrick.timescheduler.security.JwtUtil;
import io.jsonwebtoken.Claims;
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
	
	public AuthResponseDTO login(String username) {
		UserEntity user = getEntityByUsername(username);
		
		String accessToken = jwtUtil.generateToken(
						user.getUsername(),
						user.getTokenVersion(),
						user.getRole().name()
		);
		
		String refreshToken = jwtUtil.generateRefreshToken(
						user.getUsername(),
						user.getTokenVersion()
		);
		
		user.setRefreshTokenHash(hash(refreshToken));
		userRepository.save(user);
		
		return new AuthResponseDTO(
						accessToken,
						refreshToken,
						new UserResponseDTO(
										user.getUsername(),
										user.getRole().name()
						)
		);
	}
	
	public AuthResponseDTO refresh(String refreshToken) {
		
		JwtTokenInfo tokenInfo;
		
		try {
			tokenInfo = jwtUtil.parseToken(refreshToken);
		} catch (Exception e) {
			throw new InvalidTokenException("Invalid refresh token");
		}
		
		if (tokenInfo.username() == null) {
			throw new InvalidTokenException("Invalid refresh token");
		}
		
		UserEntity user = getEntityByUsername(tokenInfo.username());
		
		if (!jwtUtil.validateRefreshToken(
						tokenInfo,
						user.getTokenVersion()
		)) {
			throw new InvalidTokenException("Invalid refresh token");
		}
		
		if (!hash(refreshToken).equals(user.getRefreshTokenHash())) {
			throw new RefreshTokenRevokedException(
							"Refresh token reused or revoked"
			);
		}
		
		String newAccessToken = jwtUtil.generateToken(
						user.getUsername(),
						user.getTokenVersion(),
						user.getRole().name()
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
		JwtTokenInfo tokenInfo;
		
		try {
			tokenInfo = jwtUtil.parseToken(token);
		} catch (Exception e) {
			throw new InvalidTokenException("Invalid token");
		}
		
		if (tokenInfo.username() == null) {
			throw new InvalidTokenException("Invalid refresh token");
		}
		
		UserEntity user = getEntityByUsername(tokenInfo.username());
		
		if (!jwtUtil.validateToken(tokenInfo, user.getTokenVersion())) {
			throw new InvalidTokenException("Invalid token");
		}
		
		user.setTokenVersion(user.getTokenVersion() + 1);
		user.setRefreshTokenHash(null);
		
		userRepository.save(user);
	}
	
	public UserResponseDTO updateRole(Long id, Role role) {
		UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		if (user.getRole() != role) {
			user.setRole(role);
			user.setTokenVersion(user.getTokenVersion() + 1);
			user.setRefreshTokenHash(null);
			
			userRepository.save(user);
		}
		
		return new UserResponseDTO(
						user.getUsername(),
						user.getRole().name()
		);
	}
	
	private String hash(String token) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			
			byte[] hash = digest.digest(
							token.getBytes(StandardCharsets.UTF_8)
			);
			
			return Base64.getEncoder().encodeToString(hash);
			
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}
	
}