package com.freshdirect.transadmin.model;
import java.util.Set;

public class TrnRegion  implements java.io.Serializable, TrnBaseEntityI {
	
	private String code;
	private String name;
	private String description;
	//private Set<TrnArea> areas;
	private String isDepot;
	private String isNew;
	
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
	public String getIsDepot() {
		return isDepot;
	}

	public void setIsDepot(String isDepot) {
		this.isDepot = isDepot;
	}
	
	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TrnRegion other = (TrnRegion) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	/*public Set<TrnArea> getAreas() {
		return areas;
	}

	public void setAreas(Set<TrnArea> areas) {
		this.areas = areas;
	}*/

}
