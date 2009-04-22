package com.freshdirect.fdstore.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.DepartmentFilter;
import com.freshdirect.framework.util.StringUtil;


/**
 * Navigator component for search page.
 * 
 * @author segabor
 *
 */
public class SearchNavigator {
	public static final Logger LOGGER = Logger.getLogger(SearchNavigator.class);

	public static final String RECIPES_DEPT = "rec";
	

    String	searchTerms;
    
    public static final int VIEW_LIST		= 0;
    public static final int VIEW_GRID		= 1;
    public static final int VIEW_TEXT		= 2;
    public static final int VIEW_DEFAULT	= VIEW_GRID;
    
    
    int		view = VIEW_DEFAULT;

	String	deptFilter; // Note: Department OR Category filter. They cannot exist at once
	String	categoryFilter;

	String	brandFilter;
	
	String	recipeFilter;


	int	pageSize = 0; // 0 means show all in one page
	int pageNumber = 0;

	
	public static final int SORT_BY_NAME		= 0;
	public static final int SORT_BY_PRICE		= 1;
	public static final int SORT_BY_RELEVANCY	= 2;
	public static final int SORT_BY_POPULARITY	= 3;
	public static final int SORT_DEFAULT		= 4; // 'default' sort on text view
	public static final int SORT_BY_SALE		= 5;

	public static final int SORT_DEFAULT_TEXT = SORT_DEFAULT; // default sort on text view
	public static final int SORT_DEFAULT_NOT_TEXT = SORT_BY_RELEVANCY; // default sort on list and grid views
	public static final int SORT_DEFAULT_RECIPE = SORT_BY_NAME;

	int		sortBy;
	boolean	isOrderAscending;

	String	searchAction = "/search.jsp";

	
	public class SortDisplay {
		public final int		sortType; 	// == SORT_BY_<sort type>
		public final boolean	isSelected;
		public final boolean	ascending;	// Display ascending
		public final String		text;		// display name
		public SortDisplay(int sortType, boolean isSelected, boolean ascending, String text, String ascText, String descText) {
			this.sortType = sortType;
			this.isSelected = isSelected;
			this.ascending = ascending;
			this.text = isSelected ? (ascending ? ascText : descText) : text;
		}
	}


	public SearchNavigator(HttpServletRequest request) {
		Map p = new HashMap(request.getParameterMap().size());
		for (Enumeration it=request.getParameterNames(); it.hasMoreElements();) {
			String key=(String) it.nextElement();
			String value=request.getParameterValues(key)[0];
			p.put(key, value);
		}

		searchAction = request.getRequestURI();

		init(p);
	}


	/**
	 * Clone support
	 */
	protected SearchNavigator(String searchAction, String terms, int view, String dept, String cat, String brand, String rcp, int psize, int page, int sort, boolean ascend) {
		if (searchAction != null)
			this.searchAction = searchAction;
		
		this.searchTerms = terms;
		this.view = view;
		this.deptFilter = dept;
		this.categoryFilter = cat;
		this.brandFilter = brand;
		this.recipeFilter = rcp;
		this.pageSize = psize;
		this.pageNumber = page;
		this.sortBy = sort;
		this.isOrderAscending = ascend;
	}

	public Object clone() {
		return new SearchNavigator(searchAction, searchTerms, view, deptFilter, categoryFilter, brandFilter, recipeFilter, pageSize, pageNumber, sortBy, isOrderAscending);
	}

	/**
	 * Convenience method of clone()
	 */
	public SearchNavigator dup() {
		return (SearchNavigator) new SearchNavigator(searchAction, searchTerms, view, deptFilter, categoryFilter, brandFilter, recipeFilter, pageSize, pageNumber, sortBy, isOrderAscending);
	}




