package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;

public class CrmVSCampaignModel extends ModelSupport {	
	
	private static final long serialVersionUID = 1L;
	
	String campaignId;
	String campaignName;
	String campaignMenuId;
	String addByDate;
	String addByUser;
	String changeByDate;
	String changeByUser;
	String soundfileName;
	String soundFileText;
	String vsDetailsID;
	String route;
	String reasonId;
	String stopSequence;
	String redial;
	String startTime;
	String endTime;	
	int scheduledCalls;
	int deliveredCallsLive;
	int deliveredCallsAM;
	int undeliveredCalls;
	String callId;
	boolean updatable;
	List<String> phonenumbers = new ArrayList<String>();
	String phonenumber;
	int status;
	String saleId;
	String customerId;
	int delay;
	String delayMinutes;
	String lateIssueId;
	Hashtable<String, List> routeList;
	boolean manual;
	
	public boolean getManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

	public Hashtable<String, List> getRouteList() {
		return routeList;
	}
	
	public void setRouteList(Hashtable<String, List> routeList) {
		this.routeList = routeList;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public String getLateIssueId() {
		return lateIssueId;
	}


	public void setLateIssueId(String lateIssueId) {
		this.lateIssueId = lateIssueId;
	}
	
	public CrmVSCampaignModel() {
		super();	
	}


	public String getCampaignId() {
		return campaignId;
	}


	public String getDelayMinutes() {
		return delayMinutes;
	}


	public void setDelayMinutes(String delayMinutes) {
		this.delayMinutes = delayMinutes;
	}


	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}


	public String getCampaignName() {
		return campaignName;
	}


	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}


	public String getCampaignMenuId() {
		return campaignMenuId;
	}


	public void setCampaignMenuId(String campaignMenuId) {
		this.campaignMenuId = campaignMenuId;
	}


	public String getAddByDate() {
		return addByDate;
	}


	public void setAddByDate(String addByDate) {
		this.addByDate = addByDate;
	}


	public String getAddByUser() {
		return addByUser;
	}


	public void setAddByUser(String addByUser) {
		this.addByUser = addByUser;
	}


	public String getChangeByDate() {
		return changeByDate;
	}


	public void setChangeByDate(String changeByDate) {
		this.changeByDate = changeByDate;
	}


	public String getChangeByUser() {
		return changeByUser;
	}


	public void setChangeByUser(String changeByUser) {
		this.changeByUser = changeByUser;
	}


	public String getSoundfileName() {
		return soundfileName;
	}


	public void setSoundfileName(String soundfileName) {
		this.soundfileName = soundfileName;
	}


	public String getSoundFileText() {
		return soundFileText;
	}


	public void setSoundFileText(String soundFileText) {
		this.soundFileText = soundFileText;
	}
	
	public String getVsDetailsID() {
		return vsDetailsID;
	}


	public void setVsDetailsID(String vsDetailsID) {
		this.vsDetailsID = vsDetailsID;
	}


	public String getRoute() {
		return route;
	}


	public void setRoute(String route) {
		this.route = route;
	}


	public String getReasonId() {
		return reasonId;
	}


	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}


	public String getStopSequence() {
		return stopSequence;
	}


	public void setStopSequence(String stopSequence) {
		this.stopSequence = stopSequence;
	}


	public String getRedial() {
		return redial;
	}


	public void setRedial(String redial) {
		this.redial = redial;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getScheduledCalls() {
		return scheduledCalls;
	}


	public void setScheduledCalls(int scheduledCalls) {
		this.scheduledCalls = scheduledCalls;
	}


	public int getDeliveredCallsLive() {
		return deliveredCallsLive;
	}


	public void setDeliveredCallsLive(int deliveredCallsLive) {
		this.deliveredCallsLive = deliveredCallsLive;
	}


	public int getDeliveredCallsAM() {
		return deliveredCallsAM;
	}


	public void setDeliveredCallsAM(int deliveredCallsAM) {
		this.deliveredCallsAM = deliveredCallsAM;
	}


	public int getUndeliveredCalls() {
		return undeliveredCalls;
	}


	public void setUndeliveredCalls(int undeliveredCalls) {
		this.undeliveredCalls = undeliveredCalls;
	}


	public String getCallId() {
		return callId;
	}


	public void setCallId(String callId) {
		this.callId = callId;
	}


	public boolean isUpdatable() {
		return updatable;
	}


	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}


	public List<String> getPhonenumbers() {
		return phonenumbers;
	}


	public void setPhonenumbers(List<String> phonenumbers) {
		this.phonenumbers = phonenumbers;
	}


	public String getPhonenumber() {
		return phonenumber;
	}


	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getSaleId() {
		return saleId;
	}


	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	@Override
	public String toString() {
		return "CrmVSCampaignModel [addByDate=" + addByDate + ", addByUser="
				+ addByUser + ", callId=" + callId + ", campaignId="
				+ campaignId + ", campaignMenuId=" + campaignMenuId
				+ ", campaignName=" + campaignName + ", changeByDate="
				+ changeByDate + ", changeByUser=" + changeByUser
				+ ", customerId=" + customerId + ", deliveredCallsAM="
				+ deliveredCallsAM + ", deliveredCallsLive="
				+ deliveredCallsLive + ", endTime=" + endTime
				+ ", phonenumber=" + phonenumber + ", phonenumbers="
				+ phonenumbers + ", reasonId=" + reasonId + ", redial="
				+ redial + ", route=" + route + ", saleId=" + saleId
				+ ", scheduledCalls=" + scheduledCalls + ", soundFileText="
				+ soundFileText + ", soundfileName=" + soundfileName
				+ ", startTime=" + startTime + ", status=" + status
				+ ", stopSequence=" + stopSequence + ", undeliveredCalls="
				+ undeliveredCalls + ", updatable=" + updatable
				+ ", vsDetailsID=" + vsDetailsID + "]";
	}
	
}
