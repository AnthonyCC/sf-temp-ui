package com.freshdirect.routing.model;

public class TrnFacilityType implements java.io.Serializable {

	private String facilityTypeId;
	private String name;	
	private String description;
	private int trailerMax;
	
	public TrnFacilityType() {
	}

	public TrnFacilityType(String name, String description, int trailerMax) {
		this.name = name;
		this.description = description;
		this.trailerMax = trailerMax;
	}
	
	public int getTrailerMax() {
		return trailerMax;
	}

	public void setTrailerMax(int trailerMax) {
		this.trailerMax = trailerMax;
	}

	public String getFacilityTypeId() {
		return facilityTypeId;
	}

	public void setFacilityTypeId(String facilityTypeId) {
		this.facilityTypeId = facilityTypeId;
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
