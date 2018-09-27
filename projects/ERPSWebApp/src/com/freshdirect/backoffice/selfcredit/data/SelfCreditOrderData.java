package com.freshdirect.backoffice.selfcredit.data;

import java.util.Date;

public class SelfCreditOrderData {

    private String saleId;
    private Date requestedDate;
    private Date deliveryStart;
    private Date deliveryEnd;
    private String standingOrderName;

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
