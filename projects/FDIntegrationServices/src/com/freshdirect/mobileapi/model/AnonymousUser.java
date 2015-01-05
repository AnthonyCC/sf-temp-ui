package com.freshdirect.mobileapi.model;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.customer.FDUser;

public final class AnonymousUser extends FDUser {
    
	private static final long serialVersionUID = 2464701273755989295L;

	@Override
    public PricingContext getPricingContext() {
        return PricingContext.DEFAULT;
    }
}
