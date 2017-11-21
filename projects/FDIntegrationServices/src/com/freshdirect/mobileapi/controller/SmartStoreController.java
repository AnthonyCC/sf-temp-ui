package com.freshdirect.mobileapi.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.ValueHolder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.DepartmentCarouselResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductSearchResult;
import com.freshdirect.mobileapi.controller.data.SmartStoreProductResult;
import com.freshdirect.mobileapi.controller.data.response.SmartStoreRecommendations;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.SmartStore;
import com.freshdirect.mobileapi.model.SmartStore.SmartStoreRecommendationContainer;
import com.freshdirect.mobileapi.model.tagwrapper.SessionParamName;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.util.DepartmentCarouselUtil;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

/**
 * @author rsung, csongor
 *
 */
public class SmartStoreController extends BaseController {
  
	private static Category LOGGER = LoggerFactory.getInstance(SmartStoreController.class);

    private static String ACTION_GET_YMAL = "getymal";

    private static String ACTION_GET_YOUR_FAVORITE = "getyourfavorite";

    private static String ACTION_GET_FRESHDIRECT_FAVORITE = "getfdfavorite";

    private static String ACTION_GET_FAVORITE = "favorite";
    
    private static String ACTION_GET_DEPARTMENT_CAROUSEL = "getDepartmentCarousel";
    
    private static String ACTION_GET_CAROUSEL = "getCarousel";
    
    private static final String PARAM_DEPT_ID = "departmentId";
    
    //APPDEV-4181 Used directly as the same is done in StoreFront
    private static final String FLOWER_DEPARTMENT = "flo";
	
	private static String HMR_DEPT_CHECK = "hmr_freshdining";

    @Override
    protected boolean validateUser() {
    	return false;
    }

