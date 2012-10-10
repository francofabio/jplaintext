package com.github.francofabio.jplaintext.validator;

import java.util.Date;

import org.junit.Test;

import com.github.francofabio.jplaintext.FieldMapper;
import com.github.francofabio.jplaintext.JPlainTextValidatorFactory;

public class JPlainTextValidatorFactoryTest {

	@Test
	public void shouldValidateString() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, String.class, null, 0);
		Validator validator = JPlainTextValidatorFactory.getValidator(String.class);
		validator.validate("Fulano de tal", fieldMapper, null);
	}
	
	@Test(expected=ValidationException.class)
	public void shouldValidateStringRequired() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, String.class, null, 0);
		Validator validator = JPlainTextValidatorFactory.getValidator(String.class);
		validator.validate("", fieldMapper, null);
	}
	
	@Test
	public void shouldValidateStringAsNumber() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, String.class, "0", 0);
		Validator validator = JPlainTextValidatorFactory.getValidator(String.class);
		validator.validate("0000000234", fieldMapper, null);
	}
	
	@Test(expected=ValidationException.class)
	public void shouldValidateStringInvalidNumber() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, String.class, "0", 0);
		Validator validator = JPlainTextValidatorFactory.getValidator(String.class);
		validator.validate("0000A00234", fieldMapper, null);
	}
	
	@Test
	public void shouldValidateNumber() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Integer.class, null, 0);
		Validator validator = JPlainTextValidatorFactory.getValidator(Integer.class);
		validator.validate("0000100234", fieldMapper, null);
	}
	
	@Test
	public void shouldValidateNumberNegative() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Integer.class, null, 0);
		Validator validator = JPlainTextValidatorFactory.getValidator(Integer.class);
		validator.validate("-0000100234", fieldMapper, null);
	}
	
	@Test
	public void shouldValidateNumberDecimal() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Double.class, null, 3);
		Validator validator = JPlainTextValidatorFactory.getValidator(Double.class);
		validator.validate("0000100234", fieldMapper, null);
	}
	
	@Test
	public void shouldValidateNumberWithFormat() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Double.class, ".00", 0);
		Validator validator = JPlainTextValidatorFactory.getValidator(Double.class);
		validator.validate("0000100234", fieldMapper, null);
	}
	
	@Test
	public void shouldValidateDate() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Date.class, null, 0);
		Validator validator = JPlainTextValidatorFactory.getValidator(Date.class);
		validator.validate("20120420", fieldMapper, null);
	}
	
	@Test
	public void shouldValidateDateWithFormat() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Date.class, "dd/MM/yyyy", 0);
		Validator validator = JPlainTextValidatorFactory.getValidator(Date.class);
		validator.validate("20/12/2020", fieldMapper, null);
	}
	
}
