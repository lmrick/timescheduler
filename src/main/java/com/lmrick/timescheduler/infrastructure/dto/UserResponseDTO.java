package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserResponseDTO(
				@Schema(
								description = "Username of the user",
								example = "JaneDoe"
				)
				@NotBlank
				String username,
				
				@Schema(
								description = "User role",
								example = "ADMIN"
				)
				@NotBlank String role
) {

}
