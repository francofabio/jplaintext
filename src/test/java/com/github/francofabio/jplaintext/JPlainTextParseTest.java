package com.github.francofabio.jplaintext;

import static org.apache.commons.lang3.time.DateUtils.parseDate;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.francofabio.jplaintext.validator.ValidationsException;

public class JPlainTextParseTest {

	private JPlainTextParse<Employee> parser;
	private JPlainTextParse<Doctor> parserDoctor;
	private JPlainTextParse<Developer> parserDeveloper;
	private String lineFull;
	private String lineEmpty;
	private String lineDoctor;
	private String lineDeveloper;
	private InputStream input;

	@Before
	public void setup() {
		parser = new JPlainTextParse<Employee>(Employee.class);
		parserDoctor = new JPlainTextParse<Doctor>(Doctor.class);
		parserDeveloper = new JPlainTextParse<Developer>(Developer.class);
		lineFull      = "Fake Name For Example                   Binartecno                    00000035000019830506";
		lineEmpty     = "Fake Name For Example                   Binartecno                    000000350000        ";
		lineDoctor    = "Fake Name For Example                   Binartecno                    00000100000019830506Segunda-feira  07";
		lineDeveloper = "Fake Name For Example                   Binartecno                    00000035000019830506445e6578-1ec7-460d-9031-e19ea5a2275aJava      Python    http://www.google.com.br                                                                                                                                                                                                                   29153040";

		String lines  = "Fake Name For Example                   Binartecno                    00000035000019830506\r\n"
				      + "Fake Name For Example Female            Binar                         00000055000019860612\r\n"
				      + "Fake Name For Example Female            Binar                         00000055000019860612";
		input = new ByteArrayInputStream(lines.getBytes());
	}

	@Test
	public void shouldParseStringField() {
		Employee employee = parser.parseText(lineEmpty);

		assertEquals("Fake Name For Example", employee.getName());
	}

	@Test
	public void shouldParseDoubleField() {
		Employee employee = parser.parseText(lineEmpty);

		assertEquals(3500.00, employee.getSalary(), 0);
	}

	@Test
	public void shouldParseDateField() throws Exception {
		final Date expected = parseDate("06/05/1983", "dd/MM/yyyy");

		Employee employee = parser.parseText(lineFull);

		assertEquals(expected, employee.getDateOfBirth());
	}

	@Test
	public void shouldParseLine() throws Exception {
		final Date expectedBirth = parseDate("06/05/1983", "dd/MM/yyyy");

		Employee employee = parser.parseText(lineFull);

		assertEquals("Fake Name For Example", employee.getName());
		assertEquals("Binartecno", employee.getOrganization());
		assertEquals(3500.00, employee.getSalary(), 0);
		assertEquals(expectedBirth, employee.getDateOfBirth());
	}

	@Test
	public void shouldParseEmbeddedField() throws Exception {
		final Date expectedBirth = parseDate("06/05/1983", "dd/MM/yyyy");

		Doctor doctor = parserDoctor.parseText(lineDoctor);

		assertEquals("Fake Name For Example", doctor.getName());
		assertEquals("Binartecno", doctor.getOrganization());
		assertEquals(10000.00, doctor.getSalary(), 0);
		assertEquals(expectedBirth, doctor.getDateOfBirth());
		assertEquals("Segunda-feira", doctor.getScale().getDayOfWeek());
		assertEquals("07", doctor.getScale().getHour());
	}

	@Test
	public void shouldParseWithReadOnlyFields() throws Exception {
		final Date expectedBirth = parseDate("06/05/1983", "dd/MM/yyyy");

		Developer developer = parserDeveloper.parseText(lineDeveloper);

		assertEquals("Fake Name For Example", developer.getName());
		assertEquals("Binartecno", developer.getOrganization());
		assertEquals(3500.00, developer.getSalary(), 0);
		assertEquals(expectedBirth, developer.getDateOfBirth());
		assertEquals("445e6578-1ec7-460d-9031-e19ea5a2275a", developer.getUid());
	}
	
	@Test
	public void shouldParseWithNestedReadOnlyFields() throws Exception {
		final Date expectedBirth = parseDate("06/05/1983", "dd/MM/yyyy");

		Developer developer = parserDeveloper.parseText(lineDeveloper);

		assertEquals("Fake Name For Example", developer.getName());
		assertEquals("Binartecno", developer.getOrganization());
		assertEquals(3500.00, developer.getSalary(), 0);
		assertEquals(expectedBirth, developer.getDateOfBirth());
		assertEquals("445e6578-1ec7-460d-9031-e19ea5a2275a", developer.getUid());
		assertEquals("Java", developer.getDeveloperLanguage().getLanguage1());
		assertEquals("Python", developer.getDeveloperLanguage().getLanguage2());
	}

	@Test(expected = ValidationsException.class)
	public void parseLineInvalidSize() {
		Employee employee = parser.parseText(lineEmpty + "aaaaa");

		assertEquals("Fake Name For Example", employee.getName());
		assertEquals("Binartecno", employee.getOrganization());
		assertEquals(3500.00, employee.getSalary(), 0);
	}

	@Test(expected = ValidationsException.class)
	public void shouldParseLineRequiredField() {
		final String linha = "                                        Binartecno                    000000350000                  ";
		Employee employee = parser.parseText(linha);

		assertEquals("Fake Name For Example", employee.getName());
		assertEquals("Binartecno", employee.getOrganization());
		assertEquals(3500.00, employee.getSalary(), 0);
	}

	@Test
	public void shouldParseFile() throws Exception {
		final Object[][] expectedValues = new Object[][] {
				{ "Fake Name For Example", "Binartecno", new Double(3500.00), parseDate("06/05/1983", "dd/MM/yyyy") },
				{ "Fake Name For Example Female", "Binar", new Double(5500.00), parseDate("12/06/1986", "dd/MM/yyyy") },
				{ "Fake Name For Example Female", "Binar", new Double(5500.00), parseDate("12/06/1986", "dd/MM/yyyy") } };
		final List<Employee> records = parser.parseFile(input);

		assertEquals(3, records.size());

		for (int i = 0; i < records.size(); i++) {
			Employee employee = records.get(i);

			assertEquals(expectedValues[i][0], employee.getName());
			assertEquals(expectedValues[i][1], employee.getOrganization());
			assertEquals(expectedValues[i][2], employee.getSalary());
			assertEquals(expectedValues[i][3], employee.getDateOfBirth());
		}
	}
}
