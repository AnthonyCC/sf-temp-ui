package com.freshdirect.backoffice.selfcredit.data;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.framework.webapp.ActionResult;

public class IssueSelfCreditResponse implements Serializable {

	private static final long serialVersionUID = -621761913141517633L;
	private String complaintId;
	private String saleId;
	private String description;
	private String createdBy;
	private Date createDate;
	private EnumComplaintStatus status;
	private ActionResult result;
	private String globalErrorMessage;
	
	public String getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}
	public String getSaleId() {
		return saleId;
	}
	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public EnumComplaintStatus getStatus() {
		return status;
	}
	public void setStatus(EnumComplaintStatus status) {
		this.status = status;
	}
	public ActionResult getResult() {
		return result;
	}
	public void setResult(ActionResult result) {
		this.result = result;
	}
	public String getGlobalErrorMessage() {
		return globalErrorMessage;
	}
	public void setGlobalErrorMessage(String globalErrorMessage) {
		this.globalErrorMessage = globalErrorMessage;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
