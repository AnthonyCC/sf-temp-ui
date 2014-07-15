package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Collections;
import java.util.Comparator;
import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.SortStrategyType;

public class ProductItemSorterFactory {
	
	private static final Comparator<FilteringProductItem> NAME_INNER = new NameComparator();
	public static final Comparator<FilteringProductItem> AVAILABILITY_INNER = new AvailabilityComparator();

	private static final Comparator<FilteringProductItem> NAME = ComparatorChain.create(AVAILABILITY_INNER).chain(NAME_INNER);
	private static final Comparator<FilteringProductItem> NAME_REVERSE = ComparatorChain.create(AVAILABILITY_INNER).chain(Collections.reverseOrder(NAME_INNER));

	private static final Comparator<FilteringProductItem> CR_INNER = new CustomerRatingComparator();
	private static final Comparator<FilteringProductItem> CR = ComparatorChain.create(AVAILABILITY_INNER).chain(CR_INNER).chain(NAME_INNER);
	private static final Comparator<FilteringProductItem> CR_REVERSE = ComparatorChain.create(AVAILABILITY_INNER).chain(Collections.reverseOrder(CR_INNER)).chain(NAME_INNER);

	private static final Comparator<FilteringProductItem> ER_INNER = new ExpertRatingComparator();
	private static final Comparator<FilteringProductItem> ER = ComparatorChain.create(AVAILABILITY_INNER).chain(ER_INNER).chain(NAME_INNER);
	private static final Comparator<FilteringProductItem> ER_REVERSE = ComparatorChain.create(AVAILABILITY_INNER).chain(Collections.reverseOrder(ER_INNER)).chain(NAME_INNER);
		
	private static final Comparator<FilteringProductItem> POPULARITY_INNER = new PopularityComparator();
	private static final Comparator<FilteringProductItem> POPULARITY = ComparatorChain.create(AVAILABILITY_INNER).chain(POPULARITY_INNER).chain(NAME_INNER);
	private static final Comparator<FilteringProductItem> POPULARITY_REVERSE = ComparatorChain.create(AVAILABILITY_INNER).chain(Collections.reverseOrder(POPULARITY_INNER)).chain(NAME_INNER);
	
	private static final Comparator<FilteringProductItem> PRICE_INNER = new PriceComparator(); 
	private static final Comparator<FilteringProductItem> PRICE = ComparatorChain.create(AVAILABILITY_INNER).chain(PRICE_INNER).chain(NAME_INNER); 
	private static final Comparator<FilteringProductItem> PRICE_REVERSE = ComparatorChain.create(AVAILABILITY_INNER).chain(Collections.reverseOrder(PRICE_INNER)).chain(NAME_INNER); 

	private static final Comparator<FilteringProductItem> SALE_INNER = new SaleComparator();
	private static final Comparator<FilteringProductItem> SALE = ComparatorChain.create(AVAILABILITY_INNER).chain(SALE_INNER).chain(NAME_INNER);
	private static final Comparator<FilteringProductItem> SALE_REVERSE = ComparatorChain.create(AVAILABILITY_INNER).chain(Collections.reverseOrder(SALE_INNER)).chain(NAME_INNER); 
	
	private static final Comparator<FilteringProductItem> SR_INNER = new SustainabilityRatingComparator();
	private static final Comparator<FilteringProductItem> SR = ComparatorChain.create(AVAILABILITY_INNER).chain(SR_INNER).chain(NAME_INNER);
	private static final Comparator<FilteringProductItem> SR_REVERSE = ComparatorChain.create(AVAILABILITY_INNER).chain(Collections.reverseOrder(SR_INNER)).chain(NAME_INNER); 

	public static Comparator<FilteringProductItem> createComparator(SortStrategyType sortStrategy, boolean reverseOrder){
		switch (sortStrategy){
			case CUSTOMER_RATING:
				if (reverseOrder){
					return CR_REVERSE;
				} else {
					return CR;
				}
	
			case EXPERT_RATING:
				if (reverseOrder){
					return ER_REVERSE;
				} else {
					return ER;
				}
			
			case NAME:
				if (reverseOrder){
					return NAME_REVERSE;
				} else {
					return NAME;
				}
	
			case POPULARITY:
				if (reverseOrder){
					return POPULARITY_REVERSE;
				} else {
					return POPULARITY;
				}
			
			case PRICE:
				if (reverseOrder){
					return PRICE_REVERSE;
				} else {
					return PRICE;
				}
			
			case SALE:
				if (reverseOrder){
					return SALE_REVERSE;
				} else {
					return SALE;
				}
			
			case SUSTAINABILITY_RATING:
				if (reverseOrder){
					return SR_REVERSE;
				} else {
					return SR;
				}
		}
		return null;
	}
	
	
	public static Comparator<FilteringProductItem> wrapComparator(Comparator<FilteringProductItem> comparator){
		return ComparatorChain.create(ProductItemSorterFactory.AVAILABILITY_INNER).chain(comparator).chain(ProductItemSorterFactory.NAME_INNER); 
	}
}