package com.freshdirect.mobileapi.util;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.common.pricing.ZoneInfo.PricingIndicator;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.fdstore.content.BannerModel;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategorySectionModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductFilterGroupI;
import com.freshdirect.fdstore.content.ProductFilterGroupModel;
import com.freshdirect.fdstore.content.ProductFilterModel;
import com.freshdirect.fdstore.content.ProductFilterMultiGroupModel;
import com.freshdirect.fdstore.content.ProductItemFilterI;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.SortOptionModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.fdstore.content.TagModel;
import com.freshdirect.fdstore.content.browse.filter.ProductItemFilterFactory;
import com.freshdirect.fdstore.content.browse.filter.TagFilter;
import com.freshdirect.fdstore.content.util.SortStrategyElement;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.util.UnitPriceUtil;
import com.freshdirect.fdstore.zone.FDZoneInfoManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.catalog.model.CatalogInfo;
import com.freshdirect.mobileapi.catalog.model.CatalogInfo.CatalogId;
import com.freshdirect.mobileapi.catalog.model.CatalogKey;
import com.freshdirect.mobileapi.catalog.model.GroupInfo;
import com.freshdirect.mobileapi.catalog.model.ScalePrice;
import com.freshdirect.mobileapi.catalog.model.SkuInfo;
import com.freshdirect.mobileapi.catalog.model.SkuInfo.AlcoholType;
import com.freshdirect.mobileapi.catalog.model.SortOptionInfo;
import com.freshdirect.mobileapi.catalog.model.UnitPrice;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.Lookup;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.controller.data.response.FilterGroup;
import com.freshdirect.mobileapi.controller.data.response.FilterGroupItem;
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
import com.freshdirect.webapp.ajax.filtering.ProductItemFilterUtil;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings;
import com.freshdirect.webapp.taglib.unbxd.BrowseEventTag;

public class BrowseUtil {
	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(BrowseUtil.class);
	
