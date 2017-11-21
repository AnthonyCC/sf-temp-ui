package com.freshdirect.cms.ui.editor.service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.core.service.MassLoadingStrategy;
import com.freshdirect.cms.lucene.domain.IndexerConfiguration;
import com.freshdirect.cms.ui.editor.index.domain.IndexingStrategy;
import com.freshdirect.cms.ui.editor.index.service.IndexerService;

@Service
public class BackgroundReindexer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundReindexer.class);

    @Autowired
    private ContentProviderService contentProvider;

    @Autowired
    private IndexerService indexerService;

    @Async
    public Future<String> indexContent(Set<ContentKey> keysToIndex, IndexingStrategy indexingStrategy, MassLoadingStrategy massLoadingStrategy) {
        LOGGER.info("Preparing to index " + keysToIndex.size() + " nodes, indexing type: " + indexingStrategy);
        Map<ContentKey, Map<Attribute, Object>> nodesToIndex = contentProvider.getAllAttributesForContentKeys(keysToIndex, massLoadingStrategy);
        LOGGER.info("Loaded " + nodesToIndex.size() + " nodes");
        
        // Also add 'empty' nodes, as contentprovider did not return them if they had no attributes, but we still want to index their contentkeys.
        // This happens when they were just created without any attributes, e.g. creating Sku-s via bulkloader.
        for (ContentKey key : keysToIndex) {
            if (!nodesToIndex.containsKey(key)) {
                nodesToIndex.put(key, Collections.<Attribute, Object>emptyMap());
            }
        }
        LOGGER.info("Started indexing of " + nodesToIndex.size() + " nodes, indexing type: " + indexingStrategy);
        
        IndexerConfiguration indexerConfiguration = CmsServiceLocator.indexerConfiguration();
        if (IndexingStrategy.FULL_INDEXING.equals(indexingStrategy)) {
            indexerService.fullIndex(nodesToIndex, indexerConfiguration, null);
        } else {
            indexerService.partialIndex(nodesToIndex, indexerConfiguration, null);
        }
        LOGGER.info("Writing index files finished!");
        return new AsyncResult<String>("done");
    }
}
