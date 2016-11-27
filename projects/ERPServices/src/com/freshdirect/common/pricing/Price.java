/*
 * $Workfile: Price.java$
 *
 * $Date: 8/14/2001 5:10:00 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.pricing;

/**
 * Unmutable object representing a price in USD.
 * It has two parts: base price and surcharges.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */
public class Price {
	/** Base price in USD */
	private final double basePrice;
	
	/** Surcharges in USD */
	private final double surcharge;

	public Price(double basePrice) {
		this(basePrice, 0.0);
	}
	
	public Price(double basePrice, double surcharge) {
		this.basePrice=basePrice;
		this.surcharge=surcharge;
	}

	/**
	 * Get the "full" price (sum of base price and surcharges);
	 *
	 * @return price in USD
	 */
	public double getPrice() {
		return this.basePrice + this.surcharge;
	}
	
	/**
	 * Get the base price.
	 *
	 * @return price in USD
	 */
	public double getBasePrice() {
		return this.basePrice;
	}
	
	/**
	 * Get surcharges.
	 *
	 * @return price in USD
	 */
	public double getSurcharge() {
		return this.surcharge;
	}

	public Price add(Price p) {
		return new Price(
			this.basePrice + p.getBasePrice(),
			this.surcharge + p.getSurcharge()
		);
	}
}
