package com.freshdirect.cms.ui.editor.publish.flow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.repository.StorePublishMessageRepository;

@Service
@Transactional
public class PublishMessageDBLoggerService implements PublishMessageLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishMessageDBLoggerService.class);

    @Autowired
    private StorePublishMessageRepository repository;

    @Override
    public void log(Long publishId, StorePublishMessage message) {
        if (publishId != null) {

            if (message.getMessage().length() < 400) {
                try {
                    StorePublishMessage lastMessage = repository.findLastMessageForPublish(publishId);
                    int sortOrder = 0;
                    if (lastMessage != null) {
                        sortOrder = lastMessage.getSortOrder() + 1;
                    }

                    message.setPublishId(publishId);
                    message.setSortOrder(sortOrder);

                    repository.save(message);

                } catch (Exception exc) {
                    LOGGER.error("Error occurred while writing out message '" + message.getMessage() + "'", exc);
                }
            } else {
                LOGGER.info("Discarding too long message '" + message.getMessage() + "'");
            }
        }
    }

}
