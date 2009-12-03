package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.model.ProductDomain;


/**
 * Primary Domain
 * @author fgarcia
 *
 */
public class Domain {
    private String name;
    private List<Sku> skus = new ArrayList<Sku>();

    private boolean allSamePrice;

    private boolean allSameOption;
    
    public Domain() {
    }
    
    public Domain(ProductDomain productDomain){
        name = productDomain.getDomainValueLabel();
        allSamePrice = productDomain.isAllSamePrice();
        allSameOption = productDomain.isAllSameOption();
        for(com.freshdirect.mobileapi.model.Sku s : productDomain.getSkus()){
            Sku sku = Sku.wrap(s);
            skus.add(sku);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public boolean isAllSamePrice() {
        return allSamePrice;
    }

    public void setAllSamePrice(boolean allSamePrice) {
        this.allSamePrice = allSamePrice;
    }

    public boolean isAllSameOption() {
        return allSameOption;
    }

    public void setAllSameOption(boolean allSameOption) {
        this.allSameOption = allSameOption;
    }

}
