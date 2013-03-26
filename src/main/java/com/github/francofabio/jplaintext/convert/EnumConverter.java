package com.github.francofabio.jplaintext.convert;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.francofabio.jplaintext.FieldMapper;

public class EnumConverter implements Converter {
	
	@Override
	public String asString(Object value, FieldMapper fieldMapper) {
		String property = fieldMapper.getArguments().get("property");
		String strValue = "";
		if (value != null) {
			if (property != null) {
				try {
					strValue = (String) PropertyUtils.getProperty(value, property);
				} catch (Exception e) {
					throw new ConverterException("Error getting value for property '" + property + "'", e);
				}
			} else {
				Enum<?> e = (Enum<?>) value;
				strValue = e.name();
			}
		}
		if (!StringUtils.isBlank(fieldMapper.getFormat())) {
			if (StringUtils.repeat('0', fieldMapper.getFormat().length()).equals(fieldMapper.getFormat())) {
				return StringUtils.leftPad(strValue, fieldMapper.getSize(), '0');
			}
		}
		return StringUtils.rightPad(strValue, fieldMapper.getSize(), ' ');
	}

	@Override
	public Object asObject(String value, FieldMapper fieldMapper) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		value = value.trim();
		String property = fieldMapper.getArguments().get("property");
		Class<?> type = fieldMapper.getFieldType();
		if (property != null) {
			Enum<?>[] enums;
			try {
				enums = (Enum<?>[]) MethodUtils.invokeStaticMethod(type, "values", new Object[] {});
			} catch (Exception e) {
				throw new ConverterException("Could not get available values from enum " + type.getName(), e);
			}
			for (Enum<?> e : enums) {
				Object propValue;
				try {
					propValue = PropertyUtils.getProperty(e, property);
				} catch (Exception e1) {
					throw new ConverterException("Could not get value of property '" + property + "' from enum "
							+ type.getName(), e1);
				}
				if (propValue.equals(value)) {
					return e;
				}
			}
			
			return null;
		} else {
			try {
				return MethodUtils.invokeStaticMethod(type, "valueOf", new Object[] { value });
			} catch (Exception e) {
				throw new ConverterException("Could not invoke method valueOf from enum " + type.getName(), e);
			}
		}
	}

	@Override
	public boolean supports(Class<?> cls) {
		return Enum.class.isAssignableFrom(cls);
	}

}
