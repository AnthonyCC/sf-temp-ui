package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.survey.FDRetentionSurvey;
import com.freshdirect.fdstore.survey.FDRetentionSurveyFactory;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class RetentionSurveyControllerTag  extends AbstractControllerTag implements SessionName  {
	
	private final static Category LOGGER = LoggerFactory.getInstance( RetentionSurveyControllerTag.class );
		
	private String surveyResult;
	
	private static final String INVALID_SURVEYERROR = "We're sorry, this survey cannot be found. Please be sure to use the link that was sent to your FreshDirect account.";
	
	public String getSurveyResult() {
		return surveyResult;
	}

	public void setSurveyResult(String surveyResult) {
		this.surveyResult = surveyResult;
	}

	/**
	 * @return false to SKIP_BODY without redirect
	 */
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		return processAction(request, actionResult);
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {		
		return processAction(request, actionResult);
	}
	
	private boolean processAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
		LOGGER.debug("------------------------Start RetentionSurveyControllerTag Perform Action Method-----------------------------------");
		String saleId = (String)request.getParameter("ID");
		String surveyName = (String)request.getParameter("SURVEY");
		String surveyResponse = (String)request.getParameter("VALUE");
		boolean hasError = true;
		
		LOGGER.debug("SALEID="+saleId+"--"+"SURVEYNAME="+surveyName+"--"+"SURVEYRESPONSE="+surveyResponse);
		
		FDRetentionSurvey retentionSurvey = FDRetentionSurveyFactory.getInstance().getSurvey(surveyName);
		FDCustomerModel fdCustomer = null;
		
		try {
			
			// Check for Valid Profile Attribute
			if((fdCustomer = getSurveyUser(saleId, retentionSurvey, surveyResponse)) != null) {
								
				FDIdentity fdIdentity = new FDIdentity(fdCustomer.getErpCustomerPK(), fdCustomer.getPK().getId());
					
				String strProfileValue = fdCustomer.getProfile().getAttribute(retentionSurvey.getProfileName());
				
				String strCheckProfileValue = fdCustomer.getProfile().getAttribute(retentionSurvey.getCheckProfileName());
				
				LOGGER.debug("--SURVEY PROFILE VALUE--"+strCheckProfileValue+"&"+strProfileValue);
				
				// Check for Valid Reference Profile Attribute
				if( strCheckProfileValue != null && StringUtils.isNotEmpty(strCheckProfileValue)) {
					
					if (strProfileValue != null) {
						
						pageContext.setAttribute(this.surveyResult
													, getMediaLink(retentionSurvey, surveyResponse, false, strProfileValue));	
						LOGGER.debug("ERPCUSTOMERID="+fdCustomer.getErpCustomerPK()+"--"+"HAS ALREADY TAKEN THE SURVEY "+surveyName);
						hasError = false;
					} else {				
						
						CrmSystemCaseInfo caseInfo = getCaseInfo(retentionSurvey, surveyResponse
																	, fdCustomer.getErpCustomerPK(), strCheckProfileValue );
																
						FDCustomerManager.storeRetentionSurvey(fdIdentity
													, retentionSurvey.getProfileName()
														, retentionSurvey.getProfileValueForResponse(surveyResponse), caseInfo);
						
						String mediaLink = getMediaLink(retentionSurvey, surveyResponse, true, strCheckProfileValue);
						LOGGER.debug("--MEDIA LINK FOR LANDING--"+mediaLink);
						
						// Check to see if its a landing page or a external redirect link
						if(mediaLink != null) {
							if(mediaLink.startsWith("/")) {
								pageContext.setAttribute(this.surveyResult, mediaLink);
							} else {
								this.redirectTo(mediaLink);								
								return false;
							}
							hasError = false;
						} 
					}					
				}
			} 			
		} catch (FDResourceException ex) {
			
			LOGGER.warn("Caught FDResoureException in RetentionSurveyControllerTag.getSurveyUser() Unable to load survey user ",ex);			
		} 
		
		if(hasError) {
			actionResult.addError(new ActionError("invalid_input", INVALID_SURVEYERROR));
		}
		
		LOGGER.debug("------------------------End RetentionSurveyControllerTag Perform Action Method-----------------------------------");
		return true;
	}
	
	protected void redirectTo(String destination) throws JspException {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			response.sendRedirect(destination);
			JspWriter writer = pageContext.getOut();
			writer.close();
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}
		
	private FDCustomerModel getSurveyUser(String saleId, FDRetentionSurvey retentionSurvey,
											String surveyResponse) throws FDResourceException {
		
		FDCustomerModel fdCustomer = null;
		
		if(StringUtils.isNotEmpty(saleId)) {
			
			if(retentionSurvey != null && retentionSurvey.isValidResponse(surveyResponse)) {
				
				FDOrderI fdOrder = FDCustomerManager.getOrder(saleId);
				if(StringUtils.isNotEmpty(fdOrder.getCustomerId())) {
					
					fdCustomer = FDCustomerFactory.getFDCustomerFromErpId(fdOrder.getCustomerId());
				}		
			} else {
				LOGGER.warn("INVALID SURVEY NAME OR RESPONSE AND NOT ELIGIBLE FOR TAKING THE SURVEY");
			}
		} else {
			LOGGER.warn("CUSTOMER ID EMPTY AND NOT ELIGIBLE FOR TAKING THE SURVEY");
		}
		return fdCustomer;
	}
	
	
	private String getMediaLink(FDRetentionSurvey retentionSurvey
									, String surveyResponse, boolean isSuccess ,String profileRef ) {
		
		String successKey = isSuccess ? FDRetentionSurveyFactory.SURVEY_SUCCESS : FDRetentionSurveyFactory.SURVEY_FAILURE;
		String baseLink = retentionSurvey.getMediaLink(surveyResponse+"_"+successKey+"_"+profileRef);
		if(baseLink == null) {
			baseLink = retentionSurvey.getMediaLink(surveyResponse+"_"+successKey);
		}
		
		return baseLink;
	}
	
	private CrmSystemCaseInfo getCaseInfo(FDRetentionSurvey retentionSurvey
											, String surveyResponse, String erpCustomerId, String profileVal ) {
		
		CrmSystemCaseInfo caseInfo = null;
		
		//If the property fdstore.retentionProgram.createCase is set to "true". Case will be created in the system.
		if(FDStoreProperties.isRetProgramCreateCase()) {		
			String[] caseParam = retentionSurvey.getCaseInfo(surveyResponse+"_"+profileVal);
			
			if(caseParam != null && caseParam.length >=3) {
				caseInfo = new CrmSystemCaseInfo(new PrimaryKey(erpCustomerId)
									, CrmCaseSubject.getEnum(caseParam[0])
											, caseParam[1]);
				caseInfo.setNote(caseParam[2]);
			}
		}
		
		return caseInfo;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				 new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED), new VariableInfo(
							data.getAttributeString("surveyResult"),
							"java.lang.String",
							true,
							VariableInfo.NESTED)};
		}
	}

}
