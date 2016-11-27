package com.freshdirect.transadmin.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class DlvBuildingDetail implements java.io.Serializable, TrnBaseEntityI  {
	
	
	private DlvBuilding building;
	private String dlvBuildingDtlId;	
	private String addrType;
	private String companyName;
	
	
	private String svcValidate;
	private String svcScrubbedStreet;
	private String svcCrossStreet;
	private String svcCity;
	private String svcState;
	private String svcZip;
	
	private String doorman;
	private String walkup;
	private String elevator;
	private String svcEnt;
	private String house;
	private String freightElevator;
	
	private String handTruckAllowed;
	
	private Integer walkUpFloors;
	
	private String other;
	
	private String difficultReason;
	private String difficultToDeliver;
	private String additional;
	private String isNew;
	
	private String crossStreet;
	
	private Set operationHours = new HashSet(0);
	
	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	
	public String getDifficultToDeliver() {
		return difficultToDeliver;
	}

	public void setDifficultToDeliver(String difficultToDeliver) {
		this.difficultToDeliver = difficultToDeliver;
	}

	public String getDoorman() {
		return doorman;
	}

	public void setDoorman(String doorman) {
		this.doorman = doorman;
	}

	
	public String getDifficultReason() {
		return difficultReason;
	}
	public void setDifficultReason(String difficultReason) {
		this.difficultReason = difficultReason;
	}		
		
	public String getDlvBuildingId() {
		if(getBuilding() == null) {
			return null;
		}
		return getBuilding().getBuildingId();
	}
	public void setDlvBuildingId(String id) {
		if("null".equals(id)) {
			//setServiceTimeType(null);
		} else {
			DlvBuilding trnBuildingType = new DlvBuilding();
			trnBuildingType.setBuildingId(id);
			setBuilding(trnBuildingType);
		}
	}	
	
	public boolean isObsoleteEntity() {
		return false;
	}
	
	public DlvBuilding getBuilding() {
		return building;
	}
	public void setBuilding(DlvBuilding building) {
		this.building = building;
		this.setDlvBuildingDtlId(building.getBuildingId());		
	}
	public String getAddrType() {
		return addrType;
	}
	public void setAddrType(String addressType) {
		this.addrType = addressType;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSvcCity() {
		return svcCity;
	}

	public void setSvcCity(String svcCity) {
		this.svcCity = svcCity;
	}

	public String getSvcScrubbedStreet() {
		return svcScrubbedStreet;
	}

	public void setSvcScrubbedStreet(String svcScrubbedSt) {
		this.svcScrubbedStreet = svcScrubbedSt;
	}

	public String getSvcState() {
		return svcState;
	}

	public void setSvcState(String svcState) {
		this.svcState = svcState;
	}

	public String getSvcZip() {
		return svcZip;
	}

	public void setSvcZip(String svcZip) {
		this.svcZip = svcZip;
	}

	public String getSvcValidate() {
		return svcValidate;
	}

	public void setSvcValidate(String svcValidate) {
		this.svcValidate = svcValidate;
	}
	
	
	
	public String getElevator() {
		return elevator;
	}

	public void setElevator(String elevator) {
		this.elevator = elevator;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getSvcEnt() {
		return svcEnt;
	}

	public void setSvcEnt(String svcEnt) {
		this.svcEnt = svcEnt;
	}

	public String getWalkup() {
		return walkup;
	}

	public void setWalkup(String walkup) {
		this.walkup = walkup;
	}

	public String getFreightElevator() {
		return freightElevator;
	}

	public void setFreightElevator(String freightElevator) {
		this.freightElevator = freightElevator;
	}

	
	public String getHandTruckAllowed() {
		return handTruckAllowed;
	}

	public void setHandTruckAllowed(String handTruckAllowed) {
		this.handTruckAllowed = handTruckAllowed;
	}

	public Integer getWalkUpFloors() {
		return walkUpFloors;
	}

	public void setWalkUpFloors(Integer walkUpFloors) {
		this.walkUpFloors = walkUpFloors;
	}


	public String getDlvBuildingDtlId() {
		return dlvBuildingDtlId;
	}

	public void setDlvBuildingDtlId(String dlvBuildingDtlId) {
		this.dlvBuildingDtlId = dlvBuildingDtlId;
	}

	public String getSvcCrossStreet() {
		return svcCrossStreet;
	}

	public void setSvcCrossStreet(String svcCrossStreet) {
		this.svcCrossStreet = svcCrossStreet;
	}

	public String getCrossStreet() {
		return crossStreet;
	}

	public void setCrossStreet(String crossStreet) {
		this.crossStreet = crossStreet;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	public String getAdditional() {
		return additional;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getOther() {
		return other;
	}

	public Set getOperationHours() {
		return this.operationHours;
	}

	public void setOperationHours(Set operationHours) {
		this.operationHours = operationHours;
	}
		
	public boolean addOperationHours(BuildingOperationHours opHours) {
		return this.operationHours.add(opHours);
	}
	
	
	
	


}
