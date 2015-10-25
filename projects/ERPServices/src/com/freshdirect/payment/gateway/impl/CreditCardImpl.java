package com.freshdirect.payment.gateway.impl;

import java.util.Date;

import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.PaymentMethodType;

class CreditCardImpl extends PaymentMethodImpl implements CreditCard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9053278361999557159L;
	private String cvv;
	private CreditCardType creditCardType;
	private Date expirationDate;
	

	CreditCardImpl() {
		super(PaymentMethodType.CREDIT_CARD);
	}
	
	public CreditCardType getCreditCardType() {
		return creditCardType;
	}
	public void setCreditCardType(CreditCardType creditCardType) {
		this.creditCardType = creditCardType;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public String getCVV() {
		return cvv;
	}
	public void setCVV(String cvv) {
		this.cvv = cvv;
	}
	
	
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((creditCardType == null) ? 0 : creditCardType.hashCode());
		result = prime * result + ((cvv == null) ? 0 : cvv.hashCode());
		result = prime * result
				+ ((expirationDate == null) ? 0 : expirationDate.hashCode());
		return result;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCardImpl other = (CreditCardImpl) obj;
		if (creditCardType == null) {
			if (other.creditCardType != null)
				return false;
		} else if (!creditCardType.equals(other.creditCardType))
			return false;
		if (cvv == null) {
			if (other.cvv != null)
				return false;
		} else if (!cvv.equals(other.cvv))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CreditCardImpl [creditCardType=" + creditCardType + ", expirationDate=" + expirationDate + ", toString()="
				+ super.toString() + "]";
	}
	
}
