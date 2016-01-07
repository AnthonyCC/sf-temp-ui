/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.erp.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.EnumProductApprovalStatus;
import com.freshdirect.erp.ErpVisitorI;
import com.freshdirect.erp.PricingFactory;
import com.freshdirect.framework.collection.LocalObjectList;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author kkanuganti
 */
public class ErpMaterialModel extends ErpMaterialInfoModel {

	private static final long serialVersionUID = -9146434132902323525L;

	/** Base unit of measure */
	private String baseUnit;

	/** Characteristic name for quantity, null if none */
	private String quantityCharacteristic;

	/** Characteristic name for sales unit, null if none */
	private String salesUnitCharacteristic;
	
	/** the alcoholic content of this material */
	private EnumAlcoholicContent alcoholicContent;

	/** Is Taxable */
	private boolean taxable;

	/** Approval status of the material */
	private EnumProductApprovalStatus approvalStatus;

	/** Type of material */
	private String materialType;
	
	private String taxCode;
	
	private double basePrice;
	
	private String basePricingUnit;
	
	/** SKU code */
	private String skuCode;
	
	/** product total shelf life */
	private String daysFresh;

	/**
	 * Collection of material pricing conditions
	 * @link aggregationByValue
	 * @associates <{ErpMaterialPriceModel}>
	 */
	private LocalObjectList prices = new LocalObjectList();

	/** Collection of sales units
	 * @link aggregationByValue
	 * @associates <{com.freshdirect.erp.model.ErpSalesUnitModel}>
	 */
	private LocalObjectList salesUnits = new LocalObjectList();

	/**
	 * Collection of classes this material belongs to
	 * @link aggregationByValue
	 * @associates <{com.freshdirect.erp.model.ErpClassModel}>
	 */
	private LocalObjectList classes = new LocalObjectList();

	/** UPC code assigned by manufacturer */
	private String UPC;

	/** Collection of sales units for display only.
	 * @link aggregationByValue
	 * @associates <{com.freshdirect.erp.model.ErpSalesUnitModel}>
	 */
	private LocalObjectList displaySalesUnits = new LocalObjectList();
	
	private LocalObjectList materialPlants = new LocalObjectList();
	
	private LocalObjectList materialSalesAreas = new LocalObjectList();

	/**
	 * Default constructor.
	 */
	public ErpMaterialModel() {
		super();
	}

	/**
	 * @param sapId
	 * @param baseUnit
	 * @param description
	 * @param upc
	 * @param quantityCharacteristic
	 * @param salesUnitCharacteristic
	 * @param alcoholicContent
	 * @param taxable
	 * @param approvalStatus
	 * @param materialType
	 * @param skuCode
	 * @param daysFresh
	 * @param prices
	 * @param salesUnits
	 * @param classes
	 * @param displaySalesUnits
	 * @param materialPlants
	 * @param materialSalesAreas
	 */
	public ErpMaterialModel(String sapId, String baseUnit, String description, String upc, String quantityCharacteristic,
			String salesUnitCharacteristic, EnumAlcoholicContent alcoholicContent, boolean taxable, String taxCode, 
			String skuCode, String daysFresh, EnumProductApprovalStatus approvalStatus,
			String materialType, List prices, List salesUnits, List classes, List displaySalesUnits, List materialPlants, List materialSalesAreas)
	{
		super(sapId, description);
		this.setBaseUnit(baseUnit);
		this.setUPC(upc);
		this.setQuantityCharacteristic(quantityCharacteristic);
		this.setSalesUnitCharacteristic(salesUnitCharacteristic);
		this.setPrices(prices);
		this.setSalesUnits(salesUnits);
		this.setClasses(classes);
		this.setAlcoholicContent(alcoholicContent);
		this.setTaxable(taxable);
		this.setTaxCode(taxCode);
		this.setDisplaySalesUnits(displaySalesUnits);
		this.setSkuCode(skuCode);
		this.setDaysFresh(daysFresh);
		this.setMaterialType(materialType);
		this.setApprovalStatus(approvalStatus);
		this.setMaterialPlants(materialPlants);
		this.setMaterialSalesAreas(materialSalesAreas);
	}

	/**
	 * Get base unit of measure.
	 *
	 * @return base unit of measure
	 */
	public String getBaseUnit() {
		return this.baseUnit;
	}

