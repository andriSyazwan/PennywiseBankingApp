package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class IncorrectPasswordException extends RuntimeException {

	public IncorrectPasswordException() {
		super();
		
	}

	public IncorrectPasswordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public IncorrectPasswordException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public IncorrectPasswordException(String message) {
		super(message);
		
	}

	public IncorrectPasswordException(Throwable cause) {
		super(cause);
		
	}
	
	

}
