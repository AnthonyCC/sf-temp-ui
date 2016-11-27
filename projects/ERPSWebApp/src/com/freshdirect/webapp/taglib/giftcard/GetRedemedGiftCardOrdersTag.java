package com.freshdirect.webapp.taglib.giftcard;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GetRedemedGiftCardOrdersTag extends AbstractGetterTag{
	
	String certNum;
	
	
	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}

	protected Object getResult() throws FDResourceException {
		List dlvPassActivities = null;
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);		
		
		//certNum=pageContext.getRequest().getParameter("certNum");
		if(certNum!=null && certNum.trim().length()>0)
		   return FDCustomerManager.getGiftCardRedemedOrders(user.getIdentity(),certNum);
		
		List giftCardRecipients = FDCustomerManager.getGiftCardOrdersForCustomer(user.getIdentity());
		return giftCardRecipients;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List";
		}
	}
}
