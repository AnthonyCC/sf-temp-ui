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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.erp.DurableModelI;
import com.freshdirect.erp.EntityModelI;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;
import com.freshdirect.framework.collection.LocalObjectList;
import com.freshdirect.framework.core.VersionedPrimaryKey;


/**
 * ErpProduct model class.
 *
 * @version	$Revision$
 * @author	 $Author$
 * @stereotype fd-model
 */ 
public class ErpProductModel extends ErpModelSupport implements DurableModelI, EntityModelI {

	/** SKU code */
	private String skuCode;
	
	/** default price */
	private double defaultPrice;
	
	/** pricing unit for default price */
	private String defaultPriceUnit;
    
    /** unavailability status code */
    private String unavailabilityStatus;
    
    /** unavailability date */
    private Date unavailabilityDate;
    
    /** unavailability reason */
    private String unavailabilityReason;

	/** effective pricing date (read-only) */
	private Date pricingDate;

	/** The proxied material */ 
	private ErpMaterialModel material;

	/** Array of hidden sales unit PKs */
	private VersionedPrimaryKey[] hiddenSalesUnitPKs = new VersionedPrimaryKey[0];
 
	/** Array of hidden characteristic value PKs */
	private VersionedPrimaryKey[] hiddenCharValuePKs = new VersionedPrimaryKey[0];
	
	/** product rating */
	private String rating;
	
	private double basePrice;
	
	private String basePriceUnit;

	/**
	 * Default constructor.
	 */
	public ErpProductModel() {
		super();
	}

