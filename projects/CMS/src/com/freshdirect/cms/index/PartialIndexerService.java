package com.freshdirect.cms.index;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentNodeI;

/**
 * IndexerService implementation which only deletes the changed node related indexes and recreates them.
 *
 */
public class PartialIndexerService extends IndexerService {

    private static final Logger LOGGER = Logger.getLogger(PartialIndexerService.class);

    private static final PartialIndexerService INSTANCE = new PartialIndexerService();

    public static PartialIndexerService getInstance() {
        return INSTANCE;
    }

    private PartialIndexerService() {
    }

    /**
     * Deletes the index documents related to the param contentNodes
     * 
     * @param contentNodes
     *            collection of contentNodes which indexes should be deleted
     */
    @Override
    protected void deleteOldNodeIndexDocuments(Collection<ContentNodeI> contentNodes, Directory indexDirectory) {
        IndexReader localReader = null;
        try {
            int count = 0;
            if (IndexReader.indexExists(indexDirectory)) {
                localReader = IndexReader.open(indexDirectory, false);
                for (ContentNodeI node : contentNodes) {
                    count += localReader.deleteDocuments(new org.apache.lucene.index.Term(IndexingConstants.FIELD_CONTENT_KEY, node.getKey().getEncoded()));
                }
            }
            LOGGER.debug("Deleted " + count + " content nodes");
        } catch (IOException e) {
            LOGGER.error("Exception while deleting old indexes", e);
            throw new CmsRuntimeException(e);
        } finally {
            closeIndexReader(localReader);
        }
    }

}
