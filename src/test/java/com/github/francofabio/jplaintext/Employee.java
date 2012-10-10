package com.github.francofabio.jplaintext;

import java.util.Date;

import com.github.francofabio.jplaintext.annotation.PlainTextField;
import com.github.francofabio.jplaintext.annotation.PlainTextRecord;

@PlainTextRecord(size = 90)
public class Employee extends Person {

	@PlainTextField(order = 1, size = 15, field = "Organization")
	private String organization;

	@PlainTextField(order = 2, size = 15, field = "Department")
	private String department;

	@PlainTextField(order = 3, size = 12, field = "Salary")
	private Double salary;

	@PlainTextField(order = 4, size = 8, field = "Date of birth")
	private Date dateOfBirth;

	public Employee() {
	}

	public Employee(String name, String organization, Double salary, Date dateOfBirth) {
		super();
		super.setName(name);
		this.organization = organization;
		this.salary = salary;
		this.dateOfBirth = dateOfBirth;
	}

	public Employee(String name, String organization, String department, Double salary, Date dateOfBirth) {
		super();
		super.setName(name);
		this.organization = organization;
		this.department = department;
		this.salary = salary;
		this.dateOfBirth = dateOfBirth;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
