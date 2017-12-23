/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.warmup;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Category;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

/**
 * 
 * 
 * @version $Revision$
 * @author $Author$
 */
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
        LOGGER.info("[WARMUP]Warmup started");
        CacheWarmupUtil.warmupOAuthProvider();

        long time = System.currentTimeMillis();
        ContentFactory.getInstance().getStore();
        LOGGER.info("[WARMUP]Store warmup in " + (System.currentTimeMillis() - time) + " ms");

        Set<ContentKey> skuContentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SKU);
        int skuCount = 0;
        for (final ContentKey key : skuContentKeys) {
        	skuCodes.add(key.getId());
        	
        	if(FDStoreProperties.isLocalDeployment() && !CmsManager.getInstance().isNodeOrphan(key)) {
        		skuCount++;
        	}
        	if(FDStoreProperties.isLocalDeployment() && skuCount == LOCAL_DEVELOPER_WARMUP_PRODUCT_COUNT) {
        		break;
        	}
        }

        LOGGER.info(skuCodes.size() + " SKUs found");
        
        if(FDStoreProperties.isLocalDeployment()) {
        	LOGGER.info("Skipping Nutrition, Attribute, Inventory, NutirtionPanel cache for local deployment");
        } else {
        	CacheWarmupUtil.warmupFDCaches();
        }

        LOGGER.info("[WARMUP]Main warmup in " + (System.currentTimeMillis() - time) + " ms");

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
                    LOGGER.info("[WARMUP]Warmup done");
                    Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
                } catch (Exception e) {
                    LOGGER.error("[WARMUP]Warmup failed", e);
                    Warmup.WARMUP_STATE.set(WarmupState.FAILED);
                }
            }
        }.start();
    }

    /**
     * Used after store XML change. Warmup only CMS related items.
     */
    public void repeatWarmup() {
        LOGGER.info("[WARMUP]warmup-repeat started");
        long time = System.currentTimeMillis();

        ContentFactory.getInstance().getStore();
        CmsManager.getInstance().initPrimaryHomeMap();

        LOGGER.info("[WARMUP]Store warmup in " + (System.currentTimeMillis() - time) + " ms");
        Set<ContentKey> skuContentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SKU);
        LOGGER.info(skuContentKeys.size() + " SKUs found");

        LOGGER.info("[WARMUP]Main warmup in " + (System.currentTimeMillis() - time) + " ms");

        new Thread("warmup-repeat-step-2") {
            @Override
            public void run() {
            	try {
	                CacheWarmupUtil.warmupAutocomplete();
	                CacheWarmupUtil.warmupWineIndex();
	                CacheWarmupUtil.warmupSmartStore();
	                CacheWarmupUtil.warmupSmartCategories();
	                LOGGER.info("[WARMUP]Warmup done");
	                Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
            	} catch (Exception e) {
                    LOGGER.error("[WARMUP]Warmup failed", e);
                    Warmup.WARMUP_STATE.set(WarmupState.FAILED);
                }
            }
        }.start();
    }

}
