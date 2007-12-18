/*
 * Created on Mar 16, 2004
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

public class CateringOrderTag extends AbstractControllerTag implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance(CorporateServiceInterestTag.class);

	protected String getParam(HttpServletRequest request, String fieldName) {
		return NVL.apply(request.getParameter(fieldName), "").trim();
	}

	private String firstName;
	private String lastName;
	private String phone;
	private String phoneExt;
	private String bestTime;
	private String email;
	private String eventDate;
	private String partySize;
	private String summaryRequest;

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhoneExt(String phoneExt) {
		this.phoneExt = phoneExt;
	}

	public String getPhoneExt() {
		return this.phoneExt;
	}

	public void setBestTime(String bestTime) {
		this.bestTime = bestTime;
	}

	public String getBestTime() {
		return this.bestTime;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}
	
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventDate() {
		return this.eventDate;
	}

	public void setPartySize(String partySize) {
		this.partySize = partySize;
	}

	public String getPartySize() {
		return this.partySize;
	}

	public void setSummaryRequest(String summaryRequest) {
		this.summaryRequest = summaryRequest;
	}

	public String getSummaryRequest() {
		return this.summaryRequest;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String actionName = this.getActionName();
		System.out.println("action: " + actionName);
		try {
			if ("sendCateringOrder".equalsIgnoreCase(actionName)) {
				this.performSendCateringOrder(request, actionResult);
			}
		} catch (FDResourceException ex) {
			LOGGER.error("Error performing action " + actionName, ex);
			actionResult.addError(
				new ActionError(EnumUserInfoName.TECHNICAL_DIFFICULTY.getCode(), SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		return true;
	}
	
	protected void performSendCateringOrder(HttpServletRequest request, ActionResult result) throws FDResourceException {

		populateCateringForm(request);
		validateCateringForm(result);

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

			String subject = "Catering Order Request";
			String body =
				"Catering Order Request:\n\n"
					+ "Name:\t\t\t"
					+ firstName
					+ " "
					+ lastName
					+ "\n"
					+ "Contact #:\t\t"
					+ phone;
					
				if (!"".equals(phoneExt)) {
					body += " Ext. "
					+ phoneExt;
				}
					
					body += "\n";
				
				if (!"".equals(bestTime)) {
					body += "Best time:\t\t"
					+ bestTime
					+ "\n";
				}
				
					body += "Email Address:\t"
					+ email
					+ "\n";
				
				if (!"".equals(eventDate)) {
					body += "Date of Event:\t"
					+ eventDate
					+ "\n";
				}
				
				if (!"".equals(partySize)) {	
					body += "Number of people:\t"
					+ partySize
					+ "\n";
				}
				
				if (!"".equals(summaryRequest)) {
					body += "\n"
					+ "Event summary & Food request:\n"
					+ summaryRequest
					+ "\n\n";
				}

			/*
			if (identity != null) {
				body += "------\n\nSent by:\n\n"
					+ "Customer Name:\t"
					+ erpCust.getFirstName()
					+ " "
					+ erpCust.getLastName()
					+ "\n"
					+ "Customer Email:\t"
					+ erpCust.getEmail()
					+ "\n";
			}*/

			XMLEmailI email = FDEmailFactory.createCateringEmail(erpCust, subject, body);
			FDCustomerManager.doEmail(email);

			this.setSuccessPage(this.getSuccessPage() + "&info=thankyou");
		}
	}

	public void populateCateringForm(HttpServletRequest request) {

		firstName = getParam(request, "firstName");
		lastName = getParam(request, "lastName");
		phone = getParam(request, "phone");
		phoneExt = getParam(request, "phoneExt");
		bestTime = getParam(request, "bestTime");
		email = getParam(request, "email");
		eventDate = getParam(request, "eventDate");
		partySize = getParam(request, "partySize");
		summaryRequest = getParam(request, "summaryRequest");

	}

	protected void validateCateringForm(ActionResult result) {

		result.addError("".equals(firstName), "firstName", SystemMessageList.MSG_REQUIRED);

		result.addError("".equals(lastName), "lastName", SystemMessageList.MSG_REQUIRED);

		PhoneNumber cphone = new PhoneNumber(phone);
		result.addError(("".equals(phone) || !cphone.isValid()), "phone", SystemMessageList.MSG_REQUIRED);		
		if (cphone.isValid()) {
			phone = cphone.getPhone();			
		}

		result.addError(!"".equals(email) && !EmailUtil.isValidEmailAddress(email), "email", SystemMessageList.MSG_EMAIL_FORMAT);
		result.addError("".equals(email), "email", SystemMessageList.MSG_REQUIRED);

	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}
