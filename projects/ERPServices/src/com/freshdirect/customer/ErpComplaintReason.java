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

    private String id;
	private String departmentCode;
	private String departmentName;
	private String reason;

	public ErpComplaintReason(String id, String dept_code,String dept_name, String reason) {
        this.id = id;
        this.departmentCode = dept_code;
		this.departmentName = dept_name;
		this.reason = reason;
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

}