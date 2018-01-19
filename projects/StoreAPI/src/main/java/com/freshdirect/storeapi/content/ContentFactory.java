package com.freshdirect.storeapi.content;

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
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.common.context.FulfillmentContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.GrabberServiceLocator;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.grabber.GrabberServiceI;

public class ContentFactory {

    private final static Category LOGGER = LoggerFactory.getInstance(ContentFactory.class);

    private static ContentFactory instance = new ContentFactory();
    private static final String NODES_BY_ID_CACHE_NAME = "nodesByIdCache";
    private static final String NODES_BY_KEY_CACHE_NAME = "nodesByKeyCache";

    private static final long NEW_AND_BACK_REFRESH_PERIOD = 1000 * 60 * 15; // 15 minutes
    private static final long DAY_IN_MILLISECONDS = 1000 * 3600 * 24;

    private long newProductsLastUpdated;
    private long backInStockProductsLastUpdated;
    private long goingOutOfStockProductsLastUpdated;

    private StoreModel store;

    private Cache nodesByIdCache;
    private Cache nodesByKeyCache;

    private final Map<ContentKey, Map<String, Date>> newProductCache;
    private final Map<ContentKey, Map<String, Date>> backInStockProductCache;
    private final Map<ContentKey, Collection<String>> goingOutOfStockProductCache;

    private ThreadLocal<UserContext> currentUserContext;
    private ThreadLocal<Boolean> eligibleForDDPP;

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
        // Set default store root key
        storeKey = CmsManager.getInstance().getSingleStoreKey();
        LOGGER.info("Store key is set to " + storeKey);

        nodesByIdCache = CmsServiceLocator.cacheManager().getCache(NODES_BY_ID_CACHE_NAME);
        nodesByKeyCache = CmsServiceLocator.cacheManager().getCache(NODES_BY_KEY_CACHE_NAME);
        newProductCache = new ConcurrentHashMap<ContentKey, Map<String, Date>>();
        backInStockProductCache = new ConcurrentHashMap<ContentKey, Map<String, Date>>();
        goingOutOfStockProductCache = new HashMap<ContentKey, Collection<String>>();

        currentUserContext = new ThreadLocal<UserContext>() {

            @Override
            protected UserContext initialValue() {
                LOGGER.debug("initializing current user context with default value");
                return UserContext.createUserContext( CmsManager.getInstance().getEStoreEnum());
            }
        };

