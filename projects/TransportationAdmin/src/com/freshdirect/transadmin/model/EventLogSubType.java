package com.freshdirect.transadmin.model;

import java.io.Serializable;

import com.freshdirect.transadmin.web.model.BaseCommand;

public class EventLogSubType extends BaseCommand implements Serializable {
		
	private String name;
	private String description;
	private String eventTypeId;
	
	private EventLogMessageGroup msgGroup;
		
	public EventLogSubType() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEventTypeId() {
		return eventTypeId;
	}
	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}	
	public EventLogMessageGroup getMsgGroup() {
		return msgGroup;
	}
	public void setMsgGroup(EventLogMessageGroup msgGroup) {
		this.msgGroup = msgGroup;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((eventTypeId == null) ? 0 : eventTypeId.hashCode());
		return result;
	}
	
	@Override	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventLogSubType other = (EventLogSubType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (eventTypeId == null) {
			if (other.eventTypeId != null)
				return false;
		} else if (!eventTypeId.equals(other.eventTypeId))
			return false;

		return true;
	}
	
	
}
