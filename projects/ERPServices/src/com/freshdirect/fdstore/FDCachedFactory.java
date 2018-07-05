package com.freshdirect.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.framework.util.AsyncDBRefreshEhCache;
import com.freshdirect.framework.util.LazyTimedCache;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Caching proxy for FDFactory.
 */
public class FDCachedFactory {

    private static final Category LOGGER = LoggerFactory.getInstance(FDCachedFactory.class);

	/**
	 * @link dependency
	 * @label uses
	 */
	/* #FDFactory lnkFDFactory; */

	private final static Object SKU_NOT_FOUND = new Object();

	private final static Object GROUP_NOT_FOUND = new Object();

	/**
	 * FDProductInfo instances hashed by SKU strings.
	 */
	private final static AsyncDBRefreshEhCache<String, Object> productInfoCache = new AsyncDBRefreshEhCache<String, Object>(
			"FDProductInfo", FDStoreProperties.getProductCacheSize());

	/**
	 * FDProduct instances hashed by FDSku instances.
	 */
	private final static LazyTimedCache<FDSku, FDProduct> productCache = new LazyTimedCache<FDSku, FDProduct>("FDProduct", FDStoreProperties.getProductCacheSize(), FDStoreProperties.getRefreshSecsProduct() * 1000);


	/**
	 * FDProduct instances hashed by FDSku instances.
	 */
	private final static LazyTimedCache zoneCache = new LazyTimedCache("FDZoneInfo", FDStoreProperties.getZoneCacheSize(), FDStoreProperties.getRefreshSecsProduct() * 1000);

	/**
	 * FDProduct instances hashed by FDSku instances.
	 */
	private final static LazyTimedCache<FDGroup, GroupScalePricing> grpCache = new LazyTimedCache<FDGroup, GroupScalePricing>("FDGroupInfo", FDStoreProperties.getGrpCacheSize(), FDStoreProperties.getRefreshSecsProduct() * 1000);

	private final static Map productUpcCache = new ConcurrentHashMap();

	/**
	 * Thread that reloads expired productInfos, every 10 seconds.
	 */
	private final static Thread zRefreshThread = new LazyTimedCache.RefreshThread(zoneCache, 10000) {
		@Override
		protected void refresh(List expiredKeys) {
			try {
				LOGGER.debug("FDZoneRefresh reloading " + expiredKeys.size() + " zoneInfos");
				Collection pis = FDFactory.getZoneInfo((String[]) expiredKeys.toArray(new String[0]));

				// cache these
				ErpZoneMasterInfo tempi;
				for (Iterator i = pis.iterator(); i.hasNext();) {
					tempi = (ErpZoneMasterInfo) i.next();
					this.cache.put(tempi.getSapId(), tempi);
				}

			} catch (Exception ex) {
				LOGGER.warn("Error occured in FDProductInfoRefresh", ex);
			}
		}
	};

	private final static Thread piRefreshThread = new AsyncDBRefreshEhCache.RefreshThread<String, Object>(
			productInfoCache, FDStoreProperties.getRefreshSecsProductInfo() * 1000) {
		@Override
		protected void refresh(long lastUpdatedTimeStamp) throws Exception {
			try {
				// Add extra 120 seconds to guarantee no missing updates
				long onceInTime = lastUpdatedTimeStamp - (FDStoreProperties.getRefreshLookbackSecsProductInfo() * 1000);

				LOGGER.debug("******* calling FDFactory.getModifiedSkuCodes(" + onceInTime + ");");
				Set<String> recentlyUpdatedSkuCode = FDFactory.getModifiedSkuCodes(onceInTime);
				LOGGER.info("******* " + recentlyUpdatedSkuCode.size() + " productInfos were updated -> ["
						+ StringUtil.join(recentlyUpdatedSkuCode, ",") + "]");

				if (recentlyUpdatedSkuCode.size() == 0) {
					return;
				}

				// get the intersection of 2 sets of skyCodes
				Set<String> keysInCache = cache.keySet();
				recentlyUpdatedSkuCode.retainAll(keysInCache);

				LOGGER.info("*******  re-cache following " + recentlyUpdatedSkuCode.size()
						+ " productInfos from DB -> [" + StringUtil.join(recentlyUpdatedSkuCode, ",") + "]");

				String[] recentlyUpdatedSkuCodeArray = recentlyUpdatedSkuCode.toArray(new String[0]);
				Collection<FDProductInfo> pis = FDFactory.getProductInfos(recentlyUpdatedSkuCodeArray);

				// cache these
				FDProductInfo tempi;
				for (Iterator<FDProductInfo> i = pis.iterator(); i.hasNext();) {
					tempi = (FDProductInfo) i.next();
					this.cache.update(tempi.getSkuCode(), tempi);
				}
			} catch (FDResourceException ex) {
				LOGGER.warn("Error occured in FDProductInfoRefresh", ex);
				throw new Exception(ex);
			}
		}
	};

