package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.content.ProductsFilterTag;

/**
 * Wrapper for {@see com.freshdirect.webapp.taglib.content.ProductsFilterTag} 
 * @author sbagavathiappan
 *
 */
public class GetProductFilterTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName {
    
	public final static String domainsId = "domainsId";
    public final static String itemsId = "itemsId";
    public final static String filteredItemCountId = "filteredItemCountId";
    
    public GetProductFilterTagWrapper(SessionUser user) {
        super(new ProductsFilterTag(), user);
    }

    public ResultBundle getFilteredList(SearchResults results, Map<FilteringValue, List<Object>> filterValues) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_USER },new String[]{}); //gets,sets
        addExpectedRequestValues(new String[] {domainsId, itemsId, filteredItemCountId}
        						, new String[] { domainsId, itemsId, filteredItemCountId });//gets,sets
        
        FilteringNavigator nav = new FilteringNavigator(filterValues, 20);
        
        ((ProductsFilterTag) wrapTarget).setResults(results);
        ((ProductsFilterTag) wrapTarget).setNav(nav);
        ((ProductsFilterTag) wrapTarget).setDomainsId(domainsId);
        ((ProductsFilterTag) wrapTarget).setItemsId(itemsId);
        ((ProductsFilterTag) wrapTarget).setFilteredItemCountId(filteredItemCountId);
        

        ResultBundle result = new ResultBundle(executeTagLogic(), this);

        result.addExtraData(itemsId, getProducts());
        result.addExtraData(domainsId, getDomains());

        return result;
    }
       
    public List<FilteringSortingItem<ProductModel>> getProducts() {
    	return (List<FilteringSortingItem<ProductModel>>) this.pageContext.getAttribute(itemsId);
    }
    
    public Map<FilteringValue, Map<String, FilteringMenuItem>> getDomains() {
    	return ( Map<FilteringValue, Map<String, FilteringMenuItem>>) this.pageContext.getAttribute(domainsId);
	}

	@Override
	protected void setResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object getResult() throws FDException {
		return this.pageContext.getAttribute(filteredItemCountId);
	}
}
