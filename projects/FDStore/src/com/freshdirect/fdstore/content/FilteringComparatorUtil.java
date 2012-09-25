package com.freshdirect.fdstore.content;

import java.util.Comparator;
import java.util.List;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.util.SmartSearchUtils;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.fdstore.util.NewProductsGrouping;
import com.freshdirect.smartstore.sorting.ScriptedContentNodeComparator;

public class FilteringComparatorUtil {

	
	public static Comparator<FilteringSortingItem<ProductModel>> createProductComparator(List<FilteringSortingItem<ProductModel>> products,
			String userId, PricingContext pricingContext, String suggestedTerm, FilteringNavigator nav, boolean showGrouped) {
		
		SearchSortType sortBy = nav.getSortBy();
		boolean ascending = nav.isSortOrderingAscending();
		
		ComparatorChain<FilteringSortingItem<ProductModel>> comparator;
		switch (sortBy) {
			case DEFAULT:
				return null;
			case BY_NAME:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				break;
			case BY_PRICE:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(ProductModel.GENERIC_PRICE_COMPARATOR));
				comparator.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				break;
			case BY_POPULARITY:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(ScriptedContentNodeComparator.createGlobalComparator(userId, pricingContext)));
				comparator.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				break;
			case BY_SALE:
				SmartSearchUtils.collectSaleInfo(products, pricingContext);
				comparator = ComparatorChain.create(new SortValueComparator<ProductModel>(EnumSortingValue.DEAL))
						.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				break;
			case BY_RELEVANCY:
				// if there's only one DYM then we display products for that DYM
				// but for those products we have to use the suggested term to produce the following scores
				SmartSearchUtils.collectOriginalTermInfo(products, suggestedTerm);
				SmartSearchUtils.collectRelevancyCategoryScores(products, suggestedTerm);
				SmartSearchUtils.collectTermScores(products, suggestedTerm);
				comparator = ComparatorChain.create(new SortValueComparator<ProductModel>(EnumSortingValue.PHRASE));
				comparator.chain(FilteringSortingItem.wrap(ScriptedContentNodeComparator.createUserComparator(userId, pricingContext)));
				comparator.chain(new SortIntValueComparator<ProductModel>(EnumSortingValue.ORIGINAL_TERM));
				comparator.chain(new SortValueComparator<ProductModel>(EnumSortingValue.CATEGORY_RELEVANCY));
				comparator.chain(new SortLongValueComparator<ProductModel>(EnumSortingValue.TERM_SCORE));
				comparator.chain(FilteringSortingItem.wrap(ScriptedContentNodeComparator.createGlobalComparator(userId, pricingContext)));
				comparator.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				break;
			case BY_RECENCY:
			default:
				if (showGrouped)
					comparator = ComparatorChain.create(new NewProductsGrouping(!nav.isSortOrderingAscending()).getTimeRangeComparator()).chain(FilteringSortingItem.wrap(ProductModel.DEPTFULL_COMPARATOR))
							.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				else
					comparator = ComparatorChain.create(new SortValueComparator<ProductModel>(EnumSortingValue.NEWNESS));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
		}
		SmartSearchUtils.collectAvailabilityInfo(products, pricingContext);
		comparator.prepend(new SortValueComparator<ProductModel>(EnumSortingValue.AVAILABILITY));
		return comparator;
	}
	
	public static Comparator<FilteringSortingItem<Recipe>> RECIPE_SORT_BY_NAME_ASC = new Comparator<FilteringSortingItem<Recipe>>() {
		@Override
		public int compare(FilteringSortingItem<Recipe> arg0, FilteringSortingItem<Recipe> arg1) {
			return arg0.getModel().getName().compareTo(arg1.getModel().getName());
		}
	};
	
	public static Comparator<FilteringSortingItem<Recipe>> RECIPE_SORT_BY_NAME_DESC = new Comparator<FilteringSortingItem<Recipe>>() {
		@Override
		public int compare(FilteringSortingItem<Recipe> arg0, FilteringSortingItem<Recipe> arg1) {
			return arg1.getModel().getName().compareTo(arg0.getModel().getName());
		}
	};
}
