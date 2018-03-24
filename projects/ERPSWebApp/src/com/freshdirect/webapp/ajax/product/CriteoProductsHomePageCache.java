package com.freshdirect.webapp.ajax.product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.brandads.FDBrandProductsAdManager;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CriteoProductsHomePageCache {

	private static final Category LOGGER = LoggerFactory.getInstance(CriteoProductsHomePageCache.class);	
	private ConcurrentHashMap<String, List<HLBrandProductAdInfo>> cacheCriteoMap = new ConcurrentHashMap<String, List<HLBrandProductAdInfo>>();
	private  ArrayList<HLBrandProductAdInfo> hlba = new ArrayList<HLBrandProductAdInfo>();
	
	private final static Object lock = new Object();
	private static CriteoProductsHomePageCache sharedInstance = null;
	

	
	public ArrayList<HLBrandProductAdInfo> getProducts() {
		return this.getPrductsMap();
	}
	
	protected ArrayList<HLBrandProductAdInfo> getPrductsMap() {
		ArrayList<HLBrandProductAdInfo> productsMap = this.criteoProd.get();
		if(null !=productsMap && !productsMap.isEmpty()){
		//	this.hlba.clear();	/* if in case CretioProducts does'nt reflect Updated list, this should be uncommented. */
			this.hlba= productsMap;
		}
		return this.hlba;
	}
									/*  30 min interval , 60,000 milliseconds/Min	*/
	private ExpiringReference< ArrayList<HLBrandProductAdInfo>> criteoProd = new ExpiringReference<ArrayList<HLBrandProductAdInfo>>(FDStoreProperties.getFDHomeCriteoServerCacheRefresh() * 60 * 1000) {
		@Override
		protected ArrayList<HLBrandProductAdInfo> load() {
			try {
					LOGGER.info("REFRESHING start");
					hlba.clear();		/* clearing previous cached products and updating with new list */
					hlba =	loadProducts();
					LOGGER.info("REFRESH completed.");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			return hlba;
		}
	};
	
	
	public void forceRefresh(String type) {
			criteoProd.forceRefresh();
	}
	
	public static CriteoProductsHomePageCache getInstance() {
		synchronized(lock) {
			if (sharedInstance == null) {
				sharedInstance = new CriteoProductsHomePageCache();
			}
		}
		
		return sharedInstance;
	}
	
	private CriteoProductsHomePageCache() {
		loadProducts();
	}
	
	private  ArrayList<HLBrandProductAdInfo> loadProducts(){
			HLBrandProductAdRequest hLBrandProductAdRequest = new HLBrandProductAdRequest();
			HLBrandProductAdResponse response = null;
			List<String> keys = CriteoProductsUtil.getFDSearchPriorityKeyWords();
			int  maxCount = FDStoreProperties.getFDHomeCriteoMaxDisplayProducts();

			if (!keys.isEmpty() && keys.size() != 0) {
				for (String key : keys) {
					try {
						hLBrandProductAdRequest.setSearchKeyWord(key);
						/*
						 * we can fetch products without PUserId, but Not with  'empty string'.	 */
						if (hLBrandProductAdRequest.getUserId() == null) {
							hLBrandProductAdRequest.setUserId("null");
						}
						response = FDBrandProductsAdManager.getHLadproductToHomeByFDPriority(hLBrandProductAdRequest);

						if (null != response && null != response.getSearchProductAd()) {
								cacheCriteoMap.put(key, response.getSearchProductAd());
						}
					} catch (FDResourceException e) {
						LOGGER.debug("Exception occured while making call to Criteo search-Api: " + e);
					}
				}
			}
			//setting HasMap to ArrayList here
			int i=0;
			int j=0;
			boolean hasValues= false;
		if (!keys.isEmpty() && keys.size() != 0) {
			while (i < maxCount && j < maxCount && i >= j) {
				boolean found = false;
				for (String key : keys) {
					if (null != cacheCriteoMap.get(key) && !cacheCriteoMap.get(key).isEmpty()
							&& cacheCriteoMap.get(key).size() >= (j + 1) && (i < maxCount) ) {
						hlba.add(cacheCriteoMap.get(key).get(j));
						found = true;
						hasValues = true;
						i++;
					}
				}
				if (!found) {
					break;
				}
				j++;
			}
		}
		// to satisfy maxCount property we are duplicating 
		if(hasValues && !hlba.isEmpty() && (hlba.size()<maxCount) ) {
			i= hlba.size();
			j=0;
			while ( i < maxCount) {
				hlba.add(hlba.get(j));//System.out.println(list.get(i));
				i++;	
				j++;
			}
		}
		
		return hlba;	/* this is the final list which we are saving in server cache*/
	
	}
}