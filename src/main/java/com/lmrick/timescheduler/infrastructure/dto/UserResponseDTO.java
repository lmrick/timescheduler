package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDTO(
				@Schema(
								description = "Username of the user",
								example = "john.doe"
				)
				String username,
				
				
				@Schema(
								description = "User access role",
								example = "ADMIN"
				)
				String role
) {

}
