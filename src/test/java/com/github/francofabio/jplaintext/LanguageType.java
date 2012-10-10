package com.github.francofabio.jplaintext;

public enum LanguageType {

	STATIC("Static"),
	DYNAMIC("Dynamic");

	private String description;

	LanguageType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return description;
	}

}
