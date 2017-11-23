package com.freshdirect.cms.changecontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.changecontrol.domain.ContentChangedEvent;

@Component
@Profile("database")
public class ContentChangedEventListener implements ApplicationListener<ContentChangedEvent> {

    @Autowired
    private ChangePropagatorService changePropagatorService;

    @Override
    public void onApplicationEvent(ContentChangedEvent event) {
        changePropagatorService.notifyPreviewAboutChangedContent(event.getDraftContext(), event.getContentKeys());
    }
}
