package com.github.francofabio.jplaintext.validator;

import static org.apache.commons.lang3.ClassUtils.isAssignable;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.time.DateUtils.parseDate;

import java.text.ParseException;
import java.util.Date;

import com.github.francofabio.jplaintext.FieldMapper;
import com.github.francofabio.jplaintext.convert.DateConverter;

public class DateValidator implements Validator {

	@Override
	public void validate(String value, FieldMapper fieldMapper, Object obj) throws ValidationException {
		if (fieldMapper.isRequired()) {
			if (isBlank(value) || value.equals(repeat('0', fieldMapper.getSize()))) {
				throw new ValidationException("Field '" + fieldMapper.getField() + "' required.");
			}
		}
		if (isBlank(value)) {
			return;
		}
		
		try {
			parseDate(value, defaultIfBlank(fieldMapper.getFormat(), DateConverter.DEFAULT_FORMAT));
		} catch (ParseException e) {
			throw new ValidationException("Invalid date " + value + ".");
		}
	}

	@Override
	public boolean supports(Class<?> cls) {
		return isAssignable(cls, Date.class);
	}

}