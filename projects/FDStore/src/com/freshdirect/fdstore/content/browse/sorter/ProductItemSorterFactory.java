package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.content.EnumSortingValue;
import com.freshdirect.fdstore.content.FilteringComparatorUtil;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SortIntValueComparator;
import com.freshdirect.fdstore.content.SortLongValueComparator;
import com.freshdirect.fdstore.content.SortStrategyType;
import com.freshdirect.fdstore.content.SortValueComparator;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.smartstore.sorting.ScriptedContentNodeComparator;

public class ProductItemSorterFactory {

	private static final Comparator<FilteringProductItem> AVAILABILITY_COMPARATOR = new AvailabilityComparator();
	private static final Comparator<FilteringProductItem> NAME_COMPARATOR = new NameComparator();
	private static final Map<SortStrategyType, Comparator<FilteringProductItem>> comparatorMap = new HashMap<SortStrategyType, Comparator<FilteringProductItem>>();
	private static final Map<SortStrategyType, Comparator<FilteringProductItem>> reverseComparatorMap = new HashMap<SortStrategyType, Comparator<FilteringProductItem>>();
	
	static {
		initAvailOnly(SortStrategyType.NAME, 						NAME_COMPARATOR);
		initAvailOnly(SortStrategyType.RECENCY, 					new RecencyComparator());

		initAvailName(SortStrategyType.CUSTOMER_RATING,				new CustomerRatingComparator());
		initAvailName(SortStrategyType.EXPERT_RATING, 				new ExpertRatingComparator());
		initAvailName(SortStrategyType.POPULARITY, 					adapterForProductModel(ScriptedContentNodeComparator.createGlobalComparator(null, null)));
		initAvailName(SortStrategyType.PRICE, 						new PriceComparator());
		initAvailName(SortStrategyType.SALE, 						new SaleComparator());
		initAvailName(SortStrategyType.SUSTAINABILITY_RATING, 		new SustainabilityRatingComparator());
		initAvailName(SortStrategyType.DEPARTMENT, 					new DepartmentComparator());
		initAvailName(SortStrategyType.E_COUPON_POPULARITY, 		adapterForProductModel(FilteringComparatorUtil.COUPON_POPULARITY_COMPARATOR));
		initAvailName(SortStrategyType.E_COUPON_START_DATE, 		adapterForProductModel(FilteringComparatorUtil.COUPON_START_DATE_COMPARATOR));
		initAvailName(SortStrategyType.E_COUPON_EXPIRATION_DATE,	adapterForProductModel(FilteringComparatorUtil.COUPON_EXP_DATE_COMPARATOR));
		initAvailName(SortStrategyType.E_COUPON_PERCENT_DISCOUNT, 	adapterForProductModel(FilteringComparatorUtil.COUPON_PERCENT_OFF_COMPARATOR));
		initAvailName(SortStrategyType.E_COUPON_DOLLAR_DISCOUNT, 	adapterForProductModel(FilteringComparatorUtil.COUPON_DOLLAR_OFF_COMPARATOR));
	}

	private static void initAvailName(SortStrategyType sortStrategy, Comparator<FilteringProductItem> comparator){
		comparatorMap.put(sortStrategy, wrapAvailAndName(comparator));
		reverseComparatorMap.put(sortStrategy, wrapAvailAndName(Collections.reverseOrder(comparator)));
	}
	
	private static void initAvailOnly(SortStrategyType sortStrategy, Comparator<FilteringProductItem> comparator){
		comparatorMap.put(sortStrategy, ComparatorChain.create(AVAILABILITY_COMPARATOR).chain(comparator));
		reverseComparatorMap.put(sortStrategy, ComparatorChain.create(AVAILABILITY_COMPARATOR).chain(Collections.reverseOrder(comparator)));
	}
	
	private static Comparator<FilteringProductItem> wrapAvailAndName(Comparator<FilteringProductItem> comparator){
		return ComparatorChain.create(ProductItemSorterFactory.AVAILABILITY_COMPARATOR).chain(comparator).chain(ProductItemSorterFactory.NAME_COMPARATOR); 
	}

