package com.freshdirect.mobileapi.controller.data;

import java.util.Date;

/**
 * Refers to days when the product is not available for delivey. If the product is related to several
 * SKUs a different message should be shown. 
 * @author fgarcia
 *
 */
public class ProductBlockedDays {
    private boolean blockedDays;

    private Date earliestDelivery;

    public boolean isBlockedDays() {
        return blockedDays;
    }

    public void setBlockedDays(boolean blockedDays) {
        this.blockedDays = blockedDays;
    }

    public Date getEarliestDelivery() {
        return earliestDelivery;
    }

    public void setEarliestDelivery(Date earliestDelivery) {
        this.earliestDelivery = earliestDelivery;
    }

}
