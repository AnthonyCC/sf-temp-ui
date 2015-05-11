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
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
/**
 *
 *
 * @version $Revision:26$
 * @author $Author:Viktor Szathmary$
 */
public class LoginControllerTag extends AbstractControllerTag {
    
    private static Category LOGGER = LoggerFactory.getInstance( LoginControllerTag.class );
    
    private String mergePage;
    
    public void setMergePage(String mp) {
        this.mergePage = mp;
    }
    
    protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
        
        String userId = request.getParameter(EnumUserInfoName.USER_ID.getCode()).trim();
        String password = request.getParameter(EnumUserInfoName.PASSWORD.getCode()).trim();
        
        HttpSession session = pageContext.getSession();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        
        String updatedSuccessPage = UserUtil.loginUser(session, request, response, actionResult, userId, password, mergePage, this.getSuccessPage());
        if(updatedSuccessPage != null) {
        	this.setSuccessPage(updatedSuccessPage);
        }
        return true;
    }
        
    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }
    
}
