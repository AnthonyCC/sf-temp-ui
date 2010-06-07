package com.freshdirect.transadmin.model;

import java.io.Serializable;

public class EmployeeTeam implements Serializable {
	
	private String kronosId;
	private String leadKronosId;
	
	public EmployeeTeam() {
		super();
	
	}
	
	public EmployeeTeam(String kronosId, String leadKronosId) {
		super();
		this.kronosId = kronosId;
		this.leadKronosId = leadKronosId;
	}
	public String getKronosId() {
		return kronosId;
	}
	public void setKronosId(String kronosId) {
		this.kronosId = kronosId;
	}
	public String getLeadKronosId() {
		return leadKronosId;
	}
	public void setLeadKronosId(String leadKronosId) {
		this.leadKronosId = leadKronosId;
	}
	
	
	

}
