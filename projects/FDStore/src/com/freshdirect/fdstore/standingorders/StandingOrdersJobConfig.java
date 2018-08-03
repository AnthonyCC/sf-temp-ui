package com.freshdirect.fdstore.standingorders;

import java.io.Serializable;

public class StandingOrdersJobConfig implements Serializable {
	
	private boolean isSendReportEmail = true;
	private boolean isSendReminderNotificationEmail = true;
	private boolean createIfSoiExistsForWeek = false;
	private boolean forceCapacity = true;
	
	public boolean isSendReportEmail() {
		return isSendReportEmail;
	}
	public void setSendReportEmail(boolean isSendReportEmail) {
		this.isSendReportEmail = isSendReportEmail;
	}
	public boolean isSendReminderNotificationEmail() {
		return isSendReminderNotificationEmail;
	}
	public void setSendReminderNotificationEmail(
			boolean isSendReminderNotificationEmail) {
		this.isSendReminderNotificationEmail = isSendReminderNotificationEmail;
	}
	public boolean isCreateIfSoiExistsForWeek() {
		return createIfSoiExistsForWeek;
	}
	public void setCreateIfSoiExistsForWeek(boolean createIfSoiExistsForWeek) {
		this.createIfSoiExistsForWeek = createIfSoiExistsForWeek;
	}
	
	public boolean isForceCapacity() {
		return forceCapacity;
	}
	public void setForceCapacity(boolean forceCapacity) {
		this.forceCapacity = forceCapacity;
	}
	
	@Override
	public String toString() {
		return "StandingOrdersJobConfig [isSendReportEmail="
				+ isSendReportEmail + ", isSendReminderNotificationEmail="
				+ isSendReminderNotificationEmail
				+ ", createIfSoiExistsForWeek=" + createIfSoiExistsForWeek
				+ ", forceCapacity=" + forceCapacity + "]";
	}
	
	
}
