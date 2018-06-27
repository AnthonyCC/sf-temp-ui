package com.freshdirect.framework.mail;

import java.io.Serializable;

public class EmailAddress implements Serializable {

	private static final long serialVersionUID = 1825045149568219025L;
	private String name;
	private String address;
	
	public EmailAddress() {
		
	}
	public EmailAddress(String name, String address) {
		this.name = name;
		this.address = address;	
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}

}
