package com.github.francofabio.jplaintext;

import java.util.Date;

import com.github.francofabio.jplaintext.annotation.OverrideAttribute;
import com.github.francofabio.jplaintext.annotation.PlainTextField;
import com.github.francofabio.jplaintext.annotation.PlainTextFieldArgument;
import com.github.francofabio.jplaintext.annotation.PlainTextFieldEmbedded;
import com.github.francofabio.jplaintext.annotation.PlainTextOverrideAttributes;
import com.github.francofabio.jplaintext.annotation.PlainTextRecord;

@PlainTextRecord(size = 389)
public class Developer extends Employee {
	@PlainTextField(order = 1, size = 36)
	private String uid;

	@PlainTextFieldEmbedded(order = 2)
	private DeveloperLanguage developerLanguage;

	@PlainTextField(order = 3, size = 100)
	private String blog;

	@PlainTextField(order = 4, size = 10, arguments = @PlainTextFieldArgument(name = "property", value = "description"))
	private LanguageType languageType;

	@PlainTextFieldEmbedded(order = 5)
	@PlainTextOverrideAttributes(@OverrideAttribute(name = "street", field = @PlainTextField(order = 1,
		size = 35,
		field = "Street",
		format = "")))
	private Address address;

	public Developer() {
		super();
	}

	public Developer(String uid, String name, String organization, Double salary, Date dateOfBirth,
			DeveloperLanguage developerLanguage, String blog) {
		super(name, organization, salary, dateOfBirth);
		this.uid = uid;
		this.developerLanguage = developerLanguage;
		this.blog = blog;
	}

	public String getUid() {
		return uid;
	}

	public DeveloperLanguage getDeveloperLanguage() {
		return developerLanguage;
	}

	public void setDeveloperLanguage(DeveloperLanguage developerLanguage) {
		this.developerLanguage = developerLanguage;
	}

	public String getBlog() {
		return blog;
	}

	public LanguageType getLanguageType() {
		return languageType;
	}

	public void setLanguageType(LanguageType languageType) {
		this.languageType = languageType;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
