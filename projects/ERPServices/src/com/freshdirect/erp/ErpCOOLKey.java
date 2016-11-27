package com.freshdirect.erp;

import java.io.Serializable;

public class ErpCOOLKey implements Serializable {	
	
	private static final long	serialVersionUID	= -1125488725688833250L;
	
	private String sapID;
	private String plantId;
	
	public ErpCOOLKey(String sapID, String plantId)
	{
		super();
		this.sapID = sapID;
		this.plantId = plantId;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((sapID == null) ? 0 : sapID.hashCode());
		result = PRIME * result + ((plantId == null) ? 0 : plantId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ErpCOOLKey other = (ErpCOOLKey) obj;
		if (sapID == null) {
			if (other.sapID != null)
				return false;
		} else if (!sapID.equals(other.sapID))
			return false;
		if (plantId == null) {
			if (other.plantId != null)
				return false;
		} else if (!plantId.equals(other.plantId))
			return false;
		return true;
	}

	/**
	 * @return the plantId
	 */
	public String getSapID() {
		return sapID;
	}
		
	/**
	 * @return the plantId
	 */
	public String getPlantId() {
		return plantId;
	}
	
	public String toString() {
		return new StringBuffer(300).append("ErpCOOLInfo[sapID: ")
			.append( this.sapID)
			.append(" plantId: ")
			.append(plantId)
			.append("]").toString();
	}

}
