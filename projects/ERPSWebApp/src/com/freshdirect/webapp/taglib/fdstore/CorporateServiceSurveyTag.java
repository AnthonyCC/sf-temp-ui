/*
 * Created on Mar 23, 2004
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.util.Iterator;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.survey.EnumSurveyType;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.fdstore.survey.SurveyKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.mail.EmailUtil;
import java.util.Map.Entry;

public class CorporateServiceSurveyTag extends AbstractControllerTag implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance( CorporateServiceInterestTag.class );
	
	protected String getParam(HttpServletRequest request, String fieldName) {
		return NVL.apply(request.getParameter(fieldName), "").trim();
	}
	//Q1 - Contact Information
	private String companyName;
	private String industryType;
	private String streetAddress;
	private String floorSuite;
	//private String suite;
	private String city;
	private String state;
	private String zip;
	private String zip4;
	private String numEmp;
	
	private String contact;
	private String title;
	private String phone;
	private String email;
	private String redirectSuccessPage;
	
	// setter & getter mehods
	public void setCompanyName(String companyName) {	
		this.companyName = companyName;	
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setIndustryType(String industryType) {	
		this.industryType = industryType;		
	}

	public String getIndustryType() {
		return this.industryType;
	}	

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;	
	}

	public String getStreetAddress() {
		return this.streetAddress;
	}
	
	public void setFloorSuite(String floorSuite) {
		this.floorSuite = floorSuite;	
	}

	public String getFloorSuite() {
		return this.floorSuite;
	}

	public void setCity(String city) {
		this.city = city;	
	}

	public String getCity() {
		return this.city;
	}
	
	public void setState(String state) {
		this.state = state;	
	}

	public String getState() {
		return this.state;
	}
	
	public void setZip(String zip) {
		this.zip = zip;	
	}

	public String getZip() {
		return this.zip;
	}
	
	public void setZip4(String zip4) {
		this.zip4 = zip4;	
	}

	public String getZip4() {
		return this.zip4;
	}
	
	public void setnumEmp(String numEmp) {
		this.numEmp = numEmp;	
	}

	public String getnumEmp() {
		return this.numEmp;
	}
	
	public void setContact1(String contact) {
		this.contact = contact;	
	}

	public String getContact() {
		return this.contact;
	}
	
	public void setTitle(String title) {
		this.title = title;	
	}

	public String getTitle() {
		return this.title;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;	
	}

	public String getPhone() {
		return this.phone;
	}
		
	public void setEmail(String email) {
		this.email = email;	
	}

	public String getEmail() {
		return this.email;
	}
	
	public void setRedirectSuccesPage(String redirectSuccessPage) {
		this.redirectSuccessPage = redirectSuccessPage;	
	}

	public String getRedirectSuccessPage() {
		return this.redirectSuccessPage;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String actionName = this.getActionName();
		try {
			if ("submitCorporateServiceSurvey".equalsIgnoreCase(actionName)) {
				//this.performSendCorporateServiceInfo(request, actionResult);
				this.storeCorporateServiceInfo(request, actionResult);
			}
		} catch (FDResourceException ex) {
			LOGGER.error("Error performing action " + actionName, ex);
			actionResult.addError(new ActionError(EnumUserInfoName.TECHNICAL_DIFFICULTY.getCode(), SystemMessageList.MSG_TECHNICAL_ERROR));
		}

		return true;
	}
	
	protected void storeCorporateServiceInfo(HttpServletRequest request, ActionResult result) throws FDResourceException {
		populateCorporateServiceForm(request);
		validateCorporateServiceForm(result);
		
		if(result.isSuccess()){
			HttpSession session = pageContext.getSession();
			FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
			FDIdentity identity = user.getIdentity();		
	
			FDSurveyResponse surveyResponse = new FDSurveyResponse(identity, new SurveyKey(EnumSurveyType.COS_SURVEY_V2, user.getSelectedServiceType()));
			for(Iterator i = request.getParameterMap().entrySet().iterator(); i.hasNext();){
				Entry e = (Entry) i.next();
				String question = (String)e.getKey();
				String[] answers = (String[])e.getValue();
	
				surveyResponse.addAnswer(question, answers);
			}
			
			FDCustomerManager.storeSurvey(surveyResponse);

			this.setSuccessPage(this.getSuccessPage() + "?successPage="+URLEncoder.encode(redirectSuccessPage)+"&info=thankyou#survey");
		} 
	}

	protected void performSendCorporateServiceInfo(HttpServletRequest request, ActionResult result) throws FDResourceException {
		populateCorporateServiceForm(request);
		validateCorporateServiceForm(result);
		
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
		FDIdentity identity = user.getIdentity();		
		
		ErpCustomerInfoModel erpCust = null;
		if (identity != null) {
			erpCust = FDCustomerFactory.getErpCustomerInfo(identity);
		}
	
		if (!result.isSuccess()) {
			System.out.println(">>> not success");
			return;
		} else {
	
			String subject = "Corporate Service Survey"; 
			String body = 	"1. CONTACT INFORMATION:\n" +
							"-----------------------\n" +
							"Company Name:\t" + companyName + "\n" +
							"Industry Type:\t" + industryType + "\n" +
							"Street Address:\t" + streetAddress + "  Floor/Suite #: " + floorSuite + "\n" +
							"City:\t\t\t" + city + "\n" +
							"State:\t\t" + state + "  ZIP + 4: " + zip + " - " + zip4 + "\n\n" +
							"Contact:\t\t" + contact + "\n" +
							"Title:\t\t" + title + "\n" +
							"Phone:\t\t" + phone + "\n" +
							"Email:\t\t" + email + "\n";
		
			XMLEmailI email = FDEmailFactory.getInstance().createCorporateServiceInterestEmail(erpCust, subject, body);
			FDCustomerManager.doEmail(email);

			this.setSuccessPage(this.getSuccessPage() + "?successPage="+URLEncoder.encode(redirectSuccessPage)+"&info=thankyou#survey"); 
		}
	}

	public void populateCorporateServiceForm(HttpServletRequest request) {
		companyName = getParam(request, "companyName");
		industryType = getParam(request, "industryType");
		streetAddress = getParam(request, "streetAddress");
		floorSuite = getParam(request, "floorSuite");
		//suite = getParam(request, "suite");
		city = getParam(request, "city");
		state = getParam(request, "state");
		zip = getParam(request, "zip");
		zip4 = getParam(request, "zip4");
		numEmp = getParam(request, "numEmp");
		contact = getParam(request, "contact");
		title = getParam(request, "title");
		phone = getParam(request, "phone");
		email = getParam(request, "email");
		redirectSuccessPage = getParam(request, "successPage");		
	}

	protected void validateCorporateServiceForm(ActionResult result) {
		result.addError("".equals(companyName), "companyName", SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(streetAddress), "streetAddress", SystemMessageList.MSG_REQUIRED);
		//result.addError("".equals(floorSuite), "floorSuite", SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(city), "city", SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(state), "state", SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(zip) || zip.length()!=5, "zip", SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(numEmp), "numEmp", SystemMessageList.MSG_REQUIRED);
			try {
				Integer.parseInt(numEmp);
			} catch (NumberFormatException ne) {
				result.addError(true, "numEmp", SystemMessageList.MSG_NUM_REQ);
			}
		result.addError("".equals(contact), "contact", SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(title), "title", SystemMessageList.MSG_REQUIRED);
		PhoneNumber phoneFormatted = new PhoneNumber(phone);
		result.addError(("".equals(phone) || !phoneFormatted.isValid()), "phone", SystemMessageList.MSG_REQUIRED);
			if (phoneFormatted.isValid()) {
				phone = phoneFormatted.getPhone();			
			}
		result.addError("".equals(phone), "phone", SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(email), "email", "");
		result.addError(!"".equals(email) && !EmailUtil.isValidEmailAddress(email), "email", SystemMessageList.MSG_EMAIL_FORMAT);
	
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}
