package com.freshdirect.webapp.ajax.browse;

public enum FilteringFlowType {
	BROWSE, SEARCH, NEWPRODUCTS, ECOUPON, PRES_PICKS, STAFF_PICKS;
	
	public boolean isSearchLike() {
		return this != BROWSE;
	}
}
