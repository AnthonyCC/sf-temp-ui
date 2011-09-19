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

import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.GroupScalePricing;
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
	public static Price applyDiscount(Price price, double quantity, Discount discount, String pricingUnit) throws PricingException {
		if (discount==null) {
			// no promotion
			return price;
		}
		EnumDiscountType pt = discount.getDiscountType();
		if ( EnumDiscountType.FREE.equals(pt) || EnumDiscountType.SAMPLE.equals(pt) ) {
			// free item
			return new Price(0.0);

		} else if ( EnumDiscountType.PERCENT_OFF.equals(pt) ) {
			/*
			 * APPDEV-1784: Updating the pricing to implement sku limit into pricing
			 */
			double basePrice = 0.0;
			if(!"lb".equalsIgnoreCase(pricingUnit) && discount.getSkuLimit() > 0) {
				int skuLimit = discount.getSkuLimit();
				if(quantity > skuLimit) {
					//more items ordered than allowed.
					double unit_price = price.getBasePrice() / quantity;  //15/3=5
					double discount_allowed_amount = unit_price * skuLimit;  //5*2=10
					double remaining_amount = price.getBasePrice() - discount_allowed_amount; //15-10=5
					//apply discount on the discount_allowed_amount
					double discount_amount = discount_allowed_amount * (1.0 - discount.getAmount()); //10 * (1 - 0.50)=10*0.50=5.0
					basePrice = remaining_amount + discount_amount;  //5+5=10
				} else {
					basePrice = price.getBasePrice() * (1.0 - discount.getAmount());
				}
			} else {
				// percent off from base price
				basePrice = price.getBasePrice() * (1.0 - discount.getAmount());
			}
            // round to nearest cent
            basePrice = MathUtil.roundDecimal(basePrice);
            
			return new Price(basePrice, price.getSurcharge());

		} else if ( EnumDiscountType.DOLLAR_OFF.equals(pt) ) {
			// $$$ off
			double basePrice = 0.0;
			
			if(!"lb".equalsIgnoreCase(pricingUnit) && discount.getSkuLimit() > 0) {
				basePrice = price.getBasePrice() - (discount.getAmount() * discount.getSkuLimit());
			} else {
				basePrice = price.getBasePrice() - (discount.getAmount() * quantity);
			}
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
	public static ConfiguredPrice getConfiguredPrice(Pricing pricing, FDConfigurableI configuration, PricingContext pCtx, FDGroup group, double grpQuantity) throws PricingException {

		if (DEBUG) LOGGER.debug("getConfiguredPrice got " + configuration);

		// calculate base price
		ConfiguredPrice basePrice = calculateMaterialPrice(pricing, configuration, pCtx, group, grpQuantity);
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
	private static ConfiguredPrice calculateMaterialPrice(Pricing pricing, FDConfigurableI configuration, PricingContext ctx, FDGroup group, double grpQuantity) throws PricingException {
		// calculate price
		// check grpPrice exists
		//Group Scale Pricing
		if (group != null)
		{
				try{
					MaterialPrice grpMaterialPrice = GroupScaleUtil.getGroupScalePrice(group, ctx.getZoneId());
					if(grpMaterialPrice != null) {
						//Group Price exists for this zone.
						if(grpQuantity > 0 && grpQuantity >= grpMaterialPrice.getScaleLowerBound()){
							//Required when multiple group scale prices are supported.
							// find pricing condition for quantity (in scaleUnit)
							//MaterialPrice grpMaterialPrice = GroupScaleUtil.getGroupScalePriceByQty(group, ctx.getZoneId(), grpQuantity);
							return calculateGrpScalePrice(pricing, configuration, grpQuantity, grpMaterialPrice);		
						} else {
							return calculateSimplePrice(pricing, configuration, ctx.getZoneId());
						}
					}
				}catch(FDResourceException fe){
					throw new PricingException(fe);
				}
		}
		//Regular Pricing
		if (pricing.getZonePrice(ctx.getZoneId()).hasScales()) {
			return calculateScalePrice(pricing, configuration, ctx.getZoneId());		
		} else {
			return calculateSimplePrice(pricing, configuration, ctx.getZoneId());
		}
	}

	
	/**
	 * Perform pricing with scales.
	 *
	 * @return material price
	 *
	 * @throws PricingException
	 */
	private static ConfiguredPrice calculateGrpScalePrice(Pricing pricing, FDConfigurableI configuration, double grpQuantity, MaterialPrice grpMaterialPrice) throws PricingException {
		
		double quantity = configuration.getQuantity();
		String salesUnit = configuration.getSalesUnit();
		if (DEBUG) LOGGER.debug("group Scale pricing - scaledQuantity "+grpQuantity+" "+grpMaterialPrice.getScaleUnit());
		double pricingQuantity;
		if ( !salesUnit.equals(grpMaterialPrice.getPricingUnit()) ) { //Eg: EA vs LB
			// we need a ratio
			SalesUnitRatio ratio = pricing.findSalesUnitRatio(salesUnit);
			if (ratio==null) {
				throw new PricingException("No salesUnitRatio found for "+salesUnit+" in "+pricing);
			}
			pricingQuantity = quantity * ratio.getRatio();
		} else {
			pricingQuantity = quantity;
		}

		if (DEBUG) LOGGER.debug("Scale pricing [" + pricingQuantity + " * " + grpMaterialPrice.getPrice() + "]");

		double price = pricingQuantity * grpMaterialPrice.getPrice();
		return new ConfiguredPrice(new Price(price), grpMaterialPrice);
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
			//null check instead of throwing exception in findCharacteristicValuePrice
			chValue = (chValue == null) ? "" : chValue;
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
