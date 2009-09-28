package com.freshdirect.webapp.taglib.giftcard;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GetDeletedGiftCardTag extends AbstractGetterTag{
	
	protected Object getResult() throws FDResourceException {
		List dlvPassActivities = null;
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		List giftCardRecipients = FDCustomerManager.getDeletedGiftCardsForCustomer(user.getIdentity());
		System.out.println("get deleted gift cards :"+giftCardRecipients);
		return giftCardRecipients;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List";
		}
	}
}
