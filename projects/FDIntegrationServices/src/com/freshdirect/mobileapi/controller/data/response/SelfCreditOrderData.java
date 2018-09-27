package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelfCreditOrderData {

    private String saleId;
    private Date requestedDate;
    private Date deliveryStart;
    private Date deliveryEnd;
    private String standingOrderName;

    public SelfCreditOrderData(com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderData orderData) {
        this.saleId = orderData.getSaleId();
        this.requestedDate = orderData.getRequestedDate();
        this.deliveryStart = orderData.getDeliveryStart();
        this.deliveryEnd = orderData.getDeliveryEnd();
        this.standingOrderName = orderData.getStandingOrderName();
    }

    public static List<SelfCreditOrderData> wrap(List<com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderData> orders) {
        List<SelfCreditOrderData> selfCreditOrders = new ArrayList<SelfCreditOrderData>();
        for (com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderData orderData : orders) {
            selfCreditOrders.add(new SelfCreditOrderData(orderData));
        }
        return selfCreditOrders;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Date getDeliveryStart() {
        return deliveryStart;
    }

    public void setDeliveryStart(Date deliveryStart) {
        this.deliveryStart = deliveryStart;
    }

    public Date getDeliveryEnd() {
        return deliveryEnd;
    }

    public void setDeliveryEnd(Date deliveryEnd) {
        this.deliveryEnd = deliveryEnd;
    }

    public String getStandingOrderName() {
        return standingOrderName;
    }

    public void setStandingOrderName(String standingOrderName) {
        this.standingOrderName = standingOrderName;
    }

}
