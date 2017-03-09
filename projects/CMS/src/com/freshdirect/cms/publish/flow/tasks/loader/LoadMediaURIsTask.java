package com.freshdirect.cms.publish.flow.tasks.loader;

import java.util.List;

import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;

public final class LoadMediaURIsTask extends ContentLoaderTask<List<String>> {

    private PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    public LoadMediaURIsTask(String publishId, Phase phase) {
        super(publishId, phase);
    }

    @Override
    public List<String> call() throws Exception {
        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Loading Media URIs", LoadMediaURIsTask.class.getSimpleName()));
        return loader.fetchMediaURIs();
    }

    @Override
    public String getName() {
        return "Load URIs of media items updated since last publish";
    }

}
