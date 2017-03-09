package com.freshdirect.cms.publish.flow.tasks.loader;

import java.util.Map;

import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;

public final class LoadSkuMaterialAssociationsTask extends ContentLoaderTask<Map<String, String>> {

    private PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    public LoadSkuMaterialAssociationsTask(String publishId, Phase phase) {
        super(publishId, phase);
    }

    @Override
    public Map<String, String> call() throws Exception {
        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Loading Sku-Material associations", LoadSkuMaterialAssociationsTask.class.getSimpleName()));
        return loader.fetchSkuMaterialAssociations();
    }

    @Override
    public String getName() {
        return "Load all SKU-Material associations";
    }
}
