/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.pricing;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Pricing engine - performs pricing.
 *
 * @version $Revision$
 * @author $Author$
 */
public class PricingEngine {

	private final static boolean DEBUG = false;
	
	private static Category LOGGER = LoggerFactory.getInstance( PricingEngine.class );


	
	/**
	 * Apply discount to a price.
	 *
	 * @param price price before discounts
	 * @param quantity ordered quantity (in sales units)
	 * @param discount Discount
	 *
	 * @return price with discount applied
	 */
	public static Price applyDiscount(Price price, double quantity, Discount discount) throws PricingException {
		if (discount==null) {
			// no promotion
			return price;
		}
		EnumDiscountType pt = discount.getDiscountType();
		if ( EnumDiscountType.FREE.equals(pt) || EnumDiscountType.SAMPLE.equals(pt) ) {
			// free item
			return new Price(0.0);

		} else if ( EnumDiscountType.PERCENT_OFF.equals(pt) ) {
			// percent off from base price
			double basePrice = price.getBasePrice() * (1.0 - discount.getAmount());
            // round to nearest cent
            basePrice = MathUtil.roundDecimal(basePrice);
			return new Price(basePrice, price.getSurcharge());

		} else if ( EnumDiscountType.DOLLAR_OFF.equals(pt) ) {
			// $$$ off
			double basePrice = price.getBasePrice() - (discount.getAmount() * quantity);
            // round to nearest cent
            basePrice = MathUtil.roundDecimal(basePrice);
			return new Price(basePrice, price.getSurcharge());
		}

		throw new PricingException("Unsupported discount type "+discount);
	}

	/**
	 * Perform pricing logic. After the call the configuration will contain
	 * the pricing condition that was used for calculating the base price.
	 *
	 * @param pricing Pricing object
	 * @param configuration PricingConfiguration object
	 * 
	 * @return price in USD
	 *
	 * @throws PricingException
	 */
	public static ConfiguredPrice getConfiguredPrice(Pricing pricing, FDConfigurableI configuration, PricingContext pCtx) throws PricingException {

		if (DEBUG) LOGGER.debug("getConfiguredPrice got " + configuration);

		// calculate base price
		ConfiguredPrice basePrice = calculateMaterialPrice(pricing, configuration, pCtx.getZoneId());
		if (DEBUG) LOGGER.debug("getConfiguredPrice basePrice: "+basePrice);

		// calculate characteristic value prices
		double surcharge = calculateCVPrices(pricing, configuration);
		if (DEBUG) LOGGER.debug("getConfiguredPrice surcharges: "+surcharge);

		Price pr = new Price(MathUtil.roundDecimal(basePrice.getPrice().getBasePrice()), MathUtil.roundDecimal(surcharge));
		return new ConfiguredPrice(pr, basePrice.getPricingCondition());
	}

	/*
	public static Pricing getCompositePricing(double compositePrice) {
		MaterialPrice matPrices[] = { new MaterialPrice(compositePrice, "EA") };
		CharacteristicValuePrice[] cvPrices = new CharacteristicValuePrice[0];
		SalesUnitRatio[] suRatios = new SalesUnitRatio[0];
		return new Pricing(matPrices, cvPrices, suRatios);
	}
	*/
	/**
	 * Calculate material price.
	 *
	 * @return price in USD
	 *
	 * @throws PricingException
	 */
	private static ConfiguredPrice calculateMaterialPrice(Pricing pricing, FDConfigurableI configuration, String pZoneId) throws PricingException {
		// calculate price
		if (pricing.getZonePrice(pZoneId).hasScales()) {
			return calculateScalePrice(pricing, configuration, pZoneId);
		
		} else {
			return calculateSimplePrice(pricing, configuration, pZoneId);
		}
	}

