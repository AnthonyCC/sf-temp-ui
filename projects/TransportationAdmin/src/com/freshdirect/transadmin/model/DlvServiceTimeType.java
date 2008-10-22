package com.freshdirect.transadmin.model;

public class DlvServiceTimeType implements java.io.Serializable, TrnBaseEntityI {
	
	private String code;
	private String name;
	private String description;
	
	private String isNew;

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
