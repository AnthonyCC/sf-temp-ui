package com.freshdirect.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class TransactionModel implements Serializable{
	
	    public TransactionModel() {
		super();
	}
		private BigDecimal amount;
	    private String avsErrorResponseCode;
	    private String avsPostalCodeResponseCode;
	    private String avsStreetAddressResponseCode;
	    private String channel;
	    private Calendar createdAt;
	    private String currencyIsoCode;
	    private Map<String, String> customFields;
	    private String cvvResponseCode;
	    private String id;
	    private String merchantAccountId;
	    private String orderId;
	    private String planId;
	    private String processorAuthorizationCode;
	    private String processorResponseCode;
	    private String processorResponseText;
	    private String processorSettlementResponseCode;
	    private String processorSettlementResponseText;
	    private String additionalProcessorResponse;
	    private String voiceReferralNumber;
	    private String purchaseOrderNumber;
	    private Boolean recurring;
	    private String refundedTransactionId;
	    private String refundId;
	    private List<String> refundIds;
	    private String settlementBatchId;
	    private String subscriptionId;
	    private BigDecimal taxAmount;
	    private Boolean taxExempt;
	    private Calendar updatedAt;
	    private BigDecimal serviceFeeAmount;
	    private String paymentInstrumentType;
	    private String authorizedTransactionId;
	    private String status;
	    private String token;
	    private String authorizationId;
	    private String message;
	    private String captureId;
	    
	    public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		public String getAvsErrorResponseCode() {
			return avsErrorResponseCode;
		}
		public void setAvsErrorResponseCode(String avsErrorResponseCode) {
			this.avsErrorResponseCode = avsErrorResponseCode;
		}
		public String getAvsPostalCodeResponseCode() {
			return avsPostalCodeResponseCode;
		}
		public void setAvsPostalCodeResponseCode(String avsPostalCodeResponseCode) {
			this.avsPostalCodeResponseCode = avsPostalCodeResponseCode;
		}
		public String getAvsStreetAddressResponseCode() {
			return avsStreetAddressResponseCode;
		}
		public void setAvsStreetAddressResponseCode(String avsStreetAddressResponseCode) {
			this.avsStreetAddressResponseCode = avsStreetAddressResponseCode;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public Calendar getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(Calendar createdAt) {
			this.createdAt = createdAt;
		}
		public String getCurrencyIsoCode() {
			return currencyIsoCode;
		}
		public void setCurrencyIsoCode(String currencyIsoCode) {
			this.currencyIsoCode = currencyIsoCode;
		}
		public Map<String, String> getCustomFields() {
			return customFields;
		}
		public void setCustomFields(Map<String, String> customFields) {
			this.customFields = customFields;
		}
		public String getCvvResponseCode() {
			return cvvResponseCode;
		}
		public void setCvvResponseCode(String cvvResponseCode) {
			this.cvvResponseCode = cvvResponseCode;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getMerchantAccountId() {
			return merchantAccountId;
		}
		public void setMerchantAccountId(String merchantAccountId) {
			this.merchantAccountId = merchantAccountId;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getPlanId() {
			return planId;
		}
		public void setPlanId(String planId) {
			this.planId = planId;
		}
		public String getProcessorAuthorizationCode() {
			return processorAuthorizationCode;
		}
		public void setProcessorAuthorizationCode(String processorAuthorizationCode) {
			this.processorAuthorizationCode = processorAuthorizationCode;
		}
		public String getProcessorResponseCode() {
			return processorResponseCode;
		}
		public void setProcessorResponseCode(String processorResponseCode) {
			this.processorResponseCode = processorResponseCode;
		}
		public String getProcessorResponseText() {
			return processorResponseText;
		}
		public void setProcessorResponseText(String processorResponseText) {
			this.processorResponseText = processorResponseText;
		}
		public String getProcessorSettlementResponseCode() {
			return processorSettlementResponseCode;
		}
		public void setProcessorSettlementResponseCode(
				String processorSettlementResponseCode) {
			this.processorSettlementResponseCode = processorSettlementResponseCode;
		}
		public String getProcessorSettlementResponseText() {
			return processorSettlementResponseText;
		}
		public void setProcessorSettlementResponseText(
				String processorSettlementResponseText) {
			this.processorSettlementResponseText = processorSettlementResponseText;
		}
		public String getAdditionalProcessorResponse() {
			return additionalProcessorResponse;
		}
		public void setAdditionalProcessorResponse(String additionalProcessorResponse) {
			this.additionalProcessorResponse = additionalProcessorResponse;
		}
		public String getVoiceReferralNumber() {
			return voiceReferralNumber;
		}
		public void setVoiceReferralNumber(String voiceReferralNumber) {
			this.voiceReferralNumber = voiceReferralNumber;
		}
		public String getPurchaseOrderNumber() {
			return purchaseOrderNumber;
		}
		public void setPurchaseOrderNumber(String purchaseOrderNumber) {
			this.purchaseOrderNumber = purchaseOrderNumber;
		}
		public Boolean getRecurring() {
			return recurring;
		}
		public void setRecurring(Boolean recurring) {
			this.recurring = recurring;
		}
		public String getRefundedTransactionId() {
			return refundedTransactionId;
		}
		public void setRefundedTransactionId(String refundedTransactionId) {
			this.refundedTransactionId = refundedTransactionId;
		}
		public String getRefundId() {
			return refundId;
		}
		public void setRefundId(String refundId) {
			this.refundId = refundId;
		}
		public List<String> getRefundIds() {
			return refundIds;
		}
		public void setRefundIds(List<String> refundIds) {
			this.refundIds = refundIds;
		}
		public String getSettlementBatchId() {
			return settlementBatchId;
		}
		public void setSettlementBatchId(String settlementBatchId) {
			this.settlementBatchId = settlementBatchId;
		}
		public String getSubscriptionId() {
			return subscriptionId;
		}
		public void setSubscriptionId(String subscriptionId) {
			this.subscriptionId = subscriptionId;
		}
		public BigDecimal getTaxAmount() {
			return taxAmount;
		}
		public void setTaxAmount(BigDecimal taxAmount) {
			this.taxAmount = taxAmount;
		}
		public Boolean getTaxExempt() {
			return taxExempt;
		}
		public void setTaxExempt(Boolean taxExempt) {
			this.taxExempt = taxExempt;
		}
		public Calendar getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(Calendar updatedAt) {
			this.updatedAt = updatedAt;
		}
		public BigDecimal getServiceFeeAmount() {
			return serviceFeeAmount;
		}
		public void setServiceFeeAmount(BigDecimal serviceFeeAmount) {
			this.serviceFeeAmount = serviceFeeAmount;
		}
		public String getPaymentInstrumentType() {
			return paymentInstrumentType;
		}
		public void setPaymentInstrumentType(String paymentInstrumentType) {
			this.paymentInstrumentType = paymentInstrumentType;
		}
		public String getAuthorizedTransactionId() {
			return authorizedTransactionId;
		}
		public void setAuthorizedTransactionId(String authorizedTransactionId) {
			this.authorizedTransactionId = authorizedTransactionId;
		}
		public List<String> getPartialSettlementTransactionIds() {
			return partialSettlementTransactionIds;
		}
		public void setPartialSettlementTransactionIds(
				List<String> partialSettlementTransactionIds) {
			this.partialSettlementTransactionIds = partialSettlementTransactionIds;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getAuthorizationId() {
			return authorizationId;
		}
		public void setAuthorizationId(String authorizationId) {
			this.authorizationId = authorizationId;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getCaptureId() {
			return captureId;
		}
		public void setCaptureId(String captureId) {
			this.captureId = captureId;
		}
		private List<String> partialSettlementTransactionIds;
}
