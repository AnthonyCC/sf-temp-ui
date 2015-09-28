package com.freshdirect.mobileapi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.EnumSearchFilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.GetProductFilterTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

/**
 * @author Sivachandar
 *
 */
public class CouponBrowseController extends BaseController {
	
    private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(CouponBrowseController.class);


    private static final String ACTION_GET_DEPARTMENTS = "getDepartments";

    private static final String ACTION_GET_CATEGORIES = "getCategories";

    private static final String ACTION_GET_CATEGORYCONTENT = "getCategoryContent";
      
    private static final String ACTION_GET_CATEGORYCONTENT_PRODUCTONLY = "getCategoryContentProductOnly"; 
    
    /* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {
    	
    	// Retrieving any possible payload
        String postData = getPostData(request, response);
        BrowseQuery requestMessage = null;
        
        LOG.debug("CouponBrowseController PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            requestMessage = parseRequestObject(request, response, BrowseQuery.class);            
        }
        BrowseResult result = new BrowseResult();
                
        if(requestMessage != null && user.getFDSessionUser().isCouponsSystemAvailable()) {
        	
        	SearchResults couponProducts = FDCustomerCouponUtil.getCouponsAsSearchResults(user.getFDSessionUser(), false);
        	if(couponProducts != null && couponProducts.getProducts().size() > 0) {
        		GetProductFilterTagWrapper tag = new GetProductFilterTagWrapper(user); 
        		Map<FilteringValue, List<Object>> filterValues = new HashMap<FilteringValue, List<Object>>();
        		List<Object> actualFilter = new ArrayList<Object>();
        		Map<EnumSearchFilteringValue, Map<String, FilteringMenuItem>> domains = null;
        		
        		if (ACTION_GET_DEPARTMENTS.equals(action)) {        			       			
        			ResultBundle filterResult =  tag.getFilteredList(couponProducts, filterValues);
        			domains = (Map<EnumSearchFilteringValue, Map<String, FilteringMenuItem>>)filterResult.getExtraData(GetProductFilterTagWrapper.domainsId) ;
        			
        			List<Department> departments = new ArrayList<Department>();
        			Department _dpt = null;
        			
        			if(domains.containsKey(EnumSearchFilteringValue.DEPT)) {
        				Map<String, FilteringMenuItem> filterMenuMap = domains.get(EnumSearchFilteringValue.DEPT);
        				if(filterMenuMap != null) {
        					List<FilteringMenuItem> filters = new ArrayList<FilteringMenuItem>(filterMenuMap.values());
        					Collections.sort( filters, FilteringMenuItem.COUNT_ORDER_REV );
        					for(FilteringMenuItem menuItem : filters) {
        						ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(menuItem.getFilteringUrlValue());        			        	
        			        	if(currentFolder instanceof DepartmentModel) {
        			        		if(((DepartmentModel)currentFolder).getContentKey() != null		        				   
        			        				   && !((DepartmentModel)currentFolder).isHidden()
        			        				   && !((DepartmentModel)currentFolder).isHideIphone()) {
        			        			   _dpt = Department.wrap(((DepartmentModel)currentFolder));
        			        			   _dpt.setNoOfProducts(menuItem.getCounter());
        			        			   departments.add(_dpt);
        			        		}
        			        	}
        					}
        				}
        			}
        			
		        	ListPaginator<com.freshdirect.mobileapi.model.Department> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Department>(
		        			   departments, requestMessage.getMax());
		        	result.setDepartments(paginator.getPage(requestMessage.getPage()));
		        	result.setTotalResultCount(result.getDepartments() != null ? result.getDepartments().size() : 0);
		        	
        		} else {
        			
        			EnumSearchFilteringValue _filterDestination = null;
        			
        			if(ACTION_GET_CATEGORIES.equals(action)) {	
        				actualFilter.add(requestMessage.getDepartment());
        				_filterDestination = EnumSearchFilteringValue.CAT;
            			filterValues.put(EnumSearchFilteringValue.DEPT, actualFilter); 
        			} else {
        				actualFilter.add(requestMessage.getCategory());
        				ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(requestMessage.getCategory());        	        	
        	        	if(currentFolder instanceof CategoryModel 
        	        				&& currentFolder.getParentNode() instanceof DepartmentModel) {
        	        		_filterDestination = EnumSearchFilteringValue.SUBCAT;
                			filterValues.put(EnumSearchFilteringValue.CAT, actualFilter);
        	        	} else {
                			filterValues.put(EnumSearchFilteringValue.SUBCAT, actualFilter);
        	        	}
        			} 
        			ResultBundle filterResult = tag.getFilteredList(couponProducts, filterValues);
        			domains = (Map<EnumSearchFilteringValue, Map<String, FilteringMenuItem>>)filterResult.getExtraData(GetProductFilterTagWrapper.domainsId) ;
        			
        			List<Category> categories = new ArrayList<Category>();
        			Category _cat = null;
        			 if(_filterDestination != null && domains.containsKey(_filterDestination)) {
         				Map<String, FilteringMenuItem> filterMenuMap = domains.get(_filterDestination);
         				if(filterMenuMap != null) {
        					List<FilteringMenuItem> filteredMenu = new ArrayList<FilteringMenuItem>(filterMenuMap.values());
        					Collections.sort( filteredMenu, FilteringMenuItem.COUNT_ORDER_REV );
        					for(FilteringMenuItem menuItem : filteredMenu) {
         						ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(menuItem.getFilteringUrlValue());        			        	
         			        	if(currentFolder instanceof CategoryModel) {
         			        		if(((CategoryModel)currentFolder).getContentKey() != null		        				   
         			        				   && !((CategoryModel)currentFolder).isHidden()
         			        				   && !((CategoryModel)currentFolder).isHideIphone()
         			        				   && ((CategoryModel)currentFolder).isActive()) {
         			        				_cat = Category.wrap(((CategoryModel)currentFolder));
         			        				_cat.setNoOfProducts(menuItem.getCounter());
         			        				categories.add(_cat);
         			        		}
         			        	}
         					}
         				}
         			}
        			 
        			if(categories.size() > 0 && !ACTION_GET_CATEGORYCONTENT_PRODUCTONLY.equals(action)) {	            	
		            	ListPaginator<com.freshdirect.mobileapi.model.Category> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Category>(
		            			categories, requestMessage.getMax());
	
						result.setCategories(paginator.getPage(requestMessage.getPage()));
						result.setResultCount(result.getCategories() != null ? result.getCategories().size() : 0);	         		
						result.setTotalResultCount(categories.size());	            		         		
		            } else {
		            	List<Product> products = new ArrayList<Product>();
		            	
		            	List<FilteringSortingItem<ProductModel>> filteredProducts = (List<FilteringSortingItem<ProductModel>>)filterResult.getExtraData(GetProductFilterTagWrapper.itemsId) ;
		            	if(filteredProducts != null) {
			            	for(FilteringSortingItem<ProductModel> _filtProd : filteredProducts) {
			            		if(!_filtProd.getNode().isHideIphone() && !_filtProd.getNode().isUnavailable()) {
			            			try {
			            				products.add(Product.wrap(_filtProd.getNode(), user.getFDSessionUser().getUser(), null, EnumCouponContext.PRODUCT));
				            		} catch (Exception e) {
				                        //Don't let one rotten egg ruin it for the bunch
				                        LOG.error("CouponBrowseController ModelException encountered. Product ID=" + _filtProd.getNode().getFullName(), e);
				                    }
		                    	}
			            	}
		            	}
		            			            	
		            	ListPaginator<com.freshdirect.mobileapi.model.Product> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Product>(
		         															products, requestMessage.getMax());
		 	        	
		         		result.setProductsFromModel(paginator.getPage(requestMessage.getPage()));
		         		result.setResultCount(result.getProducts() != null ? result.getProducts().size() : 0);
		         		result.setTotalResultCount(products.size());
		            }
        		}
        	}
        } else {
        	if(!user.getFDSessionUser().isCouponsSystemAvailable() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
        		result.addWarningMessage(MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE, SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
            }
        }
        
        setResponseMessage(model, result, user);
        return model;
    }   
}
