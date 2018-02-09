package com.control.exception;

public class MyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MyException() {
	}

	public MyException(String message) {
		super(message);
	}

	public MyException(Throwable cause) {
		super(cause);
	}

	public MyException(String message, Throwable cause) {
		super(message, cause);
	}

}
