package com.freshdirect.fdstore.content.util;

import java.util.Collections;
import java.util.List;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.storeapi.content.ComparatorChain;
import com.freshdirect.storeapi.content.EnumSortingValue;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SortValueComparator;

public class SmartSearchUtils {
	public static void collectSaleInfo(List<FilteringSortingItem<ProductModel>> products, PricingContext context) {
		for (FilteringSortingItem<ProductModel> product : products) {
			ProductModel p;
			if (context != null)
				p = ProductPricingFactory.getInstance().getPricingAdapter(product.getModel(), context);
			else
				p = product.getModel();
			product.putSortingValue(EnumSortingValue.DEAL, p.getPriceCalculator().getHighestDealPercentage());
		}
	}

	public static void collectAvailabilityInfo(List<FilteringSortingItem<ProductModel>> products, PricingContext context) {
		for (FilteringSortingItem<ProductModel> product : products) {
			ProductModel p;
			if (context != null)
				p = ProductPricingFactory.getInstance().getPricingAdapter(product.getModel(), context);
			else
				p = product.getModel();
			boolean available = p.isFullyAvailable();
			product.putSortingValue(EnumSortingValue.AVAILABILITY, available ? 1 : 0);
		}
	}

	public static void collectOriginalTermInfo(List<FilteringSortingItem<ProductModel>> products, String searchTerm) {
	    // FIXME ContentSearch - restore function
	}

	public static void collectRelevancyCategoryScores(List<FilteringSortingItem<ProductModel>> products, String searchTerm) {
        // FIXME ContentSearch - restore function
	}

	public static void collectTermScores(List<FilteringSortingItem<ProductModel>> products, String searchTerm) {
	    // FIXME ContentSearch - restore function
	}

	public static List<ProductModel> sortBySale(List<ProductModel> products, PricingContext pricingContext, boolean ascending) {
		List<FilteringSortingItem<ProductModel>> items = FilteringSortingItem.wrap(products);
		ComparatorChain<FilteringSortingItem<ProductModel>> comparator = ComparatorChain.create(new SortValueComparator<ProductModel>(EnumSortingValue.DEAL));
		collectSaleInfo(items, pricingContext);
		if (!ascending)
			comparator = ComparatorChain.reverseOrder(comparator);
		comparator.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
		Collections.sort(items, comparator);
		return FilteringSortingItem.unwrap(items);
	}
}
