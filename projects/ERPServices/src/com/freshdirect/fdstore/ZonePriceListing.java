package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZonePriceListing implements Serializable {
	public static final String MASTER_DEFAULT_ZONE = "0000100000";
	public static final String RESIDENTIAL_DEFAULT_ZONE = "0000100001";
	public static final String CORPORATE_DEFAULT_ZONE = "0000100002";
	
	//zoneId -> ZonePriceModel
	Map<String, ZonePriceModel> zonePriceMap = new HashMap<String, ZonePriceModel>();
	 
	public ZonePriceListing() {
		
	}

	public void reloadZonePrices(Map zonePriceMap) {
		this.zonePriceMap.clear();
		this.zonePriceMap.putAll(zonePriceMap);
	}
	
	public void addZonePrice(String zoneId, ZonePriceModel zonePrice){
		zonePriceMap.put(zoneId.intern(), zonePrice);
	}
	
	public ZonePriceListing addZonePrice(ZonePriceModel zonePrice) {
	    zonePriceMap.put(zonePrice.getSapZoneId().intern(), zonePrice);
	    return this;
	}
	
	public ZonePriceModel getZonePrice(String zoneId) {
		return zonePriceMap.get(zoneId);
	}
	
	public Collection<ZonePriceModel> getZonePrices() {
		return zonePriceMap.values();
	}
	
	public int size() {
		return zonePriceMap.size();
	}
	
}
