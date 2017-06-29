package com.freshdirect.webapp.ajax.analytics.service;

import com.freshdirect.webapp.ajax.analytics.data.GALoginData;

public class GALoginDataService {

    private static final GALoginDataService INSTANCE = new GALoginDataService();

    private GALoginDataService() {

    }

    public static GALoginDataService defaultService() {
        return INSTANCE;
    }

    public GALoginData populateLoginData(Boolean loginSuccess) {
        GALoginData data = new GALoginData();

        if (loginSuccess != null) {
            if (loginSuccess) {
                data.setLoginAttempt("success");
            } else {
                data.setLoginAttempt("fail");
            }
        }

        return data;

    }

}