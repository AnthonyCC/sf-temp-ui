package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;

public class ChangeSetQueryResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    List<GwtChangeSet> changes;

    int changeSetCount;
    int changeCount;

    ChangeSetQuery query;
    
    String publishStatus;
    Date publishStart;
    long elapsedTime;

    public ChangeSetQueryResponse() {
    }

    public ChangeSetQueryResponse(GwtChangeSet onlyOneChange) {
        this.changes = Collections.singletonList(onlyOneChange);
        this.changeSetCount = 1;
        this.changeCount = onlyOneChange.length();
    }

    public ChangeSetQueryResponse(List<GwtChangeSet> changes, int changeSetCount, int changeCount, ChangeSetQuery query) {
        super();
        this.changes = changes;
        this.changeSetCount = changeSetCount;
        this.changeCount = changeCount;
        this.query = query;
    }
    
    

    public ChangeSetQueryResponse(String publishStatus, Date publishStart, long elapsedTime) {
        this.publishStatus = publishStatus;
        this.publishStart = publishStart;
        this.elapsedTime = elapsedTime;
    }

    public List<GwtChangeSet> getChanges() {
        return changes;
    }

    public int getChangeSetCount() {
        return changeSetCount;
    }

    public int getChangeCount() {
        return changeCount;
    }

    public ChangeSetQuery getQuery() {
        return query;
    }
    
    public String getPublishStatus() {
        return publishStatus;
    }
    
    public Date getPublishStart() {
        return publishStart;
    }
    
    public long getElapsedTime() {
        return elapsedTime;
    }

}
