package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetAddressTag extends AbstractGetterTag {
	
	private String addressId;
	private FDUserI user;
	
	public void setAddressId(String addressId){
		this.addressId = addressId;
	}
	
	public void setUser(FDUserI user){
		this.user = user;
	}

	protected Object getResult() throws Exception {
		return FDCustomerManager.getShipToAddress(this.user.getIdentity(), this.addressId); 
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.customer.ErpAddressModel";
		}
	}

}
