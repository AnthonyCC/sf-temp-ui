package com.freshdirect.transadmin.model;

// Generated Jan 7, 2009 10:45:25 AM by Hibernate Tools 3.2.2.GA

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.routing.model.IGeographicLocation;

/**
 * GeoRestriction generated by hbm2java
 */
public class GeoRestriction implements java.io.Serializable {

	private String id;
	private String name;
	private String boundaryCode;
	private Date startDate;
	private Date endDate;
	private String message;
	private String inactive;
	private String comments;

	private Set geoRestrictionDays = new HashSet(0);

	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("GeoRestriction: ");
		result.append("id="+id);
		result.append(" name= "+ name);
		result.append(" boundarycode="+boundaryCode);
		result.append("\n");
		result.append(" startDate="+startDate);
		result.append(" endDate="+endDate);
		result.append("\n");
		result.append(" message="+message);
		result.append(" inactive="+inactive);
		result.append(" comments="+comments);
		
		result.append(" \nDays: ");
		
		Iterator iterator = geoRestrictionDays.iterator();
		while(iterator.hasNext()) {
			GeoRestrictionDays var= (GeoRestrictionDays)iterator.next();
			result.append(" \nrestrictionId="+ var.getRestrictionDaysId().getRestrictionId());
			result.append(" dayOfWeek="+ var.getRestrictionDaysId().getDayOfWeek());
			result.append(" seqno="+ var.getRestrictionDaysId().getSeqno());
			result.append(" cond="+ var.getCondition());
			result.append(" startTime="+var.getStartTime());
			result.append(" endTime="+var.getEndTime());
			}
		
		return result.toString();
		
	}

	public GeoRestriction() {
	}

	public GeoRestriction(String id) {
		this.id = id;
	}

	public GeoRestriction(String id, String name, String boundaryCode,
			Date startDate, Date endDate, String message, String inactive,
			String comments) {
		this.id = id;
		this.name = name;
		this.boundaryCode = boundaryCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.message = message;
		this.inactive = inactive;
		this.comments = comments;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBoundaryCode() {
		return this.boundaryCode;
	}

	public void setBoundaryCode(String boundaryCode) {
		this.boundaryCode = boundaryCode;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInactive() {
		return this.inactive;
	}

	public void setInactive(String inactive) {
		this.inactive = inactive;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Set getGeoRestrictionDays() {
		return geoRestrictionDays;
	}

	public void setGeoRestrictionDays(Set geoRestrictionDays) {
		this.geoRestrictionDays = geoRestrictionDays;
	}

}
