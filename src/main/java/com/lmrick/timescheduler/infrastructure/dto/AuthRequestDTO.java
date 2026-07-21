package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequestDTO(
				@Schema(
								description = "Username used for authentication",
								example = "john.doe"
				)
				@Size(
								min = 3,
								max = 50,
								message = "Username must be between 3 and 100 characters"
				)
				@NotBlank String username,
				
				@Schema(
								description = "User password",
								example = "********",
								format = "password"
				)
				@Size(
								min = 8,
								max = 100,
								message = "Password must be between 8 and 100 characters"
				)
				@NotBlank String password
) {

}
