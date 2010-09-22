package com.freshdirect.fdstore;

import java.io.Serializable;

public class ZonePriceInfoModel implements Serializable {
	private static final long serialVersionUID = -6275323145928543394L;

	/** Default price in USD */
	private final double sellingPrice;

	private final double promoPrice;

	private final String sapZoneId;

	private final boolean itemOnSale;

	private final int dealPercentage;

	private final int tieredDealPercentage;

	private final int highestDealPercentage;

	private final boolean showBurstImage;

	

	public ZonePriceInfoModel(double sellingPrice, double promoPrice, boolean itemOnSale, int dealPercentage,
			int tieredDealPercentage, String sapZoneId) {
		this(sellingPrice, promoPrice, itemOnSale, dealPercentage, tieredDealPercentage, sapZoneId, true);
	}

	public ZonePriceInfoModel(double sellingPrice, double promoPrice, boolean itemOnSale, int dealPercentage,
			int tieredDealPercentage, String sapZoneId, boolean showBurstImage) {
		this.sellingPrice = sellingPrice;
		this.promoPrice = promoPrice;
		this.itemOnSale = itemOnSale;
		this.dealPercentage = dealPercentage;
		this.tieredDealPercentage = tieredDealPercentage;
		this.highestDealPercentage = Math.max(dealPercentage, tieredDealPercentage);
		this.sapZoneId = sapZoneId != null ? sapZoneId.intern() : null;
		this.showBurstImage = showBurstImage;
	}

	/**
	 * Get the "default" price - this is usually the lowest price for the
	 * product.
	 * 
	 * @return price in USD
	 */
	public double getDefaultPrice() {
		// Check if item on sale then return promo price.
		if (isItemOnSale()) {
			return this.promoPrice;
		}
		return this.sellingPrice;
	}

	public double getSellingPrice() {

		return this.sellingPrice;
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
	
        @Override
        public String toString() {
            return "ZonePriceInfoModel[" + sapZoneId + " sellingPrice:" + sellingPrice + " promoPrice:" + promoPrice + " itemOnSale:" + itemOnSale
                    + " dealPercentage:" + dealPercentage + " tieredDealPercentage:" + tieredDealPercentage + " highestDealPercentage:" + highestDealPercentage
                    + " showBurstImage:" + showBurstImage + "]";
        }
}
