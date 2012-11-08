package com.freshdirect.webapp.taglib.content;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringSortingItemFilter;
import com.freshdirect.fdstore.content.FilteringSortingMenuBuilder;
import com.freshdirect.fdstore.content.GenericFilter;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.GenericFilterValueDecoder;
import com.freshdirect.fdstore.content.GenericFilteringMenuBuilder;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.content.UrlFilterValueDecoder;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public abstract class FilteringFlow<N extends ContentNodeModel> extends BodyTagSupportEx {
	
	private static final long serialVersionUID = 3670819169820229610L;

	private String domainsId;
	private String itemsId;
	private String filteredItemCountId;

	protected GenericFilterValueDecoder decoder;
	protected GenericFilterDecorator<FilteringSortingItem<N>> filterValueDecorator;
	protected GenericFilterDecorator<FilteringSortingItem<N>> menuValueDecorator;
	protected GenericFilter<FilteringSortingItem<N>> filter;
	protected GenericFilteringMenuBuilder<FilteringSortingItem<N>> menuBuilder;
	protected Comparator<FilteringSortingItem<N>> comparator;
	protected FilteringNavigator nav;

	List<FilteringSortingItem<N>> items;

	@Override
	public int doStartTag() throws JspException {
		
		Map<EnumFilteringValue, List<Object>> filterValues = null;
		items = getItems();
		preProcess(items);

		if(nav != null) {
			filterValues = nav.getFilterValues();
			if (filterValues != null && filterValues.size() > 0 ) {
				filter = createFilter(filterValues);
				filterValueDecorator = createFilterValueDecorator();
				for (FilteringSortingItem<N> item : items) {
					filterValueDecorator.decorateItem(item);
				}
				filter.applyAllFilter(items);
			}
			
			
		}
		
		comparator = createComparator(items);
		if (comparator != null) {
			Collections.sort(items, comparator);
		}
		if (FDStoreProperties.isFavouritesTopNumberFilterSwitchedOn() && nav.getSortBy().equals(SearchSortType.BY_RELEVANCY)) {
			items = reOrganizeFavourites(items);
		}
		
		// prepare items for menu building
		menuValueDecorator = createMenuValueDecorator();

		for (FilteringSortingItem<N> item : items) {
			menuValueDecorator.decorateItem(item);
		}
		
		if(filterValues == null){
			filterValues=new HashMap<EnumFilteringValue, List<Object>>();
		}
		
		menuBuilder = createMenuBuilder(filterValues);
		menuBuilder.buildMenu(items);

		postProcess(items);
		
		pageContext.setAttribute(itemsId, items);
		pageContext.setAttribute(domainsId, menuBuilder.getDomains());
		pageContext.setAttribute(filteredItemCountId, items.size());
		return EVAL_BODY_INCLUDE;
	}


	protected abstract GenericFilterDecorator<FilteringSortingItem<N>> createFilterValueDecorator();

	protected abstract GenericFilterDecorator<FilteringSortingItem<N>> createMenuValueDecorator();

	protected abstract Comparator<FilteringSortingItem<N>> createComparator(List<FilteringSortingItem<N>> items);
	
	protected abstract List<FilteringSortingItem<N>> reOrganizeFavourites(List<FilteringSortingItem<N>> items);

	protected abstract List<FilteringSortingItem<N>> getItems();

	protected abstract Set<EnumFilteringValue> getFilterEnums();

	protected abstract void postProcess(List<FilteringSortingItem<N>> items);

	protected abstract void preProcess(List<FilteringSortingItem<N>> items);
	
	protected abstract Set<EnumFilteringValue> getFilters();

	public void setDomainsId(String domainsId) {
		this.domainsId = domainsId;
	}

	public String getDomainsId() {
		return domainsId;
	}

	public void setItemsId(String itemsId) {
		this.itemsId = itemsId;
	}

	public String getItemsId() {
		return itemsId;
	}

	public String getFilteredItemCountId() {
		return filteredItemCountId;
	}

	public void setFilteredItemCountId(String filteredItemCountId) {
		this.filteredItemCountId = filteredItemCountId;
	}
	
	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}

	protected GenericFilteringMenuBuilder<FilteringSortingItem<N>> createMenuBuilder(Map<EnumFilteringValue, List<Object>> filterValues) {
		return new FilteringSortingMenuBuilder<N>(filterValues, getFilters());
	}

	
	protected GenericFilter<FilteringSortingItem<N>> createFilter(Map<EnumFilteringValue, List<Object>> filterValues) {
		return new FilteringSortingItemFilter<N>(filterValues, getFilters());
	}

	protected GenericFilterValueDecoder createDecoder() {
		return new UrlFilterValueDecoder(getFilters());
	}
	

	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] { new VariableInfo(
					data.getAttributeString("domainsId"),
					Map.class.getName() + "<" + EnumFilteringValue.class.getName() + "," + Map.class.getName() + "<" + String.class.getName() + "," + FilteringMenuItem.class.getName() + ">>",
					true,
					VariableInfo.NESTED), new VariableInfo(
					data.getAttributeString("itemsId"),
					List.class.getName(),
					true,
					VariableInfo.NESTED), new VariableInfo(
					data.getAttributeString("filteredItemCountId"),
					Integer.class.getName(),
					true,
					VariableInfo.NESTED) };
		}
	}

}
