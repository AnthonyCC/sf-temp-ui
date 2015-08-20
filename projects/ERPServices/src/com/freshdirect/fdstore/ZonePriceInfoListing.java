package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.common.pricing.ZoneInfo;

public class ZonePriceInfoListing implements Serializable, Cloneable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    //zoneId -> ZonePriceModel
	Map<ZoneInfo, ZonePriceInfoModel> zonePriceInfoMap = new HashMap<ZoneInfo, ZonePriceInfoModel>();
	 
	public ZonePriceInfoListing() {
		
	}

	public void reloadZonePrices(Map<ZoneInfo, ZonePriceInfoModel> zonePriceMap) {
		this.zonePriceInfoMap.clear();
		this.zonePriceInfoMap.putAll(zonePriceMap);
	}
	
	public void addZonePriceInfo(ZoneInfo zoneInfo, ZonePriceInfoModel zonePrice){
		zonePriceInfoMap.put(zoneInfo, zonePrice);
	}
	
	public ZonePriceInfoModel getZonePriceInfo(ZoneInfo zoneInfo) {
		return zonePriceInfoMap.get(zoneInfo);
	}
	
	public Collection<ZonePriceInfoModel> getZonePriceInfos() {
		return zonePriceInfoMap.values();
	}
	
	public int size() {
		return zonePriceInfoMap.size();
	}
	
	public static ZonePriceInfoListing getDummy() {
		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.99, 1.99, "LB", null, false, 0, 0, ZonePriceListing.DEFAULT_ZONE_INFO,false);
		dummyList.addZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO, dummy);
		return dummyList;
	}
	
	@Override
	public ZonePriceInfoListing clone() {
	    ZonePriceInfoListing z = new ZonePriceInfoListing ();
	    for (Map.Entry<ZoneInfo, ZonePriceInfoModel> e : zonePriceInfoMap.entrySet()) {
	        z.addZonePriceInfo(e.getKey(), e.getValue().clone());
	    }
	    return z;
	}

	@Override
    public String toString() {
        return "ZonePriceInfoListing[" + zonePriceInfoMap + ']';
    }
}
