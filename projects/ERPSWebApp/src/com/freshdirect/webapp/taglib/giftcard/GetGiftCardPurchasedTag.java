package com.freshdirect.webapp.taglib.giftcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GetGiftCardPurchasedTag extends AbstractGetterTag{

	

	private String saleId = null;

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}
	
	public static final int GC_PURCHASED_DISPLAY_LIMIT = 10;
	
	public final static Comparator GIFT_CARD_PURCHASED_COMPARATOR = new Comparator() {

	public int compare(Object o1, Object o2) {

		ErpGCDlvInformationHolder p1 = (ErpGCDlvInformationHolder) o1;
		ErpGCDlvInformationHolder p2 = (ErpGCDlvInformationHolder) o2;

		int ret = p2.getPurchaseDate().compareTo(p1.getPurchaseDate());
		return ret;
	}
};

	protected Object getResult() throws FDResourceException {
		
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		List giftCardRecipients = null;
		if(null != saleId && !"".equalsIgnoreCase(saleId)){
			giftCardRecipients = FDCustomerManager.getGiftCardRecepientsForOrder(saleId);
		}else{
			giftCardRecipients = FDCustomerManager.getGiftCardRecepientsForCustomer(user.getIdentity());
		}
		if(giftCardRecipients == null){
			return Collections.EMPTY_LIST;
		}
		Collections.sort(giftCardRecipients, GIFT_CARD_PURCHASED_COMPARATOR);
		List recipients = new ArrayList();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		if(request.getParameter("showAllPurchased") != null && request.getParameter("showAllPurchased").equals("true")) {
			recipients.addAll(giftCardRecipients);
		} else {
			//show only 10
			int count =0;
			for(Iterator it= giftCardRecipients.iterator(); it.hasNext();){
				if(count >= GC_PURCHASED_DISPLAY_LIMIT) break;
				recipients.add(it.next());
				count++;
			}
		}
		return recipients;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List";
		}
	}
}
