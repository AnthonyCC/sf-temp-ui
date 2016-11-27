package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.common.pricing.ZoneInfo;

public class GrpZonePriceListing implements Serializable {
	
	//zoneId -> GrpZonePriceModel
	Map<ZoneInfo, GrpZonePriceModel> grpZonePriceMap = new HashMap<ZoneInfo, GrpZonePriceModel>();
	 
	public GrpZonePriceListing() {
		
	}

	public void reloadZonePrices(Map<ZoneInfo, GrpZonePriceModel> zonePriceMap) {
		this.grpZonePriceMap.clear();
		this.grpZonePriceMap.putAll(zonePriceMap);
	}
	
	public void addGrpZonePrice(ZoneInfo zoneInfo, GrpZonePriceModel zonePrice){
		grpZonePriceMap.put(zoneInfo, zonePrice);
	}
	
	public GrpZonePriceListing addGrpZonePrice(GrpZonePriceModel zonePrice) {
	    grpZonePriceMap.put(zonePrice.getSapZone(), zonePrice);
	    return this;
	}
	
	public GrpZonePriceModel getGrpZonePrice(ZoneInfo zoneInfo) {
		return grpZonePriceMap.get(zoneInfo);
	}
	
	public Collection<GrpZonePriceModel> getGrpZonePrices() {
		return grpZonePriceMap.values();
	}
	
	public int size() {
		return grpZonePriceMap.size();
	}
	
}
