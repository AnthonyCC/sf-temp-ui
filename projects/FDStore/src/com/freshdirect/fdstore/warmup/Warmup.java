/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.warmup;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import java.util.*;

import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.*;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class Warmup {

	private static Category LOGGER = LoggerFactory.getInstance(Warmup.class);

	protected Set skuCodes;
	protected ContentFactory contentFactory;

	public Warmup() {
		this(ContentFactory.getInstance());
	}

	public Warmup(ContentFactory contentFactory) {
		this.contentFactory = contentFactory;
		this.skuCodes = new HashSet(8000);
	}

	public void warmup() {

		LOGGER.info("Warmup started");

		contentFactory.getStore();

		Set skuContentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SKU);
		for (Iterator i=skuContentKeys.iterator(); i.hasNext(); ) {
			ContentKey key = (ContentKey) i.next();
			skuCodes.add(key.getId());
		}

		LOGGER.info(skuCodes.size() + " SKUs found");
		
		// Get instance loads up the inventory
		FDInventoryCache.getInstance();
		//Get instance loads up the Attributes
		FDAttributeCache.getInstance();
		//Get instance loads up the Nutrition
		FDNutritionCache.getInstance();
		
		ContentSearch.getInstance().getAutocompletions("qwertyuqwerty");
		
		
		new Thread() {
			public void run() {
				try {
					// Warmup
					warmupProducts();
					
					SmartStoreServiceConfiguration.getInstance().getServices(EnumSiteFeature.YMAL);

					LOGGER.info("Warmup done");
				} catch (FDResourceException e) {
					LOGGER.error("Warmup failed", e);
				}
			}
		}
		.start();
	}
	
	private final static int MAX_THREADS = 2;
	private final static int GRAB_SIZE = 100;

	private void warmupProducts() throws FDResourceException {
		LOGGER.info("Loading lightweight product data");

		final List prodInfos = new ArrayList(FDCachedFactory.getProductInfos((String[]) this.skuCodes.toArray(new String[0])));
		
		LOGGER.info("Lightweight product data loaded");

		LOGGER.info("Loading heavyweight product data in " + MAX_THREADS + " threads");

		final FDSku[] DUMMY_ARRAY = new FDSku[0];
		for (int i = 0; i < MAX_THREADS; i++) {
			Thread t = new Thread("Warmup " + i) {
				public void run() {
					while (true) {
						FDSku[] skus;
						synchronized (prodInfos) {
							int pSize = prodInfos.size();
							if (pSize == 0) {
								// nothing left to process
								return;
							}
							// grab some items, and remove from prodInfos list
							List subList = prodInfos.subList(0, Math.min(pSize, GRAB_SIZE));
							skus = (FDSku[]) subList.toArray(DUMMY_ARRAY);
							subList.clear();
							LOGGER.debug(pSize + " items left to load");
						}
						try {
							FDCachedFactory.getProducts(skus);
						} catch (FDResourceException ex) {
							LOGGER.warn("Error during warmup", ex);
						}
					}
				}
			};
			t.start();
		}

		// initiating the asynchronous load of new and reintroduced products cache
		contentFactory.getProductNewnesses();
		contentFactory.getNewProducts(60, null);
		contentFactory.getNewProducts(30, null);
		contentFactory.getNewProducts(21, null);
		contentFactory.getNewProducts(15, null);
		contentFactory.getNewProducts(14, null);
		contentFactory.getNewProducts(7, null);

		contentFactory.getReintroducedProducts(60, null);
		contentFactory.getReintroducedProducts(30, null);
		contentFactory.getReintroducedProducts(21, null);
		contentFactory.getReintroducedProducts(15, null);
		contentFactory.getReintroducedProducts(14, null);
		contentFactory.getReintroducedProducts(7, null);
	}

}
