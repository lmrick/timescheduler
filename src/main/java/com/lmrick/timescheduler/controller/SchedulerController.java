package com.lmrick.timescheduler.controller;

import com.lmrick.timescheduler.infrastructure.dto.*;
import com.lmrick.timescheduler.services.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	@ApiResponses({
					@ApiResponse(
									responseCode = "201",
									description = "Schedule successfully created",
									content = @Content(
													schema = @Schema(implementation = SchedulerResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "400",
									description = "Invalid request data",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "409",
									description = "Schedule conflict",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "401",
									description = "Unauthorized",
									content = @Content
					)
	})
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
	@ApiResponses({
					@ApiResponse(
									responseCode = "200",
									description = "Schedules successfully retrieved"
					),
					@ApiResponse(
									responseCode = "400",
									description = "Invalid date format",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "401",
									description = "Unauthorized",
									content = @Content
					)
	})
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
	@ApiResponses({
					@ApiResponse(
									responseCode = "200",
									description = "Schedule successfully updated",
									content = @Content(
													schema = @Schema(implementation = SchedulerResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "404",
									description = "Schedule not found",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "409",
									description = "Schedule conflict",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "401",
									description = "Unauthorized",
									content = @Content
					)
	})
	public ResponseEntity<SchedulerResponseDTO> updateScheduler(
					@Parameter(
									description = "Schedule ID",
									example = "1"
					)
					@PathVariable Long id,
					@RequestBody @Valid UpdateTimeRequestDTO request
	) {
		return ResponseEntity.ok(
						schedulerService.updateScheduler(id, request)
		);
	}
	
	@PatchMapping("/{id}/status")
	@Operation(
					summary = "Update scheduler status",
					description = "Updates the status of an existing schedule"
	)
	@ApiResponses({
					@ApiResponse(
									responseCode = "200",
									description = "Status successfully updated",
									content = @Content(
													schema = @Schema(implementation = SchedulerResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "400",
									description = "Invalid status",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "404",
									description = "Schedule not found",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					)
	})
	public ResponseEntity<SchedulerResponseDTO> updateStatus(
					@Parameter(description = "Schedule ID", example = "1")
					@PathVariable Long id,
					@RequestBody @Valid UpdateStatusRequestDTO request
	) {
		return ResponseEntity.ok(
						schedulerService.updateStatus(id, request)
		);
	}
	
	@PatchMapping("/{id}/phone")
	@Operation(
					summary = "Update client phone",
					description = "Updates the client's phone number for an existing schedule"
	)
	@ApiResponses({
					@ApiResponse(
									responseCode = "200",
									description = "Phone successfully updated",
									content = @Content(
													schema = @Schema(implementation = SchedulerResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "400",
									description = "Invalid phone number",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "404",
									description = "Schedule not found",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					)
	})
	public ResponseEntity<SchedulerResponseDTO> updatePhone(
					@Parameter(description = "Schedule ID", example = "1")
					@PathVariable Long id,
					@RequestBody @Valid UpdatePhoneRequestDTO request
	) {
		return ResponseEntity.ok(
						schedulerService.updatePhone(id, request)
		);
	}
	
	@DeleteMapping("/{id}")
	@Operation(
					summary = "Delete a schedule",
					description = "Removes a schedule by ID"
	)
	@ApiResponses({
					@ApiResponse(
									responseCode = "204",
									description = "Schedule successfully deleted"
					),
					@ApiResponse(
									responseCode = "404",
									description = "Schedule not found",
									content = @Content(
													schema = @Schema(implementation = ErrorResponseDTO.class)
									)
					),
					@ApiResponse(
									responseCode = "401",
									description = "Unauthorized",
									content = @Content
					)
	})
	public ResponseEntity<Void> deleteScheduler(
					@Parameter(description = "Schedule ID", example = "1")
					@PathVariable Long id
	) {
		schedulerService.deleteScheduler(id);
		return ResponseEntity.noContent().build();
	}
	
}
