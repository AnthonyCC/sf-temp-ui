package com.freshdirect.mobileapi.controller.data;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.mobileapi.util.MobileApiProperties;

public class SalesUnit {
    private String name;

    private String description;

    private String shortDescription;

    private boolean defaultSalesUnit;
    
    /**[APPDEV-4023]-Unit Pricing - Mobile API Changes */
	private int unitPriceNumerator;
	
	private int unitPriceDenominator;
	
	private String unitPriceUOM;
	
	private String unitPriceDescription;

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
        if(MobileApiProperties.isMobileUnitPriceDisplayEnabled() && FDStoreProperties.isUnitPriceDisplayEnabled() 
        		&& su.getUnitPriceNumerator() !=0 && su.getUnitPriceDenominator() !=0){
	        unitPriceNumerator = su.getUnitPriceNumerator();
	        unitPriceDenominator = su.getUnitPriceDenominator();
	        unitPriceUOM = su.getUnitPriceUOM();
	        unitPriceDescription = su.getUnitPriceDescription();
        }
        
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

	public int getUnitPriceNumerator() {
		return unitPriceNumerator;
	}

	public void setUnitPriceNumerator(int unitPriceNumerator) {
		this.unitPriceNumerator = unitPriceNumerator;
	}

	public int getUnitPriceDenominator() {
		return unitPriceDenominator;
	}

	public void setUnitPriceDenominator(int unitPriceDenominator) {
		this.unitPriceDenominator = unitPriceDenominator;
	}

	public String getUnitPriceUOM() {
		return unitPriceUOM;
	}

	public void setUnitPriceUOM(String unitPriceUOM) {
		this.unitPriceUOM = unitPriceUOM;
	}

	public String getUnitPriceDescription() {
		return unitPriceDescription;
	}

	public void setUnitPriceDescription(String unitPriceDescription) {
		this.unitPriceDescription = unitPriceDescription;
	}

    
}
