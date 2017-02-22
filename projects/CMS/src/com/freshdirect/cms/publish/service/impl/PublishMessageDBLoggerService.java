package com.freshdirect.cms.publish.service.impl;

import org.apache.log4j.Logger;

import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.repository.PublishRepository;
import com.freshdirect.cms.publish.service.PublishMessageLogger;

public class PublishMessageDBLoggerService implements PublishMessageLogger {

    private static final Logger LOGGER = Logger.getLogger(PublishMessageDBLoggerService.class);

    private PublishRepository repository = new PublishRepository();

    @Override
    public void log(String publishId, PublishMessage message) {
        if (publishId != null) {

            if (message.getMessage().length() < 400) {
                try {
                    repository.savePublishMessage(publishId, message);
                } catch (Exception exc) {
                    LOGGER.error("Error occurred while writing out message '" + message.getMessage() + "'", exc);
                }
            } else {
                LOGGER.info("Discarding too long message '" + message.getMessage() + "'");
            }
        }
    }

}
