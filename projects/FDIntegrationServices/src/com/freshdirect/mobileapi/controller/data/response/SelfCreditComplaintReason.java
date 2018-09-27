package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

public class SelfCreditComplaintReason {

    private String id;
    private String reason;

    public SelfCreditComplaintReason(com.freshdirect.backoffice.selfcredit.data.SelfCreditComplaintReason complaintReason) {
        this.id = complaintReason.getId();
        this.reason = complaintReason.getReason();
    }

    public static List<SelfCreditComplaintReason> wrap(List<com.freshdirect.backoffice.selfcredit.data.SelfCreditComplaintReason> complaintReasons) {
        List<SelfCreditComplaintReason> selfCreditComplaintReasons = new ArrayList<SelfCreditComplaintReason>();
        for (com.freshdirect.backoffice.selfcredit.data.SelfCreditComplaintReason complaintReason : complaintReasons) {
            selfCreditComplaintReasons.add(new SelfCreditComplaintReason(complaintReason));
        }
        return selfCreditComplaintReasons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
