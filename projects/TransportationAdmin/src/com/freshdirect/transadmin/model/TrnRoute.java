package com.freshdirect.transadmin.model;

// Generated Jul 30, 2007 6:23:08 PM by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

/**
 * TrnRoute generated by hbm2java
 */
public class TrnRoute  implements java.io.Serializable, TrnBaseEntityI {

	private String routeId;

	private TrnEmployee trnSupervisor;

	private TrnZone trnZone;

	private String routeAmPm;

	private String routeNumber;

	private Set trnEmployeeassignments = new HashSet(0);

	private Set trnTruckassignments = new HashSet(0);
	
	private String obsolete;

	public String getObsolete() {
		return obsolete;
	}

	public void setObsolete(String obsolete) {
		this.obsolete = obsolete;
	}

	public TrnRoute() {
	}

	public TrnRoute(String routeId) {
		this.routeId = routeId;
	}

	public TrnRoute(String routeId, TrnEmployee trnSupervisor,
			TrnZone trnZone, String routeAmPm,
			String routeNumber, Set trnEmployeeassignments,
			Set trnTruckassignments) {
		this.routeId = routeId;
		this.trnSupervisor = trnSupervisor;
		this.trnZone = trnZone;
		this.routeAmPm = routeAmPm;
		this.routeNumber = routeNumber;
		this.trnEmployeeassignments = trnEmployeeassignments;
		this.trnTruckassignments = trnTruckassignments;
	}

	public String getRouteId() {
		return this.routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public TrnEmployee getTrnSupervisor() {
		return this.trnSupervisor;
	}

	public void setTrnSupervisor(TrnEmployee trnSupervisor) {
		this.trnSupervisor = trnSupervisor;
	}
	

	public TrnZone getTrnZone() {
		return trnZone;
	}

	public void setTrnZone(TrnZone trnZone) {
		this.trnZone = trnZone;
	}

	public String getRouteAmPm() {
		return this.routeAmPm;
	}

	public void setRouteAmPm(String routeAmPm) {
		this.routeAmPm = routeAmPm;
	}

	public String getRouteNumber() {
		return this.routeNumber;
	}

	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public Set getTrnEmployeeassignments() {
		return this.trnEmployeeassignments;
	}

	public void setTrnEmployeeassignments(Set trnEmployeeassignments) {
		this.trnEmployeeassignments = trnEmployeeassignments;
	}

	public Set getTrnTruckassignments() {
		return this.trnTruckassignments;
	}

	public void setTrnTruckassignments(Set trnTruckassignments) {
		this.trnTruckassignments = trnTruckassignments;
	}
	
	public String getSupervisor() {
		if(getTrnSupervisor() == null) {
			return null;
		}
		return getTrnSupervisor().getEmployeeId();
	}

	public void setSupervisor(String trnSupervisorId) {
		if("null".equals(trnSupervisorId)) {
			setTrnSupervisor(null);
		} else {
			TrnEmployee trnSupervisor = new TrnEmployee();
			trnSupervisor.setEmployeeId(trnSupervisorId);
			setTrnSupervisor(trnSupervisor);
		}
	}
	
	public String getZone() {
		if(getTrnZone() == null) {
			return null;
		}
		return getTrnZone().getZoneId();
	}

	public void setZone(String trnzoneId) {
		if("null".equals(trnzoneId)) {
			setTrnZone(null);
		} else {
			TrnZone trnZone = new TrnZone();
			trnZone.setZoneId(trnzoneId);
			setTrnZone(trnZone);
		}
	}
	
	public boolean isObsoleteEntity() {
		return ((trnSupervisor != null && trnSupervisor.getObsolete() != null) ||				
				(trnZone != null && trnZone.getObsolete() != null));
	}

}
