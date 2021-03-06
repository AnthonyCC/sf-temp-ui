package com.freshdirect.transadmin.model;

//Generated Dec 5, 2008 2:34:33 PM by Hibernate Tools 3.2.2.GA

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.util.TransStringUtil;

/**
 * Dispatch generated by hbm2java
 */
public class IssueType implements java.io.Serializable {

	private String id;
	private String issueTypeName;
	private String issueTypeDescription;
	private int isActive;	
	private Date createdDate;
	private String createdBy;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	private Set issueSubTypes = new HashSet(0);

	public String getIssueTypeId() {
		return id;
	}

	public IssueType(String issueTypeId, String issueTypeName,
			String issueTypeDescription, int isActive, Date createdDate,
			String createdBy, Set issueSubTypes) {
		super();
		this.id = issueTypeId;
		this.issueTypeName = issueTypeName;
		this.issueTypeDescription = issueTypeDescription;
		this.isActive = isActive;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.issueSubTypes = issueSubTypes;
	}

	public int getIsActive() {		
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public void setIssueTypeId(String issueTypeId) {
		this.id = issueTypeId;
	}

	public String getIssueTypeName() {
		return issueTypeName;
	}

	public void setIssueTypeName(String issueTypeName) {
		this.issueTypeName = issueTypeName;
	}

	public String getIssueTypeDescription() {
		return issueTypeDescription;
	}

	public void setIssueTypeDescription(String issueTypeDescription) {
		this.issueTypeDescription = issueTypeDescription;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Set getIssueSubTypes() {
		return issueSubTypes;
	}

	public void setIssueSubTypes(Set issueSubTypes) {
		this.issueSubTypes = issueSubTypes;
	}

	public boolean isStatus() {
		if(this.getIsActive() == 1)
			return true;
		else
			return false;
	}

	public IssueType() {
	}

	public IssueType(String assetId) {
		super();
		this.id = id;
	}
	
	public String getCreatedDateDisplay(){
		try {
			return TransStringUtil.getDatewithTime(this.createdDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public String toString(){
		return this.id;
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
		IssueType other = (IssueType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
