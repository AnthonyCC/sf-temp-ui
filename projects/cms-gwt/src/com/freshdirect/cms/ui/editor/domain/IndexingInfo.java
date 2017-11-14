package com.freshdirect.cms.ui.editor.domain;

import java.util.Date;

public class IndexingInfo {

    private Date indexingStarted;
    private IndexingStatus indexingStatus;

    public IndexingInfo() {
        this.indexingStarted = new Date();
        indexingStatus = IndexingStatus.FINISHED_WITH_SUCCESS;
    }

    public Date getIndexingStarted() {
        return indexingStarted;
    }

    public IndexingStatus getIndexingStatus() {
        return indexingStatus;
    }

    public void setIndexingStatus(IndexingStatus indexingStatus) {
        this.indexingStatus = indexingStatus;
    }
}
