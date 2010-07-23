package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.LruCache;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @version $Revision:18$
 * @author $Author:Robert Gayle$
 */
public class ContentFactory {
	private final static Category LOGGER = LoggerFactory.getInstance(ContentFactory.class);

	private static ContentFactory instance = new ContentFactory();
	
	private static Map<ProductModel, Date> newProducts = Collections.emptyMap();
	
	private static long newProductsLastUpdated = Long.MIN_VALUE;
	
	private static final Object newProductsLock = new Object();

	private static Map<ProductModel, Date> backInStockProducts = Collections.emptyMap();
	
	private static long backInStockProductsLastUpdated = Long.MIN_VALUE;

	private static final Object backInStockProductsLock = new Object();
	
	private static final long NEW_AND_BACK_REFRESH_PERIOD = 1000 * 60 * 15; // 15 minutes

	private static final long DAY_IN_MILLISECONDS = 1000 * 3600 * 24;
	
	/**
	 * @label caches
	 */
	private StoreModel store;

	/** Map of String (IDs) -> ContentNodeModel */
	private final Map<String, ContentNodeModel> contentNodes;

	/** Map of {@link ContentKey} (IDs) -> {@link com.freshdirect.fdstore.content.ContentNodeModel} */
	private final Map<ContentKey, ContentNodeModel> nodesByKey;
	
	/** Map of sku codes / product nodes */
	private LruCache<String,ContentKey> skuProduct;
	
	private ThreadLocal<PricingContext> currentPricingContext;

	public static ContentFactory getInstance() {
		return instance;
	}

	public static void setInstance(ContentFactory newInstance) {
		instance = newInstance;
		LOGGER.info("ContentFactory instance replaced");
	}

	public static List<ProductModel> filterProductsByDeptartment(List<ProductModel> products, String departmentId) {
		if (departmentId != null)
			for (ListIterator<ProductModel> li = products.listIterator(); li.hasNext();) {
				ProductModel p = li.next();
				if (p.getDepartment() != null) {
					if (!departmentId.equals(p.getDepartment().getContentName()))
						li.remove();
				}
			}
		return products;
	}

	public ContentFactory() {
		this.store = null;

		// ISTVAN, chagned HashMap to ConcurrentHashMap
		this.contentNodes = new ConcurrentHashMap<String, ContentNodeModel>();
		this.nodesByKey = new ConcurrentHashMap<ContentKey, ContentNodeModel>();
		this.skuProduct = new LruCache<String,ContentKey>(40000);
		
		currentPricingContext = new ThreadLocal<PricingContext>() {
			@SuppressWarnings( "synthetic-access" )
			@Override
			protected PricingContext initialValue() {
				try {
					throw new FDException("initializing current pricing context with default value");
				} catch (FDException e) {
					LOGGER.warn(e.getMessage(), e);
				}
				return PricingContext.DEFAULT;
			}
		};
	}

