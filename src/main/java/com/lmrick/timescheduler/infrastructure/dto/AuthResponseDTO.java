package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthResponseDTO(
				@Schema(description = "JWT access token")
				@NotBlank String accessToken,
				
				@Schema(description = "JWT refresh token")
				@NotBlank String refreshToken,
				
				@Schema(description = "Authenticated user data")
				@NotBlank UserResponseDTO user
) {

}
