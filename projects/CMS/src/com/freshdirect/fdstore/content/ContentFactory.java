/*
 * $Workfile:ContentFactory.java$
 *
 * $Date:8/21/2003 3:29:55 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import weblogic.utils.collections.ConcurrentHashMap;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.LuceneSpellingSuggestionService;
import com.freshdirect.cms.search.SpellingHit;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.framework.util.LruCache;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 *
 * @version $Revision:18$
 * @author $Author:Robert Gayle$
 */
public class ContentFactory {

	final static Category LOGGER = LoggerFactory.getInstance(ContentFactory.class);

	private static ContentFactory instance = new ContentFactory();

	/**
	 * @label caches
	 */
	private StoreModel store;

	/** Map of String (IDs) -> ContentNodeModel */
	private final Map contentNodes;

	/** Map of {@link ContentKey} (IDs) -> {@link com.freshdirect.fdstore.content.ContentNodeI} */
	private final Map nodesByKey;

	/** Map of sku codes / product nodes */
	private LruCache skuProduct;

	public static ContentFactory getInstance() {
		return instance;
	}

	public static void setInstance(ContentFactory newInstance) {
		instance = newInstance;
		LOGGER.info("ContentFactory instance replaced");
	}

	/** Maximum number of the top spelling results that should be analyzed */
	public static int MAX_SUGGESTIONS = 5;

	public ContentFactory() {
		this.store = null;

		// ISTVAN, chagned HashMap to ConcurrentHashMap
		this.contentNodes = new ConcurrentHashMap();
		this.nodesByKey = new ConcurrentHashMap();
		this.skuProduct = new LruCache(5000);
	}

	private StoreModel loadStore() {

		//
		// should be only one of these
		//
		//com.freshdirect.cms.ContentNodeI storeNode = null;
		ContentKey storeKey = ContentKey.decode("Store:FreshDirect");
		StoreModel theStore = (StoreModel) ContentNodeModelUtil.constructModel(storeKey, true);

		List brandList = new ArrayList();
		Set brandSet = loadKey("Brand", false);
		for (Iterator i = brandSet.iterator(); i.hasNext();) {
			ContentKey brandKey = (ContentKey) i.next();
			brandList.add(ContentNodeModelUtil.constructModel(brandKey, true));
		}
		theStore.setBrands(brandList);

		List domainList = new ArrayList();
		Set domainSet = loadKey("Domain", false);
		for (Iterator i = domainSet.iterator(); i.hasNext();) {
			ContentKey domainKey = (ContentKey) i.next();
			Domain domain = (Domain) ContentNodeModelUtil.constructModel(domainKey, false);
			domainList.add(domain);
		}
		theStore.setDomains(domainList);

		return theStore;
	}

	public synchronized StoreModel getStore() {
		if (this.store == null) {
			this.preLoad();
			this.cacheStore();
		}
		return this.store;
	}

	public boolean getPreviewMode() {
		return FDStoreProperties.getPreviewMode();
	}

	public Collection getProducts(CategoryModel category) {
		this.getStore(); //ensure Store is loaded

		return Collections.unmodifiableList(category.getPrivateProducts());
	}

	public ContentNodeModel getContentNode(String id) {
		this.getStore(); // ensure Store is loaded

		if (id == null)
			return null;

		ContentNodeModel m = (ContentNodeModel) this.contentNodes.get(id);
		if (m == null) {
			for (Iterator i = ContentNodeModelUtil.TYPE_MODEL_MAP.keySet().iterator(); i.hasNext();) {
				String typeName = (String) i.next();
				ContentKey key = new ContentKey(ContentType.get(typeName), id);
				ContentNodeI node = CmsManager.getInstance().getContentNode(key);
				if (node != null) {
					m = ContentNodeModelUtil.constructModel(key, true);
					break;
				}
			}
		}

		return m;
	}

	/**
	 * Get a ContentNode by contentName.
	 * 
	 * @deprecated use {@link #getContentNode(String)}
	 */
	public ContentNodeModel getContentNodeByName(String contentName) {
		return getContentNode(contentName);
	}

	void registerContentNode(ContentNodeModel node) {
		this.contentNodes.put(node.getContentName(), node);
		this.nodesByKey.put(node.getContentKey(), node);
	}

	/**
	 * Get a ProductModel using ContentRef
	 */
	public ProductModel getProduct(ProductRef ref) {
		return this.getProductByName(ref.getCategoryName(), ref.getProductName());
	}