	private static final String ACTION_GET_CATEGORYCONTENT_PRODUCTONLY = "getCategoryContentProductOnly";
	private static final String FILTER_KEY_BRANDS = "brands";
    private static final String FILTER_KEY_TAGS = "tags";    
	
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
    			String redirectContentId = redirectParams.get("catId");
    			if(redirectContentId != null && redirectContentId.trim().length() > 0) {
    				contentId = redirectContentId;
    				currentFolder = ContentFactory.getInstance().getContentNode(redirectContentId);
    			}
				//APPDEV-4237 No Carousel Products
    			else
    			{
    				redirectContentId = redirectParams.get("deptId");
    				if(redirectContentId != null && redirectContentId.trim().length() > 0) {
        				contentId = redirectContentId;
        				currentFolder = ContentFactory.getInstance().getContentNode(redirectContentId);
        			}
    				
    			}
    		}
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
							//categories.add(category);
							//APPDEV 4231
							if(hasProduct(categoryModel)){
								categories.add(category);
							}
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
            eliminateHolidayMealBundleUnavailableProducts(unavailableProducts);
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
	
    private static void eliminateHolidayMealBundleUnavailableProducts(List<Product> unavailableProducts) {
        if (unavailableProducts != null) {
            Iterator<Product> iterator = unavailableProducts.iterator();
            while (iterator.hasNext()) {
                Product product = iterator.next();
                if (FDStoreProperties.getHolidayMealBundleCategoryId().equals(product.getCategoryId())) {
                    iterator.remove();
                }
            }
        }
    }

    // APPDEV 4231 Start
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
		
		public static List<ProductModel> filterFiltersInCategory(SessionUser user, ProductContainer pc, List<ProductModel> productList, List<String> filterIds){
			if(pc == null || productList == null || filterIds == null || filterIds.size() <= 0){
				return null;
			}
			
			List<ContentNodeModel> groups = pc.getProductFilterGroups();
			if(groups == null || groups.size() <= 0)
				return null;
			
			List<ProductModel> toReturn = productList;
			Set<ProductItemFilterI> activeFilters = new HashSet<ProductItemFilterI>(1);
			for(ContentNodeModel group : groups){
				if(group instanceof ProductFilterGroupModel){
					ProductFilterGroupModel pf = (ProductFilterGroupModel) group;
	    			for(ContentNodeModel fm : pf.getProductFilterModels()){
	    				ProductFilterModel pfm = (ProductFilterModel) fm;
	    				if(filterIds.contains(pfm.getContentName())){
	    					activeFilters.add(ProductItemFilterFactory.getInstance().getProductFilter(pfm, pc.getContentName(), user.getFDSessionUser()));    					
	    				}
	    			}
    			} else if(group instanceof ProductFilterMultiGroupModel){
    				TagModel rootTag = ((ProductFilterMultiGroupModel) group).getRootTag();
    				List<TagModel> rootChildren = rootTag.getChildren();
    				if(rootChildren != null && rootChildren.size() > 0){
    					
						List<ProductFilterGroupI> pfglist = ProductItemFilterFactory.getInstance().getProductFilterGroups((ProductFilterMultiGroupModel)group, rootChildren);

						if(pfglist != null & pfglist.size() > 0){
							
							for(ProductFilterGroupI filterGroup : pfglist){
								List<ProductItemFilterI> filterItemList = filterGroup.getProductFilters();
								if(filterItemList != null && filterItemList.size() > 0){
									for(ProductItemFilterI itemFilter : filterItemList){
										if(filterIds.contains(itemFilter.getId())){
											activeFilters.add(itemFilter);
										}
									}
								}

							}
						}
    					
    				}
    				
    			}
    		}
			
			List<FilteringProductItem> filteredItems = ProductItemFilterUtil.getFilteredProducts(
					ProductItemFilterUtil.createFilteringProductItems(productList),
					activeFilters, true, true);
			
			if(filteredItems != null && filteredItems.size() > 0){
				toReturn = new ArrayList<ProductModel>();
				for(FilteringProductItem item : filteredItems){
					toReturn.add(item.getProductModel());
				}
			} else {
				toReturn = new ArrayList<ProductModel>();
			}
			
			return toReturn;
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
	    
	    public static List<DepartmentSection> getDepartmentSectionsEX(DepartmentModel dept){
	    	List<DepartmentSection> sectionList = new ArrayList<DepartmentSection>();
	    	
	    	List<CategoryModel> categories = dept.getCategories();
	    	
	    	return sectionList;
	    }
	    
//-------------------------------------------browse/Navigation-----------------------------------------------------------------------------	    
	    /**
	     * Populate the department section for a given department 
	     * @return
	     */
	    public static List<DepartmentSection> getDepartmentSections(DepartmentModel storeDepartment, boolean isFDX){
	    	
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
	    				List<Category> selectedcategories = buildcategories(entry.getValue(), isFDX);
			    		section.setCategories(selectedcategories);
			    		section.setSectionHeader(sectionNamePref);
			    		departmentSections.add(section);
	    			} else if(entry.getKey().equals("normalCat")){
	    				List<Category> selectedcategories = buildcategories(entry.getValue(), isFDX);
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
	    			List<Category> selectedcategories = buildcategories(catSection.getSelectedCategories(), isFDX);
	    			section.setCategories(selectedcategories);
	    			departmentSections.add(section);
	    		}
	    		
	    		for(Map.Entry<String, List<CategoryModel>> entry :nosectionCatMap.entrySet()){
	    			DepartmentSection section = new DepartmentSection();
	    			//create department section for nosectionCategories for preference categories
	    			if(entry.getKey().equals("prefCat")){
	    				List<Category> selectedcategories = buildcategories(entry.getValue(), isFDX);
			    		section.setCategories(selectedcategories);
			    		section.setSectionHeader(sectionNamePref);
			    		departmentSections.add(section);
	    			} else if(entry.getKey().equals("normalCat")){
	    				List<Category> selectedcategories = buildcategories(entry.getValue(), isFDX);
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
	    private static List<Category> buildcategories(List<CategoryModel> selectedCategories, boolean isFDX){
	    	long startTime = System.currentTimeMillis();
	    	List<Category> categories = new ArrayList<Category>();
	    	for(CategoryModel model : selectedCategories){
	    		Category cat = buildCategoryData(model);
	    		categories.add(cat);
	    	}
//	    	if(!isFDX){
//	    		sortByName(categories);
//	    	}
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
    public static List<Product> getAllProducts(BrowseQuery requestMessage, SessionUser user, HttpServletRequest request) {
        String contentId = null;
        // products.clear();
        List<Product> products = new ArrayList<Product>();
        if (requestMessage.getProductIds() != null && !requestMessage.getProductIds().isEmpty()) {

        } else {
            contentId = requestMessage.getId();
            if (contentId == null) {
                contentId = requestMessage.getDepartment();
            }
            ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(contentId);
            if (!(contentNode instanceof CategoryModel)) {
                LOG.info("The id was not a category. Hence sending an empty Products List ");
                return products;
            }
            sendBrowseEventToAnalytics(request, user.getFDSessionUser(), contentNode);
            getAllProducts((CategoryModel) contentNode, user, request, products);
        }
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
	    		CategoryModel category = contentNode;
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
	    
	    
		/**
		 * @Desc Method will return list of all new products.  
		 * @param user
		 * @param request
		 * @return
		 */
		public static List<Product> getAllNewProductList(SessionUser user,HttpServletRequest request) {
			List<Product> products = new ArrayList<Product>();
			List<ProductModel> items = new ArrayList<ProductModel>();
			String productNewnessKey=getProductNewnessKey(user);
			// Get All new Products
			Map<ProductModel, Map<String,Date>> newProducts = ContentFactory.getInstance().getNewProducts();
			for (Entry<ProductModel, Map<String,Date>> entry : newProducts.entrySet()) {
				
				Map<String,Date> value=entry.getValue();
				if(value.containsKey(productNewnessKey))
					items.add(entry.getKey());
			}
			newProducts = ContentFactory.getInstance().getBackInStockProducts();

			for (Entry<ProductModel, Map<String,Date>> entry : newProducts.entrySet()) {
				Map<String,Date> value=entry.getValue();
				if(value.containsKey(productNewnessKey))
					items.add(entry.getKey());
			}

			// Now we need to wrap the product Model
			if (items != null && !items.isEmpty()) {
				// So we do have the products wrap them and add.
				for (ProductModel productModel : items) {
					try {
						if (passesFilter(productModel, request)) {
							Product product = Product.wrap(productModel, user.getFDSessionUser().getUser(), null,EnumCouponContext.PRODUCT);
							products.add(product);
						}
					} catch (Exception e) {
						// Don't let one rotten egg ruin it for the bunch
						LOG.error("ModelException encountered. Product ID= "+ productModel.getFullName(), e);
					}

				}
			}

			return products;
			
		}

		private static String getProductNewnessKey(SessionUser user) {
			String key="";
			ZoneInfo zone=user.getFDSessionUser().getUserContext().getPricingContext().getZoneInfo();
			if(zone!=null) {
				key=new StringBuilder(5).append(zone.getSalesOrg()).append(zone.getDistributionChanel()).toString();
			}
			return key;
		}
	    public static List<String> getAllProductsEX(BrowseQuery requestMessage, SessionUser user, HttpServletRequest request) throws FDException{
	    	String contentId = null;
	    	 //products.clear();
	    	List<String> products = new ArrayList<String>();
			contentId = requestMessage.getId();
	    	if (contentId == null) {
	    		contentId = requestMessage.getDepartment();
	    	}
	    	
	    	ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(contentId);
	    	List<ProductModel> productList = new ArrayList<ProductModel>();
	    	if((contentNode instanceof CategoryModel ) || (contentNode instanceof DepartmentModel)){
	    		getAllProductsEX(contentNode, requestMessage.getSortBy(), user, request,products, productList);
	    		
	    		if(requestMessage.getFilterByIds() != null && !requestMessage.getFilterByIds().isEmpty()){
	    			
	    			productList = filterFiltersInCategory(user, (ProductContainer)contentNode, productList, requestMessage.getFilterByIds());
	    			
	    		}
    			products = new ArrayList<String>(productList.size());	    		
	    		StringBuilder sb = new StringBuilder();
	    		for(ProductModel pm: productList){
	    			sb.append(pm.getFullName()).append(" - " );
	    		}
//	    		LOG.debug("PRE  SORT BY NAME: " + sb.toString());
	    		sortProductsBy(user, productList, "NAME");
//	    		sb = new StringBuilder();
//	    		for(ProductModel pm: productList){
//	    			sb.append(pm.getFullName()).append(" - " );
//	    		}
//	    		LOG.debug("POST SORT BY NAME: " + sb.toString());
	    		if(requestMessage.getSortBy() != null && !requestMessage.getSortBy().isEmpty()){
	    			sortProductsBy(user, productList, requestMessage.getSortBy());
	    		}
	    			    		
	    		for(ProductModel pm : productList){
//	    			LOG.debug("ProductName: " + pm.getFullName());
	    			products.add(pm.getContentName());
	    		}
	    		sendBrowseEventToAnalytics(request, user.getFDSessionUser(), contentNode);
	    	}
	    	return products;
	    }
	    
	    //TODO: Maybe add a depth int and cut off at 5 recursions deep on a category?
	    private static void getAllProductsEX(ContentNodeModel contentNode, String sortBy, SessionUser user, HttpServletRequest request,List<String> productIds, List<ProductModel> productList) throws FDException{
    		if(contentNode == null)
    			return ;
	    	
	    	if(contentNode instanceof DepartmentModel){
	    		DepartmentModel dept = (DepartmentModel)contentNode;
	    		for(CategoryModel cat : dept.getCategories()){
	    			getAllProductsEX(cat, sortBy, user, request, productIds, productList);
	    		}
	    	} else {

	    		CategoryModel category = (CategoryModel)contentNode;
	    		List<ProductModel> productModels = category.getProducts();
	    		//Now we need to wrap the product Model and then do a recursive call.
	    		if(productModels!=null && !productModels.isEmpty()){
	    			//So we do have the products wrap them and add.
	    			for(ProductModel productModel : productModels){
	    				try {
							if (passesFilter(productModel, request)) {
								if(!productIds.contains(productModel.getContentName())){
									productIds.add(productModel.getContentName());
									productList.add(productModel);
									
								}
							}
						} catch (Exception e) {
							//Don't let one rotten egg ruin it for the bunch
		                    LOG.error("ModelException encountered. Product ID=" + productModel.getFullName(), e);
						}
	    				
	    			}
	    		}
	    		
            	for(CategoryModel tmp : category.getSubcategories() )
            		getAllProductsEX(tmp, sortBy, user, request, productIds, productList);
	    		
	    	}
	    }
	    
	    public static SortOptionInfo getSortOptionsForCategory(BrowseQuery requestMessage,SessionUser user, HttpServletRequest request){
	    	String contentId = null;
	    	 //products.clear();
	    	SortOptionInfo sortOptions = new SortOptionInfo();
			contentId = requestMessage.getId();
	    	if (contentId == null) {
	    		//Empty string for content id
	    		contentId = "";
	    	}
	    	ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(contentId);
	    	
	    	if(contentNode instanceof CategoryModel){
		    	getSortOptionsForCategory(contentNode,user, request, sortOptions);
		    	//sortOptions.addAll(sortOptionSet);
		    	//NO FILTERS ON DEPARTMENT LEVEL
		    	if(contentNode instanceof CategoryModel){
		    		getFiltersForCategory((CategoryModel)contentNode, sortOptions, requestMessage.getFilterByIds(), user, request);
		    	}
	    	}
	    	
	    	
	    	return sortOptions;
	    }
	    
	    private static List<ProductModel> getProductListForCategory(CategoryModel category){
	    	if(category == null)
	    		return null;
	    	
	    	List<ProductModel> productList = new ArrayList<ProductModel>();
	    	productList.addAll(category.getProducts());
	    	
	    	List<CategoryModel> subCats = category.getSubcategories();
	    	
	    	if(subCats != null && subCats.size() > 0){
	    		for(CategoryModel subCat : subCats){
	    			productList.addAll(getProductListForCategory(subCat));
	    		}
	    	}
	    	
	    	return productList;
	    	
	    }
	    
	    private static void getFiltersForCategory(CategoryModel category, SortOptionInfo soi, List<String> activeFilters, SessionUser user, HttpServletRequest request) {
	    	if(category == null)
	    		return;
	    	long s = System.currentTimeMillis();
	    	FilterGroup fg = null;
	    	FilterGroupItem fgi = null;
	    	List<ProductModel> productList = getProductListForCategory(category);
	    	
	    	if(activeFilters != null && activeFilters.size() > 0){
	    		productList = filterFiltersInCategory(user, category, productList, activeFilters);
	    	}
	    	
	    	List<FilteringProductItem> filteringList = ProductItemFilterUtil.createFilteringProductItems(productList);
	    	
	    	List<ContentNodeModel> pfgiList = category.getProductFilterGroups();
	    	if(pfgiList != null && pfgiList.size() > 0){
	    		for(ContentNodeModel tmp : pfgiList){
	    			if(tmp instanceof ProductFilterGroupModel){
		    			ProductFilterGroupModel pf = (ProductFilterGroupModel) tmp;
		    			
		    			fg = new FilterGroup();
		    			fg.setName(pf.getName());
		    			fg.setId(pf.getContentName());
		    			for(ContentNodeModel fm : pf.getProductFilterModels()){
		    				ProductFilterModel pfm = (ProductFilterModel) fm;
		    				
		    				if(ProductItemFilterUtil.countItemsForFilter(filteringList, ProductItemFilterFactory.getInstance().getProductFilter(pfm, tmp.getParentId(), user.getFDSessionUser()))  > 0){
			    				fgi = new FilterGroupItem();
			    				fgi.setId(pfm.getContentName());
			    				fgi.setLabel(pfm.getName());
			    				fg.addFilterGroupItem(fgi);	    					
		    				}
		    				
		    			}
		    			if(fg.getFilterGroupItems() != null && fg.getFilterGroupItems().size() > 0){
			    			soi.addFilterGroup(fg);
		    			}
	    			} else if (tmp instanceof ProductFilterMultiGroupModel){
	    				//Currently only see this done for Country and region so will use this as base:
	    				String lvl1Id= tmp.getContentName();
	    				String lvl1Name = ((ProductFilterMultiGroupModel) tmp).getLevel1Name();
	    				
	    				fg = new FilterGroup();
	    				fg.setId(tmp.getContentName());
	    				fg.setName(lvl1Name);
	    				
	    				TagModel tmr = ((ProductFilterMultiGroupModel) tmp).getRootTag();

	    		    	FilterGroupItem fgi2 = null;
	    		    	
	    				if(tmr  != null && tmr.getChildren() != null && tmr.getChildren().size() >0){
	    					List<TagModel> tagChildren = tmr.getChildren();
	    					for(TagModel tm : tagChildren){
	    						if(ProductItemFilterUtil.countItemsForFilter(filteringList, new TagFilter(tm, lvl1Id)) > 0){
	    							
	    							fgi = new FilterGroupItem();
	    							fgi.setId(tm.getContentName());
	    							fgi.setLabel(tm.getName());
	    							fgi.setSubGroupName(((ProductFilterMultiGroupModel) tmp).getLevel2Name());
	    							fgi.setSubGroups(new ArrayList<FilterGroupItem>());
	    							List<TagModel> tagSubChildren = tm.getChildren();
	    							if(tagSubChildren != null && tagSubChildren.size() > 0){
	    								for(TagModel stm : tagSubChildren){
		    								if(ProductItemFilterUtil.countItemsForFilter(filteringList, new TagFilter(stm, tmp.getParentId())) > 0){
		    									fgi2 = new FilterGroupItem();
		    									fgi2.setId(stm.getContentName());
		    									fgi2.setLabel(stm.getName());
		    									fgi.addItemToSubGroup(fgi2);
		    								}
	    									
	    								}
	    							}
	    							fg.addFilterGroupItem(fgi);
	    						}
	    					}
	    					
	    				}
	    				if(fg.getFilterGroupItems() != null || fg.getFilterGroupItems().size() > 0){
    					soi.addFilterGroup(fg);
	    				}
	    			}
	    		}
	    	}
	    	LOG.debug("Time to getFilters: " + (System.currentTimeMillis() - s));
	    }
		    
	    
	    private static void getSortOptionsForCategory(ContentNodeModel contentNode, SessionUser user, HttpServletRequest request, SortOptionInfo soi) {
	    	//If Department loop through all categories in it
	    	if(contentNode instanceof DepartmentModel){
	    		DepartmentModel dept = (DepartmentModel) contentNode;
	    		for(CategoryModel m : dept.getCategories()){
	    			getSortOptionsForCategory(m, user, request, soi);
	    		}
	    	} else if(contentNode instanceof CategoryModel){
	    		CategoryModel cat = (CategoryModel)contentNode;
	    		List<SortOptionModel> options = cat.getSortOptions();
	    		SortType tmp;
	    		boolean isToBeAdded = true;
	    		for(SortOptionModel o : options){	
	    			tmp = SortType.wrap(o);
	    			
	    			if(!soi.getSortOptions().contains(tmp))
	    				soi.getSortOptions().add(SortType.wrap(o));
	    		}
	    		
	    		for(SortOptionModel o : options){	
	    			if(soi.getSortOptionsLookup().size()==0){
	    				soi.getSortOptionsLookup().add(new Lookup(SortType.wrap(o).toString(), o.getLabel(), o.getSelectedLabel()));
	    			} else {
	    				isToBeAdded = true;
	    				for(Lookup lookupItr: soi.getSortOptionsLookup()) {
	    					if(o.getLabel().equals(lookupItr.getName())){
	    						isToBeAdded = false;
	    					} 
	    				}
	    				if(isToBeAdded){
	    					soi.getSortOptionsLookup().add(new Lookup(SortType.wrap(o).toString(), o.getLabel(), o.getSelectedLabel()));
	    				}
	    			}
	    		}
	    		
	    		if(cat.isNutritionSort() && soi.getNutritionSortOptions().isEmpty()){
	    			
	    			List<String> nutritionOptions = new ArrayList<String>();
	    			for(ErpNutritionType.Type t : ErpNutritionType.getCommonList()){
	    				nutritionOptions.add(t.getName());
	    			}
	    			soi.setNutritionSortOptions(nutritionOptions);
	    			
	    		}
	    		
	    	}
    		
	    }
	    
	    private static AddressModel getAddress(BrowseQuery requestMessage) {
	    	
	    	AddressModel a = new AddressModel();
	        a.setZipCode(requestMessage.getZipCode());
	        a.setAddress1(requestMessage.getAddress1());
	        a.setApartment(requestMessage.getApartment());
	        a.setCity(requestMessage.getCity());
	        a.setState(requestMessage.getState());
	        a.setServiceType(EnumServiceType.getEnum(requestMessage.getServiceType()));
	        return a;
	    	
	    }
	    
	    private static List<com.freshdirect.mobileapi.catalog.model.Product> getProductsForCategory(SessionUser user, CatalogInfo catalog, CategoryModel category, Set<String> productSet, List<ProductModel> productList, String plantId, PricingContext pc) {
	    	if(category == null)
	    		return null;
	    	
	    	List<com.freshdirect.mobileapi.catalog.model.Product> returnableProductList = new ArrayList<com.freshdirect.mobileapi.catalog.model.Product>();
	    	com.freshdirect.mobileapi.catalog.model.Category cat=new com.freshdirect.mobileapi.catalog.model.Category(category.getContentName(), category.getFullName());
	    	
	    	List<ProductModel> catProducts = new ArrayList<ProductModel>();
    		
	    	
	    	for(CategoryModel _category: category.getSubcategories()) {
	    		cat.addCategory(_category.getContentName());
	    		List<ProductModel> subCatProducts = new ArrayList<ProductModel>();
	    		returnableProductList.addAll(getProductsForCategory(user, catalog, _category, productSet, subCatProducts, plantId, pc ));
	    		catProducts.addAll(subCatProducts);
	    	}
	    	
		    	List<ProductModel> pm=category.getProducts();
		    	
		    	for(ProductModel p:pm) {
		    		
		    		if(p.isTemporaryUnavailableOrAvailable() ) {
						//display(p);
						if(!productSet.contains(p.getContentName())) {
		    				com.freshdirect.mobileapi.catalog.model.Product.ProductBuilder prodBuilder=new com.freshdirect.mobileapi.catalog.model.Product.ProductBuilder(p.getContentName(),p.getFullName());
		    				prodBuilder.brandTags(p.getBrands())
					            .minQty(p.getQuantityMinimum())
					            .maxQty(p.getQuantityMaximum())
					            .incrementQty(p.getQuantityIncrement())
					            .quantityText(p.getQuantityText())
					            .images(getImages(p))
					            .tags(p.getTags())
					            .generateWineAttributes(p)
		    					.addKeyWords(p.getKeywords())
		    					.generateAdditionTagsFromProduct(p)
		    					//.setAvailability(p)
		    					.skuInfo(getSkuInfo(p,plantId,pc ))
		    					.productLayout(p.getProductLayout().getId());		    				
		    				com.freshdirect.mobileapi.catalog.model.Product product=prodBuilder.build();
		    				productSet.add(p.getContentName());
	    					returnableProductList.add(product);
						}
						catProducts.add(p);
	
						
		    		}
					
				}
	    	
	    	if(catProducts.size() > 0){
	    		sortProductByPopularity(catProducts, user);
	    		for(ProductModel pm1 : catProducts){
	    			cat.addProduct(pm1.getContentName());
	    		}
	    	}
	    	
	    	
	    	productList.addAll(catProducts);
	    	
	    	if(cat.getCategories().size()>0 || cat.getProducts().size()>0)
	    		catalog.addCategory(cat);
	    	
	    	return returnableProductList;
	    }
	    
	    public static List<com.freshdirect.mobileapi.catalog.model.Product> getProducts(List<String> productIds, String plantId, PricingContext pricingContext, List<String> errors) {
	        List<com.freshdirect.mobileapi.catalog.model.Product> products = new ArrayList<com.freshdirect.mobileapi.catalog.model.Product>();
	        if (productIds != null) {
	            for (String productId : productIds) {
	                try {
	                    ProductModel productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.decode("Product:" + productId));
	                    if (productModel != null && productModel.isTemporaryUnavailableOrAvailable()) {
	                        com.freshdirect.mobileapi.catalog.model.Product.ProductBuilder productBuilder = new com.freshdirect.mobileapi.catalog.model.Product.ProductBuilder(
	                                productModel.getContentName(), productModel.getFullName());
	                        productBuilder.brandTags(productModel.getBrands())
	                        .minQty(productModel.getQuantityMinimum())
	                        .maxQty(productModel.getQuantityMaximum())
	                        .incrementQty(productModel.getQuantityIncrement())
	                        .quantityText(productModel.getQuantityText())
	                        .images(BrowseUtil.getImages(productModel))
	                        .tags(productModel.getTags())
	                        .generateWineAttributes(productModel)
	                        .addKeyWords(productModel.getKeywords())
	                        .generateAdditionTagsFromProduct(productModel)
	                        .skuInfo(BrowseUtil.getSkuInfo(productModel, plantId, pricingContext))
	                        .productLayout(productModel.getProductLayout().getId());
	                        products.add(productBuilder.build());
	                    } else {
	                        errors.add(productId);
	                    }
	                } catch (Exception e) {
	                    errors.add(productId);
	                }
	            }
	        }
	        return products;
	    }
	    
	    private static boolean sortProductByPopularity(List nodes, SessionUser user){
	    	//First sort the list
	    	ItemSorterTagWrapper wrapper = new ItemSorterTagWrapper(user);
	    	List<SortStrategyElement> sstl = new ArrayList<SortStrategyElement>();
	    	sstl.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_POPULARITY));
	    	try {
				wrapper.sort(nodes, sstl);
			} catch (FDException ignored) {
				return false;
			}
    	
	    	return true;
	    }
	    
	    private static List<com.freshdirect.mobileapi.catalog.model.Product> getProductsForCategory(CatalogInfo catalog,CategoryModel category,Set<String> productSet,String plantId,PricingContext pc) {
	    	
	    	if(category==null)
	    		return null;
	    	com.freshdirect.mobileapi.catalog.model.Category cat=new com.freshdirect.mobileapi.catalog.model.Category(category.getContentName(), category.getFullName());
	    	
	    	List<ProductModel> pm=category.getProducts();
	    	//display(category);
	    	List<com.freshdirect.mobileapi.catalog.model.Product> productList=new ArrayList<com.freshdirect.mobileapi.catalog.model.Product>();
	    	for(ProductModel p:pm) {
	    		//SkuModel sku=p.getDefaultSku();
	    		if(p.isTemporaryUnavailableOrAvailable()) {
					//display(p);
					if(!productSet.contains(p.getContentName())) {
	    				com.freshdirect.mobileapi.catalog.model.Product.ProductBuilder prodBuilder=new com.freshdirect.mobileapi.catalog.model.Product.ProductBuilder(p.getContentName(),p.getFullName());
	    				prodBuilder.brandTags(p.getBrands())
				            .minQty(p.getQuantityMinimum())
				            .maxQty(p.getQuantityMaximum())
				            .incrementQty(p.getQuantityIncrement())
				            .quantityText(p.getQuantityText())
				            .images(getImages(p))
				            .tags(p.getTags())
				            .generateWineAttributes(p)
	    					.addKeyWords(p.getKeywords())
	    					.generateAdditionTagsFromProduct(p)
	    					.skuInfo(getSkuInfo(p,plantId,pc ))
	    					.productLayout(p.getProductLayout().getId());
	    				com.freshdirect.mobileapi.catalog.model.Product product=prodBuilder.build();
	    				productSet.add(p.getContentName());
    					productList.add(product);
					} 
					cat.addProduct(p.getContentName());
	    		}
				
			}
	    	List<CategoryModel> subCategories=category.getSubcategories();
	    	for(CategoryModel _category:subCategories) {
	    		cat.addCategory(_category.getContentName());
	    		productList.addAll(getProductsForCategory(catalog,_category,productSet,plantId,pc));
	    	}
	    	
	    	if(cat.getCategories().size()>0 || cat.getProducts().size()>0)
	    		catalog.addCategory(cat);
	    	return productList;
	    }
	    
	    public static CatalogInfo __getAllProducts(BrowseQuery requestMessage,SessionUser user){
	    	int productCount=0;
	    	CatalogInfo catalogInfo;
	    	String plantId;
	    	PricingContext pc;
	    	boolean isFDX = false;
	    	if(requestMessage.getCatalogKey() != null){
	    		isFDX = requestMessage.getCatalogKey().geteStore().contains("FDX");
	    		catalogInfo = getCatalogInfo(requestMessage.getCatalogKey());
	    		plantId = catalogInfo.getKey().getPlantId();
	    		pc = new PricingContext(catalogInfo.getKey().getPricingZone());
		    	catalogInfo.setShowKey(false);
	    	} else {
		    	catalogInfo=getCatalogInfo(requestMessage,user);
		    	plantId=user.getFDSessionUser().getUserContext().getFulfillmentContext().getPlantId();
		    	pc=user.getFDSessionUser().getUserContext().getPricingContext();
		    	user.setUserContext();
		    	
	    	}


	    	StoreModel sm=ContentFactory.getInstance().getStore();
	    	List<DepartmentModel> depts=sm.getDepartments();
	    	List<com.freshdirect.mobileapi.catalog.model.Product> productList=new ArrayList<com.freshdirect.mobileapi.catalog.model.Product>();
	    	Set<String> productSet=new HashSet<String>();
	    	if(!isFDX)
	    		isFDX = sm.getContentName().contains("FDX");
	    	
	    	if(!isFDX){
		    	for(DepartmentModel d:depts) {
		    		List<CategoryModel> cm=d.getCategories();
		    		for(CategoryModel c:cm) {
		    			productList.addAll(getProductsForCategory(catalogInfo,c,productSet,plantId,pc));
		    		}
		    		
		    	}
	    	} else {
	    		ContentFactory.getInstance().setCurrentUserContext(user.getFDSessionUser().getUserContext());
		    	for(DepartmentModel d:depts) {
		    		
		    		List<CategoryModel> cm=d.getCategories();
		    		com.freshdirect.mobileapi.catalog.model.Category cat = new com.freshdirect.mobileapi.catalog.model.Category(d.getContentName(), d.getFullName());
		    		catalogInfo.addCategory(cat);
		    		List<ProductModel> allProductsInDept = new ArrayList<ProductModel>();
		    		for(CategoryModel c : cm){
		    			cat.addCategory(c.getContentName());
		    			productList.addAll(getProductsForCategory(user, catalogInfo, c, productSet, allProductsInDept, plantId, pc));
	//	    			allProductsInDept.addAll(getProductsForCategory(catalogInfo, c, plantId, pc, user, productSet, productList));
		    		}
		    		//Adding this for a commit check
		    		sortProductByPopularity(allProductsInDept, user);
		    		
		    		for(Object pmo : allProductsInDept){
		    			cat.addProduct(((ProductModel)pmo).getContentName());
		    		}
		    		
		    	}
	    	}
	    	String val=requestMessage.getProductCount();
	    	
	    	try {
	    		productCount=Integer.parseInt(val);
	    		if(productCount<0)
	    			productCount=-1;
	    	} catch(Exception e) {
	    		productCount=10;
	    	}
	    	
	    	if(productCount!=0) {
	    		
	    		if(productCount!=-1 && productList.size()>productCount)  
	    			productList=productList.subList(0, productCount);
	    		catalogInfo.addProducts(productList);
	    	}
	    	return catalogInfo;
	    }
	    
	    public static CatalogInfo getCatalogInfo(CatalogKey key){
	    	CatalogId catid = new CatalogInfo.CatalogId(key.geteStore(), Long.toString(key.getPlantId()), key.getPricingZone());
	    	return new CatalogInfo(catid);
	    }

    public static CatalogInfo getCatalogInfo(BrowseQuery requestMessage, SessionUser user) {
    	if(user.getFDSessionUser()!=null 
    			&& user.getFDSessionUser().getIdentity()!=null
    			&& user.getAddress()!=null
    			&& !user.getAddress().isCustomerAnonymousAddress()){
    		user.setAddress(getAddress(requestMessage));
    	}
        return getCatalogInfo(user);
    }

    public static CatalogInfo getCatalogInfoAddr(AddressModel address, SessionUser user) {
        user.setAddress(address);
        return getCatalogInfo(user);
    }

    public static CatalogInfo getCatalogInfo(SessionUser user) {
        String plantId = user.getFDSessionUser().getUserContext().getFulfillmentContext().getPlantId();
        PricingContext pc = user.getFDSessionUser().getUserContext().getPricingContext();
        user.setUserContext();
        CatalogId catalogId = new CatalogInfo.CatalogId(ContentFactory.getInstance().getStoreKey().getId(), plantId, pc.getZoneInfo());
        return new CatalogInfo(catalogId);
    }

    public static String getPlantId(SessionUser user) {
        String plantid = null;
        if (user != null) {
            String zipcode = user.getZipCode();
            if (zipcode != null && zipcode.trim().length() > 0) {
                ErpAddressModel address = new ErpAddressModel();
                address.setZipCode(zipcode);
                plantid =user.getFDSessionUser().getUserContext().getFulfillmentContext().getPlantId();
                //plantid = BrowseUtil.getCatalogInfoAddr(address, user).getKey().getPlantId();
            } else {
                plantid = FDStoreProperties.getDefaultFdxPlantID();
            }
        }
        return plantid;
    }
	    
	    public static List<String> getAllFDXCatalogKeys(){
	    	
	    	String eStore = ContentFactory.getInstance().getStoreKey().getId();
			List<CatalogKey> keyList = null;
	    	
	    	try {
				List<String> zoneIds = new ArrayList<String>(FDZoneInfoManager.loadAllZoneInfoMaster());
				ZoneInfo plant1k;
				ZoneInfo plant1300;
				ZoneInfo plant1310;
				CatalogKey tmp;
				keyList = new ArrayList<CatalogKey>(zoneIds.size() * 2);
				for(String zoneId : zoneIds){
					//TODO: replace stubs with something else
					//Currently using stubs for sales and distribution
					plant1k = new ZoneInfo(zoneId, "0001", "01");
					tmp = new CatalogKey();
					tmp.seteStore(eStore);
					tmp.setPlantId(1000);
					tmp.setPricingZone(plant1k);
					keyList.add(tmp);
					//Currently using stubs for sales and distribution
					plant1300 = new ZoneInfo(zoneId, "1300", "01",PricingIndicator.BASE, plant1k);
					tmp = new CatalogKey();
					tmp.seteStore(eStore);
					tmp.setPlantId(1300);
					tmp.setPricingZone(plant1300);
					keyList.add(tmp);
					
					//Currently using stubs for sales and distribution
					plant1310 = new ZoneInfo(zoneId, "1310", "01",PricingIndicator.BASE, plant1k);
					tmp = new CatalogKey();
					tmp.seteStore(eStore);
					tmp.setPlantId(1310);
					tmp.setPricingZone(plant1310);
					keyList.add(tmp);
					
				}
				
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
	    	
	    	if(keyList != null && keyList.size() > 0){
	    		List<String> stringyfiedKeyList = new ArrayList<String>(keyList.size());
	    		for(CatalogKey key : keyList){
	    			stringyfiedKeyList.add(key.toString());
	    		}
	    		return stringyfiedKeyList;
	    	}
	    	
	    	return null;
	    }
	    
	    public static SkuInfo getSkuInfo(ProductModel prodModel,String plantID,PricingContext context) {
	    	 SkuModel sku=prodModel.getDefaultSku();
	    	 if(sku==null && prodModel.getSkus().size()>0) {
	    		 sku=prodModel.getSku(0);
	    	 }
	    	 
	    	 if(sku!=null) {
	    		 try {
					FDProductInfo productInfo=sku.getProductInfo();
					
					PriceCalculator pc=new PriceCalculator(context,prodModel,sku);
					
					FDProduct product=sku.getProduct();
					
					SkuInfo skuInfo=new SkuInfo();
					skuInfo.setFreshness(productInfo.getFreshness(plantID));
					skuInfo.setRating(productInfo.getRating(plantID)!=null?productInfo.getRating(plantID).getStatusCode():"");
					skuInfo.setSustainabilityRating(productInfo.getSustainabilityRating(plantID)!=null?productInfo.getSustainabilityRating(plantID).getStatusCode():"");
					skuInfo.setTaxable(product.isTaxable());
					skuInfo.setTaxCode(product.getTaxCode());
					skuInfo.setSkuCode(sku.getSkuCode());
					skuInfo.setProductId(prodModel.getContentName());
					skuInfo.setBasePrice(pc.getWasPrice());
					if(productInfo.getGroup(pc.getPricingContext().getZoneInfo().getSalesOrg(),pc.getPricingContext().getZoneInfo().getDistributionChanel())!=null) {
						skuInfo.setGroupInfo(getGroupInfo(productInfo.getGroup(pc.getPricingContext().getZoneInfo().getSalesOrg(),pc.getPricingContext().getZoneInfo().getDistributionChanel()),pc));
					}
					skuInfo.setSalesUnits(getSalesUnits(product.getSalesUnits()));
					if(product.getSalesUnits().length>0) {
						skuInfo.setUnitPrice(getUnitPrice(product.getSalesUnits()[0],pc));
					}
					if(sku.getSkuCode()!=null) {
						if(pc.getProduct()!=null)
							skuInfo.setScalePrice(getScalePrice(pc.getZonePriceModel()));
						
					}
					skuInfo.setAlcoholType(getAlcoholType(product));
					EnumAvailabilityStatus status = productInfo.getAvailabilityStatus(pc.getPricingContext().getZoneInfo().getSalesOrg(), pc.getPricingContext().getZoneInfo().getDistributionChanel());
					
					if(status == null || prodModel.isUnavailable()) {
						skuInfo.setAvailable(EnumAvailabilityStatus.TEMP_UNAV.getId());
					} else if(EnumAvailabilityStatus.TO_BE_DISCONTINUED_SOON.equals(status)){//APPDEV-4653-Material status 40
						LOG.info(productInfo.getSkuCode()+ " is in 40 status");
						skuInfo.setAvailable(EnumAvailabilityStatus.AVAILABLE.getId());
					} else {
						skuInfo.setAvailable(status.getId());
					}
					
					boolean isLimitedQuantity = false;
					isLimitedQuantity = productInfo.isLimitedQuantity(plantID);
					/*//TODO: Fix it - This is only for test
					if(!isLimitedQuantity){
						isLimitedQuantity = productInfo.isLimitedQuantity(pc.getPricingContext().getZoneInfo().getSalesOrg(),pc.getPricingContext().getZoneInfo().getDistributionChanel());
					}*/
					skuInfo.setLimitedQuantity(isLimitedQuantity);
					return skuInfo;
				} catch (FDResourceException e) {
					LOG.error("Error in getSkuInfo()=>"+sku.getSkuCode()+" "+e.toString());
				} catch (FDSkuNotFoundException e) {
					LOG.error("Error in getSkuInfo()=>"+sku.getSkuCode()+" "+e.toString());
				} catch (FDException e) {
					LOG.error("Error in getSkuInfo()=>"+sku.getSkuCode()+" "+e.toString());
				} catch (FDRuntimeException e){
					LOG.error("Error in getSkuInfo()=>"+sku.getSkuCode()+" " + e.toString());
				}
	    	 }
	    	 return null;
	    }
	    
	    private static AlcoholType getAlcoholType(FDProduct product) {
	    	if(!product.isAlcohol())
	    		return AlcoholType.NON_ALCOHOLIC;
	    	else if (product.isBeer())
	    		return AlcoholType.BEER;
	    	return AlcoholType.WINE_AND_SPIRITS;
	    }
	    private static List<com.freshdirect.mobileapi.catalog.model.SalesUnit> getSalesUnits(FDSalesUnit[] salesUnits) {
	    	List<com.freshdirect.mobileapi.catalog.model.SalesUnit> su=new ArrayList<com.freshdirect.mobileapi.catalog.model.SalesUnit>(salesUnits.length);
			for(int i=0;i<salesUnits.length;i++) {
				su.add(getSalesUnit(salesUnits[i]));
			}
			return su;
	    }
	    private static GroupInfo getGroupInfo(FDGroup group, PriceCalculator pc ) {
	    	
			if(group!=null) {
				GroupInfo grp=new GroupInfo();
				grp.setId(group.getGroupId());
				grp.setVersion(group.getVersion());
				grp.setMinQty(pc.getGroupQuantity());
				grp.setName(pc.getGroupShortOfferDescription());
				grp.setOffer(pc.getGroupLongOfferDescription());
				grp.setPrice(pc.getGroupPrice());
				return grp;
			}
			
			return null;	
	    }
	    
	    private static List<ScalePrice> getScalePrice(ZonePriceModel pm) {
	    	List<ScalePrice> scalePrices=new ArrayList<ScalePrice>();
	    	if(pm==null)
	    		return scalePrices;
	    	MaterialPrice[] mp=pm.getMaterialPrices();
	    	
	    	for(int i=0;i<mp.length;i++) {
	    		//double price, String pricingUnit, double scaleLowerBound, double scaleUpperBound, String scaleUnit, double promoPrice
	    		ScalePrice sp=new ScalePrice(mp[i].getOriginalPrice(),mp[i].getPricingUnit(),mp[i].getScaleLowerBound(),mp[i].getScaleUpperBound(),mp[i].getScaleUnit(),mp[i].getPromoPrice());
	    		scalePrices.add(sp);
	    	}
	    	return scalePrices;
	    }
	    private static UnitPrice getUnitPrice(FDSalesUnit fdSalesUnit,PriceCalculator pc) {
	    	UnitPrice up=new UnitPrice();
	    	up.setUnitPriceDenominator(fdSalesUnit.getUnitPriceDenominator());
	    	up.setUnitPriceDescription(fdSalesUnit.getUnitPriceDescription());
	    	up.setUnitPriceNumerator(fdSalesUnit.getUnitPriceNumerator());
	    	up.setUnitPriceUOM(fdSalesUnit.getUnitPriceUOM());
	    	try {
	    		if(pc.getZonePriceInfoModel()!=null)
				up.setPriceText(UnitPriceUtil.getUnitPrice(fdSalesUnit, pc.getZonePriceInfoModel().getDefaultPrice()));
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FDSkuNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return up;
	    	
	    }
	    private static com.freshdirect.mobileapi.catalog.model.SalesUnit getSalesUnit(FDSalesUnit fdSalesUnit) {
	    	com.freshdirect.mobileapi.catalog.model.SalesUnit su=new com.freshdirect.mobileapi.catalog.model.SalesUnit();
	    	su.setBaseUnit(fdSalesUnit.getBaseUnit());
	    	su.setDenominator(fdSalesUnit.getDenominator());
	    	su.setDescription("nm".equalsIgnoreCase(fdSalesUnit.getDescription()) ? null : fdSalesUnit.getDescription());
	    	su.setName(fdSalesUnit.getName());
	    	su.setNumerator(fdSalesUnit.getNumerator());
	       	return su;
	    }
	    
	    public static List<com.freshdirect.fdstore.content.Image> getImages(ProductModel p) {
	    	
		    List<com.freshdirect.fdstore.content.Image> images=new ArrayList<com.freshdirect.fdstore.content.Image>(4);
			images.add(p.getThumbnailImage());
			images.add(p.getCategoryImage());
			images.add(p.getDetailImage());
			images.add(p.getZoomImage());
			return images;
	    }
	    
	    private static void sortProductsBy(SessionUser user, List<ProductModel> products, String sortBy) throws FDException{
	    	List<SortStrategyElement> list = new ArrayList<SortStrategyElement>();
	    	SortType passedSortType = SortType.valueFromString(sortBy);
	    	int element;
        	switch (passedSortType) {                	

        	case NAME:
        		element = SortStrategyElement.PRODUCTS_BY_NAME;
        		break; 
        	case PRICE:
        		element = SortStrategyElement.PRODUCTS_BY_PRICE;
        		break; 
        	case POPULARITY:
        		LOG.debug("sorting By by popularity");
        		element = SortStrategyElement.PRODUCTS_BY_POPULARITY;
        		LOG.debug("sorting By by popularity element " + element);
        		break;
        	case SALE:
        		element = SortStrategyElement.PRODUCTS_BY_SALE;
        		break;

        	case EXPERT_RATING:
        		element = SortStrategyElement.PRODUCTS_BY_RATING;
        		break;

        	case SUSTAINABILITY_RATING:
        		element = SortStrategyElement.PRODUCTS_BY_SEAFOOD_SUSTAINABILITY;
        		break;
			default:
				element = SortStrategyElement.NO_SORT;								
				break;
			}
			
        	if(element == SortStrategyElement.NO_SORT){
            	ErpNutritionType.Type tmp = ErpNutritionType.getType(sortBy);
            	
            	if(tmp == null){
                	list.add(new SortStrategyElement(element));	 
            	} else {
                	list.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NUTRITION, ErpNutritionType.getType(sortBy).getDisplayName(), false));
            	}
            	
        	} else {
        		if(element == SortStrategyElement.PRODUCTS_BY_RATING){
        			list.add(new SortStrategyElement(element,true));
        		}
        		else
        		{
        			list.add(new SortStrategyElement(element));
        		}
        	}
            ItemSorterTagWrapper sortTagWrapper = new ItemSorterTagWrapper(user);
            sortTagWrapper.sort(products, list);

	    }
	    
	    public static void main(String[] ax) {
	    	List<String> a =new ArrayList<String>(4);
	    	a.add(null);
	    	a.add(null);a.add(null);a.add(null);
	    	System.out.println(a.size());
	    }
	    
	    private static void sendBrowseEventToAnalytics(HttpServletRequest request, FDUserI user, ContentNodeModel model){
	        if(FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdintegrationblackhole2016, request.getCookies(), user)){
                BrowseEventTag.doSendEvent(model.getContentKey().getId(), user, request);
            }
	    }
}
