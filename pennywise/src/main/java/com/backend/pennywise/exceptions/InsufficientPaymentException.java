package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientPaymentException extends RuntimeException{

	public InsufficientPaymentException() {
		super();
		
	}

	public InsufficientPaymentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public InsufficientPaymentException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public InsufficientPaymentException(String message) {
		super(message);
		
	}

	public InsufficientPaymentException(Throwable cause) {
		super(cause);
		
	}

}
