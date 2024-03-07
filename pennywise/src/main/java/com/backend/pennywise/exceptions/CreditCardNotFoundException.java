package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CreditCardNotFoundException extends RuntimeException{

	public CreditCardNotFoundException() {
		super();
		
	}

	public CreditCardNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public CreditCardNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public CreditCardNotFoundException(String message) {
		super(message);
		
	}

	public CreditCardNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
