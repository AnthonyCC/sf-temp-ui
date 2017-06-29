package com.freshdirect.webapp.ajax.analytics.service;

import com.freshdirect.webapp.ajax.analytics.data.GASignupData;

public class GASignupDataService {

    private static final GASignupDataService INSTANCE = new GASignupDataService();

    private GASignupDataService() {

    }

    public static GASignupDataService defaultService() {
        return INSTANCE;
    }

    public GASignupData populateSignupData(Boolean signupSuccess) {
        GASignupData data = new GASignupData();

        if (signupSuccess != null) {
            if (signupSuccess) {
                data.setSignupAttempt("success");
            } else {
                data.setSignupAttempt("fail");
            }
        }

        return data;

    }

}
