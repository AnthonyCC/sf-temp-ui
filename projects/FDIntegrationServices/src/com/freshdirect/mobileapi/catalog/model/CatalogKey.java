package com.freshdirect.mobileapi.catalog.model;

import com.freshdirect.common.pricing.*;

public class CatalogKey {
	private static final String DELIMITER = "-";
	private String eStore;
	private long plantId;
	private ZoneInfo pricingZone;
	public String geteStore() {
		return eStore;
	}
	public void seteStore(String eStore) {
		this.eStore = eStore;
	}
	public long getPlantId() {
		return plantId;
	}
	public void setPlantId(long plantId) {
		this.plantId = plantId;
	}
	public ZoneInfo getPricingZone() {
		return pricingZone;
	}
	public void setPricingZone(ZoneInfo pricingZone) {
		this.pricingZone = pricingZone;
	}
	
	@Override
	public String toString()
	{
		return eStore+DELIMITER+plantId +DELIMITER+ pricingZone.getSalesOrg()
				+DELIMITER+pricingZone.getDistributionChanel()+DELIMITER+pricingZone.getPricingZoneId();
	}
	
	public static CatalogKey parse(String stringRep){
		CatalogKey key = new CatalogKey();
		
		String[] splitString = stringRep.split(DELIMITER);
		key.seteStore(splitString[0]);
		try{
			key.setPlantId(Long.parseLong(splitString[1]));
		} catch(Exception ignored){
			key.setPlantId(0);
		}
		
		ZoneInfo pz = new ZoneInfo(splitString[4], splitString[2], splitString[3]);
		
		key.setPricingZone(pz);
		
		return key;
	}
}
