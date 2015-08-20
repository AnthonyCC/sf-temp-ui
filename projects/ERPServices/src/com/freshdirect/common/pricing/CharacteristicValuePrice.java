package com.freshdirect.common.pricing;

import java.io.Serializable;

/**
 * Class encapsulation pricing information for a characteristic value.
 *
 * @version $Revision: 4$
 * @author $Author: Viktor Szathmary$
 */
public class CharacteristicValuePrice implements Serializable {

	private static final long	serialVersionUID	= 5078567556846051266L;

	/** Apply price per pricing unit, using ratio */
	public final static int PER_PRICING_UNIT = 0;

	/** Apply price per sales unit, no ratio involved */
	public final static int PER_SALES_UNIT = 1;

	/** Characteristic name */
	private String charName;

	/** Characteristic value name */
	private String charValueName;

	/** Characteristic value price in USD */
	private double price;

	/** Pricing unit of measure */
	private String pricingUnit;
	
	/** How to apply condition (cond. type) */
	private int applyHow;
	
	/** Sales Organisation */
	private String salesOrg;
	
	/** Distribution Channel */
	private String distChannel;

	public CharacteristicValuePrice(String charName, String charValueName, double price, String pricingUnit, int applyHow, String salesOrg, String distChannel) {
		this.charName=charName;
		this.charValueName=charValueName;
		this.price=price;
		this.pricingUnit=pricingUnit;
		this.applyHow=applyHow;
		this.salesOrg = salesOrg;
		this.distChannel = distChannel;
	}

	public String getCharacteristicName() {
		return this.charName;
	}

	/**
	 * @return Characteristic value name
	 */
	public String getCharValueName() {
		return this.charValueName;
	}

	/**
	 * @return price in USD for this CV
	 */
	public double getPrice() {
		return this.price;
	}

	/**
	 * @return Optional pricing unit of measure (empty string if none).
	 */
	public String getPricingUnit() {
		return this.pricingUnit;
	}
	
	/**
	 * @return PER_PRICING_UNIT or PER_SALES_UNIT
	 */
	public int getApplyHow() {
		return this.applyHow;
	}

	/**
	 * @return the salesOrg
	 */
	public String getSalesOrg() {
		return salesOrg;
	}

	/**
	 * @return the distChannel
	 */
	public String getDistChannel() {
		return distChannel;
	}

	@Override
	public String toString() {
		return "CharacteristicValuePrice["+charName+" "+charValueName+" $"+price+" "+pricingUnit+" "+applyHow+"]";
	}
}
