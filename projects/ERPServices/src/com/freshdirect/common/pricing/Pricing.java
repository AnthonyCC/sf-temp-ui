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
import java.util.Iterator;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.ZonePriceModel;

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

	/**
	 * @param materialPrices array of material pricing conditions
	 * @param cvPrices array of characteristic value pricing conditions
	 * @param salesUnits array of sales unit ratios
	 */
	public Pricing(ZonePriceListing zonePriceList, CharacteristicValuePrice[] cvPrices, SalesUnitRatio[] salesUnits) {
		this.zonePriceList=zonePriceList;
		this.cvPrices=cvPrices;
		this.salesUnits=salesUnits;
	}

	/**
	 * Get all characteristic value pricing conditions.
	 *
	 * @return array of CharacteristicValuePrice objects
	 */
	public CharacteristicValuePrice[] getCharacteristicValuePrices() {
		return this.cvPrices;
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
			buf.append("\n\t").append("Zone ID:").append(zpModel.getSapZoneId());;
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


	
	public ZonePriceModel getZonePrice(String pZoneId) {
		try {
			ZonePriceModel zpModel = this.zonePriceList.getZonePrice(pZoneId);
			if(zpModel == null) {
				//do a item cascading to its parent until we find a price info.
				ErpZoneMasterInfo zoneInfo = FDCachedFactory.getZoneInfo(pZoneId);
				zpModel = getZonePrice(zoneInfo.getParentZone().getSapId());
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
}
