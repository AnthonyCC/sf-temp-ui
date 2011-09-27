package com.freshdirect.webapp.taglib.fdstore;

//import java.util.Date;
//import java.util.Iterator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AllPredicate;
import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.query.ContentKeysPredicate;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearchUtil;
import com.freshdirect.fdstore.content.SearchQueryStemmer;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class ContactFdControllerTag extends AbstractControllerTag implements SessionName  {

	private final static Category LOGGER = LoggerFactory.getInstance(ContactFdControllerTag.class);

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {
			HttpSession session = pageContext.getSession();
			FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
			FDIdentity identity = user.getIdentity();
			
			ContactForm form = new ContactForm(identity == null);
			form.populateForm(request);
			if(null !=request.getParameter("sendMessage.x")){
				form.validateForm(actionResult);
				
				if (actionResult.isSuccess()) {
					this.performContactFd(form, user);
				}
	
			}else{
//				return false;
				String faqKeyword = request.getParameter("searchFAQ");
				faqKeyword = NVL.apply(faqKeyword, "");
				faqKeyword = ContentSearchUtil.normalizeTerm(faqKeyword);
				this.redirectTo("/help/faq_search.jsp?searchFAQ="+faqKeyword);				
			}
		} catch (FDResourceException e) {
			LOGGER.warn("FDResourceException occured", e);
			throw new JspException(e);
		}
		return true;
	}

	private void performContactFd(ContactForm form, FDSessionUser user) throws FDResourceException {
		//this is the create case code
		/*CrmSystemCaseInfo newCase =
			new CrmSystemCaseInfo(form.customerPK, form.salePK, form.subject, form.summary);
		newCase.setOrigin(CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_WEB_FORM));
		newCase.setState(CrmCaseState.getEnum(CrmCaseState.CODE_OPEN));
		String note = form.getNote();
		LOGGER.debug(">>> note " + note);
		newCase.setNote(note);

		FDCustomerManager.createCase(newCase);*/
		
		//create email code
		FDCustomerInfo customer = null;
		if(user.getIdentity() == null){
			customer = new FDCustomerInfo(form.firstname, form.lastname);
			customer.setEmailAddress(form.email);
			customer.setHomePhone(form.homePhone);
			customer.setWorkPhone(form.workPhone);
			customer.setCellPhone(form.altPhone);
		} else{
			ErpCustomerInfoModel erpCust = FDCustomerFactory.getErpCustomerInfo(user.getIdentity());
			customer = new FDCustomerInfo(erpCust.getFirstName(), erpCust.getLastName());
			customer.setEmailAddress(erpCust.getEmail());
			customer.setHomePhone(erpCust.getHomePhone());
			customer.setWorkPhone(erpCust.getBusinessPhone());
			customer.setCellPhone(erpCust.getCellPhone());
		}
		
		String subjectPrefix = user.isCorporateUser() ? "[COS] " : "[contact_us] ";
		
		
		String subject = subjectPrefix + form.subject; 
		String body = "Name:\t\t\t" + customer.getFirstName() + " " + customer.getLastName() + "\n" +
				      "Email:\t\t" + customer.getEmailAddress() + "\n" +
				      "Home Phone:\t\t" + (customer.getHomePhone() == null ? "":customer.getHomePhone().toString()) + "\n" + 
				      "Business Phone:\t" + (customer.getWorkPhone() == null ? "":customer.getWorkPhone().toString()) + "\n" +
				      "Alt/Cell Phone:\t" + (customer.getCellPhone() == null ? "":customer.getCellPhone().toString()) + "\n";
		
		if(form.salePK != null) body = body + "\nIn Reference To Order Number: " + form.salePK.getId() + "\n";
		
		body = body + "\n\n\n" + form.message;
 		
		FDCustomerManager.sendContactServiceEmail(customer, subject, body, user.isChefsTable(), false, subject.indexOf("Fresh Meal Vending") > -1);//subject.indexOf("Delivery Areas") > -1);
		
		LOGGER.debug(">>>Sent Contact Service Email from: " + customer.getEmailAddress());
		
		this.setSuccessPage(this.getSuccessPage() + "?msg=" + form.confirmMessage);
	}
	
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		pageContext.setAttribute("savedFaqs", getTopFaqs(actionResult));
		return true;
	}
	
	private List getTopFaqs(ActionResult actionResult){
		try {
			List topFaqNodes =new ArrayList();
			List faqNodeIdList = FDCustomerManager.getTopFaqs();
			for (Iterator iterator = faqNodeIdList.iterator(); iterator
					.hasNext();) {
				String nodeId = (String) iterator.next();
//				ContentKey key = new ContentKey(FDContentTypes.FAQ, nodeId);
//				CmsManager          manager     = CmsManager.getInstance();	
//				ContentNodeI contentNode = manager.getContentNode(key);	
				ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(nodeId);
				topFaqNodes.add(contentNode);
			}
			return topFaqNodes;
		} catch (FDResourceException e) {
			actionResult.addError(true, "getSavedFaqError", " Failed to get the saved FAQs.");
			return Collections.EMPTY_LIST;
		}
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

	public static class Selection {
		private final String subjectCode;
		private final String description;

		public Selection(String subjectCode, String description) {
			this.subjectCode = subjectCode;
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public String getSubjectCode() {
			return subjectCode;
		}

	}

	//private final static String[] SUBJECT_CODES =
	//	{ CrmCaseSubject.CODE_ASQ_MISC, CrmCaseSubject.CODE_ASQ_MISC, CrmCaseSubject.CODE_ASQ_MISC, CrmCaseSubject.CODE_ASQ_MISC };

	public final static Selection[] selections =
		{
			new Selection(CrmCaseSubject.CODE_PROBLEM, "Problem with an order I received"),	
			new Selection(CrmCaseSubject.CODE_SERVICE_AVAILABILITY, "Delivery Time Status"),
			new Selection(CrmCaseSubject.CODE_PROMOTION, "Promotion"),
			new Selection(CrmCaseSubject.CODE_ASQ_MISC , "Billing"),
			new Selection(CrmCaseSubject.CODE_WEBSITE_PROBLEM, "Web Site/Technical"),
			new Selection(CrmCaseSubject.CODE_COMPLAINT, "Delivery Feedback"),
			new Selection(CrmCaseSubject.CODE_PRODUCT, "Product Request"),
			new Selection(CrmCaseSubject.CODE_SERVICE_AVAILABILITY, "Delivery Areas"),
			new Selection(CrmCaseSubject.CODE_GENERAL_INFO, "General Feedback"),
			new Selection(CrmCaseSubject.CODE_CORPORATE_INFO , "Corporate/Commercial Services")	,
			new Selection(CrmCaseSubject.CODE_GIFT_CARD_INFO , "Gift Cards"),
			new Selection(CrmCaseSubject.CODE_IPHONE_INFO , "FreshDirect iPhone app"),
			new Selection(CrmCaseSubject.CODE_GENERAL_INFO , "Fresh Meal Vending")
			};

	private static class ContactForm implements WebFormI {

		private final boolean unknownCustomer;

		private int subjectIndex;
		private String subject;
		private PrimaryKey customerPK;
		private PrimaryKey salePK;
		private String summary;
		private String message;
		private String email;
		private String firstname;
		private String lastname;
		private PhoneNumber homePhone;
		private PhoneNumber workPhone;
		private PhoneNumber altPhone;
		private String confirmMessage;

		public ContactForm(boolean unknownCustomer) {
			this.unknownCustomer = unknownCustomer;
		}

		public void populateForm(HttpServletRequest request) {
			try {
				subjectIndex = Integer.parseInt(request.getParameter("subject"));
			} catch (NumberFormatException Ex) {
				subjectIndex = -1;
			}

			LOGGER.debug(">>> subject index " + subjectIndex);

			subject = subjectIndex > -1 ? selections[subjectIndex].getDescription() : null;

			LOGGER.debug(">>> subject " + subject);
			//actionType = this.getActionTypeByName(request.getParameter("actionTypeName"));
			//assignedAgentPK = CrmManagerSessionBean.loginAgent(ErpServicesProperties.getCrmSystemUserName(), ErpServicesProperties.getCrmSystemUserPassword()).getPK();

			String custId = NVL.apply(request.getParameter("customerPK"), "");
			customerPK = "".equals(custId) ? null : new PrimaryKey(custId);
			LOGGER.debug(">>> customer " + customerPK);

			String saleId = NVL.apply(request.getParameter("salePK"), "");
			salePK = "".equals(saleId) ? null : new PrimaryKey(saleId);
			LOGGER.debug(">>> order " + salePK);

			summary = subject != null ? "Web Form Inquiry: " + selections[subjectIndex].getDescription() : "";

			//set desired confirm msg
			if (subjectIndex == 0) {
				confirmMessage = "a";
			} else if (subjectIndex != 3 && subjectIndex != 5) {
				confirmMessage = "b";
			}
			LOGGER.debug(">>> confirm " + confirmMessage);
			/* need related depts?
			String[] deptCodes = request.getParameterValues("departments");
			Set departments = new HashSet();
			if (deptCodes != null) {
				for (int i = 0; i < deptCodes.length; i++) {
					departments.add(CrmDepartment.getEnum(deptCodes[i]));
				}
			}*/

			message = getParam(request, "message");
			email = getParam(request, "email");
			firstname = getParam(request, "first_name");
			lastname = getParam(request, "last_name");
			homePhone = new PhoneNumber(getParam(request, "home_phone"), getParam(request, "home_phone_ext"));
			workPhone = new PhoneNumber(getParam(request, "work_phone"), getParam(request, "work_phone_ext"));
			altPhone =  new PhoneNumber(getParam(request, "alt_phone"), getParam(request, "alt_phone_ext"));

		}
		
		protected String getParam(HttpServletRequest request, String fieldName) {
			return NVL.apply(request.getParameter(fieldName), "").trim();
		}

		public void validateForm(ActionResult result) {
			result.addError(subjectIndex < 0, "subject", SystemMessageList.MSG_REQUIRED);
			result.addError(subject == null || "".equals(subject), "subject", SystemMessageList.MSG_REQUIRED);
			result.addError(message.length() < 1, "message", SystemMessageList.MSG_REQUIRED);
			result.addError(message.length() > 2048, "message", "Message is over the maximum allowed limit.");
			
			if (this.unknownCustomer){ 
				if (email.length() < 1) {
					result.addError(true, "email", SystemMessageList.MSG_REQUIRED);
				} else {
					result.addError(!EmailUtil.isValidEmailAddress(email), "email", SystemMessageList.MSG_EMAIL_FORMAT);
				}
				result.addError(firstname.length() < 1, "first_name", SystemMessageList.MSG_REQUIRED);
				result.addError(lastname.length() < 1, "last_name", SystemMessageList.MSG_REQUIRED);
				//LOGGER.debug(">>> FirstName " + firstname.length());
				result.addError((null != homePhone.getPhone() && homePhone.getPhone().length() > 15), "home_phone", "Phone Number is over the maximum allowed limit.");
				result.addError((null != workPhone.getPhone()&& workPhone.getPhone().length() > 15), "work_phone", "Phone Number is over the maximum allowed limit.");
				result.addError((null != altPhone.getPhone() && altPhone.getPhone().length() > 15), "alt_phone", "Phone Number is over the maximum allowed limit.");
			
				result.addError((null != homePhone.getExtension()&& homePhone.getExtension().length() > 6), "home_phone", "Extension is over the maximum allowed limit.");
				result.addError((null != workPhone.getExtension()&& workPhone.getExtension().length() > 6), "work_phone", "Extension is over the maximum allowed limit.");
				result.addError((null != altPhone.getExtension() && altPhone.getExtension().length() > 6), "alt_phone", "Extension is over the maximum allowed limit.");

			}
		}
		
		
		public String getNote() {
			//combine message & sender's info
			StringBuffer sb = new StringBuffer();
			sb.append(message);
			LOGGER.debug(">>> message " + message);
			if (firstname.trim().length() > 1) {
				sb.append(firstname.toUpperCase().charAt(0));
				sb.append(firstname.substring(1, firstname.length()).toLowerCase());
			} else {
				sb.append(firstname);
			}
			sb.append(" ");
			if (lastname.trim().length() > 1) {
				sb.append(lastname.toUpperCase().charAt(0));
				sb.append(lastname.substring(1, lastname.length()).toLowerCase());
			} else {
				sb.append(lastname);
			}
			sb.append(" ( ").append( email ).append(" )");
			sb.append("Home: " + homePhone);
			if (homePhone.getExtension()  != null && !"".equals(homePhone.getExtension())) {
				sb.append(" ex." + homePhone.getExtension());
			}
			if (workPhone != null && !"".equals(workPhone)) {
				sb.append(" | Work: " + workPhone);
				if (workPhone.getExtension() != null && !"".equals(workPhone.getExtension())) {
					sb.append(" ex." + workPhone.getExtension());
				}
			}
			if (altPhone != null && !"".equals(altPhone)) {
				sb.append(" | Other: " + altPhone);
				if (altPhone.getExtension() != null && !"".equals(altPhone.getExtension())) {
					sb.append(" ex." + altPhone.getExtension());
				}
			}

			return sb.toString();
		
		}
	}

}
