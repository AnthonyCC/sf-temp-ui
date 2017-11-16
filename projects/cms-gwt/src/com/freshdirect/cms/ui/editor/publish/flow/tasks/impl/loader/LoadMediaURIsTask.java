package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublish;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.PublishTask;
import com.freshdirect.cms.ui.editor.publish.service.StorePublishService;
import com.google.common.base.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public final class LoadMediaURIsTask extends PublishTask implements Callable<List<String>> {

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private StorePublishService storePublishService;

    @Override
    public List<String> call() throws Exception {
        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Loading Media URIs", LoadMediaURIsTask.class.getSimpleName()));

        List<String> mediaUris = Collections.emptyList();

        Optional<StorePublish> optionalPublish = storePublishService.findLastSuccessfulPublish();
        if (optionalPublish.isPresent()) {
            Date timestamp = optionalPublish.get().getTimestamp();

            List<Media> deltaMedia = mediaService.findMediaNewerThan(timestamp);
            if (!deltaMedia.isEmpty()) {
                mediaUris = new ArrayList<String>();
                for (final Media mediaItem : deltaMedia) {
                    if(mediaItem.getContentKey().type != ContentType.MediaFolder){
                        mediaUris.add(mediaItem.getUri());
                    }
                }
            }
        }

        return mediaUris;
    }

    @Override
    public String getName() {
        return "Load URIs of media items updated since last publish";
    }

}
