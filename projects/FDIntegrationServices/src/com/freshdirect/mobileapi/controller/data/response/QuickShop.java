package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.model.ProductCatDept;

public class QuickShop extends Message {
	
	private List<String> productIds;
	
	private List<ProductCatDept> productCatDeptList;
	
    List<ProductConfiguration> products;
    
    private List<FilterOption> departments = new ArrayList<FilterOption>();
    
    private Integer totalResultCount = 0;

    public List<ProductConfiguration> getProducts() {
        return products;
    }

    public void setProducts(List<ProductConfiguration> products) {
        this.products = products;
    }
    
    public List<String> getProductIds(){
    	return this.productIds;
    }
    
    public void setProductIds(List<String> productIds){
    	this.productIds = productIds;
    }

	public List<FilterOption> getDepartments() {
		return departments;
	}

	public void setDepartments(List<FilterOption> departments) {
		this.departments = departments;
	}

	public Integer getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(Integer totalResultCount) {
		this.totalResultCount = totalResultCount;
	}
    
	public Integer getResultCount() {
        return products != null ? products.size() : 0;
    }

	public List<ProductCatDept> getProductCatDeptList() {
		return productCatDeptList;
	}

	public void setProductCatDeptList(List<ProductCatDept> productCatDeptList) {
		this.productCatDeptList = productCatDeptList;
	}
    
}
