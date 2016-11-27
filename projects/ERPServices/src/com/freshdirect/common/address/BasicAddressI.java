package com.freshdirect.common.address;

import java.io.Serializable;

public interface BasicAddressI extends Serializable {

    public String getAddress1();
    public String getAddress2();
    public String getApartment();
    public String getCity();
    public String getState();
    public String getZipCode();
    public String getCountry();
    
//  changes done for GeoCode 
    public AddressInfo getAddressInfo();

}
