package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.ExternalCampaign;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class ExternalCampaignTag extends AbstractControllerTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6201818754616187135L;
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
		try {
	        HttpSession session = pageContext.getSession();
	        if(request.getParameter("campaignId") == null)
	        	return true;
	        FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	        if (user == null || (user!=null && user.getLevel() != FDUserI.SIGNED_IN)) {
	        	return true;
	        }
	        ExternalCampaign extCampaign = new ExternalCampaign();
	        extCampaign.setCampaignId(request.getParameter("campaignId"));
	        extCampaign.setTermsAccepted("on".equals(request.getParameter("terms")));
	        extCampaign.setEntered(false);
	        user.setExternalCampaign(extCampaign);
	        user = FDCustomerManager.saveExternalCampaign(user);
	        session.setAttribute(SessionName.USER, user);
	       
		} catch (FDResourceException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
	
}
