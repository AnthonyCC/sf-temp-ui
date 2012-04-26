package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
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

import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.GenericFilter;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.GenericFilterValueDecoder;
import com.freshdirect.fdstore.content.GenericFilteringMenuBuilder;
import com.freshdirect.fdstore.content.util.QueryParameter;
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
	List<N> items;

	@Override
	public int doStartTag() throws JspException {

		String filterSource = (String) request.getParameter(QueryParameter.GENERIC_FILTER);
		
		items = getItems();
		preProcess(items);

		if (filterSource != null) {
			filter = createFilter(createDecoder().decode(filterSource));
			filterValueDecorator = createFilterValueDecorator();
			for (N item : items) {
				filterValueDecorator.decorateItem(item);
			}
			filter.applyAllFilter(items);
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
		
		Map<EnumFilteringValue, List<Object>> filterValues;
		if(filter!=null){
			filterValues=filter.getFilterValues();
		}else{
			filterValues=new HashMap<EnumFilteringValue, List<Object>>();
		}
		menuBuilder = createMenuBuilder(filterValues);
		menuBuilder.buildMenu(items);

		pageContext.setAttribute(itemsId, createItemPageWindow());
		pageContext.setAttribute(domainsId, menuBuilder.getDomains());
		pageContext.setAttribute(filteredItemCountId, items.size());

		return EVAL_BODY_INCLUDE;
	}

	protected List<N> createItemPageWindow() {
		int pageSize = getPageSize();
		int pageOffset = getPageOffset();

		List<N> pageProducts = new ArrayList<N>(pageSize <= 0 ? items.size() : pageSize);
		int noOfPagedProducts = items.size();
		int pageCount = pageSize == 0 ? 1 : noOfPagedProducts / pageSize;
		if (pageSize != 0 && noOfPagedProducts % pageSize > 0) {
			pageCount++;

		}
		int max = pageSize == 0 ? pageOffset + noOfPagedProducts : pageOffset + pageSize;
		for (int i = pageOffset; i < max; i++) {
			if (i >= items.size()) {
				break;
			}
			pageProducts.add(items.get(i));
		}

		return pageProducts;
	}

	protected abstract GenericFilterValueDecoder createDecoder();

	protected abstract GenericFilterDecorator<N> createFilterValueDecorator();

	protected abstract GenericFilterDecorator<N> createMenuValueDecorator();

	protected abstract GenericFilter<N> createFilter(Map<EnumFilteringValue, List<Object>> filterValues);

	protected abstract GenericFilteringMenuBuilder<N> createMenuBuilder(Map<EnumFilteringValue, List<Object>> filterValues);

	protected abstract Comparator<N> createComparator(List<N> items);

	protected abstract List<N> getItems();

	protected abstract Set<EnumFilteringValue> getFilterEnums();

	protected abstract void postProcess(List<N> items);

	protected abstract void preProcess(List<N> items);

	protected abstract int getPageSize();

	protected abstract int getPageOffset();

	protected abstract int getPageNumber();

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
