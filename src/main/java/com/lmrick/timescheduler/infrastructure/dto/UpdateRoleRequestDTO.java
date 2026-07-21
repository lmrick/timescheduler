package com.lmrick.timescheduler.infrastructure.dto;

import com.lmrick.timescheduler.infrastructure.entity.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequestDTO(
				@NotNull Role role
) {

}
