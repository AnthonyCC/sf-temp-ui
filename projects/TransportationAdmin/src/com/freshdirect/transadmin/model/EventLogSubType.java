package com.freshdirect.transadmin.model;

import java.io.Serializable;

import com.freshdirect.transadmin.web.model.BaseCommand;

public class EventLogSubType extends BaseCommand implements Serializable {
	private String id;
	private String name;
	private String description;
	private String eventTypeId;
	private String eventTypeName;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEventTypeName() {
		return eventTypeName;
	}
	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (eventTypeId == null) {
			if (other.eventTypeId != null)
				return false;
		} else if (!eventTypeId.equals(other.eventTypeId))
			return false;

		return true;
	}
	
	
}
