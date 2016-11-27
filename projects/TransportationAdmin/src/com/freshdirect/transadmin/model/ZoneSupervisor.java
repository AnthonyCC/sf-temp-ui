package com.freshdirect.transadmin.model;

import java.util.Date;

public class ZoneSupervisor {
	private String id;	
	private String dayPart;
	private String supervisorId;
	private Date effectiveDate;	
	private Zone zone;
	
	private String zoneId;

	private String supervisorFirstName;
	private String supervisorLastName;
	private String supervisorName;
	
	public ZoneSupervisor(String dayPart, String supervisorId, Date effectiveDate,
			String zoneCode) {
		this.dayPart=dayPart;
		this.supervisorId=supervisorId;
		this.effectiveDate=effectiveDate;
		this.zoneId=zoneCode;
	}

	
	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getSupervisorFirstName() {
		return supervisorFirstName;
	}

	public ZoneSupervisor() {
		super();
	}


	public void setSupervisorFirstName(String supervisorFirstName) {
		this.supervisorFirstName = supervisorFirstName;
	}

	public String getSupervisorLastName() {
		return supervisorLastName;
	}

	public void setSupervisorLastName(String supervisorLastName) {
		this.supervisorLastName = supervisorLastName;
	}
	
	public Zone getZone() {
		return this.zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDayPart() {
		return dayPart;
	}
	public void setDayPart(String dayPart) {
		this.dayPart = dayPart;
	}
	public String getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
