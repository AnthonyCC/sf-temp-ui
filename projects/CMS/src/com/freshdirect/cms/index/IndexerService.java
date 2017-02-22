package com.freshdirect.cms.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.index.configuration.IndexConfiguration;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;
import com.freshdirect.cms.index.util.IndexDocumentCreator;
import com.freshdirect.cms.index.util.IndexerUtil;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.spell.Dictionary;
import com.freshdirect.cms.search.spell.DictionaryItem;
import com.freshdirect.framework.util.log.LoggerFactory;

public abstract class IndexerService {

    private static final int DICTIONARY_WRITER_RAM_BUFFER_SIZE = 50;
    private static final int DICTIONARY_WRITER_MERGE_FACTOR = 20;

    private final static Logger LOGGER = LoggerFactory.getInstance(IndexerService.class);

    private final IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();
    private final IndexDocumentCreator indexDocumentCreator = IndexDocumentCreator.getInstance();
    private final IndexerUtil indexerUtil = IndexerUtil.getInstance();

    /**
     * Creates an index reader
     * 
     * @param readOnly
     *            decides if the reader can modify the indexes or not
     * @param indexDirectoryPath
     *            where to open the indexReader
     * @return the IndexReader instance
     * @throws IOException
     */
    public IndexReader createReader(boolean readOnly, String indexDirectoryPath) throws IOException {
        return IndexReader.open(openIndexDirectory(indexDirectoryPath), readOnly);
    }

    /**
     * Gives a set of contentTypes which are indexed by this indexer
     * 
     * @return set of contentTypes
     */
    public Set<ContentType> getIndexedTypes() {
        return indexConfiguration.getAllIndexConfigurationsByContentType().keySet();
    }

    /**
     * @param type
     *            ContentType for which we are curious about the indexing configurations
     * @return the attribute index configurations related to the given type
     */
    public List<AttributeIndex> getIndexesForType(ContentType type) {
        return indexConfiguration.getAllIndexConfigurationsByContentType().get(type);
    }

    /**
     * creates index documents for the given contentNodes with the default indexing configurations
     * 
     * @param contentNodes
     *            the content nodes to create index documents from
     */
    public void index(Collection<ContentNodeI> contentNodes) {
        index(contentNodes, IndexerConfiguration.getDefaultConfiguration());
    }

    /**
     * Creates the indexes for a collection of content nodes with the given configuration
     * 
     * @param contentNodes
     *            the content nodes which should be indexed
     * @param indexerConfiguration
     *            the {@link IndexerConfiguration} object to use while creating the indexes
     */
    public void index(Collection<ContentNodeI> contentNodes, IndexerConfiguration indexerConfiguration) {

        indexNodes(contentNodes, indexerConfiguration);
        indexSpelling(contentNodes, indexerConfiguration);
        optimize(indexerConfiguration.getIndexDirectoryPath());

        IndexingNotifier.getInstance().sendIndexingFinishedNotification();
    }

    /**
     * Optimizes the indexes that can be found at the given location
     * 
     * @param indexDirectoryPath
     *            path to the index files
     */
    public void optimize(String indexDirectoryPath) {
        IndexWriter writer = null;
        try {
            LOGGER.debug("Starting optimization process");

            writer = new IndexWriter(openIndexDirectory(indexDirectoryPath), IndexingConstants.ANALYZER, IndexingConstants.MAX_FIELD_LENGTH_1024);
            writer.optimize();

            LOGGER.debug("Finished optimization process");
        } catch (IOException e) {
            throw new CmsRuntimeException("Exception while optimizing indexes", e);
        } finally {
            closeIndexWriter(writer);
        }
    }

