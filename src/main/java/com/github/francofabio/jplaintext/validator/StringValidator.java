package com.github.francofabio.jplaintext.validator;

import static org.apache.commons.lang3.ClassUtils.isAssignable;

import org.apache.commons.lang3.StringUtils;

import com.github.francofabio.jplaintext.FieldMapper;

public class StringValidator implements Validator {

	public void validate(String value, FieldMapper fieldMapper, Object obj) throws ValidationException {
		value = (value == null) ? "" : value;
		if (StringUtils.isBlank(value.trim()) && fieldMapper.isRequired()) {
			throw new ValidationException("Field '" + fieldMapper.getField() + "' required.");
		}
		if (!StringUtils.isBlank(fieldMapper.getFormat())) {
			if (StringUtils.repeat('0', fieldMapper.getFormat().length()).equals(fieldMapper.getFormat())) {
				if (!StringUtils.isNumeric(value)) {
					throw new ValidationException("Field '" + fieldMapper.getField() + "'. '" + value
							+ "' can not be represented as a number");
				}
			}
		}
	}

	@Override
	public boolean supports(Class<?> cls) {
		return isAssignable(cls, String.class);
	}

}
