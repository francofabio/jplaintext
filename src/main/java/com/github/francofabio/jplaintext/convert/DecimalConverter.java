package com.github.francofabio.jplaintext.convert;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.substring;

import java.lang.reflect.ParameterizedType;
import java.text.DecimalFormat;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.francofabio.jplaintext.FieldMapper;
import com.github.francofabio.jplaintext.utils.PlainTextUtils;

public abstract class DecimalConverter<T> implements Converter {

	private Class<T> cls;

	@SuppressWarnings("unchecked")
	public DecimalConverter() {
		this.cls = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private Object newValue(Object v) {
		try {
			return ConstructorUtils.invokeConstructor(this.cls, v);
		} catch (Exception e) {
			throw new ConverterException("Count not create instance of class " + this.cls.getName(), e);
		}
	}

	@Override
	public String asString(Object value, FieldMapper fieldMapper) {
		String format = PlainTextUtils.buildDecimalFormat(fieldMapper.getSize(), fieldMapper.getDecimals());
		DecimalFormat df = new DecimalFormat(defaultIfBlank(fieldMapper.getFormat(), format));
		String strValue = "";
		if (value != null) {
			strValue = df.format(value);
		}
		return StringUtils.leftPad(strValue.replaceAll("[.,]", ""), fieldMapper.getSize(), "0");
	}

	@Override
	public Object asObject(String value, FieldMapper fieldMapper) {
		String format = fieldMapper.getFormat();
		if (isBlank(value)) {
			return newValue(0.00);
		}

		if (isBlank(format) && fieldMapper.getDecimals() == 0) {
			return newValue(value);
		}

		int decimals = 0;
		if (!isBlank(format) && format.indexOf('.') > -1) {
			decimals = getDecimals(format);
		} else {
			decimals = fieldMapper.getDecimals();
		}
		if (decimals > 0) {
			String decimalPart = substring(value, -decimals, value.length());
			String integerPart = substring(value, 0, value.length() - decimals);
			String strNumber = integerPart + "." + decimalPart;
			return newValue(strNumber);
		} else {
			return newValue(value);
		}
	}

	private int getDecimals(String format) {
		String rawDecimals = format.substring(format.lastIndexOf('.') + 1, format.length());
		if (!isBlank(rawDecimals.replaceAll("[0]", ""))) {
			throw new ConverterException("Invalid format '" + format + "'");
		} else {
			return rawDecimals.length();
		}
	}

}
