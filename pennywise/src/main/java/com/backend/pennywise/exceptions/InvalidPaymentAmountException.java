package com.backend.pennywise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPaymentAmountException extends RuntimeException {

	public InvalidPaymentAmountException() {
		super();
	}

	public InvalidPaymentAmountException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidPaymentAmountException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPaymentAmountException(String message) {
		super(message);
	}

	public InvalidPaymentAmountException(Throwable cause) {
		super(cause);
	}
}
