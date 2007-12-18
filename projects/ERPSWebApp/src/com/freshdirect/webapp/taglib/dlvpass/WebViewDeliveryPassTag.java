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
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.deliverypass.WebDeliveryPassView;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public class WebViewDeliveryPassTag extends AbstractGetterTag {

	protected Object getResult() throws FDResourceException {
		WebDeliveryPassView view = null;
		HttpSession session = pageContext.getSession();
		FDSessionUser currentUser = (FDSessionUser)session.getAttribute(SessionName.USER);
		Object _pass=currentUser.getDlvPassInfo();
		String purchaseOrderId="";
		if(_pass!=null) {
			purchaseOrderId=((FDUserDlvPassInfo)_pass).getOriginalOrderId();
			
		}
		//Reload the DP status from Database to make the session and DB are in sync.
		currentUser.updateDlvPassInfo();
		if(DeliveryPassUtil.isDlvPassExistsStatus(currentUser.getDeliveryPassStatus())) {
			//Get the delivery pass info from the database.
			List dlvPasses = FDCustomerManager.getDeliveryPassesByStatus(currentUser.getIdentity(), 
																currentUser.getDeliveryPassStatus());
			if(dlvPasses == null || ((dlvPasses!=null) && dlvPasses.size() == 0)){
				throw new FDResourceException("We are unable to load the DeliveryPass information due to " +
						"technical difficulty. Please try again later.");
			}
			DeliveryPassModel model =null;
			boolean found=false;
			int count=0;
			for(int i=0;i<dlvPasses.size();i++) {
				model = (DeliveryPassModel) dlvPasses.get(i);
				if(purchaseOrderId.equals(model.getPurchaseOrderId())) {
					view = new WebDeliveryPassView(model);
					break;
				}
			}
			
			
		}
			//If dlv pass status is NONE, CANCELLED or EXPIRED.
		if(view==null) {
			view =  new WebDeliveryPassView();
		}
		
		return view;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.fdstore.deliverypass.WebDeliveryPassView";
		}

	}


}
