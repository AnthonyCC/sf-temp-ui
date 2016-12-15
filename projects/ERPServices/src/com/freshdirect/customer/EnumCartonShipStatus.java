package com.freshdirect.customer;

public enum EnumCartonShipStatus {

	SHORTSHIP ("SS", "Short Ship"), SUBSTITUTE("SB", "Substituted"), SUBSHORT("SH", "Short Shipped & Substituted");
	
	EnumCartonShipStatus(String code, String description){
		this.code = code;
		this.description = description;
	}
	private final String code;
	private final String description;
	
	public String getCode(){
		return code;
	}
	public String getDescription(){
		return description;
	}

}
