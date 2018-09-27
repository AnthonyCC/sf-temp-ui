package com.freshdirect.backoffice.selfcredit.data;

import com.freshdirect.customer.EnumComplaintDlvIssueType;

public class ComplaintReason {

    private String id;
    private String departmentCode;
    private String departmentName;
    private String reason;
    private int priority;
    private String subjectCode; // Case subject code
    private EnumComplaintDlvIssueType dlvIssueType;
    private boolean showCustomer;
    private String customerDisplayName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public EnumComplaintDlvIssueType getDlvIssueType() {
        return dlvIssueType;
    }

    public void setDlvIssueType(EnumComplaintDlvIssueType dlvIssueType) {
        this.dlvIssueType = dlvIssueType;
    }

	public boolean isShowCustomer() {
		return showCustomer;
	}

	public void setShowCustomer(boolean showCustomer) {
		this.showCustomer = showCustomer;
	}

    public String getCustomerDisplayName() {
        return customerDisplayName;
    }

    public void setCustomerDisplayName(String customerDisplayName) {
        this.customerDisplayName = customerDisplayName;
    }
}
