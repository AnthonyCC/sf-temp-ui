package com.freshdirect.transadmin.model;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.transadmin.constants.EnumIssueStatus;
import com.freshdirect.transadmin.constants.EnumServiceStatus;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.BaseCommand;

public class MaintenanceIssue extends BaseCommand {
	private String id;
	private String issueId;
	private String truckNumber;
	private String issueType;
	private String issueSubType;
	private Date createDate;
	private Date modifiedDate;
	private String createdBy;
	private String issueStatus;
	private int daysOpen;
	private String damageLocation;
	private String issueSide;
	private String comments;
	private String vendor;	
	private String serviceStatus;	
	private Date estimatedRepairDate;
	private Date actualRepairDate;
	private Date verificationDate;
	private String verifiedBy;
	private String repairedBy;	
	private String subTypeId;
	
	public String getSubTypeId() {
		if(getIssueSubType() == null)
			return null;
		subTypeId = getIssueSubType();	
		return subTypeId;
	}

	public void setSubTypeId(String subTypeId) {
		this.subTypeId = subTypeId;
	}

	private Set<VIRRecord> virRecords = new HashSet<VIRRecord>(0);	
	
	public MaintenanceIssue(){
		
	}
	
	public MaintenanceIssue(String id, String truckNumber, String issueType,
			String issueSubType, Date createDate, Date modifiedDate,
			String createdBy, String issueStatus, int daysOpen, String damageLocation,
			String issueSide, String comments, Set<VIRRecord> virRecords) {
		super();
		this.id = id;
		this.truckNumber = truckNumber;
		this.issueType = issueType;
		this.issueSubType = issueSubType;
		this.createDate = createDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.issueStatus = issueStatus;		
		this.daysOpen = daysOpen;
		this.damageLocation = damageLocation;
		this.issueSide = issueSide;
		this.comments = comments;
		this.virRecords = virRecords;
	}

	public MaintenanceIssue(String id) {
		super();
		this.id = id;
	}
	public String getTruckNumber() {
		return truckNumber;
	}
	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
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
	public Set<VIRRecord> getVirRecords() {
		return virRecords;
	}
	public void setVirRecords(Set<VIRRecord> virRecords) {
		this.virRecords = virRecords;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
		
	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
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
	
	
	public String getIssueStatus() {
		return issueStatus;
	}
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
	
	public int getDaysOpen() {
		try {
			if(EnumIssueStatus.OPEN.getName().equals(this.issueStatus)){
				daysOpen = (int) (((TransStringUtil.getDate(TransStringUtil.getCurrentDate()).getTime() - this.getCreateDate().getTime())/TransStringUtil.DAY))+1;
			}else if(EnumIssueStatus.RESOLVED.getName().equals(this.issueStatus) && this.actualRepairDate != null){			
				daysOpen = (int) (((this.actualRepairDate.getTime() - this.getCreateDate().getTime())/TransStringUtil.DAY))+1;
			}
		} catch (ParseException e) {				
			e.printStackTrace();
		}
		return daysOpen;
	}
	public void setDaysOpen(int daysOpen) {
		this.daysOpen = daysOpen;
	}
	
	public String getDamageLocation() {
		return damageLocation;
	}
	public void setDamageLocation(String damageLocation) {
		this.damageLocation = damageLocation;
	}
	public String getIssueSide() {
		return issueSide;
	}
	public void setIssueSide(String issueSide) {
		this.issueSide = issueSide;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}	
	
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	
	public Date getEstimatedRepairDate() {
		return estimatedRepairDate;
	}
	public void setEstimatedRepairDate(Date estimatedRepairDate) {
		this.estimatedRepairDate = estimatedRepairDate;
	}
	public Date getActualRepairDate() {
		return actualRepairDate;
	}
	public void setActualRepairDate(Date actualRepairDate) {
		this.actualRepairDate = actualRepairDate;
	}
	public Date getVerificationDate() {
		return verificationDate;
	}
	public void setVerificationDate(Date verificationDate) {
		this.verificationDate = verificationDate;
	}
	public String getVerifiedBy() {
		return verifiedBy;
	}
	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}
	
	public String getRepairedBy() {
		return repairedBy;
	}

	public void setRepairedBy(String repairedBy) {
		this.repairedBy = repairedBy;
	}
	
	public boolean isTruckInService() {
		if(EnumServiceStatus.INSERVICE.getDescription().equalsIgnoreCase(this.serviceStatus))
			return true;
		else
			return false;
	}
	
/*	public String getIssueTypeId() {
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
	}*/
	
	public String getCreatedDate(){
		String date = "";
		if(this.getCreateDate()!=null){
			try {
				date = TransStringUtil.getDate(this.getCreateDate());
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		}
		return date;
	}
	
	public String getDisplayVerificationDate(){
		String date = "";
		if(this.getVerificationDate()!=null){
			try {
				date = TransStringUtil.getDate(this.getVerificationDate());
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		}
		return date;
	}
	
	public String getDisplayEstimatedRepairDate(){
		String date = "";
		if(this.getEstimatedRepairDate()!=null){
			try {
				date = TransStringUtil.getDate(this.getEstimatedRepairDate());
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		}
		return date;
	}
	
	public String getDisplayActualRepairDate(){
		String date = "";
		if(this.getActualRepairDate()!=null){
			try {
				date = TransStringUtil.getDate(this.getActualRepairDate());
			} catch (ParseException e) {				
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
		MaintenanceIssue other = (MaintenanceIssue) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
