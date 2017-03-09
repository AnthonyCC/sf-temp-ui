package com.freshdirect.cms.search;

import java.io.IOException;
import java.util.List;

import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.cms.search.spell.StringDistance;

public interface SpellingSuggestionsServiceI {
	/**
	 * Get spelling suggestions and return them in order of relevance.
	 * 
	 * @param phrase word to be searched
	 * @param maxHits hits to suggest for Lucene (should be at list 5)
	 * @return suggested spellings (List<"{@link SpellingHit}>)
	 * @throws IOException 
	 */
	public abstract List<SpellingHit> getSpellingHits(String phrase, int maxHits);
	
	public abstract StringDistance getStringDistance();

	public abstract void close();

	public abstract boolean exists(String phrase);
}