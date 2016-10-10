package com.freshdirect.mobileapi.util;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.cms.ContentType;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.BannerModel;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategorySectionModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.TagModel;
import com.freshdirect.fdstore.content.util.SortStrategyElement;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.ValueHolder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.CarouselResult;
import com.freshdirect.mobileapi.controller.data.DepartmentCarouselResult;
import com.freshdirect.mobileapi.controller.data.NewBrowseResult;
import com.freshdirect.mobileapi.controller.data.ProductSearchResult;
import com.freshdirect.mobileapi.controller.data.SmartStoreProductResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.controller.data.response.SmartStoreRecommendations;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.RequestData;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.SmartStore;
import com.freshdirect.mobileapi.model.SmartStore.SmartStoreRecommendationContainer;
import com.freshdirect.mobileapi.model.Wine;
import com.freshdirect.mobileapi.model.tagwrapper.HttpContextWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.HttpSessionWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ItemGrabberTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ItemSorterTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.LayoutManagerWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.SessionParamName;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings;
import com.freshdirect.webapp.taglib.unbxd.BrowseEventTag;
import com.freshdirect.webapp.util.DepartmentCarouselUtil;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

public class NewBrowseUtil {
	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(NewBrowseUtil.class);
	
	private static final String ACTION_GET_CATEGORYCONTENT_PRODUCTONLY = "getCategoryContentProductOnly";
	private static final String FILTER_KEY_BRANDS = "brands";
    private static final String FILTER_KEY_TAGS = "tags";
    private static final String FLOWER_DEPARTMENT = "flo";
    private static String HMR_DEPT_CHECK = "hmr_freshdining";
    
    
    public static  NewBrowseResult getCategoriesForNewBrowse(BrowseQuery requestMessage, SessionUser user, HttpServletRequest request,ModelAndView model,HttpServletResponse response) throws FDException{
		
		String contentId = null;
		String action=null;
		NewBrowseResult result = new NewBrowseResult();
		contentId = requestMessage.getCategory();
		
		//Limit noOfProducts in category carousel to value given in request
		int carouselProductCount = requestMessage.getCarouselProductCount();
		
    	if (contentId == null) {
    		contentId = requestMessage.getDepartment();
    	}
    	
    	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(contentId);
    	List<CategorySectionModel> categorySections = emptyList();
    	if (currentFolder instanceof DepartmentModel) {
    		DepartmentModel department = (DepartmentModel) currentFolder;
    		categorySections = new ArrayList<CategorySectionModel>(department.getCategorySections());
    		DepartmentCarouselResult deptCarouselResult = new DepartmentCarouselResult();
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
    					deptCarouselResult.setTitle(department.getMerchantRecommenderTitle());
    					//APPDEV-4181 START
    					//Direct use of CategoryRecommender is being done as this is department level carousel but category recommender
    					// is directly being used in StoreFront for flowers department.
    					if (recommendedItems != null && recommendedItems.size() == 0 && contentId.equals(FLOWER_DEPARTMENT)) {
    						String cat = getSingleCategory(department);
    						if (cat != null) {
        						CategoryModel category = (CategoryModel)ContentFactory.getInstance().getContentNode(ContentType.get("Category"), cat);
        						recommendedItems = ProductRecommenderUtil.getMerchantRecommenderProducts(category);
        						deptCarouselResult.setTitle(category.getCatMerchantRecommenderTitle());
    						}
    					}
    					//END APPDEV-4181 START
    				} else {
    					deptCarouselResult.setTitle(department.getFeaturedRecommenderTitle());
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
    			deptCarouselResult.setSiteFeature(department.getFeaturedRecommenderSiteFeature());
    			deptCarouselResult.setProducts(setProductsFromModel(recommendedProducts));
    			
    			//APPDEV-4317 Streamline the departmentcarousel call - START
    			if(recommendedItems.size() == 0){
    				deptCarouselResult = getProductsFromCarousal(contentId,model,user,request,response);
	    		}
    			OverriddenVariantsHelper.AllowAnonymousUsers = allowRestore;
    			//APPDEV-4317 Streamline the departmentcarousel call - END
    			
    			result.setDeptCarouselResult(deptCarouselResult);
    			
			} catch (Exception e) {
				e.printStackTrace();
			}  
    		if(deptCarouselResult.getProducts() == null || deptCarouselResult.getProducts().isEmpty()) {
    			deptCarouselResult.setSuccessMessage("No recommendations found.");
			} else {
				deptCarouselResult.setSuccessMessage(deptCarouselResult.getSiteFeature() + " have been retrieved successfully.");
			}
    		
    	}
    	if(currentFolder instanceof CategoryModel) {
    		
    		String redirectURL = ((CategoryModel)currentFolder).getRedirectUrl();
    		if(redirectURL != null && redirectURL.trim().length() > 0) {
    			Map<String, String> redirectParams = getQueryMap(redirectURL);
    			String redirectContentId = (String)redirectParams.get("catId");
    			if(redirectContentId != null && redirectContentId.trim().length() > 0) {
    				contentId = redirectContentId;
    				currentFolder = ContentFactory.getInstance().getContentNode(redirectContentId);
    			}
    			else
    			{
    				redirectContentId = (String)redirectParams.get("deptId");
    				if(redirectContentId != null && redirectContentId.trim().length() > 0) {
        				contentId = redirectContentId;
        				currentFolder = ContentFactory.getInstance().getContentNode(redirectContentId);
        			}
    				
    			}
    		}
    		
    		//get the categoryCarouselResult whenn browse is called for category
    		CarouselResult categoryCarouselResult = new CarouselResult();
        	try {
        		categoryCarouselResult = getProductsFromCarouselForCategory(contentId, model, user, request, response,carouselProductCount);
			} catch (JsonException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	result.setCategoryCarouselResult(categoryCarouselResult);
        	
        	sendBrowseEventToAnalytics(request, user.getFDSessionUser(), currentFolder);
    	}
    	
    	
    	if(currentFolder instanceof CategoryModel && ((CategoryModel)currentFolder).isShowAllByDefault()) { // To Support new left nav flow[APPDEV-3251 : mobile API to utilize showAllByDefault]
        	action = ACTION_GET_CATEGORYCONTENT_PRODUCTONLY;
        }
    	if( currentFolder instanceof CategoryModel ){
    		CategoryModel cm = (CategoryModel) currentFolder;
    		BannerModel bm = cm.getTabletCallToActionBanner();
    		if( bm != null )
    		{
    			Idea featuredCardIdea = Idea.ideaFor(bm);
    			result.setFeaturedCard(featuredCardIdea);
    		}
    	}
    	List contents = new ArrayList();

        LayoutManagerWrapper layoutManagerTagWrapper = new LayoutManagerWrapper(user);
        Settings layoutManagerSetting = layoutManagerTagWrapper.getLayoutManagerSettings(currentFolder);
        
        if (layoutManagerSetting != null) {
        	if(layoutManagerSetting.getGrabberDepth() < 0) { // Overridding the hardcoded values done for new 4mm and wine layout
        		layoutManagerSetting.setGrabberDepth(0);
        	}

        	layoutManagerSetting.setReturnSecondaryFolders(true);//Hardcoded for mobile api
            ItemGrabberTagWrapper itemGrabberTagWrapper = new ItemGrabberTagWrapper(user.getFDSessionUser());
            contents = itemGrabberTagWrapper.getProducts(layoutManagerSetting, currentFolder);
            
            // Hack to make tablet work for presidents picks, tablet uses /browse/category call with department="picks_love". instead of /whatsgood/category/picks_love/
            if(currentFolder instanceof CategoryModel 
            			&& ((CategoryModel)currentFolder).getProductPromotionType() != null) {
            	layoutManagerSetting.setFilterUnavailable(true);
            	List<SortStrategyElement> list = new ArrayList<SortStrategyElement>();
            	list.add(new SortStrategyElement(SortStrategyElement.NO_SORT));
            	layoutManagerSetting.setSortStrategy(list);
            }
            
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
        List<Product> unavailableProducts = new ArrayList<Product>();

        List<Category> categories = new ArrayList<Category>();
        Set<String> categoryIDs = new TreeSet<String>();

        SortedSet<String> brands = new TreeSet<String>();
        SortedSet<String> countries = new TreeSet<String>();
        SortedSet<String> regions = new TreeSet<String>();
        SortedSet<String> grapes = new TreeSet<String>();
        SortedSet<String> typeFilters = new TreeSet<String>();
        
        boolean nextLevelIsBottom = true;

        for (Object content : contents) {
            if (content instanceof ProductModel) {
                ProductModel productModel = (ProductModel) content;
				try {
                	//if(!productModel.isHideIphone()) {			//DOOR3 FD-iPad FDIP-662
                		if (passesFilter(productModel, request)) {
                    		Product product = Product.wrap(productModel, user.getFDSessionUser().getUser(), null, EnumCouponContext.PRODUCT);

                    		for (Brand brand : product.getBrands()) {
								brands.add(brand.getName());
							}
                    		if (product instanceof Wine) {
                    			Wine wine = ((Wine) product);
                    			if (wine.getWineCountry() != null && wine.getWineRegionName() != null && wine.getGrape() != null) {
	                    			countries.add(wine.getWineCountry());
	                    			regions.add(wine.getWineRegionName());
                    			
                    			
	                    			String grape = wine.getGrape();
	                    			
	                    			String [] grapesSplit = grape.split(",");
	                    			
	                    			for(String g : grapesSplit){
	                    				g = g.replaceAll("[\\(\\)\\d\\%]*", "").trim();
	                    				grapes.add(g);
	                    			}
                    			}
                    			
                    		} else {
								SortedSet<String> types = product.getFilters().get("type");
                    			for (String type : types) {
									typeFilters.add(type);
								}
                    		}

							if(productModel.isUnavailable()) { // Segregate out unavailable to move them to the end
                    			unavailableProducts.add(product);
                    		} else {
                    			products.add(product);
                    		}
                		}
                	//}	//DOOR3 FD-iPad FDIP-662
                } catch (Exception e) {
                    //Don't let one rotten egg ruin it for the bunch
                    LOG.error("ModelException encountered. Product ID=" + productModel.getFullName(), e);
                }
            } else if(content instanceof CategoryModel) {
            	CategoryModel categoryModel = (CategoryModel)content;
                if (StringUtils.equals(contentId, categoryModel.getContentName())) {
                    // don't return recursive models
                    break;
                }
            	String parentId = categoryModel.getParentNode().getContentKey().getId();
            	
				if((categoryModel.isActive()
            					|| (categoryModel.getRedirectUrl() != null
            								&& categoryModel.getRedirectUrl().trim().length() > 0))
            				// && !categoryModel.isHideIphone()
            				// && !categoryIDs.contains(categoryModel.getParentId())
            				// && !isEmptyProductGrabberCategory(categoryModel)
            				&& contentId.equals(parentId)
            				) {	// Show only one level of category
					// check if the next level in hierarchy has only products
					// it's important for the UI
					//See if we can change this ... why should we? this is in sync with website...
					if (nextLevelIsBottom && !categoryModel.getSubcategories().isEmpty()) {
						for (CategoryModel subcategory : categoryModel.getSubcategories()) {
							if (!subcategory.getSubcategories().isEmpty()) {
								// we have another level to go
								nextLevelIsBottom = false;
								break;
							}
						}
					}
					
            		Category category = Category.wrap(categoryModel);
					addCategoryHeadline(categorySections, categoryModel, category);
					boolean remove = removeCategoryToMatchStorefront(categorySections, categoryModel, category);
					
						category.setNoOfProducts(0);
					//Change this as well.
						if(!categoryModel.getSubcategories().isEmpty())
							category.setBottomLevel(false);
						else
							category.setBottomLevel(true);
						if(!remove){
							//APPDEV 4231
							if(hasProduct(categoryModel)){
								categories.add(category);
								categoryIDs.add(categoryModel.getContentKey().getId());
							}
						}
						
						
						SortedMap carouselMap = new TreeMap();
						int loadCategoriesCarouselCount = requestMessage.getLoadCategoriesCarousel();
												
					if (categoryIDs != null) {
						for (String id : categoryIDs) {
							CarouselResult carouselResult = new CarouselResult();
							try {
								carouselResult = getProductsFromCarouselForCategory(id, model, user, request, response,carouselProductCount);
										
							} catch (JsonException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (loadCategoriesCarouselCount <= 0)
								break;

							carouselMap.put(id, carouselResult);

							if (carouselMap.size() == loadCategoriesCarouselCount)
								break;
						}
						result.setCategoriesCarousel(carouselMap);
					}
				}
			
           }
        }
        
        Map<String, SortedSet<String>> filters = result.getFilters();
        if (brands.size() > 0) filters.put("brand", brands);
        if (countries.size() > 0) filters.put("country", countries);
        if (regions.size() > 0) filters.put("region", regions);
        if (grapes.size() > 0) filters.put("grape", grapes);
        if (typeFilters.size() > 0) filters.put("type", typeFilters);
        
        categories= customizeCaegoryListForIpad(categories, categorySections);
        
        if(categories.size() > 0 && !ACTION_GET_CATEGORYCONTENT_PRODUCTONLY.equals(action)) {
        	ListPaginator<com.freshdirect.mobileapi.model.Category> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Category>(
        			categories, requestMessage.getMax());

			result.setCategories(paginator.getPage(requestMessage.getPage()));
			result.setResultCount(result.getCategories() != null ? result.getCategories().size() : 0);
			result.setTotalResultCount(categories.size());
        } else {
        	products.addAll(unavailableProducts);//add all unavailable to the end of the list

        	ListPaginator<com.freshdirect.mobileapi.model.Product> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Product>(
     															products, requestMessage.getMax());

        	// send subcategories with products
        	result.setCategories(categories);
     		result.setProductsFromModel(paginator.getPage(requestMessage.getPage()));
     		result.setResultCount(result.getProducts() != null ? result.getProducts().size() : 0);
     		result.setTotalResultCount(products.size());
        }
        
        result.setBottomLevel(nextLevelIsBottom);
        return result;
		
	}
     
	//APPDEV 4231 Start
	private static boolean isProductAvailable(List<ProductModel> prodList){
		boolean result = false;
		
		for(ProductModel model:prodList){
			if(!(model.isUnavailable() || model.isDiscontinued())){
				result = true;
				break;
			}
		}
		return result;
	}
	
	private static boolean hasProduct(CategoryModel categoryModel){
		if(!categoryModel.getSubcategories().isEmpty())
		{
			List<CategoryModel> subCategories = categoryModel.getSubcategories();
			for (CategoryModel m1 : subCategories) {
				boolean result = hasProduct(m1);
				if(result){
					return result; 
				}
			}
		}
		if(categoryModel.getProducts().size()>0)
		{
			return isProductAvailable(categoryModel.getProducts());
		}else
			return false;
	}
	//APPDEV 4231 END
	
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
	
	private static  void addCategoryHeadline(
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
	
	private static boolean removeCategoryToMatchStorefront(List<CategorySectionModel> categorySections,
			CategoryModel categoryModel, Category category){
		//The section Header is already set in category - see @addCategoryHeadline
		if(category.getSectionHeader()!=null && !category.getSectionHeader().isEmpty()){
			return false;
		} else {
			if(categorySections!=null && !categorySections.isEmpty() && !categoryModel.isPreferenceCategory()){
				return true;
			}
		}
		return false;
	}
	
	 private static  boolean passesFilter(ProductModel product,
				HttpServletRequest request) {
	        return filterTags(product, request) && filterBrands(product, request);
		}
	 
	 private static  boolean filterBrands(ProductModel product,
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

		private static  boolean filterTags(ProductModel product, HttpServletRequest request) {
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
		
		private static  class NameComparator implements Comparator<Category> {

			@Override
			public int compare(Category o1, Category o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}

		}
		
		//This method Splits the categories List into sublists based on sectionHeader and sorts each alphabetically
	    private static  List<Category> customizeCaegoryListForIpad(List<Category> categories, List<CategorySectionModel> categorySections){
	    	//get the size of categorySections which we will use to create number of sublists
	    	int numOfSections = categorySections==null||categorySections.isEmpty()? 0 : categorySections.size();
	    	NameComparator nameComparator = new NameComparator();
	    	List<List<Category>> sublists = new ArrayList<List<Category>>();
	    	
	    	// Loop on the categorySections : inside loop on categories to split into sublists based on sectionHeader
	    	if(categorySections==null||categorySections.isEmpty()){
	    		List<Category> nonPrefCats = new ArrayList<Category>();
	    		for(Category cat : categories){
	    			if(cat.getSectionHeader()!=null && !cat.getSectionHeader().isEmpty() ){
	    				nonPrefCats.add(cat);
	    			}
	    		}
	    		Collections.sort(nonPrefCats, nameComparator);
	    		sublists.add(nonPrefCats);
	    	}
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
	    		
		 public static List<ProductSearchResult> setProductsFromModel(List<com.freshdirect.mobileapi.model.Product> products) {
		    	List<ProductSearchResult> result = new ArrayList<ProductSearchResult>();
		        if(products != null) {
			    	for (com.freshdirect.mobileapi.model.Product product : products) {
			        	result.add(ProductSearchResult.wrap(product));
			        }
		        }
		        return result;
		 }
		 
			/**
			 * @param dept
			 * @return
			 * if department has only one usable category then return with that categoryId
			 */
		    //APPDEV - 4181 START
			private static String getSingleCategory(DepartmentModel dept){
				
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
			private static boolean isCategoryHiddenInContext(CategoryModel cat) {
				return 	!cat.isShowSelf() || isCategoryForbiddenInContext(cat);
			}
			
			/** if true category page cannot be displayed **/
			private static boolean isCategoryForbiddenInContext(CategoryModel cat) {
				return 	cat.isHideIfFilteringIsSupported() || 
							cat.isHidden();
			}
			//APPDEV - 4181 END
			private static DepartmentCarouselResult getProductsFromCarousal(String deptId,ModelAndView model,SessionUser user,HttpServletRequest request,HttpServletResponse response) throws JsonException 
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
					products = getPeakProduce(model, user, request, response,deptId);				
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
							request,redirectDept,deptId);

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
			return result;
			}
			
			//APPDEV-4317 Streamline the departmentcarousel call - END
			
			
			private static CarouselResult getProductsFromCarouselForCategory(String catId,ModelAndView model,SessionUser user,HttpServletRequest request,HttpServletResponse response,Integer carouselProductCount) throws JsonException 
			{
				
				String redirectDept = "";
				if(catId.equalsIgnoreCase(HMR_DEPT_CHECK))
				{
					ContentNodeModel currentFolderTemp = ContentFactory.getInstance().getContentNode(catId);
			    	String redirectURL = ((CategoryModel)currentFolderTemp).getRedirectUrl();
			    	Map<String, String> redirectParams = getQueryMap(redirectURL);
			    	redirectDept = redirectParams.get("deptId");
				}
				
				if(catId.equalsIgnoreCase(HMR_DEPT_CHECK) && redirectDept != null && !redirectDept.equals("")) {
					catId = redirectDept;
				}
			    
				EnumSiteFeature siteFeature = DepartmentCarouselUtil.getCarousel(catId);
				String title = DepartmentCarouselUtil.getCarouselTitle(catId);
				
				CarouselResult result = new CarouselResult();
				List products = null;
				
				if (EnumSiteFeature.PEAK_PRODUCE.equals(siteFeature)) {
					products = getPeakProduce(model, user, request, response,catId);				
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
							request,redirectDept,catId);

					OverriddenVariantsHelper.AllowAnonymousUsers = allowRestore;
					
					//products = getCarouselRecommendations(siteFeature,	model, user, request);
				}
				
				result.setTitle(title);
				result.setSiteFeature(siteFeature.getName());
				
				//Limit noOfProducts in category carousel to value given in request
				if(products.size()>carouselProductCount)
				{
					result.setProducts(products.subList(0, carouselProductCount));
				}
				//size less than request, set original
				else
				{
					result.setProducts(products);
				}
				
				
				if(products == null || (products != null && products.isEmpty())  ) {
					result.setSuccessMessage("No recommendations found.");
				} else {
					result.setSuccessMessage(siteFeature.getTitle() + " have been retrieved successfully.");
				}
			return result;
			}
			@SuppressWarnings("unchecked")
			private static List<ProductSearchResult> getPeakProduce(ModelAndView model,
					SessionUser user, HttpServletRequest request, HttpServletResponse response,String deptId) throws JsonException {
				
		        SmartStore smartStore = new SmartStore(user);
		        ResultBundle resultBundle = smartStore.getPeakProduceProductList(deptId);

		        ActionResult result = resultBundle.getActionResult();
		        List<com.freshdirect.mobileapi.model.Product> products = null; 
		        if(result.isSuccess()) {
		        	products = (List<Product>) resultBundle.getExtraData(SmartStore.PEAKPRODUCE);
		        }                
		        return setProductsFromModel(products);
			}
			
		   	private static List<ProductSearchResult> getMeatBestDeals(ModelAndView model,
		   			SessionUser user, HttpServletRequest request, HttpServletResponse response) throws JsonException {
		       	   
				SmartStore smartStore = new SmartStore(user);
				List<com.freshdirect.mobileapi.model.Product> products = smartStore
						.getMeatBestDeals();

				return setProductsFromModel(products);
		    }
		   	
		    private static EnumSiteFeature getSiteFeature( String sfName ) {
				EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(sfName);		
				return siteFeat;
			}
		   	   
		    private static List<SmartStoreProductResult> getCarouselRecommendations(EnumSiteFeature siteFeature, ModelAndView model,
					SessionUser user, HttpServletRequest request,String redirectDept,String deptId) throws JsonException {
				
		    	
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
		    
		    protected static RequestData qetRequestData(HttpServletRequest request) {
		        RequestData requestData = new RequestData();
		        requestData.setQueryString(request.getQueryString());
		        requestData.setServer(request.getServerName());
		        requestData.setRequestURI(request.getRequestURI());
		        requestData.setSessionId(request.getSession().getId());
		        return requestData;
		    }
		    
		    /** 
		     * Extract what we need to get out of request/session wrappers in tagwrapper
		     * and set it within our session (not request)
		     * 
		     * @param actualSession
		     * @param resultBundle
		     */
		    protected static void propogateSetSessionValues(HttpSession actualSession, ResultBundle resultBundle) {
		        HttpSessionWrapper sessionWrapper = (HttpSessionWrapper) resultBundle.getExtraData(HttpContextWrapper.SESSION);
		        if (null != sessionWrapper) {
		            sessionWrapper.propogateSetValues(actualSession);
		        }
		    }

		    private static void sendBrowseEventToAnalytics(HttpServletRequest request, FDUserI user, ContentNodeModel model){
	            if(FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, request.getCookies(), user)){
	                BrowseEventTag.doSendEvent(model.getContentKey().getId(), user, request);
	            }
	        }
}
