package com.freshdirect.mobileapi.controller.data;

public class Lookup {
	
	private String code;
	
	private String name;
	
	private String selectedLabel;
	
	public String getSelectedLabel() {
		return selectedLabel;
	}
	public void setSelectedLabel(String selectedLabel) {
		this.selectedLabel = selectedLabel;
	}
	public Lookup(String code, String name, String selectedLabel) {
		super();
		this.code = code;
		this.name = name;
	}
	
	public Lookup(String code, String name){
		this(code, name, null);
	}
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
}
