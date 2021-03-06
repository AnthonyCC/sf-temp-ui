package com.freshdirect.transadmin.model;

//Generated Dec 5, 2008 2:34:33 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Dispatch generated by hbm2java
 */
public class Dispatch implements java.io.Serializable{

	private String dispatchId;
	private Zone zone;
	private Region region;
	private DispositionType dispositionType;
	private Date dispatchDate;
	private String supervisorId;
	private String route;
	private String truck;
	private String physicalTruck;
	private Date startTime;
	private Date endTime;
	private Date dispatchGroup;
	private Date firstDlvTime;
	private Boolean confirmed;
	private String planId;
	private String comments;	
	private Boolean bullPen;
	private Set dispatchResources = new HashSet(0);
	private String userId;
	private String gpsNumber;
	private String ezpassNumber;
	private Date dispatchTime;
	private Boolean phonesAssigned;
	private Boolean keysReady;
	private Date checkedInTime;
	private Boolean keysIn;
	
	private Boolean isOverride;
	private Boolean isTeamOverride;
	private DispatchReason overrideReason;
	private String overrideUser;
	
	private String motKitNumber;
	private String additionalNextels;
	private TrnFacility originFacility;
	private TrnFacility destinationFacility;	
	private String dispatchType;
	private Double muniMeterValueAssigned;
	private Double muniMeterValueReturned;
	private String muniMeterCardNotAssigned;
	private String muniMeterCardNotReturned;
	
	
	/* Temp variable to fill START_TIME column on TRANSP.DISPATCH */
	private Date dispatchTimeEx;
	
	public TrnFacility getOriginFacility() {
		return originFacility;
	}

	public void setOriginFacility(TrnFacility originFacility) {
		this.originFacility = originFacility;
	}

	public TrnFacility getDestinationFacility() {
		return destinationFacility;
	}

	public void setDestinationFacility(TrnFacility destinationFacility) {
		this.destinationFacility = destinationFacility;
	}
	
	public Dispatch() {
	}

	public Dispatch(String dispatchId, Zone zone, Date dispatchDate,
			Date startTime) {
		this.dispatchId = dispatchId;
		this.zone = zone;
		this.dispatchDate = dispatchDate;
		this.startTime = startTime;
	}

	public Dispatch(String dispatchId, Zone zone,
			DispositionType dispositionType, Date dispatchDate,
			String supervisorId, String route, String truck, Date startTime,
			Date dispatchGroup, Boolean confirmed, String planId,
			String comments, Set dispatchResources) {
		this.dispatchId = dispatchId;
		this.zone = zone;
		this.dispositionType = dispositionType;
		this.dispatchDate = dispatchDate;
		this.supervisorId = supervisorId;
		this.route = route;
		this.truck = truck;
		this.startTime = startTime;
		this.dispatchGroup = dispatchGroup;
		this.confirmed = confirmed;
		this.planId = planId;
		this.comments = comments;
		this.dispatchResources = dispatchResources;
	}

