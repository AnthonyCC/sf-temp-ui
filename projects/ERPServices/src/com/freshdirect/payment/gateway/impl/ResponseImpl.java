package com.freshdirect.payment.gateway.impl;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.Currency;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.TransactionType;


public class ResponseImpl implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7883959564421587820L;
	private String avsResponse;
	private String cvvResponse;
	private String authCode;
	private String rawRequest;
	private String rawResponse;
	private Request request;
	private String responseTime;
	private String statusMessage;
	private String statusCode;
	private boolean isAVSMatch;
	private boolean isCVVMatch;
	private boolean isApproved;
	private boolean isDeclined;
	private boolean isError;
	private boolean isRequestProcessed;
	private BillingInfo billingInfo;
	private String responseCode;
	private String responseCodeAlt;
	private String ewalletId;
	private String ewalletTxId;
	
	/**
	 * @return the ewalletTxId
	 */
	public String getEwalletTxId() {
		return ewalletTxId;
	}

	/**
	 * @param ewalletTxId the ewalletTxId to set
	 */
	public void setEwalletTxId(String ewalletTxId) {
		this.ewalletTxId = ewalletTxId;
	}

	public ResponseImpl(Request request) {
		this.request=request;
	}
	
	public String getAVSResponse() {
		return avsResponse;
	}
	public void setAVSResponse(String avsResponse) {
		this.avsResponse = avsResponse;
	}
	public String getCVVResponse() {
		return cvvResponse;
	}
	public void setCVVResponse(String cvvResponse) {
		this.cvvResponse = cvvResponse;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getRawRequest() {
		return rawRequest;
	}
	public void setRawRequest(String rawRequest) {
		this.rawRequest = rawRequest;
	}
	public String getRawResponse() {
		return rawResponse;
	}
	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}
	public Request getRequest() {
		return request;
	}
	
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public TransactionType getTransactionType() {
		return request.getTransactionType();
	}
	
	public boolean isAVSMatch() {
		return isAVSMatch;
	}
	public void setAVSMatch(boolean isAVSMatch) {
		this.isAVSMatch = isAVSMatch;
	}
	public boolean isCVVMatch() {
		return isCVVMatch;
	}
	public void setCVVMatch(boolean isCVVMatch) {
		this.isCVVMatch = isCVVMatch;
	}
	public boolean isApproved() {
		return isApproved;
	}
	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	public boolean isDeclined() {
		return isDeclined;
	}
	public void setDeclined(boolean isDeclined) {
		this.isDeclined = isDeclined;
	}
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	public boolean isRequestProcessed() {
		return isRequestProcessed;
	}
	public void setRequestProcessed(boolean isRequestProcessed) {
		this.isRequestProcessed = isRequestProcessed;
	}
	
	public Object getGatewayRequest() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getGatewayResponse() {
		// TODO Auto-generated method stub
		return null;
	}
	public double getAmount() {
		return getBillingInfo().getAmount();
	}
	public Currency getCurrency() {
		return request.getBillingInfo().getCurrency();
	}
	public Merchant getMerchant() {
		return request.getBillingInfo().getMerchant();
	}
	public PaymentMethod getPaymentMethod() {
		return getBillingInfo().getPaymentMethod();
	}
	public double getTax() {
		return getBillingInfo().getTax();
	}
	public String getTransactionID() {
		return getBillingInfo().getTransactionID();
	}
	public String getTransactionRef() {
		return getBillingInfo().getTransactionRef();
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode=statusCode;
	}
	
	public BillingInfo getBillingInfo() {
		return billingInfo;
	}
	public void setBillingInfo(BillingInfo billingInfo) {
		this.billingInfo=billingInfo;
	}

	public boolean isSuccess() {
		if(TransactionType.CC_VERIFY.equals(request.getTransactionType())) {
				String cvv=((CreditCard)request.getBillingInfo().getPaymentMethod()).getCVV();
				return StringUtil.isEmpty(cvv)?this.isApproved && this.isAVSMatch:this.isApproved && this.isAVSMatch&&this.isCVVMatch;
		}
		if(TransactionType.ACH_VERIFY.equals(request.getTransactionType()))
			return this.isApproved;
		else if(TransactionType.AUTHORIZE.equals(request.getTransactionType()))
			return isApproved;
		else if( TransactionType.ADD_PROFILE.equals(request.getTransactionType())||
				TransactionType.GET_PROFILE.equals(request.getTransactionType())||
				TransactionType.UPDATE_PROFILE.equals(request.getTransactionType())||
				TransactionType.DELETE_PROFILE.equals(request.getTransactionType()) || 
				TransactionType.VOID_CAPTURE.equals(request.getTransactionType()) ||
				TransactionType.REVERSE_AUTHORIZE.equals(request.getTransactionType())
				) { // as per chase paymentech support for void & reverse auth we just need to check for requestProcessed flag for success
			return isRequestProcessed;
		} else 
			return isApproved;
		
	}
	
	
	
	public void setResponseCode(String responseCode) {
		this.responseCode=responseCode;
	}
	public String getResponseCode() {
		return this.responseCode;
	}
	public void setResponseCodeAlt(String responseCodeAlt) {
		this.responseCodeAlt=responseCodeAlt;
	}
	public String getResponseCodeAlt() {
		return this.responseCodeAlt;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authCode == null) ? 0 : authCode.hashCode());
		result = prime * result
				+ ((avsResponse == null) ? 0 : avsResponse.hashCode());
		result = prime * result
				+ ((billingInfo == null) ? 0 : billingInfo.hashCode());
		result = prime * result
				+ ((cvvResponse == null) ? 0 : cvvResponse.hashCode());
		result = prime * result + (isAVSMatch ? 1231 : 1237);
		result = prime * result + (isApproved ? 1231 : 1237);
		result = prime * result + (isCVVMatch ? 1231 : 1237);
		result = prime * result + (isDeclined ? 1231 : 1237);
		result = prime * result + (isError ? 1231 : 1237);
		result = prime * result + (isRequestProcessed ? 1231 : 1237);
		result = prime * result
				+ ((rawRequest == null) ? 0 : rawRequest.hashCode());
		result = prime * result
				+ ((rawResponse == null) ? 0 : rawResponse.hashCode());
		result = prime * result + ((request == null) ? 0 : request.hashCode());
		result = prime * result
				+ ((responseTime == null) ? 0 : responseTime.hashCode());
		result = prime * result
				+ ((statusCode == null) ? 0 : statusCode.hashCode());
		result = prime * result
				+ ((statusMessage == null) ? 0 : statusMessage.hashCode());
		result = prime * result
		+ ((responseCode == null) ? 0 : responseCode.hashCode());
		result = prime * result
		+ ((responseCodeAlt == null) ? 0 : responseCodeAlt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ResponseImpl))
			return false;
		ResponseImpl other = (ResponseImpl) obj;
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
		if (billingInfo == null) {
			if (other.billingInfo != null)
				return false;
		} else if (!billingInfo.equals(other.billingInfo))
			return false;
		if (cvvResponse == null) {
			if (other.cvvResponse != null)
				return false;
		} else if (!cvvResponse.equals(other.cvvResponse))
			return false;
		if (isAVSMatch != other.isAVSMatch)
			return false;
		if (isApproved != other.isApproved)
			return false;
		if (isCVVMatch != other.isCVVMatch)
			return false;
		if (isDeclined != other.isDeclined)
			return false;
		if (isError != other.isError)
			return false;
		if (isRequestProcessed != other.isRequestProcessed)
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
		if (responseTime == null) {
			if (other.responseTime != null)
				return false;
		} else if (!responseTime.equals(other.responseTime))
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
		return true;
	}

	@Override
	public String toString() {
		return "ResponseImpl [authCode=" + authCode + ", avsResponse="
				+ avsResponse + ", billingInfo=" + billingInfo
				+ ", cvvResponse=" + cvvResponse + ", isAVSMatch=" + isAVSMatch
				+ ", isApproved=" + isApproved + ", isCVVMatch=" + isCVVMatch
				+ ", isDeclined=" + isDeclined + ", isError=" + isError
				+ ", isRequestProcessed=" + isRequestProcessed
				+ ", responseCode=" + responseCode
				+ ", responseCodeAlt=" + responseCodeAlt
				+ ", statusCode=" + statusCode + ", statusMessage=" + statusMessage 
				//+ ", rawRequest=" + rawRequest + ", rawResponse=" + rawResponse
				+ ", request=" + request + ", responseTime=" + responseTime+"]";
				
	}

	@Override
	public String getEwalletId() {
		return ewalletId;
	}

	@Override
	public void setEwalletId(String ewalletId) {
		this.ewalletId = ewalletId;
	}
	
}