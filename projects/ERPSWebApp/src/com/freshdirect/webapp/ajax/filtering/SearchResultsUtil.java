package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.freshdirect.cms.util.ProductPromotionUtil;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ProductModelPromotionAdapter;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentUtil;
import com.freshdirect.fdstore.content.EnumSortingValue;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy;

public class SearchResultsUtil {

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
	
	public static SearchResults getProductsWithCoupons(FDSessionUser user) {
		
		return FDCustomerCouponUtil.getCouponsAsSearchResults(user, false);
		
	}
	
	public static SearchResults getNewProducts(CmsFilteringNavigator nav, FDSessionUser user) {
		
		//legacy code from newproducts...
		
		Date now = new Date();
		List<FilteringSortingItem<ProductModel>> items = new ArrayList<FilteringSortingItem<ProductModel>>();
		Map<ProductModel, Date> newProducts = ContentFactory.getInstance().getNewProducts();
		
		for (Entry<ProductModel, Date> entry : newProducts.entrySet()) {
			items.add(new FilteringSortingItem<ProductModel>(entry.getKey()).putSortingValue(EnumSortingValue.NEWNESS, DateUtil.diffInDays(now, entry.getValue())));
		}
		
		newProducts = ContentFactory.getInstance().getBackInStockProducts();
		
		for (Entry<ProductModel, Date> entry : newProducts.entrySet()) {
			items.add(new FilteringSortingItem<ProductModel>(entry.getKey()).putSortingValue(EnumSortingValue.NEWNESS, DateUtil.diffInDays(now, entry.getValue())));
		}
		
		CategoryModel featuredCategory = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", FDStoreProperties.getNewProductsCatId());
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

	}
}
