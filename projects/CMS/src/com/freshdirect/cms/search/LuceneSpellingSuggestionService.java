package com.freshdirect.cms.search;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.index.IndexingNotifier;
import com.freshdirect.cms.index.IndexingSubscriber;
import com.freshdirect.cms.search.spell.SpellChecker;
import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.cms.search.spell.StringDistance;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Give spelling suggestions for query terms.
 * 
 */
public class LuceneSpellingSuggestionService implements SpellingSuggestionsServiceI, IndexingSubscriber {

    private final static Logger LOGGER = LoggerFactory.getInstance(LuceneSpellingSuggestionService.class);

    // spell checker instance, created at construction
    private SpellChecker checker;

    // directory of indexes, created at construction
    private Directory directory;

    @Override
    public void receiveIndexingFinishedNotification() {
        initializeSpellChecker(directory);
    }

    /**
     * Constructor.
     * 
     * @param indexLocation
     *            directory location of indices
     * @param contentIndexes
     *            index fields to use
     * @param reader
     *            index reader
     * @throws IOException
     */
    public LuceneSpellingSuggestionService(String indexLocation) {
        try {
            directory = FSDirectory.open(new File(indexLocation));
            initializeSpellChecker(directory);
        } catch (IOException e) {
            throw new CmsRuntimeException(e);
        }
        IndexingNotifier.getInstance().subscribe(this);
    }

    private void initializeSpellChecker(Directory indexLocation) {
        try {
            this.checker = new SpellChecker(directory);
        } catch (IOException e) {
            throw new CmsRuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#getSpellingHits(java.lang.String, int)
     */
    public List<SpellingHit> getSpellingHits(String phrase, int maxHits) {
        try {
            return checker.suggestSimilar(phrase, maxHits);
        } catch (IOException e) {
            LOGGER.error("failed to retrieve spelling hits for '" + phrase + "'", e);
            return Collections.emptyList();
        }
    }

    @Override
    public StringDistance getStringDistance() {
        return checker.getStringDistance();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#close()
     */
    public void close() {
        try {
            checker.close();
        } catch (IOException e) {
            throw new CmsRuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#exists(java.lang.String)
     */
    public boolean exists(String phrase) {
        try {
            return checker.exist(phrase);
        } catch (IOException e) {
            throw new CmsRuntimeException(e);
        }
    }
}
