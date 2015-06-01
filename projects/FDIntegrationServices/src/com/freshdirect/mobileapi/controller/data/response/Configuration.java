package com.freshdirect.mobileapi.controller.data.response;

import java.io.Serializable;

public class Configuration implements Serializable {
	
	private boolean isAkamaiImageConvertorEnabled;
	private String apiCodeVersion;
	private String storeVersion;
	private boolean verifyPaymentMethod;
	private boolean ecouponEnabled;
	private String adServerUrl;
	private String tipRange;
	
	public boolean isVerifyPaymentMethod() {
		return verifyPaymentMethod;
	}
	public boolean isEcouponEnabled() {
		return ecouponEnabled;
	}
	public String getAdServerUrl() {
		return adServerUrl;
	}
	public String getTipRange() {
		return tipRange;
	}

	public boolean isAkamaiImageConvertorEnabled() {
		return isAkamaiImageConvertorEnabled;
	}
	public void setAkamaiImageConvertorEnabled(boolean isAkamaiImageConvertorEnabled) {
		this.isAkamaiImageConvertorEnabled = isAkamaiImageConvertorEnabled;
	}
	public String getApiCodeVersion() {
		return apiCodeVersion;
	}
	public void setApiCodeVersion(String apiCodeVersion) {
		this.apiCodeVersion = apiCodeVersion;
	}
	public String getStoreVersion() {
		return storeVersion;
	}
	public void setStoreVersion(String storeVersion) {
		this.storeVersion = storeVersion;
	}
	public void setVerifyPaymentMethod(boolean verifyPaymentMethod) {
		this.verifyPaymentMethod = verifyPaymentMethod;
	}
	public void setEcouponEnabled(boolean ecouponEnabled) {
		this.ecouponEnabled = ecouponEnabled;
	}
	public void setAdServerUrl(String adServerUrl) {
		this.adServerUrl = adServerUrl;
	}
	public void setTipRange(String tipRange) {
		this.tipRange = tipRange;
	}
	
	
}
