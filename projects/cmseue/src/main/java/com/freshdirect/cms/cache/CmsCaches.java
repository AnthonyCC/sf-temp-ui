package com.freshdirect.cms.cache;

import java.util.ArrayList;
import java.util.List;

public enum CmsCaches {
    ATTRIBUTE_CACHE("attributeCache", CacheDataSource.CMS, CacheWarmupAction.RELOADABLE),
    PARENT_KEYS_CACHE("parentKeysCache", CacheDataSource.CMS, CacheWarmupAction.RELOADABLE),
    DRAFT_CHANGE_CACHE("cmsDraftChangesCache", CacheDataSource.CMS, CacheWarmupAction.RELOADABLE),
    DRAFT_PARENT_CACHE("draftParentCache", CacheDataSource.CMS, CacheWarmupAction.RELOADABLE),
    DRAFT_NODES("draftNodes", CacheDataSource.CMS, CacheWarmupAction.RELOADABLE),
    NODES_BY_ID_CACHE("nodesByIdCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    NODES_BY_KEY_CACHE("nodesByKeyCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    QS_PAST_ORDERS_CACHE("pastOrdersCache", CacheDataSource.STORE, CacheWarmupAction.NOT_RELOADABLE),
    QS_SHOP_FROM_LISTS_CACHE("yourListsCache", CacheDataSource.STORE, CacheWarmupAction.NOT_RELOADABLE),
    QS_STARTER_LISTS_CACHE("fdListsCache", CacheDataSource.STORE, CacheWarmupAction.NOT_RELOADABLE),
    QS_TOP_ITEMS_CACHE("topItemsCache", CacheDataSource.STORE, CacheWarmupAction.NOT_RELOADABLE),
    BR_CMS_ONLY_PRODUCT_GRABBER_CACHE("cmsOnlyProductGrabberCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    BR_ERPS_PRODUCT_GRABBER_CACHE("erpsProductGrabberCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    BR_ERPS_ZONE_PRODUCT_GRABBER_CACHE("erpsZoneProductGrabberCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    BR_STATIC_PRODUCTS_IN_SUB_TREE_CACHE("staticProductsInSubTreeCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    BR_CATEGORY_SUB_TREE_HAS_PRODUCTS_CACHE("categorySubTreeHasProductsCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    BR_USER_REFINEMENT_CACHE("userRefinementCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    BR_CATEGORY_TOP_ITEM_CACHE("categoryTopItemCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    // TODO: review the following four caches whether they a reloadable!
    FD_FAMILY_PRODUCT_CACHE("familyProductCache", CacheDataSource.STORE, CacheWarmupAction.NOT_RELOADABLE),
    MEDIA_CHECK_CACHE_NAME("mediaCheckCache", CacheDataSource.STORE, CacheWarmupAction.NOT_RELOADABLE),
    MEDIA_CONTENT_CACHE_NAME("mediaContentCache", CacheDataSource.STORE, CacheWarmupAction.NOT_RELOADABLE),
    FD_ZONE_ID_CACHE_NAME("fdZoneIdCache", CacheDataSource.ERPS, CacheWarmupAction.NOT_RELOADABLE),
    STORE_CONTENT_KEY_BY_SKU_CODE_CACHE("keyBySkuCodeCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE),
    SKU_MATERIALS_CACHE("skuMaterialsCache", CacheDataSource.ERPS, CacheWarmupAction.NOT_RELOADABLE),
    MATERIAL_SALES_UNITS_CACHE("materialSalesUnitsCache", CacheDataSource.ERPS, CacheWarmupAction.NOT_RELOADABLE),
    MATERIAL_CHARACTERISTICS_CACHE("materialCharacteristicsCache", CacheDataSource.ERPS, CacheWarmupAction.NOT_RELOADABLE),
    MATERIAL_DATA_CACHE("materialDataCache", CacheDataSource.ERPS, CacheWarmupAction.NOT_RELOADABLE),
    RECOMMENDATION_CHECK_CACHE_NAME("recommCheckCache", CacheDataSource.STORE, CacheWarmupAction.RELOADABLE);

    public String cacheName;
    public CacheDataSource source;
    private CacheWarmupAction warmupAction;

    private CmsCaches(String cacheName, CacheDataSource source, CacheWarmupAction warmupAction) {
        this.cacheName = cacheName;
        this.source = source;
        this.warmupAction = warmupAction;
    }

    public static List<String> findAllCacheNamesBySource(CacheDataSource source) {
        List<String> result = new ArrayList<String>();
        for (CmsCaches cache : values()) {
            if (cache.source.equals(source)) {
                result.add(cache.cacheName);
            }
        }
        return result;
    }

    public static List<String> findAllCacheNamesByWarmupAction(CacheWarmupAction warmupAction) {
        List<String> result = new ArrayList<String>();
        for (CmsCaches cache : values()) {
            if (cache.warmupAction.equals(warmupAction)) {
                result.add(cache.cacheName);
            }
        }
        return result;
    }
}
