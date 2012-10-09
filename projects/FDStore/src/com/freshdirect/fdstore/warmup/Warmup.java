/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.warmup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.oauth.OAuthException;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDAttributeCache;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDDrugCache;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDInventoryCache;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.WineFilterPriceIndex;
import com.freshdirect.fdstore.content.WineFilterRatingIndex;
import com.freshdirect.fdstore.grp.FDGrpInfoManager;
import com.freshdirect.fdstore.oauth.provider.OAuthProvider;
import com.freshdirect.fdstore.zone.FDZoneInfoManager;
import com.freshdirect.framework.util.log.LoggerFactory;
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

	protected Set<String> skuCodes;
	protected ContentFactory contentFactory;

	public Warmup() {
		this(ContentFactory.getInstance());
	}

	public Warmup(ContentFactory contentFactory) {
		this.contentFactory = contentFactory;
		this.skuCodes = new HashSet<String>(8000);
	}

	public void warmup() {

		LOGGER.info("Warmup started");
		warmupOAuthProvider();
		
		long time = System.currentTimeMillis();
		contentFactory.getStore();
		LOGGER.info("Store warmup in " + (System.currentTimeMillis() - time) + " ms");

		Set<ContentKey> skuContentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SKU);
		for (Iterator<ContentKey> i = skuContentKeys.iterator(); i.hasNext();) {
			ContentKey key = (ContentKey) i.next();
			skuCodes.add(key.getId());
		}

		LOGGER.info(skuCodes.size() + " SKUs found");

		// Get instance loads up the inventory
		FDInventoryCache.getInstance();
		// Get instance loads up the Attributes
		FDAttributeCache.getInstance();
		// Get instance loads up the Nutrition
		FDNutritionCache.getInstance();
		// Get instance loads up the Drug Nutrition
		FDDrugCache.getInstance();

		LOGGER.info("main warmup in " + (System.currentTimeMillis() - time) + " ms");

		new Thread("warmup-step-2") {
			public void run() {
				try {
					// Warmup
					warmupZones();
					warmupProducts();
					if (FDStoreProperties.isPreloadAutocompletions()) {
						ContentSearch.getInstance().getAutocompletions("qwertyuqwerty");
						ContentSearch.getInstance().getBrandAutocompletions("qwertyuqwerty");
					}

					warmupProductNewness();

					warmupGroupes();
					contentFactory.refreshWineIndex(true);
					WineFilterPriceIndex.getInstance();
					WineFilterRatingIndex.getInstance();

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
		}.start();
	}

	private final static int MAX_THREADS = 2;
	private final static int GRAB_SIZE = 100;

	private void warmupSmartCategories() {
		Set<ContentKey> categories = CmsManager.getInstance().getContentKeysByType(ContentType.get("Category"));
		LOGGER.info("found " + categories.size() + " categories");
		try {
			@SuppressWarnings("unchecked")
			Collection<String> zones = FDZoneInfoManager.loadAllZoneInfoMaster();
			for (ContentKey catKey : categories) {
				ContentNodeModel node = contentFactory.getContentNodeByKey(catKey);
				if (node instanceof CategoryModel) {
					CategoryModel category = (CategoryModel) node;
					if (category.getRecommender() != null || category.getProductPromotionType() != null) {
						LOGGER.info("category " + category.getContentName() + " is smart or promo, pre-loading child products for " + zones.size() + " zones");
						for (String zone : zones) {
							contentFactory.setCurrentPricingContext(new PricingContext(zone));
							category.getProducts();
						}
					}
				}
			}
		} catch (FDResourceException e) {
			LOGGER.error("cannot load zones for Smart Categories", e);
		}
	}

	private void warmupProducts() throws FDResourceException {
		LOGGER.info("Loading lightweight product data");

		@SuppressWarnings("unchecked")
		final List<FDProductInfo> prodInfos = new ArrayList(FDCachedFactory.getProductInfos((String[]) this.skuCodes.toArray(new String[0])));
		// Filter discontinued Products
		filterDiscontinuedProducts(prodInfos);

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
							List<FDProductInfo> subList = prodInfos.subList(0, Math.min(pSize, GRAB_SIZE));
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

	private void filterDiscontinuedProducts(List<FDProductInfo> prodInfos) {
		for (Iterator<FDProductInfo> it = prodInfos.iterator(); it.hasNext();) {
			FDProductInfo prodInfo = it.next();
			if (prodInfo.isDiscontinued()) {
				it.remove();
			}
		}
	}

	private void warmupZones() throws FDResourceException {
		LOGGER.info("Loading zone data");
		Collection zoneInfoList = FDZoneInfoManager.loadAllZoneInfoMaster();
		@SuppressWarnings("unchecked")
		final List zoneInfos = new ArrayList(FDCachedFactory.getZoneInfos((String[]) zoneInfoList.toArray(new String[0])));
		LOGGER.info("Lightweight zone data loaded size is :" + zoneInfos.size());
	}

	private void warmupGroupes() throws FDResourceException {
		LOGGER.info("Loading grp data");
		Collection<FDGroup> grpInfoList=FDGrpInfoManager.loadAllGrpInfoMaster();
		final List<GroupScalePricing> grpInfos = new ArrayList<GroupScalePricing>(FDCachedFactory.getGrpInfos((FDGroup[]) grpInfoList.toArray(new FDGroup[0])));
		LOGGER.info("Lightweight grp data loaded size is :"+grpInfos.size());
	}

	private void warmupProductNewness() throws FDResourceException {
		// initiating the asynchronous load of new and reintroduced products cache
		if (FDStoreProperties.isPreloadNewness()) {
			LOGGER.info("preloading product newness");
			contentFactory.getNewProducts();
			LOGGER.info("finished preloading product newness");
		} else {
			LOGGER.info("skipped preloading product newness");
		}

		if (FDStoreProperties.isPreloadReintroduced()) {
			LOGGER.info("preloading reintroduced products");
			contentFactory.getBackInStockProducts();
			LOGGER.info("finished preloading reintroduced products");
		} else {
			LOGGER.info("skipped preloading reintroduced products");
		}
	}

	private void warmupOAuthProvider(){
		try {
			OAuthProvider.deleteOldAccessors();
		} catch (OAuthException e) {
			LOGGER.error("OAuth warmup error",e);
		}
	}
}
