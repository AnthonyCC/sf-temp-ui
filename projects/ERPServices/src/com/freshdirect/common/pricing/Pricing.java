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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.framework.util.StringUtil;

/**
 * Aggregates all relevant information for performing pricing.
 *
 * @version $Revision$
 * @author $Author$
 */
public class Pricing implements Serializable {

	//private MaterialPrice[] materialPrices;
	private ZonePriceListing zonePriceList;
	private CharacteristicValuePrice[] cvPrices;
	private SalesUnitRatio[] salesUnits;
	private boolean isWineOrSpirit;

	/**
	 * @param materialPrices array of material pricing conditions
	 * @param cvPrices array of characteristic value pricing conditions
	 * @param salesUnits array of sales unit ratios
	 */
	public Pricing(ZonePriceListing zonePriceList, CharacteristicValuePrice[] cvPrices, SalesUnitRatio[] salesUnits, boolean isWineOrSpirit) {
		this.zonePriceList=zonePriceList;
		this.cvPrices=cvPrices;
		this.salesUnits=salesUnits;
		this.isWineOrSpirit=isWineOrSpirit;
	}

	/**
	 * Get all characteristic value pricing conditions.
	 *
	 * @return array of CharacteristicValuePrice objects
	 */
	public CharacteristicValuePrice[] getCharacteristicValuePrices(PricingContext pCtx) {
		if(this.cvPrices.length==0) return this.cvPrices;
		List<CharacteristicValuePrice> cvp=new ArrayList<CharacteristicValuePrice>(cvPrices.length);
		
		for(int i=0;i<cvPrices.length;i++) {
			if(pCtx!=null && pCtx.getZoneInfo()!=null && !StringUtil.isEmpty(pCtx.getZoneInfo().getSalesOrg()) && pCtx.getZoneInfo().getSalesOrg().equals(cvPrices[i].getSalesOrg()) && 
					!StringUtil.isEmpty(pCtx.getZoneInfo().getDistributionChanel()) && pCtx.getZoneInfo().getDistributionChanel().equals(cvPrices[i].getDistChannel()) )
						 cvp.add(cvPrices[i]);
		}
		CharacteristicValuePrice[] list2 = new CharacteristicValuePrice[cvp.size()];
		return cvp.toArray(list2);
	}

	/**
	 * Get all sales unit ratios.
	 *
	 * @return array of SalesUnitRatio objects
	 */
	public SalesUnitRatio[] getSalesUnitRatios() {
		return this.salesUnits;
	}
		

	/**
	 * Get matching sales unit ratio.
	 *
	 * @return null if not found
	 */
	public SalesUnitRatio findSalesUnitRatio(String salesUnit) {
		for (int i=0; i<this.salesUnits.length; i++) {
			if (salesUnit.equals( this.salesUnits[i].getAlternateUnit() )) {
				return this.salesUnits[i];
			}
		}
		return null;
	}

	/**
	 * Get matching char.value pricing condition.
	 * 
	 * @return null if not found
	 */
	public CharacteristicValuePrice findCharacteristicValuePrice(String characteristic, String charValue, PricingContext pCtx) {
		if (characteristic==null || charValue==null) {
			throw new IllegalArgumentException("Null characteristic or charValue: "+characteristic+"/"+charValue);
		}
		CharacteristicValuePrice cvp;
		for (int i=0; i<this.cvPrices.length; i++) {
			cvp=this.cvPrices[i];
			if ( characteristic.equals(cvp.getCharacteristicName()) && charValue.equals(cvp.getCharValueName()) ) {
				
				if(pCtx!=null && pCtx.getZoneInfo()!=null && !StringUtil.isEmpty(pCtx.getZoneInfo().getSalesOrg()) && pCtx.getZoneInfo().getSalesOrg().equals(cvp.getSalesOrg()) && 
					!StringUtil.isEmpty(pCtx.getZoneInfo().getDistributionChanel()) && pCtx.getZoneInfo().getDistributionChanel().equals(cvp.getDistChannel()) )
						return cvp;
			}
		}
		return null;
	}
	
	public CharacteristicValuePrice findCharacteristicValuePrice(String characteristic, String charValue) {
		if (characteristic==null || charValue==null) {
			throw new IllegalArgumentException("Null characteristic or charValue: "+characteristic+"/"+charValue);
		}
		CharacteristicValuePrice cvp;
		for (int i=0; i<this.cvPrices.length; i++) {
			cvp=this.cvPrices[i];
			if ( characteristic.equals(cvp.getCharacteristicName()) && charValue.equals(cvp.getCharValueName()) ) {
				
				
						return cvp;
			}
		}
		return null;
	}
	

