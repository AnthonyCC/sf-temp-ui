package com.freshdirect.mobileapi.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.ItemGrabberTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ItemSorterTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.LayoutManagerWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings;

/**
 * @author Sivachandar
 *
 */
public class BrowseController extends BaseController {
    private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(BrowseController.class);


    private static final String ACTION_GET_DEPARTMENTS = "getDepartments";

    private static final String ACTION_GET_CATEGORIES = "getCategories";

    private static final String ACTION_GET_CATEGORYCONTENT = "getCategoryContent";

        
    
    /* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {
    	
    	// Retrieving any possible payload
        String postData = getPostData(request, response);
        BrowseQuery requestMessage = null;
        
        LOG.debug("PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            requestMessage = parseRequestObject(request, response, BrowseQuery.class);            
        }
        BrowseResult result = new BrowseResult();
        
        if(requestMessage != null) {
	        if (ACTION_GET_DEPARTMENTS.equals(action)) {
	           StoreModel store = ContentFactory.getInstance().getStore();
	           if(store != null) {
	        	   List<DepartmentModel> storeDepartments = store.getDepartments();
	        	   Set<String> configuredDepartments =  MobileApiProperties.getConfiguredDepartments();
	        	   
	        	   List<Department> departments = new ArrayList<Department>();
	        	   if(storeDepartments != null) {
		        	   for(DepartmentModel storeDepartment : storeDepartments) {
		        		   if(storeDepartment.getContentKey() != null 
		        				   && configuredDepartments.contains(storeDepartment.getContentKey().getId())
		        				   && !storeDepartment.isHidden()) {
		        			   departments.add(Department.wrap(storeDepartment));
		        		   }
		        	   }
	        	   }
	        	   ListPaginator<com.freshdirect.mobileapi.model.Department> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Department>(
	        			   departments, requestMessage.getMax());
	        	   result.setDepartments(paginator.getPage(requestMessage.getPage()));
	        	   result.setTotalResultCount(result.getDepartments() != null ? result.getDepartments().size() : 0);
	           }
	        } else if (ACTION_GET_CATEGORIES.equals(action)) {
	        	ContentNodeModel departmentNode = ContentFactory.getInstance().getContentNode(requestMessage.getDepartment());
	        	if(departmentNode instanceof DepartmentModel) {
	        		List<CategoryModel> storeCategories = ((DepartmentModel)departmentNode).getCategories();
	        		List<Category> categories = getCategories(storeCategories);
	        		
	        		ListPaginator<com.freshdirect.mobileapi.model.Category> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Category>(
	        				categories, requestMessage.getMax());
		        	
	        		result.setCategories(paginator.getPage(requestMessage.getPage()));
	        		result.setTotalResultCount(result.getCategories() != null ? result.getCategories().size() : 0);
	        	}
	        	
	        } else if (ACTION_GET_CATEGORYCONTENT.equals(action)) {
	        	ContentNodeModel categoryNode = ContentFactory.getInstance().getContentNode(requestMessage.getCategory());
	        	if(categoryNode instanceof CategoryModel) {
	        		List<CategoryModel> storeCategories = ((CategoryModel)categoryNode).getSubcategories();
	        		if(storeCategories.size() > 0) {
	        			List<Category> categories = getCategories(storeCategories);
		        		ListPaginator<com.freshdirect.mobileapi.model.Category> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Category>(
		        				categories, requestMessage.getMax());
			        	
		        		result.setCategories(paginator.getPage(requestMessage.getPage()));
		        		result.setTotalResultCount(result.getCategories() != null ? result.getCategories().size() : 0);
	        		} else {
	        			List<Product> products = grabProductsForCategory(request, ((CategoryModel)categoryNode), user, requestMessage);
	        			
		        		ListPaginator<com.freshdirect.mobileapi.model.Product> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Product>(
		        				products, requestMessage.getMax());
			        	
		        		result.setProductsFromModel(paginator.getPage(requestMessage.getPage()));
		        		result.setTotalResultCount(result.getProducts() != null ? result.getProducts().size() : 0);
	        		}
	        	}
	        }
        }
        
        setResponseMessage(model, result, user);
        return model;
    }
    
    private List<Category> getCategories(List<CategoryModel> storeCategories) {
    	List<Category> categories = new ArrayList<Category>();
		if(storeCategories != null) {
			for(CategoryModel storeCategory : storeCategories) {
				categories.add(Category.wrap(storeCategory));
			}
		}
		return categories;
    }
    private List<Product> grabProductsForCategory(HttpServletRequest request, ContentNodeModel currentFolder
    													, SessionUser user, BrowseQuery requestMessage) throws FDException {
    	
        List contents = new ArrayList();
        request.setAttribute("sortBy", requestMessage.getSortBy());
        request.setAttribute("nutritionName", requestMessage.getNutritionName());
        
        LayoutManagerWrapper layoutManagerTagWrapper = new LayoutManagerWrapper(user);        
        Settings layoutManagerSetting = layoutManagerTagWrapper.getLayoutManagerSettings(currentFolder);

        //We have layout manager
        if (layoutManagerSetting != null) {
            ItemGrabberTagWrapper itemGrabberTagWrapper = new ItemGrabberTagWrapper(user.getFDSessionUser());
            contents = itemGrabberTagWrapper.getProducts(layoutManagerSetting, currentFolder);

            ItemSorterTagWrapper sortTagWrapper = new ItemSorterTagWrapper(user);
            sortTagWrapper.sort(contents, layoutManagerSetting.getSortStrategy());

        } else {
            //Error happened. It's a internal error so don't expose to user. just log and return empty list
            ActionResult layoutResult = (ActionResult) layoutManagerTagWrapper.getResult();
            if (layoutResult.isFailure()) {
                Collection<ActionError> errors = layoutResult.getErrors();
                for (ActionError error : errors) {
                    LOG.error("Error while trying to retrieve whats good product: ec=" + error.getType() + "::desc="
                            + error.getDescription());
                }
            }
        }
        List<Product> products = new ArrayList<Product>();
        for (Object content : contents) {
            if (content instanceof ProductModel) {
                try {
                	products.add(Product.wrap((ProductModel) content, user.getFDSessionUser().getUser()));
                } catch (Exception e) {
                    //Don't let one rotten egg ruin it for the bunch
                    LOG.error("ModelException encountered. Product ID=" + ((ProductModel) content).getFullName(), e);
                }
            }
        }
        return products;
    }

}
