package com.freshdirect.fdstore;

import java.io.Serializable;

public class ZonePriceInfoModel implements Serializable {
	/** Default price in USD */
	private final double sellingPrice;

	private final double promoPrice;
	
	private final String sapZoneId;
	
	private final boolean itemOnSale;
	
	private final int dealPercentage;
	
	private final int tieredDealPercentage;
	
	private final int highestDealPercentage;
	
	private final String displayableDefaultPriceUnit;
	
	/** Default price unit of measure */
	private final String defaultPriceUnit;
	
	private final boolean showBurstImage;
	

	public ZonePriceInfoModel(double sellingPrice, double promoPrice, String defaultPriceUnit,String displayableDefaultPriceUnit, 
			boolean itemOnSale, int dealPercentage, int tieredDealPercentage, String sapZoneId, boolean showBurstImage) {
		this.defaultPriceUnit = defaultPriceUnit != null ? defaultPriceUnit.intern() : null;
		this.displayableDefaultPriceUnit = displayableDefaultPriceUnit != null ? displayableDefaultPriceUnit.intern() : null;
		this.sellingPrice = sellingPrice;
		this.promoPrice=promoPrice;
        this.itemOnSale=itemOnSale;
        this.dealPercentage=dealPercentage;
        this.tieredDealPercentage=tieredDealPercentage;
        this.highestDealPercentage=Math.max(dealPercentage, tieredDealPercentage);
        this.sapZoneId = sapZoneId != null ? sapZoneId.intern() : null;
        this.showBurstImage=showBurstImage;
	}
	
	/**
	 * Get the "default" price - this is usually the lowest price for the product.
	 *
	 * @return price in USD
	 */
	public double getDefaultPrice() {
		//Check if item on sale then return promo price.
		if(isItemOnSale()){
				return this.promoPrice;
		}
		return this.sellingPrice;
	}

	public double getSellingPrice() {

		return this.sellingPrice;
	}
	/*
	public double getPromoPrice() {
		return this.promoPrice;
	}
	*/
	/**
	 * Get the unit of measure for the "default" price.
	 *
	 * @return unit of measure
	 */
	public String getDefaultPriceUnit() {
		return this.defaultPriceUnit;
	}
	
	/**
	 * Get the unit of measure for the "default" price.
	 *
	 * @return pricing unit attribute if one, else return the default Price unit
	 */
	public String getDisplayableDefaultPriceUnit() {
		
		return this.displayableDefaultPriceUnit == null || "".equals(this.displayableDefaultPriceUnit) ? this.defaultPriceUnit : this.displayableDefaultPriceUnit ;
	}


	public int getDealPercentage() {
		return dealPercentage;
	}
	
	public int getTieredDealPercentage() {
		return tieredDealPercentage;
	}

	public int getHighestDealPercentage() {
		return highestDealPercentage;
	}

	public boolean isItemOnSale() {
		return this.itemOnSale;
	}

	public String getSapZoneId() {
		return sapZoneId;
	}

	public boolean isShowBurstImage() {
		return showBurstImage;
	}
}
