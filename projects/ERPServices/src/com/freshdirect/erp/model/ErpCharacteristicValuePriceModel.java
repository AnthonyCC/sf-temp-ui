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

	/**@link dependency*/
	/*#ErpMaterialModel lnkErpMaterialModel;*/

	/**@link dependency*/
	/*#ErpCharacteristicValueModel lnkErpCharacteristicValueModel;*/


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
	public ErpCharacteristicValuePriceModel(String materialId, String cvId, String sapId, double price, String pricingUnit, String conditionType) {
		super();
		this.setMaterialId(materialId);
		this.setCharacteristicValueId(cvId);
		this.setSapId(sapId);
		this.setPrice(price);
		this.setPricingUnit(pricingUnit);
		this.setConditionType(conditionType);
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
}
