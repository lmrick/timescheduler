package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateRequestDTO(
				@Schema(
								description = "Product or service name",
								example = "Haircut"
				)
				@NotBlank String product,
				
				@Schema(
								description = "Worker responsible for the appointment",
								example = "John"
				)
				@NotBlank String worker,
				
				@Schema(
								description = "Scheduled date and time",
								example = "2026-07-21T14:30:00"
				)
				@NotNull LocalDateTime scheduledTime,
				
				@Schema(
								description = "Duration of the appointment in minutes",
								example = "60"
				)
				@NotNull Integer durationMinutes,
				
				@Schema(
								description = "Client name",
								example = "Michael Smith"
				)
				@NotBlank String client,
				
				@Schema(
								description = "Client phone number",
								example = "+55 11 99999-9999"
				)
				@Size(
								min = 8,
								max = 20,
								message = "Client phone must be between 8 and 20 characters"
				)
				@NotBlank String clientPhone
) {

}
