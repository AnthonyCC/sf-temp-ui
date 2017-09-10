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
	private String middleTierUrl;
	private boolean socialLoginEnabled;
	private boolean masterPassEnabled;
	private boolean payPalEnabled;
	private boolean DCSEnabled;
	

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
	public boolean isVerifyPaymentMethod() {
		return verifyPaymentMethod;
	}
	public void setVerifyPaymentMethod(boolean verifyPaymentMethod) {
		this.verifyPaymentMethod = verifyPaymentMethod;
	}
	public boolean isEcouponEnabled() {
		return ecouponEnabled;
	}
	public void setEcouponEnabled(boolean ecouponEnabled) {
		this.ecouponEnabled = ecouponEnabled;
	}
	public String getAdServerUrl() {
		return adServerUrl;
	}
	public void setAdServerUrl(String adServerUrl) {
		this.adServerUrl = adServerUrl;
	}
	public String getTipRange() {
		return tipRange;
	}
	public void setTipRange(String tipRange) {
		this.tipRange = tipRange;
	}
	public void setMiddleTierUrl(String url){
		this.middleTierUrl = url;
	}
	public String getMiddleTierUrl(){	
		return this.middleTierUrl;
	}
	public boolean isSocialLoginEnabled() {
		return socialLoginEnabled;
	}
	public void setSocialLoginEnabled(boolean socialLoginEnabled) {
		this.socialLoginEnabled = socialLoginEnabled;
	}
	public boolean isMasterPassEnabled() {
		return masterPassEnabled;
	}
	public void setMasterPassEnabled(boolean masterPassEnabled) {
		this.masterPassEnabled = masterPassEnabled;
	}
	public boolean isPayPalEnabled() {
		return payPalEnabled;
	}
	public void setPayPalEnabled(boolean payPalEnabled) {
		this.payPalEnabled = payPalEnabled;
	}
	public boolean isDCSEnabled() {
		return DCSEnabled;
	}
	public void setDCSEnabled(boolean DCSEnabled) {
		this.DCSEnabled = DCSEnabled;
	}
	
	
}
