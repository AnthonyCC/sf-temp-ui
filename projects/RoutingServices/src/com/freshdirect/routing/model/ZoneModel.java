package com.freshdirect.routing.model;

public class ZoneModel extends BaseModel implements IZoneModel {
	
	private String zoneId;

	private String zoneNumber;

	private String neighborhood;
		
	private String routingFlag;

	private String zoneType;
	
	private String area;

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getRoutingFlag() {
		return routingFlag;
	}

	public void setRoutingFlag(String routingFlag) {
		this.routingFlag = routingFlag;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneNumber() {
		return zoneNumber;
	}

	public void setZoneNumber(String zoneNumber) {
		this.zoneNumber = zoneNumber;
	}

	public String getZoneType() {
		return zoneType;
	}

	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}
}
