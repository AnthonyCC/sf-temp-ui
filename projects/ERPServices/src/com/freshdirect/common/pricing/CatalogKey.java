package com.freshdirect.common.pricing;

import java.io.Serializable;
import java.util.Arrays;

public class CatalogKey implements Serializable{
	private static final String DELIMITER = "-";
	private String eStore;
	private long plantId;
	private ZoneInfo pricingZone;
	
	public CatalogKey(String eStore, long plantId, ZoneInfo pricingZone) {
		super();
		this.eStore = eStore;
		this.plantId = plantId;
		this.pricingZone = pricingZone;
	}
	public CatalogKey() {
		super();
		// TODO Auto-generated constructor stub
	}
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
		return eStore+DELIMITER+plantId+DELIMITER+pricingZone.stringWithDelimter(DELIMITER);
	}
	
	public static CatalogKey parse(String stringRep){
		CatalogKey key = new CatalogKey();
		
		if(null !=stringRep && !"".equalsIgnoreCase(stringRep.trim())){
			String[] splitString = stringRep.split(DELIMITER);
			key.seteStore(splitString[0]);
			try{
				key.setPlantId(Long.parseLong(splitString[1]));
			} catch(Exception ignored){
				key.setPlantId(0);
			}
			ZoneInfo pz = ZoneInfo.distributeIntoObject(Arrays.copyOfRange(splitString, 2, splitString.length ));
	//		ZoneInfo pz = new ZoneInfo(splitString[4], splitString[2], splitString[3]);
			
			key.setPricingZone(pz);
		}
		return key;
	}
}
