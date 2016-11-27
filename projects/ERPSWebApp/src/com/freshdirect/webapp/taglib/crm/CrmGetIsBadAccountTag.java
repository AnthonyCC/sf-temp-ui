/*
 * Created on Mar 17, 2005
 *
 */
package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.payment.fraud.PaymentFraudManager;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 * @author jng
 *
 */
public class CrmGetIsBadAccountTag extends AbstractGetterTag {

	private ErpPaymentMethodI paymentMethod;
	
	public void setPaymentMethod(ErpPaymentMethodI paymentMethod){
		this.paymentMethod = paymentMethod;
	}
	
	protected Object getResult() throws Exception {
		if (PaymentFraudManager.checkBadAccount(paymentMethod, false)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.lang.Boolean";
		}		
	}

}
