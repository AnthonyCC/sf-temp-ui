package com.freshdirect.transadmin.model;

public class TrnFacilityType implements java.io.Serializable {

	private String name;	
	private String description;	
	
	public TrnFacilityType() {
	}

	public TrnFacilityType(String name, String description) {
		this.name = name;
		this.description = description;	
	}
		
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		TrnFacilityType other = (TrnFacilityType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public String toString(){
		return this.name;
	}
	
}
