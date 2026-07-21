package com.lmrick.timescheduler.infrastructure.dto;

import com.lmrick.timescheduler.infrastructure.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
				@Schema(
								description = "Unique username for the user",
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
				@NotBlank String password,
				
				@Schema(
								description = "User access role",
								example = "ADMIN"
				)
				@NotNull Role role
) {

}
