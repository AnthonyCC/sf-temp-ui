package com.freshdirect.mobileapi.controller.data;

public class PhoneNumber {

    private String phone;

    private String extension;

    public PhoneNumber() {

    }

    public PhoneNumber(String phone, String extension) {
        this.phone = phone;
        this.extension = extension;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
