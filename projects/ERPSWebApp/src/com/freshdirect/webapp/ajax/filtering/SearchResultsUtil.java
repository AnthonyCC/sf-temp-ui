package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.util.ProductPromotionUtil;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ProductModelPromotionAdapter;
import com.freshdirect.fdstore.brandads.FDBrandProductsAdManager;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentKeyFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentUtil;
import com.freshdirect.fdstore.content.EnumSortingValue;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductModelBrandAdsAdapter;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy;

public class SearchResultsUtil {
	
	private static final Logger LOG = LoggerFactory.getInstance( SearchResults.class );
	
	private static final String MOBILE_PLATFORM="mobile";
	private static final String WEB_PLATFORM="web";
	
	public static SearchResults getPresidentsPicksProducts(CmsFilteringNavigator nav) {
		
		List<ProductModel> promotionProducts = new ArrayList<ProductModel>();
		CategoryModel category = (CategoryModel)ContentFactory.getInstance().getContentNode(nav.getId());
		
		boolean isPpPreview = (category==null ||null == category.getProductPromotionType() || null == nav.getPpPreviewId()) ? false : true;
	    if(category!=null) {
			if(!isPpPreview){
				promotionProducts = category.getProducts();
			}else{
				promotionProducts = category.getPromotionPageProductsForPreview(nav.getPpPreviewId());
			}
	    }
		
		List<ProductModel> featProds = ProductPromotionUtil.getFeaturedProducts(promotionProducts,isPpPreview);
		
		List<ProductModel> nonfeatProds = new ArrayList<ProductModel>();
		
		nonfeatProds = ProductPromotionUtil.getNonFeaturedProducts(promotionProducts,isPpPreview);
		List<FilteringSortingItem<ProductModel>> searchProductResults = new ArrayList<FilteringSortingItem<ProductModel>>();
		if(null !=nonfeatProds){
			for (ProductModel productModel : nonfeatProds) {
				FilteringSortingItem<ProductModel> item = new FilteringSortingItem<ProductModel>(productModel);
				item.putSortingValue(EnumSortingValue.PHRASE, 1);
				searchProductResults.add(item);
			}
		}
		
		SearchResults searchResults = new SearchResults(searchProductResults, Collections.<FilteringSortingItem<Recipe>> emptyList(), Collections.<FilteringSortingItem<CategoryModel>> emptyList(), "", true);
		searchResults.setDDPPProducts(featProds);
		
		return searchResults;
	}
	
