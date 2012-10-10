package com.github.francofabio.jplaintext;

import com.github.francofabio.jplaintext.annotation.PlainTextField;

public class ZipCode {

	@PlainTextField(order = 1, size = 5, format = "0")
	private String prefix;

	@PlainTextField(order = 2, size = 3, format = "0")
	private String suffix;

	public ZipCode() {
		super();
	}

	public ZipCode(String prefix, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public ZipCode(String zipCodeFull) {
		this.prefix = zipCodeFull.substring(0, 5);
		this.suffix = zipCodeFull.substring(5, 8);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
