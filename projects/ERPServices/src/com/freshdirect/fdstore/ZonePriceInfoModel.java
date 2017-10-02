package com.freshdirect.fdstore;

import java.io.Serializable;

import com.freshdirect.common.pricing.ZoneInfo;

public class ZonePriceInfoModel implements Serializable, Cloneable {
    private static final long serialVersionUID = -652975300786917834L;

    /** Default price in USD */
	private final double sellingPrice;

	private final double promoPrice;

	private final ZoneInfo zoneInfo;

	private final boolean itemOnSale;

	private final int dealPercentage;

	private final int tieredDealPercentage;

	private final int highestDealPercentage;

	private final String displayableDefaultPriceUnit;

	/** Default price unit of measure */
	private final String defaultPriceUnit;

	private final boolean showBurstImage;


	public ZonePriceInfoModel(double sellingPrice, double promoPrice, String defaultPriceUnit,String displayableDefaultPriceUnit,
			boolean itemOnSale, int dealPercentage, int tieredDealPercentage, ZoneInfo zoneInfo) {
		this( sellingPrice, promoPrice, defaultPriceUnit, displayableDefaultPriceUnit, itemOnSale, dealPercentage, tieredDealPercentage, zoneInfo, true );
	}

	public ZonePriceInfoModel(double sellingPrice, double promoPrice, String defaultPriceUnit,String displayableDefaultPriceUnit,
			boolean itemOnSale, int dealPercentage, int tieredDealPercentage, ZoneInfo zoneInfo, boolean showBurstImage) {
		this.defaultPriceUnit = defaultPriceUnit != null ? defaultPriceUnit.intern() : null;
		this.displayableDefaultPriceUnit = displayableDefaultPriceUnit != null ? displayableDefaultPriceUnit.intern() : null;
		this.sellingPrice = sellingPrice;
		this.promoPrice=promoPrice;
        this.itemOnSale=itemOnSale;
        this.dealPercentage=dealPercentage;
        this.tieredDealPercentage=tieredDealPercentage;
        this.highestDealPercentage=Math.max(dealPercentage, tieredDealPercentage);
        this.zoneInfo = zoneInfo;
        this.showBurstImage=showBurstImage;
	}

	/**
	 * Get the "default" price - this is usually the lowest price for the product.
	 *
	 * @return price in USD
	 */
	public double getDefaultPrice() {
		//Making the DeliveryPass Free, as SAP is not receiving the value as 0
		if (FDStoreProperties.getEnableFreeProduct() && this.sellingPrice <= 0.01) {
			return 0;
		}
		else{
			//Check if item on sale then return promo price.
			if(isItemOnSale()){
					return this.promoPrice;
			}
			return this.sellingPrice;
		}
	}

	public double getSellingPrice() {
		//Making the DeliveryPass Free, as SAP is not receiving the value as 0
		if (FDStoreProperties.getEnableFreeProduct() && this.sellingPrice <= 0.01) {
			return 0;
		}
		else{
			return this.sellingPrice;
		}
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

	public ZoneInfo getZoneInfo() {
		return zoneInfo;
	}

	public boolean isShowBurstImage() {
		return showBurstImage;
	}

	 @Override
     public String toString() {
         return "ZonePriceInfoModel[" + zoneInfo + " sellingPrice:" + sellingPrice + " promoPrice:" + promoPrice + " itemOnSale:" + itemOnSale
                 + " dealPercentage:" + dealPercentage + " tieredDealPercentage:" + tieredDealPercentage + " highestDealPercentage:" + highestDealPercentage
                 + " showBurstImage:" + showBurstImage + "]";
     }

    @Override
    public ZonePriceInfoModel clone() {
        try {
            return (ZonePriceInfoModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
