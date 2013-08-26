package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.BaseCommand;

public class MotEventModel extends BaseCommand implements Serializable {
	
	private String id;
	private Date eventDate;
	private String route;
	private String orderNo;
	private String addHocRoute;
	private String eventType;
	private String description;
	private String nextel;
	private Date verifiedDate;
	private String ticketNumber;
	private String verifiedBy;
	private boolean isVerified = false;
	
	private Set<String> stops = new HashSet<String>();
	
	private String zone;	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<String> getStops() {
		return stops;
	}
	public void setStops(Set<String> stops) {
		this.stops = stops;
	}
	public String getNextel() {
		return nextel;
	}
	public void setNextel(String nextel) {
		this.nextel = nextel;
	}
	public Date getVerifiedDate() {
		return verifiedDate;
	}
	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getVerifiedBy() {
		return verifiedBy;
	}
	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public boolean isVerified() {		
		return this.getVerifiedDate() != null ? true : isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}	
	public String getAddHocRoute() {
		return addHocRoute;
	}
	public void setAddHocRoute(String addHocRoute) {
		this.addHocRoute = addHocRoute;
	}
	
	public String getVerifiedTime() {
		
		if(this.getVerifiedDate() != null) {
			try {
				return TransStringUtil.getDatewithTime(this.getVerifiedDate());
			} catch (ParseException e) {
				// Do Nothing
			}
		}
		return null;
	}
	
	public int getTotalStopCnt() {
		return this.stops.size();
	}
}
