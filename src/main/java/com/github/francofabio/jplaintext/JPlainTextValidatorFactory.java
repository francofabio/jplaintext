package com.github.francofabio.jplaintext;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.github.francofabio.jplaintext.validator.DateValidator;
import com.github.francofabio.jplaintext.validator.NumberValidator;
import com.github.francofabio.jplaintext.validator.StringValidator;
import com.github.francofabio.jplaintext.validator.Validator;

public class JPlainTextValidatorFactory {

	private static final SortedSet<Validator> validatorRegistered;
	private static final Validator defaultValidator;

	static {
		validatorRegistered = new TreeSet<Validator>(new ValidatorComparator());

		// Register default validators
		defaultValidator = new StringValidator();
		registerValidator(defaultValidator);
		registerValidator(new NumberValidator());
		registerValidator(new DateValidator());
	}

	private static Validator validatorFor(Class<?> cls) {
		for (Validator validator : validatorRegistered) {
			if (validator.supports(cls)) {
				return validator;
			}
		}
		return null;
	}

	public static Validator getValidator(Class<?> cls) {
		Validator converter = validatorFor(cls);
		if (converter == null) {
			return defaultValidator;
		}
		return converter;
	}

	public static void registerValidator(Validator validator) {
		validatorRegistered.add(validator);
	}

	public static boolean supported(Class<?> cls) {
		return validatorFor(cls) != null;
	}

	private static class ValidatorComparator implements Comparator<Validator> {

		@Override
		public int compare(Validator o1, Validator o2) {
			return o1.getClass().getName().compareTo(o2.getClass().getName());
		}

	}

}
