package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.transadmin.web.model.BaseCommand;

public class MotEventType extends BaseCommand implements Serializable  {
	
	private String name;
	private String description;

	private EventLogMessageGroup msgGroup;
	
	public MotEventType(String name, String description) {
		super();
		this.name = name;
		this.description = description;
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
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MotEventType other = (MotEventType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
}
