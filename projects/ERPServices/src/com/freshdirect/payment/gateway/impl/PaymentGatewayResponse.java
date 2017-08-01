package com.freshdirect.payment.gateway.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.freshdirect.payment.Result;

public class PaymentGatewayResponse extends Result  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4101659469198462522L;
	
	private String avsResponse;
	
	private String cvvResponse;
	
	private String authCode;
	
	private String rawRequest;
	
	private String rawResponse;
	
	private PaymentGatewayRequest request;
	
	private String responseTime;
	
	private String statusMessage;
	
	private String statusCode;
	
	private boolean isAVSMatch;
	
	private boolean isCVVMatch;
	
	private boolean isApproved;
	
	private boolean isDeclined;
	
	private boolean isError;
	
	private boolean isRequestProcessed;
	
	private String responseCode;
	
	private String responseCodeAlt;
	
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
	private Date expirationDate;
	
	private String cardType;
	
	private boolean isDebitCard;
	
	/**ECheck specific fields */
	
	private String bankAccountType;
	
	private String routingNumber;


	/**
	 * @return the avsResponse
	 */
	public String getAvsResponse() {
		return avsResponse;
	}

	/**
	 * @param avsResponse the avsResponse to set
	 */
	public void setAvsResponse(String avsResponse) {
		this.avsResponse = avsResponse;
	}

	/**
	 * @return the cvvResponse
	 */
	public String getCvvResponse() {
		return cvvResponse;
	}

	/**
	 * @param cvvResponse the cvvResponse to set
	 */
	public void setCvvResponse(String cvvResponse) {
		this.cvvResponse = cvvResponse;
	}

	/**
	 * @return the authCode
	 */
	public String getAuthCode() {
		return authCode;
	}

	/**
	 * @param authCode the authCode to set
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/**
	 * @return the rawRequest
	 */
	public String getRawRequest() {
		return rawRequest;
	}

	/**
	 * @param rawRequest the rawRequest to set
	 */
	public void setRawRequest(String rawRequest) {
		this.rawRequest = rawRequest;
	}

	/**
	 * @return the rawResponse
	 */
	public String getRawResponse() {
		return rawResponse;
	}

	/**
	 * @param rawResponse the rawResponse to set
	 */
	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	/**
	 * @return the request
	 */
	public PaymentGatewayRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(PaymentGatewayRequest request) {
		this.request = request;
	}

	/**
	 * @return the responseTime
	 */
	public String getResponseTime() {
		return responseTime;
	}

	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the isAVSMatch
	 */
	public boolean isAVSMatch() {
		return isAVSMatch;
	}

	/**
	 * @param isAVSMatch the isAVSMatch to set
	 */
	public void setAVSMatch(boolean isAVSMatch) {
		this.isAVSMatch = isAVSMatch;
	}

	/**
	 * @return the isCVVMatch
	 */
	public boolean isCVVMatch() {
		return isCVVMatch;
	}

	/**
	 * @param isCVVMatch the isCVVMatch to set
	 */
	public void setCVVMatch(boolean isCVVMatch) {
		this.isCVVMatch = isCVVMatch;
	}

	/**
	 * @return the isApproved
	 */
	public boolean isApproved() {
		return isApproved;
	}

	/**
	 * @param isApproved the isApproved to set
	 */
	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * @return the isDeclined
	 */
	public boolean isDeclined() {
		return isDeclined;
	}

	/**
	 * @param isDeclined the isDeclined to set
	 */
	public void setDeclined(boolean isDeclined) {
		this.isDeclined = isDeclined;
	}

	/**
	 * @return the isError
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * @param isError the isError to set
	 */
	public void setError(boolean isError) {
		this.isError = isError;
	}

	/**
	 * @return the isRequestProcessed
	 */
	public boolean isRequestProcessed() {
		return isRequestProcessed;
	}

	/**
	 * @param isRequestProcessed the isRequestProcessed to set
	 */
	public void setRequestProcessed(boolean isRequestProcessed) {
		this.isRequestProcessed = isRequestProcessed;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the responseCodeAlt
	 */
	public String getResponseCodeAlt() {
		return responseCodeAlt;
	}

	/**
	 * @param responseCodeAlt the responseCodeAlt to set
	 */
	public void setResponseCodeAlt(String responseCodeAlt) {
		this.responseCodeAlt = responseCodeAlt;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the taxAmount
	 */
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	/**
	 * @param taxAmount the taxAmount to set
	 */
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the merchant
	 */
	public String getMerchant() {
		return merchant;
	}

	/**
	 * @param merchant the merchant to set
	 */
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the transactionRef
	 */
	public String getTransactionRef() {
		return transactionRef;
	}

	/**
	 * @param transactionRef the transactionRef to set
	 */
	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	/**
	 * @return the transactionIndex
	 */
	public String getTransactionIndex() {
		return transactionIndex;
	}

	/**
	 * @param transactionIndex the transactionIndex to set
	 */
	public void setTransactionIndex(String transactionIndex) {
		this.transactionIndex = transactionIndex;
	}
	
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((authCode == null) ? 0 : authCode.hashCode());
		result = prime * result
				+ ((avsResponse == null) ? 0 : avsResponse.hashCode());
		result = prime * result
				+ ((bankAccountType == null) ? 0 : bankAccountType.hashCode());
		result = prime * result
				+ ((cardType == null) ? 0 : cardType.hashCode());
		
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
	
		result = prime * result
				+ ((cvvResponse == null) ? 0 : cvvResponse.hashCode());
		result = prime * result
				+ ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + (isAVSMatch ? 1231 : 1237);
		result = prime * result + (isApproved ? 1231 : 1237);
		result = prime * result + (isCVVMatch ? 1231 : 1237);
		result = prime * result + (isDebitCard ? 1231 : 1237);
		result = prime * result + (isDeclined ? 1231 : 1237);
		result = prime * result + (isError ? 1231 : 1237);
		result = prime * result + (isRequestProcessed ? 1231 : 1237);
		result = prime * result
				+ ((merchant == null) ? 0 : merchant.hashCode());
		
	
		result = prime * result
				+ ((rawRequest == null) ? 0 : rawRequest.hashCode());
		result = prime * result
				+ ((rawResponse == null) ? 0 : rawResponse.hashCode());
		result = prime * result + ((request == null) ? 0 : request.hashCode());
		result = prime * result
				+ ((responseCode == null) ? 0 : responseCode.hashCode());
		result = prime * result
				+ ((responseCodeAlt == null) ? 0 : responseCodeAlt.hashCode());
		result = prime * result
				+ ((responseTime == null) ? 0 : responseTime.hashCode());
		result = prime * result
				+ ((routingNumber == null) ? 0 : routingNumber.hashCode());
		
		result = prime * result
				+ ((statusCode == null) ? 0 : statusCode.hashCode());
		result = prime * result
				+ ((statusMessage == null) ? 0 : statusMessage.hashCode());
		result = prime * result
				+ ((taxAmount == null) ? 0 : taxAmount.hashCode());
		result = prime * result
				+ ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime
				* result
				+ ((transactionIndex == null) ? 0 : transactionIndex.hashCode());
		result = prime * result
				+ ((transactionRef == null) ? 0 : transactionRef.hashCode());
		
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
		if (!(obj instanceof PaymentGatewayResponse))
			return false;
		PaymentGatewayResponse other = (PaymentGatewayResponse) obj;
		
	
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (authCode == null) {
			if (other.authCode != null)
				return false;
		} else if (!authCode.equals(other.authCode))
			return false;
		if (avsResponse == null) {
			if (other.avsResponse != null)
				return false;
		} else if (!avsResponse.equals(other.avsResponse))
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
		
		if (cvvResponse == null) {
			if (other.cvvResponse != null)
				return false;
		} else if (!cvvResponse.equals(other.cvvResponse))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (isAVSMatch != other.isAVSMatch)
			return false;
		if (isApproved != other.isApproved)
			return false;
		if (isCVVMatch != other.isCVVMatch)
			return false;
		if (isDebitCard != other.isDebitCard)
			return false;
		if (isDeclined != other.isDeclined)
			return false;
		if (isError != other.isError)
			return false;
		if (isRequestProcessed != other.isRequestProcessed)
			return false;
		if (merchant == null) {
			if (other.merchant != null)
				return false;
		} else if (!merchant.equals(other.merchant))
			return false;
		
		
		if (rawRequest == null) {
			if (other.rawRequest != null)
				return false;
		} else if (!rawRequest.equals(other.rawRequest))
			return false;
		if (rawResponse == null) {
			if (other.rawResponse != null)
				return false;
		} else if (!rawResponse.equals(other.rawResponse))
			return false;
		if (request == null) {
			if (other.request != null)
				return false;
		} else if (!request.equals(other.request))
			return false;
		if (responseCode == null) {
			if (other.responseCode != null)
				return false;
		} else if (!responseCode.equals(other.responseCode))
			return false;
		if (responseCodeAlt == null) {
			if (other.responseCodeAlt != null)
				return false;
		} else if (!responseCodeAlt.equals(other.responseCodeAlt))
			return false;
		if (responseTime == null) {
			if (other.responseTime != null)
				return false;
		} else if (!responseTime.equals(other.responseTime))
			return false;
		if (routingNumber == null) {
			if (other.routingNumber != null)
				return false;
		} else if (!routingNumber.equals(other.routingNumber))
			return false;
		
		if (statusCode == null) {
			if (other.statusCode != null)
				return false;
		} else if (!statusCode.equals(other.statusCode))
			return false;
		if (statusMessage == null) {
			if (other.statusMessage != null)
				return false;
		} else if (!statusMessage.equals(other.statusMessage))
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
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PaymentGatewayResponse [avsResponse=" + avsResponse
				+ ", cvvResponse=" + cvvResponse + ", authCode=" + authCode
				+ ", rawRequest=" + rawRequest + ", rawResponse=" + rawResponse
				+ ", request=" + request + ", responseTime=" + responseTime
				+ ", statusMessage=" + statusMessage + ", statusCode="
				+ statusCode + ", isAVSMatch=" + isAVSMatch + ", isCVVMatch="
				+ isCVVMatch + ", isApproved=" + isApproved + ", isDeclined="
				+ isDeclined + ", isError=" + isError + ", isRequestProcessed="
				+ isRequestProcessed + ", responseCode=" + responseCode
				+ ", responseCodeAlt=" + responseCodeAlt + ", amount=" + amount
				+ ", taxAmount=" + taxAmount + ", currency=" + currency
				+ ", merchant=" + merchant + ", transactionId=" + transactionId
				+ ", transactionRef=" + transactionRef + ", transactionIndex="
				+ transactionIndex + ",expirationDate="
				+ expirationDate + ", cardType=" + cardType + ", isDebitCard="
				+ isDebitCard + ", bankAccountType=" + bankAccountType
				+ ", routingNumber=" + routingNumber + "]";
	}


	

}
