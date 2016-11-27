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

/**
 * ErpSalesUnit model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpSalesUnitModel extends ErpModelSupport implements DurableModelI {

	/** Alternative unit of measure */
	private String alternativeUnit;

	/** Base unit of measure */
	private String baseUnit;

	/** Numerator */
	private int numerator;

	/** Denominator */
	private int denominator;

	/** Sales unit description */
	private String description;
	
	/** [APPDEV-209]Display Indicator - Only for display or not.*/
	private boolean displayInd;
	
	/** [APPDEV-3438]-Display Unit Pricing calculation */
	private int unitPriceNumerator;
	
	private int unitPriceDenominator;
	
	private String unitPriceUOM;
	
	private String unitPriceDescription;


	/**
	 * Default constructor.
	 */
	public ErpSalesUnitModel() {
		super();
	}

	/**
	 * Constructor with all properties.
	 *
	 * @param alternativeUnit Alternative unit of measure
	 * @param baseUnit Base unit of measure
	 * @param numerator Numerator
	 * @param denominator Denominator
	 * @param description Sales unit description
	 */
	public ErpSalesUnitModel(String alternativeUnit, String baseUnit, int numerator, int denominator, String description) {
		this.setAlternativeUnit( alternativeUnit );
		this.setBaseUnit( baseUnit );
		this.setNumerator( numerator );
		this.setDenominator( denominator );
		this.setDescription( description );
	}
	public ErpSalesUnitModel(String alternativeUnit, String baseUnit, int numerator, int denominator, String description, boolean displayInd) {
		this.setAlternativeUnit( alternativeUnit );
		this.setBaseUnit( baseUnit );
		this.setNumerator( numerator );
		this.setDenominator( denominator );
		this.setDescription( description );
		this.setDisplayInd(displayInd);
	}

	public ErpSalesUnitModel(String alternativeUnit, String baseUnit, int numerator, int denominator, String description, boolean displayInd,int unitPriceNumerator,
			int unitPriceDenominator,
			String unitPriceUOM, String unitPriceDescription) {
		this.setAlternativeUnit( alternativeUnit );
		this.setBaseUnit( baseUnit );
		this.setNumerator( numerator );
		this.setDenominator( denominator );
		this.setDescription( description );
		this.setDisplayInd(displayInd);
		this.setUnitPriceNumerator(unitPriceNumerator);
		this.setUnitPriceDenominator(unitPriceDenominator);
		this.setUnitPriceUOM(unitPriceUOM);
		this.setUnitPriceDescription(unitPriceDescription);
	}

	/**
	 * Get alternative unit of measure.
	 *
	 * @return unit of measure
	 */
	public String getAlternativeUnit() {
		return this.alternativeUnit;
	}

	/**
	 * Set alternative unit of measure.
	 *
	 * @param alternativeUnit unit of measure
	 */
	public void setAlternativeUnit(String alternativeUnit) {
		this.alternativeUnit = alternativeUnit != null ? alternativeUnit.intern() : null;
	}

	/**
	 * Get base unit of measure.
	 *
	 * @return unit of measure
	 */
	public String getBaseUnit() {
		return this.baseUnit;
	}

	/**
	 * Set base unit of measure.
	 *
	 * @param baseUnit unit of measure
	 */
	public void setBaseUnit(String baseUnit) {
		this.baseUnit = baseUnit != null ? baseUnit.intern() : null;
	}

	/**
	 * Get conversion ratio numerator.
	 *
	 * @return numerator
	 */
	public int getNumerator() {
		return this.numerator;
	}

	/**
	 * Set conversion ratio numerator.
	 *
	 * @param numerator numerator
	 */
	public void setNumerator(int numerator) {
		this.numerator = numerator;
	}

	/**
	 * Get conversion ratio denominator.
	 *
	 * @return denominator
	 */
	public int getDenominator() {
		return this.denominator;
	}

	/**
	 * Set conversion ratio denominator.
	 *
	 * @param denominator denominator
	 */
	public void setDenominator(int denominator) {
		this.denominator = denominator;
	}

	/**
	 * Get sales unit description.
	 *
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Set sales unit description.
	 *
	 * @param description description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the durable (long-lived) ID for the business object.
	 * This is the salesUnit's alternative unit.
	 *
	 * @return durable ID
	 */
	public String getDurableId() {
		return this.getAlternativeUnit();
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
	public boolean isDisplayInd() {
		return displayInd;
	}

	public void setDisplayInd(boolean displayInd) {
		this.displayInd = displayInd;
	}

	public int getUnitPriceNumerator() {
		return unitPriceNumerator;
	}

	public void setUnitPriceNumerator(int unitPriceNumerator) {
		this.unitPriceNumerator = unitPriceNumerator;
	}

	public int getUnitPriceDenominator() {
		return unitPriceDenominator;
	}

	public void setUnitPriceDenominator(int unitPriceDenominator) {
		this.unitPriceDenominator = unitPriceDenominator;
	}

	public String getUnitPriceUOM() {
		return unitPriceUOM;
	}

	public void setUnitPriceUOM(String unitPriceUOM) {
		this.unitPriceUOM = unitPriceUOM;
	}

	public String getUnitPriceDescription() {
		return unitPriceDescription;
	}

	public void setUnitPriceDescription(String unitPriceDescription) {
		this.unitPriceDescription = unitPriceDescription;
	}
}
