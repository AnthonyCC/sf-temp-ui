package com.freshdirect.webapp.ajax.login;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.webapp.ajax.analytics.data.GoogleAnalyticsData;

public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 3705858766196061464L;

    private boolean success;
    private Map<String, String> errorMessages;
    private String message;
    private String successPage;
    private GoogleAnalyticsData googleAnalyticsData;

    public LoginResponse() {
        errorMessages = new HashMap<String, String>();
    }

    public String getSuccessPage() {
        return successPage;
    }

    public void setSuccessPage(String successPage) {
        this.successPage = successPage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean isSuccess) {
        this.success = isSuccess;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addError(String errorCode, String errorDetail) {
        errorMessages.put(errorCode, errorDetail);
    }

    public void addError(ActionError actionError) {
        errorMessages.put(actionError.getType(), actionError.getDescription());
    }

    public void addErrors(Collection<ActionError> actionErrors) {
        if (null != actionErrors) {
            for (ActionError actionError : actionErrors) {
                addError(actionError);
            }
        }
    }

    public GoogleAnalyticsData getGoogleAnalyticsData() {
        return googleAnalyticsData;
    }

    public void setGoogleAnalyticsData(GoogleAnalyticsData googleAnalyticsData) {
        this.googleAnalyticsData = googleAnalyticsData;
    }

}
