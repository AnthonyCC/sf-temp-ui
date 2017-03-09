package com.freshdirect.cms.publish.flow.tasks.loader;

import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;

public final class LoadMaterialConfigurationsTask extends ContentLoaderTask<Map<String, Map<String, Set<String>>>> {

    private PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    public LoadMaterialConfigurationsTask(String publishId, Phase phase) {
        super(publishId, phase);
    }

    @Override
    public Map<String, Map<String, Set<String>>> call() throws Exception {
        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Loading material configurations", LoadMaterialConfigurationsTask.class.getSimpleName()));
        return loader.fetchMaterialConfigurationMap();
    }

    @Override
    public String getName() {
        return "Load Material Configurations";
    }

}
