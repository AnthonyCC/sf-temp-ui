package com.freshdirect.fdstore.customer.selfcredit;

import java.io.Serializable;
import java.util.List;

public class PendingSelfComplaintResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 173041027391637338L;

    private List<String> pendingSelfComplaints;

    public List<String> getPendingSelfComplaints() {
        return pendingSelfComplaints;
    }

    public void setPendingSelfComplaints(List<String> pendingSelfComplaints) {
        this.pendingSelfComplaints = pendingSelfComplaints;
    }
}
