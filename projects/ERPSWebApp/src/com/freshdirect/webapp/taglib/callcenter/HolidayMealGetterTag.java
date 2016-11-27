/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class HolidayMealGetterTag extends AbstractGetterTag {

	protected Object getResult() throws FDResourceException {
        HttpSession session = pageContext.getSession();
        FDIdentity identity = ((FDUserI)session.getAttribute(SessionName.USER)).getIdentity();
        return CallCenterServices.getHolidayMeals(identity);
    }


	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List";
		}

	}

}
