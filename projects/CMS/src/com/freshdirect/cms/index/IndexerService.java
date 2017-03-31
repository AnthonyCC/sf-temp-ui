package com.freshdirect.cms.index;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.store.Directory;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.index.configuration.IndexConfiguration;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;
import com.freshdirect.cms.index.util.IndexDocumentCreator;
import com.freshdirect.cms.index.util.IndexerUtil;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.configuration.SearchServiceConfiguration;
import com.freshdirect.cms.search.spell.Dictionary;
import com.freshdirect.cms.search.spell.DictionaryItem;
import com.freshdirect.framework.util.log.LoggerFactory;

public class IndexerService {

    private final static Logger LOGGER = LoggerFactory.getInstance(IndexerService.class);

    private final IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();
    private final IndexDocumentCreator indexDocumentCreator = IndexDocumentCreator.getInstance();
    private final IndexerUtil indexerUtil = IndexerUtil.getInstance();
    private final LuceneManager luceneManager = LuceneManager.getInstance();

    private static final IndexerService INSTANCE = new IndexerService();

    public static IndexerService getInstance() {
        return INSTANCE;
    }

    private IndexerService() {
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
     * Creates the indexes for a collection of content nodes with the given configuration
     * 
     * @param contentNodes
     *            the content nodes which should be indexed
     * @param indexerConfiguration
     *            the {@link IndexerConfiguration} object to use while creating the indexes
     */
    public void fullIndex(Collection<ContentNodeI> contentNodes, IndexerConfiguration config) {
        config.setPartialIndex(false);
        index(contentNodes, config);
    }

    /**
     * Creates the partial indexes for a collection of content nodes with the given configuration
     * 
     * @param contentNodes
     *            the content nodes which should be indexed
     * @param indexerConfiguration
     *            the {@link IndexerConfiguration} object to use while creating the indexes
     */
    public void partialIndex(Collection<ContentNodeI> contentNodes, IndexerConfiguration config) {
        config.setPartialIndex(true);
        index(contentNodes, config);
    }

    private void createSpellCheck(Dictionary dictionary, IndexerConfiguration config, IndexWriter indexWriter) throws IOException {
        List<Document> documents = new ArrayList<Document>();
        Iterator<DictionaryItem> iter = dictionary.getWordsIterator();
        int count = 0;
        while (iter.hasNext()) {
            DictionaryItem item = iter.next();
            documents.add(indexDocumentCreator.createDocumentForSpelling(item.getSearchTerm(), item.getSpellingTerm(), item.isSynonym()));
            if (count % config.getDictionaryWriteoutBatchSize() == 0 || !iter.hasNext()) {
                LOGGER.debug("Writing out " + documents.size() + " dictionary items for " + config.getIndexDirectoryPath());
                luceneManager.writeOutDocuments(documents, indexWriter, config.getIndexDirectoryPath());
                documents.clear();
            }
            count++;
        }
    }

    private void deleteSpellCheck(Dictionary dictionary, IndexerConfiguration config, IndexWriter indexWriter) throws IOException {
        List<BooleanQuery> deleteQueries = indexerUtil.createFindQueriesForDictionary(dictionary);
        luceneManager.deleteIndexes(deleteQueries, indexWriter, config.getIndexDirectoryPath());
    }

    private void createSynonymIndex(Collection<ContentNodeI> contentNodes, IndexerConfiguration config, IndexWriter indexWriter) throws IOException {
        List<Document> documents = indexDocumentCreator.createDocumentsFromNodes(contentNodes, config);
        luceneManager.writeOutDocuments(documents, indexWriter, config.getIndexDirectoryPath());
    }

    private void deleteSynonymIndex(Collection<ContentNodeI> contentNodes, IndexerConfiguration config, IndexWriter indexWriter) throws IOException {
        List<Term> deleteTerms = new ArrayList<Term>();
        for (ContentNodeI node : contentNodes) {
            deleteTerms.add(new Term(IndexingConstants.FIELD_CONTENT_KEY, node.getKey().getEncoded()));
        }
        luceneManager.deleteIndexes(deleteTerms, indexWriter, config.getIndexDirectoryPath());
    }

    private void index(Collection<ContentNodeI> contentNodes, IndexerConfiguration config) {
        String indexPath = config.getIndexDirectoryPath();

        try {
            synchronized (luceneManager.getWriterLock(indexPath)) {
                Directory indexDirectory = null;
                IndexWriter indexWriter = null;
                try {
                    indexDirectory = luceneManager.openDirectory(indexPath);
                    indexWriter = luceneManager.openIndexToWriter(indexDirectory, config);

                    if (config.isPartialIndex()) {
                        // in case of full index, indexwriter creates new empty index, no need to delete documents
                        deleteSynonymIndex(contentNodes, config, indexWriter);
                    }
                    createSynonymIndex(contentNodes, config, indexWriter);

                    Dictionary dictionary = indexerUtil.createDictionaryForIndexing(contentNodes, config);
                    if (config.isPartialIndex()) {
                        // in case of full index, indexwriter creates new empty index, no need to delete documents
                        deleteSpellCheck(dictionary, config, indexWriter);
                    }
                    createSpellCheck(dictionary, config, indexWriter);

                    luceneManager.optimizeIndex(indexWriter);
                } catch (IOException e) {
                    LOGGER.error(MessageFormat.format("Exception while update index of {0} path with {1} content nodes", indexPath, contentNodes), e);
                    throw new CmsRuntimeException(e);
                } finally {
                    luceneManager.closeIndexWriter(indexWriter);
                    luceneManager.closeDirectory(indexDirectory);
                }
            }
            if (SearchServiceConfiguration.getInstance().getCmsIndexLocation().equals(indexPath)) {
                luceneManager.closeIndexSearcher(indexPath);
            }
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while closing index searcher of {0} path", indexPath), e);
            throw new CmsRuntimeException(e);
        } finally {
            luceneManager.removeWriterLock(indexPath);
        }
    }

}
