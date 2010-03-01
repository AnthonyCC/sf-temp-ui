package com.freshdirect.mobileapi.model;

import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

public class VariationOption {
    String name;

    String description;

    String characteristicValuePrice;

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

    public String getCharacteristicValuePrice() {
        return characteristicValuePrice;
    }

    public void setCharacteristicValuePrice(String cvp) {
        this.characteristicValuePrice = cvp;
    }

    public String getName() {
        return name;
    }

    public boolean isUnAvailable() {
        boolean unAvailable = false;
        String optSkuCode = option.getAttribute(EnumAttributeName.SKUCODE);
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