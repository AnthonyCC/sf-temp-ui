package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrpZonePriceListing implements Serializable {
	
	//zoneId -> GrpZonePriceModel
	Map<String, GrpZonePriceModel> grpZonePriceMap = new HashMap<String, GrpZonePriceModel>();
	 
	public GrpZonePriceListing() {
		
	}

	public void reloadZonePrices(Map zonePriceMap) {
		this.grpZonePriceMap.clear();
		this.grpZonePriceMap.putAll(zonePriceMap);
	}
	
	public void addGrpZonePrice(String zoneId, GrpZonePriceModel zonePrice){
		grpZonePriceMap.put(zoneId, zonePrice);
	}
	
	public GrpZonePriceListing addGrpZonePrice(GrpZonePriceModel zonePrice) {
	    grpZonePriceMap.put(zonePrice.getSapZoneId(), zonePrice);
	    return this;
	}
	
	public GrpZonePriceModel getGrpZonePrice(String zoneId) {
		return grpZonePriceMap.get(zoneId);
	}
	
	public Collection<GrpZonePriceModel> getGrpZonePrices() {
		return grpZonePriceMap.values();
	}
	
	public int size() {
		return grpZonePriceMap.size();
	}
	
}
