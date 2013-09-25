/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.quickshop.QuickShopHelper;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class OrderHistoryInfoTag extends AbstractGetterTag {

	private QuickShopHelper helper;

	protected Object getResult() throws FDResourceException {

		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

		return getHelper().getOrderHistoryInfo(user, false);
	}
	
	private QuickShopHelper getHelper(){
		if(helper==null){
			helper = new QuickShopHelper();
		}
		return helper;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection<com.freshdirect.fdstore.customer.FDOrderInfoI>";
		}

	}

}
