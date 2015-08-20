package com.freshdirect.mobileapi.catalog.model;

public class UnitPrice {
	
private int unitPriceNumerator;
	
	private int unitPriceDenominator;
	
	private String unitPriceUOM;
	
	private String unitPriceDescription;
	
	public String getPriceText() {
		return priceText;
	}

	public void setPriceText(String priceText) {
		this.priceText = priceText;
	}

	private String priceText;

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
