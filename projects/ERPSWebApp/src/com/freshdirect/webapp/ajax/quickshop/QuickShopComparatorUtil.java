package com.freshdirect.webapp.ajax.quickshop;

import java.util.Comparator;
import java.util.List;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.content.EnumSortingValue;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SortValueComparator;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;

public class QuickShopComparatorUtil {
	
	public static Comparator<FilteringSortingItem<QuickShopLineItemWrapper>> createQuickShopItemComparator(List<FilteringSortingItem<QuickShopLineItemWrapper>> items, PricingContext pricingContext, FilteringNavigator nav) {
		
		QuickShopSortType sortBy = (QuickShopSortType) nav.getSortBy();
		if(sortBy==null){
			sortBy=QuickShopSortType.BY_FREQUENCY;
		}
		boolean ascending = nav.isSortOrderingAscending();
		
		ComparatorChain<FilteringSortingItem<QuickShopLineItemWrapper>> comparator;
		switch (sortBy) {
			case BY_SALE:
				collectSaleInfo(items, pricingContext);
				comparator = ComparatorChain.create(new SortValueComparator<QuickShopLineItemWrapper>(EnumSortingValue.DEAL));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				comparator.chain(FilteringSortingItem.wrap(QuickShopLineItemWrapper.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			case BY_FREQUENCY:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(QuickShopLineItemWrapper.FREQUENCY_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				comparator.chain(FilteringSortingItem.wrap(QuickShopLineItemWrapper.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			case BY_RECENCY:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(QuickShopLineItemWrapper.RECENT_PURCHASE_COMPARATOR_DESC));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				comparator.chain(FilteringSortingItem.wrap(QuickShopLineItemWrapper.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			case BY_EXPERT_RATING:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(QuickShopLineItemWrapper.EXPERT_RATING_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				comparator.chain(FilteringSortingItem.wrap(QuickShopLineItemWrapper.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			case BY_YOUR_FAVORITES:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(QuickShopLineItemWrapper.FAVOURITES_COMPARATOR))
				.chain(FilteringSortingItem.wrap(QuickShopLineItemWrapper.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			case BY_NAME:
			default:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(QuickShopLineItemWrapper.FULL_NAME_PRODUCT_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				break;
		}

		return comparator;
	}
	
	public static void collectSaleInfo(List<FilteringSortingItem<QuickShopLineItemWrapper>> items, PricingContext context) {
		
		for (FilteringSortingItem<QuickShopLineItemWrapper> item : items) {
			
			if(item.getNode().getItem().isAvailable()){
				
				ProductModel p;
				String actualSku = item.getNode().getItem().getSkuCode();
				
				if (context != null){
					p = ProductPricingFactory.getInstance().getPricingAdapter(item.getModel().getProduct(), context);				
				}
				else{
					p = item.getModel().getProduct();				
				}
				
				item.putSortingValue(EnumSortingValue.DEAL, p.getPriceCalculator(actualSku).getHighestDealPercentage());
				
			}else{ //put to the end of the list if unavailable
				item.putSortingValue(EnumSortingValue.DEAL, -1);
			}
		}
	}

}
