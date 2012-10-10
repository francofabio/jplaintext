package com.github.francofabio.jplaintext;

@SuppressWarnings("serial")
public class InvalidExpression extends RuntimeException {

	public InvalidExpression() {
	}

	public InvalidExpression(String message) {
		super(message);
	}

	public InvalidExpression(Throwable cause) {
		super(cause);
	}

	public InvalidExpression(String message, Throwable cause) {
		super(message, cause);
	}

}