	public static SearchResults getStaffPicksProducts(CmsFilteringNavigator nav) {
		/* close to the same as PP */
		/* added some mods, don't over-write */
		
		List<ProductModel> promotionProducts = new ArrayList<ProductModel>();
		CategoryModel category = (CategoryModel)ContentFactory.getInstance().getContentNode(nav.getId());
		
	//	String picksId = nav.getPicksId();
		boolean isPpPreview = (category==null ||null == category.getProductPromotionType() || null == nav.getPpPreviewId()) ? false : true;
	    if(category!=null) {
			if(!isPpPreview){
				/* this needs to come from URL */
				
				//Commenting this code as part of APPDEV-5988 to allow for a dynamic picks id rather than fetching based of FDStoreProperties
			/*	if(picksId==null || picksId.equals("")){
			     	   nav.setPicksId(FDStoreProperties.getStaffPicksPickId());
			        }
				promotionProducts = category.getAssortmentPromotionPageProducts(nav.getPicksId());*/
				promotionProducts = category.getProducts();
			}else{
				promotionProducts = category.getPromotionPageProductsForPreview(nav.getPpPreviewId());
			}
	    }
	    
	    //for staffpicks
		List<ProductModel> featProds = ProductPromotionUtil.getFeaturedProducts(promotionProducts, isPpPreview, FDStoreProperties.getStaffPicksPageFeatLimit()); 
		
		List<ProductModel> nonfeatProds = new ArrayList<ProductModel>();
		
		nonfeatProds = ProductPromotionUtil.getNonFeaturedProducts(promotionProducts,isPpPreview);
		List<FilteringSortingItem<ProductModel>> searchProductResults = new ArrayList<FilteringSortingItem<ProductModel>>();
		if(null !=nonfeatProds){
			for (ProductModel productModel : nonfeatProds) {
				FilteringSortingItem<ProductModel> item = new FilteringSortingItem<ProductModel>(productModel);
				item.putSortingValue(EnumSortingValue.PHRASE, 1);
				searchProductResults.add(item);
			}
		}
		
		SearchResults searchResults = new SearchResults(searchProductResults, Collections.<FilteringSortingItem<Recipe>> emptyList(), Collections.<FilteringSortingItem<CategoryModel>> emptyList(), "", true);
		searchResults.setDDPPProducts(featProds);
		Map<String,List<ProductModel>> assortProductMap=new HashMap<String, List<ProductModel>>();
		assortProductMap.put("ASSORT_PRODUCTS", nonfeatProds);
		searchResults.setAssortProducts(assortProductMap);
		
		return searchResults;
	}
	
	
public static SearchResults getHLBrandProductAdProducts(SearchResults searchResults, CmsFilteringNavigator nav, FDSessionUser user) {
		

		List<ProductModel> adPrducts = new ArrayList<ProductModel>();
		HLBrandProductAdRequest hLBrandProductAdRequest=new HLBrandProductAdRequest();
		
				
		try {
			if(user.getUser()!=null && user.getUser().getPK()!=null &&
					user.getUser().getPK().getId()!=null) {
				hLBrandProductAdRequest.setUserId(user.getUser().getPK().getId());	
			 hLBrandProductAdRequest.setSearchKeyWord(searchResults.getSuggestedTerm()!=null?searchResults.getSuggestedTerm():nav.getSearchParams());
					
			if (user.getPlatForm() != null) {
				hLBrandProductAdRequest.setPlatformSource(user.getPlatForm());
				hLBrandProductAdRequest.setLat(user.getLat());
				hLBrandProductAdRequest.setPdUserId(user.getPdUserId());
			} else {
				hLBrandProductAdRequest.setPlatformSource(user.isMobilePlatForm() ? MOBILE_PLATFORM : WEB_PLATFORM);
			}

			HLBrandProductAdResponse hlBrandProductAdResponse = FDBrandProductsAdManager.getHLBrandproducts(hLBrandProductAdRequest);
			if(hlBrandProductAdResponse!=null){
			List<HLBrandProductAdInfo> hlBrandAdProductsMeta =hlBrandProductAdResponse.getSearchProductAd();
			
			if(null !=hlBrandAdProductsMeta){
				searchResults.setHlProductsCount(hlBrandAdProductsMeta.size());
				for (Iterator<HLBrandProductAdInfo> iterator = hlBrandAdProductsMeta.iterator(); iterator.hasNext();) {
					HLBrandProductAdInfo hlBrandProductAdMetaInfo = (HLBrandProductAdInfo) iterator.next();
					hlBrandProductAdMetaInfo.setPageBeacon(hlBrandProductAdResponse.getPageBeacon());
					searchResults.setPageBeacon(hlBrandProductAdResponse.getPageBeacon());
					
					try {
						ProductModel productModel = ContentFactory.getInstance().getProduct(hlBrandProductAdMetaInfo.getProductSKU());
						
						if(null !=productModel){
							ProductModelBrandAdsAdapter pm = new ProductModelBrandAdsAdapter(productModel, hlBrandProductAdMetaInfo.getClickBeacon(), hlBrandProductAdMetaInfo.getImpBeacon());
								adPrducts.add(pm);
						}
					} catch (FDSkuNotFoundException e) {
						LOG.info("FDSkuNotFoundException while populating HookLogicproduct : ", e);
					}
				}
			}else {
				searchResults.setEmptyProductsPageBeacon(hlBrandProductAdResponse.getPageBeacon());
			}
			
		  }
	     }
	    }
      catch (Exception e) {
			LOG.info("Exception while populating HookLogicproduct: ", e);
		}
		List<FilteringSortingItem<ProductModel>> searchProductResults = new ArrayList<FilteringSortingItem<ProductModel>>();
		if(null !=adPrducts){
			for (ProductModel productModel : adPrducts) {
				FilteringSortingItem<ProductModel> item = new FilteringSortingItem<ProductModel>(productModel);
				searchProductResults.add(item);
			}
		}
		searchResults.setAdProducts(searchProductResults);
		
		return searchResults;
	}
	
	
	public static SearchResults getProductsWithCoupons(FDSessionUser user) {
		
		return FDCustomerCouponUtil.getCouponsAsSearchResults(user, false);
		
	}
	
