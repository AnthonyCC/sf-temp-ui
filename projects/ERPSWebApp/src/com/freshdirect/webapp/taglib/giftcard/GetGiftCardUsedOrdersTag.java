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

public class GetGiftCardUsedOrdersTag extends AbstractGetterTag{
	
	private String certNum = null;

	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}
	
	protected Object getResult() throws FDResourceException {
		List dlvPassActivities = null;
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);		
		Object list;
		if(certNum!=null && certNum.trim().length()>0) {
		   list  = FDCustomerManager.getGiftCardRedemedOrders(certNum);
			return list;
		}
		
		return Collections.EMPTY_LIST;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List";
		}
	}
}
