package com.freshdirect.transadmin.model;

import java.text.ParseException;
import java.util.Date;


import com.freshdirect.routing.service.exception.Issue;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.BaseCommand;

public class VIRRecord extends BaseCommand {
	private String id;	
	private String truckNumber;
	private IssueType issueType;
	private String issueTypeId;
	private IssueSubType issueSubType;
	private String issueSubTypeId;
	private String vendor;
	private Date createDate;
	private String damageLocation;
	private String issueSide;
	private String comments;
	private String createdBy;
	private EmployeeInfo reportingDriver;
	private String driver;
	private MaintenanceIssue maintenanceIssue;	
	
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

	public VIRRecord(){

	}

	public VIRRecord(String id, String truckNumber,
			IssueType issueType, IssueSubType issueSubType, String vendor,
			Date createDate, String damageLocation, String issueSide,
			String comments, String createdBy, EmployeeInfo reportingDriver,
			String driver, MaintenanceIssue maintenanceIssue) {
		super();
		this.id = id;		
		this.truckNumber = truckNumber;
		this.issueType = issueType;
		this.issueSubType = issueSubType;
		this.vendor = vendor;
		this.createDate = createDate;
		this.damageLocation = damageLocation;
		this.issueSide = issueSide;
		this.comments = comments;
		this.createdBy = createdBy;
		this.reportingDriver = reportingDriver;
		this.driver = driver;
		this.maintenanceIssue = maintenanceIssue;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public String getTruckNumber() {
		return truckNumber;
	}

	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}

	public IssueType getIssueType() {
		return issueType;
	}

	public void setIssueType(IssueType issueType) {
		this.issueType = issueType;
	}

	public IssueSubType getIssueSubType() {
		return issueSubType;
	}

	public void setIssueSubType(IssueSubType issueSubType) {
		this.issueSubType = issueSubType;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public EmployeeInfo getReportingDriver() {
		return reportingDriver;
	}

	public void setReportingDriver(EmployeeInfo reportingDriver) {
		this.reportingDriver = reportingDriver;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getIssueTypeId() {
		if(getIssueType() == null)
			return null;
		return getIssueType().getIssueTypeId();
	}

	public void setIssueTypeId(String issueTypeId) {
		if("".equals(issueTypeId)){
			setIssueType(null);
		}else{
			IssueType sType = new IssueType();
			sType.setIssueTypeId(issueTypeId);
			setIssueType(sType);
		}
	}

	public String getIssueSubTypeId() {
		if(getIssueSubType() == null)
			return null;
		return getIssueSubType().getIssueSubTypeId();
	}

	public void setIssueSubTypeId(String issueSubTypeId) {
		if("".equals(issueSubTypeId)){
			setIssueSubType(null);
		}else{
			IssueSubType subType = new IssueSubType();
			subType.setIssueSubTypeId(issueSubTypeId);
			setIssueSubType(subType);
		}
	}
	
	public String getCreatedDate(){
		String date = "";
		if(this.getCreateDate()!=null){
			try {
				date = TransStringUtil.getDate(this.getCreateDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return date;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
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
		VIRRecord other = (VIRRecord) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
