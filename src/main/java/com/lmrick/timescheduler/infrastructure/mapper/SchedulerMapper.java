package com.lmrick.timescheduler.infrastructure.mapper;

import com.lmrick.timescheduler.infrastructure.dto.SchedulerResponseDTO;
import com.lmrick.timescheduler.infrastructure.entity.SchedulerEntity;
import org.springframework.stereotype.Component;

@Component
public class SchedulerMapper {
	
	public SchedulerResponseDTO toDTO(SchedulerEntity entity) {
		return new SchedulerResponseDTO(
						entity.getId(),
						entity.getProduct(),
						entity.getWorker(),
						entity.getScheduledTime(),
						entity.getDurationMinutes(),
						entity.getClient(),
						entity.getClientPhone()
		);
	}
	
}
