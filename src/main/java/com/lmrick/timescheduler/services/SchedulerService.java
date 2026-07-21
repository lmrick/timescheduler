package com.lmrick.timescheduler.services;

import com.lmrick.timescheduler.infrastructure.dto.*;
import com.lmrick.timescheduler.infrastructure.entity.SchedulerEntity;
import com.lmrick.timescheduler.infrastructure.exceptions.ConflictException;
import com.lmrick.timescheduler.infrastructure.exceptions.ResourceNotFoundException;
import com.lmrick.timescheduler.infrastructure.mapper.SchedulerMapper;
import com.lmrick.timescheduler.infrastructure.repository.SchedulerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SchedulerService {
	private final SchedulerRepository schedulerRepository;
	private final SchedulerMapper schedulerMapper;
	
	@Autowired
	public SchedulerService(SchedulerRepository schedulerRepository, SchedulerMapper schedulerMapper) {
		this.schedulerRepository = schedulerRepository;
		this.schedulerMapper = schedulerMapper;
	}
	
	public SchedulerResponseDTO saveScheduler(CreateSchedulerRequestDTO dto) {
		LocalDateTime start = dto.scheduledTime();
		LocalDateTime end = start.plusMinutes(dto.durationMinutes());
		
		List<SchedulerEntity> conflicts =
						schedulerRepository.findByWorkerAndScheduledTimeBetween(
										dto.worker(),
										start,
										end
						);
		
		if (!conflicts.isEmpty()) {
			throw new ConflictException("Scheduler time already busy");
		}
		
		SchedulerEntity entity = new SchedulerEntity();
		entity.setProduct(dto.product());
		entity.setWorker(dto.worker());
		entity.setScheduledTime(dto.scheduledTime());
		entity.setDurationMinutes(dto.durationMinutes());
		entity.setClient(dto.client());
		entity.setClientPhone(dto.clientPhone());
		
		return schedulerMapper.toDTO(schedulerRepository.save(entity));
	}
	
	public List<SchedulerResponseDTO> findByDay(LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.atTime(23, 59, 59);
		
		return schedulerRepository.findByScheduledTimeBetween(start, end)
						.stream()
						.map(schedulerMapper::toDTO)
						.toList();
	}
	
	public SchedulerResponseDTO updateScheduler(Long id, UpdateTimeRequestDTO request) {
		SchedulerEntity scheduled = schedulerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scheduler not found"));
		
		LocalDateTime newStart = request.scheduledTime();
		LocalDateTime newEnd = newStart.plusMinutes(scheduled.getDurationMinutes());
		
		List<SchedulerEntity> conflicts = schedulerRepository.findByWorker(scheduled.getWorker());
		
		if (hasConflict(newStart, newEnd, conflicts, id)) {
			throw new ConflictException("Scheduler time already busy");
		}
		
		scheduled.setScheduledTime(newStart);
		
		SchedulerEntity saved = schedulerRepository.save(scheduled);
		return schedulerMapper.toDTO(saved);
	}
	
	public SchedulerResponseDTO updateStatus(Long id, UpdateStatusRequestDTO request) {
		SchedulerEntity scheduler = schedulerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scheduler not found"));
		scheduler.setStatus(request.status());
		schedulerRepository.save(scheduler);
		
		return schedulerMapper.toDTO(scheduler);
	}
	
	public SchedulerResponseDTO updatePhone(Long id, UpdatePhoneRequestDTO request) {
		SchedulerEntity scheduler = schedulerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scheduler not found"));
		scheduler.setClientPhone(request.clientPhone());
		schedulerRepository.save(scheduler);
		
		return schedulerMapper.toDTO(scheduler);
	}
	
	@Transactional
	public void deleteScheduler(Long id) {
		SchedulerEntity scheduler = schedulerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scheduler not found"));
		schedulerRepository.delete(scheduler);
	}
	
	private boolean hasConflict(
					LocalDateTime start,
					LocalDateTime end,
					List<SchedulerEntity> schedules,
					Long ignoreId
	) {
		for (SchedulerEntity s : schedules) {
			
			if (s.getId().equals(ignoreId)) {
				continue;
			}
			
			LocalDateTime existingStart = s.getScheduledTime();
			LocalDateTime existingEnd = existingStart.plusMinutes(s.getDurationMinutes());
			
			if (existingStart.isBefore(end) && existingEnd.isAfter(start)) {
				return true;
			}
		}
		return false;
	}
	
}