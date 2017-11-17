package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.storeapi.content.ProductModel;

public class VariationOption {
    /** SAP characteristic value name */
    private String name;

    private String description;
    
    private String productId;
    
    List<String> includedProducts;

    public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public List<String> getIncludedProducts() {
        return includedProducts;
    }

    public void setIncludedProducts(List<String> includedProducts) {
    	if(includedProducts != null && !includedProducts.isEmpty()){
    		this.includedProducts = new ArrayList<String>();
    		for(int i = 0; i < includedProducts.size(); i++){
    			this.includedProducts.add(includedProducts.get(i));
    		}
    	}
    }

	/** Chararcteristic value description */
    private String cvp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCvp() {
        return cvp;
    }

    public void setCvp(String cvp) {
        this.cvp = cvp;
    }

}
