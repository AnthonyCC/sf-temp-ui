package com.freshdirect.common.context;

import java.io.Serializable;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.customer.FDIdentity;

public class UserContext implements Serializable {

	private static final long serialVersionUID = -2523227566414843459L;

	private PricingContext pricingContext;
	private FDIdentity fdIdentity;
	private StoreContext storeContext;
	private FulfillmentContext fulfillmentContext;

	public static UserContext createDefault(){
		UserContext userContext = new UserContext();
		userContext.setPricingContext(PricingContext.DEFAULT);
		userContext.setStoreContext(StoreContext.createDefault());
		userContext.setFulfillmentContext(FulfillmentContext.createDefault());
		//default FDIdentity is null
		return userContext;
	}
	
	public PricingContext getPricingContext() {
		return pricingContext;
	}
	public void setPricingContext(PricingContext pricingContext) {
		this.pricingContext = pricingContext;
	}
	
	public void resetPricingContext(){
		this.setPricingContext(null);
	}
	
	//Added for Junit testing.
	public void setDefaultPricingContext() {
		this.setPricingContext(PricingContext.DEFAULT);
	}
	
	public FDIdentity getFdIdentity() {
		return fdIdentity;
	}
	public void setFdIdentity(FDIdentity fdIdentity) {
		this.fdIdentity = fdIdentity;
	}
	public StoreContext getStoreContext() {
		return storeContext;
	}
	public void setStoreContext(StoreContext storeContext) {
		this.storeContext = storeContext;
	}
	public FulfillmentContext getFulfillmentContext() {
		return fulfillmentContext;
	}
	public void setFulfillmentContext(FulfillmentContext fulfillmentContext) {
		this.fulfillmentContext = fulfillmentContext;
	}
	
}