	private static Comparator<FilteringProductItem> adapterForProductModel(final Comparator<ProductModel> comparator){
		return new Comparator<FilteringProductItem>(){
			@Override
			public int compare(FilteringProductItem o1, FilteringProductItem o2) {
				return comparator.compare(o1.getProductModel(), o2.getProductModel());
			}
		};
	}

	private static Comparator<FilteringProductItem> adapterForSearchResult(final Comparator<FilteringSortingItem<ProductModel>> comparator){
		return new Comparator<FilteringProductItem>(){
			@Override
			public int compare(FilteringProductItem o1, FilteringProductItem o2) {
				FilteringSortingItem<ProductModel> searchResult1 = o1.getSearchResult();
				FilteringSortingItem<ProductModel> searchResult2 = o2.getSearchResult();

				if (searchResult1 == null || searchResult2 == null){ //all or none should be a search results
					return 0;
				} else {
					return comparator.compare(searchResult1, searchResult2);
				}
			}
		};
	}

	public static Comparator<FilteringProductItem> createComparator(SortStrategyType sortStrategy, FDUserI user, boolean reverseOrder){
		if (sortStrategy == SortStrategyType.SEARCH_RELEVANCY){
			return createSearchRelevancyComparator(user, reverseOrder);

		} else {
			return reverseOrder ? reverseComparatorMap.get(sortStrategy) : comparatorMap.get(sortStrategy);
		}
	}
	
	private static Comparator<FilteringSortingItem<ProductModel>> getFavoritesComparator(FDUserI user){
		FDIdentity identity = user.getIdentity();
		return FilteringSortingItem.wrap(ScriptedContentNodeComparator.createUserComparator(identity == null ? null : identity.getErpCustomerPK(), user.getPricingContext()));
	}
	
	private static Comparator<FilteringSortingItem<ProductModel>> getGlobalComparator(FDUserI user){
		FDIdentity identity = user.getIdentity();
		return FilteringSortingItem.wrap(ScriptedContentNodeComparator.createGlobalComparator(identity == null ? null : identity.getErpCustomerPK(), user.getPricingContext()));
	}
	
	
	/**based on ProductsFilterImpl.createComparator() and FilteringComparatorUtil.createProductComparator()*/
	private static Comparator<FilteringProductItem> createSearchRelevancyComparator(FDUserI user, boolean reverseOrder){

		ComparatorChain<FilteringSortingItem<ProductModel>> comparator = ComparatorChain.create(new SortValueComparator<ProductModel>(EnumSortingValue.PHRASE));
		if (!FDStoreProperties.isFavouritesTopNumberFilterSwitchedOn()){
			comparator.chain(getFavoritesComparator(user)); //APPDEV-2725
		}
		comparator.chain(new SortIntValueComparator<ProductModel>(EnumSortingValue.ORIGINAL_TERM));
		comparator.chain(new SortValueComparator<ProductModel>(EnumSortingValue.CATEGORY_RELEVANCY));
		comparator.chain(new SortLongValueComparator<ProductModel>(EnumSortingValue.TERM_SCORE));
		comparator.chain(getGlobalComparator(user));
	
		Comparator<FilteringProductItem> searchInner = adapterForSearchResult(comparator);
		return reverseOrder ? wrapAvailAndName(Collections.reverseOrder(searchInner)) : wrapAvailAndName(searchInner);
	}
	
	/** based on FilteringComparatorUtil.reOrganizeFavourites()*/
	public static Comparator<FilteringProductItem> createSearchRelevancyComparatorForFavorites(FDUserI user){
		
		ComparatorChain<FilteringSortingItem<ProductModel>> comparator = ComparatorChain.create(getFavoritesComparator(user));
		comparator.chain(new SortValueComparator<ProductModel>(EnumSortingValue.CATEGORY_RELEVANCY));
		comparator.chain(new SortLongValueComparator<ProductModel>(EnumSortingValue.TERM_SCORE));
		return adapterForSearchResult(comparator); //TODO deal with reverse order?
	}

	
	public static Comparator<FilteringProductItem> createNutritionComparator(ErpNutritionType.Type erpNutritionTypeType){
		return wrapAvailAndName(new NutritionComparator(erpNutritionTypeType));
	}
	
	public static Comparator<FilteringProductItem> createDefaultComparator(){
		return AVAILABILITY_COMPARATOR;
	}
}