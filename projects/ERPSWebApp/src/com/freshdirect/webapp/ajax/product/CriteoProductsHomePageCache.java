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
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;

public class CriteoProductsHomePageCache {

	private static final Category LOGGER = LoggerFactory.getInstance(CriteoProductsHomePageCache.class);	
	private ConcurrentHashMap<String, List<HLBrandProductAdInfo>> cacheCriteoMap = new ConcurrentHashMap<String, List<HLBrandProductAdInfo>>();
//	private ConcurrentHashMap<String, String> pageBeaconMap = new ConcurrentHashMap<String, String>();
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
			int counter=0;

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
						List<HLBrandProductAdInfo> respList = response.getSearchProductAd();
						List<HLBrandProductAdInfo> avaiProductFDList= new ArrayList<HLBrandProductAdInfo>();
						for (HLBrandProductAdInfo resp : respList) {
							ProductModel productModel = null;
							try {
								productModel = ContentFactory.getInstance().getProduct(resp.getProductSKU());
							} catch (Exception e) {
								LOGGER.info("SKu not found for Hooklogic product: " + resp.getProductSKU());
							}
							if (null != productModel && !productModel.isUnavailable()) {
								avaiProductFDList.add(resp);
							}
						}
						/*  page beacon of Api call, we are setting to product level here */
						 final String A_SHOWN="&ashown=";
						 final StringBuffer updatedPageBeacon= new StringBuffer(A_SHOWN);
						 //String pageBeaconString = null;
						if (!avaiProductFDList.isEmpty()) {
							for (HLBrandProductAdInfo pdata : avaiProductFDList) {
								updatedPageBeacon.append(((A_SHOWN.equals(updatedPageBeacon.toString()))
										? pdata.getProductSKU() : "," + pdata.getProductSKU()));
							}
							avaiProductFDList.get(0).setPageBeacon(response.getPageBeacon() + updatedPageBeacon.toString());
						}
						
						if(!avaiProductFDList.isEmpty() && avaiProductFDList.size() < 5){
							int i= avaiProductFDList.size();
							int j=0;
							while ( i < 5) {
								avaiProductFDList.add(avaiProductFDList.get(j));
								i++;	
								j++;
							}
						}
						cacheCriteoMap.put(key, avaiProductFDList);
						//pageBeaconMap.put(key, response.getPageBeacon());
						counter = counter + avaiProductFDList.size();
					}
					} catch (FDResourceException e) {
						LOGGER.debug("Exception occured while making call to Criteo search-Api: " + e);
					}
				}
			}
			//setting HasMap to ArrayList here
			int i=0;
			int j=0;
		if (!keys.isEmpty() && keys.size() != 0 && counter != 0) {
			boolean found = false;
			while (counter >=  i++) {
				for (String key : keys) {
					if (null != cacheCriteoMap.get(key) && !cacheCriteoMap.get(key).isEmpty()
							&& cacheCriteoMap.get(key).size() >= (j + 1) ) {
						HLBrandProductAdInfo hlBrandProduct = cacheCriteoMap.get(key).get(j);
						//String pageBeacon = pageBeaconMap.get(key);
					//	hlBrandProduct.setPageBeacon(pageBeacon);
						hlba.add(hlBrandProduct);
						found = true;
						i++;
					}
				}
				if (!found) {
					break;
				}
				j++;
			}
		}

		return hlba;	/* this is the final list which we are saving in server cache*/
	
	}
}