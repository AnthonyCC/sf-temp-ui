package com.freshdirect.fdstore;

import java.io.Serializable;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.util.DealsHelper;

public class ZonePriceModel implements Serializable {
	private MaterialPrice[] materialPrices;
	private String sapZoneId;

	/**
	 * Get all material pricing conditions.
	 *
	 * @return array of MaterialPrice objects
	 */
	
	public ZonePriceModel(String sapZoneId, MaterialPrice[] matPrices) {
		this.sapZoneId = sapZoneId;
		this.materialPrices = matPrices;
	}
	
	public MaterialPrice[] getMaterialPrices() {
		return this.materialPrices;
	}

	public String getSapZoneId() {
		return this.sapZoneId;
	}
	/**
	 * Get matching pricing condition for pricing unit.
	 * Check if material has Promo Price and is valid. If so 
	 * then return promo price else selling price.
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
	
	public String[] getScaleDisplay(double savingsPercentage) {
		String[] scales = new String[ this.materialPrices.length-1 ];
		for (int i=0; i<scales.length; i++) {
			scales[i] = this.materialPrices[i+1].getScaleDisplay(savingsPercentage);
		}
		return scales;
	}

	public int[] getScalePercentage(double basePrice) {
		int[] percents = new int[ this.materialPrices.length-1 ];
		for (int i=0; i<percents.length; i++) {
			percents[i] = this.materialPrices[i+1].getScalePercentage(basePrice);
		}
		return percents;
	}
	
	public String[] getWineScaleDisplay(boolean isBreakRequired) {
		String[] scales = new String[ this.materialPrices.length-1 ];
		for (int i=0; i<scales.length; i++) {
			scales[i] = this.materialPrices[i+1].getWineScaleDisplay(isBreakRequired);
		}
		return scales;
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

	public double getMaxUnitPrice() {
		if (materialPrices.length == 0)
			return Double.NaN;

		double p = materialPrices[0].getPrice();
		for (int i=1; i<materialPrices.length; ++i) {
			p = Math.max(p, materialPrices[i].getPrice());
		}

		return p;
	}

}
