package com.freshdirect.backoffice.selfcredit.data;

import java.util.List;

public class SelfCreditOrderDetailsData {

    private List<SelfCreditOrderItemData> orderLines;

    public List<SelfCreditOrderItemData> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<SelfCreditOrderItemData> orderLines) {
        this.orderLines = orderLines;
    }
}
