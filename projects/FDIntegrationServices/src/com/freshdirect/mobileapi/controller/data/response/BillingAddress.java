package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Address;
import com.freshdirect.mobileapi.model.PaymentMethod;

public class BillingAddress extends Address {
    
    public BillingAddress(PaymentMethod paymentMethod) {
        this.name = paymentMethod.getName();
        this.street1 = paymentMethod.getAddress1();
        this.street2 = paymentMethod.getAddress2();
        this.apartment = paymentMethod.getApartment();
        this.city = paymentMethod.getCity();
        this.state = paymentMethod.getState();
        this.postalCode = paymentMethod.getZipCode();
    }

}
