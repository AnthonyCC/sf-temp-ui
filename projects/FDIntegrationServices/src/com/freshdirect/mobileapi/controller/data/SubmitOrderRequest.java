package com.freshdirect.mobileapi.controller.data;

/**
 * @author Aniwesh Vatsal
 *
 */
public class SubmitOrderRequest extends Message{

	private String deviceId;

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	
}