    protected void closeIndexReader(IndexReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            LOGGER.error("Exception while closing index reader!", e);
        }
    }

    protected void closeIndexWriter(IndexWriter writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            LOGGER.error("Exception while closing index writer", e);
        }
    }

    /**
     * Delete the old index documents at the given path
     * 
     * @param contentNodes
     * @param indexDirectoryPath
     */
    protected abstract void deleteOldNodeIndexDocuments(Collection<ContentNodeI> contentNodes, String indexDirectoryPath);

    /**
     * Opens a {@link FSDirectory} at the specified path
     * 
     * @param indexDirectoryPath
     *            the path where the FSDirectory should be opened
     * @return the opened FSDirectory
     * @throws IOException
     *             if something goes wrong when opening the FSDirectory
     */
    protected FSDirectory openIndexDirectory(String indexDirectoryPath) throws IOException {
        FSDirectory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
        return indexDirectory;
    }

    /**
     * Indexes the data from the given {@link Dictionary}.
     * 
     * @param dictionary
     *            Dictionary to index
     * @param indexDirectoryPath
     *            where to create the indexes
     */
    private void indexDictionary(Dictionary dictionary, String indexDirectoryPath, int dictionaryWriteoutBatchSize) {
        IndexWriter writer = null;
        try {
            final Directory dir = openIndexDirectory(indexDirectoryPath);
            writer = new IndexWriter(dir, new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
            writer.setMergeFactor(DICTIONARY_WRITER_MERGE_FACTOR);
            writer.setRAMBufferSizeMB(DICTIONARY_WRITER_RAM_BUFFER_SIZE);

            deleteOldDictionaryIndexes(dictionary, writer);

            List<Document> documents = new ArrayList<Document>();
            Iterator<DictionaryItem> iter = dictionary.getWordsIterator();
            int count = 0;
            while (iter.hasNext()) {
                DictionaryItem item = iter.next();
                String searchTerm = item.getSearchTerm();
                String spellingTerm = item.getSpellingTerm();

                Document document = indexDocumentCreator.createDocumentForSpelling(searchTerm, spellingTerm, item.isSynonym());
                documents.add(document);
                if (count % dictionaryWriteoutBatchSize == 0 || !iter.hasNext()) {
                    LOGGER.debug("Writing out " + documents.size() + " dictionary items for " + indexDirectoryPath);
                    writeOutDocuments(documents, writer);
                    writer.commit();
                    documents.clear();
                }
                count++;
            }

        } catch (IOException e) {
            LOGGER.error("Exception while indexing dictionary", e);
            throw new CmsRuntimeException(e);
        } finally {
            closeIndexWriter(writer);
        }
    }

    private void deleteOldDictionaryIndexes(Dictionary dictionary, IndexWriter indexWriter) throws CorruptIndexException, IOException {
        List<BooleanQuery> deleteQueries = indexerUtil.createFindQueriesForDictionary(dictionary);
        for (BooleanQuery deleteQuery : deleteQueries) {
            indexWriter.deleteDocuments(deleteQuery);
        }
    }

    private void indexNodes(Collection<ContentNodeI> contentNodes, IndexerConfiguration indexerConfiguration) {
        deleteOldNodeIndexDocuments(contentNodes, indexerConfiguration.getIndexDirectoryPath());

        List<Document> documents = indexDocumentCreator.createDocumentsFromNodes(contentNodes, indexerConfiguration);

        writeOutDocuments(documents, indexerConfiguration.getIndexDirectoryPath());
    }

    private void indexSpelling(Collection<ContentNodeI> contentNodes, IndexerConfiguration indexerConfiguration) {
        Dictionary dictionary = indexerUtil.createDictionaryForIndexing(contentNodes, indexerConfiguration);
        indexDictionary(dictionary, indexerConfiguration.getIndexDirectoryPath(), indexerConfiguration.getDictionaryWriteoutBatchSize());
    }

    private void writeOutDocuments(List<Document> documents, IndexWriter writer) {
        for (Document document : documents) {
            try {
                writer.addDocument(document);
            } catch (IOException e) {
                LOGGER.error("Exception while writing index documents", e);
                throw new CmsRuntimeException(e);
            }
        }
    }

    private void writeOutDocuments(List<Document> documents, String indexDirectoryPath) {
        IndexWriter writer = null;
        try {
            writer = new IndexWriter(openIndexDirectory(indexDirectoryPath), IndexingConstants.ANALYZER, IndexingConstants.MAX_FIELD_LENGTH_1024);

            writeOutDocuments(documents, writer);

        } catch (IOException e) {
            LOGGER.error("Exception while writing out index", e);
            throw new CmsRuntimeException(e);
        } finally {
            closeIndexWriter(writer);
        }
    }
}
