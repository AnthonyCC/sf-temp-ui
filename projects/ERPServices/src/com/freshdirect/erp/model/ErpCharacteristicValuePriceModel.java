/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.erp.model;

import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpCharacteristicValuePrice model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpCharacteristicValuePriceModel extends ModelSupport {

	/** ErpMaterial ID */
	private String materialId;

	/** ErpCharacteristicValue ID */
	private String characteristicValueId;

	/** SAP unique ID */
	private String sapId;

	/** Price in USD */
	private double price;
	
	/** Pricing unit of measure */
	private String pricingUnit;

	/** Condition type */
	private String conditionType;
	
	/** Sales Organisation */
	private String salesOrg;
	
	/** Distribution Channel */
	private String distChannel;
	
	private String characteristicValueName;
	
	private String characteristicName;
	
	private String className;
	

	/**
	 * Default constructor.
	 */
	public ErpCharacteristicValuePriceModel() {
		super();
	}

	/**
	 * Constructor with all properties.
	 *
	 * @param materialId material ID
	 * @param cvId characteristic value ID
	 * @param sapId SAP ID
	 * @param price price in USD
	 * @param conditionType condition type
	 */
	public ErpCharacteristicValuePriceModel(String materialId, String cvId, String sapId, double price, String pricingUnit, String conditionType, String salesOrg, String distChannel,
			String characteristicValueName, String characteristicName, String className) {
		super();
		this.setMaterialId(materialId);
		this.setCharacteristicValueId(cvId);
		this.setSapId(sapId);
		this.setPrice(price);
		this.setPricingUnit(pricingUnit);
		this.setConditionType(conditionType);
		this.setSalesOrg(salesOrg);
		this.setDistChannel(distChannel);
		this.setCharacteristicValueName(characteristicValueName);
		this.setCharacteristicName(characteristicName);
		this.setClassName(className);
	}

	/**
	 * Get the ErpMaterial ID this pricing condition applies to.
	 *
	 * @return material ID
	 */
	public String getMaterialId() {
		return this.materialId;
	}

	/**
	 * Set the ErpMaterial ID this pricing condition applies to.
	 *
	 * @param materialId material ID
	 */
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	/**
	 * Get the ErpCharacteristicValue ID this pricing condition applies to.
	 *
	 * @return characteristic value ID
	 */
	public String getCharacteristicValueId() {
		return this.characteristicValueId;
	}

	/**
	 * Get the ErpCharacteristicValue ID this pricing condition applies to.
	 *
	 * @param cvId characteristic value ID
	 */
	public void setCharacteristicValueId(String cvId) {
		this.characteristicValueId = cvId;
	}

	/**
	 * Get SAP unique characteristic value price ID.
	 *
	 * @return SAP ID
	 */
	public String getSapId() {
		return this.sapId;
	}

	/**
	 * Set SAP unique characteristic value price ID.
	 *
	 * @param sapId SAP ID
	 */
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	/**
	 * Get price.
	 *
	 * @return price in USD
	 */
	public double getPrice() {
		return this.price;
	}

	/**
	 * Set price.
	 *
	 * @param price price in USD
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Get pricing unit of measure.
	 *
	 * @return unit of measure
	 */
	public String getPricingUnit() {
		return this.pricingUnit;
	}

	/**
	 * Set pricing unit of measure.
	 *
	 * @param pricingUnit unit of measure
	 */
	public void setPricingUnit(String pricingUnit) {
		this.pricingUnit = pricingUnit;
	}

	/**
	 * Get condition type (eg. VA00).
	 *
	 * @return condition type.
	 */
	public String getConditionType() {
		return this.conditionType;
	}

	/**
	 * Set condition type (eg. VA00).
	 *
	 * @param conditionType condition type
	 */
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
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

	/**
	 * @return the characteristicValueName
	 */
	public String getCharacteristicValueName() {
		return characteristicValueName;
	}

	/**
	 * @param characteristicValueName the characteristicValueName to set
	 */
	public void setCharacteristicValueName(String characteristicValueName) {
		this.characteristicValueName = characteristicValueName;
	}

	/**
	 * @return the characteristicName
	 */
	public String getCharacteristicName() {
		return characteristicName;
	}

	/**
	 * @param characteristicName the characteristicName to set
	 */
	public void setCharacteristicName(String characteristicName) {
		this.characteristicName = characteristicName;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
}
