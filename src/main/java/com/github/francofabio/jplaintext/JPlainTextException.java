package com.github.francofabio.jplaintext;

@SuppressWarnings("serial")
public class JPlainTextException extends RuntimeException {

	public JPlainTextException() {
		super();
	}

	public JPlainTextException(String message, Throwable cause) {
		super(message, cause);
	}

	public JPlainTextException(String message) {
		super(message);
	}

	public JPlainTextException(Throwable cause) {
		super(cause);
	}

}
