package com.github.francofabio.jplaintext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class PlainTextCondition {

	private String expr;
	private Class<?> clazz;
	private String alias;
	private List<PlainTextExpression> expressions;

	public PlainTextCondition(String expr, Class<?> clazz, String alias) {
		this.expr = expr;
		this.clazz = clazz;
		this.alias = alias;
		this.expressions = new ArrayList<PlainTextExpression>();

		parse(expr);
	}

	public PlainTextCondition(String expr, Class<?> clazz) {
		this(expr, clazz, Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1));
	}

	public String getAlias() {
		return alias;
	}

	public String getExpr() {
		return expr;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public List<PlainTextExpression> getExpressions() {
		return Collections.unmodifiableList(expressions);
	}

	private void parse(String expr) {
		final String re = "\\[.*\\]";
		if (!Pattern.matches(re, expr)) {
			throw new InvalidExpression("Invalid expression '" + expr + "'");
		}
		String[] exprs = expr.replace("[", "").replace("]", "").split(",");
		for (String e : exprs) {
			this.expressions.add(new PlainTextExpression(e));
		}
	}

	public boolean isSatisfiedBy(String str) {
		for (PlainTextExpression e : expressions) {
			if (!e.isSatisfiedBy(str)) {
				return false;
			}
		}
		return true;
	}
}
