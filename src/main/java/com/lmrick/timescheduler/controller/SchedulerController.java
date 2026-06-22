package com.lmrick.timescheduler.controller;

import com.lmrick.timescheduler.infrastructure.dto.CreateRequestDTO;
import com.lmrick.timescheduler.infrastructure.dto.SchedulerResponseDTO;
import com.lmrick.timescheduler.infrastructure.dto.UpdateRequestDTO;
import com.lmrick.timescheduler.services.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/scheduler")
@Tag(name = "Scheduler", description = "Operations related to scheduling management")
@SecurityRequirement(name = "bearerAuth")
public class SchedulerController {
	
	private final SchedulerService schedulerService;
	
	@Autowired
	public SchedulerController(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
	
	@PostMapping
	@Operation(
					summary = "Create a new schedule",
					description = "Creates a new scheduler entry in the system"
	)
	public ResponseEntity<SchedulerResponseDTO> saveScheduler(
					@RequestBody @Valid CreateRequestDTO request
	) {
		return ResponseEntity
						.status(201)
						.body(schedulerService.saveScheduler(request));
	}
	
	@GetMapping
	@Operation(
					summary = "Get schedules by day",
					description = "Returns all schedules for a specific date"
	)
	public ResponseEntity<List<SchedulerResponseDTO>> getSchedulerByDay(
					@Parameter(description = "Date in format YYYY-MM-DD", example = "2026-06-22")
					@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
					@RequestParam LocalDate date
	) {
		return ResponseEntity.ok(
						schedulerService.findByDay(date)
		);
	}
	
	@PutMapping("/{id}")
	@Operation(
					summary = "Update a schedule",
					description = "Updates only the scheduled time of an existing schedule"
	)
	public ResponseEntity<SchedulerResponseDTO> updateScheduler(
					@Parameter(description = "Schedule ID", example = "1")
					@PathVariable Long id,
					@RequestBody UpdateRequestDTO request
	) {
		return ResponseEntity.ok(
						schedulerService.updateScheduler(id, request)
		);
	}
	
	@DeleteMapping("/{id}")
	@Operation(
					summary = "Delete a schedule",
					description = "Removes a schedule by ID"
	)
	public ResponseEntity<Void> deleteScheduler(
					@Parameter(description = "Schedule ID", example = "1")
					@PathVariable Long id
	) {
		schedulerService.deleteScheduler(id);
		return ResponseEntity.noContent().build();
	}
	
}
