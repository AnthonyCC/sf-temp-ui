/*
 * 
 * GetOrderByStatusTag.java
 * Date: Nov 13, 2002 Time: 12:42:09 PM
 */
package com.freshdirect.webapp.taglib.dlvpass;

/**
 * 
 * @author knadeem
 */

import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public class GetOrdersByDeliveryPassTag extends AbstractGetterTag {
	
	String deliveryPassId = null;
	
	public void setDeliveryPassId(String deliveryPassId) {
		this.deliveryPassId = deliveryPassId;
	}
	
	protected Object getResult() throws FDResourceException {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		FDIdentity identity = user.getIdentity();
		//Commented as part of PERF-27 task. Avoiding DB hit. Utilizing the order history cache.
		//Collection orderInfos = FDCustomerManager.getOrdersByDlvPassId(identity, deliveryPassId).getFDOrderInfos();
		Collection orderInfos = ((FDOrderHistory) user.getOrderHistory()).getDlvPassOrderInfos(deliveryPassId);
		return orderInfos;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}
