package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BankAccountNotFoundException extends RuntimeException {

	public BankAccountNotFoundException() {
		super();

	}

	public BankAccountNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public BankAccountNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public BankAccountNotFoundException(String message) {
		super(message);

	}

	public BankAccountNotFoundException(Throwable cause) {
		super(cause);

	}

}
