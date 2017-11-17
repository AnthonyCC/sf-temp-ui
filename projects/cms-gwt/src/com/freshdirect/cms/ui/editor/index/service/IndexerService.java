package com.freshdirect.cms.ui.editor.index.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.lucene.domain.AttributeIndex;
import com.freshdirect.cms.lucene.domain.IndexConfiguration;
import com.freshdirect.cms.lucene.domain.IndexerConfiguration;
import com.freshdirect.cms.lucene.domain.IndexingConstants;
import com.freshdirect.cms.lucene.service.LuceneManager;
import com.freshdirect.cms.properties.service.PropertyResolverService;
import com.freshdirect.cms.ui.editor.index.domain.Dictionary;
import com.freshdirect.cms.ui.editor.index.domain.DictionaryItem;

@Service
public class IndexerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(IndexerService.class);

    private final IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();

    @Autowired
    private IndexDocumentCreator indexDocumentCreator;

    @Autowired
    private IndexerUtil indexerUtil;

    @Autowired
    private LuceneManager luceneManager;

    @Autowired
    private PropertyResolverService propertyResolverService;

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
    public void fullIndex(Map<ContentKey, Map<Attribute, Object>> contentNodes, IndexerConfiguration config, ContentKey storeKey) {
        config.setPartialIndex(false);
        index(contentNodes, config, storeKey);
    }

    /**
     * Creates the partial indexes for a collection of content nodes with the given configuration
     *
     * @param contentNodes
     *            the content nodes which should be indexed
     * @param indexerConfiguration
     *            the {@link IndexerConfiguration} object to use while creating the indexes
     */
    public void partialIndex(Map<ContentKey, Map<Attribute, Object>> contentNodes, IndexerConfiguration config, ContentKey storeKey) {
        config.setPartialIndex(true);
        index(contentNodes, config, storeKey);
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

    private void createSynonymIndex(Map<ContentKey, Map<Attribute, Object>> contentNodes, IndexerConfiguration config, IndexWriter indexWriter, ContentKey storeKey)
            throws IOException {
        List<Document> documents = indexDocumentCreator.createDocumentsFromNodes(contentNodes, config, storeKey);
        luceneManager.writeOutDocuments(documents, indexWriter, config.getIndexDirectoryPath());
    }

    private void deleteSynonymIndex(Map<ContentKey, Map<Attribute, Object>> contentNodes, IndexerConfiguration config, IndexWriter indexWriter) throws IOException {
        List<Term> deleteTerms = new ArrayList<Term>();
        for (ContentKey nodeKey : contentNodes.keySet()) {
            deleteTerms.add(new Term(IndexingConstants.FIELD_CONTENT_KEY, nodeKey.toString()));
        }
        luceneManager.deleteIndexes(deleteTerms, indexWriter, config.getIndexDirectoryPath());
    }

    private void index(Map<ContentKey, Map<Attribute, Object>> contentNodes, IndexerConfiguration config, ContentKey storeKey) {
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
                    createSynonymIndex(contentNodes, config, indexWriter, storeKey);

                    Dictionary dictionary = indexerUtil.createDictionaryForIndexing(contentNodes, config, storeKey);
                    if (config.isPartialIndex()) {
                        // in case of full index, indexwriter creates new empty index, no need to delete documents
                        deleteSpellCheck(dictionary, config, indexWriter);
                    }

                    createSpellCheck(dictionary, config, indexWriter);

                    luceneManager.optimizeIndex(indexWriter);
                } catch (IOException e) {
                    LOGGER.error(MessageFormat.format("Exception while update index of {0} path with {1} content nodes", indexPath, contentNodes.size()), e);
                    throw new RuntimeException(e);
                } finally {
                    luceneManager.closeIndexWriter(indexWriter);
                    luceneManager.closeDirectory(indexDirectory);
                }
            }

            String cmsIndexPath = propertyResolverService.getCmsIndexPath();
            if (cmsIndexPath.equals(indexPath)) {
                luceneManager.closeIndexSearcher(indexPath);
            }
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while closing index searcher of {0} path", indexPath), e);
            throw new RuntimeException(e);
        } finally {
            luceneManager.removeWriterLock(indexPath);
        }
    }

}
