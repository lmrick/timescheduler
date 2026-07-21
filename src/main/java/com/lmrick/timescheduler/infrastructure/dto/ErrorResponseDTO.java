package com.lmrick.timescheduler.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard API error response")
public record ErrorResponseDTO(
				
				@Schema(example = "2026-07-21T18:00:00")
				LocalDateTime timestamp,
				
				@Schema(example = "404")
				Integer status,
				
				@Schema(example = "Schedule not found")
				String message,
				
				@Schema(example = "/scheduler/10")
				String path

) {

}
