package com.github.francofabio.jplaintext;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;

import com.github.francofabio.jplaintext.utils.ReflectionUtils;

public class JPlainTextParseRule {

	private final String newLineSeparator = System.getProperty("line.separator"); 
	
	private final List<PlainTextCondition> ruleMap;
	private final Map<String, ParseRule> rules;
	private final Map<String, Object> lastInstance;
	private final Map<String, PlainTextRuleValidator> validations;
	private final Map<String, Object> variables;
	private final List<PlainTextParseRuleListener> listeners;
	private int maxProgress = 100;

	public JPlainTextParseRule() {
		this.ruleMap = new ArrayList<PlainTextCondition>();
		this.rules = new HashMap<String, ParseRule>();
		this.lastInstance = new HashMap<String, Object>();
		this.validations = new HashMap<String, PlainTextRuleValidator>();
		this.variables = new HashMap<String, Object>();
		this.listeners = new ArrayList<PlainTextParseRuleListener>();
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public int getMaxProgress() {
		return maxProgress;
	}
	
	public void addListener(PlainTextParseRuleListener listener) {
		listeners.add(listener);
	}

	private void fireProgress(int current, int total) {
		int percent = maxProgress * current / total;
		for (PlainTextParseRuleListener l : listeners) {
			l.progress(percent);
		}
	}

	private PlainTextCondition satisfiedBy(String line) {
		for (PlainTextCondition c : ruleMap) {
			if (c.isSatisfiedBy(line)) {
				return c;
			}
		}
		return null;
	}

	public PlainTextCondition getRuleMap(String rule) {
		for (PlainTextCondition c : ruleMap) {
			if (c.getAlias().equals(rule)) {
				return c;
			}
		}
		return null;
	}

	public PlainTextRuleValidator addRuleMap(String rule, String expr, Class<?> clazz) {
		if (getRuleMap(rule) != null) {
			throw new JPlainTextException("Rule already mapped");
		}
		ruleMap.add(new PlainTextCondition(expr, clazz, rule));

		variables.put(getCountVarName(rule), new Integer(0));

		PlainTextRuleValidator validation = new PlainTextRuleValidator();
		validations.put(rule, validation);
		return validation;
	}

	/**
	 * Add rule
	 * 
	 * @param mappedRule
	 * Alias for mapped rule
	 * @param parent
	 * Parent of mapped rule. If string instance will used mapped rule
	 * @param property
	 * Method or property that will called to set value of matched record
	 */
	public void addRule(String mappedRule, Object parent, String call) {
		if (getRuleMap(mappedRule) == null) {
			throw new JPlainTextException("Rule " + mappedRule + " not mapped");
		}
		rules.put(mappedRule, new ParseRule(mappedRule, parent, call));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void fireRule(ParseRule parseRule, Object value) {
		Object bean = null;
		if (parseRule.type.equals(ParseRuleType.BEAN)) {
			bean = parseRule.parent;
		} else {
			bean = lastInstance.get(parseRule.parent);
		}

		if (parseRule.callType.equals(CallType.PROPERTY)) {
			try {
				PropertyUtils.setProperty(bean, parseRule.call, value);
			} catch (Exception e) {
				throw new JPlainTextException("Error while setting value in property " + parseRule.call, e);
			}
		} else if (parseRule.callType.equals(CallType.COLLECTION)) {
			Collection collection = (Collection) ReflectionUtils.getPropertyValue(bean, parseRule.call);
			if (collection != null) {
				collection.add(value);
			}
		} else {
			try {
				MethodUtils.invokeMethod(bean, parseRule.call, value);
			} catch (Exception e) {
				throw new JPlainTextException("Error while invoke method " + parseRule.call, e);
			}
		}
	}

	private String getCountVarName(String rule) {
		return rule + "_count";
	}

	private void incrementCounter(String rule) {
		String varName = getCountVarName(rule);
		Integer counter = (Integer) variables.get(varName);
		counter++;

		variables.put(varName, counter);
	}

	private Integer count(String rule) {
		String varName = getCountVarName(rule);
		return (Integer) variables.get(varName);
	}

	private List<PlainTextValidationError> validateMinAndMax() {
		List<PlainTextValidationError> result = new ArrayList<PlainTextValidationError>();
		Set<String> keys = rules.keySet();

		for (String rule : keys) {
			PlainTextRuleValidator validator = validations.get(rule);
			int count = count(rule);
			if (validator.max() > -1 && count > validator.max()) {
				result.add(new PlainTextValidationError(String
						.format("Were expected maximum %d occurrences of rule %s. Found %d.",
								validator.max(),
								rule,
								count)));
			}
			if (validator.min() > -1 && count < validator.min()) {
				result.add(new PlainTextValidationError(String
						.format("Were expected minimum %d occurrences of rule %s. Found %d",
								validator.min(),
								rule,
								count)));
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private Object evalGroovyExpression(String expr) {
		Binding binding = new Binding();
		binding.getVariables().putAll(variables);
		GroovyShell shell = new GroovyShell(binding);
		return shell.evaluate(expr);
	}

	private List<PlainTextValidationError> validateExprs(String rule, String line) {
		List<PlainTextValidationError> result = new ArrayList<PlainTextValidationError>();
		PlainTextRuleValidator validator = validations.get(rule);

		if (validator.expressions().size() > 0) {
			for (PlainTextExpression e : validator.expressions()) {
				String compiledValue = e.eval(line);
				Object eval = evalGroovyExpression(e.getValue());
				String evaluated = (eval == null) ? null : eval.toString();
				if (evaluated != null && !evaluated.equals(compiledValue)) {
					result.add(new PlainTextValidationError(String.format("Invalid %s. Expected '%s' evaluated '%s'",
							rule,
							compiledValue,
							evaluated)));
				}
			}
		}

		return result;
	}

	public void parse(InputStream input) throws IOException, PlainTextValidationsErrors {
		final byte[] inputBuffer = IOUtils.toByteArray(input);
		final int streamSize = inputBuffer.length;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(inputBuffer)));
		PlainTextValidationsErrors validationsErrors = new PlainTextValidationsErrors();
		String line;
		int currentLineNumber = 1;
		int bytesRead = 0;
		
		while ((line = reader.readLine()) != null) {
			variables.put("currentLine", line);
			variables.put("currentLineNumber", currentLineNumber);
			
			PlainTextCondition condition = satisfiedBy(line);
			if (condition != null) {
				String rule = condition.getAlias();

				incrementCounter(rule);

				validationsErrors.addErrors(validateExprs(rule, line));

				Object o = JPlainTextParseHelper.parseText(line, condition.getClazz());

				this.lastInstance.put(rule, o);

				fireRule(rules.get(rule), o);
			}

			currentLineNumber++;
			
			//Number of processed bytes
			bytesRead += line.length() + newLineSeparator.length();
			
			fireProgress(bytesRead, streamSize);
		}
		validationsErrors.addErrors(validateMinAndMax());

		if (validationsErrors.getErrors().size() > 0) {
			throw validationsErrors;
		}
	}

	public void parse(String file) throws IOException, PlainTextValidationsErrors {
		FileInputStream input = new FileInputStream(file);
		try {
			parse(input);
		} finally {
			input.close();
		}
	}

	private class ParseRule {
		@SuppressWarnings("unused")
		final String rule;
		final Object parent;
		final String call;
		final ParseRuleType type;
		final CallType callType;
		final Class<?> clazz;

		public ParseRule(String rule, Object parent, String call) {
			this.rule = rule;
			this.parent = parent;
			this.call = call;

			if (parent instanceof String) {
				this.type = ParseRuleType.RULE;
			} else {
				this.type = ParseRuleType.BEAN;
			}

			if (this.type.equals(ParseRuleType.RULE)) {
				this.clazz = JPlainTextParseRule.this.getRuleMap(rule).getClazz();
			} else {
				this.clazz = this.parent.getClass();
			}

			if (ReflectionUtils.isProperty(clazz, call)) {
				Class<?> propertyClass = ReflectionUtils.getPropertyType(clazz, call);
				if (Collection.class.isAssignableFrom(propertyClass)) {
					this.callType = CallType.COLLECTION;
				} else {
					this.callType = CallType.PROPERTY;
				}
			} else {
				this.callType = CallType.METHOD;
			}
		}
	}

	private enum ParseRuleType {
		BEAN, RULE;
	}

	private enum CallType {
		PROPERTY, COLLECTION, METHOD;
	}
}
