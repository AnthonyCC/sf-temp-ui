package com.freshdirect.transadmin.model;

// Generated Nov 18, 2008 3:11:21 PM by Hibernate Tools 3.2.2.GA

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Zone generated by hbm2java
 */
public class Zone implements java.io.Serializable {

	private String zoneCode;
	private Region region;
	private TrnZoneType trnZoneType;
	private String name;
	private String unattended;
	private TrnArea area;
	private BigDecimal priority;
	private String obsolete;
	private final BigDecimal DEFAULT_PRIORITY=new BigDecimal(-1);
    private BigInteger stemFromTime;
    private BigInteger stemToTime;
	
	public String getObsolete() {
		return obsolete;
	}
	
	public void setObsolete(String obsolete) {
		this.obsolete = obsolete;
	}
	
	public Zone() {
	}
	
	public Zone(String zonneCode) {
		this.zoneCode=zonneCode;
	}

	public Zone(String zoneCode, Region region, String name,BigDecimal priority) {
		this.zoneCode = zoneCode;
		this.region = region;
		this.name = name;
		this.priority=priority;
	}

	public Zone(String zoneCode, Region region, TrnZoneType trnZoneType,
			String name, String unattended, TrnArea area, Set plans,
			Set dispatchs,BigDecimal priority) {
		this.zoneCode = zoneCode;
		this.region = region;
		this.trnZoneType = trnZoneType;
		this.name = name;
		this.unattended = unattended;
		this.area = area;
		this.priority=priority;
	}

	public String getZoneCode() {
		return this.zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public TrnZoneType getTrnZoneType() {
		return this.trnZoneType;
	}

	public void setTrnZoneType(TrnZoneType trnZoneType) {
		this.trnZoneType = trnZoneType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnattended() {
		return this.unattended;
	}

	public void setUnattended(String unattended) {
		this.unattended = unattended;
	}

	public TrnArea getArea() {
		return this.area;
	}

	public void setArea(TrnArea area) {
		this.area = area;
	}

	public String getDisplayName() {
		return this.zoneCode+" - "+this.name;
	}

	public BigDecimal getPriority() {
		return priority==null?DEFAULT_PRIORITY:priority;
	}

	public void setPriority(BigDecimal priority) {
		this.priority = priority;
	}

	public BigInteger getStemFromTime() {
		return stemFromTime==null?stemToTime:stemFromTime;
	}

	public void setStemFromTime(BigInteger stemFromTime) {
		this.stemFromTime = stemFromTime;
	}

	public BigInteger getStemToTime() {
		return stemToTime;
	}

	public void setStemToTime(BigInteger stemToTime) {
		this.stemToTime = stemToTime;
	}

	
	

}
