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
    
    int publishMessageStart = 0;

    int publishMessageEnd = 100;

    
    String sortType;
    
    SortDir direction = SortDir.NONE;
    
    String publishSortType;
    SortDir publishDirection = SortDir.NONE;
    
    public ChangeSetQuery() {
    }
    
    public ChangeSetQuery(ChangeSetQuery copy) {
        this.byId = copy.byId;
        this.byKey = copy.byKey;
        this.publishId = copy.publishId;
        this.start = copy.start;
        this.limit = copy.limit;
        this.publishInfoQuery = copy.publishInfoQuery;
        this.publishMessageStart = copy.publishMessageStart;
        this.publishMessageEnd = copy.publishMessageEnd;
        this.sortType = copy.sortType;
        this.direction = copy.direction;
        
        this.publishDirection = copy.publishDirection;
        this.publishSortType = copy.publishSortType;
    }
    
    
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
    
    public ChangeSetQuery setPublishMessageRange(int start, int limit) {
        this.publishMessageStart = start;
        this.publishMessageEnd = start + limit;
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
    
    public int getPublishMessageStart() {
        return publishMessageStart;
    }
    
    public int getPublishMessageEnd() {
        return publishMessageEnd;
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

    public SortDir getPublishDirection() {
        return publishDirection;
    }
    
    public void setPublishDirection(SortDir publishDirection) {
        this.publishDirection = publishDirection;
    }
    
    public void setPublishSortType(String publishSortType) {
        this.publishSortType = publishSortType;
    }
    
    public String getPublishSortType() {
        return publishSortType;
    }
    
    @Override
    public String toString() {
        return "ChangeSetQuery[" + start + ',' + limit + (byId != null ? " byId:" + byId : "") + (byKey != null ? " byKey:" + byKey : "")
                + (publishId != null ? " publishId:" + publishId : "") + "]";
    }

}
