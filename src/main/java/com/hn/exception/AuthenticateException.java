package com.hn.exception;

public class AuthenticateException extends RuntimeException {
	public AuthenticateException() {
		super();
	}

	public AuthenticateException(String message) {
		super(message);
	}

	public AuthenticateException(String message, Throwable cause) {
		super(message, cause);
	}
}
