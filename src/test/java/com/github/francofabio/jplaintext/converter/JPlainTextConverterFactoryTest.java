package com.github.francofabio.jplaintext.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import com.github.francofabio.jplaintext.Doctor;
import com.github.francofabio.jplaintext.Employee;
import com.github.francofabio.jplaintext.EmployeeConverter;
import com.github.francofabio.jplaintext.FieldMapper;
import com.github.francofabio.jplaintext.JPlainTextConverterFactory;
import com.github.francofabio.jplaintext.LanguageType;
import com.github.francofabio.jplaintext.convert.Converter;
import com.github.francofabio.jplaintext.convert.ConverterException;

public class JPlainTextConverterFactoryTest {

	@Test
	public void shouldConvertByte() {
		final String linePart = "0000000100";
		final Byte expected = new Byte((byte) 100);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Byte.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(Byte.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertBytePrimitive() {
		final String linePart = "0000000100";
		final byte expected = 100;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, byte.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(byte.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertByteNegative() {
		final String linePart = "-0000000100";
		final byte expected = -100;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, byte.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(byte.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertShort() {
		final String linePart = "0000000100";
		final Short expected = new Short((short) 100);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Short.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(Short.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertShortPrimitive() {
		final String linePart = "0000000100";
		final short expected = 100;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, short.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(short.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertShortNegative() {
		final String linePart = "-0000000100";
		final short expected = -100;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, short.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(short.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertInteger() {
		final String linePart = "0000000234";
		final Integer expected = new Integer(234);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Integer.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(Integer.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertIntegerPrimitive() {
		final String linePart = "0000000234";
		final int expected = 234;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, int.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(int.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertIntegerNegative() {
		final String linePart = "-0000000234";
		final int expected = -234;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, int.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(int.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertLong() {
		final String linePart = "0000000234";
		final Long expected = new Long(234);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Long.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(Long.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertLongPrimitive() {
		final String linePart = "0000000234";
		final long expected = 234l;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, long.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(long.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertLongNegative() {
		final String linePart = "-0000000234";
		final long expected = -234l;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, long.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(long.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertFloat() {
		final String linePart = "0000010234";
		final Float expected = new Float(102.34);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Float.class, null, 2);

		Converter converter = JPlainTextConverterFactory.getConverter(Float.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertFloatPrimitive() {
		final String linePart = "0000010234";
		final float expected = (float) 102.34;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, float.class, null, 2);

		Converter converter = JPlainTextConverterFactory.getConverter(float.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertFloatNDecimals() {
		final String linePart = "000001021234";
		final Float expected = new Float(102.1234);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 12, null, true, false, Float.class, null, 4);

		Converter converter = JPlainTextConverterFactory.getConverter(Float.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertFloatNegative() {
		final String linePart = "-0000010234";
		final float expected = (float) -102.34;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, float.class, null, 2);

		Converter converter = JPlainTextConverterFactory.getConverter(float.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertDouble() {
		final String linePart = "0000010234";
		final Double expected = new Double(102.34);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Double.class, null, 2);

		Converter converter = JPlainTextConverterFactory.getConverter(Double.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertDoublePrimitive() {
		final String linePart = "0000010234";
		final double expected = 102.34;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Double.class, null, 2);

		Converter converter = JPlainTextConverterFactory.getConverter(double.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertDoubleNDecimals() {
		final String linePart = "000001021234";
		final Double expected = new Double(102.1234);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 12, null, true, false, Double.class, null, 4);

		Converter converter = JPlainTextConverterFactory.getConverter(Double.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertDoubleNegative() {
		final String linePart = "-0000010234";
		final Double expected = new Double(-102.34);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Double.class, null, 2);

		Converter converter = JPlainTextConverterFactory.getConverter(Double.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertString() {
		final String linePart = "Fabio Franco da Silva                   ";
		final String expected = "Fabio Franco da Silva";
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 40, null, true, false, String.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(String.class);
		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}
	
	@Test
	public void shouldConvertStringAsNumericRepresentation() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, String.class, "0", 0);

		Converter converter = JPlainTextConverterFactory.getConverter(String.class);
		assertEquals("0000023456", converter.asString("23456", fieldMapper));
		assertEquals("0000023456", converter.asObject("0000023456", fieldMapper));		
	}
	
	@Test(expected=ConverterException.class)
	public void shouldConvertStringInvalidNumericRepresentation() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, String.class, "0", 0);

		Converter converter = JPlainTextConverterFactory.getConverter(String.class);
		assertEquals("0000A23456", converter.asString("A23456", fieldMapper));		
	}
	
	@Test(expected=ConverterException.class)
	public void shouldParseStringInvalidNumericRepresentation() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, String.class, "0", 0);

		Converter converter = JPlainTextConverterFactory.getConverter(String.class);
		assertEquals("0000A23456", converter.asObject("0000A23456", fieldMapper));		
	}

	@Test
	public void shouldConvertStringTrimLeft() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 8, null, true, false, String.class, "0", 0);

		Converter converter = JPlainTextConverterFactory.getConverter(String.class);
		assertEquals("34567890", converter.asString("1234567890", fieldMapper));	
	}
	
	@Test
	public void shouldConvertStringTrimRight() {
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 15, null, true, false, String.class, "", 0);

		Converter converter = JPlainTextConverterFactory.getConverter(String.class);
		assertEquals("001/000025246/0", converter.asString("001/000025246/01", fieldMapper));	
	}
	
	@Test
	public void shouldConvertCharacter() {
		final String linePart = "F";
		final Character expected = new Character('F');
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 40, null, true, false, Character.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(Character.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertCharacterPrimitive() {
		final String linePart = "A";
		final char expected = 'A';
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 40, null, true, false, char.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(char.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertDate() throws Exception {
		final String linePart = "20120422";
		final Date expected = DateUtils.parseDate("22/04/2012", "dd/MM/yyyy");
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Date.class, "yyyyMMdd", 0);

		Converter converter = JPlainTextConverterFactory.getConverter(Date.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertDateOtherFormat() throws Exception {
		final String format = "MMddyyyy";
		final String linePart = "04212012";
		final Date expected = DateUtils.parseDate("04212012", format);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Date.class, format, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(Date.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}
	
	@Test
	public void shouldConvertEmptyDate() throws Exception {
		final String format = "MMddyyyy";
		final String linePart = "00000000";
		final Date expected = null;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, Date.class, format, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(Date.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}
	
	@Test
	public void shouldConvertTime() throws Exception {
		final String format = "HHmmss";
		final String linePart = "124015";
		final Date expected = DateUtils.parseDate("124015", format);
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 6, null, true, false, Date.class, format, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(Date.class);

		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));		
	}

	@Test
	public void shouldConvertEnum() {
		final String linePart = "DYNAMIC   ";
		final LanguageType expected = LanguageType.DYNAMIC;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, LanguageType.class, null, 0);

		Converter converter = JPlainTextConverterFactory.getConverter(LanguageType.class);
		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldConvertEnumByProperty() {
		final String linePart = "Static    ";
		final LanguageType expected = LanguageType.STATIC;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, LanguageType.class, null, 0);
		fieldMapper.getArguments().put("property", "description");

		Converter converter = JPlainTextConverterFactory.getConverter(LanguageType.class);
		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test(expected = ConverterException.class)
	public void shouldConvertEnumPropertyNotFound() {
		final String linePart = "Static    ";
		final LanguageType expected = LanguageType.STATIC;
		final FieldMapper fieldMapper = new FieldMapper(1, 1, 10, null, true, false, LanguageType.class, null, 0);
		fieldMapper.getArguments().put("property", "longtext");

		Converter converter = JPlainTextConverterFactory.getConverter(LanguageType.class);
		assertEquals(linePart, converter.asString(expected, fieldMapper));
		assertEquals(expected, converter.asObject(linePart, fieldMapper));
	}

	@Test
	public void shouldResolvePrimitiveTypes() {
		JPlainTextConverterFactory.getConverter(byte.class);
		JPlainTextConverterFactory.getConverter(short.class);
		JPlainTextConverterFactory.getConverter(int.class);
		JPlainTextConverterFactory.getConverter(long.class);
		JPlainTextConverterFactory.getConverter(float.class);
		JPlainTextConverterFactory.getConverter(double.class);
	}

	@Test(expected = ConverterException.class)
	public void shouldConverterNotFound() {
		JPlainTextConverterFactory.getConverter(Doctor.class);
	}

	@Test
	public void shouldSupportsEnumClass() {
		JPlainTextConverterFactory.getConverter(Enum.class);
	}

	@Test
	public void shouldRegisterConverter() {
		JPlainTextConverterFactory.registerConverter(new EmployeeConverter());

		assertTrue(JPlainTextConverterFactory.supported(Employee.class));
	}

}
