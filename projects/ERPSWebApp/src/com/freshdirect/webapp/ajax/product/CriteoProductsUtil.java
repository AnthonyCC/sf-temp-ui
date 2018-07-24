package com.freshdirect.webapp.ajax.product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.brandads.FDBrandProductsAdManager;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.filtering.SearchResultsUtil;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class CriteoProductsUtil
{
    private static final Logger LOG = LoggerFactory.getInstance(CriteoProductsUtil.class);

    private static final String A_SHOWN="&ashown=";
    private static final String A_SHOWN_ALL="&ashown=all";
    private static final String A_SHOWN_NONE="&ashown=none";

	public static void getHlHomePgBrandProducts(FDUserI user,
			ModuleData moduleData) {
		HLBrandProductAdRequest hLBrandProductAdRequest = new HLBrandProductAdRequest();
		List<ProductData> adPrducts = new ArrayList<ProductData>();
		StringBuffer updatedPageBeacon = new StringBuffer(A_SHOWN);
		try {
			FDSessionUser sessionUser = (FDSessionUser) user;
			SearchResultsUtil.setPlatFormValues(user, hLBrandProductAdRequest, sessionUser.isMobilePlatForm(),
					sessionUser.getPlatForm(), sessionUser.getLat(), sessionUser.getPdUserId());
			List<HLBrandProductAdInfo> cretioProductsList=new ArrayList<HLBrandProductAdInfo> ();
			Map<String, HLBrandProductAdResponse> cretioProductsCacheMap=new HashMap<String, HLBrandProductAdResponse> ();
			//cache Criteo products
			cretioProductsCacheMap.putAll(CriteoProductsHomePageCache.getInstance().getProducts());
			cretioProductsList= arrangeHomePageProducts(cretioProductsCacheMap,user,adPrducts);
			if (cretioProductsList != null && !cretioProductsList.isEmpty()){
				addHlBrandProducts(user, adPrducts, updatedPageBeacon, cretioProductsList,false);
			}
			int  maxCount = FDStoreProperties.getFDHomeCriteoMaxDisplayProducts();
			if( null != adPrducts && !adPrducts.isEmpty() && (adPrducts.size() > maxCount) ){
				adPrducts.subList(maxCount, adPrducts.size()).clear();
			}
			moduleData.setAdProducts(adPrducts);
			if(!adPrducts.isEmpty()){			//APPDEV-7148
				List<String> pageBeaconsList = new ArrayList<String>();
				for (ProductData pdata : adPrducts) {
					String pageBeaconOfApi = pdata.getPageBeaconOfApi();
					if (pageBeaconOfApi != null && !pageBeaconsList.contains(pageBeaconOfApi)) {
						pageBeaconsList.add(pageBeaconOfApi);
					}
				}
				moduleData.setAdHomePageBeacon(pageBeaconsList);	/* sending pagebeacons to UI */
			}
		} catch (Exception e) {
			LOG.warn("Exception while populating Criteo returned product: ", e);
		}
	}

	private static void populateHlBrandPrdData(List<ProductData> adPrducts, StringBuffer updatedPageBeacon,
			HLBrandProductAdInfo hlBrandProductAdMetaInfo, ProductData productData) {
		productData.setFeatured(true);
		productData.setClickBeacon(hlBrandProductAdMetaInfo.getClickBeacon());
		productData.setImageBeacon(hlBrandProductAdMetaInfo.getImpBeacon());
		productData.setPageBeaconOfApi(hlBrandProductAdMetaInfo.getPageBeacon());
		adPrducts.add(productData);
		updatedPageBeacon.append(((A_SHOWN.equals(updatedPageBeacon.toString()))
				? productData.getSkuCode().toUpperCase() : "," + productData.getSkuCode().toUpperCase()));
	}

	private static void addHlBrandProducts(FDUserI user, List<ProductData> adPrducts, StringBuffer updatedPageBeacon,
			List<HLBrandProductAdInfo> hlBrandAdProductsMeta, boolean pdpPage) throws FDSkuNotFoundException {
		for (Iterator<HLBrandProductAdInfo> iterator = hlBrandAdProductsMeta.iterator(); iterator.hasNext();) {
			HLBrandProductAdInfo hlBrandProductAdMetaInfo = iterator.next();
			ProductModel productModel = null;
			try{
				productModel = ContentFactory.getInstance()
					.getProduct(hlBrandProductAdMetaInfo.getProductSKU());
			}catch(Exception e){
				LOG.debug("SKu not found for Hooklogic product: "+hlBrandProductAdMetaInfo.getProductSKU());
			}

			if (null != productModel && !productModel.isUnavailable()) {
				ProductData productData = null;
				try {
					productData = ProductDetailPopulator.createProductData(user, productModel);
					if (null != productData && null != productData.getSkuCode()) {
						populateHlBrandPrdData(adPrducts, updatedPageBeacon, hlBrandProductAdMetaInfo, productData);
						if(pdpPage && adPrducts.size()==ErpServicesProperties.getPropHlPdppageMaxmesCount())
							break;

					}

				} catch (HttpErrorResponse e) {
					LOG.warn("Exception while populating Criteo returned product: ", e);
				} catch (FDResourceException e) {
					LOG.warn("Exception while populating Criteo returned product: ", e);
				} catch (FDSkuNotFoundException e) {
					LOG.warn("Exception while populating Criteo returned product: ", e);
				} catch (Exception e) {
					LOG.warn("Exception while populating Criteo returned product: ", e);
				}
			}
		}
	}


	public static void getHlBrandPdpProducts(FDUserI user, BrowseData browseData) {
		HLBrandProductAdRequest hLBrandProductAdRequest = new HLBrandProductAdRequest();
		List<ProductData> adPrducts = new ArrayList<ProductData>();
		StringBuffer updatedPageBeacon = new StringBuffer(A_SHOWN);
		int productsCount = 0;
		boolean pdpPage=true;
		try {
			FDSessionUser sessionUser = (FDSessionUser) user;
			SearchResultsUtil.setPlatFormValues(user, hLBrandProductAdRequest, sessionUser.isMobilePlatForm(),
					sessionUser.getPlatForm(), sessionUser.getLat(), sessionUser.getPdUserId());
			if (hLBrandProductAdRequest.getUserId() != null) {
				setProductSkucode(browseData, hLBrandProductAdRequest);
				HLBrandProductAdResponse response = FDBrandProductsAdManager.getHLadproductToPdp(hLBrandProductAdRequest);
				if (response != null) {
					List<HLBrandProductAdInfo> hlBrandAdProductsMeta = response.getProductAd();
					if (hlBrandAdProductsMeta != null){
						productsCount = hlBrandAdProductsMeta.size();
						addHlBrandProducts(user, adPrducts, updatedPageBeacon, hlBrandAdProductsMeta,pdpPage);
					}
					browseData.getAdProducts().setProducts(adPrducts);
					browseData.getAdProducts().setUpdatePdpPageBeacon(response.getUpdatePdpPageBeacon());
					if (productsCount!=0 && productsCount == adPrducts.size()) {
						browseData.getAdProducts().setPageBeacon(response.getPageBeacon() + A_SHOWN_ALL);
					} else if (productsCount > 0 && adPrducts.size() == 0) {
						browseData.getAdProducts().setPageBeacon(response.getPageBeacon() + A_SHOWN_NONE);
					} else {
						browseData.getAdProducts().setPageBeacon(response.getPageBeacon()+ updatedPageBeacon.toString());
					}
				}
			}
		} catch (Exception e) {
			LOG.warn("Exception while populating Criteo PDP product: ", e);
		}
	}

	public static void setProductSkucode(BrowseData browseData,	HLBrandProductAdRequest hLBrandProductAdRequest) {
		try {
			ContentNodeModel product = ContentFactory.getInstance()	.getContentNode(browseData.getProductId());
			if (product instanceof ProductModel) {
				String skuCode = ((ProductModel) product).getDefaultSkuCode();
				
				try {
					//if product is disc, then getDefaultSkuCode returns null, but on PDP we need the criteo prods anyway, so get first sku
					if (skuCode == null && ((ProductModel)product).getSkuCodes().size() > 0 ) {
						skuCode = ((ProductModel)product).getSku(0).getSkuCode();
					}
				} catch (Exception e) {
					LOG.warn("Exception while populating Criteo PDP product's sku code: ", e);
				}
				
				hLBrandProductAdRequest.setProductId(skuCode.toUpperCase());
			}
		} catch (Exception e) {
			LOG.warn("Exception while populating Criteo PDP product: ", e);
		}
	}
	
		public static List<String> getFDSearchPriorityKeyWords() {
			List<String> FDsearchKeys = new ArrayList<String>(  );
			 try {
	             String skuPrefixes = FDStoreProperties.getFDHomeCriteoPriorityKeys();
	             if (skuPrefixes != null && !"".equals(skuPrefixes)) {
	                 StringTokenizer st = new StringTokenizer(skuPrefixes, ","); // split comma-delimited list
	                 String curPrefix = ""; // holds prefix to check against

	                 while (st.hasMoreElements()) {
	                     curPrefix = st.nextToken();
	                     // if prefix matches get product info
	                     FDsearchKeys.add(curPrefix);
	                 }
	             }
	         } catch (Exception ignore) {
	        	 //ignore
	         }
			return FDsearchKeys;
		}
		
	static List<HLBrandProductAdInfo> arrangeHomePageProducts(
			Map<String, HLBrandProductAdResponse> cretioProductsCacheMap,
			FDUserI user, List<ProductData> addProducts) {
		List<String> keys = CriteoProductsUtil.getFDSearchPriorityKeyWords();
		int counter = 0;
		List<HLBrandProductAdInfo> arrangedProductList= new ArrayList<HLBrandProductAdInfo>();
		Map<String, List<HLBrandProductAdInfo>> criteoProdctMap = new HashMap<String, List<HLBrandProductAdInfo>>();
		Set<String> skuList = new HashSet<String>();
		for (String key : keys) {
			final String A_SHOWN = "&ashown=";
			final StringBuffer updatedPageBeacon = new StringBuffer(A_SHOWN);
			List<HLBrandProductAdInfo> criteoList=cretioProductsCacheMap.get(key)!=null?cretioProductsCacheMap.get(key).getSearchProductAd():null;
			List<HLBrandProductAdInfo> availableProductList= new ArrayList<HLBrandProductAdInfo>();
			if (criteoList != null) {
				for (HLBrandProductAdInfo hlBrandProductAdMetaInfo : criteoList) {
					ProductModel productModel = null;
					try {
						if (!skuList.contains(hlBrandProductAdMetaInfo
								.getProductSKU()))
							skuList.add(hlBrandProductAdMetaInfo.getProductSKU());
						else {
							continue;
						}

						productModel = ContentFactory.getInstance().getProduct(hlBrandProductAdMetaInfo.getProductSKU());
					} catch (Exception e) {
						LOG.debug("SKu not found for Hooklogic product: "
								+ hlBrandProductAdMetaInfo.getProductSKU());
					}
					if (null != productModel && !productModel.isUnavailable()) {
						ProductData productData = null;
						try {
							productData = ProductDetailPopulator.createProductData(user, productModel);
							if (null != productData && null != productData.getSkuCode()) {
								availableProductList.add(hlBrandProductAdMetaInfo);
								counter=counter+availableProductList.size();
								updatedPageBeacon.append(((A_SHOWN.equals(updatedPageBeacon.toString())) ? hlBrandProductAdMetaInfo
												.getProductSKU().toUpperCase() : ","+ hlBrandProductAdMetaInfo.getProductSKU().toUpperCase()));
							}
						} catch (Exception e) {
							LOG.debug("SKu not found for Hooklogic product: "
									+ hlBrandProductAdMetaInfo.getProductSKU());
						}
					}
				}
			}
			if(!availableProductList.isEmpty())
				availableProductList.get(0).setPageBeacon(cretioProductsCacheMap.get(key).getPageBeacon()+ updatedPageBeacon.toString());
			criteoProdctMap.put(key, availableProductList);
		}
		arrangeCriteoProducts(keys, counter, arrangedProductList, criteoProdctMap);
	return arrangedProductList;
}

	public static void arrangeCriteoProducts(List<String> keys, int counter,
			List<HLBrandProductAdInfo> arrangedProductList,
			Map<String, List<HLBrandProductAdInfo>> criteoProdctMap) {
		int i=0;
		int j=0;
	if (!keys.isEmpty() && keys.size() != 0 && counter != 0) {
		boolean found = false;
		while (counter >= i++) {
			for (String key : keys) {
				if (null != criteoProdctMap.get(key) && !criteoProdctMap.get(key).isEmpty()
						&& criteoProdctMap.get(key).size() >= (j + 1) ) {
					HLBrandProductAdInfo criteoproduct=criteoProdctMap.get(key).get(j);
					arrangedProductList.add(criteoproduct);
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
	}
					
					
					
					
		/*  page beacon of Api call, we are setting to product level here 
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
		*/

	/*	if(!avaiProductFDList.isEmpty() && avaiProductFDList.size() < 5){
							int i= avaiProductFDList.size();
							int j=0;
							while ( i < 5) {
								avaiProductFDList.add(avaiProductFDList.get(j));
								i++;	
								j++;
							}
						}*/
			
	
		
}
