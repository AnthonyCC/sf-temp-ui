package com.freshdirect.transadmin.model;

public class ZoneWorkTableForm {
 
	private String zoneWorkTable;
	private String type;
	private String deliveryFee;
	private String environment;

	public String getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(String deliveryFee) {
		//if(this.deliveryFee==null){
			this.deliveryFee = deliveryFee;
		//}
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setZoneWorkTable(String zoneWorkTable) {
		this.zoneWorkTable = zoneWorkTable;
	}

	public String getZoneWorkTable() {
		return zoneWorkTable;
	}
	
 
}
