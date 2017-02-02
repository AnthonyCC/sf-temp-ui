package com.freshdirect.webapp.ajax.backoffice.data;

import java.io.Serializable;

public class SkuModelResponse implements Serializable{

	private ProductModelResponse productModelResponse;
	private String skuCodeId;
	private String skuName;
	private String contentName;
	private String contentType;
	private String parentId;
	private String fullName;
	private String contentKey;

	public String getSkuCodeId() {
		return skuCodeId;
	}

	public void setSkuCodeId(String skuCodeId) {
		this.skuCodeId = skuCodeId;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getContentKey() {
		return contentKey;
	}

	public void setContentKey(String contentKey) {
		this.contentKey = contentKey;
	}

	public ProductModelResponse getProductModelResponse() {
		return productModelResponse;
	}

	public void setProductModelResponse(ProductModelResponse productModelResponse) {
		this.productModelResponse = productModelResponse;
	}

}
