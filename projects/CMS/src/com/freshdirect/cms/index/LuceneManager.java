package com.freshdirect.cms.index;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;

public class LuceneManager {

    private static final Logger LOGGER = Logger.getLogger(LuceneManager.class);

    private static final LuceneManager INSTANCE = new LuceneManager();

    private final Map<String, Object> indexWriterLocks;
    private final Map<String, IndexSearcher> indexSearchers;

    public static LuceneManager getInstance() {
        return INSTANCE;
    }

    private LuceneManager() {
        indexWriterLocks = new ConcurrentHashMap<String, Object>();
        indexSearchers = new ConcurrentHashMap<String, IndexSearcher>();
    }

    public boolean isIndexExist(Directory indexDirectory) throws IOException {
        return IndexReader.indexExists(indexDirectory);
    }

    public void writeOutDocuments(List<Document> documents, IndexWriter indexWriter, String indexPath) throws IOException {
        if (isIndexExist(indexWriter.getDirectory())) {
            for (Document document : documents) {
                indexWriter.addDocument(document);
            }
            indexWriter.commit();
        }
    }

    public void deleteIndexes(Collection<Term> terms, IndexWriter indexWriter, String indexPath) throws IOException {
        if (isIndexExist(indexWriter.getDirectory())) {
            for (Term term : terms) {
                indexWriter.deleteDocuments(term);
            }
            indexWriter.commit();
            LOGGER.debug(MessageFormat.format("Deleted {0} terms under {1} index path", terms, indexPath));
        }
    }

    public void deleteIndexes(List<BooleanQuery> deleteQueries, IndexWriter indexWriter, String indexPath) throws IOException {
        if (isIndexExist(indexWriter.getDirectory())) {
            for (BooleanQuery deleteQuery : deleteQueries) {
                indexWriter.deleteDocuments(deleteQuery);
            }
            indexWriter.commit();
            LOGGER.debug(MessageFormat.format("Deleted {0} queries under {1} index path", deleteQueries, indexPath));
        }
    }

    public void optimizeIndex(IndexWriter indexWriter) throws IOException {
        LOGGER.debug("Starting optimization process");
        indexWriter.optimize();
        LOGGER.debug("Finished optimization process");
    }

    public void createDefaultIndex(String indexPath) {
        IndexerConfiguration indexConfig = IndexerConfiguration.getDefaultConfiguration();
        indexConfig.setIndexDirectoryPath(indexPath);

        Directory indexDirectory = null;
        try {
            indexDirectory = openDirectory(indexPath);
            boolean exists = isIndexExist(indexDirectory);
            if (!exists) {
                synchronized (getWriterLock(indexPath)) {
                    IndexWriter indexWriter = null;
                    try {
                        indexWriter = openIndexToWriter(indexDirectory, indexConfig);
                    } catch (IOException e) {
                        LOGGER.error(MessageFormat.format("Exception while creating index under {0}", indexPath), e);
                        throw new CmsRuntimeException(e);
                    } finally {
                        closeIndexWriter(indexWriter);
                        removeWriterLock(indexPath);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while opening index under {0}", indexPath), e);
            throw new CmsRuntimeException(e);
        } finally {
            closeDirectory(indexDirectory);
        }
    }

    public IndexWriter openIndexToWriter(Directory indexDirectory, IndexerConfiguration config) throws IOException {
        return openIndexToWriter(indexDirectory, config.getIndexDirectoryPath(), config.getAnalyzer(), !config.isPartialIndex(), 
                config.getMaxFieldLength(), config.getIndexWriterMergeFactor(), config.getIndexWriterRamBufferSize());
    }

    public IndexWriter openIndexToWriter(Directory indexDirectory, String indexPath, Analyzer analyzer, 
            boolean created, MaxFieldLength maxFieldLength, int mergeFactor, int ramBufferSizeMb) throws IOException {
        IndexWriter indexWriter = new IndexWriter(indexDirectory, analyzer, created, maxFieldLength);
        indexWriter.setMergeFactor(mergeFactor);
        indexWriter.setRAMBufferSizeMB(ramBufferSizeMb);
        return indexWriter;
    }

    public void closeIndexWriter(IndexWriter indexWriter) {
        try {
            if (indexWriter != null) {
                indexWriter.close();
                indexWriter = null;
            }
        } catch (IOException e) {
            LOGGER.error("Exception while closing index writer", e);
        }
    }

    public TopDocs search(String indexPath, Query query, int maxHits) {
        TopDocs hits = null;
        try {
            IndexSearcher indexSearcher = getIndexSearcher(indexPath);
            hits = indexSearcher.search(query, maxHits);
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while searching from index under {0}, query {1}, maxhits {2}", indexPath, query, maxHits), e);
            throw new CmsRuntimeException(e);
        }
        return hits;
    }

    public List<Document> convertSearchHits(String indexPath, TopDocs hits, int maxHits) {
        int max = Math.min(maxHits, hits.totalHits);
        List<Document> hitDocuments = new ArrayList<Document>(max);
        try {
            IndexSearcher indexSearcher = getIndexSearcher(indexPath);
            for (int i = 0; i < max; i++) {
                hitDocuments.add(indexSearcher.doc(hits.scoreDocs[i].doc));
            }
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while converting search result from index under {0}, hits {1}, maxhits {2}", indexPath, hits, maxHits), e);
            throw new CmsRuntimeException(e);
        }
        return hitDocuments;
    }

    public int getWordFrequency(String indexPath, String word) throws IOException {
        IndexSearcher indexSearcher = getIndexSearcher(indexPath);
        return indexSearcher.docFreq(IndexingConstants.F_WORD_TERM.createTerm(word));
    }

    private IndexSearcher getIndexSearcher(String indexPath) throws IOException {
        Directory indexDirectory = null;
        IndexSearcher indexSearcher = null;
        try {
            indexSearcher = indexSearchers.get(indexPath);
            if (indexSearcher == null) {
                indexDirectory = openDirectory(indexPath);
                indexSearcher = new IndexSearcher(indexDirectory, true);
                indexSearchers.put(indexPath, indexSearcher);
            }
            return indexSearcher;
        } finally {
            closeDirectory(indexDirectory);
        }
    }

    public void closeIndexSearcher(String indexPath) throws IOException {
        IndexSearcher indexSearcher = indexSearchers.get(indexPath);
        if (indexSearcher != null) {
            indexSearcher.close();
            indexSearcher = null;
            indexSearchers.remove(indexPath);
        }
    }

    public Object getWriterLock(String indexPath) {
        Object object = indexWriterLocks.get(indexPath);
        if (object == null) {
            object = new Object();
            indexWriterLocks.put(indexPath, object);
        }
        return object;
    }

    public void removeWriterLock(String indexPath) {
        indexWriterLocks.remove(indexPath);
    }

    public Directory openDirectory(String path) throws IOException {
        return FSDirectory.open(new File(path));
    }

    public void closeDirectory(Directory directory) {
        try {
            if (directory != null) {
                directory.close();
            }
        } catch (IOException e) {
            LOGGER.error("Exception while closing index directory", e);
        }
    }
}
