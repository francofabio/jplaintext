package com.github.francofabio.jplaintext.convert;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.francofabio.jplaintext.FieldMapper;
import com.github.francofabio.jplaintext.utils.PlainTextUtils;

public class StringConverter implements Converter {

	@Override
	public String asString(Object value, FieldMapper fieldMapper) {
		String str = (value == null) ? "" : value.toString();
		
		if (!StringUtils.isBlank(fieldMapper.getFormat())) {
			if (StringUtils.repeat('0', fieldMapper.getFormat().length()).equals(fieldMapper.getFormat())) {
				if (StringUtils.isBlank(str)) {
					str = "0";
				}
				if (!StringUtils.isNumeric(str)) {
					throw new ConverterException("Field '" + fieldMapper.getField() + "'. '" + str
							+ "' can not be represented as a number");
				} else {
					return PlainTextUtils.leftPad(str, fieldMapper.getSize(), '0');
				}
			}
		}
		return PlainTextUtils.rightPad(str, fieldMapper.getSize(), ' ');
	}

	@Override
	public Object asObject(String value, FieldMapper fieldMapper) {
		if (!StringUtils.isBlank(fieldMapper.getFormat()) && !StringUtils.isBlank(value)) {
			if (StringUtils.repeat('0', fieldMapper.getFormat().length()).equals(fieldMapper.getFormat())) {
				if (!StringUtils.isNumeric(value)) {
					throw new ConverterException("Field '" + fieldMapper.getField() + "'. '" + value
							+ "' can not be represented as a number");
				} else {
					return value;
				}
			}
		}
		return (value == null) ? null : value.trim();
	}

	@Override
	public boolean supports(Class<?> cls) {
		return ClassUtils.isAssignable(cls, String.class);
	}

}
