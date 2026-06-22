package com.lmrick.timescheduler.infrastructure.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshRequestDTO(
				@NotNull String refreshToken
) {

}
