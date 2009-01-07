/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.pricing;

import java.io.Serializable;

/**
 * Aggregates all relevant information for performing pricing.
 *
 * @version $Revision$
 * @author $Author$
 */
public class Pricing implements Serializable {

	private MaterialPrice[] materialPrices;
	private CharacteristicValuePrice[] cvPrices;
	private SalesUnitRatio[] salesUnits;

	/**
	 * @param materialPrices array of material pricing conditions
	 * @param cvPrices array of characteristic value pricing conditions
	 * @param salesUnits array of sales unit ratios
	 */
	public Pricing(MaterialPrice[] materialPrices, CharacteristicValuePrice[] cvPrices, SalesUnitRatio[] salesUnits) {
		this.materialPrices=materialPrices;
		this.cvPrices=cvPrices;
		this.salesUnits=salesUnits;
	}

	/**
	 * Get all characteristic value pricing conditions.
	 *
	 * @return array of CharacteristicValuePrice objects
	 */
	public CharacteristicValuePrice[] getCharacteristicValuePrices() {
		return this.cvPrices;
	}

	/**
	 * Get all sales unit ratios.
	 *
	 * @return array of SalesUnitRatio objects
	 */
	public SalesUnitRatio[] getSalesUnitRatios() {
		return this.salesUnits;
	}
		
	
	/**
	 * Get all material pricing conditions.
	 *
	 * @return array of MaterialPrice objects
	 */
	public MaterialPrice[] getMaterialPrices() {
		return this.materialPrices;
	}

	/**
	 * Get matching pricing condition for pricing unit.
	 *
	 * @return null if not found
	 */
	public MaterialPrice findMaterialPrice(String pricingUnit) {
		for (int i=0; i<this.materialPrices.length; i++) {
			if (pricingUnit.equals( this.materialPrices[i].getPricingUnit() )) {
				return this.materialPrices[i];
			}
		}
		return null;
	}
	
	/**
	 * Get matching pricing condition for scaled quantity.
	 *
	 * @param scaleQuantity quantity in scale unit of measure
	 *
	 * @return matching condition, or the lowest if no match  
	 */
	public MaterialPrice findMaterialPrice(double scaleQuantity) {
		MaterialPrice lowest = null;
		double lowestBound = Double.POSITIVE_INFINITY;
		for (int i = 0; i < this.materialPrices.length; i++) {
			MaterialPrice mp = this.materialPrices[i];
			if (mp.isWithinBounds(scaleQuantity)) {
				return this.materialPrices[i];
			}
			if (mp.getScaleLowerBound() < lowestBound) {
				lowestBound = mp.getScaleLowerBound();
				lowest = mp;
			}
		}
		return lowest;
	}

	/**
	 * Get matching sales unit ratio.
	 *
	 * @return null if not found
	 */
	public SalesUnitRatio findSalesUnitRatio(String salesUnit) {
		for (int i=0; i<this.salesUnits.length; i++) {
			if (salesUnit.equals( this.salesUnits[i].getAlternateUnit() )) {
				return this.salesUnits[i];
			}
		}
		return null;
	}

	/**
	 * Get matching char.value pricing condition.
	 * 
	 * @return null if not found
	 */
	public CharacteristicValuePrice findCharacteristicValuePrice(String characteristic, String charValue) {
		if (characteristic==null || charValue==null) {
			throw new IllegalArgumentException("Null characteristic or charValue: "+characteristic+"/"+charValue);
		}
		CharacteristicValuePrice cvp;
		for (int i=0; i<this.cvPrices.length; i++) {
			cvp=this.cvPrices[i];
			if ( characteristic.equals(cvp.getCharacteristicName()) && charValue.equals(cvp.getCharValueName()) ) {
				return cvp;
			}
		}
		return null;
	}
	
	
	/**
	 * Determine if scales apply.
	 * It checks the scaleUnit on first element in the MaterialPrice array.
	 *
	 * @return true if scales apply, false if no scales (or there are no mat. pricing conditions)
	 */
	public boolean hasScales() {
		if ( this.materialPrices.length==0 ) {
			return false;
		}
		String scaleUnit = this.materialPrices[0].getScaleUnit();
		return ( "".equals(scaleUnit) ? false : true );
	}
	
	/**
	 * Determine scale unit of measure (applicable only if hasScales()==true).
	 *
	 * @return scale unit of measure
	 */
	public String getScaleUnit() {
		return this.materialPrices[0].getScaleUnit();
	}

	public String[] getScaleDisplay() {
		String[] scales = new String[ this.materialPrices.length-1 ];
		for (int i=0; i<scales.length; i++) {
			scales[i] = this.materialPrices[i+1].getScaleDisplay();
		}
		return scales;
	}
	
	
	public String[] getWineScaleDisplay(boolean isBreakRequired) {
		String[] scales = new String[ this.materialPrices.length-1 ];
		for (int i=0; i<scales.length; i++) {
			scales[i] = this.materialPrices[i+1].getWineScaleDisplay(isBreakRequired);
		}
		return scales;
	}
	
	
	public String toString() {
		StringBuffer buf=new StringBuffer("Pricing[");
		for (int i=0; i<salesUnits.length; i++) {
			buf.append("\n\t").append(salesUnits[i].toString());
		}
		for (int i=0; i<materialPrices.length; i++) {
			buf.append("\n\t").append(materialPrices[i].toString());
		}
		for (int i=0; i<cvPrices.length; i++) {
			buf.append("\n\t").append(cvPrices[i].toString());
		}
		buf.append("\n]");
		return buf.toString();
	}

	public double getMinPrice() {
		if (materialPrices.length == 0)
			return Double.NaN;

		double p = materialPrices[0].getPrice();
		for (int i=1; i<materialPrices.length; ++i) {
			p = Math.min(p, materialPrices[i].getPrice());
		}

		return p;
	}
}
