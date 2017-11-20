package com.freshdirect.webapp.warmup;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.warmup.Warmup;
import com.freshdirect.fdstore.warmup.WarmupState;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.warmup.data.WarmupPageData;

public class WarmupService {

    private static Logger LOGGER = LoggerFactory.getInstance(Warmup.class);

    private static final WarmupService INSTANCE = new WarmupService();

    private WarmupService() {
    }

    public static WarmupService defaultService() {
        return INSTANCE;
    }

    public WarmupPageData populateWarmupPageData() {
        WarmupPageData result = new WarmupPageData();
        if (WarmupState.NOT_TRIGGERED.equals(Warmup.WARMUP_STATE.get())) {
            result.setNotTriggered(true);
        }
        if (WarmupState.IN_PROGRESS.equals(Warmup.WARMUP_STATE.get())) {
            result.setInProgress(true);
        }
        if (WarmupState.FINISHED.equals(Warmup.WARMUP_STATE.get())) {
            result.setRepeatedWarmupCanHappen(CmsServiceLocator.contentProviderService().isReadOnlyContent());
        }
        result.setManualWarmupAllowed(isManualWarmupAllowed());
        return result;
    }

    public void repeatWarmup() {
        if (!isManualWarmupAllowed()) {
            return;
        }
        if (Warmup.WARMUP_STATE.compareAndSet(WarmupState.NOT_TRIGGERED, WarmupState.IN_PROGRESS)) {
            new Thread("warmup-thread") {

                @Override
                public void run() {
                    Warmup warmup = new Warmup();
                    warmup.warmup();
                    Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
                };
            }.start();
        } else if (CmsServiceLocator.contentProviderService().isReadOnlyContent() && Warmup.WARMUP_STATE.compareAndSet(WarmupState.FINISHED, WarmupState.IN_PROGRESS)) {
            new Thread("warmup-repeat-thread") {

                @Override
                public void run() {
                    WarmupReloadableCacheAdapter.defaultService().evictCaches();
                    CmsServiceLocator.contentProviderService().initializeContent();
                    try {
                        CmsServiceLocator.luceneManager().closeAllIndexSearcher();
                    } catch (IOException e) {
                        LOGGER.error("Closing Index searchers failed.", e);
                    }
                    Warmup warmup = new Warmup();
                    warmup.repeatWarmup();
                    Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
                };
            }.start();
        }
    }

    public boolean isWarmupInProgress() {
        return WarmupState.IN_PROGRESS.equals(Warmup.WARMUP_STATE.get());
    }

    public boolean isWarmupFinished() {
        return WarmupState.FINISHED.equals(Warmup.WARMUP_STATE.get());
    }

    private static final boolean isManualWarmupAllowed() {
        return FDStoreProperties.isLocalDeployment() || FDStoreProperties.getPreviewMode() || !FDStoreProperties.performStorePreLoad();
    }
}
