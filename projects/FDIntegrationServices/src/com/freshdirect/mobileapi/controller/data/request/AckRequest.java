package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class AckRequest extends Message {
	public String getAckType() {
		return ackType;
	}
	public void setAckType(String ackType) {
		this.ackType = ackType;
	}
	public String getAppSource() {
		return appSource;
	}
	public void setAppSource(String appSource) {
		this.appSource = appSource;
	}
	public boolean isAcknowledge() {
		return acknowledge;
	}
	public void setAcknowledge(boolean acknowledge) {
		this.acknowledge = acknowledge;
	}
	private String ackType;
	private String appSource;
	private boolean acknowledge;

}