	public String getDispatchId() {
		return this.dispatchId;
	}

	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}

	public Zone getZone() {
		return this.zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public DispositionType getDispositionType() {
		return this.dispositionType;
	}

	public void setDispositionType(DispositionType dispositionType) {
		this.dispositionType = dispositionType;
	}

	public Date getDispatchDate() {
		return this.dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public String getSupervisorId() {
		return this.supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public String getRoute() {
		return this.route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getTruck() {
		return this.truck;
	}

	public void setTruck(String truck) {
		this.truck = truck;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getDispatchGroup() {
		return dispatchGroup;
	}

	public void setDispatchGroup(Date dispatchGroup) {
		this.dispatchGroup = dispatchGroup;
	}
	
	public Date getFirstDlvTime() {
		return firstDlvTime;
	}

	public void setFirstDlvTime(Date firstDlvTime) {
		this.firstDlvTime = firstDlvTime;
	}

	public Boolean getConfirmed() {
		return this.confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Set getDispatchResources() {
		return this.dispatchResources;
	}

	public void setDispatchResources(Set dispatchResources) {
		this.dispatchResources = dispatchResources;
	}
		
	public boolean equals(Object o){
       if(o instanceof Dispatch){
    	   Dispatch d=(Dispatch)o;
    	   if(this.dispatchId.equalsIgnoreCase(d.getDispatchId())){
    		   return true;
    	   }
       }
       return false;
	}

	public Boolean getBullPen() {
		return bullPen;
	}

	public void setBullPen(Boolean isBullPen) {
		this.bullPen = isBullPen;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEzpassNumber() {
		return ezpassNumber;
	}

	public void setEzpassNumber(String ezpassNumber) {
		this.ezpassNumber = ezpassNumber;
	}

	public String getGpsNumber() {
		return gpsNumber;
	}

	public void setGpsNumber(String gpsNumber) {
		this.gpsNumber = gpsNumber;
	}

	public Date getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public Boolean getKeysReady() {
		return keysReady;
	}

	public void setKeysReady(Boolean keysReady) {
		this.keysReady = keysReady;
	}

	public Boolean getPhonesAssigned() {
		return phonesAssigned;
	}

	public void setPhonesAssigned(Boolean phonesAssigned) {
		this.phonesAssigned = phonesAssigned;
	}

	public Date getCheckedInTime() {
		return checkedInTime;
	}

	public void setCheckedInTime(Date checkedInTime) {
		this.checkedInTime = checkedInTime;
	}

	public Boolean getIsOverride() {
		return isOverride;
	}

	public void setIsOverride(Boolean isOverride) {
		this.isOverride = isOverride;
	}

	public DispatchReason getOverrideReason() {
		return overrideReason;
	}

	public void setOverrideReason(DispatchReason overrideReason) {
		this.overrideReason = overrideReason;
	}

	public String getOverrideUser() {
		return overrideUser;
	}

	public void setOverrideUser(String overrideUser) {
		this.overrideUser = overrideUser;
	}

	public Boolean getIsTeamOverride() {
		return isTeamOverride;
	}

	public void setIsTeamOverride(Boolean isTeamOverride) {
		this.isTeamOverride = isTeamOverride;
	}

	public String getMotKitNumber() {
		return motKitNumber;
	}

	public String getAdditionalNextels() {
		return additionalNextels;
	}

	public void setMotKitNumber(String motKitNumber) {
		this.motKitNumber = motKitNumber;
	}

	public void setAdditionalNextels(String additionalNextels) {
		this.additionalNextels = additionalNextels;
	}
	
	public String getPhysicalTruck() {
		return physicalTruck;
	}

	public void setPhysicalTruck(String physicalTruck) {
		this.physicalTruck = physicalTruck;
	}
	public String getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}

	public Boolean getKeysIn() {
		return keysIn;
	}

	public void setKeysIn(Boolean keysIn) {
		this.keysIn = keysIn;
	}

	public Date getDispatchTimeEx() {
		return dispatchTimeEx;
	}

	public void setDispatchTimeEx(Date dispatchTimeEx) {
		this.dispatchTimeEx = dispatchTimeEx;
	}

	public Double getMuniMeterValueAssigned() {
		return muniMeterValueAssigned;
	}

	public void setMuniMeterValueAssigned(Double muniMeterValueAssigned) {
		this.muniMeterValueAssigned = muniMeterValueAssigned;
	}

	public Double getMuniMeterValueReturned() {
		return muniMeterValueReturned;
	}

	public void setMuniMeterValueReturned(Double muniMeterValueReturned) {
		this.muniMeterValueReturned = muniMeterValueReturned;
	}

	public String getMuniMeterCardNotAssigned() {
		return muniMeterCardNotAssigned;
	}

	public void setMuniMeterCardNotAssigned(String muniMeterCardNotAssigned) {
		this.muniMeterCardNotAssigned = muniMeterCardNotAssigned;
	}

	public String getMuniMeterCardNotReturned() {
		return muniMeterCardNotReturned;
	}

	public void setMuniMeterCardNotReturned(String muniMeterCardNotReturned) {
		this.muniMeterCardNotReturned = muniMeterCardNotReturned;
	}
}
