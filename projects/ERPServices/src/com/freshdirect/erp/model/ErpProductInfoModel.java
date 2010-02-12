/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.erp.model;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpProductInfo model class.
 *
 * @version	$Revision$
 * @author	 $Author$
 * @stereotype fd-model
 */
public class ErpProductInfoModel extends ModelSupport {
	
	public static class ErpMaterialPrice implements Serializable{
		private final double price;
		
		private final String unit;
		
		private final double promoPrice;
		
		private final String scaleUnit;
		
		private final double scaleQuantity;
		
		private final String sapZoneId;
		
		public ErpMaterialPrice(double price, String unit, double promoPrice, String scaleUnit, double scaleQuantity, String sapZoneId) {
			this.price = price;
			this.unit = unit;
			this.promoPrice = promoPrice;
			this.scaleUnit = scaleUnit;
			this.scaleQuantity = scaleQuantity;
			this.sapZoneId = sapZoneId;
		}
		
		public double getPrice() {
			return price;
		}
		
		public String getUnit() {
			return unit;
		}
		
		public double getPromoPrice() {
			return promoPrice;
		}
		
		public String getScaleUnit() {
			return scaleUnit;
		}
		
		public double getScaleQuantity() {
			return scaleQuantity;
		}
		
		public String getSapZoneId() {
			return sapZoneId;
		}
	}
	
	/** version number */
	private final int version;

	/** SKU code */
	private final String skuCode;


	/** SapIds for materials in this product */
	private final String[] materialNumbers;
	
	/** unit prices of materials for this product */
	private final ErpMaterialPrice[] materialPrices;
	
	/** Availability checking rule */
	private final EnumATPRule atpRule;

	/** unavailability status code */
	private final String unavailabilityStatus;

	/** unavailability date */
	private final Date unavailabilityDate;

	/** unavailability reason */
	private final String unavailabilityReason;

	/** sap product description */
	private final String description;
	
	/** sap product rating */
	private final String rating;
	
	/** sap product shelf life **/
	private final String freshness;
	

	/**
	 * Constructor with all properties.
	 *
	 * @param skuCode SKU code
	 * @param defaultPrice default price
	 * @param defaultPriceUnit pricing unit for default price
	 * @param materialNumbers
	 * @param unavailabilityStatus
	 * @param unavailabilityDate
	 * @param unavailabilityReason
	 * @param description
	 * @param rating
	 * @param freshness 
	 */
	public ErpProductInfoModel(
		String skuCode,
		int version,
		String[] materialNumbers,
		ErpMaterialPrice[] materialPrices,
		EnumATPRule atpRule,
		String unavailabilityStatus,
		Date unavailabilityDate,
		String unavailabilityReason,
		String description,
		String rating,
		String freshness) {

		super();
		this.skuCode = skuCode;
		this.version = version;
		this.materialNumbers = materialNumbers;
		this.materialPrices = materialPrices;
		this.atpRule = atpRule;
		this.unavailabilityStatus = unavailabilityStatus;
		this.unavailabilityDate = unavailabilityDate;
		this.unavailabilityReason = unavailabilityReason;
		this.description = description;
		this.rating=rating;
		this.freshness=freshness;
	}

	/**
	 * Get version number
	 *
	 * @return version number
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Get Product SKU code.
	 *
	 * @return SKU code
	 */
	public String getSkuCode() {
		return this.skuCode;
	}


	/**
	 * Get Material Numbers.
	 *
	 * @return material numbers (SapIds) for this product's materials
	 */
	public String[] getMaterialSapIds() {
		return this.materialNumbers;
	}
	
	/**
	 * Get Material Prices.
	 * 
	 * @return material prices
	 */
	public ErpMaterialPrice[] getMaterialPrices() {
		return materialPrices;
	}
	
	public EnumATPRule getATPRule() {
		return this.atpRule;
	}

	/** Getter for property unavailabilityDate.
	 * @return Value of property unavailabilityDate.
	 */
	public Date getUnavailabilityDate() {
		return unavailabilityDate;
	}

	/** Getter for property unavailabilityReason.
	 * @return Value of property unavailabilityReason.
	 */
	public String getUnavailabilityReason() {
		return unavailabilityReason;
	}

	/** Getter for property unavailabilityStatus.
	 * @return Value of property unavailabilityStatus.
	 */
	public String getUnavailabilityStatus() {
		return unavailabilityStatus;
	}

	/** Getter for property description.
	 * @return Value of property description.
	 */
	public String getDescription() {
		return description;
	}

	/** Getter for property rating.
	 * @return Value of property rating.
	 */
	public String getRating() {
		return rating;
	}

	/** Getter for property freshness.
	 * @return Value for property freshness.
	 */
	public String getFreshness() {
		if(FDStoreProperties.IsFreshnessGuaranteedEnabled()){
			return freshness;
		}
		return null;
	}

}
