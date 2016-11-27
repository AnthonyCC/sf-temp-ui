package com.freshdirect.routing.model;


public class RegionModel extends BaseModel implements IRegionModel  {
	
	private String regionCode;
	
	private boolean isDepot;
	private String name;
	private String description;
	
	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	
	

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((regionCode == null) ? 0 : regionCode.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RegionModel other = (RegionModel) obj;
		if (regionCode == null) {
			if (other.regionCode != null)
				return false;
		} else if (!regionCode.equals(other.regionCode))
			return false;
		return true;
	}

	
	public boolean isDepot() {
		return isDepot;
	}

	public void setDepot(boolean isDepot) {
		this.isDepot = isDepot;
	}
	
	public String toString() {
		return "regionCode "+regionCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
