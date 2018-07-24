package com.freshdirect.webapp.ajax.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;

public class CriteoProductsHomePageCache {

	private static final Category LOGGER = LoggerFactory.getInstance(CriteoProductsHomePageCache.class);	
	private ConcurrentHashMap<String, HLBrandProductAdResponse> cacheCriteoMap = new ConcurrentHashMap<String, HLBrandProductAdResponse>();
//	private ConcurrentHashMap<String, String> pageBeaconMap = new ConcurrentHashMap<String, String>();
	//private  ArrayList<HLBrandProductAdInfo> hlba = new ArrayList<HLBrandProductAdInfo>();
	
	private Map<String, HLBrandProductAdResponse> CriteoProductnMap = new  HashMap<String, HLBrandProductAdResponse>();
	
	private final static Object lock = new Object();
	private static CriteoProductsHomePageCache sharedInstance = null;
	

	
	public Map<String,HLBrandProductAdResponse> getProducts() {
		return this.getPrductsMap();
	}
	
	protected Map<String, HLBrandProductAdResponse> getPrductsMap() {
		Map<String, HLBrandProductAdResponse> productsMap = this.criteoProd.get();
		if(null !=productsMap && !productsMap.isEmpty()){
		//	this.hlba.clear();	/* if in case CretioProducts does'nt reflect Updated list, this should be uncommented. */
			this.CriteoProductnMap= productsMap;
		}
		return this.CriteoProductnMap;
	}
									/*  30 min interval , 60,000 milliseconds/Min	*/
	private ExpiringReference<Map<String, HLBrandProductAdResponse>> criteoProd = new ExpiringReference<Map<String, HLBrandProductAdResponse>>(FDStoreProperties.getFDHomeCriteoServerCacheRefresh() * 60 * 1000) {
		@Override
		protected Map<String, HLBrandProductAdResponse> load() {
			try {
					LOGGER.info("REFRESHING start");
					CriteoProductnMap.clear();		/* clearing previous cached products and updating with new list */
					//hlba =	loadProducts();
					CriteoProductnMap =	loadProducts();
					LOGGER.info("REFRESH completed.");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			//return hlba;
			return CriteoProductnMap;
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
	
	private  Map<String, HLBrandProductAdResponse> loadProducts(){
			HLBrandProductAdRequest hLBrandProductAdRequest = new HLBrandProductAdRequest();
			HLBrandProductAdResponse response = null;
			List<String> keys = CriteoProductsUtil.getFDSearchPriorityKeyWords();
			if (!keys.isEmpty() && keys.size() != 0) {
				for (String key : keys) {
					try {
						hLBrandProductAdRequest.setSearchKeyWord(key);
						if (hLBrandProductAdRequest.getUserId() == null) {
							hLBrandProductAdRequest.setUserId("null");
						}
						hLBrandProductAdRequest.setSelectedServiceType("RESIDENTIAL");
						response = FDBrandProductsAdManager.getHLadproductToHomeByFDPriority(hLBrandProductAdRequest);

					if (null != response && null != response.getSearchProductAd()) {
						cacheCriteoMap.put(key, response);
					}
					} catch (FDResourceException e) {
						LOGGER.debug("Exception occured while making call to Criteo search-Api: " + e);
					}catch (Exception e) {
						LOGGER.debug("Exception occured while making call to Criteo search-Api: " + e);
					}
				}
			}
			
	return CriteoProductnMap=cacheCriteoMap;
	
	}
}