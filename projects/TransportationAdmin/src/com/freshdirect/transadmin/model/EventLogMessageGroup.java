package com.freshdirect.transadmin.model;

import java.io.Serializable;

import com.freshdirect.transadmin.web.model.BaseCommand;

@SuppressWarnings("serial")
public class EventLogMessageGroup extends BaseCommand implements Serializable {
		
	private String groupName;
	private String email;
	
	public EventLogMessageGroup() {
		super();
	}
	public EventLogMessageGroup(String groupName, String email) {
		super();
		this.groupName = groupName;
		this.email = email;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
