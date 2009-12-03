package com.freshdirect.mobileapi.controller.data;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.MessageCodes.ErrorCodeTranslator;
import com.freshdirect.mobileapi.model.MessageCodes.ErrorMessage;

public class Message implements DateFormat, Cloneable {

    protected final SimpleDateFormat formatter = new SimpleDateFormat(STANDARDIZED_DATE_FORMAT);

    /**
     * By setting these to null, they don't get generated in JSON object
     */
    public void disableMessageMetaData() {
        status = null;
        warnings = null;
        errors = null;
        debug = null;
        notice = null;
    }

    public static Message createSuccessMessage(String messageString) {
        Message message = new Message();
        message.status = STATUS_SUCCESS;
        message.addDebugMessage(messageString);
        return message;
    }

    public void setSuccessMessage(String messageString) {
        status = STATUS_SUCCESS;
        addDebugMessage(messageString);
    }

    public static final String STATUS_SUCCESS = "SUCCESS";

    public static final String STATUS_FAILED = "FAILED";

    private static final String GENERIC_ID = "GENERIC";

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Map<String, String> warnings = new HashMap<String, String>();

    private Map<String, String> errors = new HashMap<String, String>();

    private Map<String, String> debug = new HashMap<String, String>();

    private Map<String, Object> notice = new HashMap<String, Object>();

    public Map<String, Object> getNotice() {
        return notice;
    }

    public Map<String, String> getDebug() {
        return debug;
    }

    public Map<String, String> getWarnings() {
        return warnings;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setWarnings(Map<String, String> warnings) {
        this.warnings = warnings;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public void addNoticeMessage(String id, String message) {
        this.notice.put(id, message);
    }

    public void addNoticeMessage(String message) {
        this.notice.put(GENERIC_ID + this.debug.size(), message);
    }

    public void addNoticeMessages(Map<String, Object> messages) {
        this.notice.putAll(messages);
    }

    protected boolean translateMessage(String id, String desc, SessionUser user, Map<String, String> messages) {
        ErrorMessage translatedErrorMessage = ErrorCodeTranslator.translate(id, desc, user);
        boolean translated = false;
        if (translatedErrorMessage != null) {
            messages.put(translatedErrorMessage.getKey(), translatedErrorMessage.getMessage());
            translated = true;
        }
        return translated;
    }

    public void addDebugMessage(String id, String message) {
        this.debug.put(id, message);
    }

    public void addDebugMessage(String message) {
        this.debug.put(GENERIC_ID + this.debug.size(), message);
    }

    public void addWarningMessage(String id, String message) {
        this.warnings.put(id, message);
    }

    public void addWarningMessage(String message) {
        this.warnings.put(GENERIC_ID + this.warnings.size(), message);
    }

    public void addWarningMessages(Collection<String> messages) {
        for (String message : messages) {
            this.warnings.put(GENERIC_ID + this.warnings.size(), message);
        }
    }

    public void addWarningMessages(Map<String, String> messages) {
        this.warnings.putAll(messages);
    }

    public void addErrorMessage(String id, String message) {
        this.errors.put(id, message);
    }

    public void addErrorMessage(String message) {
        this.errors.put(GENERIC_ID + this.errors.size(), message);
    }

    public void addErrorMessages(Collection<ActionError> messages, SessionUser user) {
        for (ActionError message : messages) {
            if (!translateMessage(message.getType(), message.getDescription(), user, this.errors)) {
                this.errors.put(message.getType(), message.getDescription());
            }
        }
    }

    public void addErrorMessages(Map<String, String> messages) {
        this.errors.putAll(messages);
    }

}
