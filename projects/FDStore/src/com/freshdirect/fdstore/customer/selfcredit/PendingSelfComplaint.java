package com.freshdirect.fdstore.customer.selfcredit;

import java.io.Serializable;

public class PendingSelfComplaint implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2021504705683100358L;

    private String complaintId;
    private String customerId;
    private String note;

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
