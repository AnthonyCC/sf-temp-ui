package com.freshdirect.webapp.ajax.quickshop;

import java.util.Comparator;
import java.util.List;

import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.storeapi.content.ComparatorChain;
import com.freshdirect.storeapi.content.EnumSortingValue;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SortValueComparator;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;

public class QuickShopComparatorUtil {
	
    public static Comparator<FilteringSortingItem<QuickShopLineItemWrapper>> createQuickShopItemComparator(List<FilteringSortingItem<QuickShopLineItemWrapper>> items,
            FilteringNavigator nav) {
		
		QuickShopSortType sortBy = (QuickShopSortType) nav.getSortBy();
		if(sortBy==null){
			sortBy=QuickShopSortType.BY_FREQUENCY;
		}
		boolean ascending = nav.isSortOrderingAscending();
		
		ComparatorChain<FilteringSortingItem<QuickShopLineItemWrapper>> comparator;
		switch (sortBy) {
			case BY_SALE:
                collectSaleInfo(items);
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
	
    public static void collectSaleInfo(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		for (FilteringSortingItem<QuickShopLineItemWrapper> item : items) {
			if(item.getNode().getItem().isAvailable()){
				String actualSku = item.getNode().getItem().getSkuCode();
                ProductModel p = ProductPricingFactory.getInstance().getPricingAdapter(item.getModel().getProduct());
				item.putSortingValue(EnumSortingValue.DEAL, p.getPriceCalculator(actualSku).getHighestDealPercentage());
			}else{ //put to the end of the list if unavailable
				item.putSortingValue(EnumSortingValue.DEAL, -1);
			}
		}
	}

}