        eligibleForDDPP = new ThreadLocal<Boolean>() {

            @Override
            protected Boolean initialValue() {
                LOGGER.debug("initializing the eligibleForDDPP with default value");
                return false;
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
                model = ContentNodeModelUtil.constructModel(id, isAllowToUseContentCache());
                if (model != null) {
                    updateContentNodeCaches(id, model);
                }
            }

            if (model == null) {
                LOGGER.warn("Content node not found ! getContentNode(\"" + id + "\") returned null!");
            }
        }
        return model;
    }

    public ContentNodeModel getContentNode(String type, String id) {
        return getContentNode(ContentType.valueOf(type), id);
    }

    public ContentNodeModel getContentNode(ContentType type, String id) {
        return getContentNodeByKey(ContentKeyFactory.get(type, id));
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

            if (primaryHome != null && primaryHome.getContentKey().id.equals(product.getParentId())) {
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
            CategoryModel category = (CategoryModel) getContentNode(ContentType.Category, catName);
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
            loadKey(ContentType.Store);
            loadKey(ContentType.Brand);
            loadKey(ContentType.Domain);
            loadKey(ContentType.Department);
            loadKey(ContentType.Category);
            loadKey(ContentType.Product);
            loadKey(ContentType.Sku);
            loadKey(ContentType.DomainValue);

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

    private Set<ContentKey> loadKey(ContentType type) {
        LOGGER.debug("Begin Loading: " + type);
        Set<ContentKey> contentKeys = CmsManager.getInstance().getContentKeysByType(type);
        LOGGER.debug("Got keys for: " + type);
        LOGGER.debug("End Loading: " + type);
        return contentKeys;
    }

    public Domain getDomainById(String domainId) {
        Domain domainModel = null;

        domainModel = (Domain) getContentNodeFromCache("Domain:" + domainId);

        if (domainModel == null) {
            ContentKey key = ContentKeyFactory.get(ContentType.Domain, domainId);
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
            ContentKey key = ContentKeyFactory.get(ContentType.DomainValue, domainValueId);
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
        ContentKey skuKey = ContentKeyFactory.get(ContentType.Sku, skuCode);
        ContentNodeI skuNode = getContentNode(skuKey);
        if (skuNode != null) {
            for (ContentKey productKey : getParentKeys(skuKey)) {
                for (ContentKey categoryKey : getParentKeys(productKey)) {
                    productReferences.add(getProductByName(categoryKey.id, productKey.id));
                }
            }
        }
        return productReferences;
    }

    private ContentNodeI getContentNode(ContentKey key) {
        return CmsManager.getInstance().getContentNode(key);
    }

    public ContentNodeModel getContentNodeByKey(ContentKey key) {
        ContentNodeModel model = getContentNodeFromCache(key);
        if (model == null && getContentNode(key) != null) {
            model = ContentNodeModelUtil.constructModel(key, true);
        }
        return model;
    }

    public Set<ContentKey> getParentKeys(ContentKey key) {
        return CmsManager.getInstance().getParentKeys(key);
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

    /**
     * Return the products and their corresponding dates of becoming new
     *
     * @return a {@link Map} of {@link ProductModel} - {@link Date} pairs
     */
    public Map<ContentKey, Map<String, Date>> getNewProducts() {
        if (System.currentTimeMillis() > newProductsLastUpdated + NEW_AND_BACK_REFRESH_PERIOD) {
            // refresh
            try {
                Map<String, Map<String, Date>> skus = FDCachedFactory.getNewSkus();
                Map<ContentKey, Map<String, Date>> newCache = new HashMap<ContentKey, Map<String, Date>>(skus.size());
                for (Map.Entry<String, Map<String, Date>> entry : skus.entrySet()) {
                    ContentKey skuKey = ContentKeyFactory.get(ContentType.Sku, entry.getKey());
                    SkuModel sku = (SkuModel) getContentNodeByKey(skuKey);
                    // !!! I'm very paranoid and I don't really trust in these developer clusters !!! (by cssomogyi, on 15 Oct 2010)
                    try {

                        if (sku != null && sku.getProductModel() != null && sku.getProductInfo() != null && !sku.isUnavailable()) {
                            ProductModel p = filterProduct(sku.getContentName());
                            // Origin : [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
                            if (p != null && ContentUtil.isAvailableByContext(p)) {
                                Map<String, Date> productNewness = newCache.get(p.getContentKey());
                                for (Map.Entry<String, Date> valueEntry : entry.getValue().entrySet()) {

                                    Date prev = null;
                                    if (productNewness != null)
                                        prev = productNewness.get(valueEntry.getKey());
                                    else
                                        productNewness = new HashMap<String, Date>();
                                    if (prev == null || valueEntry.getValue().after(prev)) {
                                        productNewness.put(valueEntry.getKey(), valueEntry.getValue());
                                        newCache.put(p.getContentKey(), productNewness);
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
        final Map<ContentKey, Map<String, Date>> allNewProducts = getNewProducts();
        final Map<ProductModel, Date> productsWithRecency = new HashMap<ProductModel, Date>();
        LinkedHashMap<ProductModel, Date> productsInDays = new LinkedHashMap<ProductModel, Date>();
        ZoneInfo zone = getCurrentUserContext().getPricingContext().getZoneInfo();
        String productNewnessKey = "";
        if (zone != null) {
            productNewnessKey = new StringBuilder(5).append(zone.getSalesOrg()).append(zone.getDistributionChanel()).toString();
        }
        List<ProductModel> products = new ArrayList<ProductModel>(allNewProducts.size());
        long limit = System.currentTimeMillis() - inDays * DAY_IN_MILLISECONDS;
        for (Map.Entry<ContentKey, Map<String, Date>> entry : allNewProducts.entrySet()) {
            Map<String, Date> valueEntries = entry.getValue();
            for (Map.Entry<String, Date> valueEntry : valueEntries.entrySet()) {
                if (valueEntry.getKey().equals(productNewnessKey) && valueEntry.getValue().getTime() > limit) {
                    ProductModel product = (ProductModel) getContentNodeByKey(entry.getKey());
                    products.add(product);
                    productsWithRecency.put(product, valueEntry.getValue());
                }
            }
        }
        Collections.sort(products, new Comparator<ProductModel>() {

            @Override
            public int compare(ProductModel o1, ProductModel o2) {
                return -productsWithRecency.get(o1).compareTo(productsWithRecency.get(o2));
            }
        });

        for (ProductModel p : products) {
            productsInDays.put(p, productsWithRecency.get(p));
        }
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
        Map<ContentKey, Map<String, Date>> newProducts = getNewProducts();
        ContentKey productContentKey = product.getContentKey();
        if (newProducts.containsKey(productContentKey)) {
            Date when = newProducts.get(productContentKey).get(productNewnessKey);
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
    public Map<ContentKey, Map<String, Date>> getBackInStockProducts() {
        if (System.currentTimeMillis() > backInStockProductsLastUpdated + NEW_AND_BACK_REFRESH_PERIOD) {
            // refresh
            try {
                Map<String, Map<String, Date>> skus = FDCachedFactory.getBackInStockSkus();
                Map<ContentKey, Map<String, Date>> newCache = new HashMap<ContentKey, Map<String, Date>>(skus.size());
                for (Map.Entry<String, Map<String, Date>> entry : skus.entrySet()) {
                    ContentKey skuKey = ContentKeyFactory.get(ContentType.Sku, entry.getKey());
                    SkuModel sku = (SkuModel) getContentNodeByKey(skuKey);
                    // !!! I'm very paranoid and I don't really trust in these developer clusters !!! (by cssomogyi, on 15 Oct 2010)
                    try {
                        if (sku != null && sku.getProductModel() != null && sku.getProductInfo() != null && !sku.isUnavailable()) {
                            ProductModel p = filterProduct(sku.getContentName());
                            // Origin : [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
                            if (p != null && ContentUtil.isAvailableByContext(p)) {
                                Map<String, Date> productNewness = newCache.get(p.getContentKey());
                                for (Map.Entry<String, Date> valueEntry : entry.getValue().entrySet()) {

                                    Date prev = null;
                                    if (productNewness != null)
                                        prev = productNewness.get(valueEntry.getKey());
                                    else
                                        productNewness = new HashMap<String, Date>();
                                    if (prev == null || valueEntry.getValue().after(prev)) {
                                        productNewness.put(valueEntry.getKey(), valueEntry.getValue());
                                        newCache.put(p.getContentKey(), productNewness);
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
        final Map<ContentKey, Map<String, Date>> allBackInStockProducts = getBackInStockProducts();
        final Map<ProductModel, Date> backInStockProductsWithRecency = new HashMap<ProductModel, Date>();
        LinkedHashMap<ProductModel, Date> productsInDays = new LinkedHashMap<ProductModel, Date>();
        ZoneInfo zone = getCurrentUserContext().getPricingContext().getZoneInfo();
        String productNewnessKey = "";
        if (zone != null) {
            productNewnessKey = new StringBuilder(5).append(zone.getSalesOrg()).append(zone.getDistributionChanel()).toString();
        }
        List<ProductModel> products = new ArrayList<ProductModel>(allBackInStockProducts.size());
        long limit = System.currentTimeMillis() - inDays * DAY_IN_MILLISECONDS;
        for (Map.Entry<ContentKey, Map<String, Date>> entry : allBackInStockProducts.entrySet()) {
            Map<String, Date> valueEntries = entry.getValue();
            for (Map.Entry<String, Date> valueEntry : valueEntries.entrySet()) {
                if (valueEntry.getKey().equals(productNewnessKey) && valueEntry.getValue().getTime() > limit) {
                    ProductModel product = (ProductModel) getContentNodeByKey(entry.getKey());
                    products.add(product);
                    backInStockProductsWithRecency.put(product, valueEntry.getValue());
                }
            }
        }
        Collections.sort(products, new Comparator<ProductModel>() {

            @Override
            public int compare(ProductModel o1, ProductModel o2) {
                return -backInStockProductsWithRecency.get(o1).compareTo(backInStockProductsWithRecency.get(o2));
            }
        });

        for (ProductModel p : products) {
            productsInDays.put(p, backInStockProductsWithRecency.get(p));
        }
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
        Map<ContentKey, Map<String, Date>> newProducts = getBackInStockProducts();
        ContentKey productContentKey = product.getContentKey();
        if (newProducts.containsKey(productContentKey)) {
            Date when = newProducts.get(productContentKey).get(productNewnessKey);
            return when == null ? Integer.MAX_VALUE : ((double) System.currentTimeMillis() - when.getTime()) / DAY_IN_MILLISECONDS;
        } else {
            return Integer.MAX_VALUE; // very long time ago
        }
    }

    public boolean isGoingOutOfStockProduct(ContentKey key) {
        if (System.currentTimeMillis() > goingOutOfStockProductsLastUpdated + NEW_AND_BACK_REFRESH_PERIOD) {
            refreshGoingOutOfStockCache();
        }
        FulfillmentContext fulFillmentContext = getCurrentUserContext().getFulfillmentContext();
        return goingOutOfStockProductCache.containsKey(key)
                && goingOutOfStockProductCache.get(key).contains(fulFillmentContext.getSalesOrg() + fulFillmentContext.getDistChannel());
    }

    private void refreshGoingOutOfStockCache() {
        Map<ContentKey, Collection<String>> newCache = new HashMap<ContentKey, Collection<String>>();
        try {
            Collection<ErpMaterialSalesAreaModel> goingOutOfStockSalesAreas = FDCachedFactory.getGoingOutOfStockSalesAreas();
            for (ErpMaterialSalesAreaModel salesArea : goingOutOfStockSalesAreas) {
                ContentKey skuKey = ContentKeyFactory.get(ContentType.Sku, salesArea.getSkuCode());
                SkuModel sku = (SkuModel) getContentNodeByKey(skuKey);
                try {
                    if (sku != null && sku.getProductModel() != null && sku.getProductInfo() != null && !sku.isUnavailable()) {
                        ProductModel p = filterProduct(sku.getContentName());
                        if (p != null && ContentUtil.isAvailableByContext(p)) {
                            Collection<String> sales = newCache.get(p.getContentKey());
                            if (sales == null) {
                                sales = new ArrayList<String>();
                                newCache.put(p.getContentKey(), sales);
                            }
                            sales.add(salesArea.getSalesOrg() + salesArea.getDistChannel());
                        }
                    }
                } catch (Exception e) {
                    LOGGER.warn("skipping sku " + sku.getContentName() + " due to data discrepancy", e);
                }
            }
        } catch (FDResourceException e) {
            LOGGER.error("failed to update back-in-stock products cache; retrying a minute later", e);
        }
        LOGGER.info("reloaded going out of stock products: " + newCache.size());
        synchronized (goingOutOfStockProductCache) {
            goingOutOfStockProductCache.clear();
            goingOutOfStockProductCache.putAll(newCache);
            goingOutOfStockProductsLastUpdated = System.currentTimeMillis();
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
        refreshGoingOutOfStockCache();
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
        FDFolder domains = (FDFolder) getContentNodeByKey(ContentKeyFactory.get(ContentType.FDFolder, "domains"));
        for (ContentNodeModel node : domains.getChildren()) {
            if (!node.getContentKey().type.equals(ContentType.Domain))
                continue;

            if (node.getContentKey().id.startsWith("wine_"))
                wineDomains.add((Domain) node);
        }
        LOGGER.info("WINE INDEX: collected " + wineDomains.size() + " domains.");

        LOGGER.info("WINE INDEX: collecting all wine products...");
        CategoryModel byRegion = (CategoryModel) getContentNodeByKey(ContentKeyFactory.get(ContentType.Category, winePrefix + "_region"));
        if (byRegion != null)
            newIndex.all.addAll(byRegion.getAllChildProductKeys());
        CategoryModel byType = (CategoryModel) getContentNodeByKey(ContentKeyFactory.get(ContentType.Category, winePrefix + "_type"));
        if (byType != null)
            newIndex.all.addAll(byType.getAllChildProductKeys());
        CategoryModel more = (CategoryModel) getContentNodeByKey(ContentKeyFactory.get(ContentType.Category, winePrefix + "_more"));
        if (more != null)
            newIndex.all.addAll(more.getAllChildProductKeys());
        LOGGER.info("WINE INDEX: collected all " + newIndex.all.size() + " wine products");

        Set<DomainValue> regions = new HashSet<DomainValue>(1000);
        LOGGER.info("WINE INDEX: collecting domain value - product pairs...");
        for (Domain d : wineDomains) {
            if (d.getContentKey().id.startsWith("wine_region_"))
                regions.addAll(d.getDomainValues());
            for (DomainValue dv : d.getDomainValues()) {
                if (dv.getDomain().getContentKey().id.startsWith("wine_region_")) {
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
            Domain domain = (Domain) getContentNode(ContentType.Domain, "wine_country_usq");
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
            grabberService = GrabberServiceLocator.grabberService();
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

    public ContentKey getPrimaryHomeKey(ContentKey key) {
        return CmsManager.getInstance().getPrimaryHomeKey(key);
    }

    public ContentNodeModel getContentNodeFromCache(ContentKey key) {
        ContentNodeModel model = null;
        if (isAllowToUseContentCache()) {
            ValueWrapper valueWrapper = nodesByKeyCache.get(key);
            if (valueWrapper != null) {
                model = (ContentNodeModel) valueWrapper.get();
            }
        }
        return model;
    }

    public ContentNodeModel getContentNodeFromCache(String nodeId) {
        ContentNodeModel model = null;
        if (isAllowToUseContentCache()) {
            ValueWrapper valueWrapper = nodesByIdCache.get(nodeId);
            if (valueWrapper != null) {
                model = (ContentNodeModel) valueWrapper.get();
            }
        }
        return model;
    }

    public void updateContentNodeCaches(String nodeId, ContentNodeModel nodeModel) {
        if (nodeModel != null && isAllowToUseContentCache()) {
            nodesByIdCache.put(nodeId, nodeModel);
            nodesByKeyCache.put(nodeModel.getContentKey(), nodeModel);
        }
    }

    public void removeContentNodeCaches(ContentKey key) {
        if (isAllowToUseContentCache()) {
            nodesByIdCache.evict(key.id);
            nodesByKeyCache.evict(key);
        }
    }

    public void updateContentKeyCache(String skuCode, ContentKey key) {
        if (isAllowToUseContentCache()) {
            CmsServiceLocator.ehCacheUtil().putObjectToCache(CmsCaches.STORE_CONTENT_KEY_BY_SKU_CODE_CACHE.cacheName, skuCode, key);
        }
    }

    public ContentKey getContentKeyFromCache(String skuCode) {
        ContentKey contentKey = null;
        if (isAllowToUseContentCache()) {
            contentKey = CmsServiceLocator.ehCacheUtil().getObjectFromCache(CmsCaches.STORE_CONTENT_KEY_BY_SKU_CODE_CACHE.cacheName, skuCode);
        }
        return contentKey;
    }

    public boolean isAllowToUseContentCache() {
        return CmsServiceLocator.draftContextHolder().getDraftContext().isMainDraft();
    }

    public void evictNodesByCaches() {
        store = null;
        nodesByIdCache.clear();
        nodesByKeyCache.clear();
    }
}
