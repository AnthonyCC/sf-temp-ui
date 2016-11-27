package com.freshdirect.customer;

public enum EnumExternalLoginSource {

	SOCIAL("S"), DEVICE("D");
	private String value;
	 
	private EnumExternalLoginSource(String value) {
		this.value = value;
	}
	
	public String value(){
		return value;
	}
}
