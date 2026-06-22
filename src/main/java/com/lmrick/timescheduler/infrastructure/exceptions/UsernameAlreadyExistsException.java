package com.lmrick.timescheduler.infrastructure.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
	
	public UsernameAlreadyExistsException(String message) {
		super(message);
	}
	
}
