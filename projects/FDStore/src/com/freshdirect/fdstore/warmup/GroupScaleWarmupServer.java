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
public class GroupScaleWarmupServer extends ApplicationLifecycleListener {

	public void postStart(ApplicationLifecycleEvent evt) {
		Logger logger = LoggingHelper.getServerLogger();
		logger.info("inside GroupScaleWarmupServer ");
		if (FDStoreProperties.performGroupScalePreLoad()) {
			try {
				logger.info("initiating GroupScaleWarmup ");
				CacheWarmupUtil.warmupMaterialGroups();
				logger.info("GroupScaleWarmup completed");
			} catch (Exception e) {
				logger.log(Level.SEVERE, "error during GroupScaleWarmup", e);
				throw new RuntimeException(e);
			}
		}
	}
}
