package com.github.francofabio.jplaintext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public class PlainTextValidationsErrors extends RuntimeException {

	private List<PlainTextValidationError> errors;

	public PlainTextValidationsErrors() {
		this.errors = new ArrayList<PlainTextValidationError>();
	}

	public void addError(PlainTextValidationError exception) {
		this.errors.add(exception);
	}
	
	public void addErrors(List<PlainTextValidationError> errors) {
		if (errors.size() > 0) {
			this.errors.addAll(errors);
		}
	}

	public List<PlainTextValidationError> getErrors() {
		return Collections.unmodifiableList(errors);
	}
	
	@Override
	public String toString() {
		return errors.toString();
	}
	
}
