/*
 * Created on Nov 22, 2004
 */
package com.freshdirect.cms.application;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.hivemind.Registry;
import org.apache.log4j.Category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.cms.search.IBackgroundProcessor;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.search.SearchRelevancyList;
import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.cms.search.spell.SpellingHit;

import com.freshdirect.cms.util.MultiStoreProperties;
import com.freshdirect.cms.util.PrimaryHomeUtil;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.http.HttpService;

/**
 * Singleton entry point for global CMS content service. Obtains primary content-service instance using
 * {@link com.freshdirect.framework.conf.FDRegistry} via service name <code>com.freshdirect.cms.CmsManager</code>.
 */
public class CmsManager implements ContentServiceI {
	private final static Category LOGGER = LoggerFactory.getInstance(CmsManager.class);

	private static CmsManager instance;

	private ContentServiceI pipeline;
	private ContentSearchServiceI searchService;


	/**
	 * CMS {@link ContentKey} of the actual store node
	 */
	private ContentKey singleStoreKey;
	
	/**
	 * E-STORE id of the actual store node
	 */
	private String eStoreId;

	private boolean readOnlyContent = true; //false if CMS is in DB mode, true if XML mode

	protected CmsManager() {
	}

	/**
	 * Note: this constructor should only called from Tests project
	 * 
	 * @param pipeline
	 * @param searchService
	 */
	public CmsManager(ContentServiceI pipeline, ContentSearchServiceI searchService) {
		this.initializeInternal(pipeline, searchService, true);
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
	 * Default initialization based on values stored in property files.
	 * 
	 * @see CMSServiceConfig.xml or CMSServiceConfig_prd.xml
	 */
	public void initialize() {
		Registry registry = FDRegistry.getInstance();

		// get values from the hivemind configuration
		//   see CMSServiceConfig.xml or CMSServiceConfig_prd.xml
		ContentServiceI mgr = (ContentServiceI) registry.getService("com.freshdirect.cms.CmsManager", ContentServiceI.class);
		ContentSearchServiceI search = (ContentSearchServiceI) registry.getService(ContentSearchServiceI.class);
		final boolean flag = !registry.containsService("com.freshdirect.cms.ChangeTracker", ContentServiceI.class);

		this.initializeInternal(mgr, search, flag);
	}


	/**
	 * Internal initializer method
	 * 
	 * @param pipeline Topmost service {@link ContentServiceI}
	 * @param searchService Search facility
	 * @param isReadOnly True value indicates CMS subsystem is read-only, no save, delete or update operations are allowed.
	 */
	private void initializeInternal(ContentServiceI pipeline, ContentSearchServiceI searchService, final boolean isReadOnly) {
		this.pipeline = pipeline;
		this.searchService = searchService;

		this.readOnlyContent = isReadOnly;

		ContextService.setInstance( new ContextService(this) );

		this.singleStoreKey = calculateSingleStoreId();

		// guess corresponding eStore ID
		if (this.singleStoreKey != null) {
			final EnumEStoreId eStore = EnumEStoreId.valueOfContentId(singleStoreKey.getId());
			
			if (eStore != null) {
				this.eStoreId = eStore.getContentId();
			} else {
				LOGGER.warn("Failed to find eStore Id, falling back to " + EnumEStoreId.FD);
				this.eStoreId = EnumEStoreId.FD.getContentId();
			}    	
		} else {
			LOGGER.warn("No E-STORE ID is guessed in multi-store mode!");
		}

		if (this.readOnlyContent && this.singleStoreKey != null) {
			initPrimaryHomeCache(this.singleStoreKey);
		}

		this.readOnlyContent = isReadOnly;

		ContextService.setInstance( new ContextService(this) );

		this.singleStoreKey = calculateSingleStoreId();
		
		if (this.readOnlyContent && this.singleStoreKey != null) {
			initPrimaryHomeCache(this.singleStoreKey.getId());
		}

	}


	private ContentKey calculateSingleStoreId() {
		String theKey;
		if (!MultiStoreProperties.isCmsMultiStoreEnabled()) {
			//
			// == Single-Store CMS mode ==
			//
			
			// find first Store key
			Set<ContentKey> storeKeys = this.getContentKeysByType(FDContentTypes.STORE);
			LOGGER.debug(".. Available store keys in CMS : " + storeKeys);
			if (storeKeys == null || storeKeys.isEmpty()) {
				theKey = "FreshDirect";
				LOGGER.error("[SINGLE-STORE CMS MODE] ATTENTION!! No store nodes found in CMS. Get ready for system failure!");
			} else {
				theKey = storeKeys.iterator().next().getId();
				LOGGER.info("[SINGLE-STORE CMS MODE] booting with store ID : " + theKey);
			}

		} else {
			//
			// == Multi-Store CMS mode ==
			//
			// Preview nodes require explicit store keys
			if (!MultiStoreProperties.hasCmsStoreID()) {
				LOGGER.warn("[MULTI-STORE CMS MODE] No explicit CMS Store key is set! Will use default store key ...");
			}

			theKey = MultiStoreProperties.getCmsStoreId();
			LOGGER.info("[MULTI-STORE CMS MODE] Multi-Store mode ENABLED, booting with store ID : " + theKey);
		}
		
		return theKey != null ? new ContentKey(FDContentTypes.STORE, theKey) : null ;
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
			CmsResponseI response = pipeline.handle(request);
			propagateCmsChangeEvent(request.getNodes());
			
			try {
				rebuildIndices(request.getNodes());
			} catch (Exception exc) {
				LOGGER.error("Failed to invoke rebuildIndices", exc);
			}

			return response;
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

	 * The key of store root node for Store front or CMS preview nodes
	 *  
	 * @return CMS {@link ContentKey}
	 */
	public ContentKey getSingleStoreKey() {
		return singleStoreKey;
	}
	

	/**
	 * Return E-STORE id of the current store
	 * 
	 * @return ID or null in case of multi-store environment
	 */
	public String getEStoreId() {
		return eStoreId;
	}
	
	

	
	
	/**

	 * Convenience method to check if CMS Service is configured for XML use.
	 * 
	 * @return
	 */
	public boolean isReadOnlyContent() {
		return readOnlyContent;
	}


	private Map<ContentKey,ContentKey> primaryHomeMap; 

	public void initPrimaryHomeCache(final ContentKey storeKey) {
		primaryHomeMap = new HashMap<ContentKey,ContentKey>();
		
		Set<ContentKey> keys = this.getContentKeysByType(FDContentTypes.PRODUCT);
		for (ContentKey key : keys) {
			ContentKey priHome = PrimaryHomeUtil.pickPrimaryHomeForStore(key, storeKey, this);
			if (priHome != null) {
				primaryHomeMap.put(key, priHome);
			}
		}
	}
	
	



	private static final String PCE_EVENT_SINK_PROTOCOL = "http";
	private static final String PCE_EVENT_SINK_URI = "/api/contentcache";
	
	/**
	 * As part of CMS Change Propagation, this method broadcasts
	 * changed content keys to preview hosts.
	 * 
	 * Each content key will be sent out to all destinations.
	 * 
	 * @param nodes
	 */
    private static void propagateCmsChangeEvent(Collection<ContentNodeI> nodes) {
    	final ContentServiceI mgr = CmsManager.getInstance();

    	// #1 Collect target hosts
    	final Set<String> previewHosts = new HashSet<String>();
    	{
	    	final Set<ContentKey> _storeKeys = mgr.getContentKeysByType(FDContentTypes.STORE);
	    	for (ContentKey theKey : _storeKeys) {
	    		ContentNodeI ztore = mgr.getContentNode(theKey);
	    		if (ztore == null)
	    			continue;
	    		
	    		String host = (String) ztore.getAttributeValue("PREVIEW_HOST_NAME");
	    		if (host == null)
	    			continue;
	    		
	    		// TODO validate host against valid hostname
	    		previewHosts.add(host);
	    	}
    	}


    	// #2 transform nodes to content keys
    	Set<String> contentKeys = new HashSet<String>(nodes.size());
    	for (ContentNodeI node : nodes) {
    		contentKeys.add(node.getKey().getEncoded());
    	}

    	// #3 setup outbound packet
		final Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("contentKeys", contentKeys);


		// #4 let it go
		for (final String destination : previewHosts) {
			final String uri = PCE_EVENT_SINK_PROTOCOL + "://" + destination + PCE_EVENT_SINK_URI;

			Writer writer = new StringWriter();
    		try {
    			new ObjectMapper().writeValue(writer, payload);
    			String data = writer.toString();

    			LOGGER.info("Broadcast CMS change event to: " + uri + " with: " + data);
    			HttpService.defaultService().postData(uri, data);
    		} catch (IOException exception) {
    			LOGGER.error("CMS change event propagataion failed to preview node " + destination, exception);
    		}
		}
    }



	/**
	 * Utility method to make changes to various indices after updating content nodes
	 * 
	 * @param contentNodes
	 */
	public void rebuildIndices(Collection<ContentNodeI> contentNodes) {
		IBackgroundProcessor adminTool = (IBackgroundProcessor) FDRegistry.getInstance().getService(IBackgroundProcessor.class);

		LOGGER.debug("rebuildIndices was invoked");
		
		// invalidate search relevancy scores
		for (ContentNodeI node : contentNodes) {
			if (SearchRelevancyList.isRelatedContentNode(node)) {
				LOGGER.info("SearchRelevancyList gets invalidated, reason=" + node.getKey());
				ContentSearch.getInstance().invalidateRelevancyScores();
				break;
			}
		}

		// rebuild synonyms
		for (ContentNodeI node : contentNodes) {
	        final ContentType type = node.getKey().getType();
			if (FDContentTypes.SYNONYM.equals(type)) {
				LOGGER.info("Background reindex(1) is invoked with node " + node.getKey());

				Set<String> keywords = new HashSet<String>();
	            String[] fromValues = SynonymDictionary.getSynonymFromValues(node);
	            keywords.addAll(Arrays.asList(fromValues));

	            adminTool.backgroundReindex(keywords);
	            
	            break;
	        } else if (FDContentTypes.FDFOLDER.equals(type) && SynonymDictionary.SYNONYM_LIST_KEY.equals(node.getKey().getId())) {
				LOGGER.info("Background reindex(2) is invoked with node " + node.getKey());

				adminTool.backgroundReindex();

	            break;
	        }
		}
	}


	 

	public void initPrimaryHomeCache(final String storeId) {
		primaryHomeMap = new HashMap<ContentKey,ContentKey>();
		
		Set<ContentKey> keys = this.getContentKeysByType(FDContentTypes.PRODUCT);
		
		// final String storeId = MultiStoreProperties.getCmsStoreId();

		// System.err.println("Init cache started, evaluating " + keys.size() + " products");
		
		for (ContentKey key : keys) {
			ContentNodeI p = PrimaryHomeUtil.findParent(this.getContentNode(key), this, storeId);
			if (p != null ) {
				primaryHomeMap.put(key, p.getKey());
			}
		}
		// System.err.println("Cache init finished, size: " + primaryHomeMap.size());
	}
	
	public ContentKey getPrimaryHomeKey(ContentKey aKey) {
		if (readOnlyContent && primaryHomeMap != null) {
			return primaryHomeMap.get(aKey);
		} else {
			ContentNodeI p = PrimaryHomeUtil.findParent(this.getContentNode(aKey), this, MultiStoreProperties.getCmsStoreId());
			return p != null ? p.getKey() : null;
		}
	}
	
    /*private static void propagateCmsChangeEvent(Collection<ContentNodeI> nodes) {
    	Map<String, Set<String>> contentKeys = collectContentKeysByStore(nodes);
    	for (String previewHost : contentKeys.keySet()) {
    		Set<String> previewNodeContentKeys = contentKeys.get(previewHost);
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("contentKeys", previewNodeContentKeys);
    		Writer writer = new StringWriter();
    		try {
    			new ObjectMapper().writeValue(writer, map);
    			String data = writer.toString();
    			String uri = "http://" + previewHost + "/api/contentcache";
    			LOGGER.info("[PCE]Propagate change event to: " + uri + " with: " + data);
    			HttpService.defaultService().postData(uri, data);
    		} catch (IOException exception) {
    			LOGGER.error("CMS change event propagataion failed to preview node!");
    		}
    	}
    }*/


	private static Map<String, Set<String>> collectContentKeysByStore(Collection<ContentNodeI> nodes) {
		Map<String, Set<String>> contentKeys = new HashMap<String, Set<String>>();
		Set<String> contentKeysEncoded = new HashSet<String>();
		Set<String> hosts = new HashSet<String>();
    	for (ContentNodeI node : nodes) {
    		String key = node.getKey().getEncoded();
    		contentKeysEncoded.add(key);
    		Set<ContentKey> storeKeys = PrimaryHomeUtil.getStoreKeys(node.getKey(), CmsManager.getInstance());
    		for (ContentKey storeKey : storeKeys) {
    			String previewHost = (String) storeKey.getContentNode().getAttributeValue("PREVIEW_HOST_NAME");
    			if (previewHost != null) {
    				hosts.add(previewHost);
    			}
    		}
    	}
    	for (String host : hosts) {
    		contentKeys.put(host, contentKeysEncoded);
    	}
		return contentKeys;
	}

}
