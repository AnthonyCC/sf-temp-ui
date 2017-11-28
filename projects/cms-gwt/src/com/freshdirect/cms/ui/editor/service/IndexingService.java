package com.freshdirect.cms.ui.editor.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.core.service.MassLoadingStrategy;
import com.freshdirect.cms.lucene.domain.ContentIndex;
import com.freshdirect.cms.lucene.domain.IndexConfiguration;
import com.freshdirect.cms.ui.editor.domain.IndexingInfo;
import com.freshdirect.cms.ui.editor.domain.IndexingStatus;
import com.freshdirect.cms.ui.editor.index.domain.IndexingStrategy;
import com.freshdirect.cms.ui.editor.index.domain.SynonymDictionary;
import com.freshdirect.cms.ui.editor.index.service.IndexerUtil;
import com.freshdirect.cms.ui.editor.search.domain.SearchHit;
import com.freshdirect.cms.ui.editor.search.service.LuceneSearchService;

@Service
public class IndexingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexingService.class);

    @Autowired
    private ContentProviderService contentProvider;

    @Autowired
    private BackgroundReindexer backgroundReindexer;

    private IndexingInfo lastIndexingInfo = null;

    @Autowired
    private LuceneSearchService searchService;

    public void indexAll() {
        if (lastIndexingInfo != null && IndexingStatus.IN_PROGRESS.equals(lastIndexingInfo.getIndexingStatus())) {
            LOGGER.info("A full index is already in progress. Not doing anyting");
            return;
        }
        LOGGER.info("Starting full indexing");
        lastIndexingInfo = new IndexingInfo();
        lastIndexingInfo.setIndexingStatus(IndexingStatus.IN_PROGRESS);

        Set<ContentType> typesToIndex = new HashSet<ContentType>();
        for (ContentIndex indexRule : IndexConfiguration.getInstance().getIndexConfiguration()) {
            typesToIndex.add(indexRule.getContentType());
        }

        Set<ContentKey> contentToIndex = collectIndexableKeys(typesToIndex);
        try {
            Future<String> indexingResult = backgroundReindexer.indexContent(contentToIndex, IndexingStrategy.FULL_INDEXING, MassLoadingStrategy.LOAD_ALL_AND_SELECT_NEEDED);
            String result = indexingResult.get(); // this will wait the future to be completed
        } catch (Exception e) {
            lastIndexingInfo.setIndexingStatus(IndexingStatus.FAILED);
        }
        lastIndexingInfo.setIndexingStatus(IndexingStatus.FINISHED_WITH_SUCCESS);
    }

    public void indexChanged(Map<ContentKey, Map<Attribute, Object>> changedNodes) {
        LOGGER.info("Preparing for partial indexing, changed nodes size: " + changedNodes.size());
        Set<ContentKey> keysToReindex = new HashSet<ContentKey>(changedNodes.keySet());

        for (ContentKey nodeKey : changedNodes.keySet()) {
            if (ContentType.Synonym.equals(nodeKey.type)) {
                Set<String> keywords = new HashSet<String>();
                String[] fromValues = SynonymDictionary.getSynonymFromValues(changedNodes.get(nodeKey));
                keywords.addAll(Arrays.asList(fromValues));

                Set<SearchHit> searchResults = new HashSet<SearchHit>();
                for (String keyword : keywords) {
                    searchResults.addAll(searchService.search(keyword, true, 100));
                }
                for (SearchHit searchHit : searchResults) {
                    keysToReindex.add(searchHit.getContentKey());
                }
            } else if (IndexerUtil.SYNONYM_KEY.equals(nodeKey)) {
                LOGGER.info("List of synonyms has been changed, doing full indexing instead.");
                indexAll();
                return;
            }
        }

        LOGGER.info("Doing partial indexing, number of nodes to reindex: " + keysToReindex.size());
        backgroundReindexer.indexContent(keysToReindex, IndexingStrategy.PARTIAL_INDEXING, MassLoadingStrategy.LOAD_NEEDED);
    }

    public IndexingInfo getIndexingStatus() {
        return lastIndexingInfo;
    }

    private Set<ContentKey> collectIndexableKeys(Collection<ContentType> types) {
        Set<ContentKey> contentToIndex = new HashSet<ContentKey>();

        for (ContentKey contentKey : contentProvider.getContentKeys()) {
            if (types.contains(contentKey.type)) {
                contentToIndex.add(contentKey);
            }
        }

        return contentToIndex;
    }

}
