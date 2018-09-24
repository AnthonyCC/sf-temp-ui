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
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpGrpPriceZoneModel;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.GrpZonePriceListing;
import com.freshdirect.fdstore.GrpZonePriceModel;
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
	
	public static final Comparator<ErpMaterialPriceModel> ERP_MAT_PRICE_MODEL_COMPARATOR  = new Comparator<ErpMaterialPriceModel>() {
		public int compare(ErpMaterialPriceModel mp1, ErpMaterialPriceModel mp2) {
			
			int _comp=mp1.getSalesOrg().compareTo(mp2.getSalesOrg()); 
		       if(_comp!=0)
		    	   return _comp;
		       
		       _comp=mp1.getDistChannel().compareTo(mp2.getDistChannel());
		       if(_comp!=0)
		    	   return _comp;
			return mp1.getSapZoneId().compareTo(mp2.getSapZoneId());
		}
	};

	public static final Comparator<ErpMaterialPrice> ERP_MAT_PRICE_COMPARATOR  = new Comparator<ErpMaterialPrice>() {
		public int compare(ErpMaterialPrice mp1, ErpMaterialPrice mp2) {
			int _comp=mp1.getSalesOrg().compareTo(mp2.getSalesOrg()); 
		       if(_comp!=0)
		    	   return _comp;
		       
		       _comp=mp1.getDistChannel().compareTo(mp2.getDistChannel());
		       if(_comp!=0)
		    	   return _comp;
			return mp1.getSapZoneId().compareTo(mp2.getSapZoneId());
		}
	};
	
    public static final Comparator<ErpMaterialPrice> SCALE_QUANTITY_COMPARATOR = new Comparator<ErpMaterialPrice>() {
        public int compare(ErpMaterialPrice mp1, ErpMaterialPrice mp2) {
            return new Double(mp1.getScaleQuantity()).compareTo(new Double(mp2.getScaleQuantity()));
        }
    };

    static final Comparator<ErpMaterialPriceModel> MODEL_SCALE_QUANTITY_COMPARATOR = new Comparator<ErpMaterialPriceModel>() {
        public int compare(ErpMaterialPriceModel mp1, ErpMaterialPriceModel mp2) {
            return new Double(mp1.getScaleQuantity()).compareTo(new Double(mp2.getScaleQuantity()));
        }
    };
    
    static final Comparator<ErpGrpPriceZoneModel> GRP_PRICE_ZONE_MODEL_QTY_COMPARATOR = new Comparator<ErpGrpPriceZoneModel>() {
        @Override
        public int compare(ErpGrpPriceZoneModel z1, ErpGrpPriceZoneModel z2) {
            Double d1 = new Double( z1.getQty() );
            Double d2 = new Double( z2.getQty() );
            return d1.compareTo(d2);
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
		//Collections.sort(materialPriceList, ERP_MAT_PRICE_MODEL_COMPARATOR);
		String sapZoneId = "";
		ZoneInfo zoneInfo=null;
		ZoneInfo currZoneInfo=null;
		List<ErpMaterialPriceModel> subList = new ArrayList<ErpMaterialPriceModel>();
		ZonePriceListing zonePriceList = new ZonePriceListing();
		MaterialPrice promoPrice = null;
		for(Iterator<ErpMaterialPriceModel> it=materialPriceList.iterator();it.hasNext();){
			ErpMaterialPriceModel erpMaterialPrice = (ErpMaterialPriceModel)it.next();
			
			//if(sapZoneId.length() == 0 || sapZoneId.equals(erpMaterialPrice.getSapZoneId())){
			currZoneInfo=new ZoneInfo(erpMaterialPrice.getSapZoneId(),"1000".equals(erpMaterialPrice.getSalesOrg())?FDStoreProperties.getDefaultFdSalesOrg():erpMaterialPrice.getSalesOrg(),"1000".equals(erpMaterialPrice.getDistChannel())?FDStoreProperties.getDefaultFdDistributionChannel():erpMaterialPrice.getDistChannel());
			if((zoneInfo==null)||zoneInfo.equals(currZoneInfo)){
					subList.add(erpMaterialPrice);
			}
			
			
			//else if(!sapZoneId.equals(erpMaterialPrice.getSapZoneId())) {
		     else if(!zoneInfo.equals(currZoneInfo)) {
				MaterialPrice[] materialPrices = buildMaterialPrices(
						(ErpMaterialPriceModel[]) subList.toArray(new ErpMaterialPriceModel[0]));
				ZonePriceModel zpModel = new ZonePriceModel(zoneInfo, materialPrices);
				zonePriceList.addZonePrice(zpModel);
				subList.clear();
				subList.add(erpMaterialPrice);
			}
			sapZoneId = erpMaterialPrice.getSapZoneId();
			zoneInfo=currZoneInfo;
			
		} 
		
		MaterialPrice[] materialPrices = buildMaterialPrices(
				(ErpMaterialPriceModel[]) subList.toArray(new ErpMaterialPriceModel[0]));
		ZonePriceModel zpModel = new ZonePriceModel(zoneInfo, materialPrices);
		zonePriceList.addZonePrice(zoneInfo, zpModel);
		subList.clear();

		// build characteristic value pricing conditions
		if (DEBUG) LOGGER.debug("Building CV pricing");
		CharacteristicValuePrice[] cvPrices = buildCVPrices( material.getCharacteristics(), charValuePrices );

		// build sales unit ratios
		if (DEBUG) LOGGER.debug("Building sales unit ratios");
		SalesUnitRatio[] salesUnitRatios = buildSalesUnitRatios( material.getSalesUnits() );

		return new Pricing(zonePriceList, cvPrices, salesUnitRatios,isWineOrSpirit(material.getAlcoholicContent()));
	}

	
	public static boolean isWineOrSpirit(EnumAlcoholicContent alcoholType) {
		
		return EnumAlcoholicContent.isWineOrSpirit(alcoholType);
		
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
			Arrays.sort(erpPrices, MODEL_SCALE_QUANTITY_COMPARATOR);

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
						LOGGER.debug("scale price is greater than promo price :"+basePrice.getSapId());
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
				applyHow,cvp.getSalesOrg(),cvp.getDistChannel() );
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

	public static GroupScalePricing convertToGroupScalePricing(ErpGrpPriceModel erpGrpPriceModel) {
		List<ErpGrpPriceZoneModel> allZonePriceList = erpGrpPriceModel.getOrderedZoneModelList();
		List<ErpGrpPriceZoneModel> zonePriceList = new ArrayList<ErpGrpPriceZoneModel>();
		ZoneInfo currZone = null;
		GrpZonePriceListing grpZonePriceListing = null;
		for(Iterator<ErpGrpPriceZoneModel> it=allZonePriceList.iterator() ;it.hasNext();){
			ErpGrpPriceZoneModel zonePriceModel = it.next();
	   	 	ZoneInfo sapZone=zonePriceModel.getZone();
		   	 if(currZone==null || currZone.equals(sapZone)){
		   		zonePriceList.add(zonePriceModel);
		   	 } else {
		   		 //Different Zone. Process the previous zone price list.
		   		ErpGrpPriceZoneModel[] erpGrpPrices=(ErpGrpPriceZoneModel[])zonePriceList.toArray(new ErpGrpPriceZoneModel[0]);
				// sort by scale quantity ascending
				Arrays.sort(erpGrpPrices, GRP_PRICE_ZONE_MODEL_QTY_COMPARATOR );
				//Current Implementation only supports single group scale price. so it always keep the first element
				ErpGrpPriceZoneModel[] finalGrpPrices = new ErpGrpPriceZoneModel[] {erpGrpPrices[0]};
		   		MaterialPrice[] prices = new MaterialPrice[ finalGrpPrices.length ];										
				double lower;
				double upper;
				for (int i=0; i<prices.length; i++) {
					ErpGrpPriceZoneModel p=finalGrpPrices[i];
					lower=p.getQty();
					if (i==prices.length-1) {
						// last one
						upper=Double.POSITIVE_INFINITY;
					} else {
						upper=finalGrpPrices[i+1].getQty();
					}
					if (DEBUG) LOGGER.debug("Adding new MaterialPrice w/ scale ["+ p.getPrice() +","+ p.getUnitOfMeasure() +","+ lower +","+ upper +","+ p.getScaleUnit()+"]");
					prices[i] = new MaterialPrice(p.getPrice(), p.getUnitOfMeasure(), lower, upper, p.getScaleUnit(), 0.0);																	
				}		
				GrpZonePriceModel grpZonePriceModel = new GrpZonePriceModel(currZone, prices);
				if(grpZonePriceListing == null)
					grpZonePriceListing = new GrpZonePriceListing();
				grpZonePriceListing.addGrpZonePrice(currZone, grpZonePriceModel);
				zonePriceList.clear();
				zonePriceList.add(zonePriceModel);
			}
			currZone = sapZone; 
		}
		//Process the Group Scale Pricing for last zone.
   		ErpGrpPriceZoneModel[] erpGrpPrices=(ErpGrpPriceZoneModel[])zonePriceList.toArray(new ErpGrpPriceZoneModel[0]);
		// sort by scale quantity ascending
		Arrays.sort(erpGrpPrices, GRP_PRICE_ZONE_MODEL_QTY_COMPARATOR);
		//Current Implementation only supports single group scale price. so it always keep the first element
		ErpGrpPriceZoneModel[] finalGrpPrices = new ErpGrpPriceZoneModel[] {erpGrpPrices[0]};
						   		
   		MaterialPrice[] prices = new MaterialPrice[ finalGrpPrices.length ];										
		double lower;
		double upper;
		for (int i=0; i<prices.length; i++) {
			ErpGrpPriceZoneModel p=finalGrpPrices[i];
			lower=p.getQty();
			if (i==prices.length-1) {
				// last one
				upper=Double.POSITIVE_INFINITY;
			} else {
				upper=finalGrpPrices[i+1].getQty();
			}
			if (DEBUG) LOGGER.debug("Adding new MaterialPrice w/ scale ["+ p.getPrice() +","+ p.getUnitOfMeasure() +","+ lower +","+ upper +","+ p.getScaleUnit()+"]");
			prices[i] = new MaterialPrice(p.getPrice(), p.getUnitOfMeasure(), lower, upper, p.getScaleUnit(), 0.0);																	
		}		
		GrpZonePriceModel grpZonePriceModel = new GrpZonePriceModel(currZone, prices);
		if(grpZonePriceListing == null)
			grpZonePriceListing = new GrpZonePriceListing();
		grpZonePriceListing.addGrpZonePrice(currZone, grpZonePriceModel);
		//zonePriceList.clear();
		
		GroupScalePricing pricing = new GroupScalePricing(erpGrpPriceModel.getGrpId(), erpGrpPriceModel.getVersion(), erpGrpPriceModel.getLongDesc(), 
				erpGrpPriceModel.getShortDesc(), erpGrpPriceModel.isActive(), grpZonePriceListing, erpGrpPriceModel.getMatList(), erpGrpPriceModel.getSkuList());
		return pricing;
	}
}