package com.github.francofabio.jplaintext;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import com.github.francofabio.jplaintext.JPlainTextGenerator;

public class JPlainTextGeneratorTest {

	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String WINDOWS_LINE_SEPARATOR = "\r\n";

	private JPlainTextGenerator<Person> personGenerator;
	private JPlainTextGenerator<Employee> employeeGenerator;
	private JPlainTextGenerator<Doctor> doctorGenerator;
	private JPlainTextGenerator<Developer> developerGenerator;

	@Before
	public void setup() {
		personGenerator = new JPlainTextGenerator<Person>(Person.class);
		employeeGenerator = new JPlainTextGenerator<Employee>(Employee.class);
		doctorGenerator = new JPlainTextGenerator<Doctor>(Doctor.class);
		developerGenerator = new JPlainTextGenerator<Developer>(Developer.class);
	}

	@Test
	public void shouldGenerateTextPojo() {
		Person person = new Person();
		person.setName("Fake Name For Example");

		String text = personGenerator.generateText(person);

		assertEquals(40, text.length());
		assertEquals("Fake Name For Example                   ", text);
	}

	@Test
	public void shouldGenerateTextPojoWithInheritance() throws Exception {
		final String expected = "Fake Name For Example                   Binartecno                    00000035000019830506";
		Employee employee = new Employee();
		employee.setName("Fake Name For Example");
		employee.setOrganization("Binartecno");
		employee.setSalary(3500.00);
		employee.setDateOfBirth(DateUtils.parseDate("06/05/1983", "dd/MM/yyyy"));

		String text = employeeGenerator.generateText(employee);

		assertEquals(expected.length(), text.length());
		assertEquals(expected, text);
	}

	@Test
	public void shouldGenerateTextWithEmbeddedFields() throws Exception {
		final String expected = "Fake Name For Example                   Binartecno                    00000100000019830506Segunda-feira  07";
		Doctor doctor = new Doctor("Fake Name For Example", "Binartecno", 10000.00, DateUtils.parseDate("06/05/1983",
				"dd/MM/yyyy"), new DoctorScale("Segunda-feira", "07"));

		String text = doctorGenerator.generateText(doctor);

		assertEquals(expected.length(), text.length());
		assertEquals(expected, text);
	}

	@Test
	public void shouldGenerateTextWithReadOnlyFields() throws Exception {
		final String expected = "Fake Name For Example                   Binartecno                    00000035000019830506445e6578-1ec7-460d-9031-e19ea5a2275aJava      Python    http://www.google.com.br                                                                            Static                                                                                                                                 29153040";
		Developer developer = new Developer("445e6578-1ec7-460d-9031-e19ea5a2275a",
				"Fake Name For Example",
				"Binartecno",
				3500.00,
				DateUtils.parseDate("06/05/1983", "dd/MM/yyyy"),
				new DeveloperLanguage("Java", "Python"),
				"http://www.google.com.br");
		developer.setLanguageType(LanguageType.STATIC);
		developer.setAddress(new Address());
		developer.getAddress().setZipCode(new ZipCode("29153040"));
		String text = developerGenerator.generateText(developer);
		
		assertEquals(expected.length(), text.length());
		assertEquals(expected, text);
	}

	@Test
	public void shouldGenerateTextForOutputStream() throws Exception {
		final String expected = "Fake Name For Example                   Binartecno                    00000035000019830506"
				+ NEW_LINE;
		Employee employee = new Employee();
		employee.setName("Fake Name For Example");
		employee.setOrganization("Binartecno");
		employee.setSalary(3500.00);
		employee.setDateOfBirth(DateUtils.parseDate("06/05/1983", "dd/MM/yyyy"));

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		employeeGenerator.generateText(employee, output);
		output.flush();

		assertEquals(expected.length(), output.size());
		assertEquals(expected, output.toString());
	}

