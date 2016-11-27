package com.freshdirect.crm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import com.freshdirect.common.customer.EnumCardType;

public class CrmAuthInfo implements Serializable {
	
	
	private static final long serialVersionUID = -7116321612016667829L;
	private Date transactionTime;
	private String customerName;
	private String merchantId;
	private String address;
	private BigDecimal amount;
	private String approvalCode;
	private String authResponse;
	private String cvvResponseCode;
	private EnumCardType cardType;
	private String order;
	private String zipCheckReponse;
	private boolean isValidOrder;
	private String customerId;
	
	public String getWebOrder() {
		if(order==null) return "";
		if(order.indexOf("X")!=-1)
			return order.substring(0,order.indexOf("X"));
		return order;
	}
	public boolean isValidOrder() {
		return isValidOrder;
	}
	public void setValidOrder(boolean isValidOrder) {
		this.isValidOrder = isValidOrder;
	}
	public Date getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getApprovalCode() {
		return approvalCode;
	}
	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}
	public String getAuthResponse() {
		return authResponse;
	}
	public void setAuthResponse(String authResponse) {
		this.authResponse = authResponse;
	}
	public String getCvvResponseCode() {
		return cvvResponseCode;
	}
	public void setCvvResponseCode(String cvvResponseCode) {
		this.cvvResponseCode = cvvResponseCode;
	}
	public EnumCardType getCardType() {
		return cardType;
	}
	public void setCardType(EnumCardType cardType) {
		this.cardType = cardType;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getZipCheckReponse() {
		return zipCheckReponse;
	}
	public void setZipCheckReponse(String zipCheckReponse) {
		this.zipCheckReponse = zipCheckReponse;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((approvalCode == null) ? 0 : approvalCode.hashCode());
		result = prime * result
				+ ((authResponse == null) ? 0 : authResponse.hashCode());
		result = prime * result
				+ ((cardType == null) ? 0 : cardType.hashCode());
		result = prime * result
				+ ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result
				+ ((cvvResponseCode == null) ? 0 : cvvResponseCode.hashCode());
		result = prime * result
				+ ((merchantId == null) ? 0 : merchantId.hashCode());
		result = prime * result
		+ ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result
				+ ((transactionTime == null) ? 0 : transactionTime.hashCode());
		result = prime * result
				+ ((zipCheckReponse == null) ? 0 : zipCheckReponse.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrmAuthInfo other = (CrmAuthInfo) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (approvalCode == null) {
			if (other.approvalCode != null)
				return false;
		} else if (!approvalCode.equals(other.approvalCode))
			return false;
		if (authResponse == null) {
			if (other.authResponse != null)
				return false;
		} else if (!authResponse.equals(other.authResponse))
			return false;
		
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (cvvResponseCode == null) {
			if (other.cvvResponseCode != null)
				return false;
		} else if (!cvvResponseCode.equals(other.cvvResponseCode))
			return false;
		if (merchantId == null) {
			if (other.merchantId != null)
				return false;
		} else if (!merchantId.equals(other.merchantId))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (transactionTime == null) {
			if (other.transactionTime != null)
				return false;
		} else if (!transactionTime.equals(other.transactionTime))
			return false;
		if (zipCheckReponse == null) {
			if (other.zipCheckReponse != null)
				return false;
		} else if (!zipCheckReponse.equals(other.zipCheckReponse))
			return false;
		if(isValidOrder!=other.isValidOrder)return false;
		return true;
	}
	@Override
	public String toString() {
		return "CrmAuthInfo [address=" + address + ", amount=" + amount
				+ ", approvalCode=" + approvalCode + ", authResponse="
				+ authResponse + ", cardType=" + cardType + ", customerId="
				+ customerId + ", customerName=" + customerName
				+ ", cvvResponseCode=" + cvvResponseCode + ", isValidOrder="
				+ isValidOrder + ", merchantId=" + merchantId + ", order="
				+ order + ", transactionTime=" + transactionTime
				+ ", zipCheckReponse=" + zipCheckReponse + "]";
	}
	
	
	public void setCustomerId(String customerId) {
		if(customerId!=null )
			this.customerId = customerId.trim();
		this.customerId ="";
	}
	public String getCustomerId() {
		return customerId;
	}


	public final static Comparator<CrmAuthInfo> COMP_AMOUNT = new Comparator<CrmAuthInfo>() {
		public int compare(CrmAuthInfo c1, CrmAuthInfo c2) {
			if(null !=c1.getAmount() && null != c2.getAmount()){
				return c1.getAmount().compareTo(c2.getAmount());
			}else{
				return 0;
			}
		}
	};
	
	public final static Comparator<CrmAuthInfo> COMP_CUSTOMER_NAME = new Comparator<CrmAuthInfo>() {
		public int compare(CrmAuthInfo c1, CrmAuthInfo c2) {
			if(null !=c1.getCustomerName() && null != c2.getCustomerName()){
				return c1.getCustomerName().toLowerCase().compareTo(c2.getCustomerName().toLowerCase());
			}else{
				return 0;
			}
		}
	};
	
	
	
	public final static Comparator<CrmAuthInfo> COMP_TRANSACTION_TIME = new Comparator<CrmAuthInfo>() {
		public int compare(CrmAuthInfo c1, CrmAuthInfo c2) {
			if(null !=c1.getTransactionTime() && null != c2.getTransactionTime()){
				return c1.getTransactionTime().compareTo(c2.getTransactionTime());
			}else{
				return 0;
			}
		}
	};
	
	
	
	
	

}
