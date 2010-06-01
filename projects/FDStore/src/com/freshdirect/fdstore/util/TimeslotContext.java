package com.freshdirect.fdstore.util;

import com.freshdirect.common.pricing.PricingContext;


public class TimeslotContext {
	
	private final String viewName;
	
	public static final TimeslotContext CHECKOUT_TIMESLOTS = new TimeslotContext("CKT TIMESLOTS");
	public static final TimeslotContext CHECK_AVAILABLE_TIMESLOTS = new TimeslotContext("WEB AVLTIMESLOTS");
	public static final TimeslotContext RESERVE_TIMESLOTS = new TimeslotContext("RSV TIMESLOT");
	public static final TimeslotContext CHECK_AVAIL_SLOTS_NO_USER = new TimeslotContext("USR UNRECOGNIZED");
	public static final TimeslotContext CHECK_AVAL_SLOTS_CRM = new TimeslotContext("CRM AVLTIMESLOTS");
	public static final TimeslotContext CHECK_SLOTS_FOR_ADDRESS_CRM = new TimeslotContext("CRM CHECK TIMESLOTS");
	
	public String getViewName() {
		return viewName;
	}
	
	public TimeslotContext(String viewName) {
		this.viewName=viewName;
	}
	
	@Override
	public int hashCode() {
		return 37 * viewName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TimeslotContext other = (TimeslotContext) obj;
		if (viewName == null) {
			if (other.viewName != null)
				return false;
		} else if (!viewName.equals(other.viewName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "TimeslotContext[" + viewName + "]";
	}

	
}	