	@Test
	public void shouldGenerateTextForWriter() throws Exception {
		final String expected = "Fake Name For Example                   Binartecno                    00000035000019830506"
				+ NEW_LINE;
		Employee employee = new Employee();
		employee.setName("Fake Name For Example");
		employee.setOrganization("Binartecno");
		employee.setSalary(3500.00);
		employee.setDateOfBirth(DateUtils.parseDate("06/05/1983", "dd/MM/yyyy"));

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

		employeeGenerator.generateText(employee, writer);
		writer.flush();

		assertEquals(expected.length(), output.size());
		assertEquals(expected, output.toString());
	}

	@Test
	public void shouldGenerateTextFromList() throws Exception {
		final String expected = "Fake Name For Example                   Binartecno                    00000035000019830506"
				+ NEW_LINE
				+ "Fake Name For Example Female            Binar                         00000055000019860612"
				+ NEW_LINE;

		Employee employee1 = new Employee("Fake Name For Example",
				"Binartecno",
				3500.00,
				DateUtils.parseDate("06/05/1983", "dd/MM/yyyy"));
		Employee employee2 = new Employee("Fake Name For Example Female",
				"Binar",
				5500.00,
				DateUtils.parseDate("12/06/1986", "dd/MM/yyyy"));

		List<Employee> employees = Arrays.asList(employee1, employee2);

		String text = employeeGenerator.generateText(employees);

		assertEquals(expected.length(), text.length());
		assertEquals(expected, text);
	}

	@Test
	public void shouldGenerateTextFromListForOutputStream() throws Exception {
		final String expected = "Fake Name For Example                   Binartecno                    00000035000019830506"
				+ NEW_LINE
				+ "Fake Name For Example Female            Binar                         00000055000019860612"
				+ NEW_LINE;

		Employee employee1 = new Employee("Fake Name For Example",
				"Binartecno",
				3500.00,
				DateUtils.parseDate("06/05/1983", "dd/MM/yyyy"));
		Employee employee2 = new Employee("Fake Name For Example Female",
				"Binar",
				5500.00,
				DateUtils.parseDate("12/06/1986", "dd/MM/yyyy"));

		List<Employee> employees = Arrays.asList(employee1, employee2);

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		employeeGenerator.generateText(employees, output);

		output.flush();

		assertEquals(expected.length(), output.size());
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void shouldGenerateTextFromListForOutputStreamWithLineSeparator() throws Exception {
		final String expected = "Fake Name For Example                   Binartecno                    00000035000019830506"
				+ WINDOWS_LINE_SEPARATOR
				+ "Fake Name For Example Female            Binar                         00000055000019860612"
				+ WINDOWS_LINE_SEPARATOR;

		Employee employee1 = new Employee("Fake Name For Example",
				"Binartecno",
				3500.00,
				DateUtils.parseDate("06/05/1983", "dd/MM/yyyy"));
		Employee employee2 = new Employee("Fake Name For Example Female",
				"Binar",
				5500.00,
				DateUtils.parseDate("12/06/1986", "dd/MM/yyyy"));

		List<Employee> employees = Arrays.asList(employee1, employee2);

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		JPlainTextGenerator<Employee> plainTextGenerator = new JPlainTextGenerator<Employee>(Employee.class, WINDOWS_LINE_SEPARATOR);
		plainTextGenerator.generateText(employees, output);

		output.flush();

		assertEquals(expected.length(), output.size());
		assertEquals(expected, output.toString());
	}

	@Test
	public void shouldGenerateTextFromListForWriter() throws Exception {
		final String expected = "Fake Name For Example                   Binartecno                    00000035000019830506"
				+ NEW_LINE
				+ "Fake Name For Example Female            Binar                         00000055000019860612"
				+ NEW_LINE;
		Employee employee1 = new Employee("Fake Name For Example",
				"Binartecno",
				3500.00,
				DateUtils.parseDate("06/05/1983", "dd/MM/yyyy"));
		Employee employee2 = new Employee("Fake Name For Example Female",
				"Binar",
				5500.00,
				DateUtils.parseDate("12/06/1986", "dd/MM/yyyy"));
		List<Employee> employees = Arrays.asList(employee1, employee2);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

		employeeGenerator.generateText(employees, writer);

		writer.flush();

		assertEquals(expected.length(), output.size());
		assertEquals(expected, output.toString());
	}

}
