package com.lmrick.timescheduler.controller;

import com.lmrick.timescheduler.infrastructure.dto.UserResponseDTO;
import com.lmrick.timescheduler.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	@Operation(
					summary = "Get user by id",
					description = "Retrieves a user using the user ID"
	)
	@ApiResponses({
					@ApiResponse(
									responseCode = "200",
									description = "User found"
					),
					@ApiResponse(
									responseCode = "404",
									description = "User not found"
					)
	})
	public ResponseEntity<UserResponseDTO> getById(
					@Parameter(description = "User ID", example = "1")
					@PathVariable Long id
	) {
		return ResponseEntity.ok(userService.getById(id));
	}
	
	@GetMapping
	@Operation(
					summary = "Get paginated users",
					description = "Returns users with pagination support"
	)
	@ApiResponses({
					@ApiResponse(
									responseCode = "200",
									description = "Users retrieved successfully"
					),
					@ApiResponse(
									responseCode = "401",
									description = "Unauthorized - missing or invalid token"
					)
	})
	public ResponseEntity<Page<UserResponseDTO>> getAll(
					@Parameter(
									description = "Pagination parameters: page, size, sort"
					)
					@PageableDefault(size = 20) Pageable pageable
	) {
		return ResponseEntity.ok(userService.getAll(pageable));
	}
	
	@GetMapping("/search")
	@Operation(
					summary = "Get user by username",
					description = "Retrieves a user using their username"
	)
	@ApiResponses({
					@ApiResponse(
									responseCode = "200",
									description = "User found successfully"
					),
					@ApiResponse(
									responseCode = "404",
									description = "User not found"
					),
					@ApiResponse(
									responseCode = "401",
									description = "Unauthorized - missing or invalid token"
					)
	})
	public ResponseEntity<UserResponseDTO> getByUsername(
					@Parameter(
									description = "Username",
									example = "JaneDoe"
					)
					@RequestParam String username
	) {
		return ResponseEntity.ok(userService.getByUsername(username));
	}
	
}
