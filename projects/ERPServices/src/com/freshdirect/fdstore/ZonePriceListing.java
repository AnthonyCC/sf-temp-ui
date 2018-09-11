package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.pricing.ZoneInfo;

public class ZonePriceListing implements Serializable {
	
	public static final String DEFAULT_SALES_ORG = FDStoreProperties.getDefaultFdSalesOrg();
	public static final String DEFAULT_FDX_SALES_ORG = FDStoreProperties.getDefaultFdxSalesOrg();
	public static final String DEFAULT_DIST_CHANNEL = FDStoreProperties.getDefaultFdDistributionChannel();
	public static final String MASTER_DEFAULT_ZONE = ErpServicesProperties.getMasterDefaultZoneId();
	public static final String RESIDENTIAL_DEFAULT_ZONE = "0000100001";
	public static final String CORPORATE_DEFAULT_ZONE = "0000100002";
	
	public static final ZoneInfo DEFAULT_ZONE_INFO = new ZoneInfo(MASTER_DEFAULT_ZONE,DEFAULT_SALES_ORG,DEFAULT_DIST_CHANNEL);
	public static final ZoneInfo DEFAULT_FDX_ZONE_INFO = new ZoneInfo(MASTER_DEFAULT_ZONE,DEFAULT_FDX_SALES_ORG,DEFAULT_DIST_CHANNEL);
	
	
	//zoneId -> ZonePriceModel
	Map<ZoneInfo, ZonePriceModel> zonePriceMap = new HashMap<ZoneInfo, ZonePriceModel>();
	 
	public ZonePriceListing() {
		
	}

	public void reloadZonePrices(Map<ZoneInfo, ZonePriceModel> zonePriceMap) {
		this.zonePriceMap.clear();
		this.zonePriceMap.putAll(zonePriceMap);
	}
	
	public void addZonePrice(ZoneInfo zoneInfo, ZonePriceModel zonePrice){
		zonePriceMap.put(zoneInfo, zonePrice);
	}
	
	public ZonePriceListing addZonePrice(ZonePriceModel zonePrice) {
	   // zonePriceMap.put(zonePrice.getSapZoneId().intern(), zonePrice);
		zonePriceMap.put(zonePrice.getPricingZone(), zonePrice);
	    return this;
	}
	
	public ZonePriceModel getZonePrice(ZoneInfo zoneInfo) {
		return zonePriceMap.get(zoneInfo);
	}
	
	public Collection<ZonePriceModel> getZonePrices() {
		return zonePriceMap.values();
	}
	
	public int size() {
		return zonePriceMap.size();
	}
	
}
