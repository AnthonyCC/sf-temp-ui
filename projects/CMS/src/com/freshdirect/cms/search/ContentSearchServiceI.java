/*
 * Created on Feb 17, 2005
 *
 */
package com.freshdirect.cms.search;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;

/**
 * Service interface for indexing and searching content.
 *
 */
public interface ContentSearchServiceI {

	/**
	 * Perform a search for content objects.
	 * 
	 * @param query search term (never null)
	 * @param maxHits maximum number of search results
	 * 
	 * @return List of {@link SearchHit}
	 */
	public List<SearchHit> search(String query, int maxHits);
	
	
	/**
	 * Suggest spelling alternatives from the index.
	 * @param query search term
	 * @param maxHits maximum number of spelling recommendations
	 * @return List of queries
	 */
	public List<SpellingHit> suggestSpelling(String query, int maxHits);

	/**
	 * Update search index with some content objects.
	 * 
	 * @param contentNodes Collection of {@link com.freshdirect.cms.ContentNodeI} (never null)
	 */
	public void index(Collection<ContentNodeI> contentNodes);

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
	 * set the current synonym dictionary, used by the indexing process.
	 * 
	 * @param dictionary
	 */
	public void setDictionary(SynonymDictionary dictionary);
	

}