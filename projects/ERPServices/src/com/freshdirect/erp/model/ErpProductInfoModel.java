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
		
		public ErpMaterialPrice(double price, String unit) {
			this.price = price;
			this.unit = unit;
		}
		
		public double getPrice() {
			return price;
		}
		
		public String getUnit() {
			return unit;
		}
	}
	
	/** version number */
	private final int version;

	/** SKU code */
	private final String skuCode;

	/** default price */
	private final double defaultPrice;

	/** pricing unit for default price */
	private final String defaultPriceUnit;

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
	
	private double basePrice;
	
	private String basePriceUnit;	
	

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
		double defaultPrice,
		String defaultPriceUnit,
		String[] materialNumbers,
		ErpMaterialPrice[] materialPrices,
		EnumATPRule atpRule,
		String unavailabilityStatus,
		Date unavailabilityDate,
		String unavailabilityReason,
		String description,
		String rating,
		String freshness,
		double basePrice,
		String basePriceUnit) {
		super();
		this.skuCode = skuCode;
		this.version = version;
		this.defaultPrice = defaultPrice;
		this.defaultPriceUnit = defaultPriceUnit;
		this.materialNumbers = materialNumbers;
		this.materialPrices = materialPrices;
		this.atpRule = atpRule;
		this.unavailabilityStatus = unavailabilityStatus;
		this.unavailabilityDate = unavailabilityDate;
		this.unavailabilityReason = unavailabilityReason;
		this.description = description;
		this.rating=rating;
		this.freshness=freshness;
		this.basePrice=basePrice;
		this.basePriceUnit=basePriceUnit;
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
	 * Get default price.
	 *
	 * @return default price
	 */
	public double getDefaultPrice() {
		return this.defaultPrice;
	}

	/**
	 * Get pricing unit for default price.
	 *
	 * @return pricing unit for default price
	 */
	public String getDefaultPriceUnit() {
		return this.defaultPriceUnit;
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
		return freshness;
	}
	
	public double getBasePrice() {
		return basePrice;
	}

	public String getBasePriceUnit() {
		return basePriceUnit;
	}

}