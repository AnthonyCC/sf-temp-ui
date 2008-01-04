/**@author ekracoff
 * Created on Nov 5, 2003
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyQuestion;
import com.freshdirect.fdstore.survey.FDSurveyAnswer;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;


public class ReceiptSurveyControllerTag  extends AbstractControllerTag implements SessionName {

	private final static Category LOGGER = LoggerFactory.getInstance( SurveyControllerTag.class );

	private FDSurvey survey;

	public void setSurvey(FDSurvey survey){
		this.survey = survey;
	}

	private StringBuffer successPage = new StringBuffer();
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
		FDIdentity identity = user!= null ? user.getIdentity() : null;
		
		boolean isPostOrderSurvey = "postOrder".equalsIgnoreCase(survey.getName()) || "postOrderDetail".equalsIgnoreCase(survey.getName());
		boolean toPostOrderDetail = "PROBLEM".equalsIgnoreCase(request.getParameter("q_post_order_response"));
		boolean isCOSSurvey = "COS_Feedback_Survey".equalsIgnoreCase(survey.getName());
		boolean isHamptonsSurvey = "Hamptons05".equalsIgnoreCase(survey.getName());
		boolean isOrganicSurvey = "Organic2".equalsIgnoreCase(survey.getName());
		boolean isMorningDeliverySurvey = "MorningDelivery".equalsIgnoreCase(survey.getName());
		boolean isUsabilitySurvey = "Usability".equalsIgnoreCase(survey.getName());
		boolean isNutritionSurvey = "DietNutrition".equalsIgnoreCase(survey.getName());
		boolean isRcptPage2Survey = "ReceiptPageSurvey2".equalsIgnoreCase(survey.getName());
		boolean submitNow = false;
		
		List hamptonsPurgeReqQuestions = new ArrayList();
		hamptonsPurgeReqQuestions.add("VacationOrder");
		hamptonsPurgeReqQuestions.add("VacationTimeslot");
		hamptonsPurgeReqQuestions.add("Vacation1495");
		hamptonsPurgeReqQuestions.add("Vacation3Hour");
		hamptonsPurgeReqQuestions.add("VacationNoFriday");
		hamptonsPurgeReqQuestions.add("VacationCentralLocation");
		hamptonsPurgeReqQuestions.add("VacationPickup");
		hamptonsPurgeReqQuestions.add("VacationBuy");
		boolean purgeQuestions = false;

		FDSurveyResponse surveyResponse = null;
		List reqQuests = null;
		
		if (toPostOrderDetail) successPage.append(this.getSuccessPage());
		
		try{
			
			if("validateSurvey".equals(this.getActionName())){
				PrimaryKey saleId = null;
				 surveyResponse = new FDSurveyResponse(user.getIdentity(), survey.getName(), saleId);
				 reqQuests = survey.getRequiredQuestions();
				
				successPage.append(this.getSuccessPage());
				successPage.append("?");
				
				Map parameters = request.getParameterMap();
					for(Iterator i = parameters.entrySet().iterator(); i.hasNext();){
						Entry e = (Entry) i.next();
						String question = (String)e.getKey();
						String[] answers = (String[])e.getValue();
						
						FDSurveyQuestion q = survey.getQuestion(question);
						if(q == null){
							if (!isPostOrderSurvey && !isCOSSurvey && !isHamptonsSurvey && !isOrganicSurvey && !isMorningDeliverySurvey && !isUsabilitySurvey && !isNutritionSurvey &&!isRcptPage2Survey) {
							actionResult.addError(new ActionError("Invalid Question", question + " is an invalid question"));
							}
							continue;
						}
						
						if(reqQuests.contains(question)) reqQuests.remove(question);
						
						actionResult.addError(!q.isValidAnswer(answers), question, SystemMessageList.MSG_REQUIRED);
						
						if (answers.length == 1 && "".equals(answers[0])) continue; //don't create entry for blank open ended questions.
						
						for (int n=0; n < answers.length; n++) {
							successPage.append(question);
							successPage.append("=");
							successPage.append(answers[n]);
							successPage.append("&");
						}
					}
					
				if (isHamptonsSurvey) {
					for (int j=0; j < hamptonsPurgeReqQuestions.size(); j++) {
						if(reqQuests.contains(hamptonsPurgeReqQuestions.get(j))) reqQuests.remove(hamptonsPurgeReqQuestions.get(j));
					}
				}
				
				for(Iterator i = reqQuests.iterator(); i.hasNext();){
					String question = (String) i.next();
					actionResult.addError(new ActionError(question, SystemMessageList.MSG_REQUIRED));
				}
				
				if(actionResult.isSuccess()){
					this.setSuccessPage(successPage.toString());
				}
			}	
			
			if("submitSurvey".equals(this.getActionName())){
				boolean surveySkipped = "yes".equalsIgnoreCase(request.getParameter("skipSurvey"));
				
				LOGGER.debug("submit survey");
				if (!surveySkipped) {
					PrimaryKey saleId = null;
					if(survey.isOrderSurvey()){	
						/* Commented as part of PERF-22.
						ErpSaleInfo secondToLastSale = user.getOrderHistory().getSecondToLastSale();
						if(secondToLastSale != null) saleId = new PrimaryKey(secondToLastSale.getSaleId());
						*/
						String secondToLastSaleId = user.getOrderHistory().getSecondToLastSaleId();
						if(secondToLastSaleId != null)
								saleId = new PrimaryKey(secondToLastSaleId);
					}
					
					if(isPostOrderSurvey) {
						String sale_id = request.getParameter("sale");
						if (sale_id != null && !"".equals(sale_id)) {
							saleId = new PrimaryKey(sale_id);
							if (toPostOrderDetail) {
								successPage.append("sale=");
								successPage.append(sale_id);
								successPage.append("&");
							}
						}
					}
						
					surveyResponse = new FDSurveyResponse(user.getIdentity(), survey.getName(), saleId);
					reqQuests = survey.getRequiredQuestions();
			
					
					Map parameters = request.getParameterMap();
						for(Iterator i = parameters.entrySet().iterator(); i.hasNext();){
							Entry e = (Entry) i.next();
							String question = (String)e.getKey();
							String[] answers = (String[])e.getValue();
							
							FDSurveyQuestion q = survey.getQuestion(question);
							if(q == null){
								if (!isPostOrderSurvey && !isCOSSurvey && !isHamptonsSurvey && !isOrganicSurvey && !isMorningDeliverySurvey && !isUsabilitySurvey && !isNutritionSurvey &&!isRcptPage2Survey) {
								actionResult.addError(new ActionError("Invalid Question", question + " is an invalid question"));
								}
								continue;
							}
							
							if (toPostOrderDetail) {
								if ("q_problem_area".equals(question)) {
									FDSurveyQuestion problemArea = survey.getQuestion(question);
									List setProblemAreas = problemArea.getAnswers();
									String[] formProblemAreas = answers;
									for(Iterator k = setProblemAreas.iterator(); k.hasNext();){
										FDSurveyAnswer setAnswer = (FDSurveyAnswer) k.next();
										for (int n=0; n< formProblemAreas.length; n++) {
											if (setAnswer.getDescription().equalsIgnoreCase(formProblemAreas[n])){
												successPage.append(setAnswer.getName());
												successPage.append("=show");
												successPage.append("&");
											}
										}
									}
								} else if ("q_problem_area_other".equals(question) && answers.length >0 && !"".equals(answers[0])) {
									successPage.append("q1_other=");
									successPage.append(answers[0]);
									successPage.append("&");
								}
								this.setSuccessPage(successPage.toString());
							}
							
							if(reqQuests.contains(question)) reqQuests.remove(question);
							
							actionResult.addError(!q.isValidAnswer(answers), question, SystemMessageList.MSG_REQUIRED);
							
							if (answers.length == 1 && "".equals(answers[0])) continue; //don't create entry for blank open ended questions.
							
							
							if (isHamptonsSurvey && "VacationDestination".equalsIgnoreCase(q.getName())) {
								for (int n=0; n < answers.length; n++) {
									if (answers[n].indexOf("East LI") < 0) {
										//outside area of interest, cut survey short
										purgeQuestions = true;
									}
								}
							}
							
							surveyResponse.addAnswer(question, answers);
						}
					
					if (purgeQuestions) {
						for (int j=0; j < hamptonsPurgeReqQuestions.size(); j++) {
							if(reqQuests.contains(hamptonsPurgeReqQuestions.get(j))) reqQuests.remove(hamptonsPurgeReqQuestions.get(j));
						}
					}
						
					for(Iterator i = reqQuests.iterator(); i.hasNext();){
						String question = (String) i.next();
						actionResult.addError(new ActionError(question, SystemMessageList.MSG_REQUIRED));
					}
				}
				if(actionResult.isSuccess() || surveySkipped){
					LOGGER.debug("is success");
					if (isUsabilitySurvey) {
						FDCustomerManager.setProfileAttribute(identity, "Usability", "FILL");
					} else if (isOrganicSurvey) {
						FDCustomerManager.setProfileAttribute(identity, "Organic2", "FILL");
					} else if (isMorningDeliverySurvey) {
						FDCustomerManager.setProfileAttribute(identity, "MorningDelivery", "FILL");
					} else if (isHamptonsSurvey) {
						FDCustomerManager.setProfileAttribute(identity, "Hamptons05", "FILL");
					} else if (isNutritionSurvey) {
						FDCustomerManager.setProfileAttribute(identity, "DietNutrition", "FILL");
					} else if (isRcptPage2Survey) {
						if (surveySkipped) {
					        String rpctSrvy2Status = NVL.apply(user.getFDCustomer().getProfile().getAttribute("ReceiptPageSurvey2"),"");
					        String skipStatus = "skip_1";
							if ("skip_1".equalsIgnoreCase(rpctSrvy2Status)) {
								skipStatus = "skip_2";
							} else if ("skip_2".equalsIgnoreCase(rpctSrvy2Status)) {
								skipStatus = "skip_3";
							}
							FDCustomerManager.setProfileAttribute(identity,"ReceiptPageSurvey2",skipStatus);
							setSuccessPage(getSuccessPage()+"?info=skipped");
					} else {
							setSuccessPage(getSuccessPage()+"?info=filled");
							FDCustomerManager.setProfileAttribute(identity,"ReceiptPageSurvey2","FILL");
						}
					}
					user.invalidateCache();
					user.updateUserState();

					if (!surveySkipped) {
						LOGGER.debug("preparing to store survey");
						FDCustomerManager.storeSurvey(surveyResponse);
						LOGGER.debug("stored survey");
					}
				}
			}
			
		}catch(FDResourceException re){
			LOGGER.warn(re);
			actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		}

		return true;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}

