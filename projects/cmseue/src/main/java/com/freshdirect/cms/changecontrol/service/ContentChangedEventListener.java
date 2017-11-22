package com.freshdirect.cms.changecontrol.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.changecontrol.domain.ContentChangedEvent;

@Component
@Profile("database")
public class ContentChangedEventListener implements ApplicationListener<ContentChangedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentChangedEventListener.class);
    
    @Autowired
    private ChangePropagatorService changePropagatorService;

    @Override
    public void onApplicationEvent(ContentChangedEvent event) {
        LOGGER.debug("Received contentChangedEvent: " + event);
        changePropagatorService.notifyPreviewAboutChangedContent(event.getDraftContext(), event.getContentKeys());
    }
}
