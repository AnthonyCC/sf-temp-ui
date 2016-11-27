/*
 * $Workfile:ErpComplaintReason.java$
 *
 * $Date:8/5/2003 12:27:11 AM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

/** 
 * Simple Bean for department complaint reasons
 *
 * @version $Revision:4$
 * @author $Author:Mike Rose$
 */
public class ErpComplaintReason implements java.io.Serializable {
	private static final long serialVersionUID = -2612789052443612464L;

	private String id;
	private String departmentCode;
	private String departmentName;
	private String reason;

	int	priority;
	String subjectCode;	// Case subject code

	private EnumComplaintDlvIssueType dlvIssueType;

	public ErpComplaintReason(String id, String dept_code,String dept_name, String reason, int pri, String subjectCode, EnumComplaintDlvIssueType dlvIssueType) {
        this.id = id;
        this.departmentCode = dept_code;
		this.departmentName = dept_name;
		this.reason = reason;
		this.priority = pri;
		this.subjectCode = subjectCode;
		this.dlvIssueType = dlvIssueType;
	}

    public String getId() {
        return this.id;
    }

	public String getDepartmentCode() {
		return this.departmentCode;
	}

	public String getDepartmentName() {
		return this.departmentName;
	}

	public String getReason() {
		return this.reason;
	}

	/**
	 * Returns reason priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Returns Case Subject code
	 */
	public String getSubjectCode() {
		return subjectCode;
	}

	
	/**
	 * Returns type of delivery issue (can be null)
	 * @return
	 */
	public EnumComplaintDlvIssueType getDeliveryIssueType() {
		return dlvIssueType;
	}

	public String toString() {
		return "{id="+this.id+"; dept="+this.departmentName+"; reason="+this.reason+"}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ErpComplaintReason) {
			ErpComplaintReason other = (ErpComplaintReason) obj;
			return this.id.equalsIgnoreCase(other.getId());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}