	/**
	 * Get a ProductModel in it's primary home, using a skuCode
	 */
	public ProductModel getProduct(String skuCode) throws FDSkuNotFoundException {
		// look at cache
		ProductModel prod = (ProductModel) this.skuProduct.get(skuCode);
		if (prod != null)
			return prod;

		// get ProductRefs for sku
		List refs = getProdRefsForSku(skuCode);

		if (refs.size() == 0) {
			throw new FDSkuNotFoundException("SKU " + skuCode + " not found");
		}

		// find the one in the PRIMARY_HOME (if there's one)
		CategoryRef primaryHome = null;
		for (Iterator i = refs.iterator(); i.hasNext();) {
			ProductRef pRef = (ProductRef) i.next();

			prod = this.getProduct(pRef);
			if (prod == null) {
				// product not found, skip
				continue;
			}

			if (primaryHome == null) {
				Attribute ph = prod.getAttribute("PRIMARY_HOME");
				if (ph != null) {
					primaryHome = (CategoryRef) ph.getValue();
				}
			}

			if (primaryHome != null && primaryHome.getCategoryName().equals(pRef.getCategoryName())) {
				// whew, this is in the primary home, cache the result
				this.skuProduct.put(skuCode, prod);
				return prod;
			}

		}

		if (prod == null) {
			throw new FDSkuNotFoundException("Internal inconsistency: SKU "
				+ skuCode
				+ " not found, but ContentFactorySB thinks it does");
		}

		// cache the result
		this.skuProduct.put(skuCode, prod);

		return prod;
	}

	public ProductModel getProduct(String catId, String prodId) {
		return getProductByName(catId, prodId);
	}

	public ProductModel getProductByName(String catName, String prodName) {
		this.getStore(); // ensure Store is loaded

		CategoryModel category = (CategoryModel) this.getContentNodeByName(catName);
		if (category == null) {
			// no such category
			return null;
		}

		// HTai Refactored
		// this.cacheProducts(this.loadProducts(category));

		// now get it from the cache again, must be there
		ProductModel pm = category.getProductByName(prodName);

		return pm;
	}
	
	/**
	 * Perform a simple search.
	 * 
	 * Performs a search and filtering but does not do spell checking (unlike {@link #search(String) search}).
	 * 
	 * If term is "", return empty results.
	 * Otherwise separate the query into tokens, search for each separately, collate the results 
	 * (see {@link ContentSearchUtil}'s filter methods).
	 * 
	 * 
	 * @param criteria original query
	 * @return search results
	 */
	public SearchResults simpleSearch(String criteria) {
		String term = ContentSearchUtil.normalizeTerm(criteria);
		if ("".equals(term)) {
			return new SearchResults(Collections.EMPTY_LIST,
					Collections.EMPTY_LIST, Collections.EMPTY_LIST,
					Collections.EMPTY_LIST,false);
		}

		Collection hits = CmsManager.getInstance().search(term, 2000);
		
		Map hitsByType = ContentSearchUtil.mapHitsByType(hits);

		String[] tokens = ContentSearchUtil.tokenizeTerm(term);
		
		List allProducts = ContentSearchUtil
				.resolveHits(ContentSearchUtil
						.filterTopResults((List) hitsByType
								.get(FDContentTypes.PRODUCT), 500, 400));
		List relevantProducts = ContentSearchUtil.filterRelevantNodes(allProducts, tokens);
		List exactProducts = ContentSearchUtil.filterExactNodes(allProducts,term);

		List categories = ContentSearchUtil.filterRelevantNodes(
				ContentSearchUtil.resolveHits((List) hitsByType
						.get(FDContentTypes.CATEGORY)), tokens);
		
		List recipes = ContentSearchUtil.filterRelevantNodes(ContentSearchUtil
				.resolveHits((List) hitsByType.get(FDContentTypes.RECIPE)),
				tokens);
		
		List filteredCategories = ContentSearchUtil.filterCategoriesByVisibility(categories);
		List filteredExactProducts = ContentSearchUtil.filterProductsByDisplay(exactProducts);
		
		List filteredFuzzyProducts = 
			ContentSearchUtil.filterProductsByDisplay(
				relevantProducts.isEmpty() ? 
						ContentSearchUtil.restrictToMaximumOccuringNodes(allProducts,tokens) : 
						relevantProducts);
		
		List filteredRecipes = ContentSearchUtil.filterRecipesByAvailability(recipes);
		
		return new SearchResults(
				filteredCategories,
				filteredExactProducts,
				filteredFuzzyProducts,
				filteredRecipes,
				!relevantProducts.isEmpty());
	}
	
