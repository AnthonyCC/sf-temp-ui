package com.freshdirect.storeapi.spelling.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.spelling.domain.SpellingHit;
import com.freshdirect.storeapi.spelling.domain.StringDistance;

/**
 * Give spelling suggestions for query terms.
 */
@Service
public class LuceneSpellingSuggestionService implements SpellingSuggestionsServiceI {

    private final static Logger LOGGER = LoggerFactory.getInstance(LuceneSpellingSuggestionService.class);

    @Autowired
    private SpellChecker checker;

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#getSpellingHits(java.lang.String, int)
     */
    @Override
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
    @Override
    public void close(String indexPath) {
        try {
            if (this.checker != null) {
                checker.close(indexPath);
            }
        } catch (IOException e) {
            LOGGER.error("failed to close spell checker", e);
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#exists(java.lang.String)
     */
    @Override
    public boolean exists(String indexPath, String phrase) {
        try {
            return checker.exist(indexPath, phrase);
        } catch (IOException e) {
            LOGGER.error("failed to exists spell checker", e);
            throw new RuntimeException(e);
        }
    }
}
