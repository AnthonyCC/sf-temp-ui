package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.BaseCommand;

public class EventModel extends BaseCommand implements Serializable {
	
	private String id;
	private Date eventDate;
	private String route;
	private String orderNo;
	private String truck;
	private Date windowStartTime;
	private Date windowEndTime;
	private String eventType;
	private String eventSubType;
	private String description;
	private String crossStreet;
	private String employeeId;
	private String scannerNumber;	
	private String windowTime;
	
	private Set<String> stops = new HashSet<String>();
	
	private String eventRefId;
	
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
	public String getTruck() {
		return truck;
	}
	public void setTruck(String truck) {
		this.truck = truck;
	}
	public Date getWindowStartTime() {
		return windowStartTime;
	}
	public void setWindowStartTime(Date windowStartTime) {
		this.windowStartTime = windowStartTime;
	}
	public Date getWindowEndTime() {
		return windowEndTime;
	}
	public void setWindowEndTime(Date windowEndTime) {
		this.windowEndTime = windowEndTime;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventSubType() {
		return eventSubType;
	}
	public void setEventSubType(String eventSubType) {
		this.eventSubType = eventSubType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCrossStreet() {
		return crossStreet;
	}
	public void setCrossStreet(String crossStreet) {
		this.crossStreet = crossStreet;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getScannerNumber() {
		return scannerNumber;
	}
	public void setScannerNumber(String scannerNumber) {
		this.scannerNumber = scannerNumber;
	}
	public Set<String> getStops() {
		return stops;
	}
	public void setStops(Set<String> stops) {
		this.stops = stops;
	}
	public String getWindowTime() {
				
		try {
			if(this.getWindowStartTime() != null && this.getWindowEndTime() != null) {
				return TransStringUtil.getServerTime(this.getWindowStartTime()) + " - " + TransStringUtil.getServerTime(this.getWindowEndTime());
			}
		} catch (ParseException e) {
			// Do Nothing
		}		
		return this.windowTime;
	}
	public void setWindowTime(String windowTime) {
		this.windowTime = windowTime;
	}
	public String getEventRefId() {
		return eventRefId;
	}
	public void setEventRefId(String eventRefId) {
		this.eventRefId = eventRefId;
	}	
	
	public int getTotalStopCnt() {
		return this.stops.size();
	}
	
}
