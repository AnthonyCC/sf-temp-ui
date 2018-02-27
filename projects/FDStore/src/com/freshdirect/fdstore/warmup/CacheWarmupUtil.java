package com.freshdirect.fdstore.warmup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.fdstore.FDAttributeCache;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDInventoryCache;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDNutritionPanelCache;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.content.WineFilterPriceIndex;
import com.freshdirect.fdstore.content.WineFilterRatingIndex;
import com.freshdirect.fdstore.grp.FDGrpInfoManager;
import com.freshdirect.fdstore.oauth.provider.OAuthProvider;
import com.freshdirect.fdstore.zone.FDZoneInfoManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.service.CmsRecommenderRegistry;
import com.freshdirect.smartstore.service.SearchScoringRegistry;
import com.freshdirect.smartstore.service.VariantRegistry;
import com.freshdirect.storeapi.StoreServiceLocator;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;

import net.oauth.OAuthException;

public class CacheWarmupUtil {

    private static Logger LOGGER = LoggerFactory.getInstance(CacheWarmupUtil.class.getSimpleName());

    private final static int MAX_THREADS = 4;
    private final static int GRAB_SIZE = 1000;

    /**
     * Trigger various FD caches
     */
    public static void warmupFDCaches() {
        
        int taskSize = 4;
        ExecutorService execSvc = Executors.newFixedThreadPool(taskSize);
        Collection<Future<?>> futures = new ArrayList<Future<?>>(taskSize);
        futures.add(execSvc.submit(
            new Runnable() {
                @Override
                public void run() {
                    // Get instance loads up the inventory
                    FDInventoryCache.getInstance();                    
                }
            }
        ));
        futures.add(execSvc.submit(
            new Runnable() {
                @Override
                public void run() {
                    // Get instance loads up the Attributes
                    FDAttributeCache.getInstance();
                }
            }
        ));
        futures.add(execSvc.submit(
            new Runnable() {
                @Override
                public void run() {
                    // Get instance loads up the Nutrition
                    FDNutritionCache.getInstance();
                }
            }
        ));
        futures.add(execSvc.submit(
            new Runnable() {
                @Override
                public void run() {
                    // Get instance loads up the Drug Nutrition
                    FDNutritionPanelCache.getInstance();
                }
            }
        ));

        for (Future<?> future:futures) {
            try {
                future.get();
            } catch(Exception e) {
                LOGGER.warn("Warmup did not complete normally", e);
            }
        }

        try {
            execSvc.shutdown();
        } catch (Exception e) {
            LOGGER.warn("Exception while shutting down the ExecutorService ", e);
        }
        
    }

    @SuppressWarnings("rawtypes")
    public static void warmupZones() throws FDResourceException {
        LOGGER.info("Loading zone data");
        Collection zoneInfoList = FDZoneInfoManager.loadAllZoneInfoMaster();
        @SuppressWarnings("unchecked")
        final List zoneInfos = new ArrayList(FDCachedFactory.getZoneInfos((String[]) zoneInfoList.toArray(new String[0])));
        LOGGER.info("Lightweight zone data loaded size is :" + zoneInfos.size());
    }