	/**
	 * Set base unit of measure.
	 *
	 * @param baseUnit base unit of measure
	 */
	public void setBaseUnit(String baseUnit) {
		this.baseUnit = baseUnit;
	}

	/**
	 * Get Characteristic name that shall contain the Quantity.
	 * Optional, can be null.
	 *
	 * @return quantity characteristic name
	 */
	public String getQuantityCharacteristic() {
		return this.quantityCharacteristic;
	}

	/**
	 * Set Characteristic name that shall contain the Quantity.
	 *
	 * quantity characteristic name (can be null)
	 */
	public void setQuantityCharacteristic(String quantityCharacteristic) {
		this.quantityCharacteristic = quantityCharacteristic;
	}

	/**
	 * Get Characteristic name that shall contain the selected sales-unit.
	 * Optional, can be null.
	 *
	 * @return sales unit characteristic name
	 */
	public String getSalesUnitCharacteristic() {
		return this.salesUnitCharacteristic;
	}

	/**
	 * Set Characteristic name that shall contain the selected sales-unit.
	 *
	 * @param salesUnitCharacteristic sales unit characteristic name (can be null)
	 */
	public void setSalesUnitCharacteristic(String salesUnitCharacteristic) {
		this.salesUnitCharacteristic = salesUnitCharacteristic;
	}

	/**
	 * Get pricing conditions.
	 *
	 * @return collection of ErpMaterialPriceModel objects
	 */
	public List getPrices() {
		return Collections.unmodifiableList(this.prices);
	}

	/**
	 * Set pricing conditions.
	 *
	 * @param prices collection of ErpMaterialPriceModel objects
	 */
	public void setPrices(List prices) {
		//Sort by sap zone id before setting
		Collections.sort(prices, PricingFactory.ERP_MAT_PRICE_MODEL_COMPARATOR);
		this.prices.set(prices);
	}

	/**
	 * Get number of pricing conditions for the material.
	 *
	 * @return number of pricing conditions
	 */
	public int numberOfPrices() {
		return this.prices.size();
	}

	
	
	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	/**
	 * Get sales units.
	 *
	 * @return collection of ErpSalesUnitModel objects
	 */
	public List getSalesUnits() {
		return Collections.unmodifiableList(this.salesUnits);
	}

	/**
	 * Get sales units with specific PKs removed.
	 *
	 * @return collection of ErpSalesUnitModel objects
	 */
	LocalObjectList getFilteredSalesUnits(PrimaryKey[] pks) {
		LocalObjectList filteredList = (LocalObjectList) this.salesUnits.clone();
		for (int i = 0; i < pks.length; i++) {
			filteredList.removeByPK(pks[i]);
		}
		return filteredList;
	}

	/**
	 * Set sales units.
	 *
	 * @param salesUnits collection of ErpSalesUnitModel objects
	 */
	public void setSalesUnits(List salesUnits) {
		this.salesUnits.set(salesUnits);
	}

	/**
	 * Get number of sales units for the material.
	 *
	 * @return number of sales units
	 */
	public int numberOfSalesUnits() {
		return this.salesUnits.size();
	}

	/**
	 * Get Classes.
	 *
	 * @return collection of ErpClassModel objects
	 */
	public List getClasses() {
		return Collections.unmodifiableList(this.classes);
	}

	/**
	 * Get Classes.
	 *
	 * @return collection of ErpClassModel objects
	 */
	public void setClasses(List classes) {
		this.classes.set(classes);
	}

	/**
	 * Get number of classes for the material.
	 *
	 * @return number of classes
	 */
	public int numberOfClasses() {
		return this.classes.size();
	}

	/**
	 * Convenience method to get all characteristics, from all the classes.
	 *
	 * @return collection of ErpCharacteristicModel objects
	 */
	public List getCharacteristics() {
		LocalObjectList list = new LocalObjectList();
		for (Iterator i = this.classes.iterator(); i.hasNext();) {
			list.addAll(((ErpClassModel) i.next()).getCharacteristics());
		}
		return list;
	}

	/**
	 * Get classes, with certain characteristic values hidden.
	 *
	 * @param hiddenCharValuePKs array of characteristic value PKs to hide
	 *
	 * @return filtered collection of ErpClassModel objects
	 */
	public List getFilteredClasses(PrimaryKey[] hiddenCharValuePKs) {
		LocalObjectList list = new LocalObjectList();
		for (Iterator i = this.classes.iterator(); i.hasNext();) {
			list.add(((ErpClassModel) i.next()).getFilteredClone(hiddenCharValuePKs));
		}
		return list;
	}

