package com.freshdirect.mobileapi.catalog.model;

public class UnitPrice {
	
private int unitPriceNumerator;
	
	private int unitPriceDenominator;
	
	private String unitPriceUOM;
	
	private String unitPriceDescription;

	private String priceText;
	
	/**
	 * Concatenated version of price text and UOM
	 */
	private String unitPriceText;
	
	public String getPriceText() {
		return priceText;
	}

	public void setPriceText(String priceText) {
		this.priceText = priceText;
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
	//Hopefully this will never be used
	public void setUnitPriceText(String unitPriceText){
		this.unitPriceText = unitPriceText;
	}
	
	public String getUnitPriceText(){
		if(unitPriceText == null || unitPriceText.isEmpty()){
			updateUnitPriceText();
		}

		return this.unitPriceText;
	}
	private void updateUnitPriceText() {
		if(this.priceText == null || this.unitPriceUOM == null)
			this.priceText = null;
		else
			this.unitPriceText = priceText + "/" + unitPriceUOM;
	}

}
