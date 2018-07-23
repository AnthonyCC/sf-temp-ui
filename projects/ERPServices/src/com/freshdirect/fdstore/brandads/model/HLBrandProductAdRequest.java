package com.freshdirect.fdstore.brandads.model;

import java.io.Serializable;

public class HLBrandProductAdRequest implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2993393411190405320L;
	private String searchKeyWord;
    private String userId;
    private String categoryId;
    private String customerId;
    private String platformSource;
    private String lat;
    private String pdUserId;
    private String productId;
    private String selectedServiceType;
    
    

    public String getSearchKeyWord() {
		return searchKeyWord;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getPlatformSource() {
		return platformSource;
	}

	public void setPlatformSource(String platformSource) {
		this.platformSource = platformSource;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getPdUserId() {
		return pdUserId;
	}

	public void setPdUserId(String pdUserId) {
		this.pdUserId = pdUserId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSelectedServiceType() {
		return selectedServiceType;
	}

	public void setSelectedServiceType(String selectedServiceType) {
		this.selectedServiceType = selectedServiceType;
	}

	
	

}