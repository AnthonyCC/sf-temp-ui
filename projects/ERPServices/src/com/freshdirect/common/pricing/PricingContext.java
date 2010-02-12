package com.freshdirect.common.pricing;

import java.io.Serializable;

import com.freshdirect.fdstore.ZonePriceListing;

public class PricingContext implements Serializable {
	private static final long serialVersionUID = -952611223130736556L;

	private final String pZoneId;
	
	public static final PricingContext DEFAULT = new PricingContext(ZonePriceListing.MASTER_DEFAULT_ZONE);

	public PricingContext(String pZoneId) {
		this.pZoneId = pZoneId;
	}

	public String getZoneId() {
		return this.pZoneId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pZoneId == null) ? 0 : pZoneId.hashCode());
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
		final PricingContext other = (PricingContext) obj;
		if (pZoneId == null) {
			if (other.pZoneId != null)
				return false;
		} else if (!pZoneId.equals(other.pZoneId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PricingContext[" + pZoneId + "]";
	}
}
