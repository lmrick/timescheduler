package com.lmrick.timescheduler.infrastructure.dto;

import java.time.LocalDateTime;

public record SchedulerResponseDTO(
				Long id,
				String product,
				String worker,
				LocalDateTime scheduledTime,
				Integer durationMinutes,
				String client,
				String clientPhone
) {

}
