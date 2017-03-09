package com.freshdirect.cms.index;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class IndexingNotifier {
    
    private static final Logger LOGGER = LoggerFactory.getInstance(IndexingNotifier.class); 

    private static final IndexingNotifier INSTANCE = new IndexingNotifier();

    private List<IndexingSubscriber> subscribers = new CopyOnWriteArrayList<IndexingSubscriber>();

    public static IndexingNotifier getInstance() {
        return INSTANCE;
    }

    public void subscribe(IndexingSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(IndexingSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void sendIndexingFinishedNotification() {
        for (IndexingSubscriber subscriber : subscribers) {
            if (subscriber != null) {
                subscriber.receiveIndexingFinishedNotification();
                LOGGER.debug("Indexing finished, " + subscriber + " notified.");
            }
        }
    }
}
