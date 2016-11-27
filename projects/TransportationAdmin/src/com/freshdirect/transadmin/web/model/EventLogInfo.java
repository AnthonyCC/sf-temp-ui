package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.transadmin.model.EventLogMessageGroup;
import com.freshdirect.transadmin.model.EventLogSubType;
import com.freshdirect.transadmin.model.EventLogType;
import com.freshdirect.transadmin.model.MotEventType;

public class EventLogInfo implements Serializable {
	
	private List<EventLogType> eventType = new ArrayList<EventLogType>();

	private List<EventLogSubType> eventSubType = new ArrayList<EventLogSubType>();
	
	private List<EventLogMessageGroup> eventMessageGroup = new ArrayList<EventLogMessageGroup>();
	
	private List<MotEventType> motEventType = new ArrayList<MotEventType>();	

	public EventLogInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public List<EventLogType> getEventType() {
		return eventType;
	}
	public void setEventType(List<EventLogType> eventType) {
		this.eventType = eventType;
	}
	public List<EventLogSubType> getEventSubType() {
		return eventSubType;
	}
	public void setEventSubType(List<EventLogSubType> eventSubType) {
		this.eventSubType = eventSubType;
	}
	public List<EventLogMessageGroup> getEventMessageGroup() {
		return eventMessageGroup;
	}
	public void setEventMessageGroup(List<EventLogMessageGroup> eventMessageGroup) {
		this.eventMessageGroup = eventMessageGroup;
	}
	public List<MotEventType> getMotEventType() {
		return motEventType;
	}

	public void setMotEventType(List<MotEventType> motEventType) {
		this.motEventType = motEventType;
	}
}
