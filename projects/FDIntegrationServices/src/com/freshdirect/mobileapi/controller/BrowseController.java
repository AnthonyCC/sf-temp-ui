package com.freshdirect.mobileapi.controller;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategorySectionModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.fdstore.content.TagModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.catalog.model.CatalogInfo;
import com.freshdirect.mobileapi.catalog.model.SortOptionInfo;
import com.freshdirect.mobileapi.controller.data.AllProductsResult;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.CatalogInfoResult;
import com.freshdirect.mobileapi.controller.data.CatalogKeyResult;
import com.freshdirect.mobileapi.controller.data.GlobalNavResult;
import com.freshdirect.mobileapi.controller.data.SortOptionResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.DepartmentSection;
import com.freshdirect.mobileapi.model.FDGroup;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.mobileapi.util.SortType;

/**
 * @author Sivachandar
 *
 */
public class BrowseController extends BaseController {

	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(BrowseController.class);


    private static final String ACTION_GET_DEPARTMENTS = "getDepartments";

    private static final String ACTION_GET_CATEGORIES = "getCategories";

    private static final String ACTION_GET_CATEGORYCONTENT = "getCategoryContent";

    private static final String ACTION_GET_CATEGORYCONTENT_PRODUCTONLY = "getCategoryContentProductOnly";

    private static final String ACTION_GET_GROUP_PRODUCTS = "getGroupProducts";
    
    private static final String ACTION_GET_ALL_PRODUCTS = "getAllProducts";

    private static final String ACTION_NAVIGATION ="navigation";
    
    private static final String ACTION_GET_CATALOG_FOR_ADDRESS="getCatalogForAddress";

    private static final String ACTION_GET_CATALOG_FOR_CATALOG_KEY = "getCatalogForKey";
    
    private static final String ACTION_GET_CATALOG_ID_FOR_ADDRESS="getCatalogIdForAddress";

    private static final String ACTION_GET_CATALOG_KEY_FOR_ADDRESS="getCatalogKeyForAddress";

    private static final String ACTION_GET_SORT_OPTIONS_FOR_CATEGORY = "getSortOptionsForCategory";
    
    private static final String ACTION_GET_ALL_PRODUCTS_EX = "getAllProductsEX";

	private static final String FILTER_KEY_BRANDS = "brands";
    private static final String FILTER_KEY_TAGS = "tags";


    @Override
	protected boolean validateUser() {
		return false;
	}

    public void addSections(DepartmentModel storeDepartment, Department result) {
    	//Department sections are added here
    	//Call BrowseUtil to populate all the categories.
	   List<DepartmentSection> departmentSections = BrowseUtil.getDepartmentSections(storeDepartment);
	   result.setSections(departmentSections);
    }

