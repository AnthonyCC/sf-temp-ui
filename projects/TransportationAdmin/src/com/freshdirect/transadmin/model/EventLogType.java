package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.transadmin.web.model.BaseCommand;

public class EventLogType extends BaseCommand implements Serializable  {
		
	private String name;
	private String description;
	private String customerReq;
	private String employeeReq;
	
	private List<EventLogSubType> subTypes = new ArrayList<EventLogSubType>();
	
	public EventLogType() {
		super();
	}

	public EventLogType(String name, String description) {
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

	public List<EventLogSubType> getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(List<EventLogSubType> subTypes) {
		this.subTypes = subTypes;
	}	
	public String getCustomerReq() {
		return customerReq;
	}

	public void setCustomerReq(String customerReq) {
		this.customerReq = customerReq;
	}

	public String getEmployeeReq() {
		return employeeReq;
	}
	public void setEmployeeReq(String employeeReq) {
		this.employeeReq = employeeReq;
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
		EventLogType other = (EventLogType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
}
