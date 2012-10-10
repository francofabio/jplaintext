package com.github.francofabio.jplaintext;

import com.github.francofabio.jplaintext.annotation.PlainTextField;

public class DoctorScale {
	@PlainTextField(order = 1, size = 15)
	private String dayOfWeek;

	@PlainTextField(order = 2, size = 2)
	private String hour;

	public DoctorScale() {
	}
	
	public DoctorScale(String dayOfWeek, String hour) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.hour = hour;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

}
