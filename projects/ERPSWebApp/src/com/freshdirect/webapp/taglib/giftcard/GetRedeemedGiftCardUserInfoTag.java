package com.freshdirect.webapp.taglib.giftcard;

import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetRedeemedGiftCardUserInfoTag extends AbstractGetterTag {
	
	private String customerId = null;

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	protected Object getResult() throws FDResourceException {
		
		String erpCustEmail = "";
		if(customerId!=null && customerId.trim().length()>0) {
			ErpCustomerInfoModel erpModel = FDCustomerFactory.getErpCustomerInfo(customerId);
			erpCustEmail = erpModel.getEmail();			
		}
		
		return erpCustEmail;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.lang.String";
		}
	}
}
