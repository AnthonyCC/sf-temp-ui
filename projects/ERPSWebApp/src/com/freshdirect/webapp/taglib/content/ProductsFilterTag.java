package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.FilteringComparatorUtil;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringSortingItemFilter;
import com.freshdirect.fdstore.content.FilteringSortingMenuBuilder;
import com.freshdirect.fdstore.content.GenericFilter;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.GenericFilterValueDecoder;
import com.freshdirect.fdstore.content.GenericFilteringMenuBuilder;
import com.freshdirect.fdstore.content.ProductFilterMenuDecorator;
import com.freshdirect.fdstore.content.ProductFilterValueDecorator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.content.UrlFilterValueDecoder;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ProductsFilterTag extends FilteringFlow<FilteringSortingItem<ProductModel>> {
	
	private static Category LOGGER = LoggerFactory.getInstance(ProductsFilterTag.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -3101346359422968490L;
	
	private SearchResults results;
	private FilteringNavigator nav;
	private Set<EnumFilteringValue> filters;
	{
		filters=new HashSet<EnumFilteringValue>();
		filters.add(EnumFilteringValue.DEPT);
		filters.add(EnumFilteringValue.CAT);
		filters.add(EnumFilteringValue.SUBCAT);
		filters.add(EnumFilteringValue.BRAND);
		filters.add(EnumFilteringValue.EXPERT_RATING);
		filters.add(EnumFilteringValue.GLUTEN_FREE);
		filters.add(EnumFilteringValue.KOSHER);
		filters.add(EnumFilteringValue.NEW_OR_BACK);
		filters.add(EnumFilteringValue.ON_SALE);
	}

	private boolean mocked = false;
	private String mockCustomerId;
	private FDUserI user;
	
	protected List<ProductModel> products;

	@Override
	protected GenericFilterValueDecoder createDecoder() {
		return new UrlFilterValueDecoder(filters);
	}

	@Override
	protected GenericFilterDecorator<FilteringSortingItem<ProductModel>> createFilterValueDecorator() {
		return new ProductFilterValueDecorator(getPricingContext(), filters);
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
	protected GenericFilter createFilter(Map<EnumFilteringValue, List<Object>> filterValues) {
		return new FilteringSortingItemFilter(filterValues, filters);
	}

	@Override
	protected GenericFilteringMenuBuilder createMenuBuilder(Map<EnumFilteringValue, List<Object>> filterValues) {
		return new FilteringSortingMenuBuilder(filterValues, filters);
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
	
	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
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

}
