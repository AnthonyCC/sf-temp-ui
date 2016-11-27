package com.freshdirect.sms.service;

import java.io.Serializable;

import com.freshdirect.fdstore.FDStoreProperties;

public class SmsConfigProvider implements Serializable {

	private static final long serialVersionUID = -5407264355744046165L;

	public static String getSTUrl() {
		return FDStoreProperties.getSingleTouchServiceURL();
	}
	
	public static String getSTUserName() {
		return FDStoreProperties.getSTUsername();
	}
	
	public static String getSTPassword() {
		return FDStoreProperties.getSTPassword();
	}	
	
	public static String getSTFdxUserName() {
		return FDStoreProperties.getSTFdxUsername();
	}
	
	public static String getSTFdxPassword() {
		return FDStoreProperties.getSTFdxPassword();
	}
	
	public static Integer getSTConnectionTimeoutPeriod() {
		return FDStoreProperties.getSTConnectionTimeoutPeriod();
	}

	public static int getSTReadTimeoutPeriod() {
		return FDStoreProperties.getRTConnectionTimeoutPeriod();
	}
}
