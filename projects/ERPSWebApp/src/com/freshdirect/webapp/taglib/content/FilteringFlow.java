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

import org.apache.log4j.Logger;

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
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public abstract class FilteringFlow<N extends ContentNodeModel> extends BodyTagSupportEx {
	
	private static final long serialVersionUID = 3670819169820229610L;
	
	private final static Logger LOG = LoggerFactory.getInstance( FilteringFlow.class );

	private String domainsId;
	private String itemsId;
	private String filteredItemCountId;
	protected FilteringNavigator nav;
	
	protected List<FilteringSortingItem<N>> items;

	@Override
	public int doStartTag() throws JspException {
		
		// Check required attributes
		if ( nav == null || domainsId == null || itemsId == null || filteredItemCountId == null ) {
			LOG.warn( "FilteringFlow received null attributes, skipping..." );
			return SKIP_BODY;
		}
		
		// Get & pre-process the items - implemented by subclass		
		items = getItems();
		preProcess(items);
		
		// Decorate all items (filter values)
		GenericFilterDecorator<FilteringSortingItem<N>> filterValueDecorator = createFilterValueDecorator();
		for ( FilteringSortingItem<N> item : items ) {
			filterValueDecorator.decorateItem( item );
		}
		
		// Prepare the filters 
		Map<EnumFilteringValue, List<Object>> filterValues = nav.getFilterValues();
		if ( filterValues == null ) {
			filterValues = new HashMap<EnumFilteringValue, List<Object>>();
		}

		// Separate AND and OR filters
		Map<EnumFilteringValue, List<Object>> andFilterValues = new HashMap<EnumFilteringValue, List<Object>>();
		Map<EnumFilteringValue, List<Object>> orFilterValues = new HashMap<EnumFilteringValue, List<Object>>();
		for ( Map.Entry<EnumFilteringValue, List<Object>> e : filterValues.entrySet() ) {
			EnumFilteringValue key = e.getKey();
			List<Object> value = e.getValue();
			( key.isMultiSelect() ? orFilterValues : andFilterValues ).put( key, value );
		}
		
		// Filter the items - 1st pass - AND filters
		GenericFilter<FilteringSortingItem<N>> andFilter = createFilter( andFilterValues );
		andFilter.applyAllFilterAnd( items );	
		
		
		// to hold the expert rating counts
		// array has six items for exp.rating 0-5, 0 means item has none 
		int[] expertRatingCounts = { 0,0,0,0,0,0 };
		int[] custRatingCounts = { 0,0,0,0,0,0 };
		
		// fetch expert & customer ratings before filtering
		for ( FilteringSortingItem<N> item : items ) {
			int expR = 0, custR = 0;
			try { expR = Integer.parseInt( item.getFilteringValue( EnumFilteringValue.EXPERT_RATING ).toString() ); } catch (Exception e) {/*ignore*/}
			try { custR = Integer.parseInt( item.getFilteringValue( EnumFilteringValue.CUSTOMER_RATING ).toString() ); } catch (Exception e) {/*ignore*/}
			if ( expR >= 0 && expR <= 5 ) expertRatingCounts[expR]++;
			if ( custR >= 0 && custR <= 5 ) custRatingCounts[custR]++;
		}
		
		
		// Filter the items - 2nd pass - OR filters
		GenericFilter<FilteringSortingItem<N>> orFilter = createFilter( orFilterValues );
		orFilter.applyAllFilterOr( items );

		
		// Sort the remaining results
		Comparator<FilteringSortingItem<N>> comparator = createComparator(items);
		if (comparator != null) {
			Collections.sort(items, comparator);
		}
		// Special re-sorting for favourites
		if (FDStoreProperties.isFavouritesTopNumberFilterSwitchedOn() && nav.getSortBy().equals(SearchSortType.BY_RELEVANCY)) {
			items = reOrganizeFavourites(items);
		}
		
		// prepare items for menu building - decorate items (menu values)
		GenericFilterDecorator<FilteringSortingItem<N>> menuValueDecorator = createMenuValueDecorator();
		for ( FilteringSortingItem<N> item : items ) {
			menuValueDecorator.decorateItem( item );
		}
		
		// Build the menu
		GenericFilteringMenuBuilder<FilteringSortingItem<N>> menuBuilder = createMenuBuilder( filterValues );
		menuBuilder.buildMenu( items, expertRatingCounts, custRatingCounts );

		// Post-process items
		postProcess(items);
		
		// Put results into jsp page context
		pageContext.setAttribute( itemsId, items );
		pageContext.setAttribute( domainsId, menuBuilder.getDomains() );
		pageContext.setAttribute( filteredItemCountId, items.size() );
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
