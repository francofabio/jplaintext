package com.github.francofabio.jplaintext.convert;

import org.apache.commons.lang3.ClassUtils;

import com.github.francofabio.jplaintext.FieldMapper;

public class CharacterConverter implements Converter {

	@Override
	public String asString(Object value, FieldMapper fieldMapper) {
		String str = (value == null) ? "" : value.toString();
		return str;
	}

	@Override
	public Object asObject(String value, FieldMapper fieldMapper) {
		return (value == null) ? null : new Character(value.trim().charAt(0));
	}

	@Override
	public boolean supports(Class<?> cls) {
		return ClassUtils.isAssignable(cls, Character.class);
	}

}
