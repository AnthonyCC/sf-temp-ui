package com.freshdirect.cms.publish;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;

/**
 * A message with a priority.
 */
public class PublishMessage implements Serializable {

    private static final long serialVersionUID = 2291496572693517014L;

    /**
     * A debug message.
     */
    public final static int DEBUG = 4;

    /**
     * An information level message.
     */
    public final static int INFO = 3;

    /**
     * A warning.
     */
    public final static int WARNING = 2;

    /**
     * An error.
     */
    public final static int ERROR = 1;

    /**
     * A severe failure.
     */
    public final static int FAILURE = 0;

    /**
     * The time the message was created or last modified.
     */
    private Date timestamp;

    /**
     * The severity level.
     */
    private int severity;

    /**
     * The message.
     */
    private String message;

    /**
     * The id of the key of the content object related to the message.
     */
    private String contentId;

    /**
     * The type of the key of the content object related to the message.
     */
    private String contentType;

    private String storeId;

    private String task;

    /**
     * Constructor. Will initialize the timestamp to the current time.
     * 
     * @param severity
     *            the severity of the message
     * @param message
     *            the message text
     * @param contentKey
     *            the key of the content related to the message.
     */
    public PublishMessage(int severity, String message, ContentKey contentKey) {
        this(severity, message, contentKey, null);
    }

    public PublishMessage(int severity, String message, ContentKey contentKey, String task) {
        this(severity, message, task);
        this.contentId = contentKey.getId();
        this.contentType = contentKey.getType().getName();
    }

    /**
     * 
     * @param severity
     *            severity of the message
     * @param message
     *            the message
     * @param storeId
     *            the related storeId
     * @param task
     *            which publish task is sending the message
     */
    public PublishMessage(int severity, String message, String storeId, String task) {
        this(severity, message, task);
        this.storeId = storeId;
    }

    public PublishMessage(int severity, String message, String task) {
        this(severity, message);
        this.task = task;
    }

    /**
     * Constructor. Will initialize the timestamp to the current time.
     * 
     * @param severity
     *            the severity of the message
     * @param message
     *            the message text
     */
    public PublishMessage(int severity, String message) {
        this();
        this.severity = severity;
        this.message = message;
    }

    /**
     * Default constructor, setting only the timestampt to current date
     */
    public PublishMessage() {
        timestamp = new Date();
    }

    /**
     * @return Returns the timestamp.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            The timestamp to set.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the severity level of the message. 0 is very bad, higher numbers are OK.
     * 
     * @return the severity level
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * Return a string representation of the messages severity.
     * 
     * @return a string representation of the messages severity.
     */
    public String getSeverityString() {
        switch (severity) {
            case FAILURE:
                return "FAILURE";
            case ERROR:
                return "ERROR";
            case WARNING:
                return "WARNING";
            case INFO:
                return "INFO";
            case DEBUG:
                return "DEBUG";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * Set the severity level of the message. 0 is very bad, higher numbers are OK.
     * 
     * @param level
     *            The level to set.
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }

    /**
     * Get the message string.
     * 
     * @return the message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Return the key of the content related to this message.
     * 
     * @return the key to the content related to this message, or null if not available.
     */
    public ContentKey getContentKey() {
        return contentType != null && contentId != null ? ContentKey.getContentKey(ContentType.get(contentType), contentId) : null;
    }

    /**
     * Set the key of the content related to this message.
     * 
     * @param contentKey
     *            the key to the content related to this message, or null if not available.
     */
    public void setContentKey(ContentKey contentKey) {
        contentId = contentKey.getId();
        contentType = contentKey.getType().getName();
    }

    /**
     * Get the id of the key of the content related to the message.
     * 
     * @return the id of the key of the content related to the message.
     */
    public String getContentId() {
        return contentId;
    }

    /**
     * Set the id of the key of the content related to the message.
     * 
     * @param contentId
     *            the id of the key of the content related to the message.
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /**
     * get the type of the key of the content related to the message.
     * 
     * @return the type of the key of the content related to the message.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Set the type of the key of the content related to the message.
     * 
     * @param contentType
     *            the type of the key of the content related to the message.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "PublishMessage[storeId: " + storeId + ", task: " + task + ", contentId: " + contentId + ", severity: " + getSeverityString() + ", message: " + message + ']';
    }

}
