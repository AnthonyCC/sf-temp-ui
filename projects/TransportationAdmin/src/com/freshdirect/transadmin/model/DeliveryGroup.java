package com.freshdirect.transadmin.model;

import java.util.HashSet;
import java.util.Set;

public class DeliveryGroup implements java.io.Serializable {
	
	private String groupId;
	private String groupName;

	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
