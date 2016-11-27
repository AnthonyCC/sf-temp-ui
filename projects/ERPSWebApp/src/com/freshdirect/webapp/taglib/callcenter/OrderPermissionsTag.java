package com.freshdirect.webapp.taglib.callcenter;

/**
 * 
 * OrderActivityPermissionTag.java
 * Created Nov 27, 2002
 */

/**
 *
 *  @author knadeem
 */
import javax.servlet.http.HttpSession;

import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.OrderPermissionsImpl;

public class OrderPermissionsTag extends AbstractGetterTag {
	
	private String orderId;
	
	public void setOrderId(String orderId){
		this.orderId = orderId;
	}
	
	protected Object getResult() throws FDResourceException {
		
		HttpSession session = pageContext.getSession();
		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDIdentity identity = currentUser.getIdentity();
		
		FDOrderI order = FDCustomerManager.getOrder(identity, this.orderId);
		String application = (String) session.getAttribute(SessionName.APPLICATION);
		
		boolean makeGood = EnumPaymentType.MAKE_GOOD.equals(order.getPaymentMethod().getPaymentType()); 
		return new OrderPermissionsImpl(order.getOrderStatus(), application, makeGood, order.hasCreditIssued());
		
	}
	
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.webapp.util.OrderPermissionsI";
		}

	}
}
