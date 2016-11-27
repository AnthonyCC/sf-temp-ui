/*
 * Created on May 1, 2003
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

/**
 * @author knadeem
 */
public class HealthWarningControllerTag extends AbstractControllerTag {

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException { 
		HttpSession session = (HttpSession)pageContext.getSession();
		FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
		user.setHealthWarningAcknowledged(true);
		session.setAttribute(SessionName.USER, user);
		return true;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}
