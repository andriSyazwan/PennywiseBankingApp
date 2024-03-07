package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InstalmentTransactionNotFoundException extends RuntimeException{

	public InstalmentTransactionNotFoundException() {
		super();

	}

	public InstalmentTransactionNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public InstalmentTransactionNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public InstalmentTransactionNotFoundException(String message) {
		super(message);

	}

	public InstalmentTransactionNotFoundException(Throwable cause) {
		super(cause);

	}

}
