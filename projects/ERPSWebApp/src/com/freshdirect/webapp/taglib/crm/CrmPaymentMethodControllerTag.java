package com.freshdirect.webapp.taglib.crm;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmPaymentMethodControllerTag extends AbstractControllerTag {

	private ErpPaymentMethodI paymentMethod;

	public void setPaymentMethod(ErpPaymentMethodI paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {
			String actionName =this.getActionName();
			if ("deletePaymentMethod".equalsIgnoreCase(actionName)) {
				this.deletePaymentMethod(request, actionResult);
			}
			if ("addPaymentMethod".equalsIgnoreCase(actionName)) {
				this.addPaymentMethod(request, actionResult);
			}
			if ("editPaymentMethod".equalsIgnoreCase(actionName)) {
				this.editPaymentMethod(request, actionResult);
			}
		} catch (FDResourceException e) {
			throw new JspException(e);
		}

		return true;
	}

	private void editPaymentMethod(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		this.populatePaymentMethod(request, actionResult);
		FDUserI user = CrmSession.getUser(pageContext.getSession());
		PaymentMethodUtil.validatePaymentMethod(request, this.paymentMethod, actionResult, user);
		if (actionResult.isSuccess()) {
			if (CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)) {
				PaymentMethodUtil.editPaymentMethod(request, actionResult, this.paymentMethod);
			}
		}
	}

	private void addPaymentMethod(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		FDUserI user = CrmSession.getUser(pageContext.getSession());
		this.populatePaymentMethod(request, actionResult);

		PaymentMethodUtil.validatePaymentMethod(request, this.paymentMethod, actionResult, user);
		if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
	        String terms = request.getParameter(PaymentMethodName.TERMS);
	        actionResult.addError(
	    	        terms == null || terms.length() <= 0,
	    	        PaymentMethodName.TERMS,SystemMessageList.MSG_REQUIRED
	    	        );
			if (actionResult.isSuccess() && !PaymentMethodUtil.hasECheckAccount(user.getIdentity())) {
				paymentMethod.setIsTermsAccepted(true);
			}
		}
		if (actionResult.isSuccess()) {
			if (CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)) {
				PaymentMethodUtil.addPaymentMethod(request, actionResult, this.paymentMethod);
			}
		}
	}

	private void deletePaymentMethod(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		String paymentId = request.getParameter("deletePaymentId");
		if (paymentId == null || paymentId.length() <= 0) {
			actionResult.addError(
				new ActionError(
					"technical_difficulty",
					"Sorry, we're experiencing technical difficulties. Please try again later."));
		}
		if (actionResult.isSuccess()) {
			if (CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)) {
				PaymentMethodUtil.deletePaymentMethod(request, actionResult, paymentId);
			}
		}
	}

	private void populatePaymentMethod(HttpServletRequest request, ActionResult actionResult) {

		EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(request.getParameter(PaymentMethodName.PAYMENT_METHOD_TYPE));
		if (paymentMethod == null) {
			paymentMethod = PaymentManager.createInstance(paymentMethodType);
		}
		String month = NVL.apply(request.getParameter(PaymentMethodName.CARD_EXP_MONTH), "");
		String year = NVL.apply(request.getParameter(PaymentMethodName.CARD_EXP_YEAR), "");
		String cardType = NVL.apply(request.getParameter(PaymentMethodName.CARD_BRAND), "");
		String accountNumber = NVL.apply(request.getParameter(PaymentMethodName.ACCOUNT_NUMBER), "");
		
		if (!"".equals(year) && !"".equals(month)) {
			SimpleDateFormat sf = new SimpleDateFormat("MMyyyy");
			Date date = sf.parse(month.trim() + year.trim(), new ParsePosition(0));
			Calendar expCal = new GregorianCalendar();
			expCal.setTime(date);
			expCal.set(Calendar.DATE, expCal.getActualMaximum(Calendar.DATE));
			this.paymentMethod.setExpirationDate(expCal.getTime());
		} else { this.paymentMethod.setExpirationDate(null); }
		
        boolean verifyBankAccountNumber = (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()));

		if(accountNumber.startsWith("xxx")){
			accountNumber = paymentMethod.getAccountNumber();
			verifyBankAccountNumber = false;
		}

    	if (verifyBankAccountNumber) {        	
	            String accountNumberVerify = request.getParameter(PaymentMethodName.ACCOUNT_NUMBER_VERIFY);
		        // Check account number verify is entered correctly
		        actionResult.addError(
		        accountNumberVerify != null && !accountNumberVerify.equalsIgnoreCase(accountNumber),
				PaymentMethodName.ACCOUNT_NUMBER_VERIFY, SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER_VERIFY
				);
		        // Check account number verify
		        actionResult.addError(
		        accountNumberVerify == null || "".equals(accountNumberVerify),
				PaymentMethodName.ACCOUNT_NUMBER_VERIFY, SystemMessageList.MSG_REQUIRED
				);

		        // Check account number has at least 5 digits
		        String scrubbedAccountNumber = PaymentMethodUtil.scrubAccountNumber(accountNumber);

		        actionResult.addError(scrubbedAccountNumber.length() < 5,
				PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_ACCOUNT_NUMBER_LENGTH
				);
			
	    }
    	
    	
	
		this.paymentMethod.setName(request.getParameter(PaymentMethodName.ACCOUNT_HOLDER));
		this.paymentMethod.setAccountNumber(PaymentMethodUtil.scrubAccountNumber(accountNumber));
		this.paymentMethod.setAbaRouteNumber(request.getParameter(PaymentMethodName.ABA_ROUTE_NUMBER));
		this.paymentMethod.setBankAccountType(EnumBankAccountType.getEnum(request.getParameter(PaymentMethodName.BANK_ACCOUNT_TYPE)));
		this.paymentMethod.setBankName(request.getParameter(PaymentMethodName.BANK_NAME));
		this.paymentMethod.setCardType(EnumCardType.getCardType(cardType));	
		
		this.paymentMethod.setAddress1(NVL.apply(request.getParameter(EnumUserInfoName.BIL_ADDRESS_1.getCode()), "").trim());
		this.paymentMethod.setAddress2(NVL.apply(request.getParameter(EnumUserInfoName.BIL_ADDRESS_2.getCode()), "").trim());
		this.paymentMethod.setApartment(NVL.apply(request.getParameter(EnumUserInfoName.BIL_APARTMENT.getCode()), "").trim());
		this.paymentMethod.setCity(NVL.apply(request.getParameter(EnumUserInfoName.BIL_CITY.getCode()), "").trim());
		this.paymentMethod.setState(NVL.apply(request.getParameter(EnumUserInfoName.BIL_STATE.getCode()), "").trim());
		this.paymentMethod.setZipCode(NVL.apply(request.getParameter(EnumUserInfoName.BIL_ZIPCODE.getCode()), "").trim());
		this.paymentMethod.setCountry("US");		
		this.paymentMethod.setBypassAVSCheck(request.getParameter("bypass_avs")!=null);
		this.paymentMethod.setAvsCkeckFailed(false);
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
}
