package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CreditCardBillNotFoundException extends RuntimeException{

	public CreditCardBillNotFoundException() {
		super();
		
	}

	public CreditCardBillNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public CreditCardBillNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public CreditCardBillNotFoundException(String message) {
		super(message);
		
	}

	public CreditCardBillNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
