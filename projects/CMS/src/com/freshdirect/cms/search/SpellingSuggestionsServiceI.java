package com.freshdirect.cms.search;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.cms.search.spell.StringDistance;

public interface SpellingSuggestionsServiceI {
	/**
	 * Index specifically the words for spell-checking of the index files used.
	 * 
	 * @param contentNodes
	 *            index reader
	 * @param synonymDictionaries
	 *            list of synonym dictionaries to be applied during the indexing process
	 * @param synonymDictionaries
	 *            list of spelling synonym dictionaries to be applied during the indexing process
	 * 
	 * @throws IOException
	 */
	public abstract void indexTerms(Collection<ContentNodeI> contentNodes, List<SynonymDictionary> synonymDictionaries, 
			List<SynonymDictionary> spellingDictionaries, boolean skipKeywords);

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

	public abstract void clear();

	public abstract boolean exists(String phrase);
}