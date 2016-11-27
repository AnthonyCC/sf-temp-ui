package com.freshdirect.common.address;


public interface AddressI extends BasicAddressI {

    public void setAddress1(String address1);
    public void setAddress2(String address);
    public void setApartment(String apartment);
    public void setCity(String city);
    public void setState(String state);
    public void setZipCode(String zipCode);
    public void setCountry(String country);

}
