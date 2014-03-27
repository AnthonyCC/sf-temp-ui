/*
 * Created on Nov 22, 2004
 */
package com.freshdirect.cms.application;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.hivemind.Registry;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.framework.conf.FDRegistry;

/**
 * Singleton entry point for global CMS content service. Obtains primary content-service instance using
 * {@link com.freshdirect.framework.conf.FDRegistry} via service name <code>com.freshdirect.cms.CmsManager</code>.
 */
public class CmsManager implements ContentServiceI {

	private static CmsManager instance;

	private ContentServiceI pipeline;
	private ContentSearchServiceI searchService;
	private boolean readOnlyContent = true; //false if CMS is in DB mode, true if XML mode

	public CmsManager() {
	}

	public CmsManager(ContentServiceI pipeline, ContentSearchServiceI searchService) {
		this.initialize(pipeline, searchService);
	}

	public static void main(String args[]) {
		instance = getInstance();
		instance.toString();
	}

	public static CmsManager getInstance() {
		try {
			if (instance == null) {
				instance = new CmsManager();
				instance.initialize();
			}
		} catch (Exception ex) {
			instance = null;
			ex.printStackTrace();
			throw new CmsRuntimeException(ex);
		}

		return instance;
	}

	/**
	 * Method to set the singleton instance, useful in test-cases.
	 */
	public static void setInstance(CmsManager manager) {
		instance = manager;
	}

	/**
	 * Default initialization based on {@link FDRegistry}.
	 */
	public void initialize() {
		Registry registry = FDRegistry.getInstance();
		ContentServiceI mgr = (ContentServiceI) registry.getService("com.freshdirect.cms.CmsManager", ContentServiceI.class);
		ContentSearchServiceI search = (ContentSearchServiceI) registry.getService(ContentSearchServiceI.class);
		this.initialize(mgr, search);
		readOnlyContent = !FDRegistry.getInstance().containsService("com.freshdirect.cms.ChangeTracker", ContentServiceI.class);
	}

	private void initialize(ContentServiceI pipeline, ContentSearchServiceI searchService) {
		this.pipeline = pipeline;
		this.searchService = searchService;
		ContextService.setInstance(new ContextService(this));
	}

	/**
	 * Perform a search for content objects.
	 * 
	 * @param query
	 *            search term (never null)
	 * @param maxHits
	 *            maximum number of search results
	 * 
	 * @return List of {@link SearchHit}
	 */
	public Collection<SearchHit> search(String term, boolean phrase, int maxHits) {
		return searchService.search(term, phrase, maxHits);
	}

	/**
	 * Search for products and recipes
	 * 
	 * @param term
	 * @param maxHits
	 * @return
	 */
	public Collection<SearchHit> searchProducts(String term, boolean phrase, boolean approximate, int maxHits) {
		return searchService.searchProducts(term, phrase, approximate, maxHits);
	}

	/**
	 * Search for FAQs
	 * 
	 * @param term
	 * @param maxHits
	 * @return
	 */
	public Collection<SearchHit> searchFaqs(String term, boolean phrase, int maxHits) {
		return searchService.searchFaqs(term, phrase, maxHits);
	}

	/**
	 * Search only for recipes
	 * 
	 * @param term
	 * @param maxHits
	 * @return
	 */
	public Collection<SearchHit> searchRecipes(String term, boolean phrase, int maxHits) {
		return searchService.searchRecipes(term, phrase, maxHits);
	}

	public Collection<SpellingHit> suggestSpelling(String term, double threshold, int maxHits) {
		return searchService.suggestSpelling(term, threshold, maxHits);
	}

	public Collection<SpellingHit> reconstructSpelling(String term, double threshold, int maxHits) {
		return searchService.reconstructSpelling(term, threshold, maxHits);
	}
	
	public ContentNodeI createPrototypeContentNode(ContentKey cKey) {
		return pipeline.createPrototypeContentNode(cKey);
	}

	public ContentNodeI getContentNode(ContentKey cKey) {
		return pipeline.getContentNode(cKey);
	}

	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys) {
		return pipeline.getContentNodes(keys);
	}

	public Map<ContentKey, ContentNodeI> queryContentNodes(ContentType type, Predicate criteria) {
		return pipeline.queryContentNodes(type, criteria);
	}

	public Set<ContentKey> getContentKeys() {
		return pipeline.getContentKeys();
	}

	public Set<ContentKey> getContentKeysByType(ContentType type) {
		return pipeline.getContentKeysByType(type);
	}

	public Set<ContentKey> getParentKeys(ContentKey key) {
		return pipeline.getParentKeys(key);
	}

	public ContentTypeServiceI getTypeService() {
		return pipeline.getTypeService();
	}

	public CmsResponseI handle(CmsRequestI request) {
		if (request.getUser().isAllowedToWrite()) {
			return pipeline.handle(request);
		} else {
			throw new SecurityException("Modification is not allowed to:" + request.getUser().getName());
		}
	}

	public String getName() {
		return "CmsManager";
	}

	public ContentServiceI getSelf() {
		return this;
	}

	public ContentNodeI getRealContentNode(ContentKey key) {
		return pipeline.getRealContentNode(key);
	}

	/**
	 * Convenience method to check if CMS Service is configured for XML use.
	 * 
	 * @return
	 */
	public boolean isReadOnlyContent() {
		return readOnlyContent;
	}
}
