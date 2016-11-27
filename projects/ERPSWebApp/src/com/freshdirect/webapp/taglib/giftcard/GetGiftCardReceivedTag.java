package com.freshdirect.webapp.taglib.giftcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GetGiftCardReceivedTag extends AbstractGetterTag{
	public static final int GC_RECEIVED_DISPLAY_LIMIT = 5;

    boolean showAll = false;
    
    public boolean getShowAll() {
    	return this.showAll;
    }
    
    public void setShowAll(String val) {
    	if (val.equalsIgnoreCase("true")) {
    		this.showAll = true;
    	}else{
    		this.showAll = false;
    	}
    }
	
	protected Object getResult() throws FDResourceException {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		FDGiftCardInfoList infoList = user.getGiftCardList();
		if(infoList == null){
			return Collections.EMPTY_LIST;
		}
		List giftCards = new ArrayList();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		if (getShowAll() || (request.getParameter("showAllReceived") != null && request.getParameter("showAllReceived").equals("true")) ) {
			setShowAll("true");
		}
		if(showAll) {
			giftCards.addAll(infoList.getGiftcards());
		} else {
			//show only 5
			int count =0;
			for(Iterator it= infoList.getGiftcards().iterator(); it.hasNext();){
				if(count >= GC_RECEIVED_DISPLAY_LIMIT) break;
				giftCards.add(it.next());
				count++;
			}
		}
		
		return giftCards;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List";
		}
	}
}
