package com.lmrick.timescheduler.controller;

import com.lmrick.timescheduler.infrastructure.dto.*;
import com.lmrick.timescheduler.infrastructure.entity.UserEntity;
import com.lmrick.timescheduler.services.AuthService;
import com.lmrick.timescheduler.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication, token management and user session control")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final AuthService authService;
	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager,
					UserService userService, AuthService authService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.authService = authService;
	}
	
	@PostMapping("/login")
	@Operation(
					summary = "User login",
					description = "Authenticates user and returns JWT access and refresh tokens"
	)
	@ApiResponses(value = {
					@ApiResponse(
									responseCode = "200",
									description = "Login successful",
									content = @Content(
													mediaType = "application/json",
													schema = @Schema(example = """
                                    {
                                      "accessToken": "jwt-token",
                                      "refreshToken": "refresh-token"
                                    }
                                    """)
									)
					),
					@ApiResponse(
									responseCode = "401",
									description = "Invalid credentials"
					)
	})
	public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
		Authentication auth = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(
										request.username(),
										request.password()
						)
		);
		
		UserEntity user = userService.getEntityByUsername(auth.getName());
		
		return ResponseEntity.ok(userService.login(user));
	}
	
	@PostMapping("/register")
	@Operation(
					summary = "Register a new user",
					description = "Creates a new user account in the system"
	)
	@ApiResponses(value = {
					@ApiResponse(
									responseCode = "200",
									description = "User created successfully",
									content = @Content(
													mediaType = "application/json",
													schema = @Schema(implementation = UserResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "400",
									description = "Invalid input data"
					)
	})
	public ResponseEntity<UserResponseDTO> register(@RequestBody CreateUserDTO dto) {
		return ResponseEntity.ok(authService.createUser(dto));
	}
	
	@PostMapping("/refresh")
	@Operation(
					summary = "Refresh token",
					description = "Generates a new access token using a valid refresh token"
	)
	@ApiResponses(value = {
					@ApiResponse(
									responseCode = "200",
									description = "Token refreshed successfully"
					),
					@ApiResponse(
									responseCode = "401",
									description = "Invalid or expired refresh token"
					)
	})
	public ResponseEntity<AuthResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO request) {
		return ResponseEntity.ok(
						userService.refresh(request.refreshToken())
		);
	}
	
	@PostMapping("/logout")
	@Operation(
					summary = "User logout",
					description = "Invalidates the current JWT token"
	)
	@ApiResponses(value = {
					@ApiResponse(
									responseCode = "200",
									description = "Logged out successfully"
					)
	})
	public ResponseEntity<Void> logout(
					@Parameter(description = "Bearer JWT token", required = true)
					@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.substring(7);
		userService.logout(token);
		return ResponseEntity.ok().build();
	}
	
}
