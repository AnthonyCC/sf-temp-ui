package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.erp.model.ErpMaterialPriceModel;

public class ZonePriceModel implements Serializable {
	private static final long serialVersionUID = 3299833903663122981L;

	private MaterialPrice[] materialPrices;
	public void setMaterialPrices(MaterialPrice[] materialPrices) {
		this.materialPrices = materialPrices;
	}

	private String sapZoneId;
	/**
	 * Get all material pricing conditions.
	 *
	 * @return array of MaterialPrice objects
	 */
	
	public ZonePriceModel(String sapZoneId, MaterialPrice[] matPrices) {
		this.sapZoneId = sapZoneId != null ? sapZoneId.intern() : null;
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
	
	
	public void sortMaterialPrice(){	
		if(this.materialPrices==null || this.materialPrices.length==1) return;
		Arrays.sort(this.materialPrices, new Comparator() {
			public int compare(Object o1, Object o2) {
				Double d1 = new Double( ((MaterialPrice)o1).getScaleLowerBound() );
				Double d2 = new Double( ((MaterialPrice)o2).getScaleLowerBound() );
				return d1.compareTo(d2);
			}
		} );						
		double upper;
		for (int i=0; i<this.materialPrices.length; i++) {
			MaterialPrice p=this.materialPrices[i];		
			if (i==this.materialPrices.length-1) {
				// last one
				upper=Double.POSITIVE_INFINITY;
			} else {
				upper=this.materialPrices[i+1].getScaleLowerBound();
			}
			p.setScaleUpperBound(upper);
			//if (DEBUG) LOGGER.debug("Adding new MaterialPrice w/ scale ["+ p.getPrice() +","+ p.getPricingUnit() +","+ lower +","+ upper +","+ scaleUnit +","+p.getPromoPrice()+"]");																		
		}		
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
		return getScaleDisplay(0, null);
	}
	
	public String[] getScaleDisplay(double savingsPercentage) {
		return getScaleDisplay(savingsPercentage, null);
	}

	public String[] getScaleDisplay(double exc[]) {
		return getScaleDisplay(0, exc);
	}

	public static final double[] EMPTY_TIER_EXCLUSION_LIST = new double[0];
	
	/**
	 * Returns display of tiered pricing (scales)
	 * 
	 * @param savingsPercentage
	 * @param exc Exclude some scales from display
	 * @return
	 */
	public String[] getScaleDisplay(double savingsPercentage, double exc[]) {
		final String[] scales = new String[ this.materialPrices.length-1 ];
		
		List<String> scList = new ArrayList<String>(this.materialPrices.length-1);
		
		if (exc == null)
			exc = EMPTY_TIER_EXCLUSION_LIST;

		for (int i=0; i<scales.length; i++) {
			final MaterialPrice materialPrice = this.materialPrices[i+1];
			
			boolean skip = false;
			for (double lowerBound : exc) {
				if (lowerBound == materialPrice.getScaleLowerBound()) {
					// filter material price with matching lower bound
					skip = true;
					break;
				}
			}
			if (!skip) {
				scList.add( materialPrice.getScaleDisplay(savingsPercentage) );
			}
		}
		return scList.toArray(new String[0]);
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
