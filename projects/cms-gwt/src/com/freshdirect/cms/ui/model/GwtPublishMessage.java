package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.Date;

public class GwtPublishMessage implements Serializable {

    // the ordinals are important, check the constants in PublishMessage. 
    public enum Level {
        FAILURE, ERROR, WARNING, INFO, DEBUG
    }

    /**
     *  The time the message was created or last modified.
     */
    private Date timestamp;
    
    /**
     *  The severity level.
     */
    private Level severity;
    
    /**
     * The message.
     */
    private String message;
    
    /**
     *  The id of the key of the content object related to the message.
     */
    private String contentId;
    
    /**
     *  The type of the key of the content object related to the message.
     */
    private String contentType;
    
    
    public GwtPublishMessage() {
    }

    public Date getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    public Level getSeverity() {
        return severity;
    }


    public void setSeverity(Level severity) {
        this.severity = severity;
    }
    
    public void setSeverity(int severity) {
        this.severity = Level.values()[severity];
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public String getContentId() {
        return contentId;
    }


    public void setContentId(String contentId) {
        this.contentId = contentId;
    }


    public String getContentType() {
        return contentType;
    }


    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    
    
}
