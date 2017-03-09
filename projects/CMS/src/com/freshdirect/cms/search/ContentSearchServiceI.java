/*
 * Created on Feb 17, 2005
 *
 */
package com.freshdirect.cms.search;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.search.spell.SpellingHit;

public interface ContentSearchServiceI {
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
	 * Get the content types this search service supports.
	 * 
	 * @return Set of {@link com.freshdirect.cms.ContentType}
	 */
	public Set<ContentType> getSearchableContentTypes();

	public SpellingSuggestionsServiceI getSpellService();

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