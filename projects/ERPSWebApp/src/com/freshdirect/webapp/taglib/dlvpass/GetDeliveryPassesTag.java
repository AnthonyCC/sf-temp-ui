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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.freshdirect.deliverypass.DeliveryPassInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public class GetDeliveryPassesTag extends AbstractGetterTag {
	protected static final String DLV_PASS_PREFIX = "DLVP";
		
	/** Sorts orders by dlv. start time, descending */
	private final static Comparator DLV_PASS_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((DeliveryPassModel) o2).getStatus().compareTo(((DeliveryPassModel) o1).getStatus());
		}
	};

	protected Object getResult() throws FDResourceException {
		Map passInfo = null;
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		String sessionId = (String)session.getAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);
		String identity = user.getIdentity().getErpCustomerPK();
		if(sessionId == null || !(sessionId.equals(identity))){
			//Remove the previous user dlv pass info.
			if(sessionId != null){
				session.removeAttribute(DLV_PASS_PREFIX + sessionId);
				session.removeAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);
			}
			passInfo = FDCustomerManager.getDeliveryPassesInfo(user);
			String key = DLV_PASS_PREFIX + identity;
			session.setAttribute(key, passInfo);
			session.setAttribute(DlvPassConstants.DLV_PASS_SESSION_ID, identity);
		} else {
			//Delivery Pass Info is already in the session. So pick it up.
			passInfo = (Map) session.getAttribute(DLV_PASS_PREFIX + sessionId);
		}
		return passInfo;
	}	

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Map";
		}

	}

}
