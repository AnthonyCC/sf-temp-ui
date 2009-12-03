package com.freshdirect.mobileapi.controller.data.response;

import java.util.Map;

import com.freshdirect.mobileapi.controller.data.Message;

public class ContactUsInit extends Message {

    private Map<String, String> subjects;

    private Map<String, String> previousOrders;

    private String contactUsNumber;

    public ContactUsInit(Map<String, String> subjects, Map<String, String> previousOrders, String contactUsNumber) {
        this.subjects = subjects;
        this.previousOrders = previousOrders;
        this.contactUsNumber = contactUsNumber;
    }

    public Map<String, String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Map<String, String> subjects) {
        this.subjects = subjects;
    }

    public Map<String, String> getPreviousOrders() {
        return previousOrders;
    }

    public void setPreviousOrders(Map<String, String> previousOrders) {
        this.previousOrders = previousOrders;
    }

    public String getContactUsNumber() {
        return contactUsNumber;
    }

    public void setContactUsNumber(String contactUsNumber) {
        this.contactUsNumber = contactUsNumber;
    }

}
