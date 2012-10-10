package com.github.francofabio.jplaintext;

import com.github.francofabio.jplaintext.annotation.PlainTextField;

public class DeveloperLanguage {

	@PlainTextField(order = 1, size = 10)
	private String language1;

	@PlainTextField(order = 2, size = 10)
	private String language2;

	public DeveloperLanguage() {
	}

	public DeveloperLanguage(String language1, String language2) {
		super();
		this.language1 = language1;
		this.language2 = language2;
	}

	public String getLanguage1() {
		return language1;
	}
	
	public String getLanguage2() {
		return language2;
	}

}
