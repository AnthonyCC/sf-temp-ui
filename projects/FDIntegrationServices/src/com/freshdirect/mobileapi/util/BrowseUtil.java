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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

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
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.DepartmentSection;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.Wine;
import com.freshdirect.mobileapi.model.tagwrapper.ItemGrabberTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ItemSorterTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.LayoutManagerWrapper;
import com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings;

public class BrowseUtil {
	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(BrowseUtil.class);
	
	private static final String ACTION_GET_CATEGORYCONTENT_PRODUCTONLY = "getCategoryContentProductOnly";
	private static final String FILTER_KEY_BRANDS = "brands";
    private static final String FILTER_KEY_TAGS = "tags";
    private static final String NO_SECTIONHEADER = "noSectionHeader";
    
     
	
	public static  BrowseResult getCategories(BrowseQuery requestMessage, SessionUser user, HttpServletRequest request) throws FDException{
		
		String contentId = null;
		String action=null;
		BrowseResult result = new BrowseResult();
		contentId = requestMessage.getCategory();
    	if (contentId == null) {
    		contentId = requestMessage.getDepartment();
    	}
    	
    	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(contentId);
    	List<CategorySectionModel> categorySections = emptyList();
    	if (currentFolder instanceof DepartmentModel) {
    		DepartmentModel department = (DepartmentModel) currentFolder;
    		categorySections = new ArrayList<CategorySectionModel>(department.getCategorySections());
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
				//APPDEV-4237 No Carousel Products
    			else
    			{
    				redirectContentId = (String)redirectParams.get("deptId");
    				if(redirectContentId != null && redirectContentId.trim().length() > 0) {
        				contentId = redirectContentId;
        				currentFolder = ContentFactory.getInstance().getContentNode(redirectContentId);
        			}
    				
    			}
    		}
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
        Set<String> categoryIDs = new HashSet<String>();

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
						categories.add(category);
					}
            		categoryIDs.add(categoryModel.getContentKey().getId());
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
//-------------------------------------------browse/Navigation-----------------------------------------------------------------------------	    
	    /**
	     * Populate the department section for a given department 
	     * @return
	     */
	    public static List<DepartmentSection> getDepartmentSections(DepartmentModel storeDepartment){
	    	
	    	List<DepartmentSection> departmentSections = new ArrayList<DepartmentSection>();
	    	
	    	//get all the categories
	    	List<CategoryModel> allCategories = storeDepartment.getCategories();
	    	
	    	List<CategorySectionModel> categorySections = emptyList();
	    	categorySections = new ArrayList<CategorySectionModel>(storeDepartment.getCategorySections());
	    	//Preference categories do not have any section Header
    		String sectionHeaderPref = storeDepartment.getPreferenceCategoriesNavHeader();
    		String sectionNamePref = isNotBlank(sectionHeaderPref) ? sectionHeaderPref : storeDepartment.getFullName();
    		//Normal categories have department name as section header.
    		String sectionHeaderNormal = storeDepartment.getRegularCategoriesNavHeader();
    		String sectionNameNormal = isNotBlank(sectionHeaderNormal) ? sectionHeaderNormal : storeDepartment.getFullName();
	    	//if category sections is empty all categories under one section Header 
	    	if(categorySections==null || categorySections.isEmpty()){
	    		//Two possibilities preference category or normal category
	    		
	    		
	    		Map<String, List<CategoryModel>> nosectionCatMap = getNoSectionCategories(allCategories, null);
	    		for(Map.Entry<String, List<CategoryModel>> entry :nosectionCatMap.entrySet()){
	    			DepartmentSection section = new DepartmentSection();
	    			//create department section for nosectionCategories for preference categories
	    			if(entry.getKey().equals("prefCat")){
	    				List<Category> selectedcategories = buildcategories(entry.getValue());
			    		section.setCategories(selectedcategories);
			    		section.setSectionHeader(sectionNamePref);
			    		departmentSections.add(section);
	    			} else if(entry.getKey().equals("normalCat")){
	    				List<Category> selectedcategories = buildcategories(entry.getValue());
	    				section.setCategories(selectedcategories);
	    				section.setSectionHeader(sectionNameNormal);
	    				departmentSections.add(section);
	    			}
		    		
	    		}
	    		
	    	} else{
	    		//for each section header we add those categories to categoryList
	    		Map<String, List<CategoryModel>> nosectionCatMap = getNoSectionCategories(allCategories, categorySections);
	    		for(CategorySectionModel catSection : categorySections){
	    			DepartmentSection section = new DepartmentSection();
	    			section.setSectionHeader(catSection.getHeadline());
	    			//Call build categories with selected categories as argument and add it to categories of departmentsection
	    			List<Category> selectedcategories = buildcategories(catSection.getSelectedCategories());
	    			section.setCategories(selectedcategories);
	    			departmentSections.add(section);
	    		}
	    		
	    		for(Map.Entry<String, List<CategoryModel>> entry :nosectionCatMap.entrySet()){
	    			DepartmentSection section = new DepartmentSection();
	    			//create department section for nosectionCategories for preference categories
	    			if(entry.getKey().equals("prefCat")){
	    				List<Category> selectedcategories = buildcategories(entry.getValue());
			    		section.setCategories(selectedcategories);
			    		section.setSectionHeader(sectionNamePref);
			    		departmentSections.add(section);
	    			} else if(entry.getKey().equals("normalCat")){
	    				List<Category> selectedcategories = buildcategories(entry.getValue());
	    				section.setCategories(selectedcategories);
	    				section.setSectionHeader(sectionNameNormal);
	    				departmentSections.add(section);
	    			}
		    		
	    		}
	    		
	    		
	    	}
	    	
	    	return departmentSections;
	    }
	    
