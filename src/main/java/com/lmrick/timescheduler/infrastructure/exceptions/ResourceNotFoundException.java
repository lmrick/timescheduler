package com.lmrick.timescheduler.infrastructure.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
}
