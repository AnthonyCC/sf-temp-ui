package com.freshdirect.fdstore.content.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.EnumWineRating;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductRatingGroup;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.smartstore.sorting.PopularityComparator;

public class WineSorter implements Serializable {
	private static final long serialVersionUID = 2075923425552479422L;

	private static enum Criterium {
		BY_RATING, ALPHABETICAL, BY_POPULARITY, BY_PRICE;
	}

	public static enum Type {
		EXPERT_RATING(Criterium.BY_RATING, false),
		ABC(Criterium.ALPHABETICAL, false),
		POPULARITY(Criterium.BY_POPULARITY, false),
		PRICE(Criterium.BY_PRICE, false),
		PRICE_REVERSE(Criterium.BY_PRICE, true);

		private boolean reverse;
		private Criterium criterium;

		Type(Criterium criterium, boolean reverse) {
			this.criterium = criterium;
			this.reverse = reverse;
		}

		public boolean isReverse() {
			return reverse;
		}
		
		protected Criterium getCriteria() {
			return criterium;
		}
	}
	
	private static class RatingComparator implements Comparator<ProductModel> {
	    final boolean reverse;
	    final Comparator<? super ProductModel> chainComparator;
	    
	    public RatingComparator(boolean reverse) {
	        this.reverse = reverse;
	        chainComparator = null;
	    }

	    public RatingComparator(Comparator<? super ProductModel> chainComparator) {
	    	this.reverse = false;
	    	this.chainComparator = chainComparator;
		}

		@Override
		public int compare(ProductModel p1, ProductModel p2) {
			try {
				int result = p1.getProductRatingEnum().getValue() - p2.getProductRatingEnum().getValue();
				if (result == 0 && chainComparator != null)
					result = -chainComparator.compare(p1, p2);
				return reverse ? result : -result;
			} catch (FDResourceException e) {
				throw new FDRuntimeException(e);
			}
		}
	}
	
	private static class PriceComparator implements Comparator<ProductModel> {
	    final boolean reverse;
	    final RatingComparator ratingComparator;
	    
	    public PriceComparator(boolean reverse) {
	        this.reverse = reverse;
	        ratingComparator = new RatingComparator(reverse);
	    }
	    
        public int compare(ProductModel p1, ProductModel p2) {
            int result = Double.compare(p1.getPrice(0.), p2.getPrice(0.));
            if (result == 0) {
            	result = ratingComparator.compare(p1, p2);
            	if (result != 0)
            		return result;
            } else
            	return reverse ? result : -result;
            
            if (result == 0)
            	result = p1.getFullName().compareTo(p2.getFullName());
            return reverse ? -result : result;
        }
    }
	
	private PricingContext pricingContext;
	private Type type;
	private boolean forceNoGrouping;
	private List<ProductRatingGroup> results;
	WineFilter filter;
	Collection<ProductModel> products;
	
	public WineSorter(PricingContext pricingContext, Type type, EnumWineViewType view, WineFilter filter) {
		super();
		this.pricingContext = pricingContext;
		this.type = type;
		this.forceNoGrouping = view == EnumWineViewType.DETAILS;
		this.filter = filter;
		this.products = null;
	}

	public WineSorter(PricingContext pricingContext, Type type, EnumWineViewType view, Collection<ProductModel> products) {
		super();
		this.pricingContext = pricingContext;
		this.type = type;
		this.forceNoGrouping = view == EnumWineViewType.DETAILS;
		this.filter = null;
		this.products = products;
	}
	
	/**
	 * Special case for experts top rated feed
	 * @param pricingContext
	 * @param filter
	 */
	public WineSorter(PricingContext pricingContext, WineFilter filter) {
		super();
		this.pricingContext = pricingContext;
		this.type = null;
		this.forceNoGrouping = true;
		this.filter = filter;
		this.products = null;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isForceNoGrouping() {
		return forceNoGrouping;
	}

	public List<ProductRatingGroup> getResults() {
		if (results != null)
			return results;

		Collection<ProductModel> products = filter != null ? filter.getProducts() : this.products;
		List<ProductRatingGroup> groups = new ArrayList<ProductRatingGroup>(6);
		boolean reverse;
		boolean grouping;
		boolean forceNoGrouping = products.size() <= 6 ? true : this.forceNoGrouping;
		Comparator<? super ProductModel> comparator;
		if (type != null) {
			switch (type) {
				case PRICE_REVERSE:
					reverse = true;
					break;
				default:
					reverse = false;
			}
			switch (type.criterium) {
				case BY_RATING:
					grouping = !forceNoGrouping;
					comparator = new RatingComparator(new PriceComparator(false));
					break;
				case ALPHABETICAL:
					comparator = ContentNodeModel.FULL_NAME_WITH_ID_COMPARATOR;
					grouping = false;
					break;
				case BY_PRICE:
					comparator = new PriceComparator(reverse);
					grouping = false;
					break;
				case BY_POPULARITY:
					comparator = new PopularityComparator(false, products, pricingContext);
					grouping = false;
					break;
				default:
					throw new IllegalStateException("Unknown sorting criterium");
			}
		} else {
			comparator = new RatingComparator(new PopularityComparator(false, products, pricingContext));
			grouping = false;
		}
		if (grouping) {
			Map<EnumWineRating,ProductRatingGroup> map = new HashMap<EnumWineRating, ProductRatingGroup>(6);
			for (ProductModel p : products) {
				try {
					EnumWineRating rating = EnumWineRating.getEnumByRating(p.getProductRatingEnum());
					if (!map.containsKey(rating))
						map.put(rating, new ProductRatingGroup(rating, products.size()));
					
					map.get(rating).getProducts().add(p);
				} catch (FDResourceException e) {
					throw new FDRuntimeException(e);
				}
			}
			
			
			groups.addAll(map.values());
			Collections.sort(groups);
			Collections.reverse(groups);
		} else {
			ProductRatingGroup group = new ProductRatingGroup(EnumWineRating.NOT_RATED, products.size());
			group.getProducts().addAll(products);
			groups.add(group);
		}
		
		for (ProductRatingGroup group : groups) {
			Collections.sort(group.getProducts(), comparator);
		}
		
		return results = groups;
	}
}
