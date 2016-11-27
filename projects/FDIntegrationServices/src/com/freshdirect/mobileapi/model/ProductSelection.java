package com.freshdirect.mobileapi.model;

import java.util.Map;

import com.freshdirect.fdstore.customer.FDProductSelectionI;

public class ProductSelection {

    private FDProductSelectionI productSelection;

    /**
     * @param productSelection
     * @return
     */
    public static ProductSelection wrap(FDProductSelectionI productSelection) {
        ProductSelection newInstance = new ProductSelection();
        newInstance.productSelection = productSelection;
        return newInstance;
    }

    public float getQuantity() {
        return (float) productSelection.getQuantity();
    }

    public Map<String, String> getOptions() {
        return productSelection.getOptions();
    }

    public String getConfigurationDesc() {
        return productSelection.getConfigurationDesc();
    }

    public String getSalesUnit() {
        return productSelection.getSalesUnit();
    }

    public String getSkuCode() {
        return productSelection.getSku().getSkuCode();
    }

}
