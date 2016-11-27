/*
 * Created on Feb 17, 2005
 *
 */
package com.freshdirect.cms.search;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.search.spell.SpellingHit;

/**
 * Service interface for indexing and searching content.
 * 
 */
public interface ContentSearchServiceI {
	/** Field name to store content key as "ContentType:contentId" */
	public final static String FIELD_CONTENT_KEY = "__contentKey_";

	/** Field name to store content type */
	public final static String FIELD_CONTENT_TYPE = "__contentType_";

	/** Field name to store content id */
	public final static String FIELD_CONTENT_ID = "__contentId_";

	public final static String FIELD_PREFIX = "__"; // always start with two underscores

	public final static String NAME_PREFIX = "_name_";

	public final static String TEXT_PREFIX = "_text_";

	public final static String FULL_CONTENT = "FULL_CONTENT";

	/**
	 * Perform a search for content objects. This call can be used to search for various types of objects in CMS
	 * 
	 * @param query
	 *            search term (never null)
	 * @param maxHits
	 *            maximum number of search results
	 * 
	 * @return List of {@link SearchHit}
	 */
	public Collection<SearchHit> search(String query, boolean exact, int maxHits);

	/**
	 * Perform a search for products & recipes
	 * 
	 * @param query
	 *            search term (never null)
	 * @param maxHits
	 *            maximum number of search results
	 * 
	 * @return List of {@link SearchHit}
	 */
	public Collection<SearchHit> searchProducts(String query, boolean exact, boolean approximate, int maxHits);

	/**
	 * Perform a search for products & recipes
	 * 
	 * @param query
	 *            search term (never null)
	 * @param maxHits
	 *            maximum number of search results
	 * 
	 * @return List of {@link SearchHit}
	 */
	public Collection<SearchHit> searchFaqs(String query, boolean exact, int maxHits);

	/**
	 * Perform a search for products & recipes
	 * 
	 * @param query
	 *            search term (never null)
	 * @param maxHits
	 *            maximum number of search results
	 * 
	 * @return List of {@link SearchHit}
	 */
	public Collection<SearchHit> searchRecipes(String query, boolean exact, int maxHits);

	/**
	 * Suggest spelling alternatives from the index.
	 * 
	 * @param query
	 *            search term
	 * @param maxHits
	 *            maximum number of spelling recommendations
	 * @return List of queries
	 */
	public Collection<SpellingHit> suggestSpelling(String query, double threshold, int maxHits);

	/**
	 * Reconstruct spelling alternatives using the index.
	 * 
	 * @param query
	 *            search term
	 * @param maxHits
	 *            maximum number of spelling recommendations
	 * @return List of queries
	 */
	public Collection<SpellingHit> reconstructSpelling(String query, double threshold, int maxHits);

	/**
	 * Update search index with some content objects.
	 * 
	 * @param contentNodes
	 *            Collection of {@link com.freshdirect.cms.ContentNodeI} (never null)
	 */
	public void index(Collection<ContentNodeI> contentNodes, boolean rebuild);

	/**
	 * Update spelling index
	 */
	public void indexSpelling(Collection<ContentNodeI> contentNodes);

	/**
	 * Optimize search index.
	 */
	public void optimize();

	/**
	 * Get the content types this search service supports.
	 * 
	 * @return Set of {@link com.freshdirect.cms.ContentType}
	 */
	public Set<ContentType> getIndexedTypes();

	/**
	 * Get the attributes which are indexed by this search service
	 * 
	 * @param type
	 * 
	 * @return List of {@link AttributeIndex}
	 */
	public List<AttributeIndex> getIndexesForType(ContentType type);

	/**
	 * DO NOT CALL DIRECTLY -- USED FOR TESTING PURPOSES ONLY
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	public IndexReader getReader() throws IOException;

	/**
	 * DO NOT CALL DIRECTLY -- USED FOR TESTING PURPOSES ONLY
	 * 
	 * @throws IOException
	 */
	public void closeReader();

	public Analyzer getAnalyzer();

	public SpellingSuggestionsServiceI getSpellService();

	public void setSynonymsDisabled(boolean synonymsDisabled);

	public boolean isSynonymsDisabled();

	public void setKeywordsDisabled(boolean keywordsDisabled);

	public boolean isKeywordsDisabled();

	/**
	 * INTERNAL &ndash; Gives spelling suggestions for a given search term. Internal function.
	 * 
	 * @param searchTerm
	 * @param threshold
	 * @param maxHits
	 * @return
	 */
	public List<SpellingHit> suggestSpellingInternal(String searchTerm, double threshold, int maxHits);

	/**
	 * INTERNAL &ndash; Using a best effort approach it reconstructs the permutations of suggestions by composing it from suggestion
	 * particles
	 * 
	 * @param searchPhrase
	 * @param threshold
	 * @param maxHits
	 * @return
	 */
	public List<List<SpellingHit>> generateSpellingParticles(List<String> searchPhrase, double threshold, int maxHits);
}