	/**
	 * Search and possibly suggest alternative.
	 * 
	 * The following triggers the search for alternative spellings.
	 * 
	 * <ul>
	 * <ol>The products found by {@link #simpleSearch(String) simpleSearch} were not 100% relevant (ie. there
	 *     were no matches that covered all query terms.</ol>
	 * <ol>No exact categories and no exact products were found.
	 * </ul>
	 * 
	 * Derivation of the alternative: 
	 * <ul>
	 *    <ol>Retrieve a list of spelling suggestions, sorted by relevance (see 
	 *        {@link LuceneSpellingSuggestionService#getSpellingHits}.</ol>
	 *    <ol>For earch suggestion (but at most {@link #MAX_SUGGESTIONS} many times), gather the "would be" search results</ol>
	 *    <ul>
	 *       <ol>If there are no results, skip suggestion.</ol>
	 *       <ol>If there are some results</ol>
	 *       <ul>
	 *          <ol>See if the edit distance to the "whole" expression is better than that of the previous suggestions, if
	 *              so set this as the new candidate for the suggested alternative</ol>
	 *          <ol>See if the results could indicate that the results for the suggestion add new value (that is,
	 *              calculate (S1 + S2 - S12)/(S1 + S2 + S1S2) where S1 and S2 are the number of results unique to
	 *              the original and the suggested queries and S1S2 is the number of elements common to both. This
	 *              is similar to the so called Jaccard distance. If this value is 0.80 or higher, stop here and
	 *              declare it "the" suggestion
	 *       </ul>
	 *    </ul>
	 * </ul>
	 * @param criteria original query
	 * @return search results, possibly ammended with a spelling suggestion
	 */
	public SearchResults search(String criteria) {
		SearchResults filteredResults = simpleSearch(criteria);
		
		SearchResults.SpellingResultsDifferences diffs = null;
		String spellingSuggestion = null;
		boolean suggestionMoreRelevant = false;
		
		if (!filteredResults.isProductsRelevant() || (
					filteredResults.getExactCategories().isEmpty() && 
					filteredResults.getExactProducts().isEmpty())) {
			
			List spellingSuggestions = CmsManager.getInstance().suggestSpelling(criteria, 20);
			
			int count = 0; 
			int bestDistance = criteria.length();
			for(Iterator i = spellingSuggestions.iterator(); count < MAX_SUGGESTIONS && i.hasNext(); ++count) {
				
				SpellingHit spellingHit = (SpellingHit)i.next();
				String suggestion = spellingHit.toString();
				
				SearchResults suggestionResults = simpleSearch(suggestion);
				
				if (!suggestionResults.getRecipes().isEmpty() || !suggestionResults.getFuzzyProducts().isEmpty() || 
					!suggestionResults.getExactCategories().isEmpty() || !suggestionResults.getExactProducts().isEmpty()) { 
					// there are some results
					
					if (spellingHit.getDistance() < bestDistance) bestDistance = spellingHit.getDistance();
					
					if (!filteredResults.isProductsRelevant()) { // no original relevant products
				
						Collection[] originalHits = 
							new Collection[] { 
								filteredResults.getExactProducts(), filteredResults.getExactCategories(),
								filteredResults.getFuzzyProducts(), filteredResults.getRecipes()};
						Collection[] suggestedHits = 
							new Collection[] { 
								suggestionResults.getExactProducts(), suggestionResults.getExactCategories(),
								suggestionResults.getFuzzyProducts(), suggestionResults.getRecipes() };
						
						diffs = ContentSearchUtil.chopSets(originalHits,suggestedHits);
						spellingSuggestion = suggestion;
						suggestionMoreRelevant =
							(
								(double)(diffs.getOriginal() + diffs.getSuggested() - diffs.getIntersection()) /
								(double)(diffs.getOriginal() + diffs.getSuggested() + diffs.getIntersection())
							) > 0.80 &&
							spellingHit.getDistance() == bestDistance;
						
						if (suggestionMoreRelevant) break;
					} else if (spellingSuggestion == null) {
						spellingSuggestion = suggestion;
					}
				}
			}
		}
		
		filteredResults.setSpellingSuggestion(spellingSuggestion, suggestionMoreRelevant);
		filteredResults.setSpellingResultsDifferences(diffs);
				
		return filteredResults;
	}

