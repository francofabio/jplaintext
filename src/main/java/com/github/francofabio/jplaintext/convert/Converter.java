package com.github.francofabio.jplaintext.convert;

import com.github.francofabio.jplaintext.FieldMapper;

public interface Converter {

	String asString(Object value, FieldMapper fieldMapper);

	Object asObject(String value, FieldMapper fieldMapper);

	boolean supports(Class<?> cls);
	
}
