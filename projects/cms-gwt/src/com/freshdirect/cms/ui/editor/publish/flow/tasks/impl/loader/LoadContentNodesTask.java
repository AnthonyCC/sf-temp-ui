package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.persistence.service.CacheLoadingStrategy;
import com.freshdirect.cms.persistence.service.DatabaseContentProvider;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.PublishTask;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public final class LoadContentNodesTask extends PublishTask implements Callable<Map<ContentKey, Map<Attribute, Object>>> {

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    @Autowired
    private DatabaseContentProvider contentProvider;

    @Override
    public Map<ContentKey, Map<Attribute, Object>> call() throws Exception {
        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Loading CMS nodes", LoadContentNodesTask.class.getSimpleName()));
        Map<ContentKey, Map<Attribute, Object>> allNodes = contentProvider.loadAllWithCacheStrategy(CacheLoadingStrategy.DONT_TOUCH);
        Map<ContentKey, Map<Attribute, Object>> cloneNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (ContentKey key : allNodes.keySet()) {
            cloneNodes.put(key, new HashMap<Attribute, Object>(allNodes.get(key)));
        }
        return cloneNodes;
    }

    @Override
    public String getName() {
        return "Load Store Content Nodes";
    }

}
