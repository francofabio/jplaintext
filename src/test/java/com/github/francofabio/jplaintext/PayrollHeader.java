package com.github.francofabio.jplaintext;

import java.util.Date;

import com.github.francofabio.jplaintext.annotation.PlainTextField;
import com.github.francofabio.jplaintext.annotation.PlainTextRecord;

@PlainTextRecord(name = "header", size = 45, fillSpaceToCompleteSize = true)
public class PayrollHeader {

	@PlainTextField(order = 1, size = 1, format="0")
	private String recordType;
	
	@PlainTextField(order = 2, size = 8, format = "yyyyMMdd")
	private Date date;

	@PlainTextField(order = 3, size = 6, format = "0")
	private String reference;

	@PlainTextField(order = 4, size = 15)
	private String departament;

	@PlainTextField(order = 5, size = 15)
	private String organization;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

}
