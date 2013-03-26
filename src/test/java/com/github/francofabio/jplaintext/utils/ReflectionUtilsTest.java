package com.github.francofabio.jplaintext.utils;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Test;

import com.github.francofabio.jplaintext.Address;
import com.github.francofabio.jplaintext.Developer;
import com.github.francofabio.jplaintext.Doctor;
import com.github.francofabio.jplaintext.Employee;
import com.github.francofabio.jplaintext.Person;

public class ReflectionUtilsTest {

	private boolean containsField(Collection<Field> fields, String fieldName) {
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void shouldGetAllFieldsOfBaseClass() throws Exception {
		Collection<Field> fields = ReflectionUtils.fields(Person.class);

		assertEquals(1, fields.size());
		assertTrue(containsField(fields, "name"));
	}

	@Test
	public void shouldGetAllFieldsOfClassWithBase() throws Exception {
		Collection<Field> fields = ReflectionUtils.fields(Employee.class);

		assertEquals(5, fields.size());
		assertTrue(containsField(fields, "name"));
		assertTrue(containsField(fields, "organization"));
		assertTrue(containsField(fields, "salary"));
		assertTrue(containsField(fields, "dateOfBirth"));
	}

	@Test
	public void shouldGetAllFieldsOfClassWithTowClassLevel() {
		Collection<Field> fields = ReflectionUtils.fields(Doctor.class);

		assertEquals(6, fields.size());
		assertTrue(containsField(fields, "name"));
		assertTrue(containsField(fields, "organization"));
		assertTrue(containsField(fields, "salary"));
		assertTrue(containsField(fields, "dateOfBirth"));
		assertTrue(containsField(fields, "scale"));
	}

	@Test
	public void shouldGetField() {
		Field field = ReflectionUtils.getField(Developer.class, "uid");

		assertNotNull(field);
		assertEquals("uid", field.getName());
	}

	@Test
	public void shouldGetNestedField() {
		Field field = ReflectionUtils.getField(Doctor.class, "scale.hour");

		assertNotNull(field);
		assertEquals("hour", field.getName());
	}
	
	@Test
	public void shouldGetInheritedField() {
		Field field = ReflectionUtils.getField(Developer.class, "name");

		assertNotNull("Field 'name' not found", field);
		assertEquals("name", field.getName());
	}

	@Test
	public void shouldSetFieldValue() {
		final String expectedUID = "445e6578-1ec7-460d-9031-e19ea5a2275a";
		Developer developer = new Developer();
		ReflectionUtils.setFieldValue(developer, "uid", expectedUID);

		assertEquals(expectedUID, developer.getUid());
	}
	
	@Test
	public void shouldSetNestedFieldValue() {
		final String expectedHour = "17";
		Doctor doctor = new Doctor();
		ReflectionUtils.setFieldValue(doctor, "scale.hour", expectedHour);

		assertEquals(expectedHour, doctor.getScale().getHour());
	}
	
	@Test
	public void shouldSetInheritedFieldValue() {
		final String expectedName = "Fake Name For Example";
		Developer developer = new Developer();
		ReflectionUtils.setFieldValue(developer, "name", expectedName);

		assertEquals(expectedName, developer.getName());
	}
	
	@Test
	public void shouldGetPropertyValue() {
		final String expectedName = "Fake Name For Example";
		Developer developer = new Developer();
		developer.setName(expectedName);

		assertEquals(expectedName, ReflectionUtils.getPropertyValue(developer, "name"));
		assertEquals(null, ReflectionUtils.getPropertyValue(developer, "address.street"));
	}
	
	@Test
	public void shouldGetNestedPropertyValue() {
		final String expectedName = "Fake Name For Example";
		Developer developer = new Developer();
		developer.setName(expectedName);
		developer.setAddress(new Address());
		developer.getAddress().setStreet("Rua Bela Vista, 228");

		assertEquals(expectedName, ReflectionUtils.getPropertyValue(developer, "name"));
		assertEquals("Rua Bela Vista, 228", ReflectionUtils.getPropertyValue(developer, "address.street"));		
	}

}
