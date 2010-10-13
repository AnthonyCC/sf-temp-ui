package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;

public class QuickShop extends Message {
	
    List<ProductConfiguration> products;
    
    private List<FilterOption> departments = new ArrayList<FilterOption>();

    public List<ProductConfiguration> getProducts() {
        return products;
    }

    public void setProducts(List<ProductConfiguration> products) {
        this.products = products;
    }

	public List<FilterOption> getDepartments() {
		return departments;
	}

	public void setDepartments(List<FilterOption> departments) {
		this.departments = departments;
	}
    
    
}
