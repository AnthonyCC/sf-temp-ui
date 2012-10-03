package com.freshdirect.fdstore.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.content.UrlFilterValueDecoder;
import com.freshdirect.fdstore.content.util.QueryParameter;

public class FilteringNavigator {
	public static final Logger LOGGER = Logger.getLogger(FilteringNavigator.class);

	public static final String RECIPES_DEPT = "rec";

	public static final int VIEW_LIST = 0;
	public static final int VIEW_GRID = 1;
	public static final int VIEW_TEXT = 2;
	public static final int VIEW_DEFAULT = VIEW_GRID;

	private OriginalStatus originalStatus;

	private String searchTerm = "";
	private boolean recipes = false;

	int view = VIEW_DEFAULT;

	boolean fromDym;
	boolean refined;

	int defaultPageSize = 16;
	int pageSize = 0; // 0 means show all in one page
	int pageNumber = 0;

	SearchSortType sortBy;
	boolean isOrderAscending;

	private Set<EnumFilteringValue> filters;
	
	{
		filters = new HashSet<EnumFilteringValue>();
		filters.add(EnumFilteringValue.DEPT);
		filters.add(EnumFilteringValue.CAT);
		filters.add(EnumFilteringValue.SUBCAT);
		filters.add(EnumFilteringValue.BRAND);
		filters.add(EnumFilteringValue.EXPERT_RATING);
		filters.add(EnumFilteringValue.GLUTEN_FREE);
		filters.add(EnumFilteringValue.KOSHER);
		filters.add(EnumFilteringValue.NEW_OR_BACK);
		filters.add(EnumFilteringValue.ON_SALE);
		filters.add(EnumFilteringValue.RECIPE_CLASSIFICATION);
	}

	UrlFilterValueDecoder filterDecoder = new UrlFilterValueDecoder(filters);

	private Map<EnumFilteringValue, List<Object>> filterValues;

	public FilteringNavigator(ServletRequest servletRequest, int defaultPageSize) {
		Map<String, String> p = new HashMap<String, String>(servletRequest.getParameterMap().size());
		for (Enumeration<?> it = servletRequest.getParameterNames(); it.hasMoreElements();) {
			String key = (String) it.nextElement();
			String value = servletRequest.getParameterValues(key)[0];
			p.put(key, value);
		}

		this.defaultPageSize = defaultPageSize;
		
		init(p);
		
		saveState();
	}
	
	public FilteringNavigator() {
	}

	public FilteringNavigator clone() {

		FilteringNavigator nav = new FilteringNavigator();

		nav.setSearchTerm(searchTerm);

		nav.setFilterValues(cloneFilterValues(filterValues));
		nav.setDefaultPageSize(defaultPageSize);
		nav.setPageSize(pageSize);
		nav.setPageNumber(pageNumber);
		nav.setSortBy(sortBy);
		nav.setOrderAscending(isOrderAscending);
		nav.setFromDym(fromDym);
		nav.setRefined(refined);
		nav.setRecipes(recipes);
		nav.setView(view);

		return nav;
	}

	private Map<EnumFilteringValue, List<Object>> cloneFilterValues(Map<EnumFilteringValue, List<Object>> filterValues2) {
		Map<EnumFilteringValue, List<Object>> clone = new HashMap<EnumFilteringValue, List<Object>>();

		for (Map.Entry<EnumFilteringValue, List<Object>> e : filterValues2.entrySet()) {
			clone.put(e.getKey(), new ArrayList<Object>(e.getValue()));
		}

		return clone;
	}
	
	public void saveState() {
		originalStatus = new OriginalStatus(searchTerm, view, fromDym, refined, pageSize, pageNumber, sortBy,
				isOrderAscending, filters, cloneFilterValues(filterValues), recipes);		
	}

	public void resetState() {
		searchTerm = originalStatus.getSearchTerm();
		filters = originalStatus.getFilters();
		filterValues = cloneFilterValues(originalStatus.getFilterValues());
		pageNumber = originalStatus.getPageNumber();
		pageSize = originalStatus.getPageSize();
		sortBy = originalStatus.getSortBy();
		view = originalStatus.getView();
		isOrderAscending = originalStatus.isOrderAscending();
		fromDym = originalStatus.isFromDym();
		refined = originalStatus.isRefined();
		recipes = originalStatus.isRecipes();
	}
	
