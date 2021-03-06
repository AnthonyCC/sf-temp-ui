package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.loader;

import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.persistence.service.ERPSDataService;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.PublishTask;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public final class LoadSalesUnitsTask extends PublishTask implements Callable<Map<String, Map<String, String>>> {

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    @Autowired
    private ERPSDataService erpsService;

    @Override
    public Map<String, Map<String, String>> call() throws Exception {
        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Loading material sales units", LoadSalesUnitsTask.class.getSimpleName()));

        return erpsService.fetchSalesUnits();
    }

    @Override
    public String getName() {
        return "Load Material Sales Units";
    }

}