	public static SearchResults getNewProducts(CmsFilteringNavigator nav, FDSessionUser user) {
		
		//legacy code from newproducts...
		
		Date now = new Date();
		List<FilteringSortingItem<ProductModel>> items = new ArrayList<FilteringSortingItem<ProductModel>>();
		Map<ProductModel, Map<String,Date>> newProducts = ContentFactory.getInstance().getNewProducts();
		ZoneInfo zone=user.getUserContext().getPricingContext().getZoneInfo();
		
		String productNewnessKey="";
		if(zone!=null) {
			
			productNewnessKey=new StringBuilder(5).append(zone.getSalesOrg()).append(zone.getDistributionChanel()).toString();
		}
		for (Entry<ProductModel, Map<String,Date>> entry : newProducts.entrySet()) {
			Map<String,Date> newProductsBySalesArea=entry.getValue();
			if(newProductsBySalesArea.containsKey(productNewnessKey))
				items.add(new FilteringSortingItem<ProductModel>(entry.getKey()).putSortingValue(EnumSortingValue.NEWNESS, DateUtil.diffInDays(now, entry.getValue().get(productNewnessKey))));
		}
		
		newProducts = ContentFactory.getInstance().getBackInStockProducts();
		
		for (Entry<ProductModel, Map<String,Date>> entry : newProducts.entrySet()) {
			Map<String,Date> newProductsBySalesArea=entry.getValue();
			if(newProductsBySalesArea.containsKey(productNewnessKey))
			items.add(new FilteringSortingItem<ProductModel>(entry.getKey()).putSortingValue(EnumSortingValue.NEWNESS, DateUtil.diffInDays(now, entry.getValue().get(productNewnessKey))));
		}
		
		CategoryModel featuredCategory = null;
		try {
			// lookup category for featured new products and brands
			featuredCategory = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey( ContentKeyFactory.getIntance().getNewProductsCategoryKey() );
		} catch (InvalidContentKeyException exc) {}
		List<String> categoryFilters = nav.getRequestFilterParams().get("categoryFilterGroup");
		if (categoryFilters != null && !categoryFilters.contains("clearall")) {
			if (categoryFilters.size() > 0) { //coming from original...
				CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", categoryFilters.get(0));
				if (category == null || category.getCategoryLabel() != null) {
					featuredCategory = category;
				}
			} else {
				featuredCategory = null;
			}
		}

		List<ProductModel> ddppProducts = new ArrayList<ProductModel>();
		
		if (featuredCategory != null && featuredCategory.getCategoryLabel() != null && featuredCategory.getFeaturedNewProdBrands().size() > 0) {
			
			final int itemsMinToShow = 1;
			final int itemsMaxToShow = 4;
			
			List<ContentNodeModel> pList = featuredCategory.getFeaturedNewProdBrands();
			Iterator<ContentNodeModel> it=pList.iterator();
			while (it.hasNext()){
				ContentNodeModel node = it.next();
				if ( node == null || ! (node instanceof ProductModel) ) {
					it.remove();
				} else {
					ProductModel pm = (ProductModel)node;
					//Origin : [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
					if(pm.getPriceCalculator().getSkuModel()==null || !ContentUtil.isAvailableByContext(pm)){
						it.remove();
					}
				}
			}
			
			int itemsToShow = Math.min(itemsMaxToShow,pList.size());
			
			if (pList.size()>=itemsMinToShow) {

				ConfigurationContext confContext = new ConfigurationContext();
				confContext.setFDUser(user);
				ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();

				for (ContentNodeModel contentNode : pList.subList(0, itemsToShow)) {
					ProductImpression pi = null;
					ProductModelPromotionAdapter productModelAdapter = null;
					if ( contentNode instanceof ProductModel ) {
						ProductModel pricedProdModel = new ProductModelPricingAdapter( (ProductModel)contentNode );
						pi = confStrat.configure(pricedProdModel, confContext);
						// TODO: "013" is the 'new' ribbon, hardcoded here
						ddppProducts.add(new ProductModelPromotionAdapter (pi.getProductModel(), true, "013", pi.getSku().getSkuCode()));			
					}	
				}
				
			}
		}
		
		SearchResults searchResults = new SearchResults(items, FilteringSortingItem.<Recipe> emptyList(), FilteringSortingItem.<CategoryModel> emptyList(), null, false);
		searchResults.setDDPPProducts(ddppProducts);
		return searchResults;
		//return null;

	}
}
