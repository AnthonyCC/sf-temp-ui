package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.loader;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.PublishTask;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public final class LoadMediaNodesTask extends PublishTask implements Callable<List<Media>> {

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    @Autowired
    private MediaService mediaService;

    @Override
    public List<Media> call() throws Exception {
        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Loading Media nodes", LoadMediaNodesTask.class.getSimpleName()));

        return mediaService.loadAll();
    }

    @Override
    public String getName() {
        return "Load Media Nodes";
    }

}
