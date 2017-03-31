package com.freshdirect.cms.search;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.SingleStoreNodeCollectionSource;
import com.freshdirect.cms.application.StoreContentSource;
import com.freshdirect.cms.index.IndexerService;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;

public class SearchTestUtils {

    public static IndexerService createIndexerService(List<ContentIndex> indexes) {
    	IndexerService indexer = IndexerService.getInstance();
        return indexer;
    }

    public static ContentSearchServiceI createSearchService(List<ContentIndex> indexes, String indexLocation) {
    	LuceneSearchService indexer = LuceneSearchService.getInstance();
    	indexer.setIndexes(indexes);
    	indexer.setIndexLocation(indexLocation);
        return indexer;
    }

    public static IndexerConfiguration createIndexConfiguration(StoreContentSource source, String indexLocation) {
		IndexerConfiguration configuration = new IndexerConfiguration();
		configuration.setKeywordsDisabled(false);
		configuration.setSynonymsDisabled(false);
		configuration.setIndexDirectoryPath(indexLocation);
		configuration.setStoreContentSource(source);
		return configuration;
	}
    
    public static StoreContentSource createStoreContentSource(Collection<ContentNodeI> nodes, ContentKey storeKey) {
    	StoreContentSource source = new SingleStoreNodeCollectionSource(nodes, storeKey);
		return source;
	}
    
    public static String createTempDir(String prefix, String suffix) throws IOException {
        File tmp = File.createTempFile(prefix, suffix);
        // String tmpName = tmp.getAbsolutePath();
        // System.out.println("ZOZO: " + tmp.getName());
        tmp.delete();
        tmp.mkdir();
        tmp.deleteOnExit();
        return tmp.getAbsolutePath();
    }
}
