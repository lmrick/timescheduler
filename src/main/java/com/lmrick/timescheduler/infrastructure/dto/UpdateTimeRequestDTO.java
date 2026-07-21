package com.lmrick.timescheduler.infrastructure.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateTimeRequestDTO(
				@NotNull LocalDateTime scheduledTime
) {

}
