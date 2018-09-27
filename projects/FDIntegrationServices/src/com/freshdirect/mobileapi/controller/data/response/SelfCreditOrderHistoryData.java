package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class SelfCreditOrderHistoryData extends Message {

    private String userFirstName;
    private String userLastName;
    private List<SelfCreditOrderData> orders;

    public SelfCreditOrderHistoryData(com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderHistoryData selfCreditOrderHistoryData) {
        this.userFirstName = selfCreditOrderHistoryData.getUserFirstName();
        this.userLastName = selfCreditOrderHistoryData.getUserLastName();
        this.orders = SelfCreditOrderData.wrap(selfCreditOrderHistoryData.getOrders());
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public List<SelfCreditOrderData> getOrders() {
        return orders;
    }

    public void setOrders(List<SelfCreditOrderData> orders) {
        this.orders = orders;
    }
}
