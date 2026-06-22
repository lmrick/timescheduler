package com.lmrick.timescheduler.infrastructure.dto;

import jakarta.validation.constraints.NotNull;

public record UserResponseDTO(
				@NotNull String username,
				@NotNull String role
) {

}