	/* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {
    	String postData = getPostData(request, response);
    	long startTime=System.currentTimeMillis();
    	if (user == null) {
    		user = fakeUser(request.getSession());
    	}

    	// Retrieving any possible payload
        
        BrowseQuery requestMessage = null;

        LOG.debug("BrowseController PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            requestMessage = parseRequestObject(request, response, BrowseQuery.class);
        }
        BrowseResult result = new BrowseResult();
        
        if(ACTION_NAVIGATION.equals(action)) {
        	
        	GlobalNavResult res = new GlobalNavResult();
        	StoreModel store = ContentFactory.getInstance().getStore();
        	
        	List<Department> departments = new ArrayList<Department>();
        	Department dpt = null;
	           if(store != null) {
	        	   List<DepartmentModel> storeDepartments = store.getDepartments();
	        	 
	        	   
	        	   if(storeDepartments != null) {
	        		  
		        	   for(DepartmentModel storeDepartment : storeDepartments) {
		        		   if(storeDepartment.getContentKey() != null
		        				   && !storeDepartment.isHidden()
		        				   && !storeDepartment.isHideIphone()) {
		        			   //Add logic to populate departmentSections
		        			   dpt = Department.wrap(storeDepartment);
		        			   addSections(storeDepartment, dpt);
		        			   departments.add(dpt);		        			   
		        		   }
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
	        	
	        	
	        } else if (ACTION_GET_CATEGORIES.equals(action)
	        								|| ACTION_GET_CATEGORYCONTENT.equals(action)
	        								|| ACTION_GET_CATEGORYCONTENT_PRODUCTONLY.equals(action)) {
	        	
	        	result =  BrowseUtil.getCategories(requestMessage, user, request);
	        	
	        } else if (ACTION_GET_GROUP_PRODUCTS.equals(action)) {
	        	List<Product> products = FDGroup.getGroupScaleProducts(requestMessage.getGroupId(), requestMessage.getGroupVersion(), user);
	        	result.setProductsFromModel(products);
	        }else if(ACTION_GET_CATALOG_FOR_ADDRESS.equals(action)){
	        	
	        
	        	CatalogInfoResult res = new CatalogInfoResult();
	        	//get the list of products (this will be done recursively) 
	        	CatalogInfo catalogInfo =  BrowseUtil.__getAllProducts(requestMessage, user, request);
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
	        	CatalogInfo catalogInfo =  BrowseUtil.__getAllProducts(requestMessage, user, request);
	        	//populate the response with the products...
	        	res.setCatalogInfo(catalogInfo);
	        	//set the response and return the json.
	        	setResponseMessage(model, res, user);
//	        	long endTime=System.currentTimeMillis();
//	        	LOG.debug(((endTime-startTime)/1000)+" seconds");
	            return model;
	        	
	        } else if(ACTION_GET_CATALOG_ID_FOR_ADDRESS.equals(action)){
	        	
	        
	        	CatalogInfoResult res = new CatalogInfoResult();
	        	CatalogInfo catalogInfo =  BrowseUtil.getCatalogInfo(requestMessage, user, request);
	        	res.setCatalogInfo(catalogInfo);
	        	setResponseMessage(model, res, user);
	            return model;
	            
	        } else if(ACTION_GET_CATALOG_KEY_FOR_ADDRESS.equals(action)){

	        	CatalogKeyResult res = new CatalogKeyResult();
	        	res.setKey(BrowseUtil.getCatalogInfo(requestMessage, user, request).getKey().toString());
	        	setResponseMessage(model, res, user);
	        	return model;
	        	/*
	        	CatalogInfoResult res = new CatalogInfoResult();
	        	CatalogInfo catalogInfo =  BrowseUtil.getCatalogInfo(requestMessage, user, request);
	        	res.setCatalogInfo(catalogInfo);
	        	setResponseMessage(model, res, user);
	            return model;
	            */
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
        }

        setResponseMessage(model, result, user);
        long endTime=System.currentTimeMillis();
    	LOG.debug(((endTime-startTime)/1000)+" seconds");
        return model;
    }
    
    private class NameComparator implements Comparator<Category> {

		@Override
		public int compare(Category o1, Category o2) {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}

	}
    
    //This method Splits the categories List into sublists based on sectionHeader and sorts each alphabetically
    private List<Category> customizeCaegoryListForIpad(List<Category> categories, List<CategorySectionModel> categorySections){
    	//get the size of categorySections which we will use to create number of sublists
    	int numOfSections = categorySections.size();
    	NameComparator nameComparator = new NameComparator();
    	List<List<Category>> sublists = new ArrayList<List<Category>>();
    	
    	// Loop on the categorySections : inside loop on categories to split into sublists based on sectionHeader
    	for(int i = 0;i < numOfSections;i++){
    		List<Category> tempSublist = new ArrayList<Category>();
    		for(Category cat:categories){
    			if(cat.getSectionHeader()!=null && !cat.getSectionHeader().isEmpty() && cat.getSectionHeader().equals(categorySections.get(i).getHeadline())){
    				tempSublist.add(cat);
    			}
    		}
    		//Sort the tempSublist based on Name
    		
	        Collections.sort(tempSublist, nameComparator);
    		sublists.add(tempSublist);
    	}
    	//For Shop By sectioHeader is null
    	List<Category> temp = new ArrayList<Category>();
    	for(Category cat : categories){
    		if(cat.getSectionHeader()==null || cat.getSectionHeader().isEmpty() ){
    			temp.add(cat);
    		}
    		
    	}
    	Collections.sort(temp, nameComparator);
    	sublists.add(temp);
    	//Merge the sublists into one 
    	List<Category> sortedCategories = new ArrayList<Category>();
    	for(List<Category> tempSortedSublist: sublists){
    		sortedCategories.addAll(tempSortedSublist);
    	}
    	
    	return sortedCategories;
    	
    }

	private void addCategoryHeadline(
			List<CategorySectionModel> categorySections,
			CategoryModel categoryModel, Category category) {
	    // Simple department
	    if (categorySections.isEmpty()) {
	        if (!categoryModel.isPreferenceCategory()) {
	            final String leftNavHeader = categoryModel.getDepartment().getRegularCategoriesLeftNavBoxHeader();
                final String sectionName = isNotBlank(leftNavHeader) ? leftNavHeader : categoryModel.getDepartment().getFullName();
	            category.setSectionHeader(sectionName);
	        }
	        return;
	    }
	    // Department with sections
		for (CategorySectionModel section : categorySections) {
			for (CategoryModel c : section.getSelectedCategories()) {
				if (c.getContentName().equals(categoryModel.getContentName())) {
					category.setSectionHeader(section.getHeadline());
					return;
				}
			}
		}
	}

    private boolean passesFilter(ProductModel product,
			HttpServletRequest request) {
        return filterTags(product, request) && filterBrands(product, request);
	}

	private boolean filterBrands(ProductModel product,
			HttpServletRequest request) {
		String[] filterBrands = request.getParameterValues(FILTER_KEY_BRANDS);
    	if (filterBrands == null) {
    		return true;
    	}
    	for (String filter : filterBrands) {
			for (BrandModel brand : product.getBrands()) {
				if (StringUtils.equalsIgnoreCase(brand.getName(), filter)) return true;
			}
		}
    	return false;
	}

	private boolean filterTags(ProductModel product, HttpServletRequest request) {
		String[] filterTags = request.getParameterValues(FILTER_KEY_TAGS);
    	if (filterTags == null) {
    		return true;
    	}
    	for (String filter : filterTags) {
			for (TagModel tag : product.getAllTags()) {
				if (StringUtils.equalsIgnoreCase(tag.getName(), filter)) return true;
			}
		}
    	return false;
	}

	private boolean isEmptyProductGrabberCategory(CategoryModel category) { //APPDEV-3659 : Treat empty Product Grabber categories the same on mobile and site
    	if(category.getProductGrabbers() != null && category.getProductGrabbers().size() > 0 ) {
    		List<ProductModel> tmpProducts = category.getProducts();
    		if(tmpProducts == null || tmpProducts.size() == 0) {
    			return true;
    		}
    	}
    	return false;
    }

    @SuppressWarnings("unused")
	private List<Category> getCategories(List<CategoryModel> storeCategories) {
    	List<Category> categories = new ArrayList<Category>();
		if(storeCategories != null) {
			for(CategoryModel storeCategory : storeCategories) {
				if(storeCategory.isActive() && !storeCategory.isHideIphone()) {
					categories.add(Category.wrap(storeCategory));
				}
			}
		}
		return categories;
    }

	private Map<String, String> getQueryMap(String url) {
		Map<String, String> map = new HashMap<String, String>();
		if (url != null) {
			try {
				URI uri = new URI(url);
				String query = uri.getQuery();
				if(query != null) {
					String[] params = query.split("&");
					if(params != null) {
						for (String param : params) {
							map.put(param.split("=")[0], param.split("=")[1]);
						}
					}
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}
		return map;
	}
	
	

}
