package com.freshdirect.mobileapi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.catalog.model.CatalogInfo;
import com.freshdirect.mobileapi.catalog.model.CatalogInfo.CatalogId;
import com.freshdirect.mobileapi.catalog.model.CatalogKey;
import com.freshdirect.mobileapi.catalog.model.SortOptionInfo;
import com.freshdirect.mobileapi.controller.data.AllProductsResult;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.CatalogInfoResult;
import com.freshdirect.mobileapi.controller.data.CatalogKeyResult;
import com.freshdirect.mobileapi.controller.data.GlobalNavResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.SortOptionResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.controller.data.response.BrowsePageResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.DepartmentSection;
import com.freshdirect.mobileapi.model.FDGroup;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.mobileapi.util.ListPaginator;

public class BrowseController extends BaseController {

	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(BrowseController.class);

    private static final String ACTION_GET_DEPARTMENTS = "getDepartments";
    private static final String ACTION_GET_CATEGORIES = "getCategories";
    private static final String ACTION_GET_CATEGORYCONTENT = "getCategoryContent";
    private static final String ACTION_GET_CATEGORYCONTENT_PRODUCTONLY = "getCategoryContentProductOnly";
    private static final String ACTION_GET_GROUP_PRODUCTS = "getGroupProducts";
    private static final String ACTION_GET_ALL_PRODUCTS = "getAllProducts";
    private static final String ACTION_NAVIGATION ="navigation";
    private static final String ACTION_GET_ALL_CATALOG_KEYS = "getAllCatalogKeys";
    private static final String ACTION_GET_CATALOG_FOR_ADDRESS="getCatalogForAddress";
    private static final String ACTION_GET_CATALOG_FOR_CATALOG_KEY = "getCatalogForKey";
    private static final String ACTION_GET_CATALOG_ID_FOR_ADDRESS="getCatalogIdForAddress";
    private static final String ACTION_GET_CATALOG_KEY_FOR_ADDRESS="getCatalogKeyForAddress";
    private static final String ACTION_GET_CATALOG_KEY_FOR_SESSION="getCatalogKeyForCurrentSession";
    private static final String ACTION_GET_SORT_OPTIONS_FOR_CATEGORY = "getSortOptionsForCategory";
    private static final String ACTION_GET_ALL_PRODUCTS_EX = "getAllProductsEX";

    @Override
	protected boolean validateUser() {
		return false;
	}