	/**
	 * Template method to visit the children of this ErpModel.
	 * It should call accept(visitor) on these (or do nothing).
	 *
	 * @param visitor visitor instance to pass around
	 */
	public void visitChildren(ErpVisitorI visitor) {
		for (Iterator i = this.salesUnits.iterator(); i.hasNext();) {
			((ErpSalesUnitModel) i.next()).accept(visitor);
		}
		for (Iterator i = this.classes.iterator(); i.hasNext();) {
			((ErpClassModel) i.next()).accept(visitor);
		}
		for (Iterator i = this.prices.iterator(); i.hasNext();) {
			((ErpMaterialPriceModel) i.next()).accept(visitor);
		}
		for (Iterator i = this.displaySalesUnits.iterator(); i.hasNext();) {
			((ErpSalesUnitModel) i.next()).accept(visitor);
		}
	}

	/** Getter for property UPC.
	 * @return Value of property UPC.
	 */
	public String getUPC() {
		return UPC;
	}

	/** Setter for property UPC.
	 * @param UPC New value of property UPC.
	 */
	public void setUPC(String UPC) {
		this.UPC = UPC;
	}

	/** Getter for property alcoholicContent.
	 * @return Value of property alcoholicContent.
	 */
	public EnumAlcoholicContent getAlcoholicContent() {
		return alcoholicContent;
	}

	/** Setter for property alcoholicContent.
	 * @param alcoholicContent New value of property alcoholicContent.
	 */
	public void setAlcoholicContent(EnumAlcoholicContent alcoholicContent) {
		this.alcoholicContent = alcoholicContent;
	}

	public boolean isTaxable() {
		return taxable;
	}

	public void setTaxable(boolean b) {
		taxable = b;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public String getBasePricingUnit() {
		return basePricingUnit;
	}

	public void setBasePricingUnit(String basePricingUnit) {
		this.basePricingUnit = basePricingUnit;
	}

	/**
	 * Set sales units.
	 *
	 * @param displaySalesUnits collection of ErpSalesUnitModel objects
	 */
	public void setDisplaySalesUnits(List displaySalesUnits) {
		this.displaySalesUnits.set(displaySalesUnits);
	}
	
	/**
	 * Get display sales units .
	 *
	 * @return collection of ErpSalesUnitModel objects
	 */
	public List getDisplaySalesUnits() {
		return Collections.unmodifiableList(this.displaySalesUnits);
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	/**
	 * @return the daysFresh
	 */
	public String getDaysFresh()
	{
		return daysFresh;
	}

	/**
	 * @param daysFresh the daysFresh to set
	 */
	public void setDaysFresh(String daysFresh)
	{
		this.daysFresh = daysFresh;
	}

	/**
	 * @return the approvalStatus
	 */
	public EnumProductApprovalStatus getApprovalStatus()
	{
		return approvalStatus;
	}

	/**
	 * @param approvalStatus the approvalStatus to set
	 */
	public void setApprovalStatus(EnumProductApprovalStatus approvalStatus)
	{
		this.approvalStatus = approvalStatus;
	}

	/**
	 * @return the materialType
	 */
	public String getMaterialType()
	{
		return materialType;
	}

	/**
	 * @param materialType the materialType to set
	 */
	public void setMaterialType(String materialType)
	{
		this.materialType = materialType;
	}

	/**
	 * @return the materialPlants
	 */
	public List getMaterialPlants() {
		return Collections.unmodifiableList(this.materialPlants);
	}

	/**
	 * @param materialPlants the materialPlants to set
	 */
	public void setMaterialPlants(List materialPlants) {
		this.materialPlants.set(materialPlants);
	}

	/**
	 * @return the materialSalesAreas
	 */
	public List getMaterialSalesAreas() {
		return Collections.unmodifiableList(this.materialSalesAreas);
	}

	/**
	 * @param materialSalesAreas the materialSalesAreas to set
	 */
	public void setMaterialSalesAreas(List materialSalesAreas) {
		this.materialSalesAreas.set(materialSalesAreas);
	}
	
	
	
}
