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
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Locale;

import com.freshdirect.common.pricing.util.DealsHelper;

/**
 * Material pricing condition.
 *
 * @version $Revision$
 * @author $Author$
 */
public class MaterialPrice implements Serializable {

	private final static NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
	private final static DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat("0.##");

	/** Price in USD */
	private double price;

	/** Pricing unit of measure */
	private String pricingUnit;

	/** Scale lower bound (inclusive - qty >= scaleLowerBound). Zero if no scales apply.  */
	private double scaleLowerBound;

	/** Scale upper bound (exclusive - qty < scaleUpperBound). Double.POSITIVE_INFINITY if no scales apply. */
	private double scaleUpperBound;

	/** Scale unit of measure. Empty string if no scales apply. */
	private String scaleUnit;

	private double promoPrice;
	
	public MaterialPrice(double price, String pricingUnit,double promoPrice) {
		this(price, pricingUnit, 0.0, Double.POSITIVE_INFINITY, "", promoPrice);
	}
	
	
	

	public MaterialPrice(double price, String pricingUnit, double scaleLowerBound, double scaleUpperBound, String scaleUnit, double promoPrice) {
		this.price=price;
		this.pricingUnit=pricingUnit != null ? pricingUnit.intern() : null;
		this.scaleLowerBound=scaleLowerBound;
		this.scaleUpperBound=scaleUpperBound;
		this.scaleUnit=scaleUnit != null? scaleUnit.intern() : null;
		this.promoPrice = promoPrice;
	}

	public double getPrice() {
		if(DealsHelper.isItemOnSale(this.price, this.promoPrice))
			return this.promoPrice;
		else
			return this.price;
	}
	
	public String getPricingUnit() {
		return this.pricingUnit;
	}

	public double getScaleLowerBound() {
		return this.scaleLowerBound;
	}

	public double getScaleUpperBound() {
		return this.scaleUpperBound;
	}

	public String getScaleUnit() {
		return this.scaleUnit;
	}

	/**
	 * Check if a quantity falls between the upper and lower scale bounds.
	 * 
	 * @param scaleQuantity quantity in scale unit of measure
	 * 
	 * @return true if quantity falls between upper and lower scale bounds
	 */
	public boolean isWithinBounds(double scaleQuantity) {
		return ( (scaleQuantity>=this.getScaleLowerBound()) && (scaleQuantity<this.getScaleUpperBound()) );
	}


	public String getScaleDisplay() {
		StringBuffer buf = new StringBuffer();
		if ( this.getPricingUnit().equals("EA") ) {

			buf.append( FORMAT_QUANTITY.format( this.getScaleLowerBound() ) );
			buf.append( " for " );
			buf.append( FORMAT_CURRENCY.format( this.getPrice() * this.getScaleLowerBound() ) );

		} else {

			buf.append( FORMAT_CURRENCY.format( this.getPrice() ) );
			buf.append( '/' );
			buf.append( this.getPricingUnit().toLowerCase() );

			buf.append( " for " );

			buf.append( FORMAT_QUANTITY.format( this.getScaleLowerBound() ) );
			buf.append( this.getScaleUnit().toLowerCase() );
			
			buf.append( " or more" );
		}
		return buf.toString();
	}
	
	public int getScalePercentage(double basePrice) {
		return (int) ((basePrice - this.getPrice()) * 100.0 / basePrice);
	}
	
	public String getScaleDisplay(double savingsPercentage) {
		StringBuffer buf = new StringBuffer();
		if ( this.getPricingUnit().equals("EA") ) {

			buf.append( FORMAT_QUANTITY.format( this.getScaleLowerBound() ) );
			buf.append( " for " );
			buf.append( FORMAT_CURRENCY.format( (this.getPrice() * (1-savingsPercentage)) * this.getScaleLowerBound() ) );

		} else {

			buf.append( FORMAT_CURRENCY.format( this.getPrice() * (1-savingsPercentage)) );
			buf.append( '/' );
			buf.append( this.getPricingUnit().toLowerCase() );

			buf.append( " for " );

			buf.append( FORMAT_QUANTITY.format( this.getScaleLowerBound() ) );
			buf.append( this.getScaleUnit().toLowerCase() );
			
			buf.append( " or more" );
		}
		return buf.toString();
	}
	public double getScaledPrice() {
		if ("EA".equalsIgnoreCase(getPricingUnit())) {
			return getPrice()*getScaleLowerBound();
		} else {
			return getPrice();
		}
	}
	
	
	public String getWineScaleDisplay(boolean isBreakRequired) {
		StringBuffer buf = new StringBuffer();
		if ( this.getPricingUnit().equals("EA") ) {
			buf.append(" Just " );
			buf.append( FORMAT_CURRENCY.format( this.getPrice()));
			buf.append( " each " );
			if(isBreakRequired){
			    buf.append("<BR>" );
			}
			buf.append( " when you buy " );
			buf.append( FORMAT_QUANTITY.format( this.getScaleLowerBound() ));
			buf.append( " or more." );
		}
		return buf.toString();
	}

	

	public String toString() {
		return "MaterialPrice[$"+price+" per "+pricingUnit+" scale: "+scaleLowerBound+" - "+scaleUpperBound+" "+scaleUnit+"promo "+promoPrice+"]";
	}
	
}
