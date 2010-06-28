package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

public class DlvServiceTimeType implements java.io.Serializable, TrnBaseEntityI {
	
	private String code;
	private String name;
	private String description;
	private BigDecimal fixedServiceTime;
	private BigDecimal variableServiceTime;
	
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
	
	public BigDecimal getFixedServiceTime() {
		return fixedServiceTime;
	}

	public void setFixedServiceTime(BigDecimal fixedServiceTime) {
		this.fixedServiceTime = fixedServiceTime;
	}

	public BigDecimal getVariableServiceTime() {
		return variableServiceTime;
	}

	public void setVariableServiceTime(BigDecimal variableServiceTime) {
		this.variableServiceTime = variableServiceTime;
	}
	
	public boolean isObsoleteEntity() {
		return false;
	}
	
}
