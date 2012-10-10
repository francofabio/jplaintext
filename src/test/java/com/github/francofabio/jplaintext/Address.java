package com.github.francofabio.jplaintext;

import com.github.francofabio.jplaintext.annotation.PlainTextField;
import com.github.francofabio.jplaintext.annotation.PlainTextFieldEmbedded;

public class Address {

	@PlainTextField(order = 1, size = 35)
	private String street;

	@PlainTextField(order = 2, size = 30)
	private String district;

	@PlainTextField(order = 3, size = 30)
	private String city;

	@PlainTextField(order = 4, size = 30)
	private String state;

	@PlainTextFieldEmbedded(order = 5)
	private ZipCode zipCode;

	public Address() {
		super();
	}

	public Address(String street, String district, String city, String state, ZipCode zipCode) {
		super();
		this.street = street;
		this.district = district;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public ZipCode getZipCode() {
		return zipCode;
	}

	public void setZipCode(ZipCode zipCode) {
		this.zipCode = zipCode;
	}

}
