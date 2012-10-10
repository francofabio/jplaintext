package com.github.francofabio.jplaintext;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldMapper {

	private int order;
	private int start;
	private int size;
	private String field;
	private boolean required;
	private boolean embedded;
	private String format;
	private int decimals;
	private Class<?> fieldType;
	private Map<String, String> arguments;

	public FieldMapper(int order, int start, int size, boolean embedded) {
		super();
		this.order = order;
		this.start = start;
		this.size = size;
		this.embedded = embedded;
		this.arguments = new LinkedHashMap<String, String>();
	}

	public FieldMapper(int order, int start, int size, String field, boolean required, boolean embedded,
			Class<?> fieldType, String format, int decimals) {
		this(order, start, size, embedded);
		this.field = field;
		this.required = required;
		this.fieldType = fieldType;
		this.format = format;
		this.decimals = decimals;
	}

	public FieldMapper(int order, int start, int size, String field, boolean required, boolean embedded,
			Class<?> fieldType, String format, int decimals, Map<String, String> arguments) {
		this(order, start, size, field, required, embedded, fieldType, format, decimals);
		if (arguments != null) {
			this.arguments.putAll(arguments);
		}
	}

	public int getOrder() {
		return order;
	}

	public int getStart() {
		return start;
	}

	public int getSize() {
		return size;
	}

	public String getFormat() {
		return format;
	}

	public int getDecimals() {
		return decimals;
	}

	public String getField() {
		return field;
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isEmbedded() {
		return embedded;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public Map<String, String> getArguments() {
		return arguments;
	}

}
