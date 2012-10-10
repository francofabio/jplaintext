package com.github.francofabio.jplaintext;

import org.apache.commons.lang3.ClassUtils;

import com.github.francofabio.jplaintext.convert.Converter;

public class EmployeeConverter implements Converter {

	@Override
	public String asString(Object value, FieldMapper fieldMapper) {
		return null;
	}

	@Override
	public Object asObject(String value, FieldMapper fieldMapper) {
		return null;
	}

	@Override
	public boolean supports(Class<?> cls) {
		return ClassUtils.isAssignable(cls, Employee.class);
	}

}
