package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.EnumSearchFilteringValue;
import com.freshdirect.fdstore.content.FilteringComparatorUtil;
import com.freshdirect.fdstore.content.FilteringFlow;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.GenericFilteringMenuBuilder;
import com.freshdirect.fdstore.content.ProductFilterMenuDecorator;
import com.freshdirect.fdstore.content.ProductFilterValueDecorator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ProductsFilterImpl extends FilteringFlow<ProductModel> {
	
	@SuppressWarnings( "unused" )
	private static Category LOGGER = LoggerFactory.getInstance(ProductsFilterImpl.class);

	private SearchResults results;
	private FilteringNavigator nav;
	private PageContext pageContext;
	
	private Set<FilteringValue> filters;
	{
		filters=new HashSet<FilteringValue>();
		filters.add(EnumSearchFilteringValue.DEPT);
		filters.add(EnumSearchFilteringValue.CAT);
		filters.add(EnumSearchFilteringValue.SUBCAT);
		filters.add(EnumSearchFilteringValue.BRAND);
		filters.add(EnumSearchFilteringValue.EXPERT_RATING);
		filters.add(EnumSearchFilteringValue.CUSTOMER_RATING);
		filters.add(EnumSearchFilteringValue.GLUTEN_FREE);
		filters.add(EnumSearchFilteringValue.KOSHER);
		filters.add(EnumSearchFilteringValue.NEW_OR_BACK);
		filters.add(EnumSearchFilteringValue.ON_SALE);
	}

	private FDUserI user;
	
	protected List<ProductModel> products;

	public ProductsFilterImpl(SearchResults results, FilteringNavigator nav, PageContext pageContext) {
		super();
		this.results = results;
		this.nav = nav;
		this.pageContext = pageContext;
	}

	@Override
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
	protected Set<FilteringValue> getFilterEnums() {
		return this.filters;
	}
	
	@Override
	protected Comparator<FilteringSortingItem<ProductModel>> createComparator(List<FilteringSortingItem<ProductModel>> products1) {
		String suggestedTerm = results.getSuggestedTerm();
		if (suggestedTerm == null)
			suggestedTerm = results.getSearchTerm();
		
		return FilteringComparatorUtil.createProductComparator(getItems(), getUserId(), getPricingContext(), suggestedTerm, nav, isShowGrouped());
	}
	
	@Override
	protected List<FilteringSortingItem<ProductModel>> reOrganizeFavourites(List<FilteringSortingItem<ProductModel>> products1) {
		
		return FilteringComparatorUtil.reOrganizeFavourites(products1, getUserId(), getPricingContext());

	}

	@Override
	protected void preProcess(List<FilteringSortingItem<ProductModel>> items) {
		for(FilteringSortingItem<ProductModel> item:items){
			item.setNode(ProductPricingFactory.getInstance().getPricingAdapter(item.getNode(), getPricingContext()));
		}		
	}
	
	@Override
	protected void postProcess(List<FilteringSortingItem<ProductModel>> items, GenericFilteringMenuBuilder<FilteringSortingItem<ProductModel>> menuBuilder) {
		//FilteringComparatorUtil.logSortResult(items, getFDUser());
	}

	public void setResults(SearchResults results) {
		this.results = results;
	}
	
	public FDUserI getFDUser() {
		if (user == null) {
			user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		}
		return user;
	}

	public String getUserId() {
		getFDUser();
		if (user != null) {
			FDIdentity identity = user.getIdentity();
			if (identity != null)
				return identity.getErpCustomerPK();
		}
		return null;
	}

	public PricingContext getPricingContext() {
		getFDUser();
		if (user != null)
			return user.getPricingContext();
		return PricingContext.DEFAULT;
	}
	
	// FIXME comparing a List<Object> to a String with equals() is kind of pointless... I'm not sure about the original intention here.
	private boolean isShowGrouped() {
		return 
				nav.getSortBy().equals(SearchSortType.BY_RECENCY) && 
				nav.getFilterValues().get(EnumSearchFilteringValue.BRAND) == null && 
				nav.getFilterValues().get(EnumSearchFilteringValue.DEPT) == null && 
				(nav.getFilterValues().get(EnumSearchFilteringValue.CAT) == null || nav.getFilterValues().get(EnumSearchFilteringValue.CAT).equals(FDStoreProperties.getNewProductsCatId())
			);
	}

	@Override
	protected Set<FilteringValue> getFilters() {
		return filters;
	}

	@Override
	protected void midProcess(List<FilteringSortingItem<ProductModel>> items) {
	}

}
