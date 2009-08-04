/*
 * Created on Nov 22, 2004
 */
package com.freshdirect.cms.application;

import java.util.Collection;
import java.util.List;
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
import com.freshdirect.framework.conf.FDRegistry;

/**
 * Singleton entry point for global CMS content service. Obtains primary
 * content-service instance using {@link com.freshdirect.framework.conf.FDRegistry}
 * via service name <code>com.freshdirect.cms.CmsManager</code>.
 */
public class CmsManager implements ContentServiceI {

	private static CmsManager instance;

	private ContentServiceI pipeline;
	private ContentSearchServiceI searchService;

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
	private void initialize() {
		Registry registry = FDRegistry.getInstance();
		ContentServiceI mgr = (ContentServiceI) registry.getService("com.freshdirect.cms.CmsManager", ContentServiceI.class);
		ContentSearchServiceI search = (ContentSearchServiceI) registry.getService(ContentSearchServiceI.class);
		this.initialize(mgr, search);
	}
	
	private void initialize(ContentServiceI pipeline, ContentSearchServiceI searchService) {
		this.pipeline = pipeline;
		this.searchService = searchService;
		ContextService.setInstance(new ContextService(this));
	}

    /**
     * Perform a search for content objects.
     * 
     * @param query search term (never null)
     * @param maxHits maximum number of search results
     * 
     * @return List of {@link SearchHit}
     */
	public List search(String term, int maxHits) {
		return searchService.search(term, maxHits);
	}
	
	public List suggestSpelling(String term, int maxHits) {
		return searchService.suggestSpelling(term, maxHits);
	}
	
	public ContentNodeI createPrototypeContentNode(ContentKey cKey) {
		return pipeline.createPrototypeContentNode(cKey);
	}

	public ContentNodeI getContentNode(ContentKey cKey) {
		return pipeline.getContentNode(cKey);
	}

	public Map getContentNodes(Set keys) {
		return pipeline.getContentNodes(keys);
	}

	public Map queryContentNodes(ContentType type, Predicate criteria) {
		return pipeline.queryContentNodes(type, criteria);
	}
	
	public Set getContentKeys() {
		return pipeline.getContentKeys();
	}

	public Set getContentKeysByType(ContentType type) {
		return pipeline.getContentKeysByType(type);
	}

	public Set getParentKeys(ContentKey key) {
		return pipeline.getParentKeys(key);
	}

	public ContentTypeServiceI getTypeService() {
		return pipeline.getTypeService();
	}

	public CmsResponseI handle(CmsRequestI request) {
		if (request.getUser().isAllowedToWrite()) {
			return pipeline.handle(request);
		} else {
			throw new SecurityException("Modification is not allowed to:"+request.getUser().getName());
		}
	}

	public String getName() {
		return "CmsManager";
	}
	
	public ContentServiceI getSelf() {
		return this;
	}

}