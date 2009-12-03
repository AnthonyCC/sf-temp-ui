package com.freshdirect.mobileapi.model;

import com.freshdirect.fdstore.FDSalesUnit;

public class SalesUnit {
    FDSalesUnit fdSalesUnit;

    private boolean isDefault;

    public static SalesUnit wrap(FDSalesUnit su) {
        SalesUnit salesUnit = new SalesUnit();
        salesUnit.fdSalesUnit = su;
        return salesUnit;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getName() {
        return fdSalesUnit.getName();
    }

    public String getDescription() {
        return fdSalesUnit.getDescription();
    }

    public int getNumerator() {
        return fdSalesUnit.getNumerator();
    }

    public int getDenominator() {
        return fdSalesUnit.getDenominator();
    }

    public String getBaseUnit() {
        return fdSalesUnit.getBaseUnit();
    }

    public String getDescriptionQuantity() {
        return fdSalesUnit.getDescriptionQuantity();
    }

    public String getDescriptionUnit() {
        return fdSalesUnit.getDescriptionUnit();
    }
}
