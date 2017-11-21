package com.freshdirect.cms.ui.editor.publish.feed.converter;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublish;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessage;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;

@Service
public class FeedPublishToGwtPublishDataConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedPublishToGwtPublishDataConverter.class);

    public static final DateFormat US_DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.US);

    public GwtPublishData convert(FeedPublish feedPublish) {

        boolean hasMessages = feedPublish.getMessages() != null && feedPublish.getMessages().size() > 0;

        return convert(feedPublish, hasMessages);
    }

    public GwtPublishData convert(FeedPublish feedPublish, boolean detail) {
        GwtPublishData gwtPublishData = new GwtPublishData();
        gwtPublishData.setId(feedPublish.getPublishId());
        gwtPublishData.setComment(feedPublish.getComment());
        gwtPublishData.setLastModified(feedPublish.getLastModifiedTimestamp() == null ? "" : US_DATE_FORMAT.format(feedPublish.getLastModifiedTimestamp()));
        gwtPublishData.setCreated(feedPublish.getCreationTimestamp() == null ? "" : US_DATE_FORMAT.format(feedPublish.getCreationTimestamp()));
        gwtPublishData.setPublisher(feedPublish.getUserId());
        gwtPublishData.setStatus(feedPublish.getStatus().name());

        gwtPublishData.setFullyLoaded(detail);
        if (detail) {
            for (FeedPublishMessage message : feedPublish.getMessages()) {
                switch (message.getMessageLevel()) {
                    case FAILURE:
                    case ERROR:
                    case WARNING:
                        gwtPublishData.addMessage(String.valueOf(message.getMessageLevel().getNumericMessageLevel()));
                        break;
                    default:
                        break;
                }
            }
            Collections.sort(gwtPublishData.getMessages());
        }

        return gwtPublishData;
    }

}