	@Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws ServiceException, NoSessionException, JsonException {

    	if (user == null) {
    		user = fakeUser(request.getSession());
    	}
    	
        if (ACTION_GET_YMAL.equals(action)) {
            model = getRecommendations(EnumSiteFeature.YMAL, model, user, request);
        } else if (ACTION_GET_YOUR_FAVORITE.equals(action)) {
            model = getRecommendations(EnumSiteFeature.DYF, model, user, request);
        } else if (ACTION_GET_FRESHDIRECT_FAVORITE.equals(action)) {
            model = getRecommendations(EnumSiteFeature.FAVORITES, model, user, request);
        } else if (ACTION_GET_FAVORITE.equals(action)) {
            model = getFavoritesRecommendations(model, user, request);
        } else if (ACTION_GET_DEPARTMENT_CAROUSEL.equals(action)) {
			
        	DepartmentCarouselResult result = new DepartmentCarouselResult();
						
			String deptId = request.getParameter(PARAM_DEPT_ID);
			ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(deptId);
			if (currentFolder instanceof DepartmentModel) {
        		DepartmentModel department = (DepartmentModel) currentFolder;
        		try {
        			boolean allowRestore = OverriddenVariantsHelper.AllowAnonymousUsers;
        			OverriddenVariantsHelper.AllowAnonymousUsers = true;
        			List<ProductModel> recommendedItems = ProductRecommenderUtil.getFeaturedRecommenderProducts(department
        																							, user.getFDSessionUser()
        																							, null
        																							, new ValueHolder<Variant>());
        			List<com.freshdirect.mobileapi.model.Product> recommendedProducts = new ArrayList<Product>();
        			if(recommendedItems != null) {
        				if(recommendedItems.size() == 0) {
        					recommendedItems = ProductRecommenderUtil.getMerchantRecommenderProducts(department);
        					result.setTitle(department.getMerchantRecommenderTitle());
        					//APPDEV-4181 START
        					//Direct use of CategoryRecommender is being done as this is department level carousel but category recommender
        					// is directly being used in StoreFront for flowers department.
        					if (recommendedItems != null && recommendedItems.size() == 0 && deptId.equals(FLOWER_DEPARTMENT)) {
        						String cat = getSingleCategory(department);
        						if (cat != null) {
	        						CategoryModel category = (CategoryModel)ContentFactory.getInstance().getContentNode(ContentType.Category, cat);
	        						recommendedItems = ProductRecommenderUtil.getMerchantRecommenderProducts(category);
	        						result.setTitle(category.getCatMerchantRecommenderTitle());
        						}
        					}
        					//END APPDEV-4181 START
        				} else {
        					result.setTitle(department.getFeaturedRecommenderTitle());
        				}
        				for(ProductModel content : recommendedItems) {
        					try {
								recommendedProducts.add(Product.wrap((ProductModel) content
																			, user.getFDSessionUser().getUser()
																			, null
																			, EnumCouponContext.PRODUCT));								
							} catch (ModelException e) {
								e.printStackTrace();
							}
        				}
        			}					
	    			result.setSiteFeature(department.getFeaturedRecommenderSiteFeature());
	    			result.setProducts(setProductsFromModel(recommendedProducts));
	    			//APPDEV-4317 Streamline the departmentcarousel call - START
	    			if(recommendedItems.size() == 0){
	    				result = getProductsFromCarousal(deptId,model,user,request,response);
		    		//APPDEV-4317 Streamline the departmentcarousel call - END
	    			}
	    			
	    			OverriddenVariantsHelper.AllowAnonymousUsers = allowRestore;
	    			
				} catch (FDResourceException e) {
					e.printStackTrace();
				}     		    			
			}
						
			if(result.getProducts() == null || result.getProducts().isEmpty()) {
				result.setSuccessMessage("No recommendations found.");
			} else {
				result.setSuccessMessage(result.getSiteFeature() + " have been retrieved successfully.");
			}
			setResponseMessage(model, result, user);
		} else if (ACTION_GET_CAROUSEL.equals(action)) {
			
			String deptId = request.getParameter(PARAM_DEPT_ID);
			
			//APPDEV-4237
			String redirectDept = "";
			if(deptId.equalsIgnoreCase(HMR_DEPT_CHECK))
			{
				ContentNodeModel currentFolderTemp = ContentFactory.getInstance().getContentNode(deptId);
		    	String redirectURL = ((CategoryModel)currentFolderTemp).getRedirectUrl();
		    	Map<String, String> redirectParams = getQueryMap(redirectURL);
		    	redirectDept = redirectParams.get("deptId");
			}
			
			if(deptId.equalsIgnoreCase(HMR_DEPT_CHECK) && redirectDept != null && !redirectDept.equals("")) {
				deptId = redirectDept;
			}
		    
			EnumSiteFeature siteFeature = DepartmentCarouselUtil.getCarousel(deptId);
			String title = DepartmentCarouselUtil.getCarouselTitle(deptId);
			
			DepartmentCarouselResult result = new DepartmentCarouselResult();
			List products = null;
			
			if (EnumSiteFeature.PEAK_PRODUCE.equals(siteFeature)) {
				products = getPeakProduce(model, user, request, response);				
			} else if (EnumSiteFeature.WEEKS_MEAT_BEST_DEALS.equals(siteFeature)) {
				products = getMeatBestDeals(model, user, request, response);
			}/* else if (EnumSiteFeature.BRAND_NAME_DEALS.equals(siteFeature)) {
				products = getCarouselRecommendations(siteFeature, model, user, request);
			} */else {
				// So called 'customer favorites department level' recommendations
				// siteFeature: SideCart Featured Items (SCR_FEAT_ITEMS) + dept as currentNode
				siteFeature = getSiteFeature("SCR_FEAT_ITEMS");
			

				boolean allowRestore = OverriddenVariantsHelper.AllowAnonymousUsers;
				OverriddenVariantsHelper.AllowAnonymousUsers = true;

				products = getCarouselRecommendations(siteFeature, model, user,
						request,redirectDept);

				OverriddenVariantsHelper.AllowAnonymousUsers = allowRestore;
				
				//products = getCarouselRecommendations(siteFeature,	model, user, request);
			}
			
			result.setTitle(title);
			result.setSiteFeature(siteFeature.getName());
			result.setProducts(products);
			if(products == null || (products != null && products.isEmpty())  ) {
				result.setSuccessMessage("No recommendations found.");
			} else {
				result.setSuccessMessage(siteFeature.getTitle() + " have been retrieved successfully.");
			}
			setResponseMessage(model, result, user);
		}
        return model;
    }
	//APPDEV-4317 Streamline the departmentcarousel call - START
	private DepartmentCarouselResult getProductsFromCarousal(String deptId,ModelAndView model,SessionUser user,HttpServletRequest request,HttpServletResponse response) throws JsonException 
	{
		
		String redirectDept = "";
		if(deptId.equalsIgnoreCase(HMR_DEPT_CHECK))
		{
			ContentNodeModel currentFolderTemp = ContentFactory.getInstance().getContentNode(deptId);
	    	String redirectURL = ((CategoryModel)currentFolderTemp).getRedirectUrl();
	    	Map<String, String> redirectParams = getQueryMap(redirectURL);
	    	redirectDept = redirectParams.get("deptId");
		}
		
		if(deptId.equalsIgnoreCase(HMR_DEPT_CHECK) && redirectDept != null && !redirectDept.equals("")) {
			deptId = redirectDept;
		}
	    
		EnumSiteFeature siteFeature = DepartmentCarouselUtil.getCarousel(deptId);
		String title = DepartmentCarouselUtil.getCarouselTitle(deptId);
		
		DepartmentCarouselResult result = new DepartmentCarouselResult();
		List products = null;
		
		if (EnumSiteFeature.PEAK_PRODUCE.equals(siteFeature)) {
			products = getPeakProduce(model, user, request, response);				
		} else if (EnumSiteFeature.WEEKS_MEAT_BEST_DEALS.equals(siteFeature)) {
			products = getMeatBestDeals(model, user, request, response);
		} /*else if (EnumSiteFeature.BRAND_NAME_DEALS.equals(siteFeature)) {
			products = getCarouselRecommendations(siteFeature, model, user, request);
		}*/ else {
			// So called 'customer favorites department level' recommendations
			// siteFeature: SideCart Featured Items (SCR_FEAT_ITEMS) + dept as currentNode
			siteFeature = getSiteFeature("SCR_FEAT_ITEMS");
		

			boolean allowRestore = OverriddenVariantsHelper.AllowAnonymousUsers;
			OverriddenVariantsHelper.AllowAnonymousUsers = true;

			products = getCarouselRecommendations(siteFeature, model, user,
					request,redirectDept);

			OverriddenVariantsHelper.AllowAnonymousUsers = allowRestore;
			
			//products = getCarouselRecommendations(siteFeature,	model, user, request);
		}
		
		result.setTitle(title);
		result.setSiteFeature(siteFeature.getName());
		result.setProducts(products);
		if(products == null || (products != null && products.isEmpty())  ) {
			result.setSuccessMessage("No recommendations found.");
		} else {
			result.setSuccessMessage(siteFeature.getTitle() + " have been retrieved successfully.");
		}
	setResponseMessage(model, result, user);
	return result;
	}
	//APPDEV-4317 Streamline the departmentcarousel call - END

	
	//APPDEV-4237
	private static Map<String, String> getQueryMap(String url) {
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
	
	 public List<ProductSearchResult> setProductsFromModel(List<com.freshdirect.mobileapi.model.Product> products) {
	    	List<ProductSearchResult> result = new ArrayList<ProductSearchResult>();
	        if(products != null) {
		    	for (com.freshdirect.mobileapi.model.Product product : products) {
		        	result.add(ProductSearchResult.wrap(product));
		        }
	        }
	        return result;
	 }
	 
   	private List<ProductSearchResult> getMeatBestDeals(ModelAndView model,
   			SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException {
       	   
		SmartStore smartStore = new SmartStore(user);
		List<com.freshdirect.mobileapi.model.Product> products = smartStore
				.getMeatBestDeals();

		return setProductsFromModel(products);
    }
   	   
       	
 	@SuppressWarnings("unchecked")
	private List<ProductSearchResult> getPeakProduce(ModelAndView model,
			SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException {
		
    	String deptId = request.getParameter(PARAM_DEPT_ID);
        SmartStore smartStore = new SmartStore(user);
        ResultBundle resultBundle = smartStore.getPeakProduceProductList(deptId);

        ActionResult result = resultBundle.getActionResult();
        List<com.freshdirect.mobileapi.model.Product> products = null; 
        if(result.isSuccess()) {
        	products = (List<Product>) resultBundle.getExtraData(SmartStore.PEAKPRODUCE);
        }                
        return setProductsFromModel(products);
	}

    private List<SmartStoreProductResult> getCarouselRecommendations(EnumSiteFeature siteFeature, ModelAndView model,
			SessionUser user, HttpServletRequest request,String redirectDept) throws JsonException {
		
    	String deptId = request.getParameter(PARAM_DEPT_ID);
    	
    	if(deptId.equalsIgnoreCase(HMR_DEPT_CHECK) && redirectDept != null && !redirectDept.equals("")) {
			deptId = redirectDept;
		}
    	
    	String page = request.getParameter("page");
    	
    	SmartStore smartStore = new SmartStore(user);
       
        ResultBundle resultBundle = smartStore.getCarouselRecommendations(siteFeature, deptId, qetRequestData(request), (Recommendations) request
                .getSession().getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS), (String) request.getSession()
                .getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION), page);
        
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        SmartStoreRecommendations recommendations = null;
        if (result.isSuccess()) {
        	recommendations = new SmartStoreRecommendations((SmartStoreRecommendationContainer) resultBundle
                    .getExtraData(SmartStore.RECOMMENDATION));        
        } 
        // EW: FDIP-695
        if(recommendations == null) return null;
        return recommendations.getProducts();
	}

	private ModelAndView getFavoritesRecommendations(ModelAndView model, SessionUser user, HttpServletRequest request) throws JsonException {
        String page = request.getParameter("page");
        Message responseMessage = null;

        SmartStore smartStore = new SmartStore(user);

        ResultBundle resultBundle = smartStore.getFavoritesRecommendations(qetRequestData(request), (Recommendations) request.getSession()
                .getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS), (String) request.getSession().getAttribute(
                SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION), page);

        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = new SmartStoreRecommendations((SmartStoreRecommendationContainer) resultBundle
                    .getExtraData(SmartStore.RECOMMENDATION));
            responseMessage.setSuccessMessage("Favorite recommendations have been retrieved successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView getRecommendations(EnumSiteFeature siteFeature, ModelAndView model, SessionUser user, HttpServletRequest request)
            throws JsonException {
        String page = request.getParameter("page");
        Message responseMessage = null;

        SmartStore smartStore = new SmartStore(user);

        ResultBundle resultBundle = smartStore.getRecommendations(siteFeature, qetRequestData(request), (Recommendations) request
                .getSession().getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS), (String) request.getSession()
                .getAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION), page);

        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = new SmartStoreRecommendations((SmartStoreRecommendationContainer) resultBundle
                    .getExtraData(SmartStore.RECOMMENDATION));
            responseMessage.setSuccessMessage(siteFeature.getTitle() + " recommendations have been retrieved successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private static EnumSiteFeature getSiteFeature( String sfName ) {
		EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(sfName);		
		return siteFeat;
	}
    
	/**
	 * @param dept
	 * @return
	 * if department has only one usable category then return with that categoryId
	 */
    //APPDEV - 4181 START
	private String getSingleCategory(DepartmentModel dept){
		
		String theOnlyOne=null;
		
		if(dept.getCategories()!=null){
			
			int categoryCounter = 0;
			
			for(CategoryModel cat : dept.getCategories()){
				
				if(isCategoryHiddenInContext(cat)){
					continue;
				}
					
				++categoryCounter;
					
				if(categoryCounter>1){
					return null;
				}
					
				theOnlyOne = cat.getContentKey().getId();
			}
			
			if(categoryCounter==1){
				return theOnlyOne;				
			}
			
		}
		
		return null;
	}
	
	/** if true category does not show up in any navigation **/
	private boolean isCategoryHiddenInContext(CategoryModel cat) {
		return 	!cat.isShowSelf() || isCategoryForbiddenInContext(cat);
	}
	
	/** if true category page cannot be displayed **/
	private boolean isCategoryForbiddenInContext(CategoryModel cat) {
		return 	cat.isHideIfFilteringIsSupported() || 
					cat.isHidden();
	}
	//APPDEV - 4181 END
}
