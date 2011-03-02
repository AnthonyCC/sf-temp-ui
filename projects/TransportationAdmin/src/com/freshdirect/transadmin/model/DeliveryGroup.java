package com.freshdirect.transadmin.model;

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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.groupId;
	}
	
}
