package com.github.francofabio.jplaintext;

import com.github.francofabio.jplaintext.annotation.PlainTextField;
import com.github.francofabio.jplaintext.annotation.PlainTextRecord;

@PlainTextRecord(name = "trailer", size = 45)
public class PayrollTrailer {

	@PlainTextField(order = 1, size = 1, format = "0")
	private String recordType;

	@PlainTextField(order = 2, size = 6)
	private int totalRecords;
	
	@PlainTextField(order = 3, size = 6)
	private int totalRecordsEmployee;
	
	@PlainTextField(order = 4, size = 6)
	private int totalRecordsSalary;

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalRecordsSalary() {
		return totalRecordsSalary;
	}

	public void setTotalRecordsSalary(int totalRecordsSalary) {
		this.totalRecordsSalary = totalRecordsSalary;
	}

	public int getTotalRecordsEmployee() {
		return totalRecordsEmployee;
	}

	public void setTotalRecordsEmployee(int totalRecordsEmployee) {
		this.totalRecordsEmployee = totalRecordsEmployee;
	}

}
