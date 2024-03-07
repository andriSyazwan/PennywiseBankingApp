package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CreditLimitReachedException extends RuntimeException {

	public CreditLimitReachedException() {
		super();
		
	}

	public CreditLimitReachedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public CreditLimitReachedException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public CreditLimitReachedException(String message) {
		super(message);
		
	}

	public CreditLimitReachedException(Throwable cause) {
		super(cause);
		
	}
	
	
}
