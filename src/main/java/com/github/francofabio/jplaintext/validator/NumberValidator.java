package com.github.francofabio.jplaintext.validator;

import static org.apache.commons.lang3.ClassUtils.isAssignable;
import org.apache.commons.lang3.StringUtils;

import com.github.francofabio.jplaintext.FieldMapper;

public class NumberValidator implements Validator {

	public void validate(String value, FieldMapper fieldMapper, Object obj) throws ValidationException {
		if (StringUtils.isBlank(value)) {
			if (fieldMapper.isRequired()) {
				throw new ValidationException("Field '" + fieldMapper.getField() + "' required.");
			} else {
				return;
			}
		}
		if (value.charAt(0) == '-') {
			value = value.substring(1);
		}
		if (!StringUtils.isNumeric(value)) {
			throw new ValidationException("Field '" + fieldMapper.getField() + "'. '" + value
					+ "' can not be represented as a number");
		} else if (value.equals(StringUtils.repeat('0', fieldMapper.getSize())) && fieldMapper.isRequired()) {
			throw new ValidationException("Field '" + fieldMapper.getField() + "' required.");
		}
	}

	@Override
	public boolean supports(Class<?> cls) {
		return isAssignable(cls, Byte.class) || isAssignable(cls, Short.class) || isAssignable(cls, Integer.class)
				|| isAssignable(cls, Long.class) || isAssignable(cls, Float.class) || isAssignable(cls, Double.class);
	}

}
