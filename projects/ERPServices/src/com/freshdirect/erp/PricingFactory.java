/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp;

import java.util.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import com.freshdirect.common.pricing.*;
import com.freshdirect.erp.model.*;

/**
 * Factory class that creates Pricing objects from Erp model objects.
 *
 * @version $Revision$
 * @author $Author$
 */
public class PricingFactory {

	private static Category LOGGER = LoggerFactory.getInstance( PricingFactory.class );
	private final static boolean DEBUG = false;
	
	/**
	 * Build a Pricing object for a single material.
	 * 
	 * @param material ErpMaterialModel object
	 * @param cvPrices array of ErpCharacteristicValuePriceModel objects
	 */
	public static Pricing getPricing(ErpMaterialModel material, ErpCharacteristicValuePriceModel[] charValuePrices) {
		// build material pricing conditions
		if (DEBUG) LOGGER.debug("Building material pricing");

		MaterialPrice[] materialPrices = buildMaterialPrices(
			(ErpMaterialPriceModel[]) material.getPrices().toArray(new ErpMaterialPriceModel[0])
		);

		// build characteristic value pricing conditions
		if (DEBUG) LOGGER.debug("Building CV pricing");
		CharacteristicValuePrice[] cvPrices = buildCVPrices( material.getCharacteristics(), charValuePrices );

		// build sales unit ratios
		if (DEBUG) LOGGER.debug("Building sales unit ratios");
		SalesUnitRatio[] salesUnitRatios = buildSalesUnitRatios( material.getSalesUnits() );

		return new Pricing(materialPrices, cvPrices, salesUnitRatios);
	}

	/**
	 * Build a MaterialPrice array.
	 *
	 * @param erpPrices array of ErpMaterialPriceModel objects
	 */
	private static MaterialPrice[] buildMaterialPrices(ErpMaterialPriceModel[] erpPrices) {

		MaterialPrice[] prices = new MaterialPrice[ erpPrices.length ];
		if (prices.length==0) {
			return prices;
		}

		// get scale unit from first erp price
		String scaleUnit = erpPrices[0].getScaleUnit();

		if ("".equals(scaleUnit.trim())) {
			// no scales
			for (int i=0; i<prices.length; i++) {
				ErpMaterialPriceModel p=erpPrices[i];
				if (DEBUG) LOGGER.debug("Adding new MaterialPrice ["+ p.getPrice() +","+ p.getPricingUnit() +"]");
				prices[i] = new MaterialPrice(p.getPrice(), p.getPricingUnit());
			}

		} else {
			// with scales

			// sort by scale quantity ascending
			Arrays.sort(erpPrices, new Comparator() {
				public int compare(Object o1, Object o2) {
					Double d1 = new Double( ((ErpMaterialPriceModel)o1).getScaleQuantity() );
					Double d2 = new Double( ((ErpMaterialPriceModel)o2).getScaleQuantity() );
					return d1.compareTo(d2);
				}
			} );

			double lower;
			double upper;
			for (int i=0; i<prices.length; i++) {
				ErpMaterialPriceModel p=erpPrices[i];
				lower=p.getScaleQuantity();
				if (i==prices.length-1) {
					// last one
					upper=Double.POSITIVE_INFINITY;
				} else {
					upper=erpPrices[i+1].getScaleQuantity();
				}
				if (DEBUG) LOGGER.debug("Adding new MaterialPrice w/ scale ["+ p.getPrice() +","+ p.getPricingUnit() +","+ lower +","+ upper +","+ scaleUnit +"]");
				prices[i] = new MaterialPrice(p.getPrice(), p.getPricingUnit(), lower, upper, scaleUnit);
			}
		}
		return prices;
	}

	private final static CharacteristicValuePrice[] EMPTY_CHARVALUEPRICES = new CharacteristicValuePrice[0];
	
	/**
	 * @param characteristics collection of ErpCharacteristic objects
	 */
	private static CharacteristicValuePrice[] buildCVPrices(List characteristics, ErpCharacteristicValuePriceModel[] cvPrices) {
		
		if (cvPrices.length==0) {
			// no pricing conditions
			return EMPTY_CHARVALUEPRICES;
		}

		// build a hashmap of characteristic value IDs - [charName, charValueName] string arrays
		Map chars = new HashMap();
		for (Iterator chi=characteristics.iterator(); chi.hasNext(); ) {
			ErpCharacteristicModel ch = (ErpCharacteristicModel)chi.next();
			List cvs = ch.getCharacteristicValues();

			for (Iterator cvi=cvs.iterator(); cvi.hasNext(); ) {
				ErpCharacteristicValueModel cv = (ErpCharacteristicValueModel)cvi.next();
				String[] cvData = new String[2];
				cvData[0] = ch.getName();
				cvData[1] = cv.getName();
				chars.put( cv.getPK().getId(), cvData );
			}
		}

		// create char value prices
		CharacteristicValuePrice[] prices = new CharacteristicValuePrice[cvPrices.length];

		for (int i=0; i<prices.length; i++) {
			ErpCharacteristicValuePriceModel cvp = cvPrices[i];
			
			String condType = cvp.getConditionType();
			int applyHow;
			if (condType.equals("VA00")) {
				applyHow = CharacteristicValuePrice.PER_PRICING_UNIT;
			} else if (condType.equals("ZA00") || condType.equals("ZP00")) {
				applyHow = CharacteristicValuePrice.PER_SALES_UNIT;
			} else {
				// !!! use more proper exception
				throw new RuntimeException("Unknown condition type for charvalue pricing condition "+cvp.getPK());
			}

			// look up [charName, charValueName] by charvalue ID
			String[] cvData = (String[])chars.get( cvp.getCharacteristicValueId() );
			if (cvData == null) {
				// !!! use more proper exception
				throw new RuntimeException("Data integrity problem: couldn't find charvalue for charvalue pricing condition "+cvp.getPK());
			}
			prices[i] = new CharacteristicValuePrice(
				cvData[0],
				cvData[1],
				cvp.getPrice(),
				cvp.getPricingUnit(),
				applyHow );
		}
		
		return prices;
	}

	/**
	 * @param salesUnits collection of ErpSalesUnitModel objects
	 */
	private static SalesUnitRatio[] buildSalesUnitRatios(List salesUnits) {
		SalesUnitRatio[] ratios = new SalesUnitRatio[ salesUnits.size() ];
		for (int i=0; i<ratios.length; i++) {
			ErpSalesUnitModel su = (ErpSalesUnitModel) salesUnits.get(i);
			ratios[i] = new SalesUnitRatio(
				su.getAlternativeUnit(),
				su.getBaseUnit(),
				(double)su.getNumerator() / (double)su.getDenominator() );
		}
		return ratios;
	}


}