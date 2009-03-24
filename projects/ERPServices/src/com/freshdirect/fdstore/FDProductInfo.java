/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.util.Date;
import java.util.List;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryModel;

/**
 * Lightweight information about a product, that is necessary for display on a category page.
 *
 * @version $Revision$
 * @author $Author$
 */

public class FDProductInfo extends FDSku  {
	
	/** Default price in USD */
	private final double defaultPrice;
	
	private final String displayableDefaultPriceUnit;
	
	/** Default price unit of measure */
	private final String defaultPriceUnit;
	
	private final String[] materialNumbers;
    
	/** Availability checking rule */
    private final EnumATPRule atpRule;

    /** SAP availability status */
    private final EnumAvailabilityStatus availStatus;
    
    /** SAP availability date */
    private final Date availDate;
    
    /** inventory info for the product but this should only be set in a TEST CASE */
    private final FDInventoryCacheI inventory;
	
    private final String rating;
	/** Default price in USD */
	private final double basePrice;
	
	private final String basePriceUnit;
	
	private final boolean isDeal;
	
	private final int dealPercentage;
	
	
    
    public FDProductInfo(String skuCode, int version, double defaultPrice, String defaultPriceUnit, String[] materialNumbers, EnumATPRule atpRule, EnumAvailabilityStatus availStatus, Date availDate, String displayableDefaultPriceUnit, FDInventoryCacheI inventory, String rating,double basePrice,String basePriceUnit,boolean isDeal,int dealPercentage) {
		super(skuCode, version);
		this.defaultPrice = defaultPrice;
		this.defaultPriceUnit = defaultPriceUnit;
		this.materialNumbers = materialNumbers;
		this.atpRule = atpRule;
        this.availStatus = availStatus;
        this.availDate = availDate;
        this.displayableDefaultPriceUnit = displayableDefaultPriceUnit;
        this.inventory = inventory;
        this.rating=rating;
        this.basePrice=basePrice;
        this.basePriceUnit=basePriceUnit;
        this.isDeal=isDeal;
        this.dealPercentage=dealPercentage;
       
	}

	/**
	 * Get inventory (short term availability) information.
	 */
	public ErpInventoryModel getInventory() {
		if(this.inventory != null){
			return this.inventory.getInventory(materialNumbers[0]);
		}
		return FDInventoryCache.getInstance().getInventory(materialNumbers[0]);
	}

	/**
	 * Get the "default" price - this is usually the lowest price for the product.
	 *
	 * @return price in USD
	 */
	public double getDefaultPrice() {
		return this.defaultPrice;
	}

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

	public EnumATPRule getATPRule() {
		return this.atpRule;
	}
	
    /**
	 * Get the SAP availability status of this product.
	 *
	 * @return true if product is discontinued
	 */
	public EnumAvailabilityStatus getAvailabilityStatus() {
		return this.availStatus;
	}
    
    /**
	 * Get the SAP availability date of this product.
	 * This attribute is not used.
	 *
	 * @return the availability date, as returned by SAP.
	 */
	public Date getAvailabilityDate() {
		return this.availDate;
	}
    
    public boolean isAvailable() {
        return EnumAvailabilityStatus.AVAILABLE.equals(this.availStatus);
    }
    
    public boolean isDiscontinued() {
        return EnumAvailabilityStatus.DISCONTINUED.equals(this.availStatus);
    }
    
    public boolean isOutOfSeason() {
        return EnumAvailabilityStatus.OUT_OF_SEASON.equals(this.availStatus);
    }
    
    public boolean isTempUnavailable() {
        return EnumAvailabilityStatus.TEMP_UNAV.equals(this.availStatus);
    }

    public String getRating() {
    	return this.rating;
    }
    
	public String toString() {
		StringBuffer buf=new StringBuffer("FDProductInfo[");
		buf.append(this.getSkuCode()).append(" v").append(this.getVersion());
		buf.append("\n\t").append(this.materialNumbers);
        buf.append("\n\t").append(this.availStatus.getShortDescription());
        buf.append("\n\t").append(this.availDate);
        buf.append("\n\t").append(this.rating);
        buf.append("\n]");
		return buf.toString();
	}

	public double getBasePrice() {
		return basePrice;
	}

	public String getBasePriceUnit() {
		return basePriceUnit;
	}

	public int getDealPercentage() {
		return dealPercentage;
	}

	public boolean isDeal() {
		return isDeal;
	}
	public List getCountryOfOrigin() {
		return FDCOOLInfoCache.getInstance().getCOOLInfo(materialNumbers[0]);
	}
}
