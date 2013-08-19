
package com.freshdirect.dataloader.payment;

import java.util.Date;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;



public class ProfileCreatorOutputDetail  {
	public ProfileCreatorOutputDetail(String customerId, Date insertTimestamp,
			String paymentMethodId, String profileId, String status,
			String exceptionMsg) {
		super();
		this.customerId = customerId;
		this.insertTimestamp = insertTimestamp;
		this.paymentMethodId = paymentMethodId;
		this.profileId = profileId;
		this.status = status;
		this.exceptionMsg = exceptionMsg;
	}
	private static final Category LOGGER = LoggerFactory.getInstance(ProfileCreatorOutputDetail.class);
	
	String customerId;
	Date insertTimestamp;
	String paymentMethodId;
	String profileId;
	String status;
	String exceptionMsg;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Date getInsertTimestamp() {
		return insertTimestamp;
	}
	public void setInsertTimestamp(Date insertTimestamp) {
		this.insertTimestamp = insertTimestamp;
	}
	public String getPaymentMethodId() {
		return paymentMethodId;
	}
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	

}

