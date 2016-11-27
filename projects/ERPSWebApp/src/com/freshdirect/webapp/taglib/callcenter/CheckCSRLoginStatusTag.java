/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.callcenter;

import java.io.IOException;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.apache.log4j.*;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.CallcenterUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class CheckCSRLoginStatusTag extends com.freshdirect.framework.webapp.TagSupport implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance( CheckCSRLoginStatusTag.class );

	private String redirectPage 		= null;
	private Boolean supervisorRequired 	= null;

	public void setRedirectPage(String redirectPage){
		this.redirectPage = redirectPage;
	}

	public void setSupervisorRequired(Boolean b) {
		this.supervisorRequired = b;
	}

	public int doEndTag() throws JspException {
		HttpSession session = pageContext.getSession();
		CallcenterUser ccUser = (CallcenterUser) session.getAttribute(SessionName.CUSTOMER_SERVICE_REP);
		//
		// Check for CSR in session
		//
		if ( ccUser == null || ccUser.getId() == null || "".equals(ccUser.getId().trim()) ) {
			LOGGER.debug("No CSR in session, so redirecting to: " + this.redirectPage);
			//
			// redirect
			//
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			try {
				response.sendRedirect(response.encodeRedirectURL( this.redirectPage ));
				JspWriter writer = pageContext.getOut();
				writer.close();
				return SKIP_PAGE;
			} catch (IOException ioe) {
				throw new JspException(ioe.getMessage());
			}
		}
		//
		// Check if this is a restricted-access page
		//
		if (supervisorRequired != null && supervisorRequired.booleanValue() && !ccUser.isSupervisor()) {
			LOGGER.debug("Must be a SUPERVISOR or higher to access this page.");
			//
			// redirect
			//
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			try {
				response.sendRedirect(response.encodeRedirectURL("/main/error_insufficient_rights.jsp"));
				JspWriter writer = pageContext.getOut();
				writer.close();
				return SKIP_PAGE;
			} catch (IOException ioe) {
				throw new JspException(ioe.getMessage());
			}
		}
		return EVAL_PAGE;
	}

}