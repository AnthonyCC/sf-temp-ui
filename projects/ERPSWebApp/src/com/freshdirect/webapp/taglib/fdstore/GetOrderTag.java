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
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision:9$
 * @author $Author:Viktor Szathmary$
 */
public class GetOrderTag extends AbstractGetterTag<FDOrderI> {
	private static final long serialVersionUID = 22271992840932002L;

	private String saleId = null;
	private boolean crm;

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public void setCrm(boolean crm) {
		this.crm = crm;
	}

	protected FDOrderI getResult() throws FDResourceException {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		
		if (user != null && user.getIdentity()!=null) {
			return crm ? FDCustomerManager.getOrderForCRM(user.getIdentity(), saleId) : FDCustomerManager.getOrder(user.getIdentity(), saleId);	
		} else {
			return crm ? FDCustomerManager.getOrderForCRM(saleId) : FDCustomerManager.getOrder(saleId);
		}
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return FDOrderI.class.getCanonicalName();
		}

	}

}
