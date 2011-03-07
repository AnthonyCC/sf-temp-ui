/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.util.*;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.framework.util.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

/**
 * Caching proxy for FDFactory.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDCachedFactory {

	private static Category LOGGER = LoggerFactory.getInstance( FDCachedFactory.class );

	/**@link dependency
	 * @label uses*/
	/*#FDFactory lnkFDFactory;*/

	private final static Object SKU_NOT_FOUND = new Object();

	/** 
	 * FDProductInfo instances hashed by SKU strings.
	 */
	private final static LazyTimedCache productInfoCache =
		new LazyTimedCache("FDProductInfo", FDStoreProperties.getProductCacheSize(), FDStoreProperties.getRefreshSecsProductInfo() * 1000);

	/**
	 * FDProduct instances hashed by FDSku instances.
	 */
	private final static LazyTimedCache productCache = new LazyTimedCache("FDProduct", FDStoreProperties.getProductCacheSize(), FDStoreProperties.getRefreshSecsProduct() * 1000);
	
	
	/**
	 * FDProduct instances hashed by FDSku instances.
	 */
	private final static LazyTimedCache zoneCache = new LazyTimedCache("FDZoneInfo", FDStoreProperties.getZoneCacheSize(), FDStoreProperties.getRefreshSecsProduct() * 1000);

	
	/**
	 * FDProduct instances hashed by FDSku instances.
	 */
	private final static LazyTimedCache<FDGroup, GroupScalePricing> grpCache = new LazyTimedCache<FDGroup, GroupScalePricing>("FDGroupInfo",FDStoreProperties.getGrpCacheSize(), FDStoreProperties.getRefreshSecsProduct() * 1000);

	
	/**
	 * Thread that reloads expired productInfos, every 10 seconds.
	 */
	private final static Thread gRefreshThread = new LazyTimedCache.RefreshThread(grpCache, 10000) {
		protected void refresh(List expiredKeys) {
			try {
				LOGGER.debug("FDGrpRefresh reloading "+expiredKeys.size()+" grpInfos");
				Collection<GroupScalePricing> pis = FDFactory.getGrpInfo((FDGroup[])expiredKeys.toArray(new FDGroup[0]));

				// cache these
				GroupScalePricing tempi;
				for (Iterator i=pis.iterator(); i.hasNext(); ) {
					tempi = (GroupScalePricing)i.next();
					this.cache.put(tempi, tempi);
				}

			} catch (Exception ex) {
				LOGGER.warn("Error occured in FDGrpInfoRefresh", ex);
			}
		}
	};

	
	
	/**
	 * Thread that reloads expired productInfos, every 10 seconds.
	 */
	private final static Thread zRefreshThread = new LazyTimedCache.RefreshThread(zoneCache,10000) {
		protected void refresh(List expiredKeys) {
			try {
				LOGGER.debug("FDZoneRefresh reloading "+expiredKeys.size()+" zoneInfos");
				Collection pis = FDFactory.getZoneInfo((String[])expiredKeys.toArray(new String[0]) );

				// cache these
				ErpZoneMasterInfo tempi;
				for (Iterator i=pis.iterator(); i.hasNext(); ) {
					tempi = (ErpZoneMasterInfo)i.next();
					this.cache.put(tempi.getSapId(), tempi);
				}

			} catch (Exception ex) {
				LOGGER.warn("Error occured in FDProductInfoRefresh", ex);
			}
		}
	};

	

	/**
	 * Thread that reloads expired productInfos, every 10 seconds.
	 */
	private final static Thread piRefreshThread = new LazyTimedCache.RefreshThread(productInfoCache, 10000) {
		protected void refresh(List expiredKeys) {
			try {
				LOGGER.debug("FDProductInfoRefresh reloading "+expiredKeys.size()+" productInfos");
				Collection pis = FDFactory.getProductInfos( (String[])expiredKeys.toArray(new String[0]) );

				// cache these
				FDProductInfo tempi;
				for (Iterator i=pis.iterator(); i.hasNext(); ) {
					tempi = (FDProductInfo)i.next();
					this.cache.put(tempi.getSkuCode(), tempi);
				}

			} catch (Exception ex) {
				LOGGER.warn("Error occured in FDProductInfoRefresh", ex);
			}
		}
	};

	/**
	 * Thread that reloads expired products, every 5 minutes.
	 */
	private final static Thread prodRefreshThread = new LazyTimedCache.RefreshThread(productCache, 5*60*1000) {
		protected void refresh(List expiredKeys) {
			try {
				LOGGER.debug("FDProductRefresh reloading "+expiredKeys.size()+" products");
				Collection prods = FDFactory.getProducts( (FDSku[])expiredKeys.toArray(new FDSku[0]) );

				// cache these
				FDProduct temp;
				for (Iterator i=prods.iterator(); i.hasNext(); ) {
					temp = (FDProduct)i.next();
					this.cache.put(temp, temp);
				}

			} catch (Exception ex) {
				LOGGER.warn("Error occured in FDProductRefresh", ex);
			}
		}
	};
	
	static {
		piRefreshThread.start();
		prodRefreshThread.start();
	}

	public static FDProductInfo getProductInfo(String sku, int version) throws FDResourceException, FDSkuNotFoundException {
		return FDFactory.getProductInfo(sku, version);	
	}

	/**
	 * Get current product information object for sku.
	 *
	 * @param sku SKU code
	 *
	 * @return FDProductInfo object
	 *
 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static FDProductInfo getProductInfo(String sku) throws FDResourceException, FDSkuNotFoundException {
		Object cached = productInfoCache.get(sku);
		FDProductInfo pi;
		if (cached==null) {
			try { 
				pi = FDFactory.getProductInfo(sku);
				productInfoCache.put(sku, pi);
			} catch (FDSkuNotFoundException ex) {
				productInfoCache.put(sku, SKU_NOT_FOUND);
				throw ex;	
			}
		} else if (cached==SKU_NOT_FOUND) {
			throw new FDSkuNotFoundException("SKU "+sku+" not found (cached)");
		} else {
			pi = (FDProductInfo) cached;
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
	public static ErpZoneMasterInfo getZoneInfo(String zoneId) throws FDResourceException{
		Object cached = zoneCache.get(zoneId);
		ErpZoneMasterInfo pi;
		if (cached==null) {
			try { 
				pi = FDFactory.getZoneInfo(zoneId);
				zoneCache.put(zoneId, pi);
			} catch (FDResourceException ex) {				
				throw ex;	
			}
		}  else {
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
	public static GroupScalePricing getGrpInfo(FDGroup group) throws FDResourceException, FDGroupNotFoundException{
		GroupScalePricing cached = grpCache.get(group);
		GroupScalePricing pi;
		if (cached==null) {
			try { 
				pi = FDFactory.getGrpInfo(group);
				grpCache.put(pi, pi);
			} catch (FDResourceException ex) {				
				throw ex;	
			}
		}  else {
			pi = (GroupScalePricing) cached;
		}
		return pi;
	}
	
	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus array of SKU codes
	 *
	 * @return a list of FDProductInfo objects found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static Collection getProductInfos(String[] skus) throws FDResourceException {
		String[] missingSkus = new String[skus.length];
		int missingCount = 0;
		List foundProductInfos = new ArrayList(skus.length);

		// find skus already in the cache
		Object tempo;		
		for (int i=0; i<skus.length; i++) {
			tempo = productInfoCache.get(skus[i]);
			if (tempo==null) {
				missingSkus[missingCount++]=skus[i];
			} else if (tempo!=SKU_NOT_FOUND) {
				foundProductInfos.add(tempo);
			}
		}

		if (missingCount==0) {
			// nothing's missing
			return foundProductInfos;
		}

		// get what's missing
		String[] loadSkus;
		if (missingCount==missingSkus.length) {
			// everything's missing
			loadSkus = missingSkus;
		} else {
			loadSkus = new String[missingCount];
			System.arraycopy(missingSkus, 0, loadSkus, 0, missingCount);
		}
		Collection pis = FDFactory.getProductInfos(loadSkus);

		FDProductInfo tempi;

		// cache these
		for (Iterator i=pis.iterator(); i.hasNext(); ) {
			tempi = (FDProductInfo)i.next();
			productInfoCache.put(tempi.getSkuCode(), tempi);
		}
		
		foundProductInfos.addAll(pis);
		return foundProductInfos;
	}

	

	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus array of SKU codes
	 *
	 * @return a list of FDProductInfo objects found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static Collection getZoneInfos(String[] zoneIds) throws FDResourceException {
				
		String[] missingZoneIds = new String[zoneIds.length];
		int missingCount = 0;
		List foundZoneInfos = new ArrayList(zoneIds.length);

		// find skus already in the cache
		Object tempo;		
		for (int i=0; i<zoneIds.length; i++) {
			tempo = zoneCache.get(zoneIds[i]);
			if (tempo==null) {
				missingZoneIds[missingCount++]=zoneIds[i];
			} else if (tempo!=SKU_NOT_FOUND) {
				foundZoneInfos.add(tempo);
			}
		}

		if (missingCount==0) {
			// nothing's missing
			return foundZoneInfos;
		}

		// get what's missing
		String[] loadZoneIds;
		if (missingCount==missingZoneIds.length) {
			// everything's missing
			loadZoneIds = missingZoneIds;
		} else {
			loadZoneIds = new String[missingCount];
			System.arraycopy(missingZoneIds, 0, loadZoneIds, 0, missingCount);
		}
		List pis = new ArrayList();
		ErpZoneMasterInfo tempi;
		

		
		// cache these
		for (int i=0;i<loadZoneIds.length;i++) {
			tempi= FDFactory.getZoneInfo(zoneIds[i]);
			zoneCache.put(tempi.getSapId(), tempi);
			foundZoneInfos.add(tempi);
		}				
		return foundZoneInfos;						
	}
	
	
	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus array of SKU codes
	 *
	 * @return a list of FDProductInfo objects found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static Collection getGrpInfos(FDGroup[] grpIds) throws FDResourceException {
				
		FDGroup[] missingGrpIds = new FDGroup[grpIds.length];
		int missingCount = 0;
		List<GroupScalePricing> foundGrpInfos = new ArrayList<GroupScalePricing>(grpIds.length);

		// find Groups already in the cache
		GroupScalePricing tempo;		
		for (int i=0; i<grpIds.length; i++) {
			tempo = grpCache.get(grpIds[i]);
			if (tempo==null) {
				missingGrpIds[missingCount++]=grpIds[i];
			} else if (tempo!=SKU_NOT_FOUND) {
				foundGrpInfos.add(tempo);
			}
		}

		if (missingCount==0) {
			// nothing's missing
			return foundGrpInfos;
		}

		// get what's missing
		FDGroup[] loadGrpIds;
		if (missingCount==missingGrpIds.length) {
			// everything's missing
			loadGrpIds = missingGrpIds;
		} else {
			loadGrpIds = new FDGroup[missingCount];
			System.arraycopy(missingGrpIds, 0, loadGrpIds, 0, missingCount);
		}
		GroupScalePricing tempi;
		// cache these
		Collection<GroupScalePricing> gsInfos = FDFactory.getGrpInfo(loadGrpIds);
		for (Iterator<GroupScalePricing> i=gsInfos.iterator(); i.hasNext(); ) {
			tempi = (GroupScalePricing)i.next();
			grpCache.put(tempi, tempi);
		}
		
		foundGrpInfos.addAll(gsInfos);
		return foundGrpInfos;		
	}
	
	
	/**
	 * Get product with specified version. 
	 *
	 * @param sku SKU code
	 * @param version requested version
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static FDProduct getProduct(String sku, int version) throws FDResourceException, FDSkuNotFoundException {
		FDSku fdSku = new FDSku(sku, version);
		FDProduct p = (FDProduct)productCache.get(fdSku);
		if (p==null) {
			p = FDFactory.getProduct(sku, version);
		}
		productCache.put(fdSku, p);
		return p;
	}

	/**
	 * Convenience method to get the product for the specified FDProductInfo.
	 *
	 * @param sku FDSku instance
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static FDProduct getProduct(FDSku sku) throws FDResourceException, FDSkuNotFoundException {
		FDProduct p = (FDProduct)productCache.get(sku);
		if (p==null) {
			p = FDFactory.getProduct(sku);
		}
		productCache.put(sku, p);
		return p;
	}

	/**
	 * Convenience method to get the product for the specified FDProductInfo.
	 *
	 * @param skus array of FDSku instances
	 *
	 * @return collection of FDProduct objects found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static Collection getProducts(FDSku[] skus) throws FDResourceException {
		FDSku[] missingSkus = new FDSku[skus.length];
		int missingCount = 0;
		List foundProducts = new ArrayList(skus.length);

		// find skus already in the cache
		FDProduct temp;
		for (int i=0; i<skus.length; i++) {
			temp = (FDProduct) productCache.get(skus[i]);
			if (temp==null) {
				missingSkus[missingCount++]=skus[i];
			} else {
				foundProducts.add(temp);
			}
		}

		// get what's missing
		FDSku[] loadSkus;
		if (missingCount==missingSkus.length) {
			loadSkus = missingSkus;
		} else {
			loadSkus = new FDSku[missingCount];
			System.arraycopy(missingSkus, 0, loadSkus, 0, missingCount);
		}
		Collection prods = FDFactory.getProducts(loadSkus);

		// cache these
		Object o;
		for (Iterator i=prods.iterator(); i.hasNext(); ) {
			o = i.next();
			productCache.put(o,o);
		}
		
		foundProducts.addAll(prods);
		return foundProducts;
	}

	public static Collection getOutOfStockSkuCodes() throws FDResourceException {
		return FDFactory.getOutOfStockSkuCodes();
	}
	
	public static Collection findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes) throws FDResourceException {
		return FDFactory.findSKUsByDeal(lowerLimit, upperLimit, skuPrefixes);
	}
	
	public static List findPeakProduceSKUsByDepartment(List skuPrefixes) throws FDResourceException {
		return FDFactory.findPeakProduceSKUsByDepartment(skuPrefixes);
	}
	
	public static Map<String, Date> getNewSkus() throws FDResourceException {
		return FDFactory.getNewSkus();
	}

	public static Map<String, Date> getBackInStockSkus() throws FDResourceException {
		return FDFactory.getBackInStockSkus();
	}
	
	public static List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws FDResourceException {
		return FDFactory.getSkuAvailabilityHistory(skuCode);
	}

	public static Map<String, Date> getOverriddenNewSkus() throws FDResourceException {
		return FDFactory.getOverriddenNewSkus();
	}

	public static Map<String, Date> getOverdiddenBackInStockSkus() throws FDResourceException {
		return FDFactory.getOverriddenBackInStockSkus();
	}
	
	public static void refreshNewAndBackViews() throws FDResourceException {
		FDFactory.refreshNewAndBackViews();
	}
}
