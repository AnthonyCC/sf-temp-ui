package com.freshdirect.mobileapi.model;

import java.util.Date;


public class WebEvent {

    private String requestURI;

    private String customerId;

    private String cookie;

    private String url;

    private String queryString;

    private String eventType;

    private Date timestamp;

    private String application;

    private String server;

    private String trackingCode; // trk

    private String trackingCodeEx; // trkd

    private String source;

    private String sessionId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getTrackingCodeEx() {
        return trackingCodeEx;
    }

    public void setTrackingCodeEx(String trackingCodeEx) {
        this.trackingCodeEx = trackingCodeEx;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setRequestData(RequestData requestData) {
        setRequestURI(requestData.getRequestURI());
        setServer(requestData.getServer());
        setQueryString(requestData.getQueryString());
        setSessionId(requestData.getSessionId());
    }

}
