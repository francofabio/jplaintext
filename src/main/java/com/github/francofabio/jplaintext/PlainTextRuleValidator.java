package com.github.francofabio.jplaintext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlainTextRuleValidator {

	private int max = -1;
	private int min = -1;
	private List<PlainTextExpression> expressions;

	public PlainTextRuleValidator() {
		this.expressions = new ArrayList<PlainTextExpression>();
	}

	public PlainTextRuleValidator max(int max) {
		this.max = max;
		return this;
	}

	public PlainTextRuleValidator min(int min) {
		this.min = min;
		return this;
	}

	public PlainTextRuleValidator expr(String expr) {
		this.expressions.add(new PlainTextExpression(expr));
		return this;
	}

	public int max() {
		return max;
	}

	public int min() {
		return min;
	}

	public List<PlainTextExpression> expressions() {
		return Collections.unmodifiableList(expressions);
	}

}
