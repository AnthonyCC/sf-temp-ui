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
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.fdstore.ecoupon.EnumCouponOfferType;
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
					//basePrice = price.getBasePrice() * (1.0 - discount.getAmount());
					double percentoff = price.getBasePrice() * discount.getAmount();
					if(discount.getMaxPercentageDiscount() > 0 && percentoff > discount.getMaxPercentageDiscount()) {
						percentoff = discount.getMaxPercentageDiscount();
					}
					basePrice = price.getBasePrice() - percentoff;
				}
			} else {
				// percent off from base price
				//basePrice = price.getBasePrice() * (1.0 - discount.getAmount());
				double percentoff = price.getBasePrice() * discount.getAmount();
				if(discount.getMaxPercentageDiscount() > 0 && percentoff > discount.getMaxPercentageDiscount()) {
					percentoff = discount.getMaxPercentageDiscount();
				}
				basePrice = price.getBasePrice() - percentoff;
			}
            // round to nearest cent
            basePrice = MathUtil.roundDecimal(basePrice);
            
			return new Price(basePrice, price.getSurcharge());

		} else if ( EnumDiscountType.DOLLAR_OFF.equals(pt) ) {
			// $$$ off
			double basePrice = 0.0;
			
			/*
			 * APPDEV-4148: changed for displaying the unit price 
			 *of the item as difference of Actual price and unit discount
			 * when the quantity is less than SKU limit
			 */
			
			//if(discount.getSkuLimit() > 0){
			if(discount.getSkuLimit() > 0 && discount.getSkuLimit() < quantity) {
				if(!"lb".equalsIgnoreCase(pricingUnit)) {
					basePrice = price.getBasePrice() - (discount.getAmount() * discount.getSkuLimit());
				}
				else {
					basePrice = price.getPrice() - discount.getAmount();
				}
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
	 * @param scaleQuantity 
	 * 
	 * @return price in USD
	 *
	 * @throws PricingException
	 */
	public static ConfiguredPrice getConfiguredPrice(Pricing pricing, FDConfigurableI configuration, PricingContext pCtx, FDGroup group, double grpQuantity, Double scaleQuantity) throws PricingException {

		if (DEBUG) LOGGER.debug("getConfiguredPrice got  " + configuration);

		// calculate base price
		ConfiguredPrice basePrice = calculateMaterialPrice(pricing, configuration, pCtx, group, grpQuantity,scaleQuantity);
		if (DEBUG) LOGGER.debug("getConfiguredPrice basePrice: "+basePrice);

		// calculate characteristic value prices
		double surcharge = calculateCVPrices(pricing, configuration,pCtx);
		if (DEBUG) LOGGER.debug("getConfiguredPrice surcharges: "+surcharge);

		Price pr = new Price(MathUtil.roundDecimal(basePrice.getPrice().getBasePrice()), MathUtil.roundDecimal(surcharge));
		return new ConfiguredPrice(pr, basePrice.getPricingCondition(),basePrice.getZoneInfo(), basePrice.getUnscaledPrice());
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
	 * @param scaleQuantity 
	 *
	 * @return price in USD
	 *
	 * @throws PricingException
	 */
	private static ConfiguredPrice calculateMaterialPrice(Pricing pricing, FDConfigurableI configuration, PricingContext ctx, FDGroup group, double grpQuantity, Double scaleQuantity) throws PricingException {
		// calculate price
		// check grpPrice exists
		//Group Scale Pricing
		if (group != null)
		{
				try{
					MaterialPrice grpMaterialPrice = GroupScaleUtil.getGroupScalePrice(group, ctx!=null?ctx.getZoneInfo():null);
					if(grpMaterialPrice != null) {
						ZoneInfo grpScalePriceZoneInfo = GroupScaleUtil.getGroupPricingZoneId(group, ctx!=null?ctx.getZoneInfo():null);
						//Group Price exists for this zone.
						if(grpQuantity > 0 && grpQuantity >= grpMaterialPrice.getScaleLowerBound()){
							//Required when multiple group scale prices are supported.
							// find pricing condition for quantity (in scaleUnit)
							//MaterialPrice grpMaterialPrice = GroupScaleUtil.getGroupScalePriceByQty(group, ctx.getZoneId(), grpQuantity);
//							return calculateGrpScalePrice(pricing, configuration, grpQuantity, grpMaterialPrice,ctx.getZoneInfo());		
							return calculateGrpScalePrice(pricing, configuration, grpQuantity, grpMaterialPrice,grpScalePriceZoneInfo);
						} else {
							return calculateSimplePrice(pricing, configuration, ctx!=null?ctx.getZoneInfo():null);
						}
					}
				}catch(FDResourceException fe){
					throw new PricingException(fe);
				}
		}
		//Regular Pricing
		ZonePriceModel zonePriceModel=pricing.getZonePrice(ctx!=null?ctx.getZoneInfo():null);
		if(zonePriceModel==null) {
			ZoneInfo zone=ctx!=null?ctx.getZoneInfo():null;
			while(zone!=null && zone.hasParentZone() && zonePriceModel==null ) {
				zone=zone.getParentZone();
				zonePriceModel=pricing.getZonePrice(zone);
			}
		}
		if(zonePriceModel==null) 
			throw new PricingException("No price defined for "+(ctx!=null?ctx.getZoneInfo():null));
		if (zonePriceModel.hasScales()) {
			return calculateScalePrice(pricing, configuration, ctx!=null?ctx.getZoneInfo():null,scaleQuantity);		
		} else {
			return calculateSimplePrice(pricing, configuration, ctx!=null?ctx.getZoneInfo():null);
		}
	}

	
	/**
	 * Perform pricing with scales.
	 *
	 * @return material price
	 *
	 * @throws PricingException
	 */
	private static ConfiguredPrice calculateGrpScalePrice(Pricing pricing, FDConfigurableI configuration, double grpQuantity, MaterialPrice grpMaterialPrice,ZoneInfo zone) throws PricingException {
		
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
		return new ConfiguredPrice(new Price(price), grpMaterialPrice,zone);
	}

	
	
	/**
	 * Perform pricing with scales.
	 * @param scaleQuantity 
	 *
	 * @return material price
	 *
	 * @throws PricingException
	 */
	private static ConfiguredPrice calculateScalePrice(Pricing pricing, FDConfigurableI configuration, ZoneInfo pricingZone, Double scaleQuantity) throws PricingException {
		
		double quantity = configuration.getQuantity();
		String salesUnit = configuration.getSalesUnit();
		
		// pricing with scales
		String scaleUnit = pricing.getZonePrice(pricingZone).getScaleUnit();
		double scaledQuantity ;
		if ( !salesUnit.equals(scaleUnit) ) {
			// different UOMs, perform conversion
			SalesUnitRatio ratio = pricing.findSalesUnitRatio(salesUnit);
			if (ratio==null) {
				throw new PricingException("No salesUnitRatio found for "+salesUnit+" in "+pricing);
			}
			// mutliply by ratio
			scaledQuantity = scaleQuantity==null? (quantity * ratio.getRatio()): (scaleQuantity * ratio.getRatio());		
			
			if ( !scaleUnit.equals( ratio.getSalesUnit() ) ) {
				// this is not the scale unit yet, we need another conversion (division)
				ratio = pricing.findSalesUnitRatio( scaleUnit );
				if (ratio==null) {
					throw new PricingException("No salesUnitRatio for scale "+scaleUnit+" in "+pricing);
				}
				scaledQuantity /= ratio.getRatio();
			}
		} else {
			if(null==scaleQuantity){
			scaledQuantity = quantity;
			}else{
				scaledQuantity = scaleQuantity;
	
			}
		}
		if (DEBUG) LOGGER.debug("Scale pricing - scaledQuantity "+scaledQuantity+" "+scaleUnit);

		// find pricing condition for quantity (in scaleUnit)
		ZonePriceModel zpm=pricing.getZonePrice(pricingZone);
		MaterialPrice materialPrice = zpm.findMaterialPrice(scaledQuantity);
		// get the base price for quantity = 1
		MaterialPrice materialPriceQuantity1 = zpm.findMaterialPrice(1);
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
		return new ConfiguredPrice(new Price(price), materialPrice,zpm.getPricingZone(), materialPriceQuantity1 != null? materialPriceQuantity1.getOriginalPrice() * pricingQuantity : 0);
	}

	/**
	 * Perform pricing without scales.
	 *
	 * @return material price
	 * @throws PricingException
	 */
	private static ConfiguredPrice calculateSimplePrice(Pricing pricing, FDConfigurableI configuration, ZoneInfo pricingZone) throws PricingException {

		double quantity = configuration.getQuantity();
		String salesUnit = configuration.getSalesUnit();

		// regular pricing
		ZonePriceModel zpm=pricing.getZonePrice(pricingZone);
		ZoneInfo pz=pricingZone;
		while(zpm==null && pz.hasParentZone() ) {
			pz=pz.getParentZone();
			zpm=pricing.getZonePrice(pz);
			
		}
		
		MaterialPrice materialPrice = zpm.findMaterialPrice(salesUnit);
		if (materialPrice==null) {
			// not found, we need a ratio
			SalesUnitRatio ratio = pricing.findSalesUnitRatio(salesUnit);
			if (ratio==null) {
				throw new PricingException("No salesUnitRatio found for "+salesUnit+" in "+pricing);
			}

			// find pricing condition by pricing unit
			zpm=pricing.getZonePrice(pricingZone);
			if (zpm == null) {
				throw new PricingException("No materialPrice found for "+ratio.getSalesUnit());
			}
			materialPrice = zpm.findMaterialPrice( ratio.getSalesUnit() );
			if (materialPrice == null) {
				throw new PricingException("No materialPrice found for "+ratio.getSalesUnit());
			}
			if (DEBUG) LOGGER.debug("calculateMaterialPrice ["+quantity+" * "+ratio.getRatio()+" * "+materialPrice.getPrice()+"]");
			
			double price = quantity * ratio.getRatio() * materialPrice.getPrice();
			return new ConfiguredPrice(new Price(price), materialPrice,zpm.getPricingZone());

		} else {
			// found, just apply price
			if (DEBUG) LOGGER.debug("calculateMaterialPrice ["+quantity+" * "+materialPrice.getPrice()+"]");

			double price = quantity * materialPrice.getPrice();
			return new ConfiguredPrice(new Price(price), materialPrice,zpm.getPricingZone());
		}
	}

	/**
	 * Calculate characteristic value prices.
	 *
	 * @throws PricingException
	 */
	private static double calculateCVPrices(Pricing pricing, FDConfigurableI configuration,PricingContext pCtx) throws PricingException {
        
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
			CharacteristicValuePrice cvp = pricing.findCharacteristicValuePrice(characteristic, chValue,pCtx);
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

	
	public static Price applyCouponDiscount(Price price, double quantity, ErpCouponDiscountLineModel couponDiscount, String pricingUnit) throws PricingException {
		if (couponDiscount==null) {
			return price;
		}
		EnumCouponOfferType pt = couponDiscount.getDiscountType();
		if ( EnumCouponOfferType.PERCENT_OFF.equals(pt) || EnumCouponOfferType.DOLLAR_OFF.equals(pt)) {
			double basePrice = 0.0;
			basePrice = price.getBasePrice() - (couponDiscount.getDiscountAmt() * quantity);			
	        // round to nearest cent
	        basePrice = MathUtil.roundDecimal(basePrice);            
			return new Price(basePrice, price.getSurcharge());
		}
		/*EnumCouponOfferType pt = couponDiscount.getDiscountType();
		if ( EnumCouponOfferType.PERCENT_OFF.equals(pt) ) {			
			double basePrice = 0.0;
			// percent off from base price
			//basePrice = price.getBasePrice() * (1.0 - discount.getAmount());
			double percentoff = price.getBasePrice() * couponDiscount.getDiscountAmt();
			basePrice = price.getBasePrice() - percentoff;
            // round to nearest cent
            basePrice = MathUtil.roundDecimal(basePrice);            
			return new Price(basePrice, price.getSurcharge());

		} else if ( EnumCouponOfferType.DOLLAR_OFF.equals(pt) ) {
			double basePrice = 0.0;
			basePrice = price.getBasePrice() - (couponDiscount.getDiscountAmt() * quantity);			
            // round to nearest cent
            basePrice = MathUtil.roundDecimal(basePrice);            
			return new Price(basePrice, price.getSurcharge());
		}*/

		throw new PricingException("Unsupported coupon discount type "+couponDiscount);
	}
}
