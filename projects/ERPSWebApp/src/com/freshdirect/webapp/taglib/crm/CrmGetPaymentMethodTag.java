package com.freshdirect.webapp.taglib.crm;

import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetPaymentMethodTag extends AbstractGetterTag {
	
	private String paymentId;
	private FDUserI user;
	
	public void setPaymentId(String paymentId){
		this.paymentId = paymentId;
	}
	
	public void setUser(FDUserI user){
		this.user = user;
	}

	protected Object getResult() throws Exception {
		ErpPaymentMethodI paymentMethod = FDCustomerManager.getPaymentMethod(this.user.getIdentity(), this.paymentId);
		if(paymentMethod == null){
			throw new JspException("No payment method found for id: "+this.paymentId);
		}
		return paymentMethod;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.customer.ErpPaymentMethodI";
		}		
	}
}
