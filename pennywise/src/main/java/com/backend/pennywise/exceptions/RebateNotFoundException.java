package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RebateNotFoundException extends RuntimeException{

	public RebateNotFoundException() {
		super();
		
	}

	public RebateNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public RebateNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public RebateNotFoundException(String message) {
		super(message);
		
	}

	public RebateNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
