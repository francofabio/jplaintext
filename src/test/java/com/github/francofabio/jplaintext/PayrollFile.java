package com.github.francofabio.jplaintext;

import java.util.ArrayList;
import java.util.List;

public class PayrollFile {

	private PayrollHeader header;
	private List<PayrollDetail> details;
	private PayrollTrailer trailer;

	public PayrollFile() {
		this.details = new ArrayList<PayrollDetail>();
	}

	public List<PayrollDetail> getDetails() {
		return details;
	}

	public PayrollHeader getHeader() {
		return header;
	}

	public void setHeader(PayrollHeader header) {
		this.header = header;
	}

	public PayrollTrailer getTrailer() {
		return trailer;
	}

	public void setTrailer(PayrollTrailer trailer) {
		this.trailer = trailer;
	}
	
	public void setupTrailer(PayrollTrailer trailer) {
		setTrailer(trailer);
	}

}