	public String toString() {
		StringBuffer buf=new StringBuffer("Pricing[");
		for (int i=0; i<salesUnits.length; i++) {
			buf.append("\n\t").append(salesUnits[i].toString());
		}
		Iterator<ZonePriceModel> it = zonePriceList.getZonePrices().iterator();
		while(it.hasNext()){
			ZonePriceModel zpModel = (ZonePriceModel) it.next();
			buf.append("\n\t").append("ZoneInfo:").append(zpModel.getPricingZone());;
			MaterialPrice[] materialPrices = zpModel.getMaterialPrices();
			for (int i=0; i<materialPrices.length; i++) {
				buf.append("\n\t").append(materialPrices[i].toString());
			}
		}
		for (int i=0; i<cvPrices.length; i++) {
			buf.append("\n\t").append(cvPrices[i].toString());
		}
		buf.append("\n]");
		return buf.toString();
	}


	/**
	 * The new cascading logic goes here.
	 * @param zoneInfo
	 * @return
	 */
	private ZonePriceModel _getZonePrice(ZoneInfo pricingZoneInfo) {
		try {
		
			ZonePriceModel zpModel = this.zonePriceList.getZonePrice((pricingZoneInfo!=null && pricingZoneInfo.hasParentZone())?new ZoneInfo(pricingZoneInfo.getPricingZoneId(),pricingZoneInfo.getSalesOrg(),pricingZoneInfo.getDistributionChanel()):pricingZoneInfo);
			if(zpModel == null) {
				//::FDX:do a item cascading to its parent until we find a price info.
				ErpZoneMasterInfo zoneInfo = FDCachedFactory.getZoneInfo(pricingZoneInfo!=null?pricingZoneInfo.getPricingZoneId():null);
				if(zoneInfo!=null && zoneInfo.getParentZone()!=null)
					zpModel = _getZonePrice(new ZoneInfo(zoneInfo.getParentZone().getSapId(),pricingZoneInfo!=null?pricingZoneInfo.getSalesOrg():null,pricingZoneInfo!=null?pricingZoneInfo.getDistributionChanel():null));
			}
			return zpModel;
		}
		catch(FDResourceException fe){
			throw new FDRuntimeException(fe, "Unexcepted error happened while fetching the Zone Price Model");
		}
	}
	
	public ZonePriceListing getZonePriceList(){
		return this.zonePriceList;
	}
	
	
	public ZonePriceModel getZonePrice(ZoneInfo pricingZoneInfo) {
		ZoneInfo zone=pricingZoneInfo;
		ZonePriceModel zpModel=_getZonePrice(zone);
		while(zone!=null && zpModel==null && zone.hasParentZone()) {
			zone=zone.getParentZone();
			zpModel=_getZonePrice(zone);
		}
		try {
			if(!pricingZoneInfo.getSalesOrg().equals(zpModel.getPricingZone().getSalesOrg()) && ZoneInfo.PricingIndicator.BASE.equals(pricingZoneInfo.getPricingIndicator()) && !isWineOrSpirit) {
				MaterialPrice[] baseIndicatorMaterialPrice=getBaseIndicatorMaterialPrice(zpModel.getMaterialPrices());
				return new ZonePriceModel(zpModel.getPricingZone(),baseIndicatorMaterialPrice);
			}else 
				return zpModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return zpModel;
	}
	
	private MaterialPrice[] getBaseIndicatorMaterialPrice(MaterialPrice[] matPrice) {
		
		List<MaterialPrice> list = new ArrayList<MaterialPrice>();
		MaterialPrice mp=null;
		for (int i=0;i<matPrice.length;i++) {
			mp=matPrice[i];
//			baseIndicatorMaterialPrice[i]=new MaterialPrice(mp.getOriginalPrice(),mp.getPricingUnit(),mp.getScaleLowerBound(),mp.getScaleUpperBound(),mp.getScaleUnit(),0);
			if(null !=mp && mp.getScaleLowerBound() <= 0){
				list.add(new MaterialPrice(mp.getOriginalPrice(),mp.getPricingUnit(),mp.getScaleLowerBound(),Double.POSITIVE_INFINITY,mp.getScaleUnit(),0));
			}
		}
		MaterialPrice[] baseIndicatorMaterialPrice=new MaterialPrice[list.size()];
		return list.toArray(baseIndicatorMaterialPrice);
		
	}

}

