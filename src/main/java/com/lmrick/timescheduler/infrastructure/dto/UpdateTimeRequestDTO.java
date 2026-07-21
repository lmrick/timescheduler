package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UpdateTimeRequestDTO(
				@Schema(
								description = "New scheduled date and time",
								example = "2026-07-21T14:30:00"
				)
				@NotNull(message = "Scheduled time is required")
				LocalDateTime scheduledTime
) {

}