	/**
	 * Build navigator from request parameters
	 * 
	 * @param params
	 */
	private void init(Map<String, String> params) {
		String val;

		/* search terms */
		val = (String) params.get("searchParams");
		if (val != null && val.trim().length() > 0) {
			searchTerm = val.trim();
		}

		/* view */
		val = (String) params.get("view");
		if (val != null && val.length() > 0) {
			if ("list".equalsIgnoreCase(val)) {
				view = VIEW_LIST;
			} else if ("grid".equalsIgnoreCase(val)) {
				view = VIEW_GRID;
			} else if ("text".equalsIgnoreCase(val)) {
				view = VIEW_TEXT;
			}
		} else {
			// default view
			view = VIEW_DEFAULT;
		}

		/* paging parameters */

		val = (String) params.get("pageSize");
		if (val != null && val.length() > 0) {
			pageSize = Integer.parseInt(val);
		} else {
			if (view == VIEW_LIST) {
				pageSize = defaultPageSize;
			} else if (view == VIEW_GRID) {
				pageSize = defaultPageSize;
			} else {
				pageSize = 0;
			}
		}

		// start = offset*page size
		val = (String) params.get("start");
		if (val != null && val.length() > 0) {
			int offset = Integer.parseInt(val);
			pageNumber = offset / pageSize;
		}

		// offset = page number
		val = (String) params.get("offset");
		if (val != null && val.length() > 0) {
			pageNumber = Integer.parseInt(val);
		}

		/* filters */

		String filterSource = (String) params.get(QueryParameter.GENERIC_FILTER);

		if (filterSource != null) {
			filterValues = filterDecoder.decode(filterSource);
		} else {
			filterValues = new HashMap<EnumFilteringValue, List<Object>>();
		}

		/* sort */

		val = (String) params.get("sort");
		if (val != null && val.length() > 0) {
			sortBy = SearchSortType.findByLabel(val) /*
													 * SearchNavigator.convertToSort (val)
													 */;
			if (sortBy == null)
				sortBy = SearchSortType.DEFAULT;

			/*
			 * if (sortBy < 0 || sortBy > SORT_BY_SALE) { sortBy = SORT_DEFAULT; }
			 */
		} else {
			if (filterValues.containsKey(EnumFilteringValue.RECIPE_CLASSIFICATION)) {
				sortBy = SearchSortType.DEF4RECIPES /* SORT_DEFAULT_RECIPE */;
			} else {
				// sortBy = (view == VIEW_TEXT ? SORT_DEFAULT_TEXT :
				// SORT_DEFAULT_NOT_TEXT);
				sortBy = (view == VIEW_TEXT ? SearchSortType.DEF4TEXT : SearchSortType.DEF4NOTTEXT);
			}
		}

		val = (String) params.get("order");
		if (val != null && val.length() > 0) {
			isOrderAscending = !("desc".equalsIgnoreCase(val));
		} else {
			isOrderAscending = true;
		}

		val = (String) params.get("fromDym");
		if (val != null && val.length() > 0)
			fromDym = true;

		val = (String) params.get("refinement");
		if (val != null && val.length() > 0)
			refined = true;

		/* recipes tab */

		recipes = params.containsKey("recipes");
	}

