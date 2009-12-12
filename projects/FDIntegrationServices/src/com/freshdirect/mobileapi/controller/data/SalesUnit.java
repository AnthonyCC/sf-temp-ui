package com.freshdirect.mobileapi.controller.data;

public class SalesUnit {
    private String name;

    private String description;

    private String shortDescription;

    private boolean defaultSalesUnit;

    public SalesUnit(String name) {
        this.name = name;
    }

    public SalesUnit() {

    }

    public SalesUnit(com.freshdirect.mobileapi.model.SalesUnit su) {
        name = su.getName();
        description = su.getDescription();
        defaultSalesUnit = su.isDefault();

        int lbIndex = description.indexOf("lb");
        if (lbIndex >= 0) {
            shortDescription = description.substring(0, lbIndex).trim();
        } else {
            shortDescription = description;
        }
        
        /*
        if (salesUnitOnly){
            double fraction = (double) su.getNumerator() / (double) su.getDenominator();
            if (fraction >= 1) {
                int integerPart = (int) fraction;
                fraction -= (double) integerPart;
                int remainderNumerator = (int) ((double) fraction * (double) su.getDenominator());
                if (remainderNumerator >= 1) {
                    shortDescription = integerPart + " " + remainderNumerator + "/" + su.getDenominator();
                } else {
                    shortDescription = String.valueOf(integerPart);
                }
            } else {
                shortDescription = su.getNumerator() + "/" + su.getDenominator();
            }
        }
        */
    }

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

    public boolean isDefaultSalesUnit() {
        return defaultSalesUnit;
    }

    public void setDefaultSalesUnit(boolean defaultSalesUnit) {
        this.defaultSalesUnit = defaultSalesUnit;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

}
