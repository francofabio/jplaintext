package com.github.francofabio.jplaintext.validator;

import com.github.francofabio.jplaintext.FieldMapper;

public interface Validator {

	void validate(String value, FieldMapper fieldMapper, Object instance) throws ValidationException;
	
	boolean supports(Class<?> cls);

}
