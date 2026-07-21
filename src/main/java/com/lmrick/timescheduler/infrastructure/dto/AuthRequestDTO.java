package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
				@Schema(
								description = "Username used for authentication",
								example = "john.doe"
				)
				@NotBlank String username,
				
				@Schema(
								description = "User password",
								example = "********",
								format = "password"
				)
				@NotBlank String password
) {

}