	public List getNewProducts(int days, String department) throws FDResourceException {
		Collection skus = FDCachedFactory.getNewSkuCodes(days);
		List prods = this.filterWholeProducts(skus);
		this.filterProdsByDept(prods, department);
		return prods;
	}

	public List getReintroducedProducts(int days, String department) throws FDResourceException {
		Collection skus = FDCachedFactory.getReintroducedSkuCodes(days);
		List prods = this.filterWholeProducts(skus);
		this.filterProdsByDept(prods, department);
		return prods;
	}

	/**
	 * If all of the skus for a ProductModel are in the list of skus,
	 * it is a new product, otherwise it's filtered out.
	 *  
	 * @param coll collection of skucodes
	 * @return List of ProductModels
	 */
	private List filterWholeProducts(Collection coll) {

		List prods = new ArrayList(coll.size());

		/** map of ProductModel -> Integer */
		Map limbo = new HashMap();

		for (Iterator pIter = coll.iterator(); pIter.hasNext();) {
			String skuCode = (String) pIter.next();
			try {
				ProductModel prod = this.getProduct(skuCode);

				if (prod.isHidden() || !prod.isSearchable() || prod.isUnavailable()) {
					continue;
				}

				int skuSize = prod.getSkus().size();
				if (skuSize == 1) {
					// single sku, just add it to list
					prods.add(prod);

				} else {
					// more than one sku, need to maintain sku counts 
					Integer remaining = (Integer) limbo.get(prod);
					if (remaining == null) {
						limbo.put(prod, new Integer(skuSize));
					} else {
						int r = remaining.intValue() - 1;
						limbo.put(prod, new Integer(r));
						if (r == 0) {
							// no more skus left, it's okay to include this in the list
							prods.add(prod);
						}
					}
				}

			} catch (FDSkuNotFoundException fdsnfe) {
				LOGGER.info("No matching product node for sku " + skuCode);
			}
		}
		LOGGER.debug("Dubious products " + limbo);
		return prods;
	}

	private void filterProdsByDept(List prods, String dept) {
		if (dept == null) {
			return;
		}
		for (ListIterator li = prods.listIterator(); li.hasNext();) {
			ProductModel prod = (ProductModel) li.next();
			if (!dept.equals(prod.getDepartment().getContentName())) {
				li.remove();
			}
		}
	}

