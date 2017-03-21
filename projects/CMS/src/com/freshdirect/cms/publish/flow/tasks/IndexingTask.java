package com.freshdirect.cms.publish.flow.tasks;

import java.util.Collection;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.SingleStoreNodeCollectionSource;
import com.freshdirect.cms.application.StoreContentSource;
import com.freshdirect.cms.index.IndexerService;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.ConsumerTask;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;
import com.freshdirect.fdstore.EnumEStoreId;

public class IndexingTask extends ConsumerTask<Collection<ContentNodeI>> {

    private EnumEStoreId storeId;

    private String storePublishPath;

    private final PublishMessageLoggerService messageLogger = PublishMessageLoggerService.getInstance();

    private final IndexerService indexerService = IndexerService.getInstance();

    public IndexingTask(String publishId, Phase phase, String storePublishPath, EnumEStoreId storeId, Collection<ContentNodeI> input) {
        super(publishId, phase, input);
        this.storeId = storeId;
        this.storePublishPath = storePublishPath;
    }

    @Override
    public void run() {
        messageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Indexing started", storeId.getContentId(), IndexingTask.class.getSimpleName()));

        ContentKey storeKey = ContentKey.getContentKey("Store:" + storeId.getContentId());
        StoreContentSource source = new SingleStoreNodeCollectionSource(input, storeKey);

        IndexerConfiguration indexerConfiguration = createIndexerConfiguration(source, storePublishPath);
        indexerService.fullIndex(input, indexerConfiguration);

        messageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Indexing completed", storeId.getContentId(), IndexingTask.class.getSimpleName()));
    }

    @Override
    public String getName() {
        return "Indexing Task for " + storeId.getContentId() + " Store";
    }

    private IndexerConfiguration createIndexerConfiguration(StoreContentSource storeContentSource, String indexDirectory) {
        IndexerConfiguration indexerConfiguration = IndexerConfiguration.getDefaultConfiguration();
        indexerConfiguration.setIndexDirectoryPath(storePublishPath + "/index");
        indexerConfiguration.setStoreContentSource(storeContentSource);
        return indexerConfiguration;
    }

}
