package com.hn.exception;

public class RepeatSignUpException extends SignUpException {
	public RepeatSignUpException() {
		super();
	}

	public RepeatSignUpException(String message) {
		super(message);
	}

	public RepeatSignUpException(String message, Throwable cause) {
		super(message, cause);
	}

}
