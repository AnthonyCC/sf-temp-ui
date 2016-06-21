package com.freshdirect.fdstore.warmup;

import java.io.Serializable;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.promotion.FDPromotionNewModelFactory;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 * @author ksriram
 *
 */
public class FDAdminWarmup implements Serializable{

	private static Category LOGGER = LoggerFactory.getInstance(FDAdminWarmup.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2319867198413913956L;

	public void init(){
		// Get instance loads up all the Promotions
//		FDPromotionNewModelFactory.getInstance();
		
		new Thread("FDAdminWarmup-Thread-1") {
			public void run() {
				LOGGER.info("FDAdminWarmup-Thread-1 started");
				try {
					FDPromotionNewModelFactory.getInstance();
				} catch (Exception e) {
					LOGGER.warn("Exception loading promotions in FDAdminWarmup-Thread-1: "+e);
				}
				LOGGER.info("FDAdminWarmup-Thread-1 completed");
			}
		}.start();

	}

}
