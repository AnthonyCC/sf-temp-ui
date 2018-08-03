package com.freshdirect.fdstore.warmup;

import org.apache.log4j.Category;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.freshdirect.cms.configuration.RootConfiguration;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.configuration.StoreAPIConfig;

public class WarmupInitiator {
	
	private static Category logger = LoggerFactory.getInstance(WarmupInitiator.class);
	
    public static void initWarmup() {        

        if (FDStoreProperties.performStorePreLoad()) {
            Class warmupClass = Warmup.class;
            String className = FDStoreProperties.getWarmupClass();
            if (className != null) {
                try {
                    warmupClass = Class.forName(className);
                } catch (Exception e) {
                    logger.error("Could not find Warmup class " + className + " fallback to default.", e);
                }
            }
            try {
                logger.info("initiating warmup using class '" + warmupClass.getName() + "'");
                Warmup warmup = (Warmup) warmupClass.newInstance();
                Warmup.WARMUP_STATE.set(WarmupState.IN_PROGRESS);
                warmup.warmup();
                Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
                logger.info("warmup completed");
            } catch (Exception e) {
                logger.error("error during warmup", e);
                throw new RuntimeException(e);
            }
        }
    }

    public static void initializeSpringContext() {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfiguration.class);
        rootContext.register(StoreAPIConfig.class);
        rootContext.refresh();
    }
}
