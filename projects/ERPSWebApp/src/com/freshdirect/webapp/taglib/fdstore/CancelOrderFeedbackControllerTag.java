/*
 * CancelOrderFeedbackTag.java
 *
 * Created on May 1, 2002, 12:34 PM
 */

package com.freshdirect.webapp.taglib.fdstore;

/**
 *
 * @author  rfalck
 * @version
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CancelOrderFeedbackControllerTag extends AbstractControllerTag implements SessionName {
    
    private final static Category LOGGER = LoggerFactory.getInstance( CancelOrderFeedbackControllerTag.class );


	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {

		FeedbackForm form = new FeedbackForm();
        form.populateForm(request);
        form.validateForm(actionResult);
        
        if (!actionResult.isSuccess()) {
        	return true;	
        }
            
        HttpSession session = pageContext.getSession();
        FDIdentity identity = ((FDSessionUser) session.getAttribute(USER)).getIdentity();
        try{

            FDCustomerManager.storeSurvey(form.getSurvey(identity, "Cancel_order_feedback"));

        } catch(FDResourceException re) {
            LOGGER.warn("FDResourceException", re);
            actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
        }
        
		return true;
	}
    
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
    
   	private static class FeedbackForm implements WebFormI {
		private String orderId;
		private String reason;
		private String comment;

	    public void populateForm(HttpServletRequest request) {
	        this.orderId = request.getParameter("orderId");
	        this.reason = request.getParameter("reason");
	        this.comment = request.getParameter("comment");
	    }
	    
	    public void validateForm(ActionResult result) {
			if (reason == null ||  reason.trim().length() < 1 || "".equals(reason) ) {
	            result.addError(new ActionError("reason", SystemMessageList.MSG_REQUIRED));
	        }	        
	    }

		public FDSurveyResponse getSurvey(FDIdentity identity, String name) {
            FDSurveyResponse survey = new FDSurveyResponse(identity,  name);
			survey.addAnswer("orderId", this.orderId);
			survey.addAnswer("reason", this.reason);
			survey.addAnswer("comment", this.comment);
            return survey;
		}
	}

}