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
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import java.util.*;

import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.*;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;
import com.freshdirect.smartstore.service.CmsRecommenderRegistry;
import com.freshdirect.smartstore.service.SearchScoringRegistry;
import com.freshdirect.smartstore.service.VariantRegistry;

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
		
		if (FDStoreProperties.isPreloadAutocompletions()) {
			ContentSearch.getInstance().getAutocompletions("qwertyuqwerty");
		}
		
		new Thread() {
			public void run() {
				try {
					// Warmup
					warmupProducts();
					
					warmupProductNewness();
					
					if (FDStoreProperties.isPreloadSmartStore()) {
						LOGGER.info("preloading Smart Store");
						VariantRegistry.getInstance().reload();
						CmsRecommenderRegistry.getInstance().reload();
						SearchScoringRegistry.getInstance().load();
					} else {
						LOGGER.info("skipped preloading Smart Store");
					}

					warmupSmartCategories();

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
	
	private void warmupSmartCategories() {
		Set<ContentKey> categories = CmsManager.getInstance().getContentKeysByType(ContentType.get("Category"));
		LOGGER.info("found " + categories.size() + " categories");
		for (ContentKey catKey : categories) {
			ContentNodeModel node = contentFactory.getContentNodeByKey(catKey);
			if (node instanceof CategoryModel) {
				CategoryModel category = (CategoryModel) node;
				if (category.getRecommender() != null)
					LOGGER.info("category " + category.getContentName() + " is smart, pre-loading child products");
					category.getProducts();
			}
		}
	}

	private void warmupProducts() throws FDResourceException {
		LOGGER.info("Loading lightweight product data");

		final List prodInfos = new ArrayList(FDCachedFactory.getProductInfos((String[]) this.skuCodes.toArray(new String[0])));
		
		LOGGER.info("Lightweight product data loaded");

		LOGGER.info("Loading heavyweight product data in " + MAX_THREADS + " threads");

		final FDSku[] DUMMY_ARRAY = new FDSku[0];
		for (int i = 0; i < MAX_THREADS; i++) {
			Thread t = new Thread("Warmup " + i) {
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
					LOGGER.info("completed " + Thread.currentThread().getName());
				}
			};
			t.setDaemon(true);
			t.start();
		}
	}

	private void warmupProductNewness() throws FDResourceException {
		// initiating the asynchronous load of new and reintroduced products cache
		if (FDStoreProperties.isPreloadNewness()) {
			LOGGER.info("preloading product newness");
			contentFactory.getProductNewnesses();
			LOGGER.info("preloading product newness, phase #2");
			contentFactory.getNewProducts(60, null);
			contentFactory.getNewProducts(30, null);
			contentFactory.getNewProducts(21, null);
			contentFactory.getNewProducts(15, null);
			contentFactory.getNewProducts(14, null);
			contentFactory.getNewProducts(7, null);
			LOGGER.info("finished preloading product newness");
		} else {
			LOGGER.info("skipped preloading product newness");			
		}

		if (FDStoreProperties.isPreloadReintroduced()) {
			LOGGER.info("preloading reintroduced products");
			contentFactory.getReintroducedProducts(60, null);
			contentFactory.getReintroducedProducts(30, null);
			contentFactory.getReintroducedProducts(21, null);
			contentFactory.getReintroducedProducts(15, null);
			contentFactory.getReintroducedProducts(14, null);
			contentFactory.getReintroducedProducts(7, null);
			LOGGER.info("finished preloading reintroduced products");
		} else {
			LOGGER.info("skipped preloading reintroduced products");			
		}
	}

}
