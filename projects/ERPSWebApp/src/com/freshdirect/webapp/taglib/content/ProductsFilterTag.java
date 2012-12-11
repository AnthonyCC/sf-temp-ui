package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.EnumSortingValue;
import com.freshdirect.fdstore.content.FilteringComparatorUtil;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.ProductFilterMenuDecorator;
import com.freshdirect.fdstore.content.ProductFilterValueDecorator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.content.SortIntValueComparator;
import com.freshdirect.fdstore.content.SortLongValueComparator;
import com.freshdirect.fdstore.content.SortValueComparator;
import com.freshdirect.fdstore.content.util.SmartSearchUtils;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.sorting.ScriptedContentNodeComparator;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ProductsFilterTag extends FilteringFlow<ProductModel> {
	
	private static Category LOGGER = LoggerFactory.getInstance(ProductsFilterTag.class);

	private static final long serialVersionUID = -3101346359422968490L;
	
	private SearchResults results;
	private Set<EnumFilteringValue> filters;
	{
		filters=new HashSet<EnumFilteringValue>();
		filters.add(EnumFilteringValue.DEPT);
		filters.add(EnumFilteringValue.CAT);
		filters.add(EnumFilteringValue.SUBCAT);
		filters.add(EnumFilteringValue.BRAND);
		filters.add(EnumFilteringValue.EXPERT_RATING);
		filters.add(EnumFilteringValue.CUSTOMER_RATING);
		filters.add(EnumFilteringValue.GLUTEN_FREE);
		filters.add(EnumFilteringValue.KOSHER);
		filters.add(EnumFilteringValue.NEW_OR_BACK);
		filters.add(EnumFilteringValue.ON_SALE);
	}

	private boolean mocked = false;
	private String mockCustomerId;
	private FDUserI user;
	
	protected List<ProductModel> products;

	protected GenericFilterDecorator<FilteringSortingItem<ProductModel>> createFilterValueDecorator() {
		return new ProductFilterValueDecorator(filters);
	}

	@Override
	protected GenericFilterDecorator<FilteringSortingItem<ProductModel>> createMenuValueDecorator() {
		return new ProductFilterMenuDecorator(filters);
	}
	
	@Override
	protected List<FilteringSortingItem<ProductModel>> getItems() {
		return new ArrayList<FilteringSortingItem<ProductModel>>(results.getProducts());
	}

	@Override
	protected Set<EnumFilteringValue> getFilterEnums() {
		return this.filters;
	}
	
	@Override
	protected Comparator<FilteringSortingItem<ProductModel>> createComparator(List<FilteringSortingItem<ProductModel>> products) {
		String suggestedTerm = results.getSuggestedTerm();
		if (suggestedTerm == null)
			suggestedTerm = results.getSearchTerm();
		
		return FilteringComparatorUtil.createProductComparator(getItems(), getUserId(), getPricingContext(), suggestedTerm, nav, isShowGrouped());
	}
	
	protected List<FilteringSortingItem<ProductModel>> reOrganizeFavourites(List<FilteringSortingItem<ProductModel>> products) {
		//Collecting favourites
		ComparatorChain<FilteringSortingItem<ProductModel>> comparator = ComparatorChain.create(FilteringSortingItem.wrap(ScriptedContentNodeComparator.createUserComparator(getUserId(), getPricingContext())));
		SmartSearchUtils.collectAvailabilityInfo(products, getPricingContext());
		comparator.prepend(new SortValueComparator<ProductModel>(EnumSortingValue.AVAILABILITY));
		Collections.sort(products, comparator);
		List<FilteringSortingItem<ProductModel>> favourites = new ArrayList<FilteringSortingItem<ProductModel>>();
		for (FilteringSortingItem<ProductModel> product : products) {
			if (ScoreProvider.getInstance().isUserHasScore(getUserId(), product.getNode().getContentKey()) && product.getModel().isFullyAvailable()) {
				favourites.add(product);
			}
		}

		//Sorting favourites according to user relevance
		comparator = ComparatorChain.create(new SortValueComparator<ProductModel>(EnumSortingValue.CATEGORY_RELEVANCY));
		comparator.chain(new SortLongValueComparator<ProductModel>(EnumSortingValue.TERM_SCORE));
		SmartSearchUtils.collectAvailabilityInfo(products, getPricingContext());
		comparator.prepend(new SortValueComparator<ProductModel>(EnumSortingValue.AVAILABILITY));
		Collections.sort(favourites, comparator);

		//Reordering favourites in the product list
		for (int index = 0; index < Math.min(FDStoreProperties.getSearchPageTopFavouritesNumber(), favourites.size()); index ++) {
			products.remove(favourites.get(index));
		}
		
		comparator = ComparatorChain.create(new SortValueComparator<ProductModel>(EnumSortingValue.PHRASE));
		comparator.chain(new SortIntValueComparator<ProductModel>(EnumSortingValue.ORIGINAL_TERM));
		comparator.chain(new SortValueComparator<ProductModel>(EnumSortingValue.CATEGORY_RELEVANCY));
		comparator.chain(new SortLongValueComparator<ProductModel>(EnumSortingValue.TERM_SCORE));
		comparator.chain(FilteringSortingItem.wrap(ScriptedContentNodeComparator.createGlobalComparator(getUserId(), getPricingContext())));
		comparator.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
		SmartSearchUtils.collectAvailabilityInfo(products, getPricingContext());
		comparator.prepend(new SortValueComparator<ProductModel>(EnumSortingValue.AVAILABILITY));

		Collections.sort(products, comparator);
		for (int index = Math.min(FDStoreProperties.getSearchPageTopFavouritesNumber(), favourites.size()) - 1; index >= 0 ; index --) {
			products.add(0,favourites.get(index));
		}
		
		return products;
	}

	@Override
	protected void preProcess(List<FilteringSortingItem<ProductModel>> items) {
		for(FilteringSortingItem<ProductModel> item:items){
			item.setNode(ProductPricingFactory.getInstance().getPricingAdapter(item.getNode(), getPricingContext()));
		}		
	}
	
	@Override
	protected void postProcess(List<FilteringSortingItem<ProductModel>> items) {
			
	}

	public void setResults(SearchResults results) {
		this.results = results;
	}
	
	public FDUserI getFDUser() {
		if (user == null) {
			if (mocked) {
				if (mockCustomerId != null)
					try {
						user = FDCustomerManager.recognize(new FDIdentity(mockCustomerId));
					} catch (FDAuthenticationException e) {
						LOGGER.warn("authentication failed for customer: " + mockCustomerId, e);
					} catch (FDResourceException e) {
						LOGGER.warn("failed to recognize customer: " + mockCustomerId, e);
					}
			} else
				user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		}
		return user;
	}

	public String getUserId() {
		FDUserI user = getFDUser();
		if (user != null) {
			FDIdentity identity = user.getIdentity();
			if (identity != null)
				return identity.getErpCustomerPK();
		}
		return null;
	}

	public PricingContext getPricingContext() {
		FDUserI user = getFDUser();
		if (user != null)
			return user.getPricingContext();
		return PricingContext.DEFAULT;
	}
	
	private boolean isShowGrouped() {
		return nav.getSortBy().equals(SearchSortType.BY_RECENCY) && nav.getFilterValues().get(
				EnumFilteringValue.BRAND) == null && nav.getFilterValues().get(EnumFilteringValue.DEPT) == null && (nav.getFilterValues().get(
				EnumFilteringValue.CAT) == null || nav.getFilterValues().get(EnumFilteringValue.CAT).equals(
				FDStoreProperties.getNewProductsCatId()));
	}

	@Override
	protected Set<EnumFilteringValue> getFilters() {
		return filters;
	}

}
