package com.freshdirect.cms.ui.editor.publish.feed.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessage;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;

@Service
public class FeedPublishMessageToGwtPublishMessageConverter {

    public GwtPublishMessage toPublishMessage(FeedPublishMessage publishMessage) {
        GwtPublishMessage gwtPublishMessage = new GwtPublishMessage();
        ContentKey contentKey = publishMessage.getContentKey();
        if (contentKey != null) {
            gwtPublishMessage.setKey(contentKey.toString());
        }
        gwtPublishMessage.setMessage(publishMessage.getMessage());
        gwtPublishMessage.setTimestamp(publishMessage.getTimestamp());
        gwtPublishMessage.setSeverity(publishMessage.getMessageLevel().name());
        gwtPublishMessage.setStoreId(publishMessage.getStoreId());
        gwtPublishMessage.setTask(publishMessage.getTask());
        return gwtPublishMessage;
    }

    public List<GwtPublishMessage> toPublishMessages(List<FeedPublishMessage> publishMessages, int start, int end) {
        start = Math.max(start, 0);
        end = Math.min(end, publishMessages.size());
        List<GwtPublishMessage> result = new ArrayList<GwtPublishMessage>(end - start);
        for (int i = start; i < end; i++) {
            GwtPublishMessage gwtPublishMessage = toPublishMessage(publishMessages.get(i));
            if (gwtPublishMessage != null) {
                result.add(gwtPublishMessage);
            }
        }
        return result;
    }
}
