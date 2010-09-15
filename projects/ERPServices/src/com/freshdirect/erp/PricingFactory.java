/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Factory class that creates Pricing objects from Erp model objects.
 *
 * @version $Revision$
 * @author $Author$
 */
public class PricingFactory {

	private static Category LOGGER = LoggerFactory.getInstance( PricingFactory.class );
	private final static boolean DEBUG = false;
	
	public static Comparator erpMatPriceModelComparator  = new Comparator() {
		public int compare(Object obj1, Object obj2) {
				ErpMaterialPriceModel mp1 = (ErpMaterialPriceModel) obj1;
				ErpMaterialPriceModel mp2 = (ErpMaterialPriceModel) obj2;
				return mp1.getSapZoneId().compareTo(mp2.getSapZoneId());
		}
	};

	public static Comparator<ErpMaterialPrice> erpMatpriceComparator  = new Comparator<ErpMaterialPrice>() {
		public int compare(ErpMaterialPrice mp1, ErpMaterialPrice mp2) {
				return mp1.getSapZoneId().compareTo(mp2.getSapZoneId());
		}
	};
	
    public static Comparator<ErpMaterialPrice> scaleQuantityComparator = new Comparator<ErpMaterialPrice>() {
        public int compare(ErpMaterialPrice mp1, ErpMaterialPrice mp2) {
            return new Double(mp1.getScaleQuantity()).compareTo(new Double(mp2.getScaleQuantity()));
        }
    };
    
	
	/**
	 * Build a Pricing object for a single material.
	 * 
	 * @param material ErpMaterialModel object
	 * @param cvPrices array of ErpCharacteristicValuePriceModel objects
	 */
	public static Pricing getPricing(ErpMaterialModel material, ErpCharacteristicValuePriceModel[] charValuePrices) {
		// build material pricing conditions
		if (DEBUG) LOGGER.debug("Building material pricing");
		List<ErpMaterialPriceModel> materialPriceList =material.getPrices(); 
		//Collections.sort(materialPriceList, erpMatPriceModelComparator);
		String sapZoneId = "";
		List subList = new ArrayList<ErpMaterialPriceModel>();
		ZonePriceListing zonePriceList = new ZonePriceListing();
		MaterialPrice promoPrice = null;
		for(Iterator it=materialPriceList.iterator();it.hasNext();){
			ErpMaterialPriceModel erpMaterialPrice = (ErpMaterialPriceModel)it.next();
			
			if(sapZoneId.length() == 0 || sapZoneId.equals(erpMaterialPrice.getSapZoneId())){
					subList.add(erpMaterialPrice);
			}
			else if(!sapZoneId.equals(erpMaterialPrice.getSapZoneId())) {
				MaterialPrice[] materialPrices = buildMaterialPrices(
						(ErpMaterialPriceModel[]) subList.toArray(new ErpMaterialPriceModel[0]));
				ZonePriceModel zpModel = new ZonePriceModel(sapZoneId, materialPrices);
				zonePriceList.addZonePrice(zpModel);
				subList.clear();
				subList.add(erpMaterialPrice);
			}
			sapZoneId = erpMaterialPrice.getSapZoneId();
		} 
		//Do the same for the last zone in the list.
		MaterialPrice[] materialPrices = buildMaterialPrices(
				(ErpMaterialPriceModel[]) subList.toArray(new ErpMaterialPriceModel[0]));
		ZonePriceModel zpModel = new ZonePriceModel(sapZoneId, materialPrices);
		zonePriceList.addZonePrice(sapZoneId, zpModel);
		subList.clear();

		// build characteristic value pricing conditions
		if (DEBUG) LOGGER.debug("Building CV pricing");
		CharacteristicValuePrice[] cvPrices = buildCVPrices( material.getCharacteristics(), charValuePrices );

		// build sales unit ratios
		if (DEBUG) LOGGER.debug("Building sales unit ratios");
		SalesUnitRatio[] salesUnitRatios = buildSalesUnitRatios( material.getSalesUnits() );

		return new Pricing(zonePriceList, cvPrices, salesUnitRatios);
	}

	private static MaterialPrice buildPromoPrice(ErpMaterialPriceModel erpPrice) {

		// get scale unit from first erp price
		String scaleUnit = erpPrice.getScaleUnit();
		if (!"".equals(scaleUnit.trim())) {
			//Cannot have promo price for scaled pricing.
			return null;
		}
		if (DEBUG) LOGGER.debug("Adding new PromoPrice ["+ erpPrice.getPrice() +","+ erpPrice.getPricingUnit() +"]");
		return new MaterialPrice(erpPrice.getPrice(), erpPrice.getPricingUnit(),erpPrice.getPromoPrice());
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
				prices[i] = new MaterialPrice(p.getPrice(), p.getPricingUnit(), p.getPromoPrice());
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
			
			
			if(erpPrices.length>1)
			{
				List newSubList=new ArrayList();												
				ErpMaterialPriceModel basePrice=(ErpMaterialPriceModel)erpPrices[0];
				newSubList.add(basePrice);						
				for(int i=1;i<erpPrices.length;i++){								
					ErpMaterialPriceModel nextPrice=(ErpMaterialPriceModel)erpPrices[i];
					if(basePrice.getPromoPrice()>0){
						if(basePrice.getPromoPrice()>=nextPrice.getPrice())
						    newSubList.add(nextPrice);
					}else if(basePrice.getPrice() >=nextPrice.getPrice()){
						newSubList.add(nextPrice);
					}else{
						LOGGER.debug("scale price is less then promo price :"+basePrice.getSapId());
					}
				}
				erpPrices=(ErpMaterialPriceModel[])newSubList.toArray(new ErpMaterialPriceModel[0]);
				prices = new MaterialPrice[ erpPrices.length ];
			}								 												
			
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
				if (DEBUG) LOGGER.debug("Adding new MaterialPrice w/ scale ["+ p.getPrice() +","+ p.getPricingUnit() +","+ lower +","+ upper +","+ scaleUnit +","+p.getPromoPrice()+"]");
				prices[i] = new MaterialPrice(p.getPrice(), p.getPricingUnit(), lower, upper, scaleUnit, p.getPromoPrice());
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