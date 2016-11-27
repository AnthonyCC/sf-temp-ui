package com.freshdirect.mobileapi.controller.data;

/**
 * Label information for displaying quantity field on screen
 * @author fgarcia
 *
 */
public class ProductQuantity {
    private String quantityText;

    private String salesUnitLabel;

    public String getQuantityText() {
        return quantityText;
    }

    public void setQuantityText(String quantityText) {
        this.quantityText = quantityText;
    }

    public String getSalesUnitLabel() {
        return salesUnitLabel;
    }

    public void setSalesUnitLabel(String salesUnitLabel) {
        this.salesUnitLabel = salesUnitLabel;
    }

}
