package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.EStoreId;
import com.freshdirect.cms.core.service.ContentKeyParentsCollectorService;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.core.service.NodeCollectionContentProviderService;
import com.freshdirect.cms.lucene.domain.IndexerConfiguration;
import com.freshdirect.cms.ui.editor.index.service.IndexerService;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.ConsumerTask;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IndexingTask extends ConsumerTask<Map<ContentKey, Map<Attribute, Object>>> {

    private EStoreId storeId;

    private String storePublishPath;

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    @Autowired
    private IndexerService indexerService;

    @Autowired
    private IndexerConfiguration indexerConfiguration;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private ContentKeyParentsCollectorService contentKeyParentsCollectorService;

    @Autowired
    private ContextService contextService;

    public void setStoreId(EStoreId storeId) {
        this.storeId = storeId;
    }

    public void setStorePublishPath(String storePublishPath) {
        this.storePublishPath = storePublishPath;
    }

    @Override
    public void run() {
        publishMessageLogger.log(publishId,
                new StorePublishMessage(StorePublishMessageSeverity.INFO, "Indexing started", storeId.getContentId(), IndexingTask.class.getSimpleName()));

        ContentKey storeKey = ContentKeyFactory.get(ContentType.Store, storeId.getContentId());

        // TODO: consider using nodecollection as input
        NodeCollectionContentProviderService objectsToIndex = new NodeCollectionContentProviderService(contentTypeInfoService, contentKeyParentsCollectorService, contextService, input);

        IndexerConfiguration indexerConfiguration = createIndexerConfiguration(objectsToIndex, storePublishPath);
        indexerService.fullIndex(input, indexerConfiguration, storeKey);

        publishMessageLogger.log(publishId,
                new StorePublishMessage(StorePublishMessageSeverity.INFO, "Indexing completed", storeId.getContentId(), IndexingTask.class.getSimpleName()));
    }

    @Override
    public String getName() {
        return "Indexing Task for " + storeId.getContentId() + " Store";
    }

    private IndexerConfiguration createIndexerConfiguration(ContextualContentProvider storeContentSource, String indexDirectory) {
        indexerConfiguration.setIndexDirectoryPath(storePublishPath + "/index");
        indexerConfiguration.setContentProviderService(storeContentSource);
        return indexerConfiguration;
    }

}
