package com.lmrick.timescheduler.infrastructure.exceptions;

public class UserNotFoundException extends RuntimeException {
	
	public UserNotFoundException(String message) {
		super(message);
	}
	
}
