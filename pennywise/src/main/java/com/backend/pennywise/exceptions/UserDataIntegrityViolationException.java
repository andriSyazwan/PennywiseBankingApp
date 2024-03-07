package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

public class UserDataIntegrityViolationException extends RuntimeException{

	public UserDataIntegrityViolationException() {
		super();
		 
	}

	public UserDataIntegrityViolationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		 
	}

	public UserDataIntegrityViolationException(String message, Throwable cause) {
		super(message, cause);
		 
	}

	public UserDataIntegrityViolationException(String message) {
		super(message);
		 
	}

	public UserDataIntegrityViolationException(Throwable cause) {
		super(cause);
		 
	}
	
	
}
