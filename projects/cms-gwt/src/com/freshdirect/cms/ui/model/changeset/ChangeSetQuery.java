package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;
import java.util.Date;

import com.extjs.gxt.ui.client.Style.SortDir;

public class ChangeSetQuery implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String contentKey;
	private String publishId;

	private String user;
	private Date startDate;
	private Date endDate;

	private String publishType;

	private int start = 0;
	private int limit = 0;

	private boolean publishInfoQuery = false;
	private boolean changeSetQuery = false;

	private int publishMessageStart = 0;
	private int publishMessageEnd = 100;

	private int messageSeverity = -1;

	private String sortType;

	private SortDir direction = SortDir.NONE;

	private String publishSortType;
	private SortDir publishDirection = SortDir.NONE;

	private String contentType;
	private String contributor;

	public ChangeSetQuery() {
    }

    public ChangeSetQuery(ChangeSetQuery copy) {
        this.contentKey = copy.contentKey;
        this.publishId = copy.publishId;
        this.start = copy.start;
        this.limit = copy.limit;
        this.publishInfoQuery = copy.publishInfoQuery;
        this.publishMessageStart = copy.publishMessageStart;
        this.publishMessageEnd = copy.publishMessageEnd;
        this.sortType = copy.sortType;
        this.direction = copy.direction;
        this.contentType = copy.contentType;
        this.contributor = copy.contributor;        
        this.publishType = copy.publishType;
        this.publishDirection = copy.publishDirection;
        this.publishSortType = copy.publishSortType;
        this.messageSeverity = copy.messageSeverity;
        this.user = copy.user;
        this.startDate = copy.startDate;
        this.endDate = copy.endDate;
    }
    
    public String getContentKey() {
        return contentKey;
    }

    public ChangeSetQuery setContentKey(String key) {
        this.contentKey = key;
        return this;
    }

    public String getPublishId() {
        return publishId;
    }
    
    public int getMessageSeverity() {
    	return messageSeverity;
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
    
    public void setContributor(String contributor) {
        this.contributor = contributor;    
    }
    
    public String getContributor() {
        return contributor;
    }
    
    public void setContentType(String type) {
        contentType= type;    
    }
    
    public String getContentType() {
        return contentType;
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
    
	public boolean isChangeSetQuery() {
		return changeSetQuery;
	}	
	public void setChangeSetQuery( boolean changeSetQuery ) {
		this.changeSetQuery = changeSetQuery;
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
    
    public void setMessageSeverity(int severity) {
    	messageSeverity = severity;
    }
        
	public String getUser() {
		return user;
	}	
	public void setUser( String user ) {
		this.user = user;
	}
	
	public Date getStartDate() {
		return startDate;
	}	
	public void setStartDate( Date startDate ) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}	
	public void setEndDate( Date endDate ) {
		this.endDate = endDate;
	}

	public String getPublishType() {
		return publishType;
	}

	public void setPublishType(String publishType) {
		this.publishType = publishType;
	}

	@Override
    public String toString() {
        return "ChangeSetQuery[" + start + ',' + limit + (publishId != null ? " publishId:" + publishId : "") + (contentKey != null ? " contentKey:" + contentKey : "") + "]";
    }
}