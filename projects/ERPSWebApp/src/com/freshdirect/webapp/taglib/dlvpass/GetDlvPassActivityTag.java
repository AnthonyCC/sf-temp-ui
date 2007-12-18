/*
 * 
 * GetOrderByStatusTag.java
 * Date: Nov 13, 2002 Time: 12:42:09 PM
 */
package com.freshdirect.webapp.taglib.dlvpass;

/**
 * 
 * @author skrishnasamy
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public class GetDlvPassActivityTag extends AbstractGetterTag {
	//Optional Attribute
	private String deliveryPassId;


	public void setDeliveryPassId(String dlvPassId) {
		this.deliveryPassId = dlvPassId;
	}
	
	/** Sorts orders by dlv. start time, descending */
	private final static Comparator DLV_PASS_ACTIVITY_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((ErpActivityRecord) o2).getDate().compareTo(((ErpActivityRecord) o1).getDate());
		}
	};

	protected Object getResult() throws FDResourceException {
		List dlvPassActivities = null;
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		FDIdentity identity  = user.getIdentity();
		ErpActivityRecord template = new ErpActivityRecord();
		template.setCustomerId(user.getIdentity().getErpCustomerPK());
		//template.setActivityType(EnumAccountActivityType.EDIT_DLV_PASS);
		if(deliveryPassId != null){
			/*
			 * Is null if delivery pass activities are at customer level.
			 * Not Null if delivery pass activities are for a particular delivery 
			 * pass.
			 */
			template.setDeliveryPassId(deliveryPassId);
		}
		dlvPassActivities = (List) ActivityLog.getInstance().findActivityByTemplate(template);
		Collections.sort(dlvPassActivities, DLV_PASS_ACTIVITY_COMPARATOR);
 
		return dlvPassActivities;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List";
		}

	}


}
