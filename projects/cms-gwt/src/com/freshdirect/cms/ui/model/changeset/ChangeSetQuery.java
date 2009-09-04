package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;

import com.extjs.gxt.ui.client.Style.SortDir;

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
    
    String sortType;
    
    SortDir direction = SortDir.NONE;
    
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
    
    public ChangeSetQuery setSortType(String sortType) {
        this.sortType = sortType;
        return this;
    }
    
    public String getSortType() {
        return sortType;
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
    
    public SortDir getDirection() {
        return direction;
    }
    
    public void setDirection(SortDir direction) {
        this.direction = direction;
    }
    
    @Override
    public String toString() {
        return "ChangeSetQuery[" + start + ',' + limit + (byId != null ? " byId:" + byId : "") + (byKey != null ? " byKey:" + byKey : "")
                + (publishId != null ? " publishId:" + publishId : "") + "]";
    }

}