	/**
	 * Thread that reloads expired products, every 5 minutes.
	 */
	@SuppressWarnings("unchecked")
	private final static Thread prodRefreshThread = new LazyTimedCache.RefreshThread(productCache, 5 * 60 * 1000) {
		@Override
		protected void refresh(List expiredKeys) {
			try {
				LOGGER.debug("FDProductRefresh reloading " + expiredKeys.size() + " products");

				if (FDStoreProperties.isProductCacheOptimizationEnabled()) {
					// check and refresh only the latest version of the expired
					// products in the cache.
					for (Iterator<?> iterator = expiredKeys.iterator(); iterator.hasNext();) {
						FDSku fdSku = (FDSku) iterator.next();
						if (null != fdSku) {
							FDProductInfo pi = (FDProductInfo) productInfoCache.get(fdSku.getSkuCode());
							if (null != pi) {
								if (null == this.cache.get(pi)) {
									fdSku = new FDSku(pi.getSkuCode(), pi.getVersion());
									try {
										this.cache.put(fdSku, FDFactory.getProduct(fdSku));
									} catch (FDSkuNotFoundException ex) {
										// not found
									}
								} else {
									this.cache.remove(fdSku);// remove the old
																// versions of
																// the product
																// from cache
																// once expired,
																// as the cache
																// has latest
																// version.
								}
							}
						}

					}
				} else {
					Collection<FDProduct> prods = FDFactory.getProducts((FDSku[]) expiredKeys.toArray(new FDSku[0]));

					// cache these
					FDProduct temp;
					for (Iterator<FDProduct> i = prods.iterator(); i.hasNext();) {
						temp = (FDProduct) i.next();
						this.cache.put(temp, temp);
					}
				}
				LOGGER.info("FDProductRefresh cache size: " + this.cache.size() + " products");

			} catch (Exception ex) {
				LOGGER.warn("Error occured in FDProductRefresh", ex);
			}
		}
	};

