package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePhoneRequestDTO(
				@Schema(
								description = "Client phone number",
								example = "+55 11 99999-9999"
				)
				@NotBlank(message = "Client phone is required")
				@Size(
								min = 8,
								max = 20,
								message = "Client phone must be between 8 and 20 characters"
				)
				String clientPhone
) {

}