	private StoreModel loadStore() {

		//
		// should be only one of these
		//
		//com.freshdirect.cms.ContentNodeI storeNode = null;
		ContentKey storeKey = ContentKey.decode("Store:FreshDirect");
		StoreModel theStore = (StoreModel) ContentNodeModelUtil.constructModel(storeKey, true);

		List<BrandModel> brandList = new ArrayList<BrandModel>();
		Set<ContentKey> brandSet = loadKey("Brand", false);
		for ( ContentKey brandKey : brandSet ) {
			brandList.add((BrandModel)ContentNodeModelUtil.constructModel(brandKey, true));
		}
		theStore.setBrands(brandList);

		List<Domain> domainList = new ArrayList<Domain>();
		Set<ContentKey> domainSet = loadKey("Domain", false);
		for (Iterator<ContentKey> i = domainSet.iterator(); i.hasNext();) {
			ContentKey domainKey = i.next();
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

	public Collection<ProductModel> getProducts(CategoryModel category) {
		this.getStore(); //ensure Store is loaded

		return Collections.unmodifiableList(category.getPrivateProducts());
	}

	/**
	 * Get a ContentNode by id.
	 */
	public ContentNodeModel getContentNode(String id) {
		this.getStore(); // ensure Store is loaded

		if ( id == null || id.trim().equals( "" ) )
			return null;

		ContentNodeModel m = this.contentNodes.get(id);
		if (m == null) {
			for ( String typeName : ContentNodeModelUtil.TYPE_MODEL_MAP.keySet() ) {
				ContentKey key = new ContentKey(ContentType.get(typeName), id);
				ContentNodeI node = CmsManager.getInstance().getContentNode(key);
				if (node != null) {
					m = ContentNodeModelUtil.constructModel(key, true);
					break;
				}
			}
		}
		
		if ( m == null ) {
			LOGGER.warn( "Content node not found ! getContentNode(\""+id+"\") returned null!" );
		}

		return m;
	}
	
	public ContentNodeModel getContentNode(ContentType type, String id) {
	    return getContentNodeByKey(new ContentKey(type, id));
	}
	

	void registerContentNode(ContentNodeModel node) {
		this.contentNodes.put(node.getContentName(), node);
		this.nodesByKey.put(node.getContentKey(), node);
	}

	public ProductModel getProduct(ProductModel ref) {
	    return ref;
	}
	
//	final Counter prodRefsForSkuCall = new Counter("ContentFactory_getProdRefsForSku").register();
	
	/**
	 * Get a ProductModel in it's primary home, using a skuCode
	 */
	public ProductModel getProduct(String skuCode) throws FDSkuNotFoundException {
		// look at cache
		ContentKey prodKey = this.skuProduct.get(skuCode);
		if (prodKey != null)
			return (ProductModel) getContentNodeByKey(prodKey);

		// get ProductModelss for sku
//		long time = prodRefsForSkuCall.start();
		List<ProductModel> refs = getProdRefsForSku(skuCode);
//		prodRefsForSkuCall.end(time);
		if (refs.size() == 0) {
			throw new FDSkuNotFoundException("SKU " + skuCode + " not found");
		}

		// find the one in the PRIMARY_HOME (if there's one)
		ProductModel prod = null;
		CategoryModel primaryHome = null;
		for (Iterator<ProductModel> i = refs.iterator(); i.hasNext();) {
		        prod = i.next();

		        if (prod == null) {
				// product not found, skip
				continue;
			}

			if (primaryHome == null) {
				CategoryModel ph = prod.getPrimaryHome();
				if (ph != null) {
				    primaryHome = ph;
				}
			}

			if (primaryHome != null && primaryHome.getContentKey().getId().equals(prod.getParentId())) {
				// whew, this is in the primary home, cache the result
				this.skuProduct.put(skuCode, prod.getContentKey());
				return prod;
			}

		}

		if (prod == null) {
			throw new FDSkuNotFoundException("Internal inconsistency: SKU "
				+ skuCode
				+ " not found, but ContentFactorySB thinks it does");
		}

		// cache the result
		this.skuProduct.put(skuCode, prod.getContentKey());

		return prod;
	}

	public ProductModel getProduct(String catId, String prodId) {
		return getProductByName(catId, prodId);
	}

	public ProductModel getProductByName(String catName, String prodName) {
		this.getStore(); // ensure Store is loaded

		CategoryModel category = (CategoryModel) this.getContentNode(catName);
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
	 * If all of the skus for a ProductModel are in the list of skus,
	 * it is a new product, otherwise it's filtered out.
	 *  
	 * @param coll collection of skucodes
	 * @return List of ProductModels
	 */
	@SuppressWarnings( "unused" )
	private List<ProductModel> filterWholeProducts(Collection<String> skuCodes) {

		List<ProductModel> prods = new ArrayList<ProductModel>(skuCodes.size());

		/** map of ProductModel -> Integer */
		Map<ProductModel, Integer> limbo = new HashMap<ProductModel, Integer>();

        for ( String skuCode : skuCodes ) {
            ProductModel prod = this.filterProduct(skuCode);

            if (prod != null) {
                int skuSize = prod.getPrimarySkus().size();
                if (skuSize == 1) {
                    // single sku, just add it to list
                    prods.add(prod);

                } else {
                    // more than one sku, need to maintain sku counts
                    Integer remaining = limbo.get(prod);
                    if (remaining == null) {
                        limbo.put(prod, new Integer(skuSize));
                    } else {
                        int r = remaining.intValue() - 1;
                        limbo.put(prod, new Integer(r));
                        if (r == 0) {
                            // no more skus left, it's okay to include this in
                            // the list
                            prods.add(prod);
                        }
                    }
                }
            }
        }
		LOGGER.debug("Dubious products " + limbo);
		return prods;
	}

    private ProductModel filterProduct(String skuCode) {
        try {
            ProductModel prod = this.getProduct(skuCode);

            if (prod.isHidden() || !prod.isSearchable() || prod.isUnavailable()) {
                return null;
            }

            return prod;
        } catch (FDSkuNotFoundException fdsnfe) {
            LOGGER.info("No matching product node for sku " + skuCode);
            return null;
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

	private Set<ContentKey> loadKey(String type, boolean getNodes) {
		LOGGER.debug("Begin Loading: " + type + " " + new java.util.Date());
		ContentServiceI cms = CmsManager.getInstance();
		ContentType t = ContentType.get(type);
		Set<ContentKey> s = cms.getContentKeysByType(t);
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
			//Don't cache regular way; do our own caching
			ContentKey ckey = ContentKey.decode("DomainValue:" + domainValueId);
			cNode = (DomainValue) ContentNodeModelUtil.constructModel(ckey, false);
			Domain d = (Domain) cNode.getParentNode();
			List<ContentKey> dValues = d.getDomainValueKeys();
			for ( int i = 0; i < dValues.size(); i++ ) {
				ContentKey dvr = dValues.get( i );
				if ( dvr.equals( cNode.getContentKey() ) ) {
					cNode.setPriority( i + 1 );
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
		List<DepartmentModel> depts = this.store.getDepartments();
		for ( DepartmentModel dm : depts ) {
			cacheDepartment(dm);
		}
		List<BrandModel> brands = this.store.getBrands();
		if (brands != null) {
			for ( BrandModel bm : brands ) {
				cacheBrand( bm );
			}
		}
		List<Domain> storeDomains = this.store.getDomains();
		if (storeDomains != null) {
			for ( Domain d : storeDomains ) {
				cacheDomain( d );
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
		List<DomainValue> dmvList = domain.getDomainValues();
		if (dmvList != null) {
			for ( DomainValue dv : dmvList ) {
				cacheDomainValue(domain, dv);
			}
		}
	}

	private void cacheDomainValue(Domain domain, DomainValue domainValue) {
		contentNodes.put("DomainValue:" + domainValue.getContentName(), domainValue);
		nodesByKey.put(domainValue.getContentKey(), domainValue);
	}

	private List<ProductModel> getProdRefsForSku(String skuCode) {
		ContentServiceI manager = CmsManager.getInstance();
		List<ProductModel> prodRefs = new ArrayList<ProductModel>();

		ContentNodeI skuNode = manager.getContentNode(new ContentKey(FDContentTypes.SKU, skuCode));
		if (skuNode == null)
			return prodRefs;

		Set<ContentKey> productKeys = manager.getParentKeys(skuNode.getKey());
		for ( ContentKey prodKey : productKeys ) {
			Collection<ContentKey> categoryKeys = manager.getParentKeys(prodKey);
			for ( ContentKey catKey : categoryKeys ) {
				prodRefs.add(getProductByName(catKey.getId(), prodKey.getId()));
			}
		}
		return prodRefs;
	}

	public ContentNodeModel getContentNodeByKey(ContentKey key) {
		this.getStore();
		ContentNodeModel model = nodesByKey.get(key);
		if(model == null){
			ContentNodeI cmsCNode = CmsManager.getInstance().getContentNode(key);
			if(cmsCNode != null) {
				model = ContentNodeModelUtil.constructModel(key, true);
			}
		}
			
		return model;
	}
	
	public ContentNodeModel getCachedContentNodeByKey(ContentKey key) {
	    this.getStore();
	    return nodesByKey.get(key);
	}
	
	
	public Set<ContentKey> getParentKeys(ContentKey key) {
		Set<ContentKey> s = CmsManager.getInstance().getParentKeys(key);
		return Collections.unmodifiableSet(s);
	}
	
	public PricingContext getCurrentPricingContext() {
		return currentPricingContext.get();
	}
	
	public void setCurrentPricingContext(PricingContext pricingContext) {
		LOGGER.debug("setting pricing context to " + pricingContext + " in thread " + Thread.currentThread().getId());
		currentPricingContext.set(pricingContext);
	}
	
	/**
	 * Return the products and their corresponding dates of becoming new
	 * 
	 * @return a {@link Map} of {@link ProductModel} - {@link Date} pairs
	 */
	public Map<ProductModel, Date> getNewProducts() {
		synchronized (newProductsLock) {
			if (System.currentTimeMillis() > newProductsLastUpdated + NEW_AND_BACK_REFRESH_PERIOD) {
				// refresh
				try {
					Map<String, Date> skus = FDCachedFactory.getNewSkus();
					Map<ProductModel, Date> newCache = new HashMap<ProductModel, Date>(skus.size());
					for (Map.Entry<String, Date> entry : skus.entrySet()) {
						SkuModel sku = (SkuModel) getContentNodeByKey(new ContentKey(FDContentTypes.SKU, entry.getKey()));
						if (sku != null && !sku.isUnavailable()) {
							ProductModel p = this.filterProduct(sku.getContentName());
							if (p != null) {
								Date prev = newCache.get(p);
								if (prev == null || entry.getValue().after(prev))
									newCache.put(p, entry.getValue());
							}
						}
					}
					newProducts = newCache;
					newProductsLastUpdated = System.currentTimeMillis();
					LOGGER.info("reloaded new products: " + newProducts.size());
				} catch (FDResourceException e) {
					LOGGER.error("failed to update new products cache; retrying a minute later", e);
					newProductsLastUpdated = System.currentTimeMillis() - 14 * 60000;
				}
			}
			return Collections.unmodifiableMap(newProducts);
		}
	}

	/**
	 * Return the products new in the last n days
	 * 
	 * @param inDays
	 *            the n days criteria
	 * @return the list of the products in the sorted order where the most recent product comes first
	 */
	public LinkedHashMap<ProductModel, Date> getNewProducts(int inDays) {
		final Map<ProductModel, Date> x = getNewProducts();
		List<ProductModel> products = new ArrayList<ProductModel>(x.size());
		long limit = System.currentTimeMillis() - inDays * DAY_IN_MILLISECONDS;
		for (Map.Entry<ProductModel, Date> entry : x.entrySet()) {
			if (entry.getValue().getTime() > limit)
				products.add(entry.getKey());
		}
		Collections.sort(products, new Comparator<ProductModel>() {
			@Override
			public int compare(ProductModel o1, ProductModel o2) {
				return -x.get(o1).compareTo(x.get(o2));
			}
		});
		LinkedHashMap<ProductModel, Date> productsInDays = new LinkedHashMap<ProductModel, Date>();
		for (ProductModel p : products)
			productsInDays.put(p, x.get(p));
		return productsInDays;
	}
	
	/**
	 * Returns for how many days the product has been new.
	 * 
	 * @param product
	 * @return the number of days (plus fraction of day)
	 */
	public double getProductAge(ProductModel product) {
		Date when = getNewProducts().get(product);
		if (when == null) {
			return Integer.MAX_VALUE; // very long time ago
		}		
		return ((double) System.currentTimeMillis() - when.getTime()) / DAY_IN_MILLISECONDS;
	}
	
	/**
	 * Alias to {@link #getProductAge(ProductModel)}
	 * 
	 * @param product
	 * @return
	 */
	public double getNewProductAge(ProductModel product) {
		return getProductAge(product);
	}

	/**
	 * Return the products and their corresponding dates of becoming back in stock
	 * 
	 * @return a {@link Map} of {@link ProductModel} - {@link Date} pairs
	 */
	public Map<ProductModel, Date> getBackInStockProducts() {
		synchronized (backInStockProductsLock) {
			if (System.currentTimeMillis() > backInStockProductsLastUpdated + NEW_AND_BACK_REFRESH_PERIOD) {
				// refresh
				try {
					Map<String, Date> skus = FDCachedFactory.getBackInStockSkus();
					Map<ProductModel, Date> newCache = new HashMap<ProductModel, Date>(skus.size());
					for (Map.Entry<String, Date> entry : skus.entrySet()) {
						SkuModel sku = (SkuModel) getContentNodeByKey(new ContentKey(FDContentTypes.SKU, entry.getKey()));
						if (sku != null && !sku.isUnavailable()) {
							ProductModel p = this.filterProduct(sku.getContentName());
							if (p != null) {
								Date prev = newCache.get(p);
								if (prev == null || entry.getValue().after(prev))
									newCache.put(p, entry.getValue());
							}
						}
					}
					backInStockProducts = newCache;
					backInStockProductsLastUpdated = System.currentTimeMillis();
					LOGGER.info("reloaded back-in-stock products: " + backInStockProducts.size());
				} catch (FDResourceException e) {
					LOGGER.error("failed to update back-in-stock products cache; retrying a minute later", e);
					backInStockProductsLastUpdated = System.currentTimeMillis() - 14 * 60000;
				}
			}
			return Collections.unmodifiableMap(backInStockProducts);
		}
	}

	/**
	 * Return the products back-in-stock in the last n days
	 * 
	 * @param inDays
	 *            the n days criteria
	 * @return the list of the products in the sorted order where the most recent product comes first
	 */
	public LinkedHashMap<ProductModel, Date> getBackInStockProducts(int inDays) {
		final Map<ProductModel, Date> x = getBackInStockProducts();
		List<ProductModel> products = new ArrayList<ProductModel>(x.size());
		long limit = System.currentTimeMillis() - inDays * DAY_IN_MILLISECONDS;
		for (Map.Entry<ProductModel, Date> entry : x.entrySet()) {
			if (entry.getValue().getTime() > limit)
				products.add(entry.getKey());
		}
		Collections.sort(products, new Comparator<ProductModel>() {
			@Override
			public int compare(ProductModel o1, ProductModel o2) {
				return -x.get(o1).compareTo(x.get(o2));
			}
		});
		LinkedHashMap<ProductModel, Date> productsInDays = new LinkedHashMap<ProductModel, Date>();
		for (ProductModel p : products)
			productsInDays.put(p, x.get(p));
		return productsInDays;
	}
	
	/**
	 * Returns for how many days the product has been back in stock.
	 * 
	 * @param product
	 * @return the number of days (plus fraction of day)
	 */
	public double getBackInStockProductAge(ProductModel product) {
		Date when = getBackInStockProducts().get(product);
		if (when == null) {
			return Integer.MAX_VALUE; // very long time ago
		}
		return ((double) System.currentTimeMillis() - when.getTime()) / DAY_IN_MILLISECONDS;
	}
	
	/**
	 * Forces to refresh the new and back-in-stock products cache
	 */
	public void refreshNewAndBackCache() {
		synchronized (newProductsLock) {
			newProductsLastUpdated = Integer.MIN_VALUE;
			getNewProducts();
		}
		synchronized (backInStockProductsLock) {
			backInStockProductsLastUpdated = Integer.MIN_VALUE;
			getBackInStockProducts();
		}
	}
}
