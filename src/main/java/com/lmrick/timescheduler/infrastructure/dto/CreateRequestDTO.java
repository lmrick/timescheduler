package com.lmrick.timescheduler.infrastructure.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateRequestDTO(
				@NotNull String product,
				@NotNull String worker,
				@NotNull LocalDateTime scheduledTime,
				@NotNull Integer durationMinutes,
				String client,
				String clientPhone
) {

}
