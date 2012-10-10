package com.github.francofabio.jplaintext.validator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class ValidationsException extends RuntimeException {

	private List<ValidationException> exceptions;
	private int line;

	public ValidationsException() {
		exceptions = new LinkedList<ValidationException>();
	}

	public ValidationsException(int line) {
		this();
		setLine(line);
	}

	public List<ValidationException> getExceptions() {
		return Collections.unmodifiableList(exceptions);
	}

	public void addException(ValidationException e) {
		exceptions.add(e);
	}

	public int count() {
		return exceptions.size();
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ValidationException e : exceptions) {
			sb.append(e.toString()).append("\n");
		}
		return sb.toString();
	}

}
