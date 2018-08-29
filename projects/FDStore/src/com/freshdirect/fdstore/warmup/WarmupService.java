package com.freshdirect.fdstore.warmup;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class WarmupService {

    private static Logger LOGGER = LoggerFactory.getInstance(WarmupService.class);

    private static final WarmupService INSTANCE = new WarmupService();

    private WarmupService() {
        LOGGER.info("Creating new WarmupService instance");
    }

    public static WarmupService defaultService() {
        return INSTANCE;
    }

    public WarmupPageData populateWarmupPageData() {
        WarmupState warmupState = Warmup.WARMUP_STATE.get();
        WarmupPageData result = new WarmupPageData();
        result.setWarmupState(warmupState.toString());
        if (WarmupState.NOT_TRIGGERED.equals(warmupState)) {
            result.setNotTriggered(true);
        }
        if (WarmupState.IN_PROGRESS.equals(warmupState)) {
            result.setInProgress(true);
        }
        if (WarmupState.FINISHED.equals(warmupState)) {
            result.setRepeatedWarmupCanHappen(CmsServiceLocator.contentProviderService().isReadOnlyContent());
        }
        result.setManualWarmupAllowed(isManualWarmupAllowed());
        return result;
    }

    public void repeatWarmup() {
    	
        if (!isManualWarmupAllowed()) {
            LOGGER.info("[WARMUP] Manual warmup is not allowed, not starting warmup!");
            return;
        }

        if (Warmup.WARMUP_STATE.compareAndSet(WarmupState.NOT_TRIGGERED, WarmupState.IN_PROGRESS) || Warmup.WARMUP_STATE.compareAndSet(WarmupState.FAILED, WarmupState.IN_PROGRESS)) {
            LOGGER.info("[WARMUP] Starting warmup-thread");
            new Thread("warmup-thread") {
                @Override
                public void run() {
                    Warmup warmup = new Warmup();
                    warmup.warmup();
                    //Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
                };
            }.start();
        } else if (CmsServiceLocator.contentProviderService().isReadOnlyContent() && Warmup.WARMUP_STATE.compareAndSet(WarmupState.FINISHED, WarmupState.IN_PROGRESS)) {
            LOGGER.info("[WARMUP] Starting warmup-repeat-thread");
            new Thread("warmup-repeat-thread") {
                @Override
                public void run() {
                    LOGGER.info("[WARMUP] Evicting caches");
                    WarmupReloadableCacheAdapter.defaultService().evictCaches();
                    LOGGER.info("[WARMUP] Caches evicted.");
                    
                    LOGGER.info("[WARMUP] Reloading contentprovider service");
                    CmsServiceLocator.contentProviderService().initializeContent();
                    LOGGER.info("[WARMUP] Contentprovider service reloaded");
                    
                    LOGGER.info("[WARMUP] Reloading media service");
                    CmsServiceLocator.mediaService().loadAll();
                    LOGGER.info("[WARMUP] Media service reloaded");
                    
                    try {
                        LOGGER.info("[WARMUP] Closing lucene index searcher");
                        CmsServiceLocator.luceneManager().closeAllIndexSearcher();
                        LOGGER.info("[WARMUP] Lucene index searcher closed");
                    } catch (IOException e) {
                        LOGGER.error("[WARMUP] Closing Index searchers failed.", e);
                    }
                    
                    LOGGER.info("[WARMUP] Starting repeat-warmup");
                    Warmup warmup = new Warmup();
                    warmup.repeatWarmup();
                    LOGGER.info("[WARMUP] Repeat-warmup started");
                    //Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
                };
            }.start();
        } else {
            LOGGER.info("[WARMUP] NOT starting warmup!");
        }
    }

    public boolean isWarmupInProgress() {
        return WarmupState.IN_PROGRESS.equals(Warmup.WARMUP_STATE.get());
    }

    public boolean isWarmupFinished() {
        return WarmupState.FINISHED.equals(Warmup.WARMUP_STATE.get());
    }

    private static final boolean isManualWarmupAllowed() {
        return FDStoreProperties.isRepeatWarmupEnabled() || FDStoreProperties.isLocalDeployment() || FDStoreProperties.getPreviewMode() || !FDStoreProperties.performStorePreLoad();
    }
}
