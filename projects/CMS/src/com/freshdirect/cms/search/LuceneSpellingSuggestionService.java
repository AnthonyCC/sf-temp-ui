package com.freshdirect.cms.search;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.search.spell.Dictionary;
import com.freshdirect.cms.search.spell.FreshDirectDictionary;
import com.freshdirect.cms.search.spell.SpellChecker;
import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.cms.search.spell.StringDistance;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Give spelling suggestions for query terms.
 * 
 */
public class LuceneSpellingSuggestionService implements SpellingSuggestionsServiceI  {
	private final static Logger LOGGER = LoggerFactory.getInstance(LuceneSpellingSuggestionService.class);

	// spell checker instance, created at construction
	private SpellChecker checker;

	// directory of indexes, created at construction
	private Directory directory;

	private Map<ContentType, List<AttributeIndex>> indexes;

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
	public LuceneSpellingSuggestionService(String indexLocation, Map<ContentType, List<AttributeIndex>> contentIndexes) {
		try {
			directory = FSDirectory.open(new File(indexLocation));
			this.indexes = contentIndexes;
			this.checker = new SpellChecker(directory);
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#indexWords(java.util.Collection)
	 */
	public void indexTerms(Collection<ContentNodeI> contentNodes, List<SynonymDictionary> synonymDictionaries,
			List<SynonymDictionary> spellingDictionaries, boolean skipKeywords) {
		LOGGER.info("Started indexing words for spell-checker");
		Dictionary dictionary = new FreshDirectDictionary(contentNodes, indexes, synonymDictionaries,
				spellingDictionaries, skipKeywords);
		try {
			checker.indexDictionary(dictionary);
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
		LOGGER.info("Indexing words for spell-checker complete");
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#close()
	 */
	public void close() {
		try {
			checker.close();
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.search.SpellingSuggestionsServiceI#clear()
	 */
	public void clear() {
		try {
			checker.clearIndex();
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
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
