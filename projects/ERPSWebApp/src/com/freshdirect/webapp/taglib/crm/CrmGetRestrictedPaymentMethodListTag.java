package com.freshdirect.webapp.taglib.crm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.EnumMonth;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.EnumRestrictionReason;
import com.freshdirect.payment.fraud.PaymentFraudManager;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodCriteria;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetRestrictedPaymentMethodListTag extends AbstractGetterTag {

	RestrictedPaymentMethodCriteria criteria = new RestrictedPaymentMethodCriteria();
	
	private String customerId = null;
	private EnumRestrictedPaymentMethodStatus status = EnumRestrictedPaymentMethodStatus.BAD;  // default to BAD accounts only
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public void setStatus(String statusCode) {
		this.status = EnumRestrictedPaymentMethodStatus.getEnum(statusCode);
	}

	protected Object getResult() throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		if ("true".equalsIgnoreCase(request.getParameter("display_results"))) {
			populateCriteria(request);		
			return PaymentFraudManager.getRestrictedPaymentMethods(criteria);
		} else if (customerId != null) {
			return PaymentFraudManager.getRestrictedPaymentMethodsByCustomerId(customerId, status);			
		}
		return new ArrayList();
	}

	private void populateCriteria(HttpServletRequest request) {
		String abaRouteNumber = request.getParameter("aba_route_number");
		if (abaRouteNumber != null && !"".equals(abaRouteNumber)) {
			criteria.setAbaRouteNumber(abaRouteNumber);
		}
		String accountNumber = request.getParameter("account_number");
		if (accountNumber != null && !"".equals(accountNumber)) {
			criteria.setAccountNumber(accountNumber);
		}
		String bankAccountType = request.getParameter("bank_account_type");
		if (bankAccountType != null && !"".equals(bankAccountType)) {
			criteria.setBankAccountType(EnumBankAccountType.getEnum(bankAccountType));
		}
		String firstName = request.getParameter("first_name");
		if (firstName != null && !"".equals(firstName)) {
			criteria.setFirstName(firstName);
		}
		String lastName = request.getParameter("last_name");
		if (lastName != null && !"".equals(lastName)) {
			criteria.setLastName(lastName);
		}
		Date createDate = null;
		String createDay = NVL.apply(request.getParameter("create_day"), "");
		String createMonth = NVL.apply(request.getParameter("create_month"), "");
		String createYear = NVL.apply(request.getParameter("create_year"), "");
		if (!"".equals(createYear) && !"".equals(createMonth) && !"".equals(createDay)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EnumMonth enumMonth = EnumMonth.getEnum(createMonth);
			if (enumMonth == null) { enumMonth = EnumMonth.JAN;}  
			if (createDay.length() < 2) {createDay = "0" + createDay;}
			try {
				createDate = sdf.parse(createDay.trim() + "-" +  enumMonth.getDescription().trim() + "-" + createYear.trim());
				criteria.setCreateDate(createDate);
			} catch (Exception e) {}
		}
		String reasonCode = NVL.apply(request.getParameter("reason_code"), "");
		criteria.setReason(EnumRestrictionReason.getEnum(reasonCode)); 
		String statusCode = NVL.apply(request.getParameter("status"), "");
		criteria.setStatus(EnumRestrictedPaymentMethodStatus.getEnum(statusCode)); 
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}

}
