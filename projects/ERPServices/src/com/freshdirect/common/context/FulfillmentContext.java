package com.freshdirect.common.context;

import java.io.Serializable;

import com.freshdirect.framework.util.StringUtil;

public class FulfillmentContext implements Serializable {
	
	private static final long serialVersionUID = -5918599113189069637L;

	private static final String DEFAULT_PLANT="1000";

	private boolean alcoholRestricted;
	private String plantId;
	private String salesOrg;
	private String distChannel;
	
	public String getPlantId() {
		return StringUtil.isEmpty(plantId)?DEFAULT_PLANT:plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public static FulfillmentContext createDefault() {
		return new FulfillmentContext();
	}
	
	public boolean isAlcoholRestricted() {
		return alcoholRestricted;
	}

	public void setAlcoholRestricted(boolean alcoholRestricted) {
		this.alcoholRestricted = alcoholRestricted;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public String getDistChannel() {
		return distChannel;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	public void setDistChannel(String distChannel) {
		this.distChannel = distChannel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (alcoholRestricted ? 1231 : 1237);
		result = prime * result + ((plantId == null) ? 0 : plantId.hashCode());
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
		FulfillmentContext other = (FulfillmentContext) obj;
		if (alcoholRestricted != other.alcoholRestricted)
			return false;
		if (plantId == null) {
			if (other.plantId != null)
				return false;
		} else if (!plantId.equals(other.plantId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FulfillmentContext [alcoholRestricted=" + alcoholRestricted
				+ ", plantId=" + plantId + "]";
	}
	
}
