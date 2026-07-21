package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(
				@Schema(
								description = "JWT refresh token used to generate a new access token",
								example = "********"
				)
				@NotBlank String refreshToken
) {

}
