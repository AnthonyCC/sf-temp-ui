package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;

public class ChangeSetQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    String byId;
    
    String byKey;

    String publishId;
    
    int start = 0;
    
    int limit = 0;
    
    boolean publishInfoQuery = false;
    
    public String getById() {
        return byId;
    }

    public ChangeSetQuery setById(String byId) {
        this.byId = byId;
        return this;
    }

    public String getByKey() {
        return byKey;
    }

    public ChangeSetQuery setByKey(String key) {
        this.byKey = key;
        return this;
    }

    public String getPublishId() {
        return publishId;
    }

    public ChangeSetQuery setPublishId(String publishId) {
        this.publishId = publishId;
        return this;
    }
    
    public ChangeSetQuery setRange(int start, int limit) {
        this.start = start;
        this.limit = limit;
        return this;
    }
    
    public int getLimit() {
        return limit;
    }
    
    public int getStart() {
        return start;
    }
    
    public void setPublishInfoQuery(boolean publishInfoQuery) {
        this.publishInfoQuery = publishInfoQuery;
    }
    
    public boolean isPublishInfoQuery() {
        return publishInfoQuery;
    }
    
    @Override
    public String toString() {
        return "ChangeSetQuery[" + start + ',' + limit + (byId != null ? " byId:" + byId : "") + (byKey != null ? " byKey:" + byKey : "")
                + (publishId != null ? " publishId:" + publishId : "") + "]";
    }

}
