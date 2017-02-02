package com.freshdirect.webapp.ajax.backoffice.data;


import java.io.Serializable;

public class OverriddenVariantsResponse implements Serializable {
	
	private String featureName;
	private String variant;
	private String options;
	/**
	 * @return the featureName
	 */
	public String getFeatureName() {
		return featureName;
	}
	/**
	 * @param featureName the featureName to set
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	/**
	 * @return the variant
	 */
	public String getVariant() {
		return variant;
	}
	/**
	 * @param variant the variant to set
	 */
	public void setVariant(String variant) {
		this.variant = variant;
	}
	/**
	 * @return the options
	 */
	public String getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(String options) {
		this.options = options;
	}
	
	
	

}
