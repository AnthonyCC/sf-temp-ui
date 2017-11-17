package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;

public class VariationOption {
    String name;

    String description;

    String characteristicValuePrice;

    String id;
    
    List<String> includedProducts;
    
    FDVariationOption option;

    public VariationOption(FDVariationOption option) {
        this.option = option;
        this.name = option.getName();
        this.description = option.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<String> getIncludedProducts() {
        return includedProducts;
    }

    public void setIncludedProducts(List<ProductModel> includedProducts) {
    	if(includedProducts != null && !includedProducts.isEmpty()){
    		this.includedProducts = new ArrayList<String>();
    		for(int i = 0; i < includedProducts.size(); i++){
    			this.includedProducts.add(includedProducts.get(i).getContentKey().getId());
    		}
    	}
    }

    public String getCharacteristicValuePrice() {
        return characteristicValuePrice;
    }

    public void setCharacteristicValuePrice(String cvp) {
        this.characteristicValuePrice = cvp;
    }

    public String getName() {
        return name;
    }

    public String getId(){
    	return this.id;
    }
    
    public void setId(String id){
    	this.id = id;
    }
    
    public boolean isUnAvailable() {
        boolean unAvailable = false;
        String optSkuCode = option.getSkuCode();
        ProductModel pm;
        try {
            pm = ContentFactory.getInstance().getProduct(optSkuCode);
            if (pm == null) {
                unAvailable = true;
            } else {
                SkuModel sku = pm.getSku(optSkuCode);
                if (sku == null || sku.isUnavailable()) {
                    unAvailable = true;
                }
            }
        } catch (FDSkuNotFoundException e) {
            unAvailable = true;
        }
        return unAvailable;
    }

}