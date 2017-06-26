package com.freshdirect.webapp.ajax.analytics.service;

import com.freshdirect.webapp.ajax.analytics.data.GASignupData;

public class GASignupDataService {

    private static final GASignupDataService INSTANCE = new GASignupDataService();

    private GASignupDataService() {

    }

    public static GASignupDataService defaultService() {
        return INSTANCE;
    }

    public GASignupData populateSignupData() {
        GASignupData data = new GASignupData();

        // if (loginSuccess != null) {
        // if (loginSuccess) {
        // data.setLoginAttempt("success");
        // } else {
        // data.setLoginAttempt("fail");
        // }
        // }

        return data;

    }

}
