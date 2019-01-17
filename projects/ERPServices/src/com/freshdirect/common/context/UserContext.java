package com.freshdirect.common.context;

import java.io.Serializable;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.customer.FDIdentity;

public class UserContext implements Serializable {

    private static final long serialVersionUID = -2523227566414843459L;

    private PricingContext pricingContext;
    private FDIdentity fdIdentity;
    private String custSapId;
    private StoreContext storeContext;
    private FulfillmentContext fulfillmentContext;

    @Deprecated
	public static UserContext createDefault(EnumEStoreId eStore){
		UserContext userContext = new UserContext();
		userContext.setPricingContext(PricingContext.DEFAULT);
		userContext.setStoreContext(StoreContext.createStoreContext(eStore));
		userContext.setFulfillmentContext(FulfillmentContext.createDefault());
		//default FDIdentity is null
		return userContext;
	}
	
    public static UserContext createUserContext(EnumEStoreId eStore) {
        UserContext userContext = new UserContext();
        userContext.setPricingContext(PricingContext.createPricingContext(eStore));
        userContext.setStoreContext(StoreContext.createStoreContext(eStore));
        userContext.setFulfillmentContext(FulfillmentContext.createDefault());
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
	public String getCustSapId() {
		return custSapId;
	}
	public void setCustSapId(String custSapId) {
		this.custSapId = custSapId;
	}

	@Override
	public String toString() {
		return "UserContext [pricingContext=" + pricingContext
				+ ", fdIdentity=" + fdIdentity + ", custSapId=" + custSapId
				+ ", storeContext=" + storeContext + ", fulfillmentContext="
				+ fulfillmentContext + "]";
	}
	
	
}
