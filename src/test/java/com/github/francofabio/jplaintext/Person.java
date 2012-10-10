package com.github.francofabio.jplaintext;

import com.github.francofabio.jplaintext.annotation.PlainTextRecord;
import com.github.francofabio.jplaintext.annotation.PlainTextField;

@PlainTextRecord(name = "person", size = 40)
public class Person {
	@PlainTextField(field = "Name", required = true, order = 1, size = 40)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
