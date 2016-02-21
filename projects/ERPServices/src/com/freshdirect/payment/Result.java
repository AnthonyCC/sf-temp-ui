package com.freshdirect.payment;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Result implements Serializable {
    /**
     * By setting these to null, they don't get generated in JSON object
     */
    public void disableResultMetaData() {
        status = null;
        warnings = null;
        errors = null;
        debug = null;
        notice = null;
    }

    public static Result createSuccessMessage(String messageString) {
        Result message = new Result();
        message.status = STATUS_SUCCESS;
        message.addDebugMessage(messageString);
        return message;
    }
    
    public static Result createFailureMessage(String messageString) {
        Result message = new Result();
        message.status = STATUS_FAILED;
        message.addErrorMessage(messageString);
        return message;
    }

    public void setSuccessMessage(String messageString) {
        status = STATUS_SUCCESS;
        addDebugMessage(messageString);
    }
    
    public void setWarningMessage(String messageString) {
        status = STATUS_WARNING;
        addWarningMessage(messageString);
    }

    public static final String STATUS_SUCCESS = "SUCCESS";
    
    public static final String STATUS_WARNING = "WARNING";

    public static final String STATUS_FAILED = "FAILED";

    private static final String GENERIC_ID = "GENERIC";

    private String status;

    private int errorCode;
    
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

    public void addWarningMessages(Collection<ActionWarning> messages) {
        for (ActionWarning message : messages) {
            this.warnings.put(GENERIC_ID + this.warnings.size(), message.getDescription());
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

    public void addErrorMessages(Collection<ActionError> messages) {
        for (ActionError message : messages) {
            this.errors.put(message.getType(), message.getDescription());
        }
    }
    
    public void addErrorMessages(ActionError message) {
    	this.errors.put(message.getType(), message.getDescription());
    }
    public void addErrorMessages(Map<String, String> messages) {
        this.errors.putAll(messages);
    }

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		final int maxLen = 5;
		return "Result ["
				+ (status != null ? "status=" + status + ", " : "")
				+ "errorCode="
				+ errorCode
				+ ", "
				+ (warnings != null ? "warnings="
						+ toString(warnings.entrySet(), maxLen) + ", " : "")
				+ (errors != null ? "errors="
						+ toString(errors.entrySet(), maxLen) + ", " : "")
				+ (debug != null ? "debug="
						+ toString(debug.entrySet(), maxLen) + ", " : "")
				+ (notice != null ? "notice="
						+ toString(notice.entrySet(), maxLen) : "") + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

}
