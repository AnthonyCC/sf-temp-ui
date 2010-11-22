package com.freshdirect.routing.model;

public class ZoneModel extends BaseModel implements IZoneModel {
	
	private String zoneId;

	private String zoneNumber;

	private String neighborhood;
		
	private String routingFlag;

	private String zoneType;
	
	private IAreaModel area;
	
	private IServiceTimeTypeModel serviceTimeType;
	
	private int loadingPriority;

	public IAreaModel getArea() {
		return area;
	}

	public void setArea(IAreaModel area) {
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
	
	
	public IServiceTimeTypeModel getServiceTimeType() {
		return serviceTimeType;
	}

	public void setServiceTimeType(IServiceTimeTypeModel serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}

	public int getLoadingPriority() {
		return loadingPriority;
	}

	public void setLoadingPriority(int loadingPriority) {
		this.loadingPriority = loadingPriority;
	}

	public String toString() {
		return zoneId+"|"+zoneNumber+"|"+area;
	}
}
