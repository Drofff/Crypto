package com.drofff.crypto.exception;

public class AESException extends RuntimeException {
	public AESException() {
		super();
	}

	public AESException(String message) {
		super(message);
	}

	public AESException(String message, Throwable cause) {
		super(message, cause);
	}

	public AESException(Throwable cause) {
		super(cause);
	}

	protected AESException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
