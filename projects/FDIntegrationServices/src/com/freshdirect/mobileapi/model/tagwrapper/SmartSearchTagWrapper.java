package com.freshdirect.mobileapi.model.tagwrapper;

import javax.servlet.jsp.tagext.BodyTagSupport;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.SmartSearchTag;

public class SmartSearchTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName {

    public static final String SEARCH_RESULTS = "results";

    public static final String PRODUCT_LIST = "productList";

    public static final String CATEGORY_SET_NAME = "categorySet";

    public static final String BRAND_SET_NAME = "brandSet";

    public static final String CATEGORY_TREE_NAME = "categoryTree";

    public static final String FILTERED_CATEGORY_TREE_NAME = "filteredCategoryTree";

    public static final String ORDER_ASCENDANT = "asc";

    public static final String ORDER_DESCENDANT = "desc";

    public SmartSearchTagWrapper(SessionUser user) {
        super(new SmartSearchTag(), user);
    }

    public ResultBundle getSearchResult(String searchTerm, String upc, String deptId, String catId, String brandValue, Integer start, Integer pageSize,
            String order, String sort) throws FDException {
        ResultBundle result = null;
        ((SmartSearchTag) this.wrapTarget).setSearchResults(SEARCH_RESULTS);
        ((SmartSearchTag) this.wrapTarget).setProductList(PRODUCT_LIST);
        ((SmartSearchTag) this.wrapTarget).setCategorySet(CATEGORY_SET_NAME);
        ((SmartSearchTag) this.wrapTarget).setBrandSet(BRAND_SET_NAME);
        ((SmartSearchTag) this.wrapTarget).setCategoryTree(CATEGORY_TREE_NAME);
        ((SmartSearchTag) this.wrapTarget).setFilteredCategoryTreeName(FILTERED_CATEGORY_TREE_NAME);

        addExpectedRequestValues(
                new String[] { REQ_PARAM_SEARCH_BRAND_VALUE, REQ_PARAM_SEARCH_CLASSIFICATION, REQ_PARAM_SEARCH_DEPT_ID,
                        REQ_PARAM_SEARCH_ORDER, REQ_PARAM_SEARCH_SORT, REQ_PARAM_SEARCH_PAGE_SIZE, REQ_PARAM_SEARCH_START,
                        REQ_PARAM_SEARCH_PARAMS,REQ_PARAM_UPC, REQ_PARAM_SEARCH_VIEW, REQ_PARAM_SEARCH_CAT_ID, SEARCH_RESULTS }, new String[] { REQ_PARAM_SEARCH_PARAMS, REQ_PARAM_UPC, REQ_PARAM_SEARCH_BRAND_VALUE,
                        REQ_PARAM_SEARCH_CLASSIFICATION, REQ_PARAM_SEARCH_DEPT_ID, REQ_PARAM_SEARCH_ORDER, REQ_PARAM_SEARCH_SORT,
                        REQ_PARAM_SEARCH_CAT_ID, REQ_PARAM_SEARCH_VIEW, CATEGORY_TREE_NAME, BRAND_SET_NAME, CATEGORY_SET_NAME,
                        PRODUCT_LIST, SEARCH_RESULTS, FILTERED_CATEGORY_TREE_NAME, REQ_PARAM_SEARCH_START,REQ_PARAM_SEARCH_PAGE_SIZE });

        this.pageContext.setAttribute(REQ_PARAM_SEARCH_PARAMS, searchTerm);
        this.pageContext.setAttribute(REQ_PARAM_UPC, upc);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_DEPT_ID, deptId);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_CAT_ID, catId);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_BRAND_VALUE, brandValue);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_START, start.toString());
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_PAGE_SIZE, pageSize.toString());
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_SORT, sort);
        this.pageContext.setAttribute(REQ_PARAM_SEARCH_ORDER, "asc"); //locked to asc

        result = new ResultBundle(executeTagLogic(), this);

        result.addExtraData(SEARCH_RESULTS, getResult());

        return result;
    }

    @Override
    protected void setResult() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Object getResult() throws FDException {
        return pageContext.getAttribute(SEARCH_RESULTS);
    }

}
