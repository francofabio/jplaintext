package com.github.francofabio.jplaintext.convert;

import org.apache.commons.lang3.ClassUtils;

public class FloatConverter extends DecimalConverter<Float> {

	@Override
	public boolean supports(Class<?> cls) {
		return ClassUtils.isAssignable(cls, Float.class);
	}

}
