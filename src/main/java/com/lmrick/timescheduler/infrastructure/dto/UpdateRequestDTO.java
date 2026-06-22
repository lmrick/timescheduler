package com.lmrick.timescheduler.infrastructure.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateRequestDTO(
				@NotNull LocalDateTime scheduledTime
) {

}
