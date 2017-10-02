package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draft.service.DraftService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.grabber.GrabberServiceI;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.LruCache;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ContentFactory {

    private final static Category LOGGER = LoggerFactory.getInstance(ContentFactory.class);

    private static ContentFactory instance = new ContentFactory();

    private static final long NEW_AND_BACK_REFRESH_PERIOD = 1000 * 60 * 15; // 15 minutes
    private static final long DAY_IN_MILLISECONDS = 1000 * 3600 * 24;

    private long newProductsLastUpdated = Long.MIN_VALUE;
    private long backInStockProductsLastUpdated = Long.MIN_VALUE;

    private StoreModel store;

    /** Map of String (IDs) -> ContentNodeModel */
    private final Map<String, ContentNodeModel> nodesByIdCache;

    /** Map of {@link ContentKey} (IDs) -> {@link com.freshdirect.fdstore.content.ContentNodeModel} */
    private final Map<ContentKey, ContentNodeModel> nodesByKeyCache;

    /** Map of sku codes / content keys */
    private final LruCache<String, ContentKey> keyBySkuCodeCache;

    private final Map<ProductModel, Map<String, Date>> newProductCache;
    private final Map<ProductModel, Map<String, Date>> backInStockProductCache;
    private final Map<ContentKey, Set<ContentKey>> parentKeysByKeyCache;
    
    private ThreadLocal<UserContext> currentUserContext;
    private ThreadLocal<Boolean> eligibleForDDPP;
    private ThreadLocal<DraftContext> currentDraftContext;
    private ThreadLocal<HashMap<String, Object>> userAllSoData;

    private class WineIndex {

        // all wine products
        private Set<ContentKey> all;

        // the maind domain value - product index
        private Map<DomainValue, Set<ContentKey>> index;

        // category - domain value mappings
        private Map<ContentKey, DomainValue> categories;

        // domain - category value mappings
        private Map<DomainValue, ContentKey> catReverse;

        // category - domain values mappings (for TLCs: usq_region, usq_type and usq_more)
        private Map<ContentKey, Collection<WineFilterValue>> categoryDomains;

        // category - sub domain values mapping
        private Map<ContentKey, Set<DomainValue>> subDomains;

        // domain value - domain encoded mappings
        private Map<DomainValue, String> encodedDomains;

        public WineIndex() {
            all = new HashSet<ContentKey>(1000);
            index = new HashMap<DomainValue, Set<ContentKey>>(1000);
            categories = new HashMap<ContentKey, DomainValue>(100);
            catReverse = new HashMap<DomainValue, ContentKey>(100);
            categoryDomains = new HashMap<ContentKey, Collection<WineFilterValue>>();
            encodedDomains = new HashMap<DomainValue, String>(1000);
            subDomains = new HashMap<ContentKey, Set<DomainValue>>(100);
        }
    }

    private WineIndex wineIndex;

    private Object wineIndexLock = new Object();

    public static ContentFactory getInstance() {
        return instance;
    }

    public static void setInstance(ContentFactory newInstance) {
        instance = newInstance;
        LOGGER.info("ContentFactory instance replaced");
    }

    /**
     * [FDX] The key of currently loaded store
     */
    private ContentKey storeKey;

    private GrabberServiceI grabberService;
    private CmsManager cmsManager;

    /**
     * The main point to know the actual store node
     * 
     * @return
     */
    public ContentKey getStoreKey() {
        return storeKey;
    }

    /**
     * Shortcut to get the Store model directly
     * 
     * @return
     */
    public StoreModel getStoreModel() {
        return (StoreModel) getContentNodeByKey(getStoreKey());
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
        cmsManager = CmsManager.getInstance();

        // Set default store root key
        storeKey = cmsManager.getSingleStoreKey();
        LOGGER.info("Store key is set to " + storeKey);

        nodesByIdCache = new ConcurrentHashMap<String, ContentNodeModel>();
        nodesByKeyCache = new ConcurrentHashMap<ContentKey, ContentNodeModel>();
        keyBySkuCodeCache = new LruCache<String, ContentKey>(40000);
        newProductCache = new ConcurrentHashMap<ProductModel, Map<String, Date>>();
        backInStockProductCache = new ConcurrentHashMap<ProductModel, Map<String, Date>>();
        parentKeysByKeyCache = new ConcurrentHashMap<ContentKey, Set<ContentKey>>();

        currentUserContext = new ThreadLocal<UserContext>() {

            @Override
            protected UserContext initialValue() {
                LOGGER.debug("initializing current user context with default value");
                return UserContext.createDefault(cmsManager.getEStoreEnum());
            }
        };

        eligibleForDDPP = new ThreadLocal<Boolean>() {

            @Override
            protected Boolean initialValue() {
                LOGGER.debug("initializing the eligibleForDDPP with default value");
                return false;
            }
        };

        currentDraftContext = new ThreadLocal<DraftContext>() {

            @Override
            protected DraftContext initialValue() {
                LOGGER.debug("initializing draft context with main draft.");
                return DraftContext.MAIN;
            }
        };
        
        userAllSoData = new ThreadLocal<HashMap<String,Object>> () {
        	@Override
            protected HashMap<String,Object> initialValue() {
                LOGGER.debug("initializing draft context with main draft.");
                return null;
            }
        };
    }

    public synchronized StoreModel getStore() {
        if (store == null) {
            preLoad();

            LOGGER.debug("BEGIN LOAD STORE");
            store = getStoreModel();
            LOGGER.debug("END LOAD STORE: ");
        }
        return store;
    }

    public boolean getPreviewMode() {
        return FDStoreProperties.getPreviewMode();
    }

    public Collection<ProductModel> getProducts(CategoryModel category) {
        List<ProductModel> products = Collections.emptyList();
        if (category.getProductPromotionType() != null && isEligibleForDDPP()) {
            products = category.getProducts();
        } else {
            products = category.getPrivateProducts();
        }
        return Collections.unmodifiableList(products);
    }

    /**
     * Get a ContentNode by id.
     */
    public ContentNodeModel getContentNode(String id) {
        ContentNodeModel model = null;
        if (id != null && !id.trim().equals("")) {

            model = getContentNodeFromCache(id);

            if (model == null) {
                model = ContentNodeModelUtil.constructModel(id, isAllowToUseContentCache(id));
            }

            if (model == null) {
                LOGGER.warn("Content node not found ! getContentNode(\"" + id + "\") returned null!");
            }
        }
        return model;
    }

    public ContentNodeModel getContentNode(String type, String id) {
        return getContentNodeByKey(ContentKey.getContentKey(ContentType.get(type), id));
    }

    public ContentNodeModel getContentNode(ContentType type, String id) {
        return getContentNodeByKey(ContentKey.getContentKey(type, id));
    }

    @Deprecated
    public ProductModel getProduct(ProductModel ref) {
        return ref;
    }

    /**
     * Get a ProductModel in it's primary home, using a skuCode
     */
    public ProductModel getProduct(String skuCode) throws FDSkuNotFoundException {
        // look at cache
        ContentKey productKey = getContentKeyFromCache(skuCode);
        if (productKey != null)
            return (ProductModel) getContentNodeByKey(productKey);

        // get ProductModels for sku
        List<ProductModel> refs = getProdRefsForSku(skuCode);
        if (refs.size() == 0) {
            throw new FDSkuNotFoundException("SKU " + skuCode + " not found");
        }

        // find the one in the PRIMARY_HOME (if there's one)
        ProductModel product = null;
        CategoryModel primaryHome = null;
        Iterator<ProductModel> i = refs.iterator();
        while (i.hasNext()) {
            product = i.next();

            if (product == null) {
                // product not found, skip
                continue;
            }

            if (primaryHome == null) {
                CategoryModel ph = product.getPrimaryHome();
                if (ph != null) {
                    primaryHome = ph;
                }
            }

            if (primaryHome != null && primaryHome.getContentKey().getId().equals(product.getParentId())) {
                // whew, this is in the primary home, cache the result
                updateContentKeyCache(skuCode, product.getContentKey());
                return product;
            }
        }

        if (product == null) {
            throw new FDSkuNotFoundException("Internal inconsistency: SKU " + skuCode + " not found, but ContentFactorySB thinks it does");
        }

        // cache the result
        updateContentKeyCache(skuCode, product.getContentKey());

        return product;
    }

    @Deprecated
    public ProductModel getProduct(String catId, String prodId) {
        return getProductByName(catId, prodId);
    }

    public ProductModel getProductByName(String catName, String prodName) {
        ProductModel productModel = null;

        if (catName != null && prodName != null) {
            CategoryModel category = (CategoryModel) getContentNode(FDContentTypes.CATEGORY, catName);
            if (category != null) {
                productModel = category.getProductByName(prodName);
            }
        }
        return productModel;
    }

    private ProductModel filterProduct(String skuCode) {
        try {
            ProductModel prod = getProduct(skuCode);
            return filterProduct(prod);

        } catch (FDSkuNotFoundException fdsnfe) {
            LOGGER.info("No matching product node for sku " + skuCode);
            return null;
        }
    }

    private ProductModel filterProduct(ProductModel prod) {
        if (prod.isHidden() || !prod.isSearchable() || prod.isUnavailable()) {
            return null;
        }
        return prod;
    }

    private void preLoad() {
        LOGGER.debug("starting store preLoad");
        try {
            // load store via CMS services
            LOGGER.debug("BEGIN GET PRE STORE");

            // Warm-up routine
            loadKey("Store", true);
            loadKey("Brand", true);
            loadKey("Domain", true);
            loadKey("Department", true);
            loadKey("Category", true);
            loadKey("Product", true);
            loadKey("Sku", true);
            loadKey("DomainValue", true);

            LOGGER.debug("END GET PRE STORE");
        } catch (Exception ex) {
            LOGGER.error("preLoad failed :" + ex.getMessage(), ex);
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException("PreLoad failed : " + ex.getMessage(), ex);
            }
        }

        LOGGER.debug("done with store preLoad");
    }

    private Set<ContentKey> loadKey(String type, boolean getNodes) {
        LOGGER.debug("Begin Loading: " + type);
        Set<ContentKey> contentKeys = getContentKeysByType(ContentType.get(type));
        LOGGER.debug("Got keys for: " + type);
        if (getNodes) {
            getContentNodes(contentKeys);
        }
        LOGGER.debug("End Loading: " + type);
        return contentKeys;
    }

    public Domain getDomainById(String domainId) {
        Domain domainModel = null;

        domainModel = (Domain) getContentNodeFromCache("Domain:" + domainId);

        if (domainModel == null) {
            ContentKey key = ContentKey.getContentKey("Domain:" + domainId);
            // Don't cache regular way; do our own caching
            domainModel = (Domain) ContentNodeModelUtil.constructModel(key, false);
        }

        updateContentNodeCaches("Domain:" + domainId, domainModel);

        return domainModel;
    }

    public DomainValue getDomainValueById(String domainValueId) {
        DomainValue domainValueModel = null;

        domainValueModel = (DomainValue) getContentNodeFromCache("DomainValue:" + domainValueId);

        if (domainValueModel == null) {
            // Don't cache regular way; do our own caching
            ContentKey key = ContentKey.getContentKey("DomainValue:" + domainValueId);
            domainValueModel = (DomainValue) ContentNodeModelUtil.constructModel(key, false);
            List<ContentKey> domainValueKeys = ((Domain) domainValueModel.getParentNode()).getDomainValueKeys();
            for (int i = 0; i < domainValueKeys.size(); i++) {
                ContentKey domainValueKey = domainValueKeys.get(i);
                if (domainValueKey.equals(domainValueModel.getContentKey())) {
                    domainValueModel.setPriority(i + 1);
                    break;
                }
            }
        }

        updateContentNodeCaches("DomainValue:" + domainValueId, domainValueModel);

        return domainValueModel;
    }

    private List<ProductModel> getProdRefsForSku(String skuCode) {
        List<ProductModel> productReferences = new ArrayList<ProductModel>();
        ContentNodeI skuNode = getContentNode(ContentKey.getContentKey(FDContentTypes.SKU, skuCode));
        if (skuNode != null) {
            for (ContentKey productKey : getParentKeys(skuNode.getKey())) {
                for (ContentKey categoryKey : getParentKeys(productKey)) {
                    productReferences.add(getProductByName(categoryKey.getId(), productKey.getId()));
                }
            }
        }
        return productReferences;
    }

    public ContentNodeModel getContentNodeByKey(ContentKey key) {
        ContentNodeModel model = getContentNodeFromCache(key);
        if (model == null && getContentNode(key) != null) {
            model = ContentNodeModelUtil.constructModel(key, true);
        }
        return model;
    }

    public Set<ContentKey> getParentKeys(ContentKey key) {
        Set<ContentKey> parentContentKeys = parentKeysByKeyCache.get(key);
        if (parentContentKeys == null){
            parentContentKeys = cmsManager.getParentKeys(key, getCurrentDraftContext());
            if (isAllowToUseContentCache(key.getId())) {
                parentKeysByKeyCache.put(key, parentContentKeys);
            }
        }
        return Collections.unmodifiableSet(parentContentKeys);
    }

    public UserContext getCurrentUserContext() {
        return currentUserContext.get();
    }

    public void setCurrentUserContext(UserContext userContext) {
        currentUserContext.set(userContext);
    }

    public Boolean isEligibleForDDPP() {
        return eligibleForDDPP.get();
    }

    public void setEligibleForDDPP(Boolean isEligible) {
        eligibleForDDPP.set(isEligible);
    }

    public DraftContext getCurrentDraftContext() {
        return currentDraftContext.get();
    }

    public void setCurrentDraftContext(DraftContext draftContext) {
        this.currentDraftContext.set(draftContext);
    }

    /**
     * Return the products and their corresponding dates of becoming new
     * 
     * @return a {@link Map} of {@link ProductModel} - {@link Date} pairs
     */
    public Map<ProductModel, Map<String, Date>> getNewProducts() {
        if (System.currentTimeMillis() > newProductsLastUpdated + NEW_AND_BACK_REFRESH_PERIOD) {
            // refresh
            try {
                Map<String, Map<String, Date>> skus = FDCachedFactory.getNewSkus();
                Map<ProductModel, Map<String, Date>> newCache = new HashMap<ProductModel, Map<String, Date>>(skus.size());
                for (Map.Entry<String, Map<String, Date>> entry : skus.entrySet()) {
                    SkuModel sku = (SkuModel) getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.SKU, entry.getKey()));
                    // !!! I'm very paranoid and I don't really trust in these developer clusters !!! (by cssomogyi, on 15 Oct 2010)
                    try {

                        if (sku != null && sku.getProductModel() != null && sku.getProductInfo() != null && !sku.isUnavailable()) {
                            ProductModel p = filterProduct(sku.getContentName());
                            // Origin : [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
                            if (p != null && ContentUtil.isAvailableByContext(p)) {
                                Map<String, Date> productNewness = newCache.get(p);
                                for (Map.Entry<String, Date> valueEntry : entry.getValue().entrySet()) {

                                    Date prev = null;
                                    if (productNewness != null)
                                        prev = productNewness.get(valueEntry.getKey());
                                    else
                                        productNewness = new HashMap<String, Date>();
                                    if (prev == null || valueEntry.getValue().after(prev)) {
                                        productNewness.put(valueEntry.getKey(), valueEntry.getValue());
                                        newCache.put(p, productNewness);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.warn("skipping sku " + sku.getContentName() + " due to data discrepancy", e);
                    }
                }
                newProductCache.clear();
                newProductCache.putAll(newCache);
                newProductsLastUpdated = System.currentTimeMillis();
                LOGGER.info("reloaded new products: " + newProductCache.size());
            } catch (FDResourceException e) {
                LOGGER.error("failed to update new products cache; retrying a minute later", e);
                newProductsLastUpdated = System.currentTimeMillis() - 14 * 60000;
            }
        }
        return Collections.unmodifiableMap(newProductCache);
    }

    /**
     * Return the products new in the last n days
     * 
     * @param inDays
     *            the n days criteria
     * @return the list of the products in the sorted order where the most recent product comes first
     */
    public LinkedHashMap<ProductModel, Date> getNewProducts(int inDays) {
        final Map<ProductModel, Map<String, Date>> x = getNewProducts();
        final Map<ProductModel, Date> y = new HashMap<ProductModel, Date>();
        LinkedHashMap<ProductModel, Date> productsInDays = new LinkedHashMap<ProductModel, Date>();
        ZoneInfo zone = getCurrentUserContext().getPricingContext().getZoneInfo();
        String productNewnessKey = "";
        if (zone != null) {
            productNewnessKey = new StringBuilder(5).append(zone.getSalesOrg()).append(zone.getDistributionChanel()).toString();
        }
        List<ProductModel> products = new ArrayList<ProductModel>(x.size());
        long limit = System.currentTimeMillis() - inDays * DAY_IN_MILLISECONDS;
        for (Map.Entry<ProductModel, Map<String, Date>> entry : x.entrySet()) {
            Map<String, Date> valueEntries = entry.getValue();
            for (Map.Entry<String, Date> valueEntry : valueEntries.entrySet()) {
                if (valueEntry.getKey().equals(productNewnessKey) && valueEntry.getValue().getTime() > limit) {
                    products.add(entry.getKey());
                    y.put(entry.getKey(), valueEntry.getValue());
                }
            }
        }
        Collections.sort(products, new Comparator<ProductModel>() {

            @Override
            public int compare(ProductModel o1, ProductModel o2) {

                return -y.get(o1).compareTo(y.get(o2));
            }
        });

        for (ProductModel p : products)
            productsInDays.put(p, y.get(p));
        return productsInDays;

    }

    /**
     * Returns for how many days the product has been new.
     * 
     * @param product
     * @return the number of days (plus fraction of day)
     */
    public double getProductAge(ProductModel product) {
        String productNewnessKey = getProductNewnessKey(product);
        Map<ProductModel, Map<String, Date>> newProducts = getNewProducts();
        if (newProducts.containsKey(product)) {
            Date when = newProducts.get(product).get(productNewnessKey);
            return when == null ? Integer.MAX_VALUE : ((double) System.currentTimeMillis() - when.getTime()) / DAY_IN_MILLISECONDS;
        } else {
            return Integer.MAX_VALUE; // very long time ago
        }
    }

    private String getProductNewnessKey(ProductModel product) {
        String key = "";
        ZoneInfo zone = product.getUserContext().getPricingContext().getZoneInfo();
        if (zone != null) {
            key = new StringBuilder(5).append(zone.getSalesOrg()).append(zone.getDistributionChanel()).toString();
        }
        return key;
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
    public Map<ProductModel, Map<String, Date>> getBackInStockProducts() {
        if (System.currentTimeMillis() > backInStockProductsLastUpdated + NEW_AND_BACK_REFRESH_PERIOD) {
            // refresh
            try {
                Map<String, Map<String, Date>> skus = FDCachedFactory.getBackInStockSkus();
                Map<ProductModel, Map<String, Date>> newCache = new HashMap<ProductModel, Map<String, Date>>(skus.size());
                for (Map.Entry<String, Map<String, Date>> entry : skus.entrySet()) {
                    SkuModel sku = (SkuModel) getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.SKU, entry.getKey()));
                    // !!! I'm very paranoid and I don't really trust in these developer clusters !!! (by cssomogyi, on 15 Oct 2010)
                    try {
                        if (sku != null && sku.getProductModel() != null && sku.getProductInfo() != null && !sku.isUnavailable()) {
                            ProductModel p = filterProduct(sku.getContentName());
                            // Origin : [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
                            if (p != null && ContentUtil.isAvailableByContext(p)) {
                                Map<String, Date> productNewness = newCache.get(p);
                                for (Map.Entry<String, Date> valueEntry : entry.getValue().entrySet()) {

                                    Date prev = null;
                                    if (productNewness != null)
                                        prev = productNewness.get(valueEntry.getKey());
                                    else
                                        productNewness = new HashMap<String, Date>();
                                    if (prev == null || valueEntry.getValue().after(prev)) {
                                        productNewness.put(valueEntry.getKey(), valueEntry.getValue());
                                        newCache.put(p, productNewness);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.warn("skipping sku " + sku.getContentName() + " due to data discrepancy", e);
                    }
                }
                backInStockProductCache.clear();
                backInStockProductCache.putAll(newCache);
                backInStockProductsLastUpdated = System.currentTimeMillis();
                LOGGER.info("reloaded back-in-stock products: " + backInStockProductCache.size());
            } catch (FDResourceException e) {
                LOGGER.error("failed to update back-in-stock products cache; retrying a minute later", e);
                backInStockProductsLastUpdated = System.currentTimeMillis() - 14 * 60000;
            }
        }
        return Collections.unmodifiableMap(backInStockProductCache);
    }

    /**
     * Return the products back-in-stock in the last n days
     * 
     * @param inDays
     *            the n days criteria
     * @return the list of the products in the sorted order where the most recent product comes first
     */
    public LinkedHashMap<ProductModel, Date> getBackInStockProducts(int inDays) {
        final Map<ProductModel, Map<String, Date>> x = getBackInStockProducts();
        final Map<ProductModel, Date> y = new HashMap<ProductModel, Date>();
        LinkedHashMap<ProductModel, Date> productsInDays = new LinkedHashMap<ProductModel, Date>();
        ZoneInfo zone = getCurrentUserContext().getPricingContext().getZoneInfo();
        String productNewnessKey = "";
        if (zone != null) {
            productNewnessKey = new StringBuilder(5).append(zone.getSalesOrg()).append(zone.getDistributionChanel()).toString();
        }
        List<ProductModel> products = new ArrayList<ProductModel>(x.size());
        long limit = System.currentTimeMillis() - inDays * DAY_IN_MILLISECONDS;
        for (Map.Entry<ProductModel, Map<String, Date>> entry : x.entrySet()) {
            Map<String, Date> valueEntries = entry.getValue();
            for (Map.Entry<String, Date> valueEntry : valueEntries.entrySet()) {
                if (valueEntry.getKey().equals(productNewnessKey) && valueEntry.getValue().getTime() > limit) {
                    products.add(entry.getKey());
                    y.put(entry.getKey(), valueEntry.getValue());
                }
            }
        }
        Collections.sort(products, new Comparator<ProductModel>() {

            @Override
            public int compare(ProductModel o1, ProductModel o2) {

                return -y.get(o1).compareTo(y.get(o2));
            }
        });

        for (ProductModel p : products)
            productsInDays.put(p, y.get(p));
        return productsInDays;
    }

    /**
     * Returns for how many days the product has been back in stock.
     * 
     * @param product
     * @return the number of days (plus fraction of day)
     */
    public double getBackInStockProductAge(ProductModel product) {
        String productNewnessKey = getProductNewnessKey(product);
        Map<ProductModel, Map<String, Date>> newProducts = getBackInStockProducts();
        if (newProducts.containsKey(product)) {
            Date when = newProducts.get(product).get(productNewnessKey);
            return when == null ? Integer.MAX_VALUE : ((double) System.currentTimeMillis() - when.getTime()) / DAY_IN_MILLISECONDS;
        } else {
            return Integer.MAX_VALUE; // very long time ago
        }
    }

    /**
     * Forces to refresh the new and back-in-stock products cache
     */
    public void refreshNewAndBackCache() {
        newProductsLastUpdated = Integer.MIN_VALUE;
        getNewProducts();
        backInStockProductsLastUpdated = Integer.MIN_VALUE;
        getBackInStockProducts();
    }

    private void buildWineIndex() {
        Set<Domain> wineDomains = new HashSet<Domain>(100);
        String winePrefix = FDStoreProperties.getWineAssid().toLowerCase();
        if ("fdw".equalsIgnoreCase(winePrefix)) {
            winePrefix = "vin"; // make it match CMS setup
        }

        WineIndex newIndex = new WineIndex();
        // fetch wine domains
        LOGGER.info("WINE INDEX: collecting domains...");
        FDFolder domains = (FDFolder) getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.FDFOLDER, "domains"));
        for (ContentNodeModel node : domains.getChildren()) {
            if (!node.getContentKey().getType().equals(FDContentTypes.DOMAIN))
                continue;

            if (node.getContentKey().getId().startsWith("wine_"))
                wineDomains.add((Domain) node);
        }
        LOGGER.info("WINE INDEX: collected " + wineDomains.size() + " domains.");

        LOGGER.info("WINE INDEX: collecting all wine products...");
        CategoryModel byRegion = (CategoryModel) getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.CATEGORY, winePrefix + "_region"));
        if (byRegion != null)
            newIndex.all.addAll(byRegion.getAllChildProductKeys());
        CategoryModel byType = (CategoryModel) getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.CATEGORY, winePrefix + "_type"));
        if (byType != null)
            newIndex.all.addAll(byType.getAllChildProductKeys());
        CategoryModel more = (CategoryModel) getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.CATEGORY, winePrefix + "_more"));
        if (more != null)
            newIndex.all.addAll(more.getAllChildProductKeys());
        LOGGER.info("WINE INDEX: collected all " + newIndex.all.size() + " wine products");

        Set<DomainValue> regions = new HashSet<DomainValue>(1000);
        LOGGER.info("WINE INDEX: collecting domain value - product pairs...");
        for (Domain d : wineDomains) {
            if (d.getContentKey().getId().startsWith("wine_region_"))
                regions.addAll(d.getDomainValues());
            for (DomainValue dv : d.getDomainValues()) {
                if (dv.getDomain().getContentKey().getId().startsWith("wine_region_")) {
                    newIndex.encodedDomains.put(dv, "_Domain:wine_region");
                }
                for (ContentKey key : newIndex.all) {
                    ProductModel p = (ProductModel) getContentNodeByKey(key);
                    if (p.getWineDomainValues().contains(dv)) {
                        if (!newIndex.index.containsKey(dv))
                            newIndex.index.put(dv, new HashSet<ContentKey>());
                        newIndex.index.get(dv).add(key);
                    }
                }
            }
        }
        LOGGER.info("WINE INDEX: collected domain value - product pairs for " + newIndex.index.size() + " domain values");

        if (byRegion != null) {
            LOGGER.info("WINE INDEX: resolving TLC - domain value pairs (By Region as " + byRegion.toString() + ")...");
            Set<WineFilterValue> domainValues = new HashSet<WineFilterValue>();
            Domain domain = (Domain) getContentNode(FDContentTypes.DOMAIN, "wine_country_usq");
            if (domain != null)
                domainValues.addAll(domain.getDomainValues());
            else
                LOGGER.warn("WINE INDEX: -- wine country domain values cannot be resolved");
            newIndex.categoryDomains.put(byRegion.getContentKey(), domainValues);
            for (CategoryModel c : byRegion.getSubcategories()) {
                Set<DomainValue> subDomains = new HashSet<DomainValue>();
                Map<DomainValue, Integer> counters = new LinkedHashMap<DomainValue, Integer>();
                for (ContentKey key : c.getAllChildProductKeys()) {
                    ProductModel p = (ProductModel) getContentNodeByKey(key);
                    DomainValue country = p.getWineCountry();
                    if (!counters.containsKey(country))
                        counters.put(country, 0);
                    counters.put(country, counters.get(country) + 1);
                    for (DomainValue region : p.getNewWineRegion())
                        subDomains.add(region);
                }
                List<Map.Entry<DomainValue, Integer>> values = new ArrayList<Map.Entry<DomainValue, Integer>>(counters.entrySet());
                Collections.sort(values, new Comparator<Map.Entry<DomainValue, Integer>>() {

                    @Override
                    public int compare(Entry<DomainValue, Integer> v1, Entry<DomainValue, Integer> v2) {
                        // in descending order
                        return v2.getValue() - v1.getValue();
                    }
                });
                newIndex.subDomains.put(c.getContentKey(), subDomains);
                if (values.size() > 0) {
                    newIndex.categories.put(c.getContentKey(), values.get(0).getKey());
                    newIndex.catReverse.put(values.get(0).getKey(), c.getContentKey());
                    LOGGER.info("WINE INDEX: -- matching domain value found for category " + c.getFullName() + ": " + values.get(0).getKey().getLabel());
                } else
                    LOGGER.warn("WINE INDEX: -- no matching domain value found for category " + c.toString() + ": " + c.getFullName());
            }
        }
        LOGGER.info("WINE INDEX: resolved " + newIndex.categories.size() + " TLC - domain value pairs (By Region).");

        int soFar = newIndex.categories.size();
        LOGGER.info("WINE INDEX: resolving TLC - domain value pairs (By Type)...");
        if (byType != null) {
            @SuppressWarnings("unchecked")
            Set<WineFilterValue> domainValues = new ListOrderedSet();
            newIndex.categoryDomains.put(byType.getContentKey(), domainValues);
            for (CategoryModel c : byType.getSubcategories()) {
                Set<DomainValue> subDomains = new HashSet<DomainValue>();
                Map<DomainValue, Integer> counters = new LinkedHashMap<DomainValue, Integer>();
                for (ContentKey key : c.getAllChildProductKeys()) {
                    ProductModel p = (ProductModel) getContentNodeByKey(key);
                    List<DomainValue> types = p.getNewWineType();
                    for (DomainValue type : types) {
                        if (!counters.containsKey(type))
                            counters.put(type, 0);
                        counters.put(type, counters.get(type) + 1);
                    }
                    for (DomainValue varietal : p.getWineVarietal())
                        subDomains.add(varietal);
                }
                List<Map.Entry<DomainValue, Integer>> values = new ArrayList<Map.Entry<DomainValue, Integer>>(counters.entrySet());
                Collections.sort(values, new Comparator<Map.Entry<DomainValue, Integer>>() {

                    @Override
                    public int compare(Entry<DomainValue, Integer> v1, Entry<DomainValue, Integer> v2) {
                        // in descending order
                        return v2.getValue() - v1.getValue();
                    }
                });
                newIndex.subDomains.put(c.getContentKey(), subDomains);
                DomainValue overridden = c.getWineFilterValue();
                if (overridden != null) {
                    newIndex.categories.put(c.getContentKey(), overridden);
                    newIndex.catReverse.put(overridden, c.getContentKey());
                    newIndex.encodedDomains.put(overridden, "_Domain:usq_type");
                    newIndex.encodedDomains.put(overridden, "_Domain:vin_type");
                    domainValues.add(overridden);
                    LOGGER.info("WINE INDEX: -- overridden domain value found for category " + c.getFullName() + ": " + overridden.getLabel());
                } else if (values.size() > 0) {
                    DomainValue dv = values.get(0).getKey();
                    newIndex.categories.put(c.getContentKey(), overridden != null ? overridden : dv);
                    newIndex.catReverse.put(overridden != null ? overridden : dv, c.getContentKey());
                    newIndex.encodedDomains.put(dv, "_Domain:usq_type");
                    newIndex.encodedDomains.put(dv, "_Domain:vin_type");
                    domainValues.add(dv);
                    LOGGER.info("WINE INDEX: -- matching domain value found for category " + c.getFullName() + ": " + dv.getLabel());
                } else
                    LOGGER.warn("WINE INDEX: -- no matching domain value found for category " + c.toString() + ": " + c.getFullName());
            }
        }
        LOGGER.info("WINE INDEX: resolved " + (newIndex.categories.size() - soFar) + " TLC - domain value pairs (By Type).");

        soFar = newIndex.categories.size();
        LOGGER.info("WINE INDEX: resolving TLC - domain value pairs (More...)...");
        if (more != null) {
            @SuppressWarnings("unchecked")
            Set<WineFilterValue> domainValues = new ListOrderedSet();
            newIndex.categoryDomains.put(more.getContentKey(), domainValues);
            for (CategoryModel c : more.getSubcategories()) {
                Map<DomainValue, Integer> counters = new LinkedHashMap<DomainValue, Integer>();
                for (ContentKey key : c.getAllChildProductKeys()) {
                    ProductModel p = (ProductModel) getContentNodeByKey(key);
                    List<DomainValue> types = p.getNewWineType();
                    for (DomainValue type : types) {
                        if (!counters.containsKey(type))
                            counters.put(type, 0);
                        counters.put(type, counters.get(type) + 1);
                    }
                }
                List<Map.Entry<DomainValue, Integer>> values = new ArrayList<Map.Entry<DomainValue, Integer>>(counters.entrySet());
                Collections.sort(values, new Comparator<Map.Entry<DomainValue, Integer>>() {

                    @Override
                    public int compare(Entry<DomainValue, Integer> v1, Entry<DomainValue, Integer> v2) {
                        // in descending order
                        return v2.getValue() - v1.getValue();
                    }
                });
                DomainValue overridden = c.getWineFilterValue();
                if (overridden != null) {
                    newIndex.categories.put(c.getContentKey(), overridden);
                    newIndex.catReverse.put(overridden, c.getContentKey());
                    newIndex.encodedDomains.put(overridden, "_Domain:usq_more");
                    newIndex.encodedDomains.put(overridden, "_Domain:vin_more");
                    domainValues.add(overridden);
                    LOGGER.info("WINE INDEX: -- overridden domain value found for category " + c.getFullName() + ": " + overridden.getLabel());
                } else if (values.size() > 0) {
                    DomainValue dv = values.get(0).getKey();
                    newIndex.categories.put(c.getContentKey(), dv);
                    newIndex.catReverse.put(dv, c.getContentKey());
                    newIndex.encodedDomains.put(dv, "_Domain:usq_more");
                    newIndex.encodedDomains.put(dv, "_Domain:vin_more");
                    domainValues.add(dv);
                    LOGGER.info("WINE INDEX: -- matching domain value found for category " + c.getFullName() + ": " + dv.getLabel());
                } else
                    LOGGER.warn("WINE INDEX: -- no matching domain value found for category " + c.toString() + ": " + c.getFullName());
            }
        }
        LOGGER.info("WINE INDEX: resolved " + (newIndex.categories.size() - soFar) + " TLC - domain value pairs (More...).");
        wineIndex = newIndex;
    }

    public void refreshWineIndex(boolean force) {
        synchronized (wineIndexLock) {
            if ((force || wineIndex == null) && isAllowToUseContentCache()) {
                buildWineIndex();
            }
        }
    }

    public Collection<ContentKey> getWineProductKeysByDomainValue(DomainValue value) {
        refreshWineIndex(false);
        Set<ContentKey> v = wineIndex.index.get(value);
        return v != null ? Collections.unmodifiableCollection(v) : Collections.<ContentKey> emptyList();
    }

    public Collection<ContentKey> getAllWineProductKeys() {
        refreshWineIndex(false);
        return Collections.unmodifiableCollection(wineIndex.all);
    }

    public DomainValue getDomainValueForWineCategory(CategoryModel category) {
        refreshWineIndex(false);
        return wineIndex.categories.get(category.getContentKey());
    }

    public CategoryModel getCategoryForWineDomainValue(DomainValue dv) {
        refreshWineIndex(false);
        ContentKey key = wineIndex.catReverse.get(dv);
        if (key == null) {
            return null;
        }
        return (CategoryModel) getContentNodeByKey(key);
    }

    public String getDomainEncodedForWineDomainValue(DomainValue value) {
        refreshWineIndex(false);
        return wineIndex.encodedDomains.get(value);
    }

    public Collection<WineFilterValue> getDomainValuesForWineDomainCategory(CategoryModel category) {
        refreshWineIndex(false);
        if (category == null)
            return null;
        return wineIndex.categoryDomains.get(category.getContentKey());
    }

    public Collection<DomainValue> getSubDomainValuesForWineDomainValue(DomainValue domainValue) {
        refreshWineIndex(false);
        CategoryModel domainCat = getCategoryForWineDomainValue(domainValue);
        if (domainCat != null)
            return wineIndex.subDomains.get(domainCat.getContentKey());
        else
            return null;
    }

    public synchronized GrabberServiceI getProductGrabberService() {
        if (grabberService == null) {
            grabberService = (GrabberServiceI) FDRegistry.getInstance().getService(GrabberServiceI.class);

            if (grabberService == null) {
                LOGGER.warn("Product Grabber service is not available yet");
            }
        }

        return grabberService;
    }

    /**
     * DON'T USE IT! Retained for test purposes!
     * 
     * @param svc
     */
    void setProductGrabberService(GrabberServiceI grabberService) {
        this.grabberService = grabberService;
    }

    public ContentNodeI getContentNode(ContentKey key) {
        return cmsManager.getContentNode(key, getCurrentDraftContext());
    }

    public Set<ContentKey> getContentKeysByType(ContentType contentType) {
        return cmsManager.getContentKeysByType(contentType, getCurrentDraftContext());
    }

    public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> contentKeys) {
        return cmsManager.getContentNodes(contentKeys, getCurrentDraftContext());
    }

    public ContentKey getPrimaryHomeKey(ContentKey key) {
        return cmsManager.getPrimaryHomeKey(key, getCurrentDraftContext());
    }

    public ContentNodeModel getContentNodeFromCache(ContentKey key) {
        ContentNodeModel model = null;
        if (isAllowToUseContentCache(key.getId())) {
            model = nodesByKeyCache.get(key);
        }
        return model;
    }

    public ContentNodeModel getContentNodeFromCache(String nodeId) {
        ContentNodeModel model = null;
        if (isAllowToUseContentCache(nodeId)) {
            model = nodesByIdCache.get(nodeId);
        }
        return model;
    }

    public void updateContentNodeCaches(String nodeId, ContentNodeModel nodeModel) {
        if (isAllowToUseContentCache(nodeId)) {
            nodesByIdCache.put(nodeId, nodeModel);
            nodesByKeyCache.put(nodeModel.getContentKey(), nodeModel);
        }
    }

    public void removeContentNodeCaches(ContentKey key) {
        if (isAllowToUseContentCache(key.getId())) {
            nodesByIdCache.remove(key.getId());
            nodesByKeyCache.remove(key);
        }
    }

    public void updateContentKeyCache(String skuCode, ContentKey key) {
        if (isAllowToUseContentCache()) {
            keyBySkuCodeCache.put(skuCode, key);
        }
    }

    public ContentKey getContentKeyFromCache(String skuCode) {
        ContentKey contentKey = null;
        if (isAllowToUseContentCache()) {
            contentKey = keyBySkuCodeCache.get(skuCode);
        }
        return contentKey;
    }

    public boolean isAllowToUseContentCache() {
        return getCurrentDraftContext().isMainDraft();
    }

    public boolean isAllowToUseContentCache(String nodeId) {
        boolean isAllowToUseContentCache = isAllowToUseContentCache();
        if (!isAllowToUseContentCache){
            isAllowToUseContentCache = !DraftService.defaultService().isContentKeyChanged(getCurrentDraftContext().getDraftId(), nodeId);
        }
        return isAllowToUseContentCache;
    }

	public HashMap<String, Object> getUserAllSoData() {
		return userAllSoData.get();
	}

	public void setUserAllSoData(HashMap<String, Object> userAllSoData) {
		this.userAllSoData.set(userAllSoData);
	}
		  
}
