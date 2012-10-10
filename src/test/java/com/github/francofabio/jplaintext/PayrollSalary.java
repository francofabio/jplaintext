package com.github.francofabio.jplaintext;

import com.github.francofabio.jplaintext.annotation.PlainTextField;
import com.github.francofabio.jplaintext.annotation.PlainTextRecord;

@PlainTextRecord(name = "salary", size = 45)
public class PayrollSalary {

	@PlainTextField(order = 1, size = 1)
	private String recordType;

	@PlainTextField(order = 1, size = 15, decimals = 2)
	private double salary;

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

}
