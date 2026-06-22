package com.lmrick.timescheduler.infrastructure.exceptions;

public class InvalidTokenException extends RuntimeException {
	
	public InvalidTokenException(String message) {
		super(message);
	}
	
}