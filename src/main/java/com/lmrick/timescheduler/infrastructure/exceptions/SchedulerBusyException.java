package com.lmrick.timescheduler.infrastructure.exceptions;

public class SchedulerBusyException extends RuntimeException {
	
	public SchedulerBusyException(String message) {
		super(message);
	}
	
}
