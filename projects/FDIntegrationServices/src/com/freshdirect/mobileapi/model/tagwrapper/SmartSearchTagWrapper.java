package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.ArrayList;
import java.util.Collection;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductFilterI;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.util.SearchNavigator;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.filter.AvailableFilter;
import com.freshdirect.mobileapi.model.filter.DiscontinuityFilter;
import com.freshdirect.mobileapi.model.filter.IphoneFilter;
import com.freshdirect.webapp.taglib.fdstore.SmartSearchTag;

public class SmartSearchTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName {

    public static final String ID = "id";

    public static final String ORDER_ASCENDANT = "asc";

    public static final String ORDER_DESCENDANT = "desc";

    private static final AvailableFilter availableFilter = new AvailableFilter();
    
    private static final DiscontinuityFilter discontinuityFilter = new DiscontinuityFilter();

    private static final IphoneFilter iphoneFilter = new IphoneFilter();
    
    public SmartSearchTagWrapper(SessionUser user) {
        super(new SmartSearchTag(), user);
    }

    public ResultBundle getSearchResult(String searchTerm, String upc, String deptId, String catId, String brandValue, Integer start, Integer pageSize,
            String order, String sort) throws FDException {
        ResultBundle result = null;

        ((SmartSearchTag) this.wrapTarget).setId(ID);
		SearchSortType searchSortType = SearchSortType.findByLabel(sort);
		if (searchSortType == null)
			searchSortType = SearchSortType.BY_RELEVANCY;
		((SmartSearchTag) this.wrapTarget).setNav(SearchNavigator.mock(searchTerm, upc, SearchNavigator.VIEW_DEFAULT,
				deptId, catId, brandValue, null, pageSize != null ? pageSize : -1, start != null ? start : 0, 
				searchSortType, true, false, true));
		Collection<ProductFilterI> filters = new ArrayList<ProductFilterI>();
		if (FDStoreProperties.isIPhoneSearchFilterDiscontinuedOnly())
			filters.add(discontinuityFilter);
		/*else if(!(ContentFactory.getInstance()!=null && ContentFactory.getInstance().getStore()!=null && ContentFactory.getInstance().getStore().getContentName()!=null && ContentFactory.getInstance().getStore().getContentName().equals("FDX")))
			filters.add(availableFilter);*/
		filters.add(iphoneFilter);
		((SmartSearchTag) this.wrapTarget).setProductFilters(filters);

        addExpectedRequestValues(
                new String[] { REQ_PARAM_SEARCH_BRAND_VALUE, REQ_PARAM_SEARCH_CLASSIFICATION, REQ_PARAM_SEARCH_DEPT_ID,
                        REQ_PARAM_SEARCH_ORDER, REQ_PARAM_SEARCH_SORT, REQ_PARAM_SEARCH_PAGE_SIZE, REQ_PARAM_SEARCH_START,
                        REQ_PARAM_SEARCH_PARAMS,REQ_PARAM_UPC, REQ_PARAM_SEARCH_VIEW, REQ_PARAM_SEARCH_CAT_ID, ID },
                new String[] { REQ_PARAM_SEARCH_PARAMS, REQ_PARAM_UPC, REQ_PARAM_SEARCH_BRAND_VALUE, REQ_PARAM_SEARCH_CLASSIFICATION,
                		REQ_PARAM_SEARCH_DEPT_ID, REQ_PARAM_SEARCH_ORDER, REQ_PARAM_SEARCH_SORT, REQ_PARAM_SEARCH_CAT_ID,
                		REQ_PARAM_SEARCH_VIEW, REQ_PARAM_SEARCH_START, REQ_PARAM_SEARCH_PAGE_SIZE, ID });

        this.pageContext.setAttribute(REQ_PARAM_SEARCH_PARAMS, searchTerm);
        this.pageContext.setAttribute(REQ_PARAM_UPC, upc);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_DEPT_ID, deptId);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_CAT_ID, catId);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_BRAND_VALUE, brandValue);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_START, start.toString());
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_PAGE_SIZE, pageSize.toString());
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_SORT, searchSortType);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_ORDER, "asc"); //locked to asc

        result = new ResultBundle(executeTagLogic(), this);

        result.addExtraData(ID, getResult());

        return result;
    }

    @Override
    protected void setResult() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Object getResult() throws FDException {
        return pageContext.getAttribute(ID);
    }

}
