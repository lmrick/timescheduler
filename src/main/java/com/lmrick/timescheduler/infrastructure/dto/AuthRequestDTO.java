package com.lmrick.timescheduler.infrastructure.dto;

import jakarta.validation.constraints.NotNull;

public record AuthRequestDTO(
				@NotNull String username,
				@NotNull String password
) {

}
