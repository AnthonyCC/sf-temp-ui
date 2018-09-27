package com.freshdirect.mobileapi.controller.data.response;

import java.util.Date;

import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;

public class IssueSelfCreditResponse extends Message {

    private String complaintId;
    private String saleId;
    private String description;
    private String createdBy;
    private Date createDate;
    private EnumComplaintStatus complaintStatus;
    private ActionResult result;

    public IssueSelfCreditResponse(com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditResponse issueSelfCreditResponse) {
        this.complaintId = issueSelfCreditResponse.getComplaintId();
        this.saleId = issueSelfCreditResponse.getSaleId();
        this.description = issueSelfCreditResponse.getDescription();
        this.createdBy = issueSelfCreditResponse.getCreatedBy();
        this.createDate = issueSelfCreditResponse.getCreateDate();
        this.complaintStatus = issueSelfCreditResponse.getStatus();
        this.result = issueSelfCreditResponse.getResult();
        this.addErrorMessage(issueSelfCreditResponse.getGlobalErrorMessage());
    }

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

    public EnumComplaintStatus getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(EnumComplaintStatus complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public ActionResult getResult() {
        return result;
    }

    public void setResult(ActionResult result) {
        this.result = result;
    }
}
