package com.freshdirect.cms.ui.editor.publish.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishStatus;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublish;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.repository.StorePublishRepository;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;
import com.google.common.base.Optional;

@Service
public class StorePublishService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorePublishService.class);

    private static final Date THE_EPOCH = new Date(0l);

    @Autowired
    private StorePublishRepository repository;

    @Autowired
    private AsyncStorePublishService asyncService;

    public static final DateTimeFormatter US_DATE_FORMAT = DateTimeFormat.mediumDateTime().withLocale(Locale.US);

    @Transactional
    public StorePublish startPublish(GwtUser author, String comment) {

        StorePublish publish = new StorePublish();

        final Date date = new Date();

        publish.setTimestamp(date);
        publish.setUserId(author.getName());
        publish.setStatus(StorePublishStatus.PROGRESS);
        publish.setDescription(comment);
        publish.setLastModified(date);

        publish = repository.save(publish);

        try {
            asyncService.startStorePublishFlowAsync(publish);
        } catch (InterruptedException e) {
            LOGGER.error("Failed to kick-off new publish by " + author.getName(), e);

            publish.setStatus(StorePublishStatus.FAILED);
            publish = repository.save(publish);
        }

        return publish;
    }

    @Transactional(readOnly = true)
    public List<StorePublish> publishHistory() {
        return repository.findAllByOrderByTimestampDesc();
    }

    @Transactional(readOnly = true)
    public Optional<StorePublish> findPublish(Long id) {
        StorePublish publish = null;

        if (id == null) {
            // find the latest publish
            publish = repository.getMostRecentPublish();
        } else {
            publish = repository.findOne(id);
        }

        return Optional.fromNullable(publish);
    }

    @Transactional(readOnly = true)
    public Optional<StorePublish> findLastSuccessfulPublish() {
        StorePublish publish = repository.getMostRecentPublish();
        return Optional.fromNullable(publish);
    }

    @Transactional(readOnly = true)
    public StorePublish getLastStorePublish() {
        return repository.getLastPublish();
    }

    @Transactional(readOnly = true)
    public String getLastStorePublishStatus() {
        StorePublish lastPublish = repository.getLastPublish();

        return lastPublish != null
                ? lastPublish.getStatus().name()
                : "UNKNOWN";
    }


    /**
     * Returns a tuple consisting two timestamps
     *
     * values  |         id='latest'         |       id=store publish ID
     * --------+-----------------------------+--------------------------------
     * Date[0] | Current Time                | last publish.getTimestamp();
     * Date[1] | publish[id].getTimestamp(); | publish[prev.id].getTimestamp();
     *
     * @param id publish ID or null referring to the latest publish
     * @return
     */
    public Optional<Date[]> timeInfo(Long id) {
        List<StorePublish> history = repository.findAllByOrderByTimestampDesc();

        Date timeInfo[] = null;

        if (id == null) {

            timeInfo = new Date[2];

            timeInfo[0] = new Date();
            timeInfo[1] = history.isEmpty() ? THE_EPOCH : history.get(0).getTimestamp();

        } else {

            StorePublish publish = null;
            int index = 0;

            while (index < history.size()) {
                publish = history.get(index);
                if (id.equals(publish.getId())) {

                    timeInfo = new Date[2];
                    timeInfo[0] = publish.getTimestamp();
                    timeInfo[1] = (index+1) < history.size() ? history.get(index+1).getTimestamp() : THE_EPOCH;

                    break;
                }

                index ++;
            }
            if (timeInfo == null) {
                LOGGER.error("Publish with ID " + id + " could not be found in publish history");
            }

        }

        return Optional.fromNullable(timeInfo);
    }

    public GwtPublishData toPublishData(StorePublish publish, boolean details) {
        final DateTime created = publish.getTimestamp() != null ? new DateTime(publish.getTimestamp()) : null;
        final DateTime mod = publish.getLastModified() != null ? new DateTime(publish.getLastModified()) : null;


        GwtPublishData publishData = new GwtPublishData();
        publishData.setId(publish.getId() != null ? publish.getId().toString() : null);
        publishData.setFullyLoaded(details);
        publishData.setComment(publish.getDescription());
        publishData.setPublisher(publish.getUserId());
        publishData.setCreated(created != null ? US_DATE_FORMAT.print(created) : "");
        publishData.setLastModified(mod != null ? US_DATE_FORMAT.print(mod) : "");
        publishData.setStatus(publish.getStatus().name());

        if (details) {
            for (StorePublishMessage message : publish.getMessages()) {
                switch (message.getSeverity()) {
                    case FAILURE:
                    case ERROR:
                    case WARNING:
                        publishData.addMessage(Integer.toString(message.getSeverity().code));
                        break;
                    default:
                        break;
                }
            }
            Collections.sort(publishData.getMessages());
        }

        return publishData;
    }

    public GwtPublishMessage toPublishMessage(StorePublishMessage publishMessage) {
        GwtPublishMessage gwtPublishMessage = new GwtPublishMessage();
        ContentKey contentKey = publishMessage.getContentKey();
        if (contentKey != null) {
            gwtPublishMessage.setKey(contentKey.toString());
            gwtPublishMessage.setType(contentKey.type.toString());
        }
        gwtPublishMessage.setMessage(publishMessage.getMessage());
        gwtPublishMessage.setTimestamp(publishMessage.getTimestamp());
        gwtPublishMessage.setSeverity(publishMessage.getSeverityString());
        gwtPublishMessage.setStoreId(publishMessage.getStoreId());
        gwtPublishMessage.setTask(publishMessage.getTask());
        return gwtPublishMessage;
    }

    public List<GwtPublishMessage> toPublishMessages(List<StorePublishMessage> publishMessages, int start, int end) {
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

    public List<StorePublishMessage> filterMessagesBySeverity(List<StorePublishMessage> messages, StorePublishMessageSeverity severity) {
        List<StorePublishMessage> filteredMessages = null;
        if (StorePublishMessageSeverity.UNKNOWN == severity ) {
            filteredMessages = messages;
        } else {
            filteredMessages = new ArrayList<StorePublishMessage>();
            for (StorePublishMessage message : messages) {
                if (message.getSeverity() == severity) {
                    filteredMessages.add(message);
                }
            }
        }
        return filteredMessages;
    }

    @Transactional
    public void update(StorePublish storePublish) {
        repository.save(storePublish);
    }
}
