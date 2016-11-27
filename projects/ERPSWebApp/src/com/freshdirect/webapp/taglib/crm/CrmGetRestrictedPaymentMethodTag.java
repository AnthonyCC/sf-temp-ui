package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.PaymentFraudManager;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodCriteria;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.framework.core.PrimaryKey;

public class CrmGetRestrictedPaymentMethodTag extends AbstractGetterTag {

	private static final String RESTRICTED_PAYMENT_METHOD_SESSION_NAME = "EDITING_RESTRICTED_PAYMENT_METHOD";
	private String restrictedPaymentMethodId = null;
	private String paymentMethodId = null;
	private String abaRouteNumber = null;
	private String accountNumber = null;
	private EnumBankAccountType bankAccountType = null;
	private EnumRestrictedPaymentMethodStatus status = EnumRestrictedPaymentMethodStatus.BAD;  // default to BAD accounts only

	public void setRestrictedPaymentMethodId(String restrictedPaymentMethodId) {
		this.restrictedPaymentMethodId = restrictedPaymentMethodId;
	}
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
	public void setStatus(String statusCode) {
		this.status = EnumRestrictedPaymentMethodStatus.getEnum(statusCode);
	}

	public void setAbaRouteNumber(String abaRouteNumber) {
		this.abaRouteNumber = abaRouteNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setBankAccountType(String bankAccountTypeCode) {
		this.bankAccountType = EnumBankAccountType.getEnum(bankAccountTypeCode);
	}

	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		RestrictedPaymentMethodModel restrictedPaymentMethod = (RestrictedPaymentMethodModel) session.getAttribute(RESTRICTED_PAYMENT_METHOD_SESSION_NAME);
		if (restrictedPaymentMethod == null || !restrictedPaymentMethod.getPK().getId().equalsIgnoreCase(this.restrictedPaymentMethodId)) {
			try{
				if (restrictedPaymentMethodId != null) {
					restrictedPaymentMethod = PaymentFraudManager.getRestrictedPaymentMethod(new PrimaryKey(this.restrictedPaymentMethodId));
				} else if (paymentMethodId != null) {
					restrictedPaymentMethod = PaymentFraudManager.getRestrictedPaymentMethodByPaymentMethodId(this.paymentMethodId, status);
					if (restrictedPaymentMethod == null) {
						return new RestrictedPaymentMethodModel();
					}
				} else if (abaRouteNumber != null && abaRouteNumber.length() >0 && accountNumber != null && accountNumber.length() > 0 && bankAccountType != null) {
					RestrictedPaymentMethodCriteria criteria = new RestrictedPaymentMethodCriteria();
					criteria.setAbaRouteNumber(abaRouteNumber);
					criteria.setAccountNumber(accountNumber);
					criteria.setBankAccountType(bankAccountType);
					criteria.setStatus(status);
					List list = PaymentFraudManager.getRestrictedPaymentMethods(criteria);
					if (list != null && list.size() > 0) {
						return list.get(0);
					} else {
						return new RestrictedPaymentMethodModel();						
					}
				}
				session.setAttribute(RESTRICTED_PAYMENT_METHOD_SESSION_NAME, restrictedPaymentMethod);
			}catch(FDResourceException e){
				throw new JspException(e);
			}
		}
		return restrictedPaymentMethod;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.payment.fraud.RestrictedPaymentMethodModel";
		}
	}

}
