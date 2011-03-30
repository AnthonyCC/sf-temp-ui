package com.freshdirect.webapp.taglib.fdstore;

import java.util.List;
import java.util.Map;

import com.freshdirect.framework.webapp.ActionResult;

public class Result extends ActionResult {
	public boolean isZoneCtActive() {
		return zoneCtActive;
	}
	Result(){
	}
	Result(List timeslots, Map zones, boolean zoneCtActive, List messages, List comments) {
		this.timeslots = timeslots;
		this.zones  =zones;
		this.zoneCtActive = zoneCtActive;
		this.messages = messages;
		this.comments= comments;
	}
	List timeslots;
	Map zones;
	boolean zoneCtActive = false;
	List messages;
	List comments;
	
	public List getComments() {
		return comments;
	}
	public void setComments(List comments) {
		this.comments = comments;
	}
	public List getTimeslots() {
		return timeslots;
	}
	public Map getZones() {
		return zones;
	}
	public List getMessages() {
		return messages;
	}
	public void setMessages(List messages) {
		this.messages = messages;
	}
	
}