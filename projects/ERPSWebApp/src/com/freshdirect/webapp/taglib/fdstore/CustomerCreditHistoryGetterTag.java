package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerCreditHistoryModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;




/**@author ekracoff*/
public class CustomerCreditHistoryGetterTag extends AbstractGetterTag implements SessionName {
	
	protected Object getResult() throws FDResourceException {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(USER);
		FDCustomerCreditHistoryModel creditHistory = FDCustomerManager.getCreditHistory(user.getIdentity());
		
		
		if (creditHistory == null) {
			throw new FDResourceException("No Credit History");
		}
		return creditHistory;
	}


	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.fdstore.customer.FDCustomerCreditHistoryModel";
		}

	}

}
