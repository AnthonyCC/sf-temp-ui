/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.erp.model;

import com.freshdirect.erp.DurableModelI;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.StringUtil;

/**
 * ErpMaterialPrice model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpMaterialPriceModel extends ErpModelSupport implements DurableModelI {

	/** SAP unique ID */
	private String sapId;

	/** Price in USD per pricing unit */
	private double price;

	/** Pricing unit of measure */
	private String pricingUnit;

	/** Scale quantity */
	private double scaleQuantity;

	/** Scale unit of measure */
	private String scaleUnit;

	private String sapZoneId;
	
	private double promoPrice;
	
	/** Sales Organisation */
	private String salesOrg;
	
	/** Distribution Channel */
	private String distChannel;
	/**
	 * Default constructor.
	 */
	public ErpMaterialPriceModel() {
		super();
	}

	/**
	 * Constructor with all properties.
	 *
	 * @param sapId SAP unique ID
	 * @param price Price in USD per pricing unit
	 * @param pricingUnit Pricing unit of measure
	 * @param scaleQuantity Scale quantity
	 * @param scaleUnit Scale unit of measure
	 */
	public ErpMaterialPriceModel(String sapId, double price, String pricingUnit, double scaleQuantity, String scaleUnit, String sapZoneId, double promoPrice, String salesOrg, String distChannel) {
		this.setSapId(sapId);
		this.setPrice(price);
		this.setPricingUnit(pricingUnit);
		this.setScaleQuantity(scaleQuantity);
		this.setScaleUnit(scaleUnit);		
		this.setSapZoneId(sapZoneId);
		this.setPromoPrice(promoPrice);
		if("1000".equals(salesOrg)) {
			salesOrg=FDStoreProperties.getDefaultFdSalesOrg();
			distChannel=FDStoreProperties.getDefaultFdDistributionChannel();
		}
		this.setSalesOrg(StringUtil.isEmpty(salesOrg)?FDStoreProperties.getDefaultFdSalesOrg():salesOrg);
		this.setDistChannel(StringUtil.isEmpty(distChannel)?FDStoreProperties.getDefaultFdDistributionChannel():distChannel);
		this.setSalesOrg("1000".equals(getSalesOrg())?FDStoreProperties.getDefaultFdSalesOrg():getSalesOrg());
		this.setDistChannel("1000".equals(getDistChannel())?FDStoreProperties.getDefaultFdDistributionChannel():getDistChannel());
	}
	
	/**
	 * Get SAP unique ID.
	 *
	 * @return SAP ID
	 */
	public String getSapId() {
		return this.sapId;
	}

	/**
	 * Set SAP unique ID.
	 *
	 * @param sapId SAP ID to set
	 */
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	/**
	 * Get price per pricing unit for this pricing condition.
	 *
	 * @return price in USD
	 */
	public double getPrice() {
		return this.price;
	}

	/**
	 * Set price per pricing unit for this pricing condition.
	 *
	 * @param price price in USD
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Get pricing unit.
	 *
	 * @return pricing unit of measure
	 */
	public String getPricingUnit() {
		return this.pricingUnit;
	}

	/**
	 * Set pricing unit.
	 *
	 * @param pricingUnit pricing unit of measure
	 */
	public void setPricingUnit(String pricingUnit) {
		this.pricingUnit = pricingUnit;
	}

	/**
	 * Get scale quantity. Optional, defaults to zero.
	 *
	 * @return scale quantity
	 */
	public double getScaleQuantity() {
		return this.scaleQuantity;
	}

	/**
	 * Set scale quantity.
	 *
	 * @return scale quantity
	 */
	public void setScaleQuantity(double scaleQuantity) {
		this.scaleQuantity = scaleQuantity;
	}

	/**
	 * Get the scale unit. Empty string means no scales.
	 *
	 * @return scale unit of measure
	 */
	public String getScaleUnit() {
		return this.scaleUnit;
	}

	/**
	 * Set the scale unit.
	 *
	 * @param scaleUnit scale unit of measure
	 */
	public void setScaleUnit(String scaleUnit) {
		this.scaleUnit = scaleUnit;
	}

	/**
	 * Get the durable (long-lived) ID for the business object.
	 * This is the materialPrice's SAP ID.
	 *
	 * @return durable ID
	 */
	public String getDurableId() {
		return this.getSapId();
	}

	/**
	 * Template method to visit the children of this ErpModel.
	 * It should call accept(visitor) on these (or do nothing).
	 *
	 * @param visitor visitor instance to pass around
	 */
	public void visitChildren(ErpVisitorI visitor) {
		// no children
	}

	public String getSapZoneId() {
		return this.sapZoneId;
	}

	public void setSapZoneId(String sapZoneId) {
		this.sapZoneId = sapZoneId;
	}

	public double getPromoPrice() {
		return promoPrice;
	}

	public void setPromoPrice(double promoPrice) {
		this.promoPrice = promoPrice;
	}

	/**
	 * @return the salesOrg
	 */
	public String getSalesOrg() {
		return salesOrg;
	}

	/**
	 * @param salesOrg the salesOrg to set
	 */
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	/**
	 * @return the distChannel
	 */
	public String getDistChannel() {
		return distChannel;
	}

	/**
	 * @param distChannel the distChannel to set
	 */
	public void setDistChannel(String distChannel) {
		this.distChannel = distChannel;
	}

	
}
