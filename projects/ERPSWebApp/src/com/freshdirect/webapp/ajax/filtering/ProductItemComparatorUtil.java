package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SortStrategyType;
import com.freshdirect.fdstore.content.browse.sorter.ProductItemSorterFactory;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.SectionContext;

public class ProductItemComparatorUtil {
	private static final Logger LOGGER = LoggerFactory.getInstance(ProductItemComparatorUtil.class);

	public static void sortSectionDatas(BrowseDataContext data, Comparator<FilteringProductItem> comparator) {
	
		if (comparator == null) {
			LOGGER.error("No sorter given, leave collections in natural order");
			return;
		}

		for(SectionContext context : data.getSectionContexts()){
			sortSectionContext(context, comparator);
		}
	}
	
	private static void sortSectionContext(SectionContext context, Comparator<FilteringProductItem> comparator){
		List<SectionContext> subSectionContexts = context.getSectionContexts();
		List<FilteringProductItem> productItems = context.getProductItems();

		if (subSectionContexts==null || subSectionContexts.size()==0){
			if(productItems!=null){
				Collections.sort(productItems, comparator);
			}
		} else {
			for (SectionContext subSectionContext : subSectionContexts){
				sortSectionContext(subSectionContext, comparator);
			}
		}
	}
	
	/** based on FilteringComparatorUtil.reOrganizeFavourites()*/
	public static void postProcess(BrowseDataContext data, SortStrategyType sortStrategy, FDUserI user){
		if (sortStrategy == SortStrategyType.SEARCH_RELEVANCY && FDStoreProperties.isFavouritesTopNumberFilterSwitchedOn() && data.getSectionContexts().size() > 0) { //APPDEV-2725
				
			FDIdentity identity = user.getIdentity();
			String userId = identity == null ? null : identity.getErpCustomerPK();
			List<FilteringProductItem> allProductItems = data.getSectionContexts().get(0).getProductItems();
			
			//collect available favorites
			List<FilteringProductItem> favourites = new ArrayList<FilteringProductItem>();
			for (FilteringProductItem productItem : allProductItems){
				ProductModel productModel = productItem.getProductModel();  
				
				if (ScoreProvider.getInstance().isUserHasScore(userId, productModel.getContentKey()) && productModel.isFullyAvailable()) {
					favourites.add(productItem);
				}
			}
			
			Collections.sort(favourites, ProductItemSorterFactory.createSearchRelevancyComparatorForFavorites(user)); //sort favorites
			List<FilteringProductItem> favouritesToMove = favourites.subList(0, Math.min(FDStoreProperties.getSearchPageTopFavouritesNumber(), favourites.size())); //get top favorites that will be moved
			allProductItems.removeAll(favouritesToMove); //remove top favorites from original place
			
			for (int index = favouritesToMove.size() - 1; index >= 0 ; index --) { //place top favorites to the beginning of the products
				allProductItems.add(0,favourites.get(index));
			}
		}
	}
}
