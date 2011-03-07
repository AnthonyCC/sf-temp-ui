package com.freshdirect.common.pricing;

import java.io.Serializable;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PricingContext implements Serializable {
	
	private static final long serialVersionUID = -952611223130736556L;
	
	private final static Category LOGGER = LoggerFactory.getInstance(PricingContext.class);
	

	private final String pZoneId;
	public double  getGrpDiscountScale() {
		return discountScale;
	}
		
	private double discountScale;
	
	public static final PricingContext DEFAULT = new PricingContext(ZonePriceListing.MASTER_DEFAULT_ZONE);

	public PricingContext(String pZoneId) {
	    if (pZoneId == null) {
	    	// using default if null, passing an exception to have a stacktrace
	    	LOGGER.warn( "PricingContext initialized with null! Using default master zone instead.", new NullPointerException("pZoneId") );
	    	pZoneId = ZonePriceListing.MASTER_DEFAULT_ZONE;
	    }
	    this.pZoneId = pZoneId;
	}

	public PricingContext(String pZoneId,double discountScale) {
		this.discountScale = discountScale;
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
	
	public EnumServiceType getServiceType() {
		if(pZoneId != null && pZoneId.length()>= 10){
			String serviceTypeCode = pZoneId.substring(8); //00 0r 01 0r 02
			if(serviceTypeCode.equals("01"))
				return EnumServiceType.HOME;
			else if(serviceTypeCode.equals("02"))
				return EnumServiceType.CORPORATE;
			else
				return EnumServiceType.PICKUP;
		}
		return null;
	}
}