	public SortDisplay[] getSortBar() {
		SortDisplay[] sortDisplayBar;

		if (this.isTextView()) {
			sortDisplayBar = new SortDisplay[6];

			sortDisplayBar[0] = new SortDisplay(SearchSortType.DEFAULT , isDefaultSort(), isSortOrderingAscending(), "Default",
					"Default", "Default");
			sortDisplayBar[1] = new SortDisplay(SearchSortType.BY_RELEVANCY, isSortByRelevancy(), isSortOrderingAscending(),
					"Relevance", "Most Relevant", "Least Relevant");
			sortDisplayBar[2] = new SortDisplay(SearchSortType.BY_NAME, isSortByName(), isSortOrderingAscending(), "Name",
					"Name (A-Z)", "Name (Z-A)");
			sortDisplayBar[3] = new SortDisplay(SearchSortType.BY_PRICE, isSortByPrice(), isSortOrderingAscending(), "Price",
					"Price (low)", "Price (high)");
			sortDisplayBar[4] = new SortDisplay(SearchSortType.BY_POPULARITY, isSortByPopularity(), isSortOrderingAscending(),
					"Popularity", "Most Popular", "Least Popular");
			sortDisplayBar[5] = new SortDisplay(SearchSortType.BY_SALE, isSortBySale(), isSortOrderingAscending(), "Sale",
					"Sale (yes)", "Sale (no)");
		} else {
			sortDisplayBar = new SortDisplay[5];

			sortDisplayBar[0] = new SortDisplay(SearchSortType.BY_RELEVANCY, isDefaultSort(), isSortOrderingAscending(),
					"Relevance", "Most Relevant", "Least Relevant");
			sortDisplayBar[1] = new SortDisplay(SearchSortType.BY_NAME, isSortByName(), isSortOrderingAscending(), "Name",
					"Name (A-Z)", "Name (Z-A)");
			sortDisplayBar[2] = new SortDisplay(SearchSortType.BY_PRICE, isSortByPrice(), isSortOrderingAscending(), "Price",
					"Price (low)", "Price (high)");
			sortDisplayBar[3] = new SortDisplay(SearchSortType.BY_POPULARITY, isSortByPopularity(), isSortOrderingAscending(),
					"Popularity", "Most Popular", "Least Popular");
			sortDisplayBar[4] = new SortDisplay(SearchSortType.BY_SALE, isSortBySale(), isSortOrderingAscending(), "Sale",
					"Sale (yes)", "Sale (no)");
		}
		return sortDisplayBar;
	}
	
