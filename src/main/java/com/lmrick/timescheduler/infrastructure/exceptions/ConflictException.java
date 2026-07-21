package com.lmrick.timescheduler.infrastructure.exceptions;

public class ConflictException extends RuntimeException {
	
	public ConflictException(String message) {
		super(message);
	}
	
}
