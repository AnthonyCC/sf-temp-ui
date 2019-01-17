package com.freshdirect.fdstore.warmup;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Category;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDAttributeCache;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

public class Warmup {

    private static Category LOGGER = LoggerFactory.getInstance(Warmup.class);
    public static final AtomicReference<WarmupState> WARMUP_STATE = new AtomicReference<WarmupState>(WarmupState.NOT_TRIGGERED);
    protected Set<String> skuCodes = new HashSet<String>(8000);
    private final int LOCAL_DEVELOPER_WARMUP_PRODUCT_COUNT = 1000;
    
    public Warmup() {
    }

    /**
     * Used for full warmup on server start.
     */
    public void warmup() {
        LOGGER.info("[WARMUP] Warmup started");

        long time = System.currentTimeMillis();
        ContentFactory.getInstance().getStore();
        LOGGER.info("[WARMUP] Store warmup in " + (System.currentTimeMillis() - time) + " ms");

        Set<ContentKey> skuContentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SKU);
        int skuCount = 0;
        for (final ContentKey key : skuContentKeys) {
            skuCodes.add(key.getId());

            if (FDStoreProperties.isLocalDeployment() && !CmsManager.getInstance().isNodeOrphan(key)) {
                skuCount++;
            }
            if (FDStoreProperties.isLocalDeployment() && skuCount == LOCAL_DEVELOPER_WARMUP_PRODUCT_COUNT) {
                break;
            }
        }

        LOGGER.info(skuCodes.size() + " SKUs found");

        if (FDStoreProperties.isLocalDeployment()) {
            LOGGER.info("Skipping Nutrition, Inventory, NutirtionPanel cache for local deployment");
            FDAttributeCache.getInstance(); // Attribute cache is required even for local deployment.
        } else {
            CacheWarmupUtil.warmupFDCaches();
        }

        LOGGER.info("[WARMUP] Main warmup in " + (System.currentTimeMillis() - time) + " ms");

        new Thread("warmup-step-2") {
            @Override
            public void run() {
                try {
                    CacheWarmupUtil.warmupZones();
                    CacheWarmupUtil.warmupMaterialGroups();
                    CacheWarmupUtil.warmupProducts(skuCodes);
                    CacheWarmupUtil.warmupAutocomplete();
                    CacheWarmupUtil.warmupProductNewness();
                    CacheWarmupUtil.warmupGroupes();
                    CacheWarmupUtil.warmupWineIndex();
                    CacheWarmupUtil.warmupSmartStore();
                    CacheWarmupUtil.warmupSmartCategories();
                    LOGGER.info("[WARMUP] WARMUPEVENT-NORMAL-COMPLETED");
                    Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
                } catch (Exception e) {
                    LOGGER.error("[WARMUP] WARMUPEVENT-NORMAL-FAILED", e);
                    Warmup.WARMUP_STATE.set(WarmupState.FAILED);
                }
            }
        }.start();
    }

    /**
     * Used after store XML change. Warmup only CMS related items.
     */
    public void repeatWarmup() {
        LOGGER.info("[WARMUP] warmup-repeat started");
        final long time = System.currentTimeMillis();

        LOGGER.info("[WARMUP] Load ContentFactory store");
        ContentFactory.getInstance().getStore();
        LOGGER.info("[WARMUP] ContentFactory store loaded");
        
        LOGGER.info("[WARMUP] Initializing CmsManager primary home map");
        CmsManager.getInstance().initPrimaryHomeMap();
        LOGGER.info("[WARMUP] CmsManager primary home map initialized");
        
        LOGGER.info("[WARMUP] Initializing CmsManager corporate home map");
        CmsManager.getInstance().initCorporateHomeMap();
        LOGGER.info("[WARMUP] CmsManager corporate home map initialized");

        LOGGER.info("[WARMUP] Store warmup in " + (System.currentTimeMillis() - time) + " ms");
        Set<ContentKey> skuContentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SKU);
        LOGGER.info(skuContentKeys.size() + " SKUs found");

        LOGGER.info("[WARMUP] Main warmup done in " + (System.currentTimeMillis() - time) + " ms");

        new Thread("warmup-repeat-step-2") {
            @Override
            public void run() {
                try {
                    LOGGER.info("[WARMUP] Warmup autocomplete");
                    CacheWarmupUtil.warmupAutocomplete();
                    LOGGER.info("[WARMUP] Autocomplete warmed up");
                    
                    LOGGER.info("[WARMUP] Warmup wine index");
                    CacheWarmupUtil.warmupWineIndex();
                    LOGGER.info("[WARMUP] Wine index warmed up");
                    
                    LOGGER.info("[WARMUP] Warmup smart store");
                    CacheWarmupUtil.warmupSmartStore();
                    LOGGER.info("[WARMUP] Smart store warmed up");
                    
                    LOGGER.info("[WARMUP] Warmup smart categories");
                    CacheWarmupUtil.warmupSmartCategories();
                    LOGGER.info("[WARMUP] Smart categories warmed up");
                    
                    LOGGER.info("[WARMUP] WARMUPEVENT-REPEAT-COMPLETED " + (System.currentTimeMillis() - time) + " ms");
                    Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
                } catch (Exception e) {
                    LOGGER.error("[WARMUP] WARMUPEVENT-REPEAT-FAILED ", e);
                    Warmup.WARMUP_STATE.set(WarmupState.FAILED);
                }
            }
        }.start();
    }
}
