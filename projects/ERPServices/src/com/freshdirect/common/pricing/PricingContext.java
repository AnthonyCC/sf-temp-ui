package com.freshdirect.common.pricing;

import java.io.Serializable;

import org.apache.log4j.Category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PricingContext implements Serializable {
	
	private static final long serialVersionUID = -952611223130736556L;
	
	private final static Category LOGGER = LoggerFactory.getInstance(PricingContext.class);

	//private final String pZoneId;
	//TODO SalesArea comes here
	private final ZoneInfo pricingZone;

	@Deprecated
	public static final PricingContext DEFAULT = new PricingContext(ZonePriceListing.DEFAULT_ZONE_INFO);
	@Deprecated
	public static final PricingContext FDX_DEFAULT = new PricingContext(ZonePriceListing.DEFAULT_FDX_ZONE_INFO);

	@JsonCreator
	public PricingContext(@JsonProperty("zoneInfo") ZoneInfo pricingZone) {
	    if (pricingZone == null) {
	    	// using default if null, passing an exception to have a stacktrace
	    	LOGGER.warn( "PricingContext initialized with null! Using default master zone instead.", new NullPointerException("pZoneId") );
	    	pricingZone = ZonePriceListing.DEFAULT_ZONE_INFO;
	    }
	    this.pricingZone = pricingZone;
	}
	
	public ZoneInfo getZoneInfo() {
		return this.pricingZone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pricingZone == null) ? 0 : pricingZone.hashCode());
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
		if (pricingZone == null) {
			if (other.pricingZone != null)
				return false;
		} else if (!pricingZone.equals(other.pricingZone))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PricingContext[" + pricingZone + "]";
	}
	
	public EnumServiceType getServiceType() {
		if(pricingZone != null && pricingZone.getPricingZoneId()!=null && pricingZone.getPricingZoneId().length()>= 10){
			String serviceTypeCode = pricingZone.getPricingZoneId().substring(8); //00 0r 01 0r 02
			if(serviceTypeCode.equals("01"))
				return EnumServiceType.HOME;
			else if(serviceTypeCode.equals("02"))
				return EnumServiceType.CORPORATE;
			else
				return EnumServiceType.PICKUP;
		}
		return null;
	}

    public static PricingContext createPricingContext(EnumEStoreId eStore) {
        if (EnumEStoreId.FDX == eStore){
            return FDX_DEFAULT;
        }
        return DEFAULT;
    }
}
