package com.lmrick.timescheduler.infrastructure.exceptions;

import com.lmrick.timescheduler.infrastructure.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(
					ResourceNotFoundException ex,
					HttpServletRequest request
	) {
		
		return ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.body(new ErrorResponseDTO(
										LocalDateTime.now(),
										404,
										ex.getMessage(),
										request.getRequestURI()
						));
	}
	
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponseDTO> handleConflict(
					ConflictException ex,
					HttpServletRequest request
	) {
		
		return ResponseEntity
						.status(HttpStatus.CONFLICT)
						.body(new ErrorResponseDTO(
										LocalDateTime.now(),
										409,
										ex.getMessage(),
										request.getRequestURI()
						));
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> handleValidation(
					MethodArgumentNotValidException ex,
					HttpServletRequest request
	) {
		
		String message = ex.getBindingResult()
						.getFieldErrors()
						.stream()
						.map(error -> error.getField() + ": " + error.getDefaultMessage())
						.collect(Collectors.joining(", "));
		
		
		return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponseDTO(
										LocalDateTime.now(),
										400,
										message,
										request.getRequestURI()
						));
	}
	
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(
					MethodArgumentTypeMismatchException ex,
					HttpServletRequest request
	) {
		
		String message = "Invalid value for parameter: " + ex.getName();
		
		
		return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponseDTO(
										LocalDateTime.now(),
										400,
										message,
										request.getRequestURI()
						));
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGenericException(
					Exception ex,
					HttpServletRequest request
	) {
		
		log.error("Unexpected error", ex);
		
		return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ErrorResponseDTO(
										LocalDateTime.now(),
										500,
										"Internal server error",
										request.getRequestURI()
						));
	}
	
}
