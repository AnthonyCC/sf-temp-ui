package com.freshdirect.webapp.ajax.product;

import java.util.List;
import java.util.Map;
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
	private ConcurrentHashMap<String, List<HLBrandProductAdInfo>> hlba = new ConcurrentHashMap<String, List<HLBrandProductAdInfo>>();
	
	private final static Object lock = new Object();
	private static CriteoProductsHomePageCache sharedInstance = null;
	

	
	public Map<String, List<HLBrandProductAdInfo>> getProducts() {
		return this.getPrductsMap();
	}
	
	protected Map<String, List<HLBrandProductAdInfo>> getPrductsMap() {
		Map<String, List<HLBrandProductAdInfo>> productsMap = this.criteoProd.get();
		if(null !=productsMap && !productsMap.isEmpty()){
			//this.hlba.clear();	/* if in case CretioProducts does'nt reflect Updated list, this should be uncommented. */
			this.hlba.putAll(productsMap);
		}
		return this.hlba;
	}
																																		/*  30 min interval , 60,000 milliseconds/Min	*/
	private ExpiringReference< Map<String, List<HLBrandProductAdInfo>>> criteoProd = new ExpiringReference<Map<String, List<HLBrandProductAdInfo>>>(30 * 60 * 1000) {
		@Override
		protected ConcurrentHashMap<String, List<HLBrandProductAdInfo>> load() {
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
	
	private  ConcurrentHashMap<String, List<HLBrandProductAdInfo>> loadProducts(){
			HLBrandProductAdRequest hLBrandProductAdRequest = new HLBrandProductAdRequest();
			HLBrandProductAdResponse response = null;
			List<String> keys = CriteoProductsUtil.getFDSearchPriorityKeyWords();
			int  maxCount = FDStoreProperties.getFDHomeCriteoMaxDisplayProducts();
			int count = 1;
			while (count <= maxCount) {
				if (!keys.isEmpty() && keys.size() != 0) {
				for (String key : keys) {
					try {
						hLBrandProductAdRequest.setSearchKeyWord(key);
						//we can fetch products without PUserId, but Not with 'empty string'.
						if (hLBrandProductAdRequest.getUserId() == null) {
							hLBrandProductAdRequest.setUserId("null");
						}
						response = FDBrandProductsAdManager.getHLadproductToHomeByFDPriority(hLBrandProductAdRequest);
						if(null == response){
							count++;		/* if criteo api is down this will be useful to break out of the loop */
							break;
						}
						if ( null != response.getSearchProductAd()) {
								response.getSearchProductAd().get(0).setPageBeacon(response.getPageBeacon());
								if (!hlba.containsKey(key)) {
									hlba.put(key, response.getSearchProductAd());
								} else { 					// for repeated Keys also we are adding to List here, list.size=12
									hlba.get(key).addAll(response.getSearchProductAd());
								}
								count++;
								if (count >= maxCount) {
									break;
								}
							}
					} catch (FDResourceException e) {
						LOGGER.debug("Exception occured while making call to Criteo search-Api: "+e);
					}
				}
				}
			}
			return hlba;
	}
	
}
