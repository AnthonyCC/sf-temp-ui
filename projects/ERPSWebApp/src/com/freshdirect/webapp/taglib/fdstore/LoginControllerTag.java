/*
 * $Workfile:LoginControllerTag.java$
 *
 * $Date:8/23/2003 7:26:57 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.enums.CaptchaType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
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

	@Override
    protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = request.getSession(true);
        Integer fdLoginAttempt = session.getAttribute(SessionName.LOGIN_ATTEMPT) != null ? (Integer) session.getAttribute(SessionName.LOGIN_ATTEMPT) : Integer.valueOf(0);

		boolean isCaptchaSuccess = CaptchaUtil.validateCaptcha(request.getParameter("g-recaptcha-response"),
				request.getRemoteAddr(), CaptchaType.SIGN_IN, session, SessionName.LOGIN_ATTEMPT,
				FDStoreProperties.getMaxInvalidLoginAttempt());

        if (request.getParameter(EnumUserInfoName.USER_ID.getCode()) == null) {
            actionResult.addError(new ActionError(EnumUserInfoName.USER_ID.getCode(), SystemMessageList.MSG_REQUIRED));
            return true;
        }

        if (request.getParameter(EnumUserInfoName.PASSWORD.getCode()) == null) {
            actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_REQUIRED));
            return true;
        }

		String userId = request.getParameter(EnumUserInfoName.USER_ID.getCode()).trim();
		String password = request.getParameter(EnumUserInfoName.PASSWORD.getCode()).trim();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		String updatedSuccessPage = null;
		if(isCaptchaSuccess){
			updatedSuccessPage = UserUtil.loginUser(session, request, response, actionResult, userId, password, mergePage, this.getSuccessPage(), false);
		} else {
			actionResult.addError(new ActionError("captcha", SystemMessageList.MSG_INVALID_CAPTCHA)); 
		}
		
		
		/* code for merging social account */
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
        if ( user != null && FDStoreProperties.isSocialLoginEnabled() ) {
	    	HashMap<String, String> userLinkAccPendingProp = null;
	        Object objLinkAccPendingProp = session.getAttribute(userId);
		    
		    if(objLinkAccPendingProp != null)
		    {
		    	userLinkAccPendingProp = (HashMap<String, String>)objLinkAccPendingProp;
		    	
		    	if(userLinkAccPendingProp != null)
		    	{
		    		// add this pending account entry to database.
		    		try {
	
						ExternalAccountManager.linkUserTokenToUserId(
								user.getIdentity().getFDCustomerPK(),
								userLinkAccPendingProp.get("email"),
								userLinkAccPendingProp.get("userToken"),
								userLinkAccPendingProp.get("identityToken"),
								userLinkAccPendingProp.get("provider"),
								userLinkAccPendingProp.get("displayName"),
								userLinkAccPendingProp.get("preferredUsername"),
								userLinkAccPendingProp.get("email"), 
								userLinkAccPendingProp.get("emailVerified")
								);
						
						LOGGER.info("Account: "+userLinkAccPendingProp.get("email")+" provider: "+ userLinkAccPendingProp.get("provider")+" merged");
						session.removeAttribute(userLinkAccPendingProp.get("email")); // remove pending link social account.
						
						
					} catch (FDResourceException e1) {
						LOGGER.error("error in merging account:" + e1.getMessage());
					}
		    		
		    		
		    	}
		    }
        }
        
	    /* merging social account code ends here */
		String newURL = request.getScheme() + "://" + request.getServerName();
		if(FDStoreProperties.isLocalDeployment()){
			newURL = newURL + ":" + request.getServerPort();
		}
	    if(updatedSuccessPage != null && updatedSuccessPage.length() > 0) {
	    	if ( user != null && FDStoreProperties.isSocialLoginEnabled() ) {
	    		

	    		
				 // determine whether login is trigger from workflow
//				 String preSuccessPage = (String) session.getAttribute(SessionName.PREV_SUCCESS_PAGE);
//				 if( preSuccessPage != null){
//					 session.removeAttribute(SessionName.PREV_SUCCESS_PAGE);
//					 this.setSuccessPage(newURL + "/social/success.jsp?successPage="+preSuccessPage.substring(1, preSuccessPage.length()), true);
//				 } else {
	    			/* the start slash is prefixed in the success.jsp */
					 this.setSuccessPage(newURL + "/social/success.jsp?successPage="+URLEncoder.encode(updatedSuccessPage.substring(1, updatedSuccessPage.length())), true);
//				 }					 	    		
	    	} else {
	    		this.setSuccessPage(updatedSuccessPage);
	    	}
			
			if(actionResult.getErrors() != null && actionResult.getErrors().size() <= 0  && isCaptchaSuccess){		
				fdLoginAttempt = 0;

			} else {
				fdLoginAttempt++;
			}
			
			
			
			
			
			 // APPDEV-4381 TC Accept.
			
			if(actionResult.getErrors() == null || actionResult.getErrors().size() <= 0){
			 if(user !=null&&!user.getTcAcknowledge()){
				 
				 session.setAttribute("nextSuccesspage", updatedSuccessPage);
					session.setAttribute("fdTcAgree", false);
	 				
			 }else{
				 session.setAttribute("fdTcAgree", true);
			 }
			}
	    	
        } else {
			fdLoginAttempt++;
		}

        session.setAttribute(SessionName.LOGIN_ATTEMPT, fdLoginAttempt);

        session.setAttribute(SessionName.LOGIN_SUCCESS, fdLoginAttempt != null && fdLoginAttempt == 0);
		
		return true;
		
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
}
