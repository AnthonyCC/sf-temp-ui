package com.freshdirect.webapp.ajax.backoffice.data;

import java.io.Serializable;

public class DomainValueModelResponse implements Serializable{
	
	private String label;

	private String value;

	private String domainEncoded;

	private String theValue;

	private String domainName;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDomainEncoded() {
		return domainEncoded;
	}

	public void setDomainEncoded(String domainEncoded) {
		this.domainEncoded = domainEncoded;
	}

	public String getTheValue() {
		return theValue;
	}

	public void setTheValue(String theValue) {
		this.theValue = theValue;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

}
