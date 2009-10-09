package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;

public class ChangeSetQueryResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    List<GwtChangeSet> changes;
    List<GwtPublishMessage> publishMessages;
    int publishMessageCount;
    
    int changeSetCount;
    int changeCount;

    ChangeSetQuery query;
    
    String publishStatus;
    Date publishStart;
    long elapsedTime;
    String lastInfo;
    

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
    
    public ChangeSetQueryResponse(List<GwtChangeSet> changes, int changeSetCount, int changeCount, ChangeSetQuery query, List<GwtPublishMessage> pbMessages, int publishMessageCount) {
        super();
        this.changes = changes;
        this.changeSetCount = changeSetCount;
        this.changeCount = changeCount;
        this.query = query;
        this.publishMessages = pbMessages;
        this.publishMessageCount = publishMessageCount;
    }
    

    public ChangeSetQueryResponse(String publishStatus, Date publishStart, long elapsedTime, List<GwtPublishMessage> pbMessages, String lastInfo) {
        this.publishStatus = publishStatus;
        this.publishStart = publishStart;
        this.elapsedTime = elapsedTime;
        this.publishMessages = pbMessages;
        this.lastInfo = lastInfo;
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
    
    public int getPublishMessageCount() {
        return publishMessageCount;
    }
    
    public Date getPublishStart() {
        return publishStart;
    }
    
    public long getElapsedTime() {
        return elapsedTime;
    }
    
    public String getLastInfo() {
        return lastInfo;
    }
    
    public List<GwtPublishMessage> getPublishMessages() {
        return publishMessages;
    }
    

}
