package com.freshdirect.transadmin.web.model;

import com.freshdirect.transadmin.model.TrnDispatchPlan;


public class DispatchSheetCommand extends BaseCommand {
	
	private Long planId;
	private String routeId;
	
	private String selected;
	private String supervisorId;
	private String zoneId;
	private String slotId;
	private String driverId;
	private String primaryHelperId;
	private String secondaryHelperId;	
	private String truckId;
	private String nextelId;
	
	public DispatchSheetCommand() {
	
	}
	public DispatchSheetCommand(TrnDispatchPlan plan) {
		this.setPlanId(plan.getPlanId());
		this.setSupervisorId(plan.getTrnSupervisor() != null ? plan.getTrnSupervisor().getEmployeeId() : null);
		this.setZoneId(plan.getTrnZone() != null ? plan.getTrnZone().getZoneId() : null);
		this.setSlotId(plan.getTrnTimeslot() != null ? plan.getTrnTimeslot().getSlotId() : null);
		this.setDriverId(plan.getTrnDriver() != null ? plan.getTrnDriver().getEmployeeId() : null);
		this.setPrimaryHelperId(plan.getTrnPrimaryHelper() != null ? plan.getTrnPrimaryHelper().getEmployeeId() : null);
		this.setSecondaryHelperId(plan.getTrnSecondaryHelper() != null ? plan.getTrnSecondaryHelper().getEmployeeId() : null);
	}
	
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public String getNextelId() {
		return nextelId;
	}
	public void setNextelId(String nextelId) {
		this.nextelId = nextelId;
	}
	public Long getPlanId() {
		return planId;
	}
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	public String getPrimaryHelperId() {
		return primaryHelperId;
	}
	public void setPrimaryHelperId(String primaryHelperId) {
		this.primaryHelperId = primaryHelperId;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getSecondaryHelperId() {
		return secondaryHelperId;
	}
	public void setSecondaryHelperId(String secondaryHelperId) {
		this.secondaryHelperId = secondaryHelperId;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}
	public String getSlotId() {
		return slotId;
	}
	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}
	public String getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}
	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	
	public String toString() {
		
		return selected+"|"+zoneId+"|"
				+slotId+"|"+driverId+"|"+primaryHelperId+"|"+secondaryHelperId+"|"
					+routeId+"|"+supervisorId+"|"+truckId+"|"+nextelId+"|"+planId+"\n";
	}

}
