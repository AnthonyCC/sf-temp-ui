package com.freshdirect.webapp.ajax.backoffice.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class CRMSmartStoreInfoResponse implements Serializable {

	
	private List<VariantResponse> variants = new  ArrayList<VariantResponse>();
	
	private List<OverriddenVariantsResponse> overriddenVariants = new ArrayList<OverriddenVariantsResponse>();
	
	private String cohortName;
	
	private String emailId;
	

	/**
	 * @return the variants
	 */
	public List<VariantResponse> getVariants() {
		return variants;
	}

	/**
	 * @param variants the variants to set
	 */
	public void setVariants(List<VariantResponse> variants) {
		this.variants = variants;
	}

	/**
	 * @return the overriddenVariants
	 */
	public List<OverriddenVariantsResponse> getOverriddenVariants() {
		return overriddenVariants;
	}

	/**
	 * @param overriddenVariants the overriddenVariants to set
	 */
	public void setOverriddenVariants(
			List<OverriddenVariantsResponse> overriddenVariants) {
		this.overriddenVariants = overriddenVariants;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getCohortName() {
		return cohortName;
	}

	public void setCohortName(String cohortName) {
		this.cohortName = cohortName;
	}
		
	
}
