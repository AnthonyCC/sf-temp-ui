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
import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public class GetRecentOrdersByDlvPassTag extends AbstractGetterTag {
	
	String deliveryPassId = null;
	
	public void setDeliveryPassId(String deliveryPassId) {
		this.deliveryPassId = deliveryPassId;
	}
	
	protected Object getResult() throws FDResourceException {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		FDIdentity identity = user.getIdentity();
		//Orders that are 7 days old 
		List recentOrders = FDCustomerManager.getRecentOrdersByDlvPassId(identity, deliveryPassId, 7);
		return recentOrders;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List";
		}

	}

}
