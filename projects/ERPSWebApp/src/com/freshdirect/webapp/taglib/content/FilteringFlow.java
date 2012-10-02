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

import org.apache.openjpa.jdbc.kernel.exps.FilterValue;

import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringSortingItemFilter;
import com.freshdirect.fdstore.content.FilteringSortingMenuBuilder;
import com.freshdirect.fdstore.content.GenericFilter;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.GenericFilterValueDecoder;
import com.freshdirect.fdstore.content.GenericFilteringMenuBuilder;
import com.freshdirect.fdstore.content.UrlFilterValueDecoder;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public abstract class FilteringFlow<N> extends BodyTagSupportEx {
	private static final long serialVersionUID = 3670819169820229610L;

	private String domainsId;
	private String itemsId;
	private String filteredItemCountId;

	protected GenericFilterValueDecoder decoder;
	protected GenericFilterDecorator<N> filterValueDecorator;
	protected GenericFilterDecorator<N> menuValueDecorator;
	protected GenericFilter<N> filter;
	protected GenericFilteringMenuBuilder<N> menuBuilder;
	protected Comparator<N> comparator;
	protected FilteringNavigator nav;

	List<N> items;

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
				for (N item : items) {
					filterValueDecorator.decorateItem(item);
				}
				filter.applyAllFilter(items);
			}
			
			
		}
		
		comparator = createComparator(items);
		if (comparator != null) {
			Collections.sort(items, comparator);
		}
		
		// prepare items for menu building
		menuValueDecorator = createMenuValueDecorator();

		for (N item : items) {
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


	protected abstract GenericFilterDecorator<N> createFilterValueDecorator();

	protected abstract GenericFilterDecorator<N> createMenuValueDecorator();

	protected abstract Comparator<N> createComparator(List<N> items);

	protected abstract List<N> getItems();

	protected abstract Set<EnumFilteringValue> getFilterEnums();

	protected abstract void postProcess(List<N> items);

	protected abstract void preProcess(List<N> items);
	
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

	protected GenericFilteringMenuBuilder createMenuBuilder(Map<EnumFilteringValue, List<Object>> filterValues) {
		return new FilteringSortingMenuBuilder(filterValues, getFilters());
	}

	
	protected GenericFilter createFilter(Map<EnumFilteringValue, List<Object>> filterValues) {
		return new FilteringSortingItemFilter(filterValues, getFilters());
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
