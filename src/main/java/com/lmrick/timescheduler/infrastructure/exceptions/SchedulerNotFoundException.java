package com.lmrick.timescheduler.infrastructure.exceptions;

public class SchedulerNotFoundException extends RuntimeException {
	
	public SchedulerNotFoundException(String message) {
		super(message);
	}
	
}
