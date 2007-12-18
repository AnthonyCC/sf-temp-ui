/*
 * $Workfile:GetOrderTag.java$
 *
 * $Date:8/13/2003 6:40:45 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision:9$
 * @author $Author:Viktor Szathmary$
 */
public class GetOrderTag extends AbstractGetterTag {

	private String saleId = null;

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	protected Object getResult() throws FDResourceException {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		
		if (user != null && user.getIdentity()!=null) {
			return FDCustomerManager.getOrder(user.getIdentity(), saleId);
			
		}else{
			return FDCustomerManager.getOrder(saleId);
		}
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.fdstore.customer.FDOrderI";
		}

	}

}