	/**
	 * Constructor with all properties.
	 *
	 * @param skuCode SKU code
	 * @param defaultPrice default price
	 * @param defaultPriceUnit pricing unit for default price
	 * @param materialProxies collection of material proxy model objects
	 */
	public ErpProductModel(String skuCode, double defaultPrice, String defaultPriceUnit, String unavailabilityStatus, Date unavailabilityDate, String unavailabilityReason, Date pricingDate, ErpMaterialModel material, VersionedPrimaryKey[] suPKs, VersionedPrimaryKey[] cvPKs, String _rating,double basePrice, String basePriceUnit ) {
		super();
		if (skuCode==null) {
			throw new IllegalArgumentException("SKU code cannot be null");
		}
		this.setSkuCode(skuCode);
		this.setDefaultPrice(defaultPrice);
		this.setDefaultPriceUnit(defaultPriceUnit);
	    this.setUnavailabilityStatus(unavailabilityStatus);
        this.setUnavailabilityDate(unavailabilityDate);
        this.setUnavailabilityReason(unavailabilityReason);
        this.pricingDate = pricingDate;

		this.setProxiedMaterial(material);
		this.setHiddenSalesUnitPKs( suPKs );
		this.setHiddenCharacteristicValuePKs( cvPKs );
		this.setRating(_rating);
		this.setBasePrice(basePrice);
		this.setBasePriceUnit(basePriceUnit);
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
	 * Set Product SKU code.
	 *
	 * @param skuCode SKU code
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	

	/**
	 * Get effective pricing date.
	 */
	public Date getPricingDate() {
		return this.pricingDate;
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
	 * Set default price.
	 *
	 * @param default price
	 */
	public void setDefaultPrice(double defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
	
	/**
	 * Get pricing unit for default price.
	 *
	 * @return pricing unit for default price
	 */
	public String getDefaultPriceUnit() {
		return this.defaultPriceUnit;
	}
	/** Setter for property rating.
    * @param  _rating
     */
    public void setRating(String _rating) {
         rating=_rating;
    }
	/** Getter for property rating.
     * @return Value of property rating.
     */
    public String getRating() {
        return rating;
    }	
	/**
	 * Set pricing unit for default price
	 *
	 * @param defaultPriceUnit pricing unit for default price
	 */
	public void setDefaultPriceUnit(String defaultPriceUnit) {
		this.defaultPriceUnit = defaultPriceUnit;
	}

    /** Getter for property unavailabilityDate.
     * @return Value of property unavailabilityDate.
     */
    public Date getUnavailabilityDate() {
        return unavailabilityDate;
    }    
    
    /** Setter for property unavailabilityDate.
     * @param unavailabilityDate New value of property unavailabilityDate.
     */
    public void setUnavailabilityDate(Date unavailabilityDate) {
        this.unavailabilityDate = unavailabilityDate;
    }    

    /** Getter for property unavailabilityReason.
     * @return Value of property unavailabilityReason.
     */
    public String getUnavailabilityReason() {
        return unavailabilityReason;
    }
    
    /** Setter for property unavailabilityReason.
     * @param unavailabilityReason New value of property unavailabilityReason.
     */
    public void setUnavailabilityReason(String unavailabilityReason) {
        this.unavailabilityReason = unavailabilityReason;
    }
    
    /** Getter for property unavailabilityStatus.
     * @return Value of property unavailabilityStatus.
     */
    public String getUnavailabilityStatus() {
        return unavailabilityStatus;
    }
    
    /** Setter for property unavailabilityStatus.
     * @param unavailabilityStatus New value of property unavailabilityStatus.
     */
    public void setUnavailabilityStatus(String unavailabilityStatus) {
        this.unavailabilityStatus = unavailabilityStatus;
    }

	/**
	 * Get the durable (long-lived) ID for the business object.
	 * This is the product's SKU code.
	 *
	 * @return durable ID
	 */
	public String getDurableId() {
		return this.getSkuCode();
	}

	/**
	 * Get the proxied material.
	 *
	 * @return ErpMaterialModel object
	 */
	public ErpMaterialModel getProxiedMaterial() {
		return this.material;
	}

	/**
	 * Set the proxied material.
	 *
	 * @param material ErpMaterialModel object
	 */
	public void setProxiedMaterial(ErpMaterialModel material) {
		this.material = material;
	}

	/**
	 * Get the PKs for hidden sales units.
	 * 
	 * @return array of VersionedPrimaryKey objects
	 */
	public VersionedPrimaryKey[] getHiddenSalesUnitPKs() {
		// shallow copy
		synchronized (this.hiddenSalesUnitPKs) {
			VersionedPrimaryKey[] pks=new VersionedPrimaryKey[ this.hiddenSalesUnitPKs.length ];
			System.arraycopy(this.hiddenSalesUnitPKs, 0, pks, 0, pks.length);
			return pks;
		}
	}

	/**
	 * Set the PKs for hidden sales units.
	 * 
	 * @param suPKs array of VersionedPrimaryKey objects
	 */
	public void setHiddenSalesUnitPKs(VersionedPrimaryKey[] suPKs) {
		// shallow copy
		synchronized (this.hiddenSalesUnitPKs) {
			VersionedPrimaryKey[] pks=new VersionedPrimaryKey[ suPKs.length ];
			System.arraycopy(suPKs, 0, pks, 0, pks.length);
			this.hiddenSalesUnitPKs = pks;
		}
	}

	/**
	 * Get the PKs for hidden characteristic values.
	 * 
	 * @return array of VersionedPrimaryKey objects
	 */
	public VersionedPrimaryKey[] getHiddenCharacteristicValuePKs() {
		// shallow copy
		synchronized (this.hiddenCharValuePKs) {
			VersionedPrimaryKey[] pks=new VersionedPrimaryKey[ this.hiddenCharValuePKs.length ];
			System.arraycopy(this.hiddenCharValuePKs, 0, pks, 0, pks.length);
			return pks;
		}
	}

	/**
	 * Set the PKs for hidden characteristic values.
	 * 
	 * @param cvPKs array of VersionedPrimaryKey objects
	 */
	public void setHiddenCharacteristicValuePKs(VersionedPrimaryKey[] cvPKs) {
		// shallow copy
		synchronized (this.hiddenCharValuePKs) {
			VersionedPrimaryKey[] pks=new VersionedPrimaryKey[ cvPKs.length ];
			System.arraycopy(cvPKs, 0, pks, 0, pks.length);
			this.hiddenCharValuePKs = pks;
		}
	}

	
	/**
	 * Template method to visit the children of this ErpModel.
	 * It should call accept(visitor) on these (or do nothing).
	 *
	 * @param visitor visitor instance to pass around
	 */
	public void visitChildren(ErpVisitorI visitor) {
		this.material.accept(visitor);
	}
    
	/**
	 * Get sales units, with hiding.
	 *
	 * @return collection of ErpSalesUnitModel objects
	 */
	public List getSalesUnits() {
		return Collections.unmodifiableList( this.material.getFilteredSalesUnits( this.hiddenSalesUnitPKs ) );
	}

	/**
	 * Get classes, with hiding.
	 *
	 * @return collection of ErpClassEB remote interfaces or ErpClassModel objects
	 */
	public List getClasses() {
		return this.material.getFilteredClasses(this.hiddenCharValuePKs);
	}

	/**
	 * Convenience method to get all characteristic, with hiding applied.
	 *
	 * @return filtered collection of ErpCharacteristicModel objects
	 */
	public List getCharacteristics() {
		List classes = this.getClasses();
		LocalObjectList list = new LocalObjectList();
		for (Iterator i=classes.iterator(); i.hasNext(); ) {
			list.addAll( ((ErpClassModel)i.next()).getCharacteristics() );
		}
		return list;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public String getBasePriceUnit() {
		return basePriceUnit;
	}

	public void setBasePriceUnit(String basePriceUnit) {
		this.basePriceUnit = basePriceUnit;
	}

}
