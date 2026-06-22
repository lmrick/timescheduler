package com.lmrick.timescheduler.controller;

import com.lmrick.timescheduler.infrastructure.dto.UserResponseDTO;
import com.lmrick.timescheduler.infrastructure.entity.UserEntity;
import com.lmrick.timescheduler.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User operations")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
	
	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/{username}")
	@Operation(summary = "Get user by username")
	public ResponseEntity<UserResponseDTO> getUser(@PathVariable String username) {
		UserResponseDTO user = userService.getByUsername(username);
		
		return ResponseEntity.ok(user);
	}
	
}
