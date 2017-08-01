package com.freshdirect.payment.gateway.impl;

import com.freshdirect.payment.gateway.BankAccountType;
import com.freshdirect.payment.gateway.ECheck;
import com.freshdirect.payment.gateway.PaymentMethodType;

public class ECheckImpl extends PaymentMethodImpl implements ECheck {

	private String  routingNumber;
	private BankAccountType bankAccountType;
	
	public ECheckImpl(){
		super(PaymentMethodType.ECHECK);
	}
	
	public String getRoutingNumber() {
		return routingNumber;
	}
	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}
	public BankAccountType getBankAccountType() {
		return bankAccountType;
	}
	public void setBankAccountType(BankAccountType bankAccountType) {
		this.bankAccountType = bankAccountType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((bankAccountType == null) ? 0 : bankAccountType.hashCode());
		result = prime * result
				+ ((routingNumber == null) ? 0 : routingNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ECheckImpl))
			return false;
		ECheckImpl other = (ECheckImpl) obj;
		if (bankAccountType == null) {
			if (other.bankAccountType != null)
				return false;
		} else if (!bankAccountType.equals(other.bankAccountType))
			return false;
		if (routingNumber == null) {
			if (other.routingNumber != null)
				return false;
		} else if (!routingNumber.equals(other.routingNumber))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ECheckImpl [bankAccountType=" + bankAccountType
				+ ", routingNumber=" + routingNumber + ", toString()="
				+ super.toString() + "]";
	}

}
