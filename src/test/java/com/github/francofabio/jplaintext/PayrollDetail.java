package com.github.francofabio.jplaintext;

import com.github.francofabio.jplaintext.annotation.PlainTextField;
import com.github.francofabio.jplaintext.annotation.PlainTextFieldEmbedded;
import com.github.francofabio.jplaintext.annotation.PlainTextRecord;

@PlainTextRecord(name = "detail", size = 45, fillSpaceToCompleteSize = true)
public class PayrollDetail {

	@PlainTextField(order = 1, size = 1, format = "0")
	private String recordType;

	@PlainTextFieldEmbedded(order = 2)
	private Person person;

	private PayrollSalary salary;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public PayrollSalary getSalary() {
		return salary;
	}

	public void setSalary(PayrollSalary salary) {
		this.salary = salary;
	}

}
