package com.github.francofabio.jplaintext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class PlainTextExpression {

	private static final String reCheckExpression = "([\\-]?[1-9]{1}[0-9]*)\\:([1-9]{1}[0-9]*)\\=(.+)";

	private String expression;
	private int start;
	private int length;
	private String value;
	
	public PlainTextExpression(String expression) {
		parse(expression);
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public String getValue() {
		return value;
	}

	public String getExpression() {
		return expression;
	}

	private void parse(String expr) throws InvalidExpression {
		PlainTextExpression.validateExpression(expr);
		
		Pattern pattern = Pattern.compile(reCheckExpression);
		Matcher matcher = pattern.matcher(expr);
		if (matcher.find()) {
			this.expression = expr;
			this.start = Integer.parseInt(matcher.group(1));
			this.length = Integer.parseInt(matcher.group(2));
			this.value = matcher.group(3);
		}
	}

	public String eval(String s) {
		if (!StringUtils.isBlank(s)) {
			int beginIndex = this.start;
			if (beginIndex < 0) {
				beginIndex = s.length() + beginIndex;
			} else {
				beginIndex--;
			}
			int endIndex = beginIndex + this.length;
			if (endIndex > s.length()) {
				throw new IllegalStateException("End index greater than parameter string");
			}
			String other = s.substring(beginIndex, endIndex);
			return other.trim();
		}
		return null;
	}
	
	public boolean isSatisfiedBy(String s) {
		String other = eval(s);
		if (other != null) {
			return other.trim().equals(this.value.trim());
		}
		return false;
	}

	public static void validateExpression(String expr) throws InvalidExpression {
		if (!isValid(expr)) {
			throw new InvalidExpression("Invalid expression '" + expr + "'");
		}
	}
	
	public static boolean isValid(String expr) {
		return Pattern.matches(reCheckExpression, expr);
	}
	
	@Override
	public String toString() {
		return expression;
	}

}
