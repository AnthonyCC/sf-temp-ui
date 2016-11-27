/*
 * Created on Mar 3, 2004
 *
 */
package com.freshdirect.webapp.taglib.fdstore;


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
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.mail.EmailUtil;

public class CorporateServiceInterestTag extends AbstractControllerTag implements SessionName {
	
	private static Category LOGGER = LoggerFactory.getInstance( CorporateServiceInterestTag.class );
	
	protected String getParam(HttpServletRequest request, String fieldName) {
		return NVL.apply(request.getParameter(fieldName), "").trim();
	}
	
	private String name;
	private String email;
	private String company;
	private String telephone;


	public void setName(String name) {	
		this.name = name;	
	}

	public String getName() {
		return this.name;
	}

	public void setEmail(String email) {	
		this.email = email;		
	}

	public String getEmail() {
		return this.email;
	}	

	public void setCompany(String company) {
		this.company = company;	
	}

	public String getCompany() {
			return this.company;
		}

	public void setTelephone(String telephone) {
			this.telephone = telephone;	
		}

	public String getTelephone() {
		return this.telephone;
	}
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String actionName = this.getActionName();
		System.out.println("action: " + actionName);
		try {
			if ("sendCorporateServiceInfo".equalsIgnoreCase(actionName)) {
				this.performSendCorporateServiceInfo(request, actionResult);
			}
		} catch (FDResourceException ex) {
			LOGGER.error("Error performing action " + actionName, ex);
			actionResult.addError(new ActionError(EnumUserInfoName.TECHNICAL_DIFFICULTY.getCode(), SystemMessageList.MSG_TECHNICAL_ERROR));
		}

		return true;
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
		
			String subject = "Corporate Service Interest"; 
			String body = 	"Interested party:\n\n" +
							"Name:\t\t\t" + name + "\n" +
							"Company:\t\t" + company + "\n" +
							"Email:\t\t" + email + "\n" +
							"Telephone:\t\t" + telephone + "\n\n";
			
			if(identity != null) {
				body += "------\n\nSent by:\n\n"+
				"Customer Name:\t" + erpCust.getFirstName() + " " + erpCust.getLastName() + "\n" +
				"Customer Email:\t" + erpCust.getEmail() + "\n";
			}

			XMLEmailI email = FDEmailFactory.getInstance().createCorporateServiceInterestEmail(erpCust, subject, body);
			FDCustomerManager.doEmail(email);

			this.setSuccessPage(this.getSuccessPage() + "?info=thankyou"); 
		}
	}
	
	public void populateCorporateServiceForm(HttpServletRequest request) {
        
		name = getParam(request, "name");
		email = getParam(request, "email");
		company = getParam(request, "company");
		telephone = getParam(request, "telephone");
    
	}
	
	protected void validateCorporateServiceForm(ActionResult result) {
		
		result.addError(!"".equals(email) && !EmailUtil.isValidEmailAddress(email), "email", SystemMessageList.MSG_EMAIL_FORMAT);
		result.addError("".equals(email) && "".equals(telephone), "email", SystemMessageList.MSG_REQUIRED);
		
		PhoneNumber phone = new PhoneNumber(telephone);
		result.addError(("".equals(telephone) || !phone.isValid()) && "".equals(email), "telephone", SystemMessageList.MSG_REQUIRED);
		if (phone.isValid()) {
			telephone = phone.getPhone();			
		}

	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}
