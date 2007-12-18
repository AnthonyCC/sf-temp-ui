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
		new LazyTimedCache(FDStoreProperties.getProductCacheSize(), FDStoreProperties.getRefreshSecsProductInfo() * 1000);

	/**
	 * FDProduct instances hashed by FDSku instances.
	 */
	private final static LazyTimedCache productCache = new LazyTimedCache(FDStoreProperties.getProductCacheSize(), FDStoreProperties.getRefreshSecsProduct() * 1000);

	/**
	 * Thread that reloads expired productInfos, every 10 seconds.
	 */
	private final static Thread piRefreshThread = new LazyTimedCache.RefreshThread(productInfoCache, "FDProductInfoRefresh", 10000) {
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
	private final static Thread prodRefreshThread = new LazyTimedCache.RefreshThread(productCache, "FDProductRefresh", 5*60*1000) {
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

	public static Collection getNewSkuCodes(int days) throws FDResourceException {
    	return FDFactory.getNewSkuCodes(days);
    }
	
	public static Collection getReintroducedSkuCodes(int days) throws FDResourceException {
    	return FDFactory.getReintroducedSkuCodes(days);
    }

	public static Collection getOutOfStockSkuCodes() throws FDResourceException {
		return FDFactory.getOutOfStockSkuCodes();
	}
	
}
