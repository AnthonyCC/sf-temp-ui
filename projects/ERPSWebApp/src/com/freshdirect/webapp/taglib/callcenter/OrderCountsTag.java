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
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class OrderCountsTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance( OrderCountsTag.class );

	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		int totalOrders = 0;
		int webOrders = 0;
		int phoneOrders = 0;

		try {

			totalOrders = user.getOrderHistory().getValidOrderCount();
			phoneOrders = user.getValidPhoneOrderCount();
			webOrders = totalOrders - phoneOrders;

			pageContext.setAttribute("totalOrders", new Integer(totalOrders));
			pageContext.setAttribute("webOrders", new Integer(webOrders));
			pageContext.setAttribute("phoneOrders", new Integer(phoneOrders));

			return EVAL_BODY_BUFFERED;

		} catch (FDResourceException ex) {
			LOGGER.warn("Unable to retrieve order counts", ex);
			throw new JspException("Unable to retrieve order counts "+ex.getMessage());
		}
	}


}
