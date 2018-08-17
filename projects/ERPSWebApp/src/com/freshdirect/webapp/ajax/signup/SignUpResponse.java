package com.freshdirect.webapp.ajax.signup;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.webapp.ajax.analytics.data.GoogleAnalyticsData;

public class SignUpResponse implements Serializable {

    private static final long serialVersionUID = 5914440459073286113L;

    private String email;
    private String serviceType;
    private boolean success;
    private String successPage;
    private boolean skipPopup;
    private String message;
    private Map<String, String> errorMessages;
    private GoogleAnalyticsData googleAnalyticsData;

    public SignUpResponse() {
        errorMessages = new HashMap<String, String>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSuccessPage() {
        return successPage;
    }

    public void setSuccessPage(String successPage) {
        this.successPage = successPage;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public void addErrorMessage(String type, String message) {
        this.errorMessages.put(type, message);
    }

    public GoogleAnalyticsData getGoogleAnalyticsData() {
        return googleAnalyticsData;
    }

    public void setGoogleAnalyticsData(GoogleAnalyticsData googleAnalyticsData) {
        this.googleAnalyticsData = googleAnalyticsData;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSkipPopup() {
        return skipPopup;
    }

    public void setSkipPopup(boolean skipPopup) {
        this.skipPopup = skipPopup;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
