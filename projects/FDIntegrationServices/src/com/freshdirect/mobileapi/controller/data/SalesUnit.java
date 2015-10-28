package com.freshdirect.mobileapi.controller.data;


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
	
	private double ratio;
	
    public SalesUnit(String name) {
        this.name = name;
    }

    public SalesUnit() {

    }

    public SalesUnit(com.freshdirect.mobileapi.model.SalesUnit su) {
        name = su.getName();
        description = su.getDescription();
        defaultSalesUnit = su.isDefault();
        ratio = (double)su.getNumerator() / (double)su.getDenominator();
        int lbIndex = description.indexOf("lb");
        if (lbIndex >= 0) {
            shortDescription = description.substring(0, lbIndex).trim();
        } else {
            shortDescription = description;
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

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	

    
}
