/*
 * Created on Oct 22, 2004
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyAnswer;
import com.freshdirect.fdstore.survey.FDSurveyQuestion;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

/**
 * @author jangela
 *
 * for surveys shown prior to the next page being offered.
 * e.g: checkout, between review cart and step 1 
 */
public class IntermediateSurveyControllerTag extends AbstractControllerTag implements SessionName {
	private final static Category LOGGER = LoggerFactory.getInstance( SurveyControllerTag.class );
	
	private FDSurvey survey;

	public void setSurvey(FDSurvey survey){
		this.survey = survey;
	}
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException { 
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
		
		String response = "";
		// This is to avoid javascript in page to set the value of response 'cause there are multiple submit images on page
		// and Netscape does not execute onclick if the input types are images
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
			String controlName = (String)e.nextElement();
			if("submit_survey.x".equalsIgnoreCase(controlName) || "submit_survey.y".equalsIgnoreCase(controlName)){
				response = "filled_survey";
				break;
			}
			if("skip_survey.x".equalsIgnoreCase(controlName) || "skip_survey.y".equalsIgnoreCase(controlName)){
				response = "skip_survey";
				break;
			}
		}
		
		try{
			if("filled_survey".equalsIgnoreCase(response)){
				
				if("submitSurvey".equals(this.getActionName())){
					FDSurveyResponse surveyResponse = new FDSurveyResponse(user.getIdentity(), survey.getKey()); //, saleId
					List reqQuests = survey.getRequiredQuestions();
					Map parameters = request.getParameterMap();
						for(Iterator i = parameters.entrySet().iterator(); i.hasNext();){
							Entry e = (Entry) i.next();
							String question = (String)e.getKey();
							String[] answers = (String[])e.getValue();
							
							if ("skipSurvey".equals(question)) continue;
							
							FDSurveyQuestion q = survey.getQuestion(question);
							if(q == null){
								System.out.println("Invalid Question,"+ question + " is an invalid question");
								continue;
							}
							
							if(reqQuests.contains(question)) reqQuests.remove(question);
							actionResult.addError(!q.isValidAnswer(answers), question, SystemMessageList.MSG_REQUIRED);
							
							if (answers.length == 1 && "".equals(answers[0])) continue; //don't create entry for blank open ended questions.
							surveyResponse.addAnswer(question, answers);
							System.out.println(question+", "+answers);
						}
						
					for(Iterator i = reqQuests.iterator(); i.hasNext();){
						System.out.println("for loop");
						String question = (String) i.next();
						System.out.println("question error: " + question);
						actionResult.addError(new ActionError(question, SystemMessageList.MSG_REQUIRED));
					}
					
					if(actionResult.isSuccess()){
						FDCustomerManager.storeSurvey(surveyResponse);
						
						if (user.getSelectedServiceType().equals(EnumServiceType.CORPORATE)) {
							FDCustomerManager.setProfileAttribute(user.getIdentity(), "fourth_order_cos_survey", "FILL");
						} else {
							FDCustomerManager.setProfileAttribute(user.getIdentity(), "fourth_order_survey", "FILL");
						}
						
						user.setSurveySkipped(false);	
						user.updateUserState();
						
						user.invalidateCache();
					}
					
				}
				
			}
			if("skip_survey".equalsIgnoreCase(response)){
				FDCartModel cart = user.getShoppingCart();
				
				if (user.getSelectedServiceType().equals(EnumServiceType.CORPORATE)) {
					FDCustomerManager.setProfileAttribute(user.getIdentity(), "fourth_order_cos_survey", "SKIP");
				} else {
					FDCustomerManager.setProfileAttribute(user.getIdentity(), "fourth_order_survey", "SKIP");
				}
				
				user.setSurveySkipped(true);
				user.updateUserState();

				user.setShoppingCart(cart);
				session.setAttribute(USER, user);
				
				user.invalidateCache();
			}

		}catch(FDResourceException re){
			LOGGER.warn(re);
			actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		for (Iterator itr = actionResult.getErrors().iterator();itr.hasNext();) {
			System.out.println(((ActionError)itr.next()).getDescription());
		}
		return true;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
	
}
