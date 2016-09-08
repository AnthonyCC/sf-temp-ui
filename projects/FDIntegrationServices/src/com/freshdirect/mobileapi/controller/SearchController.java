package com.freshdirect.mobileapi.controller;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.SearchResult;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;
import com.freshdirect.mobileapi.controller.data.response.AutoComplete;
import com.freshdirect.mobileapi.controller.data.response.FilterOption;
import com.freshdirect.mobileapi.controller.data.response.SearchMessageResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.comparator.FilterOptionLabelComparator;
import com.freshdirect.mobileapi.service.ProductServiceImpl;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.mobileapi.util.SortType;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SearchController extends BaseController {
    private static org.apache.log4j.Category LOG = LoggerFactory.getInstance(SearchController.class);

    private static final String AUTOCOMPLETE_ACTION = "autocomplete";
    private static final String ACTION_SEARCH_EX = "searchEX";

    @Override
    protected boolean validateUser() {
        return false;
    }

    @Override
    /**
     * There are two ways to access this services. The basic way is just sending the search term, page and page size(max)
     * in the querystring as a GET request. Default values: searchTerm is an empty string that will return all products, 
     * page default value is 1, max default value is 25. The payload is checked in case something was send through POST 
     * and will overwrite any value from query string. 
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, NoSessionException, JsonException {
    	if (user == null) {
    		user = fakeUser(request.getSession());
    	}

        if (AUTOCOMPLETE_ACTION.equalsIgnoreCase(action)) {
            autocomplete(request, response, user, model);
        } else if (ACTION_SEARCH_EX.equalsIgnoreCase(action)){
        	searchEX(request, response, model, user);
        } else { // default go to search
            search(request, response, model, user);
        }
        return model;
    }

    private ModelAndView search(HttpServletRequest request, HttpServletResponse response, ModelAndView model, SessionUser user)
            throws FDException, ServiceException, NoSessionException, JsonException {
        // Default values retrieved from GET request
        String searchTerm = request.getParameter("searchTerm");
        String upc = request.getParameter("upc");
        int page = (StringUtils.isNumeric(request.getParameter("page")) ? Integer.parseInt(request.getParameter("page")) : 1);
        int resultMax = (StringUtils.isNumeric(request.getParameter("max")) ? Integer.parseInt(request.getParameter("max")) : 25);
        SortType sortType = SortType.RELEVANCY; //Default sort type
        String brandToFilter = null;
        String categoryToFilter = null;
        String departmentToFilter = null;

        // Retrieving any possible payload
        String postData = getPostData(request, response);

        LOG.debug("PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            SearchQuery requestMessage = parseRequestObject(request, response, SearchQuery.class);
            searchTerm = requestMessage.getQuery();
            upc = requestMessage.getUpc();
            page = requestMessage.getPage();
            resultMax = requestMessage.getMax();
            sortType = SortType.valueFromString(requestMessage.getSortBy());
            brandToFilter = requestMessage.getBrand();
            categoryToFilter = requestMessage.getCategory();
            departmentToFilter = requestMessage.getDepartment();
        }

        // If there is no searchTerm, default is blank string (will retrieve everything)
        if (searchTerm == null) {
            searchTerm = "";
        }
        
        // brand search
        if (isBlank(searchTerm) && isNotBlank(brandToFilter)) {
        	searchTerm = brandToFilter;
        }
        
        if (null != searchTerm) {
            searchTerm = searchTerm.trim();
        }

        ProductServiceImpl productService = new ProductServiceImpl();
        FilterOptionLabelComparator filterComparator = new FilterOptionLabelComparator();

        List<Product> products = productService.search(searchTerm, upc, page, resultMax, sortType, brandToFilter, categoryToFilter, departmentToFilter,
                getUserFromSession(request, response));
        
        if(request.getSession().getAttribute(SessionName.APPLICATION)!=null 
        		&& EnumTransactionSource.FRIDGE.getCode().equalsIgnoreCase((String)request.getSession().getAttribute(SessionName.APPLICATION))){
        		ListPaginator<Product> paginator = new ListPaginator<Product>(
           			   products, resultMax);
                products = paginator.getPage(page);
        }
        		
        
        // Data required for filtering: Brands
        Set<Brand> brands = productService.getBrands();
        List<FilterOption> brandList = new ArrayList<FilterOption>();
        Iterator<Brand> bit = brands.iterator();
        while (bit.hasNext()) {
            Brand brand = bit.next();
            FilterOption option = new FilterOption();
            option.setId(brand.getId());
            option.setLabel(brand.getName());
            brandList.add(option);
        }
        
        Collections.sort(brandList,filterComparator);

        // Data required for filtering: Categories
        Set<Category> categories = productService.getCategories();
        List<FilterOption> categoryList = new ArrayList<FilterOption>();
        Iterator<Category> cit = categories.iterator();
        while (cit.hasNext()) {
            Category category = cit.next();
            FilterOption option = new FilterOption();
            option.setId(category.getId());
            option.setLabel(category.getName());
            categoryList.add(option);
        }

        Collections.sort(categoryList,filterComparator);

        // Data required for filtering: Departments
        Set<Department> departments = productService.getDepartments();
        List<FilterOption> departmentList = new ArrayList<FilterOption>();
        Iterator<Department> dit = departments.iterator();
        while (dit.hasNext()) {
            Department department = dit.next();
            FilterOption option = new FilterOption();
            option.setId(department.getId());
            option.setLabel(department.getName());
            departmentList.add(option);
        }

        Collections.sort(departmentList, filterComparator);

        SearchResult data = new SearchResult();
        data.setTotalResultCount(productService.getRecentSearchTotalCount());
        data.setQuery(searchTerm);
        data.setProductsFromModel(products);
        data.setBrands(brandList);
        data.setCategories(categoryList);
        data.setDepartments(departmentList);
        data.setDidYouMean(productService.getSpellingSuggestion());
        data.setDefaultSortOptions();
        //Use below at later time.
        //LOG.debug(ContentFactory.getInstance().getStore().getSearchPageSortOptions());
        
        if (isCheckLoginStatusEnable(request)){
            SearchMessageResponse searchResponse = new SearchMessageResponse();
            populateMessageResponse(user, searchResponse, request);
            searchResponse.setSearch(data);
            setResponseMessage(model, searchResponse, user);
        } else{
            setResponseMessage(model, data, user);
        }

        return model;
    }
    
    private ModelAndView searchEX(HttpServletRequest request, HttpServletResponse response, ModelAndView model, SessionUser user)
            throws FDException, ServiceException, NoSessionException, JsonException {
        // Default values retrieved from GET request
        String searchTerm = request.getParameter("searchTerm");
        String upc = request.getParameter("upc");
        int page = (StringUtils.isNumeric(request.getParameter("page")) ? Integer.parseInt(request.getParameter("page")) : 1);
        int resultMax = (StringUtils.isNumeric(request.getParameter("max")) ? Integer.parseInt(request.getParameter("max")) : 25);
        SortType sortType = SortType.RELEVANCY; //Default sort type
        String brandToFilter = null;
        String categoryToFilter = null;
        String departmentToFilter = null;
        SearchQuery requestMessage = null;

        // Retrieving any possible payload
        String postData = getPostData(request, response);

        LOG.debug("PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            requestMessage = parseRequestObject(request, response, SearchQuery.class);
            searchTerm = requestMessage.getQuery();
            upc = requestMessage.getUpc();
            page = requestMessage.getPage();
            resultMax = requestMessage.getMax();
            sortType = SortType.valueFromString(requestMessage.getSortBy());
            brandToFilter = requestMessage.getBrand();
            categoryToFilter = requestMessage.getCategory();
            departmentToFilter = requestMessage.getDepartment();
        }

        // If there is no searchTerm, default is blank string (will retrieve everything)
        if (searchTerm == null) {
            searchTerm = "";
        }
        
        // brand search
        if (isBlank(searchTerm) && isNotBlank(brandToFilter)) {
        	searchTerm = brandToFilter;
        }
        
        if (null != searchTerm) {
            searchTerm = searchTerm.trim();
        }

        ProductServiceImpl productService = new ProductServiceImpl();
        FilterOptionLabelComparator filterComparator = new FilterOptionLabelComparator();

        List<String> products = productService.searchProductIds(searchTerm, upc, page, resultMax, sortType, brandToFilter, categoryToFilter, departmentToFilter,
                getUserFromSession(request, response));
        		 
        // Data required for filtering: Brands
        Set<Brand> brands = productService.getBrands();
        List<FilterOption> brandList = new ArrayList<FilterOption>();
        Iterator<Brand> bit = brands.iterator();
        while (bit.hasNext()) {
            Brand brand = bit.next();
            FilterOption option = new FilterOption();
            option.setId(brand.getId());
            option.setLabel(brand.getName());
            brandList.add(option);
        }
        
        Collections.sort(brandList,filterComparator);

        // Data required for filtering: Categories
        Set<Category> categories = productService.getCategories();
        List<FilterOption> categoryList = new ArrayList<FilterOption>();
        Iterator<Category> cit = categories.iterator();
        while (cit.hasNext()) {
            Category category = cit.next();
            FilterOption option = new FilterOption();
            option.setId(category.getId());
            option.setLabel(category.getName());
            categoryList.add(option);
        }

        Collections.sort(categoryList,filterComparator);

        // Data required for filtering: Departments
        Set<Department> departments = productService.getDepartments();
        List<FilterOption> departmentList = new ArrayList<FilterOption>();
        Iterator<Department> dit = departments.iterator();
        while (dit.hasNext()) {
            Department department = dit.next();
            FilterOption option = new FilterOption();
            option.setId(department.getId());
            option.setLabel(department.getName());
            departmentList.add(option);
        }

        Collections.sort(departmentList, filterComparator);

        SearchResult data = null;
        data = new SearchResult();
        data.setTotalResultCount(productService.getRecentSearchTotalCount());
        data.setQuery(searchTerm);
//        data.setProductsFromModel(products);
        data.setProductIds(products);
        data.setBrands(brandList);
        data.setCategories(categoryList);
        data.setDepartments(departmentList);
        data.setDidYouMean(productService.getSpellingSuggestion());
        data.setDefaultSortOptions();
        
        setResponseMessage(model, data, user);
        //Use below at later time.
        //LOG.debug(ContentFactory.getInstance().getStore().getSearchPageSortOptions());
        return model;
    }

    private ModelAndView autocomplete(HttpServletRequest request, HttpServletResponse response, SessionUser user, ModelAndView model)
            throws JsonException {
        String searchTerm = request.getParameter("searchTerm");
        LOG.debug("Prefix received: " + searchTerm);
        
        if (null != searchTerm) {
            searchTerm = searchTerm.trim();
        }
        
        ProductServiceImpl productService = new ProductServiceImpl();
        List<String> suggestions = productService.getAutoSuggestions(searchTerm, request);

        AutoComplete data = new AutoComplete();
        data.setSuggestions(suggestions);
        data.setPrefix(searchTerm);

        setResponseMessage(model, data, user);
        return model;
    }
}
