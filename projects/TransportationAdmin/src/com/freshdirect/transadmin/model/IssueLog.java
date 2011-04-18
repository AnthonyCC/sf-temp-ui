package com.freshdirect.transadmin.model;

import com.freshdirect.transadmin.web.model.BaseCommand;

public class IssueLog extends BaseCommand {
	private String id;
	private String issueType;
	private String issueSubType;
	private String damageLocation;
	private String issueSide;
	private String comments;
	private MaintenanceIssue maintenanceIssue;
	private VIRRecord virRecord;
	
	public String getIssueSide() {
		return issueSide;
	}

	public void setIssueSide(String issueSide) {
		this.issueSide = issueSide;
	}

	public MaintenanceIssue getMaintenanceIssue() {
		return maintenanceIssue;
	}

	public void setMaintenanceIssue(MaintenanceIssue maintenanceIssue) {
		this.maintenanceIssue = maintenanceIssue;
	}

	public VIRRecord getVirRecord() {
		return virRecord;
	}

	public void setVirRecord(VIRRecord virRecord) {
		this.virRecord = virRecord;
	}

	public IssueLog() {

	}

	public IssueLog(String id, String issueType, String issueSubType,
			String damageLocation, String issueSide, String comments,
			MaintenanceIssue maintenanceIssue, VIRRecord virRecord) {
		super();
		this.id = id;
		this.issueType = issueType;
		this.issueSubType = issueSubType;
		this.damageLocation = damageLocation;
		this.issueSide = issueSide;
		this.comments = comments;
		this.maintenanceIssue = maintenanceIssue;
		this.virRecord = virRecord;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getIssueSubType() {
		return issueSubType;
	}

	public void setIssueSubType(String issueSubType) {
		this.issueSubType = issueSubType;
	}

	public String getDamageLocation() {
		return damageLocation;
	}

	public void setDamageLocation(String damageLocation) {
		this.damageLocation = damageLocation;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String toString(){
		return this.issueType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((issueType == null) ? 0 : issueType.hashCode());
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
		IssueLog other = (IssueLog) obj;
		if (issueType == null) {
			if (other.issueType != null)
				return false;
		} else if (!issueType.equals(other.issueType))
			return false;
		return true;
	}
}
