package com.freshdirect.cms.publish.flow.tasks.loader;

import java.util.Collection;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;

public final class LoadContentNodesTask extends ContentLoaderTask<Collection<ContentNodeI>> {

    private PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    public LoadContentNodesTask(String publishId, Phase phase) {
        super(publishId, phase);
    }

    @Override
    public Collection<ContentNodeI> call() throws Exception {
        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Loading CMS nodes", LoadContentNodesTask.class.getSimpleName()));
        return loader.fetchAllContentNodes();
    }

    @Override
    public String getName() {
        return "Load Store Content Nodes";
    }

}
