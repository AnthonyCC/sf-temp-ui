package com.freshdirect.webapp.ajax.backoffice.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BackOfficeResponse implements Serializable{
	
	private SkuModelResponse skuModelResponse;
	private CategoryModelResponse categoryModelResponse;
	private ProductModelResponse productModelResponse;
	private List<SkuModelResponse> skuModelResponseList;
	private Map<String, DomainValueModelResponse> domainValueModelResponse;
	private String skuInfo;
	
	public SkuModelResponse getSkuModelResponse() {
		return skuModelResponse;
	}

	public void setSkuModelResponse(SkuModelResponse skuModelResponse) {
		this.skuModelResponse = skuModelResponse;
	}

	public CategoryModelResponse getCategoryModelResponse() {
		return categoryModelResponse;
	}

	public void setCategoryModelResponse(CategoryModelResponse categoryModelResponse) {
		this.categoryModelResponse = categoryModelResponse;
	}

	public ProductModelResponse getProductModelResponse() {
		return productModelResponse;
	}

	public void setProductModelResponse(ProductModelResponse productModelResponse) {
		this.productModelResponse = productModelResponse;
	}

	public List<SkuModelResponse> getSkuModelResponseList() {
		return skuModelResponseList;
	}

	public void setSkuModelResponseList(List<SkuModelResponse> skuModelResponseList) {
		this.skuModelResponseList = skuModelResponseList;
	}

	public Map<String, DomainValueModelResponse> getDomainValueModelResponse() {
		return domainValueModelResponse;
	}

	public void setDomainValueModelResponse(Map<String, DomainValueModelResponse> domainValueModelResponse) {
		this.domainValueModelResponse = domainValueModelResponse;
	}

	public String getSkuInfo() {
		return skuInfo;
	}

	public void setSkuInfo(String skuInfo) {
		this.skuInfo = skuInfo;
	}


}