	public ArrayList<SortDisplay> getSortBar(SearchSortType[] sorts) {
		ArrayList<SortDisplay> sortDisplayBar = new ArrayList<SortDisplay>();

		for(SearchSortType sort : sorts) {
			sortDisplayBar.add(new SortDisplay(sort,sortBy == sort, isSortOrderingAscending()));
		}
		
		return sortDisplayBar;		
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public boolean isFullPageMode() {
		return pageSize == 0;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int size) {
		if (pageSize == size)
			return;

		// preserve offset
		int offset = pageSize * pageNumber;

		if (size > 0) {
			pageSize = size;
			pageNumber = offset / size; // recalculate page number
		} else {
			pageSize = 0;
			pageNumber = 0;
		}
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int ix) {
		pageNumber = ix;
	}

	public int getPageOffset() {
		return pageSize * pageNumber;
	}

	public void setPageOffset(int offset) {
		if (pageSize > 0) {
			pageNumber = offset / pageSize;
		} else {
			pageNumber = 0;
		}
	}

	public FilteringNavigator incPage() {
		pageNumber++;
		return this;
	}

	public FilteringNavigator decPage() {
		if (pageNumber > 0)
			--pageNumber;
		return this;
	}

	public void removeFilter(EnumFilteringValue filter, String value) {
		List<Object> values = filterValues.get(filter);
		if (values != null) {
			for (Iterator<Object> it = values.iterator(); it.hasNext();) {
				if (value.equals(it.next())) {
					it.remove();
				}
			}
		}
	}

	public void removeAllFilters() {
		filterValues = new HashMap<EnumFilteringValue, List<Object>>();
	}

	public void setDeptFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.DEPT, v);
	}

	public void addDeptFilter(String value) {
		if (filterValues.get(EnumFilteringValue.DEPT) != null) {
			filterValues.get(EnumFilteringValue.DEPT).add(value);
		} else {
			setDeptFilter(value);
		}
	}

	public void removeDeptFilters() {
		filterValues.remove(EnumFilteringValue.DEPT);
	}

	public void setCatFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.CAT, v);
	}

	public void addCatFilter(String value) {
		if (filterValues.get(EnumFilteringValue.CAT) != null) {
			filterValues.get(EnumFilteringValue.CAT).add(value);
		} else {
			setCatFilter(value);
		}
	}

	public void removeCatFilters() {
		filterValues.remove(EnumFilteringValue.CAT);
	}

	public void setSubCatFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.SUBCAT, v);
	}

	public void addSubCatFilter(String value) {
		if (filterValues.get(EnumFilteringValue.SUBCAT) != null) {
			filterValues.get(EnumFilteringValue.SUBCAT).add(value);
		} else {
			setSubCatFilter(value);
		}
	}

	public void removeSubCatFilters() {
		filterValues.remove(EnumFilteringValue.SUBCAT);
	}

	public void setBrandFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.BRAND, v);
	}

	public void addBrandFilter(String value) {
		if (filterValues.get(EnumFilteringValue.BRAND) != null) {
			filterValues.get(EnumFilteringValue.BRAND).add(value);
		} else {
			setBrandFilter(value);
		}
	}

	public void removeBrandFilters() {
		filterValues.remove(EnumFilteringValue.BRAND);
	}

	public void removeBrandFilter(String value) {
		List<Object> values = filterValues.get(EnumFilteringValue.BRAND);
		if (values != null) {
			for (Iterator<Object> it = values.iterator(); it.hasNext();) {
				if (value.equals(it.next())) {
					it.remove();
				}
			}
		}
	}

	public void setExpRatingFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.EXPERT_RATING, v);
	}

	public void addExpRatingFilter(String value) {
		if (filterValues.get(EnumFilteringValue.EXPERT_RATING) != null) {
			filterValues.get(EnumFilteringValue.EXPERT_RATING).add(value);
		} else {
			setExpRatingFilter(value);
		}
	}

	public void removeExpRatingFilters() {
		filterValues.remove(EnumFilteringValue.EXPERT_RATING);
	}

	public void removeExpRatingFilter(String value) {
		List<Object> values = filterValues.get(EnumFilteringValue.EXPERT_RATING);
		if (values != null) {
			for (Iterator<Object> it = values.iterator(); it.hasNext();) {
				if (value.equals(it.next())) {
					it.remove();
				}
			}
		}
	}

	public void setOnSalelFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.ON_SALE, v);
	}

	public void addOnSalelFilter(String value) {
		if (filterValues.get(EnumFilteringValue.ON_SALE) != null) {
			filterValues.get(EnumFilteringValue.ON_SALE).add(value);
		} else {
			setOnSalelFilter(value);
		}
	}

	public void removeOnSaleFilters() {
		filterValues.remove(EnumFilteringValue.ON_SALE);
	}

	public void setNewOrBackFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.NEW_OR_BACK, v);
	}

	public void addNewOrBackFilter(String value) {
		if (filterValues.get(EnumFilteringValue.NEW_OR_BACK) != null) {
			filterValues.get(EnumFilteringValue.NEW_OR_BACK).add(value);
		} else {
			setNewOrBackFilter(value);
		}
	}

	public void removeNewOrBackFilters() {
		filterValues.remove(EnumFilteringValue.NEW_OR_BACK);
	}

	public void setKosherFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.KOSHER, v);
	}

	public void addKosherFilter(String value) {
		if (filterValues.get(EnumFilteringValue.KOSHER) != null) {
			filterValues.get(EnumFilteringValue.KOSHER).add(value);
		} else {
			setKosherFilter(value);
		}
	}

	public void removeKosherFilters() {
		filterValues.remove(EnumFilteringValue.KOSHER);
	}

	public void setGlutenFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.GLUTEN_FREE, v);
	}

	public void addGlutenFilter(String value) {
		if (filterValues.get(EnumFilteringValue.GLUTEN_FREE) != null) {
			filterValues.get(EnumFilteringValue.GLUTEN_FREE).add(value);
		} else {
			setGlutenFilter(value);
		}
	}

	public void removeGlutenFilters() {
		filterValues.remove(EnumFilteringValue.GLUTEN_FREE);
	}

	public void setRecipeFilter(String value) {
		List<Object> v = new ArrayList<Object>();
		v.add(value);
		filterValues.put(EnumFilteringValue.RECIPE_CLASSIFICATION, v);
	}

	public void addRecipeFilter(String value) {
		if (filterValues.get(EnumFilteringValue.RECIPE_CLASSIFICATION) != null) {
			filterValues.get(EnumFilteringValue.RECIPE_CLASSIFICATION).add(value);
		} else {
			setRecipeFilter(value);
		}
	}

	public void removeRecipeFilters() {
		filterValues.remove(EnumFilteringValue.RECIPE_CLASSIFICATION);
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public void setFromDym(boolean fromDym) {
		this.fromDym = fromDym;
	}

	public void setRefined(boolean refined) {
		this.refined = refined;
	}

	public boolean isRefined() {
		return refined;
	}

	public void switchView(String viewName) {
		if (viewName != null && viewName.length() > 0) {
			if ("list".equalsIgnoreCase(viewName)) {
				view = VIEW_LIST;
			} else if ("grid".equalsIgnoreCase(viewName)) {
				view = VIEW_GRID;
			} else if ("text".equalsIgnoreCase(viewName)) {
				view = VIEW_TEXT;
			}
		} else {
			// default view
			view = VIEW_DEFAULT;
		}
	}

	public int getView() {
		return view;
	}

	public String getViewName() {
		switch (view) {
			case VIEW_LIST:
				return "list";
			case VIEW_GRID:
			default: // VIEW_DEFAULT
				return "grid";
			case VIEW_TEXT:
				return "text";
		}
	}

	public static String getViewName(int viewType) {
		switch (viewType) {
			case VIEW_LIST:
				return "list";
			case VIEW_GRID:
			default: // VIEW_DEFAULT
				return "grid";
			case VIEW_TEXT:
				return "text";
		}
	}

	public static String getDefaultViewName() {
		return getViewName(VIEW_DEFAULT);
	}

	public void setView(int viewType) {
		if (viewType == view)
			return;

		int oldView = view;

		if (viewType >= VIEW_LIST && viewType <= VIEW_TEXT) {
			view = viewType;
		} else {
			view = VIEW_DEFAULT;
		}

		// adjust sort
		if (oldView == VIEW_TEXT && (view == VIEW_GRID || view == VIEW_LIST) && sortBy == SearchSortType.DEFAULT) {
			sortBy = SearchSortType.BY_RELEVANCY /* SORT_DEFAULT_NOT_TEXT */;
			isOrderAscending = true;
		} else if ((oldView == VIEW_GRID || oldView == VIEW_LIST) && view == VIEW_TEXT && sortBy == SearchSortType.DEFAULT /* SORT_DEFAULT_NOT_TEXT */) {
			sortBy = SearchSortType.BY_RELEVANCY;
			isOrderAscending = true;
		}

		// reset page number and size to default
		pageNumber = 0;
		switch (view) {
			case VIEW_LIST:
				pageSize = defaultPageSize;
				break;
			case VIEW_GRID:
				pageSize = defaultPageSize;
				break;
			default:
				pageSize = 0;
		}
	}

	public boolean isListView() {
		return view == VIEW_LIST;
	}

	public boolean isGridView() {
		return view == VIEW_GRID;
	}

	public boolean isTextView() {
		return view == VIEW_TEXT;
	}

	public SearchSortType getSortBy() {
		return sortBy;
	}

	// convenience method
	public String getSortByName() {
		return sortBy.getLabel();
	}

	public void setSortBy(SearchSortType sortType) {
		if (sortBy == sortType)
			return;

		if (view == VIEW_TEXT) {
			sortBy = (sortType == null ? SearchSortType.DEF4TEXT : sortType);
		} else if (view == VIEW_GRID || view == VIEW_LIST) {
			sortBy = (sortType == null ? SearchSortType.DEF4NOTTEXT : sortType);
		} else {
			sortBy = SearchSortType.DEF4NOTTEXT;
		}

		isOrderAscending = true; // reset order direction
	}

	public boolean isSortByRelevancy() {
		return sortBy == SearchSortType.BY_RELEVANCY;
	}

	public boolean isSortByName() {
		return sortBy == SearchSortType.BY_NAME;
	}

	public boolean isSortByPrice() {
		return sortBy == SearchSortType.BY_PRICE;
	}

	public boolean isSortByPopularity() {
		return sortBy == SearchSortType.BY_POPULARITY;
	}

	public boolean isSortBySale() {
		return sortBy == SearchSortType.BY_SALE;
	}

	public boolean isDefaultSort() {
		return (view == VIEW_TEXT && sortBy == SearchSortType.DEF4TEXT)
				|| ((view == VIEW_GRID || view == VIEW_LIST) && sortBy == SearchSortType.DEF4NOTTEXT)
				|| (filterValues.containsKey(EnumFilteringValue.RECIPE_CLASSIFICATION) && sortBy == SearchSortType.DEF4RECIPES);
	}

	public boolean isTextViewDefault() {
		return view == VIEW_TEXT && sortBy == SearchSortType.DEF4TEXT;
	}

	public boolean isOrderAscending() {
		return isOrderAscending;
	}

	public void setOrderAscending(boolean isOrderAscending) {
		this.isOrderAscending = isOrderAscending;
	}

	public FilteringNavigator revertSortOrdering() {
		isOrderAscending = !isOrderAscending;
		return this;
	}

	// convenience method
	private String safeURLEncode(String str) {
		try {
			return URLEncoder.encode(str, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// NOTE: this should never happen!
			return "";
		}
	}

	/**
	 * Serialize navigator state into action link
	 */
	public String getLink() {
		StringBuffer buf = new StringBuffer();

		// URL prefix - the search page
		// buf.append( (this.searchAction != null ? this.searchAction :
		// "/search.jsp") + "?");
		buf.append("?"); // generate relative url

		// search terms
		buf.append("searchParams=");
		// buf.append(safeURLEncode(searchTerms, "ISO-8859-1"));
		// buf.append(StringUtil.escapeUri(searchTerms));
		buf.append(safeURLEncode(searchTerm));

		/* VIEW */
		buf.append("&amp;view=" + getViewName());

		if (!(view == VIEW_TEXT || (view == VIEW_LIST && pageSize == defaultPageSize) || (view == VIEW_GRID && pageSize == defaultPageSize))) {
			buf.append("&amp;pageSize=");
			buf.append(pageSize);
		}

		/* PAGE */
		if (pageSize > 0 && pageNumber > 0) {
			buf.append("&amp;start=");
			buf.append(pageSize * pageNumber);
		}

		/* FILTER */

		if (!filterValues.isEmpty()) {
			buf.append("&amp;genericFilter=");

			for (EnumFilteringValue key : filterValues.keySet()) {
				List<Object> fl = filterValues.get(key);
				for (Object value : fl) {
					buf.append(key.getName() + "=" + value.toString() + ",");
				}
			}

			int lastComma = buf.lastIndexOf(",");
			if (lastComma != -1) {
				buf = new StringBuffer(buf.substring(0, lastComma));
			}
		}

		/* SORT */
		// no sort options in recipes view, ignore them
		if (!isDefaultSort()) {
			buf.append("&amp;sort=" + sortBy.getLabel());
		}

		if (!isOrderAscending) {
			buf.append("&amp;order=desc");
		}
		
		/* DEPT */
		if (filterValues.get(EnumFilteringValue.DEPT) != null && !filterValues.get(EnumFilteringValue.DEPT).isEmpty() ) {
			buf.append("&amp;deptId=" + filterValues.get(EnumFilteringValue.DEPT).get(0) );
		}

		if (fromDym)
			buf.append("&amp;fromDym=1");

		if (recipes)
			buf.append("&amp;recipes");

		buf.append("&amp;refinement=1");

		return buf.toString();
	}

	public String getNextPageAction() {
		return clone().incPage().getLink();
	}

	public String getPrevPageAction() {
		return clone().decPage().getLink();
	}

	public String getPageSizeAction(int size) {
		FilteringNavigator n = clone();
		n.setPageSize(size);
		return n.getLink();
	}

	public String getSwitchViewAction(int viewType) {
		FilteringNavigator n = clone();
		n.setView(viewType);
		return n.getLink();
	}

	public String getRevertedOrderingAction() {
		return clone().revertSortOrdering().getLink();
	}

	public String getChangeSortAction(SearchSortType sortType) {
		FilteringNavigator n = clone();
		if (n.getSortBy() != sortType) {
			n.setSortBy(sortType);
		} else {
			// 'Default' sort has no reverse ordering
			if (SearchSortType.DEFAULT != n.getSortBy())
				n.revertSortOrdering();
		}
		n.pageNumber = 0;
		return n.getLink();
	}

	public String getDepartmentAction(String deptId) {
		FilteringNavigator n = clone();

		n.setDeptFilter(deptId);

		// reset params
		n.setBrandFilter(null);
		n.setPageNumber(0);

		return n.getLink();
	}

	public String getRecipesAction() {
		FilteringNavigator n = clone();

		n.setDeptFilter(RECIPES_DEPT);

		// reset params
		n.setBrandFilter(null);
		n.setPageNumber(0);

		// reset sort
		n.setSortBy(SearchSortType.DEF4RECIPES);

		return n.getLink();
	}

	public String getUnfilteredPageAction() {
		// delete department and category filters
		return getDepartmentAction(null);
	}

	public String getJumpToPageAction(int offset) {
		FilteringNavigator n = clone();

		n.setPageOffset(offset);

		return n.getLink();
	}

	public String getJumpToFilteredRecipesAction(String filter) {
		FilteringNavigator n = clone();

		n.setDeptFilter(RECIPES_DEPT);
		n.setRecipeFilter(filter);

		// reset sort
		n.setSortBy(SearchSortType.DEF4RECIPES);

		return n.getLink();
	}

	public static int convertToView(String viewName) {
		int view = VIEW_DEFAULT;

		if ("list".equalsIgnoreCase(viewName)) {
			view = VIEW_LIST;
		} else if ("grid".equalsIgnoreCase(viewName)) {
			view = VIEW_GRID;
		} else if ("text".equalsIgnoreCase(viewName)) {
			view = VIEW_TEXT;
		}

		return view;
	}

	public static String convertToViewName(int viewType) {
		switch (viewType) {
			case VIEW_LIST:
				return "list";
			case VIEW_GRID: // == VIEW_DEFAULT
			default: // VIEW_GRID == VIEW_DEFAULT
				return "grid";
			case VIEW_TEXT:
				return "text";
		}
	}

	public static SearchSortType convertToSort(String sortName) {
		SearchSortType s = SearchSortType.findByLabel(sortName);
		if (s == null)
			s = SearchSortType.DEF4NOTTEXT;

		return s;
	}

	public static String convertToSortName(int sortType) {
		SearchSortType s = SearchSortType.findByType(sortType);
		if (s == null)
			s = SearchSortType.DEF4NOTTEXT;

		return s.getLabel();
	}

	public void setRecipes(boolean recipes) {
		this.recipes = recipes;
	}

	public boolean isRecipes() {
		return recipes;
	}

	public String toString() {
		return getLink();
	}
	
	public int getDefaultPageSize() {
		return defaultPageSize;
	}
	
	public void setDefaultPageSize(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}

	public Map<EnumFilteringValue, List<Object>> getFilterValues() {
		return filterValues;
	}

	public void setFilterValues(Map<EnumFilteringValue, List<Object>> filterValues) {
		this.filterValues = filterValues;
	}

	public boolean isSortOrderingAscending() {
		return isOrderAscending;
	}

	private class OriginalStatus {

		private String searchTerm;
		private int view;
		private boolean fromDym;
		private boolean refined;
		private int pageSize;
		private int pageNumber;
		private SearchSortType sortBy;
		private boolean isOrderAscending;
		private Set<EnumFilteringValue> filters;
		private final Map<EnumFilteringValue, List<Object>> filterValues;
		private boolean recipes;

		public OriginalStatus(String searchTerm, int view, boolean fromDym, boolean refined, int pageSize, int pageNumber,
				SearchSortType sortBy, boolean isOrderAscending, Set<EnumFilteringValue> filters,
				Map<EnumFilteringValue, List<Object>> filterValues, boolean recipes) {
			super();
			this.searchTerm = searchTerm;
			this.view = view;
			this.fromDym = fromDym;
			this.refined = refined;
			this.pageSize = pageSize;
			this.pageNumber = pageNumber;
			this.sortBy = sortBy;
			this.isOrderAscending = isOrderAscending;
			this.filters = filters;
			this.filterValues = filterValues;
			this.recipes = recipes;
		}

		public String getSearchTerm() {
			return searchTerm;
		}

		public int getView() {
			return view;
		}

		public boolean isFromDym() {
			return fromDym;
		}

		public boolean isRefined() {
			return refined;
		}

		public int getPageSize() {
			return pageSize;
		}

		public int getPageNumber() {
			return pageNumber;
		}

		public SearchSortType getSortBy() {
			return sortBy;
		}

		public boolean isOrderAscending() {
			return isOrderAscending;
		}

		public Set<EnumFilteringValue> getFilters() {
			return filters;
		}

		public Map<EnumFilteringValue, List<Object>> getFilterValues() {
			return filterValues;
		}

		public boolean isRecipes() {
			return recipes;
		}
	}
}
