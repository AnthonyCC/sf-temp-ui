package com.freshdirect.transadmin.model;

public class TrnDeliveryModel  implements java.io.Serializable, TrnBaseEntityI {
	
	private String code;
	private String name;
		

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isObsoleteEntity() {
		return false;
	}

	
}
