package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

public class Restriction extends Message {

    private String name;

    private String message;

    public Restriction(String restrictionMessage) {
        this(null, restrictionMessage);
    }

    public Restriction(String name, String restrictionMessage) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
