package com.freshdirect.webapp.taglib.fdstore;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.fdstore.survey.EnumSurveyType;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyCachedFactory;
import com.freshdirect.fdstore.survey.FDSurveyResponse;

public class CustomerProfileSurveyTag extends AbstractControllerTag {
	
	private String survey;

	public void setSurvey(String survey){
		this.survey = survey;
	}
	private static Category LOGGER = LoggerFactory.getInstance(CustomerProfileSurveyTag.class);

	 protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {
	 	
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		String actionName = this.getActionName();
	 	if ("submitSurvey".equalsIgnoreCase(actionName)){
	 		try {
				FDSurvey _survey=FDSurveyCachedFactory.getSurvey(EnumSurveyType.getEnum(survey));
				FDSurveyResponse surveyResponse=SurveyHelper.getSurveyResponse(user.getIdentity(), _survey, result, request.getParameterMap());
				if(result.isSuccess()) {
					FDCustomerManager.storeSurvey(surveyResponse);
				}
			} catch (FDResourceException e) {
				LOGGER.error(e.toString());
				throw new JspException(e.getMessage());
			}
	 	}
	 	return true;
	 }
    
    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }

}

