package com.freshdirect.transadmin.model;

// Generated Nov 18, 2008 3:11:21 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.transadmin.util.EnumResourceType;

/**
 * Plan generated by hbm2java
 */
public class Plan implements java.io.Serializable, TrnBaseEntityI, IWaveInstanceSource  {

	private String planId;
	private Zone zone;
	private Region region;
	private Date planDate;
	private Date firstDeliveryTime;
	private Date startTime;
	private int sequence;
	private String isBullpen;
	private Set planResources = new HashSet(0);
	private String ignoreErrors;
	private Date errorDate;
	private String supervisorId;
	private String userId;
	private Date maxTime;
	private String isOpen;
	private Boolean isTeamOverride;
	
	private Date lastDeliveryTime;
	private Date cutOffTime;
	private TrnFacility originFacility;
	private TrnFacility destinationFacility;
	private Set waveResources = new HashSet(0);
	
	public Set getWaveResources() {
		return waveResources;
	}

	public void setWaveResources(Set waveResources) {
		this.waveResources = waveResources;
	}

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

	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public Plan() {
	}
	
	public Date getErrorDate() {
		return errorDate;
	}
	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}
	public String getIgnoreErrors() {
		return ignoreErrors;
	}
	public void setIgnoreErrors(String ignoreErrors) {
		this.ignoreErrors = ignoreErrors;
	}

	

	public Plan(String planId, Region region, Date planDate, Date firstDeliveryTime,
			Date startTime, int sequence) {
		this.planId = planId;
		this.region = region;
		this.planDate = planDate;
		this.firstDeliveryTime = firstDeliveryTime;
		this.startTime = startTime;
		this.sequence = sequence;
	}

	public Plan(String planId, Zone zone, Region region, Date planDate,
			Date firstDeliveryTime, Date startTime, int sequence, String isBullpen,
			Set planResources) {
		this.planId = planId;
		this.zone = zone;
		this.region = region;
		this.planDate = planDate;
		this.firstDeliveryTime = firstDeliveryTime;
		this.startTime = startTime;
		this.sequence = sequence;
		this.isBullpen = isBullpen;
		this.planResources = planResources;
	}

	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Zone getZone() {
		return this.zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Date getPlanDate() {
		return this.planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public Date getFirstDeliveryTime() {
		return this.firstDeliveryTime;
	}

	public void setFirstDeliveryTime(Date firstDeliveryTime) {
		this.firstDeliveryTime = firstDeliveryTime;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getSequence() {
		return this.sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getIsBullpen() {
		return this.isBullpen;
	}

	public void setIsBullpen(String isBullpen) {
		this.isBullpen = isBullpen;
	}

	public Set getPlanResources() {
		return this.planResources;
	}

	public void setPlanResources(Set planResources) {
		this.planResources = planResources;
	}

	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}
	public String getZoneCode() {
		if(getZone() == null) {
			return null;
		}
		return getZone().getZoneCode();
	}

	public void setZoneCode(String trnzoneId) {
		if("null".equals(trnzoneId)) {
			setZone(null);
		} else {
			Zone trnZone = new Zone();
			trnZone.setZoneCode(trnzoneId);
			setZone(trnZone);
		}
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}

	public String getOpen() {
		return isOpen;
	}

	public void setOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public Boolean getIsTeamOverride() {
		return isTeamOverride;
	}

	public void setIsTeamOverride(Boolean isTeamOverride) {
		this.isTeamOverride = isTeamOverride;
	}

	public Date getLastDeliveryTime() {
		return lastDeliveryTime;
	}

	public Date getCutOffTime() {
		return cutOffTime;
	}

	public void setLastDeliveryTime(Date lastDeliveryTime) {
		this.lastDeliveryTime = lastDeliveryTime;
	}

	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}

	@Override
	public Date getDeliveryDate() {
		// TODO Auto-generated method stub
		return this.getPlanDate();
	}

	@Override
	public int getNoOfResources() {
		// TODO Auto-generated method stub
		return this.getZone() != null && this.getZone().getArea() != null 
				&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot()) ? getNoOfRunners() : 1 ;
	}
	
	public int getNoOfResources1() {
		// TODO Auto-generated method stub
		return this.getZone() != null && this.getZone().getArea() != null 
				&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot()) ? getNoOfRunners() : 1 ;
	}
	
	private int getNoOfRunners() {
		int runnerCount = 0;
		if(this.getWaveResources() != null && this.getWaveResources().size() > 0){
			Iterator iterator = this.getWaveResources().iterator();
			while(iterator.hasNext()){
			   PlanResource resource = (PlanResource)iterator.next();
			   if(EnumResourceType.RUNNER.getName().equals(resource.getEmployeeRoleType().getCode())) {
				   runnerCount++;
			   }
			}
		} else if(this.getPlanResources() != null && this.getPlanResources().size() > 0){

			Iterator iterator = this.getPlanResources().iterator();
			while(iterator.hasNext()){
			   PlanResource resource = (PlanResource)iterator.next();
			   if(EnumResourceType.RUNNER.getName().equals(resource.getEmployeeRoleType().getCode())) {
				   runnerCount++;
			   }
			}

		}
		return runnerCount;
	}

	@Override
	public boolean isValidSource() {
		// TODO Auto-generated method stub
		return this.getDeliveryDate() != null && this.getStartTime()!= null 
						&& this.getFirstDeliveryTime() != null && this.getLastDeliveryTime() != null 
							&& this.getCutOffTime() != null && this.getZone() != null; // && !"Y".equalsIgnoreCase(this.isOpen);
	}
	
	@Override
	public boolean needsConsolidation() {
		// For now consolidation will happen in UPS.
		// It can be turned off anytime based on future business requirements
		//return false;
		return this.getZone() != null && this.getZone().getArea() != null 
								&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot())
								&& !(this.getOriginFacility()!=null && this.getOriginFacility().getTrnFacilityType()!=null && EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equals(this.getOriginFacility().getTrnFacilityType().getName()))
								&& !(this.getDestinationFacility()!=null && this.getDestinationFacility().getTrnFacilityType()!=null && EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equals(this.getDestinationFacility().getTrnFacilityType().getName()));
	}

	@Override
	public void setNoOfResources(int value) {
		// TODO Auto-generated method stub
		if(getNoOfRunners() != value) {
			waveResources = new HashSet(0);
			EmployeeRoleType roleType = new EmployeeRoleType(EnumResourceType.RUNNER.getName(), EnumResourceType.RUNNER.getName());
			for(int intCount = 0; intCount < value; intCount++) {
				waveResources.add(new PlanResource(null, roleType));
			}
		}
	}

}
