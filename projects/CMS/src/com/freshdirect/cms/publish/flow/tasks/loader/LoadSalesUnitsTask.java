package com.freshdirect.cms.publish.flow.tasks.loader;

import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;

public final class LoadSalesUnitsTask extends ContentLoaderTask<Map<String, Set<String>>> {

    private PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    public LoadSalesUnitsTask(String publishId, Phase phase) {
        super(publishId, phase);
    }

    @Override
    public Map<String, Set<String>> call() throws Exception {
        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Loading material sales units", LoadSalesUnitsTask.class.getSimpleName()));
        return loader.fetchMaterialSalesUnits();
    }

    @Override
    public String getName() {
        return "Load Material Sales Units";
    }

}
