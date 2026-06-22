package com.lmrick.timescheduler.infrastructure.exceptions;

public class RefreshTokenRevokedException extends RuntimeException {
	
	public RefreshTokenRevokedException(String message) {
		super(message);
	}
	
}
