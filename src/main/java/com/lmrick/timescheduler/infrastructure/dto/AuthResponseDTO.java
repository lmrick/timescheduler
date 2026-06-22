package com.lmrick.timescheduler.infrastructure.dto;

import jakarta.validation.constraints.NotNull;

public record AuthResponseDTO(
				@NotNull String accessToken,
				@NotNull String refreshToken,
				@NotNull UserResponseDTO user
) {

}