	public void preLoad() {

		LOGGER.debug("starting store preLoad");
		try {
			// load store via CMS services
			LOGGER.debug("BEGIN GET STORE: " + new java.util.Date());

			// Warm-up routine
			loadKey("Store", true);

			loadKey("Brand", true);
			loadKey("Domain", true);
			loadKey("Department", true);
			//catSet = loadKey("Category", true);

			loadKey("Category", true);
			loadKey("Product", true);
			loadKey("Sku", true);
			loadKey("DomainValue", true);

			//loadKey("Image", true);
			//loadKey("Html", true);

			this.store = loadStore();
			LOGGER.debug("END GET STORE: " + new java.util.Date());
			LOGGER.debug("BEGIN CACHE STORE: " + new java.util.Date());
			this.cacheStore();
			LOGGER.debug("END CACHE STORE: " + new java.util.Date());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		LOGGER.debug("done with store preLoad");

	}

	private Set loadKey(String type, boolean getNodes) {
		LOGGER.debug("Begin Loading: " + type + " " + new java.util.Date());
		ContentServiceI cms = CmsManager.getInstance();
		ContentType t = ContentType.get(type);
		Set s = cms.getContentKeysByType(t);
		LOGGER.debug("Got keys for: " + type + " " + new java.util.Date());
		if (getNodes)
			cms.getContentNodes(s);
		LOGGER.debug("End Loading: " + type + " " + new java.util.Date());
		return s;
	}

	public Domain getDomainById(String domainId) {
		this.getStore(); // ensure Store is loaded
		Domain cNode = (Domain) contentNodes.get("Domain:" + domainId);
		if (cNode == null) {
			ContentKey ckey = ContentKey.decode("Domain:" + domainId);

			//Don't cache regular way; do our own caching
			cNode = (Domain) ContentNodeModelUtil.constructModel(ckey, false);
			contentNodes.put("Domain:" + domainId, cNode);
			this.nodesByKey.put(cNode.getContentKey(), cNode);
		}
		return cNode;
	}

	public DomainValue getDomainValueById(String domainValueId) {
		this.getStore(); // ensure Store is loaded
		DomainValue cNode = (DomainValue) contentNodes.get("DomainValue:" + domainValueId);
		if (cNode == null) {
			if (cNode == null) {
				//Don't cache regular way; do our own caching
				ContentKey ckey = ContentKey.decode("DomainValue:" + domainValueId);
				cNode = (DomainValue) ContentNodeModelUtil.constructModel(ckey, false);
			}
			Domain d = (Domain) cNode.getParentNode();
			List dValues = (List) d.getAttribute("domainValues").getValue();
			for (int i = 0; i < dValues.size(); i++) {
				DomainValueRef dvr = (DomainValueRef) dValues.get(i);
				if (dvr.getRefName2().equals(cNode.getContentName())) {
					cNode.setPriority(i + 1);
					break;
				}
			}
			contentNodes.put("DomainValue:" + domainValueId, cNode);
			this.nodesByKey.put(cNode.getContentKey(), cNode);
		}

		return cNode;
	}

	private void cacheStore() {
		contentNodes.put(store.getContentName(), store);
		nodesByKey.put(store.getContentKey(), store);
		List depts = this.store.getDepartments();
		for (Iterator i = depts.iterator(); i.hasNext();) {
			DepartmentModel dm = (DepartmentModel) i.next();
			cacheDepartment(dm);
		}
		List brands = this.store.getBrands();
		if (brands != null) {
			for (Iterator i = brands.iterator(); i.hasNext();) {
				cacheBrand((BrandModel) i.next());
			}
		}
		List storeDomains = this.store.getDomains();
		if (storeDomains != null) {
			for (Iterator di = storeDomains.iterator(); di.hasNext();) {
				cacheDomain((Domain) di.next());
			}
		}
	}

	private void cacheDepartment(DepartmentModel dept) {
		contentNodes.put(dept.getContentName(), dept);
		nodesByKey.put(dept.getContentKey(), dept);
	}

	private void cacheBrand(BrandModel brand) {
		contentNodes.put(brand.getContentName(), brand);
		nodesByKey.put(brand.getContentKey(), brand);
	}

	private void cacheDomain(Domain domain) {
		contentNodes.put("Domain:" + domain.getContentName(), domain);
		nodesByKey.put(domain.getContentKey(), domain);
		//domains.put(domain.getPK().getId(), domain);
		//nameDomains.put(domain.getName(), domain);
		/* place the domain values in this map also, so we don't have to traverse the list
		 *. We'll make the key's for this domId/Name + > + domValID/domValName
		 */
		List dmvList = domain.getDomainValues();
		if (dmvList != null) {
			for (Iterator dmvi = dmvList.iterator(); dmvi.hasNext();) {
				cacheDomainValue(domain, (DomainValue) dmvi.next());
			}
		}
	}

	private void cacheDomainValue(Domain domain, DomainValue domainValue) {
		contentNodes.put("DomainValue:" + domainValue.getContentName(), domainValue);
		nodesByKey.put(domainValue.getContentKey(), domainValue);
	}

	private List getProdRefsForSku(String skuCode) {
		ContentServiceI manager = CmsManager.getInstance();
		List prodRefs = new ArrayList();

		ContentNodeI skuNode = manager.getContentNode(new ContentKey(FDContentTypes.SKU, skuCode));
		if (skuNode == null)
			return prodRefs;

		Set productKeys = manager.getParentKeys(skuNode.getKey());
		for (Iterator i = productKeys.iterator(); i.hasNext();) {
			ContentKey prodKey = (ContentKey) i.next();
			Collection categoryKeys = manager.getParentKeys(prodKey);
			for (Iterator j = categoryKeys.iterator(); j.hasNext();) {
				ContentKey catKey = (ContentKey) j.next();
				prodRefs.add(new ProductRef(catKey.getId(), prodKey.getId()));
			}
		}

		return prodRefs;

	}

	public ContentNodeModel getContentNodeByKey(ContentKey key) {
		this.getStore();
		ContentNodeModel model = (ContentNodeModel) nodesByKey.get(key);
		if(model == null){
			ContentNodeI cmsCNode = CmsManager.getInstance().getContentNode(key);
			if(cmsCNode != null) {
				model = ContentNodeModelUtil.constructModel(key, true);
			}
		}
		return model;
	}
	
	public Set getParentKeys(ContentKey key) {
		Set s = CmsManager.getInstance().getParentKeys(key);
		return Collections.unmodifiableSet(s);
	}
	
}
