package com.github.francofabio.jplaintext;

import static com.github.francofabio.jplaintext.JPlainTextMapper.mapperDataKey;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.francofabio.jplaintext.convert.Converter;
import com.github.francofabio.jplaintext.utils.ReflectionUtils;
import com.github.francofabio.jplaintext.validator.ValidationException;
import com.github.francofabio.jplaintext.validator.ValidationsException;
import com.github.francofabio.jplaintext.validator.Validator;

public class JPlainTextParse<T> {
	private final Class<T> cls;
	private JPlainTextMapper mapper;

	public JPlainTextParse(Class<T> cls) {
		this.cls = cls;
		this.mapper = new JPlainTextMapper(cls);
	}
	
	private FieldMapper getFieldMapper(NamedTreeNode node) {
		return node.getDataAs(mapperDataKey, FieldMapper.class);
	}

	protected String getValue(String s, int startPosition, int size, String property) {
		int start = startPosition - 1;
		int end = start + size;
		if (end > s.length()) {
			throw new IllegalArgumentException(String.format("Final position of the %s field exceeds the size of the line. End position %d line length %d",
					property,
					end,
					s.length()));
		} else {
			return s.substring(start, end);
		}
	}

	protected void parseValue(T o, String text, NamedTreeNode node, FieldMapper fieldMapper) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, InstantiationException {
		if (fieldMapper.isEmbedded()) {
			// Initialize property
			String property = node.getPath();
			Object value = PropertyUtils.getProperty(o, property);
			if (value == null) {
				value = PropertyUtils.getPropertyType(o, property).newInstance();
				PropertyUtils.setProperty(o, property, value);
			}
			
			for (NamedTreeNode child : node.getChilds()) {
				parseValue(o, text, child, getFieldMapper(child));
			}
		} else {
			String property = node.getPath();
			int startPosition = fieldMapper.getStart();
			int size = fieldMapper.getSize();
			String rawValue = getValue(text, startPosition, size, property);
			Object value = null;
			String fieldName = fieldMapper.getField();
			
			try {
				Validator validator = (Validator) JPlainTextValidatorFactory.getValidator(fieldMapper.getFieldType());
				validator.validate(rawValue, fieldMapper, o);

				Converter converter = (Converter) JPlainTextConverterFactory.getConverter(fieldMapper.getFieldType());
				value = converter.asObject(rawValue, fieldMapper);
				
				if (PropertyUtils.isWriteable(o, property)) {
					PropertyUtils.setProperty(o, property, value);
				} else {
					ReflectionUtils.setFieldValue(o, property, value);
				}
			} catch (ValidationException e) {
				throw e;
			} catch (Exception ex) {
				throw new JPlainTextException("Error set the value '" + value.toString() + "' on " + fieldName
						+ " property: " + ex.getMessage(), ex);
			}
		}
	}

	public T parseText(String text) throws ValidationsException {
		return parseText(text, 0);
	}

	public T parseText(String text, int lineIndex) throws ValidationsException {
		T bean;
		try {
			bean = cls.newInstance();
		} catch (Exception e) {
			throw new ValidationException("Error create instance for class " + cls.getName());
		}

		ValidationsException exceptions = new ValidationsException(lineIndex);

		if (StringUtils.isBlank(text)) {
			exceptions.addException(new ValidationException("Empty line"));
			throw exceptions;
		}
		if (mapper.getPlainTextRecord().size() > 0 && text.length() != mapper.getPlainTextRecord().size()) {
			exceptions.addException(new ValidationException(String
					.format("%d characters were expected on line %d, but found %d characters", mapper
							.getPlainTextRecord().size(), lineIndex, text.length())));
			throw exceptions;
		}

		Field lineNumberField = mapper.getLineNumberField();
		if (lineNumberField != null) {
			try {
				BeanUtils.setProperty(bean, lineNumberField.getName(), lineIndex);
			} catch (Exception e) {
				throw new RuntimeException("Error set the line number on '" + lineNumberField.getName() + "' property");
			}
		}

		NamedTreeNode root = mapper.getTreeFields();
		for (NamedTreeNode child : root.getChilds()) {
			FieldMapper fieldMapper = getFieldMapper(child);
			try {
				parseValue(bean, text, child, fieldMapper);
			} catch (ValidationException e) {
				exceptions.addException(e);
			} catch (JPlainTextException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new JPlainTextException(String.format("Error setting value on field %s. %s",
						fieldMapper.getField(),
						ex.getMessage()), ex);
			}
		}
		if (exceptions.count() > 0) {
			throw exceptions;
		}
		return bean;
	}

	public void parseFile(InputStream input, List<T> result) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		try {
			String line = null;
			int lineIndex = 1;
			while ((line = reader.readLine()) != null) {
				result.add(parseText(line, lineIndex));
				lineIndex++;
			}
		} finally {
			reader.close();
		}
	}

	public List<T> parseFile(InputStream input) throws IOException {
		List<T> result = new LinkedList<T>();
		parseFile(input, result);
		return result;
	}

	public void parseFile(String fileName, List<T> result) throws IOException {
		FileInputStream input = new FileInputStream(fileName);
		try {
			parseFile(input, result);
		} finally {
			input.close();
		}
	}

	public List<T> parseFile(String fileName) throws IOException {
		List<T> result = new LinkedList<T>();
		parseFile(fileName, result);
		return result;
	}

}