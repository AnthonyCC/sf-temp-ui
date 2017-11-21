package com.freshdirect.storeapi.spelling.service;

import java.io.IOException;
import java.util.List;

import com.freshdirect.storeapi.spelling.domain.SpellingHit;
import com.freshdirect.storeapi.spelling.domain.StringDistance;

public interface SpellingSuggestionsServiceI {

    /**
     * Get spelling suggestions and return them in order of relevance.
     * 
     * @param phrase
     *            word to be searched
     * @param maxHits
     *            hits to suggest for Lucene (should be at list 5)
     * @return suggested spellings (List<"{@link SpellingHit}>)
     * @throws IOException
     */
    List<SpellingHit> getSpellingHits(String indexPath, String phrase, int maxHits);

    StringDistance getStringDistance();

    void close(String indexPath);

    boolean exists(String indexPath, String phrase);
}
