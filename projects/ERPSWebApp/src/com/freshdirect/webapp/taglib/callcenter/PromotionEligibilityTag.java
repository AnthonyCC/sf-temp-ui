/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class PromotionEligibilityTag extends AbstractControllerTag {
    
    private static Category LOGGER = LoggerFactory.getInstance( PromotionEligibilityTag.class );
    
    protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
        
        HttpSession session = pageContext.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
        
        try {
        	boolean eligible; 
            if ("allow_promo".equalsIgnoreCase(this.getActionName())) {
            	eligible = true;
            } else if ("deny_promo".equalsIgnoreCase(this.getActionName())) {
            	eligible = false;
            } else {
            	return false;
            }
            
            FDCustomerManager.setSignupPromotionEligibility(AccountActivityUtil.getActionInfo(session), eligible);
            
            user.invalidateCache();
            
            // recalculate promotions
            user.updateUserState();
            pageContext.getSession().setAttribute( SessionName.USER, user );
            
        } catch (FDResourceException ex) {
            LOGGER.error("Exception trying to update user profile.", ex);
            actionResult.addError(new ActionError("technical_difficulty", "Could not modify promotion eligibility due to a system error. Please try again later."));
            return false;
        }
        
        return true;
        
    }
    
    
}