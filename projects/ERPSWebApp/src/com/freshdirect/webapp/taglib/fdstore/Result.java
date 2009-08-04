package com.freshdirect.webapp.taglib.fdstore;

import java.util.List;
import java.util.Map;

public class Result {
	public boolean isZoneCtActive() {
		return zoneCtActive;
	}
	Result(List timeslots, Map zones, boolean zoneCtActive, List messages) {
		this.timeslots = timeslots;
		this.zones  =zones;
		this.zoneCtActive = zoneCtActive;
		this.messages = messages;
	}
	List timeslots;
	Map zones;
	boolean zoneCtActive = false;
	List messages;
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