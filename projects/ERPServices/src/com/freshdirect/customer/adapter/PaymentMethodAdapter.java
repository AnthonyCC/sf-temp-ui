package com.freshdirect.customer.adapter;

import java.util.Date;

import com.freshdirect.common.customer.PaymentMethodI;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.payment.EnumBankAccountType;

public class PaymentMethodAdapter implements PaymentMethodI {

	private ErpPaymentMethodI erpPaymentMethod;

	public PaymentMethodAdapter(ErpPaymentMethodI erpPaymentMethod) {
		this.erpPaymentMethod = erpPaymentMethod;
	}

	public EnumCardType getType() {
		return this.erpPaymentMethod.getCardType();
	}

	public String getNumber() {
		return this.erpPaymentMethod.getMaskedAccountNumber();
	}

	public Date getExpiration() {
		return this.erpPaymentMethod.getExpirationDate();
	}

	public String getName() {
		return this.erpPaymentMethod.getName();
	}

	public String getAbaRouteNumber() {
		return this.erpPaymentMethod.getAbaRouteNumber();
	}

	public String getBankName() {
		return this.erpPaymentMethod.getBankName();
	}
	
	public EnumBankAccountType getBankAccountType() {
		return this.erpPaymentMethod.getBankAccountType();
	}
}

