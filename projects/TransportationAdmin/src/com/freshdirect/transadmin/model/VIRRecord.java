package com.freshdirect.transadmin.model;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import com.freshdirect.routing.service.exception.Issue;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.BaseCommand;

public class VIRRecord extends BaseCommand {
	private String id;	
	private String truckNumber;
	private String vendor;
	private Date createDate;	
	private String createdBy;
	private EmployeeInfo reportingDriver;
	private String driver;
		
	private Set virRecordIssues = new HashSet(0);
	
	public VIRRecord(){	}

	public VIRRecord(String id, String truckNumber,IssueType issueType, IssueSubType issueSubType, String vendor,
			Date createDate, String createdBy, EmployeeInfo reportingDriver,
			String driver, MaintenanceIssue maintenanceIssue,Set virRecordIssues) {
		super();
		this.id = id;		
		this.truckNumber = truckNumber;		
		this.vendor = vendor;
		this.createDate = createDate;		
		this.createdBy = createdBy;
		this.reportingDriver = reportingDriver;
		this.driver = driver;
		this.virRecordIssues = virRecordIssues;
	}

	public Set getVirRecordIssues() {
		return virRecordIssues;
	}

	public void setVirRecordIssues(Set virRecordIssues) {
		this.virRecordIssues = virRecordIssues;
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
