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

	

}