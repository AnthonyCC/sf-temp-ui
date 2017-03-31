package com.freshdirect.cms.search;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.search.spell.SpellChecker;
import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.cms.search.spell.StringDistance;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Give spelling suggestions for query terms.
 */
public class LuceneSpellingSuggestionService implements SpellingSuggestionsServiceI {

    private final static Logger LOGGER = LoggerFactory.getInstance(LuceneSpellingSuggestionService.class);

    private static final LuceneSpellingSuggestionService INSTANCE = new LuceneSpellingSuggestionService();

    private SpellChecker checker;

    public static LuceneSpellingSuggestionService getInstance() {
        return INSTANCE;
    }

    private LuceneSpellingSuggestionService() {
        checker = SpellChecker.getInstance();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#getSpellingHits(java.lang.String, int)
     */
    public List<SpellingHit> getSpellingHits(String indexPath, String phrase, int maxHits) {
        try {
            return checker.suggestSimilar(indexPath, phrase, maxHits);
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
    public void close(String indexPath) {
        try {
            if (this.checker != null) {
                checker.close(indexPath);
            }
        } catch (IOException e) {
            LOGGER.error("failed to close spell checker", e);
            throw new CmsRuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#exists(java.lang.String)
     */
    public boolean exists(String indexPath, String phrase) {
        try {
            return checker.exist(indexPath, phrase);
        } catch (IOException e) {
            LOGGER.error("failed to exists spell checker", e);
            throw new CmsRuntimeException(e);
        }
    }
}
