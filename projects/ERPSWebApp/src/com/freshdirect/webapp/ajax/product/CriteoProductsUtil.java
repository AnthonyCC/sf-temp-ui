package com.freshdirect.webapp.ajax.product;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.brandads.FDBrandProductsAdManager;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.filtering.SearchResultsUtil;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleData;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class CriteoProductsUtil
{
    private static final Logger LOG = LoggerFactory.getInstance(CriteoProductsUtil.class);

    private static final String A_SHOWN="&ashown=";

	public static void addFeatureProductsToHomePage(FDUserI user, ModuleData moduleData) {
		HLBrandProductAdRequest hLBrandProductAdRequest = new HLBrandProductAdRequest();
		List<ProductData> adPrducts = new ArrayList<ProductData>();
		StringBuffer updatedPageBeacon = new StringBuffer(A_SHOWN);
		try {
			SearchResultsUtil.setPlatFormValues((FDSessionUser) user, hLBrandProductAdRequest);
			HLBrandProductAdResponse response = FDBrandProductsAdManager.getHLadproductToHome(hLBrandProductAdRequest);
			if (response != null) {
				List<HLBrandProductAdInfo> hlBrandAdProductsMeta = response.getProductAd();
				if (hlBrandAdProductsMeta != null)
				addFeatureProducts(user, adPrducts, updatedPageBeacon, hlBrandAdProductsMeta);
			}
			moduleData.setAdProducts(adPrducts);
			moduleData.setAdHomePageBeacon(updatedPageBeacon != null ? updatedPageBeacon.toString(): null);
		} catch (Exception e) {
			LOG.warn("Exception while populating Criteo returned product: ", e);
		}
	}

	private static void populateFeaturePrdData(List<ProductData> adPrducts, StringBuffer updatedPageBeacon,
			HLBrandProductAdInfo hlBrandProductAdMetaInfo, ProductData productData) {
		productData.setFeatured(true);
		productData.setClickBeacon(hlBrandProductAdMetaInfo.getClickBeacon());
		productData.setImageBeacon(hlBrandProductAdMetaInfo.getImpBeacon());
		adPrducts.add(productData);
		updatedPageBeacon.append(((A_SHOWN.equals(updatedPageBeacon.toString()))
				? productData.getSkuCode() : "," + productData.getSkuCode()));
	}


	public static void setPdpProduct(FDUserI user, String productId, ProductPotatoData productPotatoData) {
		HLBrandProductAdRequest hLBrandProductAdRequest = new HLBrandProductAdRequest();
		List<ProductData> adPrducts = new ArrayList<ProductData>();
		StringBuffer updatedPageBeacon = new StringBuffer(A_SHOWN);
		try {
			SearchResultsUtil.setPlatFormValues((FDSessionUser) user, hLBrandProductAdRequest);
			HLBrandProductAdResponse response = FDBrandProductsAdManager.getHLadproductToPdp(hLBrandProductAdRequest);
			if (response != null) {
				List<HLBrandProductAdInfo> hlBrandAdProductsMeta = response.getProductAd();
				if (hlBrandAdProductsMeta != null)
				addFeatureProducts(user, adPrducts, updatedPageBeacon, hlBrandAdProductsMeta);
			}
			//productPotatoData.setAdProducts(adPrducts);
			//productPotatoData.setAdPdpPageBeacon(updatedPageBeacon != null ? updatedPageBeacon.toString(): null);
		} catch (Exception e) {
			LOG.warn("Exception while populating Criteo returned product: ", e);
		}
	}

	private static void addFeatureProducts(FDUserI user, List<ProductData> adPrducts, StringBuffer updatedPageBeacon,
			List<HLBrandProductAdInfo> hlBrandAdProductsMeta) throws FDSkuNotFoundException {
		for (Iterator<HLBrandProductAdInfo> iterator = hlBrandAdProductsMeta.iterator(); iterator.hasNext();) {
			HLBrandProductAdInfo hlBrandProductAdMetaInfo = iterator.next();
			ProductModel productModel = ContentFactory.getInstance()
					.getProduct(hlBrandProductAdMetaInfo.getProductSKU());

			if (null != productModel && !productModel.isUnavailable()) {
				ProductData productData = null;
				try {
					productData = ProductDetailPopulator.createProductData(user, productModel);
					if (null != productData && null != productData.getSkuCode()) {
						populateFeaturePrdData(adPrducts, updatedPageBeacon, hlBrandProductAdMetaInfo, productData);
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

}
