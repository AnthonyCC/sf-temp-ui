package com.freshdirect.transadmin.datamanager.model;

public class ZoneWorktableModel {
	
	private final String code;
	private final String name;
	private final boolean temp;
	
	public boolean isTemp() {
		return temp;
	}

	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public ZoneWorktableModel(String code, String name, boolean temp) {
		super();
		this.code = code;
		this.name = name;
		this.temp = temp;
	}

}
