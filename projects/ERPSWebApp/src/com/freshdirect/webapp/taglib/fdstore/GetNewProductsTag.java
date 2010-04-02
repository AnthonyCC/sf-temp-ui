package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;
import org.hibernate.Criteria;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.util.AbstractNavigator;
import com.freshdirect.fdstore.util.NewProductsNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetNewProductsTag extends AbstractNavigationTag {

	private static Category LOGGER = LoggerFactory.getInstance( GetNewProductsTag.class );

	//private int days = 120;
	private String department = null;
	
	public void setDepartment(String department) {
		this.department = department;
	}

    public SearchResults getResults(Map<String, String> criteria) {
    	//int days = new Integer(criteria.get("days")).intValue();
    	String department =  criteria.get("deptId");
		List<ProductModel> prods = new ArrayList<ProductModel>(ContentFactory.getInstance().getNewProducts().keySet());
		prods.addAll(ContentFactory.getInstance().getBackInStockProducts().keySet()); 
		prods = ContentFactory.filterProductsByDeptartment(prods, department);
		String searchTerm = "";
		SearchResults results = new SearchResults(prods, Collections.EMPTY_LIST,false,searchTerm);
		return results;
    }
    
    public Map getCriteria(ServletRequest request){
    	Map criteria = new HashMap<String, String>();
    	//criteria.put("days", String.valueOf(days));
    	criteria.put("deptId", this.department);
    	return criteria;
    }
    
    public AbstractNavigator getNavigator(){
    	return new NewProductsNavigator((HttpServletRequest)pageContext.getRequest());
    }
}
