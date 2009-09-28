/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.erp.EnumATPRule;

public interface SapOrderLineI extends Serializable {

	/**
	 * Get the orderline number (6 char, zeropadded, eg 000100).
	 */
	public String getLineNumber();

	/**
	 * Get the SAP material number.
	 */
	public String getMaterialNumber();

	/**
	 * Get the material pricing condition (PR00) to be used.
	 *
	 * @return pricing condition 
	 */
	public MaterialPrice getPricingCondition();
	
	/**
	 * @return true to override PR00 to be zero priced
	 */
	public boolean isZeroBasePrice();

	public boolean isRecipeItem();

	/**
	 * Get the availability check rule.
	 */
	public EnumATPRule getAtpRule();

	/**
	 * Get the name of the characteristic to put the selected sales unit in.
	 *
	 * @return null or empty String if none
	 */
	public String getSalesUnitCharacteristic();
	
	/**
	 * Get the name of the characteristic to put the ordered quantity in.
	 *
	 * @return null or empty String if none
	 */
	public String getQuantityCharacteristic();

	/**
	 * Get the delivery group.
	 */
	public int getDeliveryGroup();

	/**
	 * Get quantity ordered.
	 */
	public double getQuantity();

	/**
	 * Get selected sales unit.
	 */
	public String getSalesUnit();

	/**
	 * Get selected variation-variation option pairs.
	 */
	public Map getOptions();

	/**
	 * Get optional orderline discount.
	 */
	public Discount getDiscount();

	/**
	 * Get tax rate as percentage (0.0825 means 8.25%).
	 */
	public double getTaxRate();

	public double getDepositValue();
	
	public ErpAffiliate getAffiliate();

	/**
	 * Get orderline description.
	 */
	public String getDescription();

	/**
	 * Get orderline configuration description.
	 */
	public String getConfigurationDesc();

	/**
	 * Get orderline department description.
	 */
	public String getDepartmentDesc();

	/**
	 * Get description of ingredients.
	 */
	public String getIngredientsDesc();
	
	/**
	 * Set the inventory list for this material. Called after a ATP check.
	 * 
	 * @param inventories List of ErpInventoryModel 
	 */
	public void setInventories(List inventories);

	/**
	 * Get the inventory list for this material (after an ATP check).
	 *
	 * @return List of ErpInventoryModel (or null)
	 *
	 * @throws IllegalStateException if the ATP was not executed yet
	 */
	public List getInventories();
	
	public double getFixedPrice();

}
