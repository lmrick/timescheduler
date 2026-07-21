package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record SchedulerResponseDTO(
				@Schema(
								description = "Scheduler identifier",
								example = "1"
				)
				Long id,
				
				
				@Schema(
								description = "Product or service name",
								example = "Haircut"
				)
				String product,
				
				
				@Schema(
								description = "Worker responsible for the scheduler",
								example = "John"
				)
				String worker,
				
				
				@Schema(
								description = "Scheduled date and time",
								example = "2026-07-21T14:30:00"
				)
				LocalDateTime scheduledTime,
				
				
				@Schema(
								description = "Duration of the appointment in minutes",
								example = "60"
				)
				Integer durationMinutes,
				
				
				@Schema(
								description = "Client name",
								example = "Michael Smith"
				)
				String client,
				
				
				@Schema(
								description = "Client phone number",
								example = "+55 11 99999-9999"
				)
				String clientPhone
) {

}

