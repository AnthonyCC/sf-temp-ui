package com.freshdirect.cms.lucene.service;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.lucene.domain.IndexerConfiguration;
import com.freshdirect.cms.lucene.domain.IndexingConstants;

@Service
public final class LuceneManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneManager.class);

    private final Map<String, Object> indexWriterLocks;
    private final Map<String, IndexSearcher> indexSearchers;

    @Autowired
    private IndexerConfiguration indexConfig;

    private LuceneManager() {
        indexWriterLocks = new ConcurrentHashMap<String, Object>();
        indexSearchers = new ConcurrentHashMap<String, IndexSearcher>();
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

    public void closeIndexSearcher(String indexPath) throws IOException {
        IndexSearcher indexSearcher = indexSearchers.get(indexPath);
        if (indexSearcher != null) {
            indexSearcher.close();
            indexSearcher = null;
            indexSearchers.remove(indexPath);
        }
    }

    public void closeAllIndexSearcher() throws IOException {
        for (Iterator<String> indexPathIterator = indexSearchers.keySet().iterator(); indexPathIterator.hasNext();) {
            String indexPath = indexPathIterator.next();
            closeIndexSearcher(indexPath);
        }
    }

    public void closeIndexWriter(IndexWriter indexWriter) {
        try {
            if (indexWriter != null) {
                indexWriter.close();
            }
        } catch (IOException e) {
            LOGGER.error("Exception while closing index writer", e);
        }
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
            LOGGER.error(MessageFormat.format("Exception while converting search result from index under {0}, hits {1}, maxhits {2}", indexPath, hits.totalHits, maxHits), e);
            throw new RuntimeException(e);
        }
        return hitDocuments;
    }

    public void createDefaultIndex(String indexPath) {
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
                        throw new RuntimeException(e);
                    } finally {
                        closeIndexWriter(indexWriter);
                        removeWriterLock(indexPath);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while opening index under {0}", indexPath), e);
            throw new RuntimeException(e);
        } finally {
            closeDirectory(indexDirectory);
        }
    }

    public void deleteIndexes(Collection<Term> terms, IndexWriter indexWriter, String indexPath) throws IOException {
        if (isIndexExist(indexWriter.getDirectory())) {
            for (Term term : terms) {
                indexWriter.deleteDocuments(term);
            }
            indexWriter.commit();
            LOGGER.debug(MessageFormat.format("Deleted {0} terms under {1} index path", terms.size(), indexPath));
        }
    }

    public void deleteIndexes(List<BooleanQuery> deleteQueries, IndexWriter indexWriter, String indexPath) throws IOException {
        if (isIndexExist(indexWriter.getDirectory())) {
            for (BooleanQuery deleteQuery : deleteQueries) {
                indexWriter.deleteDocuments(deleteQuery);
            }
            indexWriter.commit();
            LOGGER.debug(MessageFormat.format("Deleted {0} queries under {1} index path", deleteQueries.size(), indexPath));
        }
    }

    public int getWordFrequency(String indexPath, String word) throws IOException {
        IndexSearcher indexSearcher = getIndexSearcher(indexPath);
        return indexSearcher.docFreq(IndexingConstants.F_WORD_TERM.createTerm(word));
    }

    public Object getWriterLock(String indexPath) {
        Object object = indexWriterLocks.get(indexPath);
        if (object == null) {
            object = new Object();
            indexWriterLocks.put(indexPath, object);
        }
        return object;
    }

    public boolean isIndexExist(Directory indexDirectory) throws IOException {
        return IndexReader.indexExists(indexDirectory);
    }

    public Directory openDirectory(String path) throws IOException {
        return FSDirectory.open(new File(path));
    }

    public IndexWriter openIndexToWriter(Directory indexDirectory, IndexerConfiguration config) throws IOException {
        return openIndexToWriter(indexDirectory, config.getIndexDirectoryPath(), config.getAnalyzer(), !config.isPartialIndex(), config.getMaxFieldLength(),
                config.getIndexWriterMergeFactor(), config.getIndexWriterRamBufferSize());
    }

    public IndexWriter openIndexToWriter(Directory indexDirectory, String indexPath, Analyzer analyzer, boolean created, MaxFieldLength maxFieldLength, int mergeFactor,
            int ramBufferSizeMb) throws IOException {
        IndexWriter indexWriter = new IndexWriter(indexDirectory, analyzer, created, maxFieldLength);
        indexWriter.setMergeFactor(mergeFactor);
        indexWriter.setRAMBufferSizeMB(ramBufferSizeMb);
        return indexWriter;
    }

    public void optimizeIndex(IndexWriter indexWriter) throws IOException {
        LOGGER.debug("Starting optimization process");
        indexWriter.optimize();
        LOGGER.debug("Finished optimization process");
    }

    public void removeWriterLock(String indexPath) {
        indexWriterLocks.remove(indexPath);
    }

    public TopDocs search(String indexPath, Query query, int maxHits) {
        TopDocs hits = null;
        try {
            IndexSearcher indexSearcher = getIndexSearcher(indexPath);
            hits = indexSearcher.search(query, maxHits);
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while searching from index under {0}, query {1}, maxhits {2}", indexPath, query, maxHits), e);
            throw new RuntimeException(e);
        }
        return hits;
    }

    public void writeOutDocuments(List<Document> documents, IndexWriter indexWriter, String indexPath) throws IOException {
        if (isIndexExist(indexWriter.getDirectory())) {
            for (Document document : documents) {
                indexWriter.addDocument(document);
            }
            indexWriter.commit();
        }
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
}
