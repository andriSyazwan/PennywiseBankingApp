package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MerchantNotFoundException extends RuntimeException{

	public MerchantNotFoundException() {
		super();
		
	}

	public MerchantNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public MerchantNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public MerchantNotFoundException(String message) {
		super(message);
		
	}

	public MerchantNotFoundException(Throwable cause) {
		super(cause);
		
	}
	
	

}
