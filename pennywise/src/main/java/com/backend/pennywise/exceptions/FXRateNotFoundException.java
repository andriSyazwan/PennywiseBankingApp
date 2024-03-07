package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FXRateNotFoundException extends RuntimeException{

	public FXRateNotFoundException() {
		super();
	}

	public FXRateNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FXRateNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public FXRateNotFoundException(String message) {
		super(message);
	}

	public FXRateNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
