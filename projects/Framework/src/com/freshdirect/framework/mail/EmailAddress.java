package com.freshdirect.framework.mail;

import java.io.Serializable;

public class EmailAddress implements Serializable {

	private final String name;
	private final String address;
	
	public EmailAddress(String name, String address) {
		this.name = name;
		this.address = address;	
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}

}