	    /**
	     * This should be a recursive call to build the tree of categories from CMS
	     * @return
	     */
	    private static List<Category> buildcategories(List<CategoryModel> selectedCategories){
	    	long startTime = System.currentTimeMillis();
	    	List<Category> categories = new ArrayList<Category>();
	    	for(CategoryModel model : selectedCategories){
	    		Category cat = buildCategoryData(model);
	    		categories.add(cat);
	    	}
	    	sortByName(categories);
	    	long endTime   = System.currentTimeMillis();
        	long totalTime = endTime - startTime;
        	LOG.debug("Time to construct  categories :" +totalTime);
	    	return categories;
	    }
	    
	    private static void sortByName(List<Category> categories){
	    	if(categories==null || categories.isEmpty()){
	    		return;
	    	} else {
	    		NameComparator nameComparator = new NameComparator();
		    	Collections.sort(categories, nameComparator);
		    	for(Category cat : categories){
		    		sortByName(cat.getCategories());
		    	}
	    	}
	    }
	    
	    private static Category buildCategoryData (CategoryModel model){
	    	if(model==null) return null;
	    	Category category = Category.wrap(model);
	    	if(model.getSubcategories()!=null && !model.getSubcategories().isEmpty()){
	    		for(CategoryModel subcat : model.getSubcategories()){
	    			category.addCategories(buildCategoryData(subcat));
	    		}
	    	}
	    	return category;
	    }
	    
	    /**
	     * This method will separate categories into separate lists for adding to department section
	     * @param allCategories
	     * @param categorySections
	     */
	    private static Map<String, List<CategoryModel>> getNoSectionCategories(List<CategoryModel> allCategories, List<CategorySectionModel> categorySections){
	    	
	    	List<CategoryModel> noSectionCats = new ArrayList<CategoryModel>();
	    	List<CategoryModel> noSectionPrefCats = new ArrayList<CategoryModel>();
	    	Map<String, List<CategoryModel>> nosectionCatMap = new HashMap<String, List<CategoryModel>>();
	    	if(categorySections==null || categorySections.isEmpty()){
	    		for(CategoryModel itemInAllCategories : allCategories){
	    			if(itemInAllCategories.isPreferenceCategory()){
	    				noSectionPrefCats.add(itemInAllCategories);
	    			} else {
	    				noSectionCats.add(itemInAllCategories);
	    			}
	    		}
	    		nosectionCatMap.put("normalCat", noSectionCats);
		    	nosectionCatMap.put("prefCat", noSectionPrefCats);
		    	return nosectionCatMap;
	    	}
	    	for(CategoryModel itemInAllCategories : allCategories){
	    		boolean present = true;
	    		for(CategorySectionModel catSection : categorySections){
	    			if(catSection.getSelectedCategories().contains(itemInAllCategories)){
	    				present = true;
	    				break;
	    			} else {
	    				present = false;
	    			}
	    		}
	    		if(!present){
	    			//need to check if this is a preference category or normal category
	    			if(itemInAllCategories.isPreferenceCategory()){
	    				noSectionPrefCats.add(itemInAllCategories);
	    			} else {
	    				noSectionCats.add(itemInAllCategories);
	    			}
	    			
	    		}
	    		
	    	}
	    	nosectionCatMap.put("normalCat", noSectionCats);
	    	nosectionCatMap.put("prefCat", noSectionPrefCats);
	    	
	    	return nosectionCatMap;
	    }
	  //----------------------------------------getAllProducts-----------------------------------------------------
	    public static List<Product> getAllProducts(BrowseQuery requestMessage,SessionUser user, HttpServletRequest request){
	    	String contentId = null;
	    	 //products.clear();
	    	List<Product> products = new ArrayList<Product>();
			contentId = requestMessage.getId();
	    	if (contentId == null) {
	    		contentId = requestMessage.getDepartment();
	    	}
	    	ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(contentId);
	    	if(!(contentNode instanceof CategoryModel )){
	    		LOG.info("The id was not a category. Hence sending an empty Products List ");
	    		return products;
	    	}
	    	getAllProducts((CategoryModel)contentNode,user, request,products);
	    	return products;
	    }
	    
	    
	    private static void getAllProducts(CategoryModel contentNode,SessionUser user, HttpServletRequest request,List<Product> products){
	    	//Assuming only the id which comes in the request is at category level and not at department level...
	    	
	    	
	    	//if the content node is not a category then throw an error
	    	if(contentNode==null || !contentNode.isActive()){
	    		LOG.info("The id was not a category. Hence sending an empty Products List ");
	    		return ;
	    	} else {
	    		//Loop through the content node to get all the products recursively
	    		CategoryModel category = (CategoryModel)contentNode;
	    		List<ProductModel> productModels = category.getProducts();
	    		//Now we need to wrap the product Model and then do a recursive call.
	    		if(productModels!=null && !productModels.isEmpty()){
	    			//So we do have the products wrap them and add.
	    			for(ProductModel productModel : productModels){
	    				try {
							if (passesFilter(productModel, request)) {
								Product product = Product.wrap(productModel, user.getFDSessionUser().getUser(), null, EnumCouponContext.PRODUCT);
								products.add(product);
							}
						} catch (Exception e) {
							//Don't let one rotten egg ruin it for the bunch
		                    LOG.error("ModelException encountered. Product ID=" + productModel.getFullName(), e);
						}
	    				
	    			}
	    		}
	    		List<CategoryModel> subCats = category.getSubcategories();
				if(subCats!=null && !subCats.isEmpty()){
					for(CategoryModel subCat : subCats){
						getAllProducts(subCat, user, request, products);
					}
				} else {
					return;
				}
	    	}
	    	
	    	
	    	
	    	
	    }
}
