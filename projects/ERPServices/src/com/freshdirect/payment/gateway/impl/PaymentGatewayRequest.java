package com.freshdirect.payment.gateway.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PaymentGatewayRequest implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1088005424996060672L;

	private  String transactionType;
	
	private BigDecimal amount;
	
	private BigDecimal taxAmount;
	
	private String currency;
	
	private String merchant;
	
	private String transactionId;
	
	private String transactionRef;
	
	private String transactionIndex;
		
	/** PaymentMethod **/
	
	private PaymentMethodData paymentMethod;
	


	/** Credit/Debit Card specific fields*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date expirationDate;
	
	private String cardType;
	
	private boolean isDebitCard;
	
	/**ECheck specific fields */
	
	private String bankAccountType;
	
	private String routingNumber;
	
	private String eStore;
	
	
	
	public PaymentMethodData getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethodData paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return the isDebitCard
	 */
	public boolean isDebitCard() {
		return isDebitCard;
	}

	/**
	 * @param isDebitCard the isDebitCard to set
	 */
	public void setDebitCard(boolean isDebitCard) {
		this.isDebitCard = isDebitCard;
	}

	/**
	 * @return the bankAccountType
	 */
	public String getBankAccountType() {
		return bankAccountType;
	}

	/**
	 * @param bankAccountType the bankAccountType to set
	 */
	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	/**
	 * @return the routingNumber
	 */
	public String getRoutingNumber() {
		return routingNumber;
	}

	/**
	 * @param routingNumber the routingNumber to set
	 */
	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getEStore() {
		return eStore;
	}

	public void setEStore(String eStore) {
		this.eStore = eStore;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public String getTransactionIndex() {
		return transactionIndex;
	}

	public void setTransactionIndex(String transactionIndex) {
		this.transactionIndex = transactionIndex;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((bankAccountType == null) ? 0 : bankAccountType.hashCode());
		result = prime * result
				+ ((cardType == null) ? 0 : cardType.hashCode());
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());

		result = prime * result + ((eStore == null) ? 0 : eStore.hashCode());
		result = prime * result
				+ ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + (isDebitCard ? 1231 : 1237);
		result = prime * result
				+ ((merchant == null) ? 0 : merchant.hashCode());

		result = prime * result
				+ ((routingNumber == null) ? 0 : routingNumber.hashCode());

		result = prime * result
				+ ((taxAmount == null) ? 0 : taxAmount.hashCode());
		result = prime * result
				+ ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime
				* result
				+ ((transactionIndex == null) ? 0 : transactionIndex.hashCode());
		result = prime * result
				+ ((transactionRef == null) ? 0 : transactionRef.hashCode());
		result = prime * result
				+ ((transactionType == null) ? 0 : transactionType.hashCode());
	
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PaymentGatewayRequest))
			return false;
		PaymentGatewayRequest other = (PaymentGatewayRequest) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (bankAccountType == null) {
			if (other.bankAccountType != null)
				return false;
		} else if (!bankAccountType.equals(other.bankAccountType))
			return false;
		if (cardType == null) {
			if (other.cardType != null)
				return false;
		} else if (!cardType.equals(other.cardType))
			return false;
		
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		
		if (eStore == null) {
			if (other.eStore != null)
				return false;
		} else if (!eStore.equals(other.eStore))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (isDebitCard != other.isDebitCard)
			return false;
		if (merchant == null) {
			if (other.merchant != null)
				return false;
		} else if (!merchant.equals(other.merchant))
			return false;
		
		if (routingNumber == null) {
			if (other.routingNumber != null)
				return false;
		} else if (!routingNumber.equals(other.routingNumber))
			return false;
		
		if (taxAmount == null) {
			if (other.taxAmount != null)
				return false;
		} else if (!taxAmount.equals(other.taxAmount))
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		if (transactionIndex == null) {
			if (other.transactionIndex != null)
				return false;
		} else if (!transactionIndex.equals(other.transactionIndex))
			return false;
		if (transactionRef == null) {
			if (other.transactionRef != null)
				return false;
		} else if (!transactionRef.equals(other.transactionRef))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PaymentGatewayRequest [transactionType=" + transactionType
				+ ", amount=" + amount + ", taxAmount=" + taxAmount
				+ ", currency=" + currency + ", merchant=" + merchant
				+ ", transactionId=" + transactionId + ", transactionRef="
				+ transactionRef + ", transactionIndex=" + transactionIndex
				+ ", expirationDate="
				+ expirationDate + ", cardType=" + cardType + ", isDebitCard="
				+ isDebitCard + ", bankAccountType=" + bankAccountType
				+ ", routingNumber=" + routingNumber + ", eStore=" + eStore
				+ "]";
	}
	
	

}
