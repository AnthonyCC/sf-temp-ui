package com.freshdirect.fdstore.warmup;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.promotion.FDPromotionNewModelFactory;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.logging.LoggingHelper;

/**
 * 
 * @author ksriram
 *
 */
public class FDAdminWarmupServer extends ApplicationLifecycleListener {

	public void postStart(ApplicationLifecycleEvent evt) {
		Logger logger = LoggingHelper.getServerLogger();
		logger.info("inside FDAdminWarmup ");
		if (FDStoreProperties.performPromotionsPreLoad()) {
			try {
				logger.info("initiating warmup using FDAdminWarmup ");
				FDAdminWarmup warmup = new FDAdminWarmup();
				warmup.init();
				logger.info("FDAdminWarmup completed");
			} catch (Exception e) {
				logger.log(Level.SEVERE, "error during FDAdminWarmup", e);
				throw new RuntimeException(e);
			}
		}
	}
}
