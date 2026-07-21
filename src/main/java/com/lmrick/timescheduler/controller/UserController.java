package com.lmrick.timescheduler.controller;

import com.lmrick.timescheduler.infrastructure.dto.UserResponseDTO;
import com.lmrick.timescheduler.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	
	@GetMapping("/{id:\\d+}")
	@Operation(summary = "Get user by id")
	public ResponseEntity<UserResponseDTO> getById(
					@Parameter(description = "User ID", example = "1")
					@PathVariable Long id
	) {
		return ResponseEntity.ok(userService.getById(id));
	}
	
	@GetMapping
	@Operation(summary = "Get paginated users")
	public ResponseEntity<Page<UserResponseDTO>> getAll(
					@PageableDefault(size = 20) Pageable pageable
	) {
		return ResponseEntity.ok(userService.getAll(pageable));
	}
	
	@GetMapping("/search")
	@Operation(summary = "Get user by username")
	public ResponseEntity<UserResponseDTO> getByUsername(
					@Parameter(description = "Username", example = "JaneDoe")
					@RequestParam String username
	) {
		return ResponseEntity.ok(userService.getByUsername(username));
	}
	
}
