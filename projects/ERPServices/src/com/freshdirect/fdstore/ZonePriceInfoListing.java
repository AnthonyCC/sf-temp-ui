package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZonePriceInfoListing implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = -4590703376366147655L;
    
    //zoneId -> ZonePriceModel
	Map<String, ZonePriceInfoModel> zonePriceInfoMap = new HashMap<String, ZonePriceInfoModel>();
	 
	public ZonePriceInfoListing() {
		
	}

	public void addZonePriceInfo(String zoneId, ZonePriceInfoModel zonePrice){
		zonePriceInfoMap.put(zoneId.intern(), zonePrice);
	}
	
	public ZonePriceInfoModel getZonePriceInfo(String zoneId) {
		return zonePriceInfoMap.get(zoneId);
	}
	
	public Collection<ZonePriceInfoModel> getZonePriceInfos() {
		return zonePriceInfoMap.values();
	}
	
	public int size() {
		return zonePriceInfoMap.size();
	}
	
	public static ZonePriceInfoListing getDummy() {
		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.99, 1.99, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE,false);
		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
		return dummyList;
	}
	
        @Override
        public String toString() {
            return "ZonePriceInfoListing[" + zonePriceInfoMap + ']';
        }
}
