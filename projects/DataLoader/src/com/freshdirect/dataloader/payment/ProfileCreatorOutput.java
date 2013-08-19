
package com.freshdirect.dataloader.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;



public class ProfileCreatorOutput  {
	public ProfileCreatorOutput(String customerId, Date insertTimestamp,
			boolean profileCreated, String exceptionMsg) {
		super();
		this.customerId = customerId;
		this.insertTimestamp = insertTimestamp;
		this.profileCreated = profileCreated;
		this.exceptionMsg = exceptionMsg;
	}
	
	private static final Category LOGGER = LoggerFactory.getInstance(ProfileCreatorOutput.class);
	
	String customerId;
	Date insertTimestamp;
	boolean profileCreated;
	String exceptionMsg;
	List<ProfileCreatorOutputDetail> details = new ArrayList<ProfileCreatorOutputDetail>();
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
	public boolean isProfileCreated() {
		return profileCreated;
	}
	public void setProfileCreated(boolean profileCreated) {
		this.profileCreated = profileCreated;
	}
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	public List<ProfileCreatorOutputDetail> getDetails() {
		return details;
	}
	public void setDetails(List<ProfileCreatorOutputDetail> details) {
		this.details = details;
	}
	

}

