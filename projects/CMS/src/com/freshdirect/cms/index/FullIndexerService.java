package com.freshdirect.cms.index;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentNodeI;

/**
 * IndexerService implementation which deletes all the previous index files and recreates them.
 *
 */
public class FullIndexerService extends IndexerService {

    private static final Logger LOGGER = Logger.getLogger(FullIndexerService.class);

    private static final FullIndexerService INSTANCE = new FullIndexerService();

    public static FullIndexerService getInstance() {
        return INSTANCE;
    }

    private FullIndexerService() {
    }

    /**
     * Deletes all the previous index documents
     */
    @Override
    protected void deleteOldNodeIndexDocuments(Collection<ContentNodeI> contentNodes, String indexDirectoryPath) {
        IndexWriter writer = null;
        Directory indexDirectory = null;
        try {
            indexDirectory = openIndexDirectory(indexDirectoryPath);
            if (IndexReader.indexExists(indexDirectory)) {
                writer = new IndexWriter(indexDirectory, IndexingConstants.ANALYZER, IndexingConstants.MAX_FIELD_LENGTH_1024);
                writer.deleteAll();
                writer.commit();
            }
        } catch (IOException e) {
            LOGGER.error("Exception while deleting old indexes", e);
            throw new CmsRuntimeException(e);
        } finally {
            closeIndexWriter(writer);
            closeIndexDirectory(indexDirectory);
        }
    }
}
