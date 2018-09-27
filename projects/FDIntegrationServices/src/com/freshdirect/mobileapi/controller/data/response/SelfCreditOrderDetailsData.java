package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class SelfCreditOrderDetailsData extends Message {

    private List<SelfCreditOrderItemData> orderLines;

    public SelfCreditOrderDetailsData(com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderDetailsData selfCreditOrderDetailsData) {
        this.orderLines = com.freshdirect.mobileapi.controller.data.response.SelfCreditOrderItemData.wrap(selfCreditOrderDetailsData.getOrderLines());
    }

    public List<SelfCreditOrderItemData> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<SelfCreditOrderItemData> orderLines) {
        this.orderLines = orderLines;
    }
}
