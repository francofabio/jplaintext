package com.github.francofabio.jplaintext.convert;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static org.apache.commons.lang3.time.DateUtils.parseDate;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.francofabio.jplaintext.FieldMapper;

public class DateConverter implements Converter {

	public static final String DEFAULT_FORMAT = "yyyyMMdd";

	@Override
	public String asString(Object value, FieldMapper fieldMapper) {
		final String format = defaultIfBlank(fieldMapper.getFormat(), DEFAULT_FORMAT);
		if (value == null) {
			return StringUtils.leftPad("0", format.length(), '0');
		}
		return format((Date) value, format);
	}

	@Override
	public Object asObject(String value, FieldMapper fieldMapper) {
		final String format = defaultIfBlank(fieldMapper.getFormat(), DEFAULT_FORMAT);
		try {
			if (StringUtils.isBlank(value) || StringUtils.repeat('0', format.length()).equals(value)) {
				return null;
			}
			return parseDate(value, format);
		} catch (ParseException e) {
			throw new ConverterException(String.format("Invalid date %s", value), e);
		}
	}

	@Override
	public boolean supports(Class<?> cls) {
		return ClassUtils.isAssignable(cls, Date.class);
	}

}
