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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DlvServiceTimeType other = (DlvServiceTimeType) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
