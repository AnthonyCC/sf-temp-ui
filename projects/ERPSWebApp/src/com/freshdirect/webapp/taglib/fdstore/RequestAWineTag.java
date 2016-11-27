/** 
 * RequestAProductTag.java
 * Created Dec 30, 2002
 */
package com.freshdirect.webapp.taglib.fdstore;

/**
 *  @author jangela
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyQuestion;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class RequestAWineTag extends AbstractControllerTag {
	
	private FDSurvey survey;

	public void setSurvey(FDSurvey survey){
		this.survey = survey;
	}
	private static Category LOGGER = LoggerFactory.getInstance(RequestAWineTag.class);

	 protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {
	 	
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		FDIdentity identity  = user.getIdentity();
		String customerId=identity.getErpCustomerPK();
	 	String actionName = this.getActionName();
	 	if ("wineRequest".equalsIgnoreCase(actionName)){

	 		List productRequests=getProductRequests(request,customerId);
			boolean hasProductReq=false;
			boolean hasSurveyResponse=false;
			if( productRequests.size()>0) {
				hasProductReq=true;
			}
			FDSurveyResponse surveyResponse = new FDSurveyResponse(user.getIdentity(), survey.getKey());
			List reqQuests = null;
			reqQuests = survey.getRequiredQuestions();
           	Map parameters = request.getParameterMap();
			for(Iterator i = parameters.entrySet().iterator(); i.hasNext();){
				Entry e = (Entry) i.next();
				String question = (String)e.getKey();
				String[] answers = (String[])e.getValue();
				FDSurveyQuestion q = survey.getQuestion(question);
				if(q==null)continue;
				if(reqQuests.contains(question)) reqQuests.remove(question);
				result.addError(!q.isValidAnswer(answers), question, SystemMessageList.MSG_REQUIRED);
				if (answers.length == 1 && "".equals(answers[0])) continue; //don't create entry for blank open ended questions.
				surveyResponse.addAnswer(question, answers);
				if(!hasSurveyResponse) {
					hasSurveyResponse=true;
				}
			}
			for(Iterator i = reqQuests.iterator(); i.hasNext();){
				String question = (String) i.next();
				result.addError(new ActionError(question, SystemMessageList.MSG_REQUIRED));
			}
			if (!hasProductReq && !hasSurveyResponse ) {
				result.addError(new ActionError("error", "Please enter at least one request."));
				return true;
			}
			try {
				if(result.isSuccess()) {
					FDCustomerManager.storeProductRequest(productRequests,surveyResponse);
				}
			} catch (FDResourceException ex) {
				LOGGER.error(ex.toString());
				throw new JspException(ex.getMessage());
			}
	 	}
	 	return true;

	 }

	private List getProductRequests(HttpServletRequest request, String customerId) {
		
 		String[][] productReq=new String[3][3];
 		List productRequests=new ArrayList(3);
 		
 		for(int row=0;row<3;row++) {
	 		productReq[row][0]= request.getParameter("category"+(row+1));
	 		productReq[row][1]=request.getParameter("subCategory"+(row+1));
	 		productReq[row][2]=request.getParameter("product"+(row+1));
	 		if(isValued(productReq[row][0])|| isValued(productReq[row][1])||isValued(productReq[row][2])) {
	 			productRequests.add(getProductRequest(productReq[row][0], productReq[row][1], productReq[row][2], request.getParameter("department"), customerId)) ;
	 		}
 		}
		return productRequests;
	}
	
    private FDProductRequest getProductRequest(String cat, String subCat, String prodName, String dept, String customerId) {
    	
    	FDProductRequest prodReq=new FDProductRequest();
    	prodReq.setCategory(cat);
    	prodReq.setSubCategory(subCat);
    	prodReq.setProductName(prodName);
    	prodReq.setDept(dept);
    	prodReq.setCustomerId(customerId);
    	
    	return prodReq;
    	
    }
    private boolean isValued(String input) {
    	if(input==null || "".equals(input.trim())) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }

}



