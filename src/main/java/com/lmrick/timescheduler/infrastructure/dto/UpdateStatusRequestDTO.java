package com.lmrick.timescheduler.infrastructure.dto;

import com.lmrick.timescheduler.infrastructure.entity.SchedulerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequestDTO(
				@Schema(
								description = "New scheduler status",
								example = "CONFIRMED"
				)
				@NotNull SchedulerStatus status
) {

}
