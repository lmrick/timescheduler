package com.lmrick.timescheduler.infrastructure.dto;

import com.lmrick.timescheduler.infrastructure.entity.Role;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(
				@NotNull String username,
				@NotNull String password,
				@NotNull Role role
) {

}
