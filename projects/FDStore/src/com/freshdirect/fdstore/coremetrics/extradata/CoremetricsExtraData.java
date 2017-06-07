package com.freshdirect.fdstore.coremetrics.extradata;

import java.io.Serializable;

public class CoremetricsExtraData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6441413882038674509L;

    private String customerType;

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}
