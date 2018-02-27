package com.freshdirect.webapp.ajax.product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
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
		int productsCount = 0;
		try {
			FDSessionUser sessionUser = (FDSessionUser) user;
			SearchResultsUtil.setPlatFormValues(user, hLBrandProductAdRequest, sessionUser.isMobilePlatForm(),
					sessionUser.getPlatForm(), sessionUser.getLat(), sessionUser.getPdUserId());

			if (hLBrandProductAdRequest.getUserId()!= null) {
				HLBrandProductAdResponse response = FDBrandProductsAdManager.getHLadproductToHome(hLBrandProductAdRequest);
				if (response != null && null !=response.getProductAd()) {
					List<HLBrandProductAdInfo> hlBrandAdProductsMeta = response.getProductAd();
					productsCount = hlBrandAdProductsMeta.size();
					if (hlBrandAdProductsMeta != null)
						addHlBrandProducts(user, adPrducts, updatedPageBeacon, hlBrandAdProductsMeta,false);
					moduleData.setAdProducts(adPrducts);
					if (productsCount == adPrducts.size()) {
						moduleData.setAdHomePageBeacon(response.getPageBeacon()	+ A_SHOWN_ALL);
					} else if (productsCount > 0 && adPrducts.size() == 0) {
						moduleData.setAdHomePageBeacon(response.getPageBeacon()	+ A_SHOWN_NONE);
					} else {
						moduleData.setAdHomePageBeacon(response.getPageBeacon()	+ updatedPageBeacon.toString());
					}
				}
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
		/*productData.setImageBeacon("//uat-beam.hlserve.com/beacon?fid=258&hl_qs=JPrhL9T5mOV3wMt8nUd3R1%2fnaUJD4%2b%2fQjx7QrFzy8Xgh1DPl" +
		"Fiu6X3mUcwYc49GV9BIQ55Dq52qgUXByfVYHAkBakkF2RZ32A%2bCtBq87FY05gZbkiyAb7vbHZmOPKN7ZXhkH6KgZW%2fO33Fr7DUD2RROiEUpVZNzs" +
		"jNijiqy1PKGEchrGwEJOazg1k9bLZX7nZyNn9ORUpN8lNfiUQ%2fxv%2bRF3RSxz%2fS8EHy8DeTbkUaOkn4MFxz%2fwjJ%2fnnYEz24hnAB%2bZEw2QZ9yKMDbCWEL" +
		"bPfTpTnZgj0BOVNrQfPiesoffrTBTB%2btWafwZWB666oQYov%2bZSfRKuZht38TE3FCf3QxkkoYpEQE6bJ2PjGT75kHXG7whvjEzRnAiXAR03lfUzd%2b0KWP7NauuC1JTndp1q" +
		"fjDMdryMd27TE2eQf%2bUKTr8l1WGZhdVh0NzimFoDBsF%2bVoDFtVv7UpxFKBECQuoVYuD6PhM7ms49%2bcU6i5FL9LF3%2fG6iMeWcpLJs%2blZYCwKgrhA91Yu0oMU4xun719u" +
		"yHKo9StgQxtuj9mWgXCDq5V%2bLLl9ERS6HMF7zqSot8vCjfHLh6jP4XDf1LpuyPHz5iaPlVS2fcTLxZiKY5fWZmEg2p2mx1IOHMzoejQegL87gcnXDGSUbJ9FfKPcyMQ97%2bPK%2b1QBDd" +
		"8MQzVsKAtK9%2fefxMY%2f0WiG7HoCtEEQMitz0Jp5k%2bvSfOQTcT9syxcGbv2y411F1DNafBBuNT9T1G7i1482jNmBLwIOwt9yKFV0ky9zBRHNcFD52TnvrB0HzbuOUtvdCom85K16159%2b2Qpm" +
		"viQvhEWK%2fYlgD3TB%2beZcOJzq0%2blnxcMaq54lYGhRxdvWwkYYOR5tOrW9vF01auVQf9Pfmv%2b3Qx7rQV4OYHYqvbEoObrAcfCWZKPDhnldhcyxcQrKG19nSciYPzE0asVRwrwdiDTxCuuwSkFO8a" +
		"h4HueKQ95EJfa0mUPIkwDevYttm1yZsPJlZ1XD8wz1vB1ATmMgwHcONkQ%2bTlVF2twkBnb5p7O%2fp61usnxyCdxwN2fPKNrH9NAenwMKrjvWngtb2YWDmZ%2bGp6N%2fZ9ShP6v&ev=1&action=imp&p" +
		"ageguid=867097b1-2968-4fa2-b0cf-f4b76c797614&pid=d82fd515-8f6c-4646-8a4c-66a69c1fbd49&rn=228163497&hmguid=543386f2-8556-4787-becd-f149f84e4e04");*/
		adPrducts.add(productData);
		updatedPageBeacon.append(((A_SHOWN.equals(updatedPageBeacon.toString()))
				? productData.getSkuCode() : "," + productData.getSkuCode()));
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
				
				hLBrandProductAdRequest.setProductId(skuCode);
			}
		} catch (Exception e) {
			LOG.warn("Exception while populating Criteo PDP product: ", e);
		}
	}


}
