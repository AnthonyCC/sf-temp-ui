package com.freshdirect.webapp.taglib.crm;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.common.customer.*;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.fraud.EnumRestrictedPatternType;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.EnumRestrictionReason;
import com.freshdirect.payment.fraud.PaymentFraudManager;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmRestrictedPaymentMethodControllerTag extends AbstractControllerTag {

	private RestrictedPaymentMethodModel restrictedPaymentMethod;

	public void setRestrictedPaymentMethod(RestrictedPaymentMethodModel restrictedPaymentMethod) {
		this.restrictedPaymentMethod = restrictedPaymentMethod;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {
			if (this.getActionName().equalsIgnoreCase("delete")) {
				this.deleteRestrictedPaymentMethod(request, actionResult);
			}
			if (this.getActionName().equalsIgnoreCase("add")) {
				this.addRestrictedPaymentMethod(request, actionResult);
			}
			if (this.getActionName().equalsIgnoreCase("edit")) {
				this.editRestrictedPaymentMethod(request, actionResult);
			}
		} catch (FDResourceException e) {
			throw new JspException(e);
		}

		return true;
	}

	private void editRestrictedPaymentMethod(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		this.populateRestrictedPaymentMethod(request);
		CrmAgentModel agent = CrmSession.getCurrentAgent(pageContext.getSession());
		this.restrictedPaymentMethod.setLastModifyUser(agent.getUserId());
		this.restrictedPaymentMethod.setLastModifyDate(new Date());
		validateRestrictedPaymentMethod(request, actionResult, agent);
		if (actionResult.isSuccess()) {
			PaymentFraudManager.storeRestrictedPaymentMethod(restrictedPaymentMethod);
		}
	}

	private void addRestrictedPaymentMethod(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		this.populateRestrictedPaymentMethod(request);
		CrmAgentModel agent = CrmSession.getCurrentAgent(pageContext.getSession());
		this.restrictedPaymentMethod.setCreateUser(agent.getUserId());
		this.restrictedPaymentMethod.setCreateDate(new Date());
		validateRestrictedPaymentMethod(request, actionResult, agent);
		if (actionResult.isSuccess()) {
			ErpPaymentMethodModel paymentMethod = (ErpPaymentMethodModel)PaymentFraudManager.getPaymentMethodByAccountInfo(restrictedPaymentMethod);			
			if (paymentMethod != null) { 
				if (paymentMethod.getPK() != null) {
					restrictedPaymentMethod.setPaymentMethodId(paymentMethod.getPK().getId());
				}
				restrictedPaymentMethod.setCustomerId(paymentMethod.getCustomerId());
			}
			PaymentFraudManager.createRestrictedPaymentMethod(restrictedPaymentMethod);
		}		
	}

	private void deleteRestrictedPaymentMethod(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		String id = NVL.apply(request.getParameter("restrict_payment_method_id"), ""); 
		this.restrictedPaymentMethod.setId(id);
		this.restrictedPaymentMethod.setPK(new PrimaryKey(id));
		CrmAgentModel agent = CrmSession.getCurrentAgent(pageContext.getSession());
		validateRestrictedPaymentMethod(request, actionResult, agent);		
		if (actionResult.isSuccess()) {
			PaymentFraudManager.removeRestrictedPaymentMethod(new PrimaryKey(this.restrictedPaymentMethod.getId()), agent.getUserId());
		}
	}

	private void populateRestrictedPaymentMethod(HttpServletRequest request) {

		this.restrictedPaymentMethod.setId(NVL.apply(request.getParameter("restrict_payment_method_id"), ""));
		this.restrictedPaymentMethod.setFirstName(NVL.apply(request.getParameter("first_name"), ""));
		this.restrictedPaymentMethod.setLastName(NVL.apply(request.getParameter("last_name"), ""));
		String paymentMethodType = NVL.apply(request.getParameter("payment_method_type"), "");
		if (!"".equalsIgnoreCase(paymentMethodType)) {
			this.restrictedPaymentMethod.setPaymentMethodType(EnumPaymentMethodType.getEnum(paymentMethodType));
		} else {
			this.restrictedPaymentMethod.setPaymentMethodType(EnumPaymentMethodType.ECHECK);
		}
		String status = NVL.apply(request.getParameter("status"), "");
		if (!"".equalsIgnoreCase(status)) {
			this.restrictedPaymentMethod.setStatus(EnumRestrictedPaymentMethodStatus.getEnum(status));
		} else {
			this.restrictedPaymentMethod.setStatus(EnumRestrictedPaymentMethodStatus.BAD);			
		}
		this.restrictedPaymentMethod.setSource(EnumTransactionSource.CUSTOMER_REP);

		this.restrictedPaymentMethod.setCustomerId(NVL.apply(request.getParameter("customer_id"), ""));

		String cardType = NVL.apply(request.getParameter("card_type"), ""); 
		this.restrictedPaymentMethod.setCardType(EnumCardType.getCardType(cardType));
		String expMonth = NVL.apply(request.getParameter("expiration_month"), "");
		String expYear = NVL.apply(request.getParameter("expiration_year"), "");
		if (!"".equals(expYear) && !"".equals(expMonth)) {
			SimpleDateFormat sf = new SimpleDateFormat("MMyyyy");
			Date date = sf.parse(expMonth.trim() + expYear.trim(), new ParsePosition(0));
			Calendar expCal = new GregorianCalendar();
			expCal.setTime(date);
			expCal.set(Calendar.DATE, expCal.getActualMaximum(Calendar.DATE));
			this.restrictedPaymentMethod.setExpirationDate(expCal.getTime());
		} else { 
			this.restrictedPaymentMethod.setExpirationDate(null);
		}
		
		this.restrictedPaymentMethod.setBankName(NVL.apply(request.getParameter("bank_name"), ""));		

		String abaRoutePatternType = NVL.apply(request.getParameter("aba_route_pattern_type"), "");
		this.restrictedPaymentMethod.setAbaRoutePatternType(EnumRestrictedPatternType.getEnum(abaRoutePatternType));
		this.restrictedPaymentMethod.setAbaRouteNumber(NVL.apply(request.getParameter("aba_route_number"), ""));		

		String accountPatternType = NVL.apply(request.getParameter("account_pattern_type"), "");
		this.restrictedPaymentMethod.setAccountPatternType(EnumRestrictedPatternType.getEnum(accountPatternType));
		this.restrictedPaymentMethod.setAccountNumber(NVL.apply(request.getParameter("account_number"), ""));		
		String bankAccountType = NVL.apply(request.getParameter("bank_account_type"), ""); 
		this.restrictedPaymentMethod.setBankAccountType(EnumBankAccountType.getEnum(bankAccountType));

		this.restrictedPaymentMethod.setCaseId(NVL.apply(request.getParameter("case_id"), ""));		
		
		String reasonCode = NVL.apply(request.getParameter("reason_code"), "");
		this.restrictedPaymentMethod.setReason(EnumRestrictionReason.getEnum(reasonCode));		

		this.restrictedPaymentMethod.setNote(NVL.apply(request.getParameter("note"), ""));		
	}

	private void validateRestrictedPaymentMethod(HttpServletRequest request, ActionResult result, CrmAgentModel agent) {
		
		if (this.getActionName().equalsIgnoreCase("delete")) {
			if(agent == null || !agent.isSupervisor()){
				result.addError(true, "authentication", "Agent not authorized to delete restricted payment method.");
			}
			String password = NVL.apply(request.getParameter("password"), "");
			if("".equals(password)){
				result.addError(true, "password", SystemMessageList.MSG_REQUIRED);
			}
			try {
				CrmManager.getInstance().loginAgent(agent.getUserId(), password);
	 		} catch (FDResourceException e) {
				result.addError(true, "technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR);
			} catch (CrmAuthenticationException e) {
				result.addError(true, "authentication", "Password is wrong");
			}		
		}
		
		if (this.getActionName().equalsIgnoreCase("delete") || this.getActionName().equalsIgnoreCase("edit")) {
			if (this.restrictedPaymentMethod.getId() == null) {
				result.setError("Restricted Payment Method Primary Key is not set");
			}			
		}
		
		if (this.getActionName().equalsIgnoreCase("add") || this.getActionName().equalsIgnoreCase("edit")) {
			result.addError("".equals(this.restrictedPaymentMethod.getAbaRouteNumber()), "aba_route_number", "required");
			result.addError("".equals(this.restrictedPaymentMethod.getAccountNumber()), "account_number", "required");
			result.addError(this.restrictedPaymentMethod.getBankAccountType() == null, "bank_account_type", "required");
			result.addError("".equals(this.restrictedPaymentMethod.getFirstName()), "first_name", "required");
			result.addError("".equals(this.restrictedPaymentMethod.getLastName()), "last_name", "required");
			result.addError(this.restrictedPaymentMethod.getPaymentMethodType() == null, "payment_method_type", "required");
			result.addError(this.restrictedPaymentMethod.getReason() == null, "reason_code", "required");
			result.addError(this.restrictedPaymentMethod.getStatus() == null, "status", "required");
		}
		
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}

}
