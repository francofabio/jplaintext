package com.github.francofabio.jplaintext.convert;

import org.apache.commons.lang3.ClassUtils;

public class DoubleConverter extends DecimalConverter<Double> {

	@Override
	public boolean supports(Class<?> cls) {
		return ClassUtils.isAssignable(cls, Double.class);
	}

}