    public static void warmupProducts(final Collection<String> skuCodes) throws FDResourceException {
        LOGGER.info("Loading lightweight product data");

        UserContext userCtx = ContentFactory.getInstance().getCurrentUserContext();
        @SuppressWarnings("unchecked")
        final List<FDProductInfo> prodInfos = new ArrayList<FDProductInfo>(FDCachedFactory.getProductInfos(skuCodes.toArray(new String[0])));
        // Filter discontinued Products
        filterDiscontinuedProducts(prodInfos, userCtx);

        LOGGER.info("Loading heavyweight product data in " + MAX_THREADS + " threads");

        final FDSku[] DUMMY_ARRAY = new FDSku[0];
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread t = new Thread("Warmup " + i) {

                @Override
                public void run() {
                    LOGGER.info("started " + Thread.currentThread().getName());
                    while (true) {
                        FDSku[] skus;
                        synchronized (prodInfos) {
                            int pSize = prodInfos.size();
                            if (pSize == 0) {
                                // nothing left to process
                                break;
                            }
                            // grab some items, and remove from prodInfos list
                            List<FDProductInfo> subList = prodInfos.subList(0, Math.min(pSize, GRAB_SIZE));
                            skus = subList.toArray(DUMMY_ARRAY);
                            subList.clear();
                            LOGGER.debug(pSize + " items left to load");
                        }
                        try {
                            FDCachedFactory.getProducts(skus);
                        } catch (FDResourceException ex) {
                            LOGGER.warn("Error during warmup", ex);
                        }
                    }
                    LOGGER.info("completed " + Thread.currentThread().getName());
                }
            };
            t.setDaemon(true);
            t.start();
        }
    }

    private static void filterDiscontinuedProducts(List<FDProductInfo> prodInfos, UserContext userCtx) {
        // UserContext userCtx=FACTORY.getCurrentUserContext();
        ZoneInfo zone = userCtx.getPricingContext().getZoneInfo();
        for (Iterator<FDProductInfo> it = prodInfos.iterator(); it.hasNext();) {
            FDProductInfo prodInfo = it.next();
            if (prodInfo.isDiscontinued(zone.getSalesOrg(), zone.getDistributionChanel())) {
                it.remove();
            }
        }
    }

    public static void warmupProductNewness() throws FDResourceException {
        ContentFactory factory = ContentFactory.getInstance();

        // initiating the asynchronous load of new and reintroduced products cache
        if (FDStoreProperties.isPreloadNewness()) {
            LOGGER.info("preloading product newness");
            factory.getNewProducts();
            LOGGER.info("finished preloading product newness");
        } else {
            LOGGER.info("skipped preloading product newness");
        }

        if (FDStoreProperties.isPreloadReintroduced()) {
            LOGGER.info("preloading reintroduced products");
            factory.getBackInStockProducts();
            LOGGER.info("finished preloading reintroduced products");
        } else {
            LOGGER.info("skipped preloading reintroduced products");
        }
    }

    public static void warmupGroupes() throws FDResourceException {
        LOGGER.info("Loading grp data");
        try {
			Collection<FDGroup> grpInfoList = FDGrpInfoManager.loadAllGrpInfoMaster();
			@SuppressWarnings("unchecked")
			final Collection<GroupScalePricing> grpInfos = FDCachedFactory.getGrpInfos(grpInfoList.toArray(new FDGroup[0]));
			LOGGER.info("Lightweight grp data loaded size is :" + grpInfos.size());
		} catch (Exception e) {
			 LOGGER.warn("Loading grp data failed:", e);
		}
    }

    public static void warmupMaterialGroups() throws FDResourceException {
        if (FDStoreProperties.isGroupScalePerfImproveEnabled()) {
            LOGGER.info("Loading Group Materials Data");
            FDCachedFactory.loadMaterialGroupCache();
            LOGGER.info("Loaded Group Materials Data");
        }
    }

    public static void warmupOAuthProvider() {
        try {
            OAuthProvider.deleteOldAccessors();
        } catch (OAuthException e) {
            LOGGER.error("OAuth warmup error", e);
        }
    }

    public static void warmupSmartCategories() {
        Set<ContentKey> categories = CmsManager.getInstance().getContentKeysByType(ContentType.Category);
        LOGGER.info("found " + categories.size() + " categories");
        try {
            @SuppressWarnings("unchecked")
            Collection<String> zones = FDZoneInfoManager.loadAllZoneInfoMaster();
            for (ContentKey catKey : categories) {
                ContentNodeModel node = ContentFactory.getInstance().getContentNodeByKey(catKey);
                if (node instanceof CategoryModel) {
                    CategoryModel category = (CategoryModel) node;
                    if (category.getRecommender() != null || category.getProductPromotionType() != null) {
                        //LOGGER.info("category " + category.getContentName() + " is smart or promo, pre-loading child products for " + zones.size() + " zones");
                        category.getProducts();
                    }
                }
            }
        } catch (FDResourceException e) {
            LOGGER.error("cannot load zones for Smart Categories", e);
        }
    }

    public static void warmupSmartStore() {
        if (FDStoreProperties.isPreloadSmartStore()) {
            LOGGER.info("[WARMUP]Preloading Smart Store");
            VariantRegistry.getInstance().reload();
            CmsRecommenderRegistry.getInstance().reload();
            SearchScoringRegistry.getInstance().load();
        } else {
            LOGGER.info("[WARMUP]Skipped preloading Smart Store");
        }
    }

    public static void warmupWineIndex() {
        ContentFactory.getInstance().refreshWineIndex(true);
        WineFilterPriceIndex.getInstance();
        WineFilterRatingIndex.getInstance();
    }

    public static void warmupAutocomplete() {
        if (FDStoreProperties.isPreloadAutocompletions()) {
            StoreServiceLocator.contentSearch().getAutocompletions("qwertyuqwerty");
            StoreServiceLocator.contentSearch().getBrandAutocompletions("qwertyuqwerty");
        }
    }
}