	/* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {
    	String postData = getPostData(request, response);
    	long startTime=System.currentTimeMillis();
    	LOG.debug("BrowseController PostData received: [" + postData + "]");

        if (isCheckLoginStatusEnable(request)) {
            if (ACTION_GET_CATEGORIES.equals(action)) {
                BrowsePageResponse res = BrowseUtil.getBrowseResponse(user, request);
                populateResponseWithEnabledAdditionsForWebClient(user, res, request, null);
                setResponseMessage(model, res, user);
                long endTime = System.currentTimeMillis();
                LOG.debug(((endTime - startTime) / 1000) + " seconds");
                return model;
            }
        }

        BrowseQuery requestMessage = null;
        if (StringUtils.isNotEmpty(postData)) {
            requestMessage = parseRequestObject(request, response, BrowseQuery.class);
        }
        if (user == null && requestMessage != null) {
            user = fakeUser(request.getSession(), requestMessage);
        } else if (user == null) {
            user = fakeUser(request.getSession());
        }

    	// Retrieving any possible payload
        
        BrowseResult result = new BrowseResult();
        
        if (ACTION_NAVIGATION.equals(action)) {
            GlobalNavResult res = new GlobalNavResult();
            StoreModel store = ContentFactory.getInstance().getStore();
            boolean isExtraResponse = isCheckLoginStatusEnable(request);
            List<Department> departments = new ArrayList<Department>();
            for (DepartmentModel storeDepartment : store.getDepartments()) {
                if (storeDepartment.getContentKey() != null && !storeDepartment.isHidden() && !storeDepartment.isHideIphone()) {
                    // Add logic to populate departmentSections
                    List<DepartmentSection> departmentSections = BrowseUtil.getDepartmentSections(user, storeDepartment, isExtraResponse);
                    if (!departmentSections.isEmpty()) {
                        Department department = Department.wrap(storeDepartment);
                        department.setSections(departmentSections);
                        departments.add(department);
                    }
                }
            }
            res.setDepartments(departments);
            setResponseMessage(model, res, user);
            return model;
        }

        if(requestMessage != null) {
	        if (ACTION_GET_DEPARTMENTS.equals(action)) {
	           StoreModel store = ContentFactory.getInstance().getStore();
	           if(store != null) {
	        	   List<DepartmentModel> storeDepartments = store.getDepartments();
	        	  // storeDepartments.get(0).getCategories().get(0).getSubcategories()

	        	   List<Department> departments = new ArrayList<Department>();
	        	   if(storeDepartments != null) {
		        	   for(DepartmentModel storeDepartment : storeDepartments) {
		        		   if(storeDepartment.getContentKey() != null
		        				   && !storeDepartment.isHidden()
		        				   && !storeDepartment.isHideIphone()) {
		        			   departments.add(Department.wrap(storeDepartment));
		        		   }
		        	   }
		        	   
	        	   }
	        	   ListPaginator<com.freshdirect.mobileapi.model.Department> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Department>(
	        			   departments, requestMessage.getMax());
	        	   result.setDepartments(paginator.getPage(requestMessage.getPage()));
	        	   result.setTotalResultCount(result.getDepartments() != null ? result.getDepartments().size() : 0);
	           }
	        } else if(ACTION_GET_ALL_PRODUCTS.equals(action)){
	        	
	        	
	        	AllProductsResult res = new AllProductsResult();
	        	//get the list of products (this will be done recursively) 
	        	List<Product> products =  BrowseUtil.getAllProducts(requestMessage, user, request);
	        	//populate the response with the products...
	        	res.setProductsFromModel(products);
	        	//set the response and return the json.
	        	setResponseMessage(model, res, user);
	        	long endTime=System.currentTimeMillis();
	        	LOG.debug(((endTime-startTime)/1000)+" seconds");
	            return model;
	        	
	        	
            } else if (ACTION_GET_CATEGORIES.equals(action) || ACTION_GET_CATEGORYCONTENT.equals(action) || ACTION_GET_CATEGORYCONTENT_PRODUCTONLY.equals(action)) {
                Message res = BrowseUtil.getCategories(requestMessage, user, request);
                setResponseMessage(model, res, user);
                long endTime = System.currentTimeMillis();
                LOG.debug(((endTime - startTime) / 1000) + " seconds");
                return model;
	        } else if (ACTION_GET_GROUP_PRODUCTS.equals(action)) {
	            final boolean isWebRequest = isCheckLoginStatusEnable(request);
	        	List<Product> products = FDGroup.getGroupScaleProducts(requestMessage.getGroupId(), requestMessage.getGroupVersion(), user, isWebRequest);
	        	result.setProductsFromModel(products);
	        }else if(ACTION_GET_CATALOG_FOR_ADDRESS.equals(action)){
	        	
	        
	        	CatalogInfoResult res = new CatalogInfoResult();
	        	//get the list of products (this will be done recursively) 
	        	CatalogInfo catalogInfo =  BrowseUtil.__getAllProducts(requestMessage, user);
	        	//populate the response with the products...
	        	res.setCatalogInfo(catalogInfo);
	        	//set the response and return the json.
	        	setResponseMessage(model, res, user);
	        	long endTime=System.currentTimeMillis();
	        	LOG.debug(((endTime-startTime)/1000)+" seconds");
	            return model;
	        
	        } else if (ACTION_GET_CATALOG_FOR_CATALOG_KEY.equals(action)){
	        	
	        	CatalogInfoResult res = new CatalogInfoResult();
	        	//get the list of products (this will be done recursively) 
	        	CatalogInfo catalogInfo =  BrowseUtil.__getAllProducts(requestMessage, user);
	        	//populate the response with the products...
	        	res.setCatalogInfo(catalogInfo);
	        	//set the response and return the json.
	        	setResponseMessage(model, res, user);
//	        	long endTime=System.currentTimeMillis();
//	        	LOG.debug(((endTime-startTime)/1000)+" seconds");
	            return model;
	        	
	        } else if(ACTION_GET_CATALOG_ID_FOR_ADDRESS.equals(action)){
	        	
	        
	        	CatalogInfoResult res = new CatalogInfoResult();
	        	CatalogInfo catalogInfo =  BrowseUtil.getCatalogInfo(requestMessage, user);
	        	res.setCatalogInfo(catalogInfo);
	        	setResponseMessage(model, res, user);
	            return model;
	            
	        } else if(ACTION_GET_CATALOG_KEY_FOR_ADDRESS.equals(action)){

	        	CatalogKeyResult res = new CatalogKeyResult();
	        	CatalogInfo ci = null;
	        	
	        	if(requestMessage != null && requestMessage.getZipCode() != null && requestMessage.getZipCode().trim().length() > 0) {
	        		ci = BrowseUtil.getCatalogInfo(requestMessage, user);
	        	} else {
	        		ci = BrowseUtil.getCatalogInfo(user);
	        	}
	        	CatalogId cid = ci.getKey();
	        	CatalogKey ck = new CatalogKey();
	        	ck.seteStore(cid.geteStore());
	        	ck.setPlantId(Long.parseLong(cid.getPlantId()));
	        	ck.setPricingZone(cid.getPricingZone());
	        	res.setKey(ck.toString());
	        	setResponseMessage(model, res, user);
	        	return model;
	        } else if(ACTION_GET_CATALOG_KEY_FOR_SESSION.equals(action)){

	        	CatalogKeyResult res = new CatalogKeyResult();
	        	
	        	CatalogInfo ci = BrowseUtil.getCatalogInfo(user);
	        	CatalogId cid = ci.getKey();
	        	CatalogKey ck = new CatalogKey();
	        	ck.seteStore(cid.geteStore());
	        	ck.setPlantId(Long.parseLong(cid.getPlantId()));
	        	ck.setPricingZone(cid.getPricingZone());
	        	res.setKey(ck.toString());
	        	setResponseMessage(model, res, user);
	        	return model;
	        } else if(ACTION_GET_SORT_OPTIONS_FOR_CATEGORY.equals(action)){
	        	//List<SortType> optionList = BrowseUtil.getSortOptionsForCategory(requestMessage, user, request);
	        	//SortOptionResult res = new SortOptionResult();
	        	//res.setSortOptions(optionList);
	        	SortOptionInfo options = BrowseUtil.getSortOptionsForCategory(requestMessage, user, request);
	        	SortOptionResult res = new SortOptionResult();
	        	res.setSortOptionInfo(options);
	        	setResponseMessage(model, res, user);
	        	return model;
	        } else if(ACTION_GET_ALL_PRODUCTS_EX.equals(action)){
	        	AllProductsResult res = new AllProductsResult	();
	        	//get the list of products (this will be done recursively) 
	        	List<String> products =  BrowseUtil.getAllProductsEX(requestMessage, user, request);
	        	//populate the response with the products...
	        	res.setProductIds(products);
	        	//set the response and return the json.
	        	setResponseMessage(model, res, user);
	        	long endTime=System.currentTimeMillis();
	        	LOG.debug(((endTime-startTime)/1000)+" seconds");
	            return model;
            }
        } else {
        	if (ACTION_GET_ALL_CATALOG_KEYS.equals(action)){
	        	//TODO: actually generate list of CatalogKeys;
	        	CatalogKeyResult res = new CatalogKeyResult();
	        	res.setKeyList(BrowseUtil.getAllFDXCatalogKeys());
	        	setResponseMessage(model, res, user);
	        	return model;
	        } 
        }

        setResponseMessage(model, result, user);
        long endTime=System.currentTimeMillis();
    	LOG.debug(((endTime-startTime)/1000)+" seconds");
        return model;
    }

}