	static {
		// Staggered start times for different threads.
		piRefreshThread.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// Do nothing
		}
		prodRefreshThread.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// Do nothing.
		}
		// gRefreshThread.start();
	}

	public static FDProductInfo getProductInfo(String sku, int version)
			throws FDResourceException, FDSkuNotFoundException {
		return FDFactory.getProductInfo(sku, version);
	}

	public static int getProductInfoCacheSize() {
		return productInfoCache.size();
	}

	/**
	 * Get current product information object for sku.
	 *
	 * @param sku
	 *            SKU code
	 *
	 * @return FDProductInfo object
	 *
	 * @throws FDSkuNotFoundException
	 *             if the SKU was not found in ERP services
	 * @throws FDResourceException
	 *             if an error occured using remote resources
	 */
	public static FDProductInfo getProductInfo(String sku) throws FDResourceException, FDSkuNotFoundException {
		if (sku == null) {
			throw new FDSkuNotFoundException("SKU " + sku + " not found (cached)");
		}

		Object cached = productInfoCache.get(sku);
		FDProductInfo pi;
		if (cached == null) {
			try {
				pi = FDFactory.getProductInfo(sku);
				productInfoCache.put(sku, pi);
				if (null != pi && pi.getUpc() != null && pi.getUpc().trim().length() > 0) {
					productUpcCache.put(pi.getUpc(), pi);
				}
			} catch (FDSkuNotFoundException ex) {
				productInfoCache.put(sku, SKU_NOT_FOUND);
				throw ex;
			}
		} else if (cached == SKU_NOT_FOUND) {
			throw new FDSkuNotFoundException("SKU " + sku + " not found (cached)");
		} else {
			pi = (FDProductInfo) cached;
		}

		return pi;
	}

	public static FDProductInfo getProductInfoByUpc(String upc) {
		return (FDProductInfo) productUpcCache.get(upc);
	}

	/**
	 * Get zone information.
	 * 
	 * @param zoneId
	 * @return
	 * @throws FDResourceException
	 */
	public static ErpZoneMasterInfo getZoneInfo(String zoneId) throws FDResourceException {
		Object cached = zoneCache.get(zoneId);
		ErpZoneMasterInfo pi;
		if (cached == null) {
			try {
				pi = FDFactory.getZoneInfo(zoneId);
				zoneCache.put(zoneId, pi);
			} catch (FDResourceException ex) {
				throw ex;
			}
		} else {
			pi = (ErpZoneMasterInfo) cached;
		}
		return pi;
	}

	/**
	 * Get zone information.
	 * 
	 * @param zoneId
	 * @return
	 * @throws FDResourceException
	 */
	public static GroupScalePricing getGrpInfo(FDGroup group) throws FDResourceException, FDGroupNotFoundException {
		GroupScalePricing cached = grpCache.get(group);
		GroupScalePricing pi;
		if (cached == null) {
			try {
				pi = FDFactory.getGrpInfo(group);
				grpCache.put(pi, pi);
			} catch (FDResourceException ex) {
				throw ex;
			}
		} else {
			pi = cached;
		}
		return pi;
	}

	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus
	 *            array of SKU codes
	 *
	 * @return a list of FDProductInfo objects found
	 *
	 * @throws FDResourceException
	 *             if an error occured using remote resources
	 */
	public static Collection getProductInfos(String[] skus) throws FDResourceException {
		String[] missingSkus = new String[skus.length];
		int missingCount = 0;
		List foundProductInfos = new ArrayList(skus.length);

		// find skus already in the cache
		Object tempo;
		for (int i = 0; i < skus.length; i++) {
			String sku = skus[i];
			if (sku == null) {
				continue;
			}
			tempo = productInfoCache.get(sku);
			if (tempo == null) {
				missingSkus[missingCount++] = skus[i];
			} else if (tempo != SKU_NOT_FOUND) {
				foundProductInfos.add(tempo);
			}
		}

		if (missingCount == 0) {
			// nothing's missing
			return foundProductInfos;
		}

		// get what's missing
		String[] loadSkus;
		if (missingCount == missingSkus.length) {
			// everything's missing
			loadSkus = missingSkus;
		} else {
			loadSkus = new String[missingCount];
			System.arraycopy(missingSkus, 0, loadSkus, 0, missingCount);
		}
		Collection pis = FDFactory.getProductInfos(loadSkus);

		FDProductInfo tempi;

		// cache these
		for (Iterator i = pis.iterator(); i.hasNext();) {
			tempi = (FDProductInfo) i.next();
			productInfoCache.put(tempi.getSkuCode(), tempi);
			// New Cache to Speed up barcode scanning
			if (tempi.getUpc() != null && tempi.getUpc().trim().length() > 0) {
				productUpcCache.put(tempi.getUpc(), tempi);
			}
		}

		foundProductInfos.addAll(pis);
		return foundProductInfos;
	}

	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus
	 *            array of SKU codes
	 *
	 * @return a list of FDProductInfo objects found
	 *
	 * @throws FDResourceException
	 *             if an error occured using remote resources
	 */
	public static Collection getZoneInfos(String[] zoneIds) throws FDResourceException {

		String[] missingZoneIds = new String[zoneIds.length];
		int missingCount = 0;
		List foundZoneInfos = new ArrayList(zoneIds.length);

		// find skus already in the cache
		Object tempo;
		for (int i = 0; i < zoneIds.length; i++) {
			tempo = zoneCache.get(zoneIds[i]);
			if (tempo == null) {
				missingZoneIds[missingCount++] = zoneIds[i];
			} else if (tempo != SKU_NOT_FOUND) {
				foundZoneInfos.add(tempo);
			}
		}

		if (missingCount == 0) {
			// nothing's missing
			return foundZoneInfos;
		}

		// get what's missing
		String[] loadZoneIds;
		if (missingCount == missingZoneIds.length) {
			// everything's missing
			loadZoneIds = missingZoneIds;
		} else {
			loadZoneIds = new String[missingCount];
			System.arraycopy(missingZoneIds, 0, loadZoneIds, 0, missingCount);
		}
		List pis = new ArrayList();
		ErpZoneMasterInfo tempi;

		// cache these
		for (int i = 0; i < loadZoneIds.length; i++) {
			tempi = FDFactory.getZoneInfo(zoneIds[i]);
			zoneCache.put(tempi.getSapId(), tempi);
			foundZoneInfos.add(tempi);
		}
		return foundZoneInfos;
	}

	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus
	 *            array of SKU codes
	 *
	 * @return a list of FDProductInfo objects found
	 *
	 * @throws FDResourceException
	 *             if an error occured using remote resources
	 */
	public static Collection getGrpInfos(FDGroup[] grpIds) throws FDResourceException {

		FDGroup[] missingGrpIds = new FDGroup[grpIds.length];
		int missingCount = 0;
		List<GroupScalePricing> foundGrpInfos = new ArrayList<GroupScalePricing>(grpIds.length);

		// find Groups already in the cache
		GroupScalePricing tempo;
		for (int i = 0; i < grpIds.length; i++) {
			tempo = grpCache.get(grpIds[i]);
			if (tempo == null) {
				missingGrpIds[missingCount++] = grpIds[i];
			} else if (tempo != GROUP_NOT_FOUND) {
				foundGrpInfos.add(tempo);
			}
		}

		if (missingCount == 0) {
			// nothing's missing
			return foundGrpInfos;
		}

		// get what's missing
		FDGroup[] loadGrpIds;
		if (missingCount == missingGrpIds.length) {
			// everything's missing
			loadGrpIds = missingGrpIds;
		} else {
			loadGrpIds = new FDGroup[missingCount];
			System.arraycopy(missingGrpIds, 0, loadGrpIds, 0, missingCount);
		}
		GroupScalePricing tempi;
		// cache these
		Collection<GroupScalePricing> gsInfos = FDFactory.getGrpInfo(loadGrpIds);
		for (Iterator<GroupScalePricing> i = gsInfos.iterator(); i.hasNext();) {
			tempi = i.next();
			grpCache.put(tempi, tempi);
		}

		foundGrpInfos.addAll(gsInfos);
		return foundGrpInfos;
	}

	/**
	 * Get product with specified version.
	 *
	 * @param sku
	 *            SKU code
	 * @param version
	 *            requested version
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException
	 *             if the SKU was not found in ERP services
	 * @throws FDResourceException
	 *             if an error occured using remote resources
	 */
	public static FDProduct getProduct(String sku, int version) throws FDResourceException, FDSkuNotFoundException {
		FDSku fdSku = new FDSku(sku, version);
		FDProduct p = productCache.get(fdSku);
		if (p == null) {
			p = FDFactory.getProduct(sku, version);
			productCache.put(fdSku, p);
		}
		// productCache.put(fdSku, p);
		return p;
	}

	/**
	 * Convenience method to get the product for the specified FDProductInfo.
	 *
	 * @param sku
	 *            FDSku instance
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException
	 *             if the SKU was not found in ERP services
	 * @throws FDResourceException
	 *             if an error occured using remote resources
	 */
	public static FDProduct getProduct(FDSku sku) throws FDResourceException, FDSkuNotFoundException {
		FDProduct p = productCache.get(sku);
		if (p == null) {
			p = FDFactory.getProduct(sku);
			productCache.put(sku, p);
		}
		// productCache.put(sku, p);
		return p;
	}

	/**
	 * Convenience method to get the product for the specified FDProductInfo.
	 *
	 * @param skus
	 *            array of FDSku instances
	 *
	 * @return collection of FDProduct objects found
	 *
	 * @throws FDResourceException
	 *             if an error occured using remote resources
	 */
	public static Collection getProducts(FDSku[] skus) throws FDResourceException {
		FDSku[] missingSkus = new FDSku[skus.length];
		int missingCount = 0;
		List foundProducts = new ArrayList(skus.length);

		// find skus already in the cache
		FDProduct temp;
		for (int i = 0; i < skus.length; i++) {
			temp = productCache.get(skus[i]);
			if (temp == null) {
				missingSkus[missingCount++] = skus[i];
			} else {
				foundProducts.add(temp);
			}
		}

		// get what's missing
		FDSku[] loadSkus;
		if (missingCount == missingSkus.length) {
			loadSkus = missingSkus;
		} else {
			loadSkus = new FDSku[missingCount];
			System.arraycopy(missingSkus, 0, loadSkus, 0, missingCount);
		}
		Collection prods = FDFactory.getProducts(loadSkus);

		// cache these
		FDProduct o;
		for (Iterator<FDProduct> i = prods.iterator(); i.hasNext();) {
			o = i.next();
			productCache.put(o, o);
		}

		foundProducts.addAll(prods);
		return foundProducts;
	}

	public static Collection getOutOfStockSkuCodes() throws FDResourceException {
		return FDFactory.getOutOfStockSkuCodes();
	}

	public static Collection findSKUsByDeal(double lowerLimit, double upperLimit, List skuPrefixes)
			throws FDResourceException {
		return FDFactory.findSKUsByDeal(lowerLimit, upperLimit, skuPrefixes);
	}

	public static List findPeakProduceSKUsByDepartment(List skuPrefixes) throws FDResourceException {
		return FDFactory.findPeakProduceSKUsByDepartment(skuPrefixes);
	}

	public static Map<String, Map<String, Date>> getNewSkus() throws FDResourceException {
		return FDFactory.getNewSkus();
	}

	public static Map<String, Map<String, Date>> getBackInStockSkus() throws FDResourceException {
		return FDFactory.getBackInStockSkus();
	}

	public static List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws FDResourceException {
		return FDFactory.getSkuAvailabilityHistory(skuCode);
	}

	public static Map<String, Map<String, Date>> getOverriddenNewSkus() throws FDResourceException {
		return FDFactory.getOverriddenNewSkus();
	}

	public static Map<String, Map<String, Date>> getOverdiddenBackInStockSkus() throws FDResourceException {
		return FDFactory.getOverriddenBackInStockSkus();
	}

	public static void refreshNewAndBackViews() throws FDResourceException {
		FDFactory.refreshNewAndBackViews();
	}

	public static FDGroup getLatestActiveGroup(String groupId) throws FDResourceException, FDGroupNotFoundException {
		return FDFactory.getLatestActiveGroup(groupId);
	}

	public static void refreshProductPromotionSku(String sku) throws FDResourceException {
		FDProductInfo pi = null;
		try {
			pi = FDFactory.getProductInfo(sku);
			productInfoCache.put(sku, pi);
			if (null != pi && pi.getUpc() != null && pi.getUpc().trim().length() > 0) {
				productUpcCache.put(pi.getUpc(), pi);
			}
			if (null != pi) {
				FDSku fdSku = new FDSku(sku, pi.getVersion());
				FDProduct p = FDFactory.getProduct(fdSku);
				productCache.put(fdSku, p);
			}
		} catch (FDSkuNotFoundException ex) {
			productInfoCache.put(sku, SKU_NOT_FOUND);
		}
	}

	public static void loadMaterialGroupCache() {
		FDMaterialGroupCache.loadMaterialGroups();
	}

	public static Map<String, FDGroup> getGroupsByMaterial(String material) {
		return FDMaterialGroupCache.getGroupsByMaterial(material);
	}
}
