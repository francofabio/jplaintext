package com.github.francofabio.jplaintext.convert;

import static org.apache.commons.lang3.StringUtils.repeat;

import java.text.DecimalFormat;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.francofabio.jplaintext.FieldMapper;

public class IntegerConverter implements Converter {

	@Override
	public String asString(Object value, FieldMapper fieldMapper) {
		String format = repeat('0', fieldMapper.getSize());
		DecimalFormat df = new DecimalFormat(StringUtils.defaultIfBlank(fieldMapper.getFormat(), format));
		return df.format(value);
	}

	@Override
	public Object asObject(String value, FieldMapper fieldMapper) {
		value = (value == null) ? "" : value.trim();
		if (value.isEmpty()) {
			value = "0";
		}
		return new Integer(value);
	}

	@Override
	public boolean supports(Class<?> cls) {
		return ClassUtils.isAssignable(cls, Integer.class);
	}
	
}
