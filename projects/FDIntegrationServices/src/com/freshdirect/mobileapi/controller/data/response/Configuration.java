package com.freshdirect.mobileapi.controller.data.response;

public class Configuration {
	
	private boolean isAkamaiImageConvertorEnabled;
	private String apiCodeVersion;
	private String storeVersion;
	
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
	
	
}
