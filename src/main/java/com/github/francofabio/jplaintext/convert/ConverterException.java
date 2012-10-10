package com.github.francofabio.jplaintext.convert;

@SuppressWarnings("serial")
public class ConverterException extends RuntimeException {

	public ConverterException() {
		super();
	}

	public ConverterException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ConverterException(String message) {
		super(message);
	}

	public ConverterException(Throwable throwable) {
		super(throwable);
	}

}
