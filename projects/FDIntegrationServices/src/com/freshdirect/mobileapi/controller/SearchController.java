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

import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.DYFUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.SearchResult;
import com.freshdirect.mobileapi.controller.data.SearchWebResult;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;
import com.freshdirect.mobileapi.controller.data.request.SearchWebQuery;
import com.freshdirect.mobileapi.controller.data.response.AutoComplete;
import com.freshdirect.mobileapi.controller.data.response.BrowsePageResponse;
import com.freshdirect.mobileapi.controller.data.response.FilterOption;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.AdProducts;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.comparator.FilterOptionLabelComparator;
import com.freshdirect.mobileapi.model.tagwrapper.SmartSearchTagWrapper;
import com.freshdirect.mobileapi.service.ProductServiceImpl;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.mobileapi.util.SortType;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductModelBrandAdsAdapter;
import com.freshdirect.storeapi.content.SearchResults;
import com.freshdirect.storeapi.content.SortStrategyType;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.MenuDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SectionDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringFlow;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.filtering.SearchResultsUtil;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.search.SearchService;
import com.freshdirect.webapp.search.unbxd.UnbxdServiceUnavailableException;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.RequestUtil;

public class SearchController extends BaseController {

    private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(SearchController.class);

    private static final String AUTOCOMPLETE_ACTION = "autocomplete";
    private static final String ACTION_SEARCH_EX = "searchEX";
    private static final String ACTION_SEARCH_WEB = "searchWeb";

    @Override
    protected boolean validateUser() {
        return false;
    }

