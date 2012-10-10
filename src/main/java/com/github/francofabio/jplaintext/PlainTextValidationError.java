package com.github.francofabio.jplaintext;

public class PlainTextValidationError {

	private String message;
	
	public PlainTextValidationError(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return message;
	}

}
