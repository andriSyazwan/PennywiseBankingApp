package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PointNotFoundException extends RuntimeException{

	public PointNotFoundException() {
		super();
		
	}

	public PointNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public PointNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public PointNotFoundException(String message) {
		super(message);
		
	}

	public PointNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