    @Override
    /**
     * There are two ways to access this services. The basic way is just sending the search term, page and page size(max) in the querystring as a GET request. Default values:
     * searchTerm is an empty string that will return all products, page default value is 1, max default value is 25. The payload is checked in case something was send through POST
     * and will overwrite any value from query string.
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action, SessionUser user)
            throws FDException, ServiceException, NoSessionException, JsonException {
        if (user == null) {
            user = fakeUser(request.getSession());
        }

        if (AUTOCOMPLETE_ACTION.equalsIgnoreCase(action)) {
            autocomplete(request, response, user, model);
        } else if (ACTION_SEARCH_EX.equalsIgnoreCase(action)) {
            searchEX(request, response, model, user);
        } else if (ACTION_SEARCH_WEB.equalsIgnoreCase(action)){
            searchWeb(request, response, model, user);
        } else { // default go to search
            search(request, response, model, user);
        }
        return model;
    }

    private ModelAndView search(HttpServletRequest request, HttpServletResponse response, ModelAndView model, SessionUser user)
            throws FDException, ServiceException, NoSessionException, JsonException {

        if (isExtraResponseRequested(request)) {
            BrowsePageResponse data = BrowseUtil.getBrowseResponse(user, request);
            populateResponseWithEnabledAdditionsForWebClient(user, data, request, null);
            setResponseMessage(model, data, user);
        } else {
            // Default values retrieved from GET request
            String searchTerm = request.getParameter("searchTerm");
            String upc = request.getParameter("upc");
            int page = (StringUtils.isNumeric(request.getParameter("page")) ? Integer.parseInt(request.getParameter("page")) : 1);
            int resultMax = (StringUtils.isNumeric(request.getParameter("max")) ? Integer.parseInt(request.getParameter("max")) : 25);
            SortType sortType = SortType.RELEVANCY; // Default sort type
            String brandToFilter = null;
            String categoryToFilter = null;
            String departmentToFilter = null;
            String pageType = request.getParameter("pageType");
            String searchParams = request.getParameter("searchParams");
            String pdUserId = request.getParameter("pdUserId");
            String lat = request.getParameter("lat");
            String platform = request.getParameter("platform");

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
                pageType = requestMessage.getPageType();
                searchParams = requestMessage.getSearchParams();
                pdUserId = requestMessage.getPdUserId();
                lat = requestMessage.getLat();
                platform = requestMessage.getPlatform();
            }

            SearchResult data = new SearchResult();

            try {
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
                SmartSearchTagWrapper wrapper = new SmartSearchTagWrapper(user);
                wrapper.setRequestUrl(RequestUtil.getFullRequestUrl(request));
                setContextHeaders(request, wrapper);
                List<Product> products = productService.search(searchTerm, upc, page, resultMax, sortType, brandToFilter, categoryToFilter, departmentToFilter,
                        getUserFromSession(request, response), request, wrapper);

                List<Product> favProducts = new ArrayList<Product>();
                favProducts.clear();
                if(!products.isEmpty()){
                	for(Product product : products){
                		ProductModel productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get("Product:" + product.getProductId()));
                		if(productModel!=null && productModel.getContentKey()!=null){
                			boolean isYourFave = DYFUtil.isFavorite( productModel, user.getFDSessionUser() );
                			if(isYourFave){
                				favProducts.add(product);
                			}
                		}
                	}
                }

                // search results
                if(pageType!=null && searchParams!=null){
                    final CmsFilteringNavigator nav = CmsFilteringNavigator.createInstance(request, user.getFDSessionUser());
                    SearchResults sr = SearchService.getInstance().searchProducts(searchTerm, nav.getRequestCookies(), user.getFDSessionUser(), nav.getRequestUrl(), nav.getReferer());

                    SearchResultsUtil.getHLBrandProductAdProducts(sr, nav, user.getFDSessionUser(), platform, lat, pdUserId);
                    if(!sr.getAdProducts().isEmpty()){
                    	List<AdProducts> adProductList = new ArrayList<AdProducts>();
                    	for(FilteringSortingItem<ProductModel> adproduct : sr.getAdProducts()){
                    		ProductModelBrandAdsAdapter pdma =  (ProductModelBrandAdsAdapter)adproduct.getNode();
                    		if(pdma!=null && pdma.getProductModel()!=null){
        	            		AdProducts adproductsl = new AdProducts(pdma.getProductModel().getContentName(),pdma.getClickBeacon(),pdma.getImpBeacon());
        	            		adProductList.add(adproductsl);
                    		}
                    	}
                    	data.setAdProducts(adProductList);
                    	data.setPageBeacon(sr.getPageBeacon());
                    }
                }

                if (request.getSession().getAttribute(SessionName.APPLICATION) != null
                        && EnumTransactionSource.FRIDGE.getCode().equalsIgnoreCase((String) request.getSession().getAttribute(SessionName.APPLICATION))) {
                    ListPaginator<Product> paginator = new ListPaginator<Product>(products, resultMax);
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

                Collections.sort(brandList, filterComparator);

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

                Collections.sort(categoryList, filterComparator);

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
                data.setTotalResultCount(productService.getRecentSearchTotalCount());
                data.setQuery(searchTerm);
                data.setProductsFromModel(products);
                data.setFavProductsFromModel(favProducts);
                data.setBrands(brandList);
                data.setCategories(categoryList);
                data.setDepartments(departmentList);
                data.setDidYouMean(productService.getSpellingSuggestion());
                data.setDefaultSortOptions();
                // Use below at later time.
                // LOG.debug(ContentFactory.getInstance().getStore().getSearchPageSortOptions());
                setResponseMessage(model, data, user);
            } catch (UnbxdServiceUnavailableException exception) {
                LOG.error(exception);

                data.setTotalResultCount(0);
                data.setQuery(searchTerm);
                data.setProductsFromModel(Collections.<Product> emptyList());
                data.setFavProductsFromModel(Collections.<Product> emptyList());
                data.setBrands(Collections.<FilterOption> emptyList());
                data.setCategories(Collections.<FilterOption> emptyList());
                data.setDepartments(Collections.<FilterOption> emptyList());
                data.setDidYouMean("");
                data.setDefaultSortOptions();

                setResponseMessage(model, data, user);
            } catch (InvalidFilteringArgumentException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
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
        SortType sortType = SortType.RELEVANCY; // Default sort type
        String brandToFilter = null;
        String categoryToFilter = null;
        String departmentToFilter = null;
        SearchQuery requestMessage = null;
        String pageType = request.getParameter("pageType");
        String searchParams = request.getParameter("searchParams");
        String pdUserId = request.getParameter("pdUserId");
        String lat = request.getParameter("lat");
        String platform = request.getParameter("platform");

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
            pageType = requestMessage.getPageType();
            searchParams = requestMessage.getSearchParams();
            pdUserId = requestMessage.getPdUserId();
            lat = requestMessage.getLat();
            platform = requestMessage.getPlatform();
        }


        SearchResult data = new SearchResult();

        try {
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

        SmartSearchTagWrapper wrapper = new SmartSearchTagWrapper(user);
        wrapper.setRequestUrl(request.getRequestURL().toString());
        setContextHeaders(request, wrapper);
        List<String> products = productService.searchProductIds(searchTerm, upc, page, resultMax, sortType, brandToFilter, categoryToFilter, departmentToFilter,
                getUserFromSession(request, response), request, wrapper);

        List<String> favProducts = new ArrayList<String>();
        favProducts.clear();
        if(!products.isEmpty()){
        	for(String product : products){
        		ProductModel productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get(ContentType.Product, product));
        		if(productModel!=null && productModel.getContentKey()!=null){
        			boolean isYourFave = DYFUtil.isFavorite( productModel, user.getFDSessionUser() );
        			if(isYourFave){
        				favProducts.add(product);
        			}
        		}
        	}
        }

        // search results
        if(pageType!=null && searchParams!=null){
            final CmsFilteringNavigator nav = CmsFilteringNavigator.createInstance(request, user.getFDSessionUser());
            SearchResults sr = SearchService.getInstance().searchProducts(searchTerm, nav.getRequestCookies(), user.getFDSessionUser(), nav.getRequestUrl(), nav.getReferer());

            SearchResultsUtil.getHLBrandProductAdProducts(sr, nav, user.getFDSessionUser(), platform, lat, pdUserId);
            if(!sr.getAdProducts().isEmpty()){
            	List<AdProducts> adProductList = new ArrayList<AdProducts>();
            	for(FilteringSortingItem<ProductModel> adproduct : sr.getAdProducts()){
            		ProductModelBrandAdsAdapter pdma =  (ProductModelBrandAdsAdapter)adproduct.getNode();
            		if(pdma!=null && pdma.getProductModel()!=null){
	            		AdProducts adproductsl = new AdProducts(pdma.getProductModel().getContentName(),pdma.getClickBeacon(),pdma.getImpBeacon());
	            		adProductList.add(adproductsl);
            		}
            	}
            	data.setAdProducts(adProductList);
            	data.setPageBeacon(sr.getPageBeacon());
            }
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

        Collections.sort(brandList, filterComparator);

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

        Collections.sort(categoryList, filterComparator);

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

        data.setTotalResultCount(productService.getRecentSearchTotalCount());
        data.setQuery(searchTerm);
        // data.setProductsFromModel(products);
        data.setProductIds(products);
        data.setFavProductIds(favProducts);
        data.setBrands(brandList);
        data.setCategories(categoryList);
        data.setDepartments(departmentList);
        data.setDidYouMean(productService.getSpellingSuggestion());
        data.setDefaultSortOptions();

        } catch (UnbxdServiceUnavailableException e) {
            LOG.error(e);

            data.setTotalResultCount(0);
            data.setQuery(searchTerm);
            data.setProductIds(Collections.<String>emptyList());
            data.setFavProductIds(Collections.<String>emptyList());
            data.setBrands(Collections.<FilterOption> emptyList());
            data.setCategories(Collections.<FilterOption> emptyList());
            data.setDepartments(Collections.<FilterOption> emptyList());
            data.setDidYouMean("");
            data.setDefaultSortOptions();
        } catch (InvalidFilteringArgumentException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

        setResponseMessage(model, data, user);
        // Use below at later time.
        // LOG.debug(ContentFactory.getInstance().getStore().getSearchPageSortOptions());
        return model;
    }

    private ModelAndView autocomplete(HttpServletRequest request, HttpServletResponse response, SessionUser user, ModelAndView model) throws JsonException {
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
    
    private ModelAndView searchWeb(HttpServletRequest request, HttpServletResponse response, ModelAndView model, SessionUser user) throws JsonException {
        SearchWebResult result = new SearchWebResult();

        String postData = getPostData(request, response);
        LOG.debug("PostData received: [" + postData + "]");

        if (StringUtils.isNotEmpty(postData)) {
            SearchWebQuery requestMessage = parseRequestObject(request, response, SearchWebQuery.class);

            try {
                FDSessionUser sessionUser = user.getFDSessionUser();
                final CmsFilteringNavigator navigator = new CmsFilteringNavigator();
                navigator.setPageTypeType(FilteringFlowType.SEARCH);
                navigator.setSearchParams(requestMessage.getQuery());
                navigator.setSortBy(NVL.apply(requestMessage.getSortBy(), SortStrategyType.RECENCY.name()));
                navigator.setOrderAscending(requestMessage.isOrderAscending());
                navigator.setRequestFilterParams(requestMessage.getFilterByIds());
                navigator.setActivePage(requestMessage.getPage());
                navigator.setPageSize(requestMessage.getMax() > 0 ? requestMessage.getMax() : FDStoreProperties.getSearchPageSize());

                final CmsFilteringFlowResult flow = CmsFilteringFlow.getInstance().doFlow(navigator, sessionUser);
                BrowseData browseDataPrototype = flow.getBrowseDataPrototype();

                SectionDataCointainer sectionDataContainer = browseDataPrototype.getSections();
                List<ProductData> products = new ArrayList<ProductData>();
                BrowseUtil.loadProductsFromSections(sectionDataContainer.getSections(), products);
                List<String> productIds = new ArrayList<String>();
                List<String> favProductIds = new ArrayList<String>();
                for (ProductData productData : products) {
                    productIds.add(productData.getProductId());
                    if (DYFUtil.isFavorite(ContentKeyFactory.get(productData.getCMSKey()), sessionUser)) {
                        favProductIds.add(productData.getProductId());
                    }
                }

                MenuDataCointainer menuBoxes = browseDataPrototype.getMenuBoxes();
                for (MenuBoxData menuBox : menuBoxes.getMenuBoxes()) {
                    result.getFilterGroups().add(BrowseUtil.createFilterGroup(menuBox));
                }

                result.setSortOptions(browseDataPrototype.getSortOptions().getSortOptions());
                result.setTotalResultCount(browseDataPrototype.getPager().getItemCount());
                result.setPager(browseDataPrototype.getPager());
                result.setQuery(requestMessage.getQuery());
                result.setProductIds(productIds);
                result.setFavProductIds(favProductIds);

                List<String> suggestions = browseDataPrototype.getSearchParams().getSuggestions();
                if (!suggestions.isEmpty()) {
                    result.setDidYouMean(suggestions.get(0));
                }
            } catch (FDResourceException e) {
                result.addErrorMessage(e.getMessage());
                LOG.error(e);
            } catch (InvalidFilteringArgumentException e) {
                result.addErrorMessage(e.getMessage());
                LOG.error(e);
            } catch (UnbxdServiceUnavailableException e) {
                result.addErrorMessage(e.getMessage());
                LOG.error(e);
            } catch (FDNotFoundException e) {
                result.addErrorMessage(e.getMessage());
                LOG.error(e);
            }
        }
        setResponseMessage(model, result, user);
        return model;
    }
}
