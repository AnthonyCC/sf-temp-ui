package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.transadmin.web.model.BaseCommand;

public class MotEventType extends BaseCommand implements Serializable  {
	
	private String id;
	private String name;
	private String description;
	private String orderNumberReq;
	private EventLogMessageGroup msgGroup;
	
	public MotEventType(String id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getOrderNumberReq() {
		return orderNumberReq;
	}

	public void setOrderNumberReq(String orderNumberReq) {
		this.orderNumberReq = orderNumberReq;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