	/**
	 * Perform pricing with scales.
	 *
	 * @return material price
	 *
	 * @throws PricingException
	 */
	private static ConfiguredPrice calculateScalePrice(Pricing pricing, FDConfigurableI configuration, String pZoneId) throws PricingException {
		
		double quantity = configuration.getQuantity();
		String salesUnit = configuration.getSalesUnit();
		
		// pricing with scales
		String scaleUnit = pricing.getZonePrice(pZoneId).getScaleUnit();
		double scaledQuantity;
		if ( !salesUnit.equals(scaleUnit) ) {
			// different UOMs, perform conversion
			SalesUnitRatio ratio = pricing.findSalesUnitRatio(salesUnit);
			if (ratio==null) {
				throw new PricingException("No salesUnitRatio found for "+salesUnit+" in "+pricing);
			}
			// mutliply by ratio
			scaledQuantity = quantity * ratio.getRatio();		
			if ( !scaleUnit.equals( ratio.getSalesUnit() ) ) {
				// this is not the scale unit yet, we need another conversion (division)
				ratio = pricing.findSalesUnitRatio( scaleUnit );
				if (ratio==null) {
					throw new PricingException("No salesUnitRatio for scale "+scaleUnit+" in "+pricing);
				}
				scaledQuantity /= ratio.getRatio();
			}
		} else {
			scaledQuantity = quantity;
		}
		if (DEBUG) LOGGER.debug("Scale pricing - scaledQuantity "+scaledQuantity+" "+scaleUnit);

		// find pricing condition for quantity (in scaleUnit)
		MaterialPrice materialPrice = pricing.getZonePrice(pZoneId).findMaterialPrice(scaledQuantity);
	
		double pricingQuantity;
		if ( !salesUnit.equals(materialPrice.getPricingUnit()) ) {
			// we need a ratio
			SalesUnitRatio ratio = pricing.findSalesUnitRatio(salesUnit);
			if (ratio==null) {
				throw new PricingException("No salesUnitRatio found for "+salesUnit+" in "+pricing);
			}
			pricingQuantity = quantity * ratio.getRatio();
		} else {
			pricingQuantity = quantity;
		}

		if (DEBUG) LOGGER.debug("Scale pricing [" + pricingQuantity + " * " + materialPrice.getPrice() + "]");

		double price = pricingQuantity * materialPrice.getPrice();
		return new ConfiguredPrice(new Price(price), materialPrice);
	}

	/**
	 * Perform pricing without scales.
	 *
	 * @return material price
	 * @throws PricingException
	 */
	private static ConfiguredPrice calculateSimplePrice(Pricing pricing, FDConfigurableI configuration, String pZoneId) throws PricingException {

		double quantity = configuration.getQuantity();
		String salesUnit = configuration.getSalesUnit();

		// regular pricing
		MaterialPrice materialPrice = pricing.getZonePrice(pZoneId).findMaterialPrice(salesUnit);
		if (materialPrice==null) {
			// not found, we need a ratio
			SalesUnitRatio ratio = pricing.findSalesUnitRatio(salesUnit);
			if (ratio==null) {
				throw new PricingException("No salesUnitRatio found for "+salesUnit+" in "+pricing);
			}

			// find pricing condition by pricing unit
			materialPrice = pricing.getZonePrice(pZoneId).findMaterialPrice( ratio.getSalesUnit() );
			if (materialPrice == null) {
				throw new PricingException("No materialPrice found for "+ratio.getSalesUnit());
			}
			if (DEBUG) LOGGER.debug("calculateMaterialPrice ["+quantity+" * "+ratio.getRatio()+" * "+materialPrice.getPrice()+"]");
			
			double price = quantity * ratio.getRatio() * materialPrice.getPrice();
			return new ConfiguredPrice(new Price(price), materialPrice);

		} else {
			// found, just apply price
			if (DEBUG) LOGGER.debug("calculateMaterialPrice ["+quantity+" * "+materialPrice.getPrice()+"]");

			double price = quantity * materialPrice.getPrice();
			return new ConfiguredPrice(new Price(price), materialPrice);
		}
	}

	/**
	 * Calculate characteristic value prices.
	 *
	 * @throws PricingException
	 */
	private static double calculateCVPrices(Pricing pricing, FDConfigurableI configuration) throws PricingException {

		double quantity = configuration.getQuantity();
		String salesUnit = configuration.getSalesUnit();
		Map options = configuration.getOptions();
		
		double price=0.0;

		Iterator i = options.keySet().iterator();
		while (i.hasNext()) {
			String characteristic = (String) i.next();
			String chValue = (String) options.get(characteristic);
			if (DEBUG) LOGGER.debug("trying to price CV "+characteristic+","+chValue);
			CharacteristicValuePrice cvp = pricing.findCharacteristicValuePrice(characteristic, chValue);
			if (cvp==null) {
				if (DEBUG) LOGGER.debug("No pricing info, assuming 0, skipping");
				continue;
			}
			switch (cvp.getApplyHow()) {
				case CharacteristicValuePrice.PER_PRICING_UNIT:		// VA00
					if ( salesUnit.equals( cvp.getPricingUnit() )) {
						price += quantity * cvp.getPrice();
					} else {
						SalesUnitRatio ratio = pricing.findSalesUnitRatio(salesUnit);
						price += quantity * ratio.getRatio() * cvp.getPrice();
					}

					break;
				case CharacteristicValuePrice.PER_SALES_UNIT:		// ZA00
					price += quantity * cvp.getPrice();
					break;
				default:
					throw new PricingException("Unknown char.value pricing condition encountered");
			}
		}
		return price;
	}

}
