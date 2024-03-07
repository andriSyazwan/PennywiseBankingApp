package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException{

	public TransactionNotFoundException() {
		super();
		
	}

	public TransactionNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public TransactionNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public TransactionNotFoundException(String message) {
		super(message);
		
	}

	public TransactionNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
