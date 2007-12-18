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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import java.text.MessageFormat;
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
        
        if (userId == null ||  userId.length() < 1 ) {
            actionResult.addError(new ActionError(EnumUserInfoName.USER_ID.getCode(), SystemMessageList.MSG_REQUIRED));
        } else if (!EmailUtil.isValidEmailAddress(userId)) {
            actionResult.addError(new ActionError(EnumUserInfoName.EMAIL_FORMAT.getCode(), SystemMessageList.MSG_EMAIL_FORMAT));
        }
        
        if (password==null || password.length() < 1) {
            actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_REQUIRED));
        } else if (password.length() < 4) {
            actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_PASSWORD_TOO_SHORT));
        }
        
        if (!actionResult.isSuccess()) {
            return true;
        }
        
        try {
        	FDIdentity identity = FDCustomerManager.login(userId,password);
            LOGGER.info("Identity : erpId = " + identity.getErpCustomerPK() + " : fdId = " + identity.getFDCustomerPK());
            
            FDUser loginUser = FDCustomerManager.recognize(identity);
            LOGGER.info("FDUser : erpId = " + loginUser.getIdentity().getErpCustomerPK() + " : " + loginUser.getIdentity().getFDCustomerPK());
            
            HttpSession session = pageContext.getSession();
            FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
            
            HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
            
            LOGGER.info("loginUser is " + loginUser.getFirstName() + " Level = " + loginUser.getLevel());
            LOGGER.info("currentUser is " + (currentUser==null?"null":currentUser.getFirstName()+currentUser.getLevel()));
            
            if (currentUser == null) {
                // this is the case right after signout
                UserUtil.createSessionUser(request, response, loginUser);
                
            } else if (!loginUser.getCookie().equals(currentUser.getCookie())) {
                // current user is different from user who just logged in
                
                int currentLines = currentUser.getShoppingCart().numberOfOrderLines();
                int loginLines = loginUser.getShoppingCart().numberOfOrderLines();
                
                if ((currentLines > 0) && (loginLines > 0)) {
                    // keep the current cart in the session and send them to the merge cart page
                    
                    session.setAttribute(SessionName.CURRENT_CART, currentUser.getShoppingCart());
                    this.setSuccessPage( mergePage + "?successPage=" + URLEncoder.encode( this.getSuccessPage() ) );
                    
                } else if ((currentLines > 0) && (loginLines == 0)) {
                    // keep current cart
                    loginUser.setShoppingCart(currentUser.getShoppingCart());
                }
                
                UserUtil.createSessionUser(request, response, loginUser);
                
            } else {
                // the logged in user was the same as the current user,
                // that means that they were previously recognized by their cookie before log in
                // just set their login status and move on
                currentUser.isLoggedIn(true);
                session.setAttribute(SessionName.USER, currentUser);
            }
            
        } catch (FDResourceException fdre) {
            LOGGER.warn("Resource error during authentication", fdre);
            actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
            
        } catch (FDAuthenticationException fdae) {
        	actionResult.addError(new ActionError("authentication", 
            		MessageFormat.format(SystemMessageList.MSG_AUTHENTICATION, 
            		new Object[] { UserUtil.getCustomerServiceContact(request)})));
        }
        
        return true;
    }
    
    
    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }
    
}