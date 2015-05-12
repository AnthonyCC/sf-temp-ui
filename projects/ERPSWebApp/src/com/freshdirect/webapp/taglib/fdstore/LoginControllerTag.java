/*
 * $Workfile:LoginControllerTag.java$
 *
 * $Date:8/23/2003 7:26:57 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;



import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.util.CaptchaUtil;

/**
 * 
 * 
 * @version $Revision:26$
 * @author $Author:Viktor Szathmary$
 */
public class LoginControllerTag extends AbstractControllerTag {

	private static Category LOGGER = LoggerFactory.getInstance(LoginControllerTag.class);

	private String mergePage;

	public void setMergePage(String mp) {
		this.mergePage = mp;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = request.getSession(true);
		Integer fdLoginAttempt = session.getAttribute("fdLoginAttempt") != null ? (Integer) session.getAttribute("fdLoginAttempt") : Integer.valueOf(0);
		boolean isCaptchaSuccess = true;
		
		if (request.getParameter("captchaEnabled") != null) {
			isCaptchaSuccess = CaptchaUtil.validateCaptcha(request);
			LOGGER.debug("Captcha enabled");
		}
		
		String userId = request.getParameter(EnumUserInfoName.USER_ID.getCode()).trim();
		String password = request.getParameter(EnumUserInfoName.PASSWORD.getCode()).trim();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		String updatedSuccessPage = null;
		if(isCaptchaSuccess){
			updatedSuccessPage = UserUtil.loginUser(session, request, response, actionResult, userId, password, mergePage, this.getSuccessPage());
		} else {
			actionResult.addError(new ActionError("captcha", SystemMessageList.MSG_INVALID_CAPTCHA)); 
		}
		if (updatedSuccessPage != null) {
			this.setSuccessPage(updatedSuccessPage);
			
			if(actionResult.getErrors() != null){
				
				if(!(actionResult.getErrors().size()>0)  && isCaptchaSuccess){		
					fdLoginAttempt = 0;
				} else {
					fdLoginAttempt++;
				}
				
			}			
		} else {
			fdLoginAttempt++;
		}
		session.setAttribute("fdLoginAttempt", fdLoginAttempt);
		return true;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
}
