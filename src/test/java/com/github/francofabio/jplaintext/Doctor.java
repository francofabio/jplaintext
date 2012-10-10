package com.github.francofabio.jplaintext;

import java.util.Date;

import com.github.francofabio.jplaintext.annotation.PlainTextFieldEmbedded;
import com.github.francofabio.jplaintext.annotation.PlainTextRecord;

@PlainTextRecord(size = 107)
public class Doctor extends Employee {

	@PlainTextFieldEmbedded(order = 1)
	private DoctorScale scale;

	public Doctor() {
	}

	public Doctor(String name, String organization, Double salary, Date dateOfBirth, DoctorScale scale) {
		super(name, organization, salary, dateOfBirth);
		this.scale = scale;
	}

	public DoctorScale getScale() {
		return scale;
	}

	public void setScale(DoctorScale scale) {
		this.scale = scale;
	}

}
