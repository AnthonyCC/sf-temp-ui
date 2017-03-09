package com.freshdirect.cms.publish.flow.tasks.loader;

import java.util.Collection;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;

public final class LoadMediaNodesTask extends ContentLoaderTask<Collection<ContentNodeI>> {

    private PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    public LoadMediaNodesTask(String publishId, Phase phase) {
        super(publishId, phase);
    }

    @Override
    public Collection<ContentNodeI> call() throws Exception {
        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Loading Media nodes", LoadMediaNodesTask.class.getSimpleName()));
        return loader.fetchAllMediaNodes();
    }

    @Override
    public String getName() {
        return "Load Media Nodes";
    }

}
