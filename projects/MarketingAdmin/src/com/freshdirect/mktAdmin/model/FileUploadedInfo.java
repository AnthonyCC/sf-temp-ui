package com.freshdirect.mktAdmin.model;

import java.util.Collection;

public class FileUploadedInfo {

	private String fileName;
	private Collection<RestrictedPromoCustomerModel> totalCustInfo;
	private Collection<RestrictedPromoCustomerModel> failedCustInfo;
	private boolean isSuccessful = false;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}	
	public boolean isSuccessful() {
		return isSuccessful;
	}
	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
	public Collection<RestrictedPromoCustomerModel> getTotalCustInfo() {
		return totalCustInfo;
	}
	public void setTotalCustInfo(
			Collection<RestrictedPromoCustomerModel> totalCustInfo) {
		this.totalCustInfo = totalCustInfo;
	}
	public Collection<RestrictedPromoCustomerModel> getFailedCustInfo() {
		return failedCustInfo;
	}
	public void setFailedCustInfo(
			Collection<RestrictedPromoCustomerModel> failedCustInfo) {
		this.failedCustInfo = failedCustInfo;
	}
	
	
}