	/**
	 * Build navigator from request parameters
	 * 
	 * @param params
	 */
	private void init(Map params) {
		String val;


		/* search terms */
		val = (String)params.get("searchParams");
		if (val != null && val.length() > 0) {
			searchTerms = val;
		}

		
		/* view */
		val = (String)params.get("view");
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
				pageSize = 30;
			} else if (view == VIEW_GRID) {
				pageSize = 40;
			} else {
				pageSize = 0;
			}
		}

		// start = offset*page size
		val = (String) params.get("start");
		if (val != null && val.length() > 0) {
			int offset = Integer.parseInt(val);
			pageNumber = offset/pageSize;
		}

		// offset = page number
		val = (String) params.get("offset");
		if (val != null && val.length() > 0) {
			pageNumber = Integer.parseInt(val);
		}


		/* filters */

		val = (String) params.get("catId");
		if (val != null && val.length() > 0) {
			categoryFilter = val;
			deptFilter = null;
		}

		val = (String) params.get("deptId");
		if (val != null && val.length() > 0) {
			categoryFilter = null;
			deptFilter = val;
		}
		
		val = (String) params.get("brandValue");
		if (val != null && val.length() > 0) {
			brandFilter = val;
		}

		if (RECIPES_DEPT.equalsIgnoreCase(deptFilter)) {
			val = (String) params.get("classification");
			if (val != null && val.length() > 0) {
				recipeFilter = val;
			}
		}

		/* sort */
		
		val = (String) params.get("sort");
		if (val != null && val.length() > 0) {
			// sortBy = Integer.parseInt(val);
			sortBy = SearchNavigator.convertToSort(val);
			
			if (sortBy < 0 || sortBy > SORT_BY_SALE) {
				sortBy = SORT_DEFAULT;
			}
		} else {
			if (RECIPES_DEPT.equalsIgnoreCase(deptFilter)) {
				sortBy = SORT_DEFAULT_RECIPE;
			} else  {
				sortBy = (view == VIEW_TEXT ? SORT_DEFAULT_TEXT : SORT_DEFAULT_NOT_TEXT);
			}
		}

		val = (String) params.get("order");
		if (val != null && val.length() > 0) {
			isOrderAscending = !("desc".equalsIgnoreCase(val));
		} else {
			isOrderAscending = true;
		}
	}


	public SortDisplay[] getSortBar() {
		SortDisplay[] sortDisplayBar;
		
		if (this.isTextView()) {
			sortDisplayBar = new SortDisplay[6];

			sortDisplayBar[0] = new SortDisplay(SearchNavigator.SORT_DEFAULT, isDefaultSort(), isSortOrderingAscending(), "Default", "Default", "Default");
			sortDisplayBar[1] = new SortDisplay(SearchNavigator.SORT_BY_RELEVANCY, isSortByRelevancy(), isSortOrderingAscending(), "Relevance", "Most Relevant", "Least Relevant");
			sortDisplayBar[2] = new SortDisplay(SearchNavigator.SORT_BY_NAME, isSortByName(), isSortOrderingAscending(), "Name", "Name (A-Z)", "Name (Z-A)");
			sortDisplayBar[3] = new SortDisplay(SearchNavigator.SORT_BY_PRICE, isSortByPrice(), isSortOrderingAscending(), "Price", "Price (low)", "Price (high)");
			sortDisplayBar[4] = new SortDisplay(SearchNavigator.SORT_BY_POPULARITY, isSortByPopularity(), isSortOrderingAscending(), "Popularity", "Most Popular", "Least Popular");
			sortDisplayBar[5] = new SortDisplay(SearchNavigator.SORT_BY_SALE, isSortBySale(), isSortOrderingAscending(), "Sale", "Sale (yes)", "Sale (no)");
		} else {
			sortDisplayBar = new SortDisplay[5];

			sortDisplayBar[0] = new SortDisplay(SearchNavigator.SORT_BY_RELEVANCY, isDefaultSort(), isSortOrderingAscending(), "Relevance", "Most Relevant", "Least Relevant");
			sortDisplayBar[1] = new SortDisplay(SearchNavigator.SORT_BY_NAME, isSortByName(), isSortOrderingAscending(), "Name", "Name (A-Z)", "Name (Z-A)");
			sortDisplayBar[2] = new SortDisplay(SearchNavigator.SORT_BY_PRICE, isSortByPrice(), isSortOrderingAscending(), "Price", "Price (low)", "Price (high)");
			sortDisplayBar[3] = new SortDisplay(SearchNavigator.SORT_BY_POPULARITY, isSortByPopularity(), isSortOrderingAscending(), "Popularity", "Most Popular", "Least Popular");
			sortDisplayBar[4] = new SortDisplay(SearchNavigator.SORT_BY_SALE, isSortBySale(), isSortOrderingAscending(), "Sale", "Sale (yes)", "Sale (no)");
		}
		return sortDisplayBar;
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
		int offset = pageSize*pageNumber;
		
		if (size > 0) {
			pageSize = size;
			pageNumber = offset/size; // recalculate page number
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
		return pageSize*pageNumber;
	}

	public void setPageOffset(int offset) {
		if (pageSize > 0) {
			pageNumber = offset/pageSize;
		} else {
			pageNumber = 0;
		}
	}


	public SearchNavigator incPage() {
		pageNumber++; return this;
	}
	
	public SearchNavigator decPage() {
		if (pageNumber > 0) --pageNumber;
		return this;
	}


	


	public String getBrand() {
		return brandFilter;
	}


	public void setBrand(String brandName) {
		brandFilter = brandName;
	}




	public String getCategory() {
		return categoryFilter;
	}

	public void setCategory(String catId) {
		categoryFilter = catId;
		deptFilter = null;
		recipeFilter = null;
	}

	
	


	public String getDepartment() {
		return deptFilter;
	}
	
	
	public void setDepartment(String deptId) {
		deptFilter = deptId;
		categoryFilter = null;
		recipeFilter = null;
	}
	
	public String getRecipeFilter() {
		return this.recipeFilter;
	}
	
	
	public void setRecipeFilter(String rcp) {
		this.recipeFilter = rcp;
	}
	

	public boolean isRecipesDeptSelected() {
		return RECIPES_DEPT.equalsIgnoreCase(deptFilter);
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
	
	protected String getViewName() {
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
		if (oldView == VIEW_TEXT && 
			(view == VIEW_GRID || view == VIEW_LIST) &&
			sortBy == SORT_DEFAULT
		){
			sortBy = SORT_DEFAULT_NOT_TEXT;
			isOrderAscending = true;
		} else if ((oldView == VIEW_GRID || oldView == VIEW_LIST) &&
				view == VIEW_TEXT &&
				sortBy == SORT_DEFAULT_NOT_TEXT)
		{
			sortBy = SORT_BY_RELEVANCY;
			isOrderAscending = true;
		}

		// reset page number and size to default
		pageNumber = 0;
		switch(view) {
		case VIEW_LIST:
			pageSize = 30;
			break;
		case VIEW_GRID:
			pageSize = 40;
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
	public boolean isRecipesView() {
		// show recipes if recipes department is selected or all products are shown (no filters given)
		return RECIPES_DEPT.equalsIgnoreCase(deptFilter) || (categoryFilter == null && deptFilter == null);
	}



	public int getSortBy() {
		return sortBy;
	}


	// convenience method
	public String getSortByName() {
		return SearchNavigator.convertToSortName(sortBy);
	}


	public void setSortBy(int sortType) {
		if (sortBy == sortType)
			return;
		
		if (view == VIEW_TEXT) {
			switch(sortType) {
			case SORT_DEFAULT_TEXT:
			default:
				sortBy = SORT_DEFAULT_TEXT;
				break;
			case SORT_BY_NAME:
			case SORT_BY_POPULARITY:
			case SORT_BY_PRICE:
			case SORT_BY_RELEVANCY:
			case SORT_BY_SALE:
				sortBy = sortType;
			}
		} else if (view == VIEW_GRID || view == VIEW_LIST) {
			switch(sortType) {
			case SORT_DEFAULT_NOT_TEXT: // == SORT_BY_RELEVANCY
			default:
				sortBy = SORT_DEFAULT_NOT_TEXT;
				break;
			case SORT_BY_NAME:
			case SORT_BY_POPULARITY:
			case SORT_BY_PRICE:
			case SORT_BY_SALE:
				sortBy = sortType;
			}
		} else if (RECIPES_DEPT.equalsIgnoreCase(recipeFilter)) {
			sortBy = SORT_DEFAULT_RECIPE;
		} else {
			sortBy = SORT_DEFAULT_NOT_TEXT;
		}
		isOrderAscending = true; // reset order direction
	}

	
	public boolean isSortByRelevancy() {
		return sortBy == SORT_BY_RELEVANCY;
	}
	
	public boolean isSortByName() {
		return sortBy == SORT_BY_NAME;
	}
	
	public boolean isSortByPrice() {
		return sortBy == SORT_BY_PRICE;
	}
	
	public boolean isSortByPopularity() {
		return sortBy == SORT_BY_POPULARITY;
	}

	public boolean isSortBySale() {
		return sortBy == SORT_BY_SALE;
	}

	public boolean isDefaultSort() {
		return (view == VIEW_TEXT && sortBy == SORT_DEFAULT_TEXT) ||
		( (view == VIEW_GRID || view == VIEW_LIST) && sortBy == SORT_DEFAULT_NOT_TEXT) ||
		( RECIPES_DEPT.equalsIgnoreCase(deptFilter) && sortBy == SORT_DEFAULT_RECIPE);
	}



	
	public boolean isTextViewDefault() {
		return view == VIEW_TEXT && sortBy == SORT_DEFAULT_TEXT;
	}
	

	public boolean isSortOrderingAscending() {
		return isOrderAscending;
	}


	public SearchNavigator revertSortOrdering() {
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
//		buf.append( (this.searchAction != null ? this.searchAction : "/search.jsp") + "?");
		buf.append("?"); // generate relative url
		
		// search terms
		buf.append("searchParams=");
		// buf.append(safeURLEncode(searchTerms, "ISO-8859-1"));
		//buf.append(StringUtil.escapeUri(searchTerms));
		buf.append(safeURLEncode(searchTerms));

		if (view != VIEW_DEFAULT) {
			buf.append("&amp;view=" + getViewName());
		}
		
		
		if ( !(view == VIEW_TEXT || (view == VIEW_LIST && pageSize == 30) || (view == VIEW_GRID && pageSize == 40)) ) {
			buf.append("&amp;pageSize=");
			buf.append(pageSize);
		}
		if ( pageSize > 0 && pageNumber > 0) {
			buf.append("&amp;start=");
			buf.append(pageSize*pageNumber);
		}


		if (deptFilter != null) {
			buf.append("&amp;deptId=");
			buf.append(deptFilter);
		} else if (categoryFilter != null) {
			buf.append("&amp;catId=");
			buf.append(categoryFilter);
		}
		if (brandFilter != null) {
			buf.append("&amp;brandValue=");
			buf.append(brandFilter);
		}

		if (RECIPES_DEPT.equalsIgnoreCase(deptFilter) && recipeFilter != null) {
			buf.append("&amp;classification=");
			buf.append(recipeFilter);
		}

		// no sort options in recipes view, ignore them
		if ( !isDefaultSort() ) {
			buf.append("&amp;sort=" + SearchNavigator.convertToSortName(sortBy));
		}

		if ( !isOrderAscending ) {
			buf.append("&amp;order=desc");
		}

		buf.append("&amp;refinement=1");

		return buf.toString();
	}


	public void appendToBrandForm(JspWriter out) throws IOException {
		if (view != VIEW_DEFAULT) {
			append(out, "view", getViewName());
		}


		if (deptFilter != null) {
			append(out, "deptId", deptFilter);
		} else if (categoryFilter != null) {
			append(out, "catId", categoryFilter);
		}

		append(out, "refinement", "1");
	}


	private void append(JspWriter out, String name, String value) throws IOException {
		if (out != null && name != null && value != null) {
			out.write("<input type=\"hidden\" name=\""+name+"\" value=\""+value+"\">\n");
		}
	}

	
	//
	// ACTIONS (LINKS)
	//
	
	public String getNextPageAction()  {
		return dup().incPage().getLink();
	}
	
	public String getPrevPageAction() {
		return dup().decPage().getLink();
	}


	public String getPageSizeAction(int size) {
		SearchNavigator n = dup();
		n.setPageSize(size);
		return n.getLink();
	}

	
	public String getSwitchViewAction(int viewType) {
		SearchNavigator n = dup();
		n.setView(viewType);
		return n.getLink();
	}


	public String getRevertedOrderingAction() {
		return dup().revertSortOrdering().getLink();
	}


	public String getChangeSortAction(int sortType) {
		SearchNavigator n = dup();
		if (n.getSortBy() != sortType) {
			n.setSortBy(sortType);
		} else {
			// 'Default' sort has no reverse ordering
			if (SearchNavigator.SORT_DEFAULT != n.getSortBy())
				n.revertSortOrdering();
		}
		return n.getLink();
	}
	
	
	public String getDepartmentAction(String deptId) {
		SearchNavigator n = dup();
		
		n.setDepartment(deptId);

		// reset params
		n.setBrand(null);
		n.setPageNumber(0);
		
		return n.getLink();
	}
	

	public String getCategoryAction(String catId) {
		SearchNavigator n = dup();
		
		n.setCategory(catId);

		// reset params
		n.setBrand(null);
		n.setPageNumber(0);
		
		return n.getLink();
	}


	public String getRecipesAction() {
		SearchNavigator n = dup();
		
		n.setDepartment(RECIPES_DEPT);

		// reset params
		n.setBrand(null);
		n.setPageNumber(0);

		// reset sort
		n.setSortBy(SORT_DEFAULT_RECIPE);
		
		return n.getLink();
	}


	public String getUnfilteredPageAction() {
		// delete department and category filters
		return getDepartmentAction(null);
	}



	

	public String getJumpToPageAction(int offset) {
		SearchNavigator n = dup();

		n.setPageOffset(offset);

		return n.getLink();
	}

	
	
	public String getJumpToFilteredRecipesAction(String filter) {
		SearchNavigator n = dup();

		n.setDepartment(RECIPES_DEPT);
		n.setRecipeFilter(filter);

		// reset sort
		n.setSortBy(SORT_DEFAULT_RECIPE);

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
		switch(viewType) {
		case VIEW_LIST:
			return "list";
		case VIEW_GRID: // == VIEW_DEFAULT
		default: // VIEW_GRID == VIEW_DEFAULT
			return "grid";
		case VIEW_TEXT:
			return "text";
		}
	}





	public static int convertToSort(String sortName) {
		if ("tdef".equalsIgnoreCase(sortName)) {
			return SORT_DEFAULT;
		} else if ("pplr".equalsIgnoreCase(sortName)) {
			return SORT_BY_POPULARITY;
		} else if ("prce".equalsIgnoreCase(sortName)) {
			return SORT_BY_PRICE;
		} else if ("relv".equalsIgnoreCase(sortName)) {
			return SORT_BY_RELEVANCY;
		} else if ("name".equalsIgnoreCase(sortName)) {
			return SORT_BY_NAME;
		} else if ("sale".equalsIgnoreCase(sortName)) {
			return SORT_BY_SALE;
		}

		return SORT_DEFAULT_NOT_TEXT;
	}


	public static String convertToSortName(int sortType) {
		switch(sortType) {
		case SORT_DEFAULT:
			return "tdef";
		case SORT_BY_POPULARITY:
			return "pplr";
		case SORT_BY_PRICE:
			return "prce";
		default:
		case SORT_BY_RELEVANCY:
			return "relv";
		case SORT_BY_NAME:
			return "name";
		case SORT_BY_SALE:
			return "sale";
		}
	}
}
