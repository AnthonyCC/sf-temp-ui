/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.framework.cache.CacheI;
import com.freshdirect.framework.cache.ExternalMemCache;
import com.freshdirect.framework.cache.MemcacheConfiguration;
import com.freshdirect.framework.cache.StatRecorderCache;
import com.freshdirect.framework.util.LazyTimedCache;
import com.freshdirect.framework.util.log.LoggerFactory;

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

	
	/**
	 * FDProduct instances hashed by FDSku instances.
	 */
	private final static StatRecorderCache<String, ErpZoneMasterInfo> zoneCache = StatRecorderCache.wrap( 
	    new LazyTimedCache<String, ErpZoneMasterInfo> ("zoneCache", FDStoreProperties.getZoneCacheSize(), FDStoreProperties.getRefreshSecsProduct() * 1000));

        /**
         * Thread that reloads expired productInfos, every 10 seconds.
         */
        private static Thread piRefreshThread;
	
	
	/**
	 * Thread that reloads expired productInfos, every 10 seconds.
	 */
	private final static Thread zRefreshThread = new LazyTimedCache.RefreshThread<String, ErpZoneMasterInfo>((LazyTimedCache<String, ErpZoneMasterInfo>) zoneCache.getInner(), 10000) {
		protected void refresh(List<String> expiredKeys) {
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
				LOGGER.warn("Error occured in FDZoneRefresh", ex);
			}
		}
	};


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
            try {
                FDProductInfo productInfo = FDProductInfoCache.getInstance().get(sku);
                if ((productInfo == null) || (FDFactory.SKU_NOT_FOUND == productInfo)) {
                    throw new FDSkuNotFoundException("SKU " + sku + " not found (cached)");
                }
                return productInfo;
            } catch (FDRuntimeException e) {
                if (e.getNestedException() instanceof FDSkuNotFoundException) {
                    throw (FDSkuNotFoundException) e.getNestedException(); 
                }
                if (e.getNestedException() instanceof FDResourceException) {
                    throw (FDResourceException) e.getNestedException(); 
                }
                throw e;
            }
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
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus array of SKU codes
	 *
	 * @return a list of FDProductInfo objects found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static List<FDProductInfo> getProductInfos(Collection<String> skus) throws FDResourceException {
	    List<FDProductInfo> foundProductInfos = new ArrayList<FDProductInfo>(skus.size());
	    for (String s : skus) {
	        FDProductInfo productInfo = FDProductInfoCache.getInstance().get(s);
	        if (productInfo != null && productInfo != FDFactory.SKU_NOT_FOUND) {
	            foundProductInfos.add(productInfo);
	        }
	    } 
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
			} else if (tempo!=FDFactory.SKU_NOT_FOUND) {
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
	
	
	public static void setupMemcached() {
            MemcacheConfiguration.configureClient(FDStoreProperties.getMemCachedHost(), FDStoreProperties.getMemCachedPort());
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
		return getProduct(fdSku);
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
	    try {
	        FDProduct prod = FDProductCache.getInstance().getFDProduct(sku);
	        if (prod == null) {
	            throw new FDSkuNotFoundException("SKU not found :" + sku.getSkuCode() + " with version " + sku.getVersion());
	        }
	        return prod;
	    } catch (FDRuntimeException e) {
	        Exception nestedException = e.getNestedException();
                if (nestedException instanceof FDResourceException) {
                    throw (FDResourceException) nestedException;
                }
                if (nestedException instanceof FDSkuNotFoundException) {
                    throw (FDSkuNotFoundException) nestedException;
                }
                throw e;
	    }
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
        public static Collection<FDProduct> getProducts(Collection<FDSku> skus) throws FDResourceException {
            List<FDProduct> foundProducts = new ArrayList<FDProduct>(skus.size());
            final FDProductCache cache = FDProductCache.getInstance();
            for (FDSku sku : skus) {
                try {
                    FDProduct p = cache.get(sku);
                    if (p != null) {
                        foundProducts.add(p);
                    }
                } catch (FDRuntimeException e) {
                    if (e.getNestedException() instanceof FDResourceException) {
                        throw (FDResourceException) e.getNestedException();
                    }
                }
            }
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
	
	public static CacheI<String, ErpZoneMasterInfo> getZoneCache() {
            return zoneCache;
        }

    public static void mockInstances() {
        FDProductInfoCache.mockInstance();
        FDProductCache.mockInstance();
        FDAttributesCache.mockInstance();
    }
}
