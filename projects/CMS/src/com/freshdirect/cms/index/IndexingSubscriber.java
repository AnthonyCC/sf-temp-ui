package com.freshdirect.cms.index;

public interface IndexingSubscriber {

    /**
     * The one and only time when a notification arrives is when the indexing is finished
     */
    void receiveIndexingFinishedNotification();
}
