package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.browse.filter.BrandFilter;
import com.freshdirect.fdstore.content.browse.filter.ContentNodeFilter;
import com.freshdirect.fdstore.content.browse.filter.KosherFilter;
import com.freshdirect.fdstore.content.browse.filter.NewProductFilter;
import com.freshdirect.fdstore.content.browse.filter.OnSaleFilter;
import com.freshdirect.fdstore.content.browse.filter.OrganicFilter;
import com.freshdirect.fdstore.content.browse.filter.ProductItemFilterFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.Domain;
import com.freshdirect.storeapi.content.DomainValue;
import com.freshdirect.storeapi.content.EnumBrandFilterLocation;
import com.freshdirect.storeapi.content.GlobalNavigationModel;
import com.freshdirect.storeapi.content.ProductContainer;
import com.freshdirect.storeapi.content.ProductFilterGroupI;
import com.freshdirect.storeapi.content.ProductFilterGroupImpl;
import com.freshdirect.storeapi.content.ProductFilterMultiGroupModel;
import com.freshdirect.storeapi.content.ProductItemFilterI;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.RecipeSearchPage;
import com.freshdirect.storeapi.content.SuperDepartmentModel;
import com.freshdirect.storeapi.content.TagModel;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.cache.EhCacheUtilWrapper;
import com.freshdirect.webapp.globalnav.GlobalNavContextUtil;

public class NavigationUtil {
	
	public static final String RECIPE_CATEGORY_FILTER_GROUP = "recipeCategoryFilterGroup";
	public static final String SHOW_ME_ONLY_FILTER_GROUP_ID = "showMeOnlyFilterGroup";
	public static final String BRAND_FILTER_GROUP_ID = "brandFilterGroup";
	public static final String DEPARTMENT_FILTER_GROUP_ID = "departmentFilterGroup";
	public static final String CATEGORY_FILTER_GROUP_ID = "categoryFilterGroup";
	public static final String SUBCATEGORY_FILTER_GROUP_ID = "subCategoryFilterGroup";
	
    public static NavigationModel createNavigationModel(ContentNodeModel node, CmsFilteringNavigator navigator, FDUserI user) throws InvalidFilteringArgumentException,
            FDResourceException {
		
		NavigationModel model = new NavigationModel();
		
		model.setUser(user);

		LinkedList<ContentNodeModel> contentNodeModelPath = new LinkedList<ContentNodeModel>();
		
		model.setSelectedContentNodeModel(node);
		model.setContentNodeModelPath(contentNodeModelPath);
		
		// get parents and breadcrumb
		ContentNodeModel tempNode = node;
		while (true){
			contentNodeModelPath.push(tempNode);
			if (tempNode instanceof SuperDepartmentModel){
				break;
			}
			if (tempNode instanceof DepartmentModel){
				break;
			}
			tempNode=tempNode.getParentNode();
			if (tempNode==null){
				break;
			}
		}

        if (node instanceof CategoryModel) {
            if (EnumEStoreId.FDX == CmsManager.getInstance().getEStoreEnum()) {
                model.setPageTitle(((CategoryModel) node).getFdxPageTitle());
                model.setMetaDescription(((CategoryModel) node).getFdxSEOMetaDescription());
            } else {
                model.setPageTitle(((CategoryModel) node).getPageTitle());
                model.setMetaDescription(((CategoryModel) node).getSEOMetaDescription());
            }
        }

        if (node instanceof SuperDepartmentModel) {
            if (EnumEStoreId.FDX == CmsManager.getInstance().getEStoreEnum()) {
                model.setPageTitle(((SuperDepartmentModel) node).getFdxPageTitle());
                model.setMetaDescription(((SuperDepartmentModel) node).getFdxSEOMetaDescription());
            } else {
                model.setPageTitle(((SuperDepartmentModel) node).getPageTitle());
                model.setMetaDescription(((SuperDepartmentModel) node).getSEOMetaDescription());
            }
        }

        if (node instanceof DepartmentModel) {
            if (EnumEStoreId.FDX == CmsManager.getInstance().getEStoreEnum()) {
                model.setPageTitle(((DepartmentModel) node).getFdxPageTitle());
                model.setMetaDescription(((DepartmentModel) node).getFdxSEOMetaDescription());
            } else {
                model.setPageTitle(((DepartmentModel) node).getPageTitle());
                model.setMetaDescription(((DepartmentModel) node).getSEOMetaDescription());
            }
        }

		String id = navigator.getId();
		
		// validation for orphan categories
		if(!(contentNodeModelPath.get(0) instanceof DepartmentModel) && !model.isSuperDepartment()){
			throw new InvalidFilteringArgumentException("Orphan category: "+id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, id);
		}
		
		int contentNodeModelPathSize = contentNodeModelPath.size();
		if(!navigator.isPdp()){
			if (contentNodeModelPathSize > NavDepth.getMaxLevel()+1){ //validate if level not too deep
				throw new InvalidFilteringArgumentException("Category is too deep in hierarchy: "+ id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, id);
			}			
		}
		
		// determine the depth of the navigation
		Map<NavDepth, ContentNodeModel> navigationHierarchy = new HashMap<NavDepth, ContentNodeModel>();
		for(NavDepth navDepth : NavDepth.values()){
			if (contentNodeModelPath.size()>navDepth.getLevel()){
				model.setNavDepth(navDepth);
				// skip if superdepartment page
				if (!(NavDepth.DEPARTMENT.equals(navDepth) && !(contentNodeModelPath.get(navDepth.getLevel()) instanceof DepartmentModel))) {
					navigationHierarchy.put(navDepth, contentNodeModelPath.get(navDepth.getLevel()));
				}
			}
		}
		model.setNavigationHierarchy(navigationHierarchy);
		
		// find superdepartment in hierarchy if any
		if(navigationHierarchy.get(NavDepth.DEPARTMENT)!=null){
			model.setSuperDepartmentModel(getSuperDepartment(user, navigationHierarchy.get(NavDepth.DEPARTMENT).getContentName()));			
		} else if (node instanceof SuperDepartmentModel) {
			model.setSuperDepartmentModel((SuperDepartmentModel) node);
		}

		if (!model.isSuperDepartment()) {
			// search for categories and preference categories - prepare for menu building
			List<CategoryModel> regularCategories = new ArrayList<CategoryModel>();
			List<CategoryModel> prefCategories = new ArrayList<CategoryModel>();
			DepartmentModel department = (DepartmentModel) model.getNavigationHierarchy().get(NavDepth.DEPARTMENT);
	
			for (CategoryModel cat : department.getCategories()){
				if (cat instanceof CategoryModel) {
					if (isCategoryHiddenAndSelectedNotShowSelf(user, cat.getContentName(), cat)) {
						continue;
					}
					if (cat.isPreferenceCategory()){
						prefCategories.add(cat);
					} else {
						regularCategories.add(cat);
					}
				}
			}
			model.setRegularCategories(regularCategories);
			model.setPreferenceCategories(prefCategories);
			model.setCategorySections(department.getCategorySections());
			
			if(!navigator.isPdp()) {
				//-- CREATE FILTERS --
			
				// create actual filters
				model.setAllFilters(createFilterGroups((ProductContainer)node, navigator, user));
				
				// select active filters
				model.setActiveFilters(selectActiveFilters(model.getAllFilters(), navigator));
			}
			
			if(((ProductContainer)node).isShowPopularCategories()){
				
				List<CategoryModel> popularCategories = new ArrayList<CategoryModel>();
				
				for(CategoryModel cat : ((ProductContainer)node).getPopularCategories()){
					if (cat.isHideIfFilteringIsSupported() && NavigationUtil.isUserContextEligibleForHideFiltering(user)) {
						continue;
					}
					popularCategories.add(cat);
				}
				
				model.setPopularCategories(popularCategories);
			}
		
		} else if(model.isSuperDepartment()) {
			
			model.setDepartmentsOfSuperDepartment(((SuperDepartmentModel)model.getSelectedContentNodeModel()).getDepartments());
			
			//no filters in case of super department
			model.setAllFilters(new ArrayList<ProductFilterGroupI>());
			model.setActiveFilters(new HashSet<ProductItemFilterI>());
			
		}
		
		// set brand filter location
		if(node instanceof CategoryModel){
			model.setBrandFilterLocation(((CategoryModel)node).getBrandFilterLocation(EnumBrandFilterLocation.BELOW_LOWEST_LEVEL_CATEGROY.toString()));
		}
		
		// -- CREATE MENU --
		// create menuBuilder based on page type
		MenuBuilderI menuBuilder = MenuBuilderFactory.createBuilderByPageType(model.getNavDepth(), model.isSuperDepartment(), null); //TODO

		// are we showing products?
		model.setProductListing( !model.isSuperDepartment() && model.getNavDepth()!=NavDepth.DEPARTMENT && (
				model.getNavDepth()!=NavDepth.CATEGORY || 
				((CategoryModel)model.getNavigationHierarchy().get(NavDepth.CATEGORY)).getSubcategories().size()==0 || 
				navigator.isAll()));
		
		// create menu
		model.setLeftNav(menuBuilder.buildMenu(model.getAllFilters(), model, navigator));
		
		return model;
	}
	
	/**
	 * @param productContainer
	 * @param navigator
	 * @return
	 * create the flat filter group structure
	 */
	public static List<ProductFilterGroupI> createFilterGroups(ProductContainer productContainer, CmsFilteringNavigator navigator, FDUserI user){
		
		// construct selectionMap from filter parameters
		// check what filters are selected from a multigroup (multigroups can contain tagmodels only)
		Map<String,List<TagModel>> selectionMap = new HashMap<String, List<TagModel>>();

		for (ContentNodeModel multiGroup : productContainer.getProductFilterGroups()){
			if (multiGroup instanceof ProductFilterMultiGroupModel){
				
				for (String param : navigator.getRequestFilterParams().keySet()){
					String multiGroupId = multiGroup.getContentName(); 
					
					if(param.startsWith(multiGroupId)){
						List<TagModel> selection = selectionMap.get(multiGroupId);
						if (selection == null){
							selection = new ArrayList<TagModel>();
							selectionMap.put(multiGroupId, selection);
						}
						
						List<String> paramValues = navigator.getRequestFilterParams().get(param);
						for(String paramValue : paramValues){
							selection.add((TagModel) ContentFactory.getInstance().getContentNode(ContentType.Tag, paramValue));							
						}
					}
				}
			}
		}
		
		// create a flat group hierarchy, create simple groups from the multigroups and create the x level for the selected multigroup filters (selectionMap)
		return ProductItemFilterFactory.getInstance().getDefaultProductFilterGroups(productContainer, selectionMap, user);
		
	}
	
	public static List<ProductFilterGroupI> createSearchFilterGroups(NavigationModel navigationModel, CmsFilteringNavigator navigator, FDUserI user){
		List<ProductFilterGroupI> list = new ArrayList<ProductFilterGroupI>();
		if (navigationModel.isProductListing()) {
			list = createSearchFilterGroupsForProduct(navigationModel, navigator);
		} else if (navigationModel.isRecipeListing()) {
			list = createSearchFilterGroupsForRecipes(navigationModel, navigator);
		}
		return list;
	}

	private static List<ProductFilterGroupI> createSearchFilterGroupsForRecipes(NavigationModel navigationModel, CmsFilteringNavigator navigator) {
		List<ProductFilterGroupI> results = new ArrayList<ProductFilterGroupI>();
		List<ProductItemFilterI> productFilters = new ArrayList<ProductItemFilterI>();
		Map<String, DomainValue> domainValues = new HashMap<String, DomainValue>();
		RecipeSearchPage recipeSearchPage = RecipeSearchPage.getDefault();
		List<Domain> classificationDomains = recipeSearchPage.getFilterByDomains();
		for (Recipe recipe : navigationModel.getRecipeResults()) {
			for (DomainValue domainValue : recipe.getClassifications()) {
				if (classificationDomains.contains(domainValue.getDomain())) {
					domainValues.put(domainValue.getContentName(), domainValue);
				}
			}
		}
		for (String domainValueContentName : domainValues.keySet()) {
			DomainValue domainValue = domainValues.get(domainValueContentName);
			productFilters.add(new ContentNodeFilter(domainValue, RECIPE_CATEGORY_FILTER_GROUP));
		}
		ProductFilterGroupImpl recipeCategoryFilterGroup = new ProductFilterGroupImpl();
		recipeCategoryFilterGroup.setProductFilters(productFilters);
		recipeCategoryFilterGroup.setId(RECIPE_CATEGORY_FILTER_GROUP);
		recipeCategoryFilterGroup.setName("Recipes");
		recipeCategoryFilterGroup.setType("SINGLE");
		recipeCategoryFilterGroup.setAllSelectedLabel("ALL RECIPES");
		recipeCategoryFilterGroup.setDisplayOnCategoryListingPage(false);
		results.add(recipeCategoryFilterGroup);
		return results;
	}

	private static List<ProductFilterGroupI> createSearchFilterGroupsForProduct(NavigationModel navigationModel, CmsFilteringNavigator navigator) {
		List<ProductFilterGroupI> results = new ArrayList<ProductFilterGroupI>();
		List<ProductItemFilterI> productFilters = new ArrayList<ProductItemFilterI>();

		for (DepartmentModel departmentModel : navigationModel.getDepartmentsOfSearchResults().values()) {
			
			productFilters.add(new ContentNodeFilter(departmentModel, DEPARTMENT_FILTER_GROUP_ID));
		}

		ProductFilterGroupImpl departmentFilterGroup = new ProductFilterGroupImpl();
		departmentFilterGroup.setProductFilters(productFilters);
		departmentFilterGroup.setId(DEPARTMENT_FILTER_GROUP_ID);
		departmentFilterGroup.setName("DEPARTMENT");
		departmentFilterGroup.setType("SINGLE");
		departmentFilterGroup.setAllSelectedLabel("ALL DEPARTMENTS");
		departmentFilterGroup.setDisplayOnCategoryListingPage(false);
		
		results.add(departmentFilterGroup);

		if (isFilterActive(navigator, departmentFilterGroup)) { //show only if department filter is selected

			productFilters = new ArrayList<ProductItemFilterI>();

			for (CategoryModel categoryModel : navigationModel.getCategoriesOfSearchResults().values()) {
				if (navigator.getRequestFilterParams().get(departmentFilterGroup.getId()).contains(categoryModel.getDepartment().getContentName())) {
					productFilters.add(new ContentNodeFilter(categoryModel, CATEGORY_FILTER_GROUP_ID));
				}
			}

			ProductFilterGroupImpl categoryFilterGroup = new ProductFilterGroupImpl();
			categoryFilterGroup.setProductFilters(productFilters);
			categoryFilterGroup.setId(CATEGORY_FILTER_GROUP_ID);
			categoryFilterGroup.setName("Category");
			categoryFilterGroup.setType("SINGLE");
			categoryFilterGroup.setAllSelectedLabel("ALL CATEGORIES");
			categoryFilterGroup.setDisplayOnCategoryListingPage(false);

			if (categoryFilterGroup.getProductFilters().size() > 0) {
				departmentFilterGroup.setType("POPUP");
				results.add(categoryFilterGroup);
			}
	
			if (isFilterActive(navigator, categoryFilterGroup)) { //show only if category filter is selected

				productFilters = new ArrayList<ProductItemFilterI>();
				
				for (CategoryModel subCategoryModel : navigationModel.getSubCategoriesOfSearchResults().values()) {
					if (navigator.getRequestFilterParams().get(categoryFilterGroup.getId()).contains(subCategoryModel.getParentNode().getContentName()) && navigator.getRequestFilterParams().get(departmentFilterGroup.getId()).contains(subCategoryModel.getDepartment().getContentName())) {
						productFilters.add(new ContentNodeFilter(subCategoryModel, SUBCATEGORY_FILTER_GROUP_ID));
					}
				}
				ProductFilterGroupImpl subCategoryFilterGroup = new ProductFilterGroupImpl();
				subCategoryFilterGroup.setProductFilters(productFilters);
				subCategoryFilterGroup.setId(SUBCATEGORY_FILTER_GROUP_ID);
				subCategoryFilterGroup.setName("SubCategory");
				subCategoryFilterGroup.setType("SINGLE");
				subCategoryFilterGroup.setAllSelectedLabel("ALL SUBCATEGORIES");
				subCategoryFilterGroup.setDisplayOnCategoryListingPage(false);
				
				if (subCategoryFilterGroup.getProductFilters().size() > 0) {
					categoryFilterGroup.setType("POPUP");
					results.add(subCategoryFilterGroup);
				}
			}
		}
		
		productFilters = new ArrayList<ProductItemFilterI>();

		for (BrandModel brandModel : navigationModel.getBrandsOfSearchResults().values()) {
			
			productFilters.add(new BrandFilter(brandModel, BRAND_FILTER_GROUP_ID));
		}

		ProductFilterGroupImpl brandFilterGroup = new ProductFilterGroupImpl();
		brandFilterGroup.setProductFilters(productFilters);
		brandFilterGroup.setId(BRAND_FILTER_GROUP_ID);
		brandFilterGroup.setName("Brand");
		brandFilterGroup.setType("POPUP");
		brandFilterGroup.setAllSelectedLabel("All Brands");
		brandFilterGroup.setDisplayOnCategoryListingPage(false);
		
		results.add(brandFilterGroup);
		
		productFilters = new ArrayList<ProductItemFilterI>();
		
		for (String showMeOnlyItem : navigationModel.getShowMeOnlyOfSearchResults()) {
			if (showMeOnlyItem.equals("new")) {
				productFilters.add(new NewProductFilter("new", SHOW_ME_ONLY_FILTER_GROUP_ID, "New"));
			}
			if (showMeOnlyItem.equals("kosher")) {
				productFilters.add(new KosherFilter("kosher", SHOW_ME_ONLY_FILTER_GROUP_ID, "Kosher"));
			}
			if (showMeOnlyItem.equals("onsale")) {
				productFilters.add(new OnSaleFilter("onsale", SHOW_ME_ONLY_FILTER_GROUP_ID, "On Sale"));
			}
			if (showMeOnlyItem.equals("organic")) {
				productFilters.add(new OrganicFilter("organic", SHOW_ME_ONLY_FILTER_GROUP_ID, "ORGANIC", "Organic, All Natural"));
			}
		}
		
		ProductFilterGroupImpl showMeOnlyFilterGroup = new ProductFilterGroupImpl();
		showMeOnlyFilterGroup.setProductFilters(productFilters);
		showMeOnlyFilterGroup.setId(SHOW_ME_ONLY_FILTER_GROUP_ID);
		showMeOnlyFilterGroup.setName("SHOW ME ONLY");
		showMeOnlyFilterGroup.setType("MULTI");
		showMeOnlyFilterGroup.setDisplayOnCategoryListingPage(false);
		
		results.add(showMeOnlyFilterGroup);		
		return results;
	}

	private static boolean isFilterActive(CmsFilteringNavigator navigator, ProductFilterGroupImpl productFilterGroup) {
		if (navigator.getRequestFilterParams().get(productFilterGroup.getId()) != null &&
				!navigator.getRequestFilterParams().get(productFilterGroup.getId()).contains("clearall")) {
			return true;
		}
		return false;

	}
	
	/**
	 * @param filterGroups
	 * @param navigator
	 * @return
	 * select active filters
	 */
	public static Set<ProductItemFilterI> selectActiveFilters(List<ProductFilterGroupI> filterGroups, CmsFilteringNavigator navigator){
		
		Set<ProductItemFilterI> activeFilters = new HashSet<ProductItemFilterI>();
		
		for (ProductFilterGroupI filterGroup : filterGroups){

			List<String> selectedFilterIds = navigator.getRequestFilterParams().get(filterGroup.getId());
			
			if (selectedFilterIds != null) {

				for (String selectedFilterId : selectedFilterIds) {
					
					for (ProductItemFilterI productFilter : filterGroup.getProductFilters()) {
						
						if (selectedFilterId.equals(productFilter.getId())) {
							activeFilters.add(productFilter);
						}
					}
				}
			}
		}
		
		return activeFilters;
	}


	public static boolean isUserContextEligibleForHideFiltering(FDUserI user) {
		boolean isOnWeb = EnumTransactionSource.WEBSITE.equals(user.getApplication());
		boolean isLeftNavRolledOut = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user);
		return  isLeftNavRolledOut && isOnWeb;
	}
	
	/** if true category does not show up in any navigation **/
	public static boolean isCategoryHiddenInContext(FDUserI user, CategoryModel cat) {
		return 	!cat.isShowSelf() || isCategoryForbiddenInContext(user, cat);
	}
	
	/** if true category page cannot be displayed **/
	public static boolean isCategoryForbiddenInContext(FDUserI user, CategoryModel cat) {
		return 	!ContentFactory.getInstance().getPreviewMode() && (
					cat.isHideIfFilteringIsSupported() && isUserContextEligibleForHideFiltering(user) || 
					cat.isHidden() || 
					!isCategoryDisplayableCached(user, cat)
				);
	}
	
	public static boolean isCategoryDisplayableCached(FDUserI user, CategoryModel cat){
		String contentName = cat.getContentName();
		String plantId = user.getUserContext().getFulfillmentContext().getPlantId();
		if (CmsManager.getInstance().isReadOnlyContent()){
            Boolean displayable = EhCacheUtilWrapper.getObjectFromCache(CmsCaches.BR_CATEGORY_SUB_TREE_HAS_PRODUCTS_CACHE.cacheName, contentName + "_" + plantId);
			
			if (displayable == null) {
				displayable = cat.isDisplayable(); //do the recursive check
                EhCacheUtilWrapper.putObjectToCache(CmsCaches.BR_CATEGORY_SUB_TREE_HAS_PRODUCTS_CACHE.cacheName, contentName + "_" + plantId, displayable);
			}
			
			return displayable;
		} else {
			//CMS DB mode
			return cat.isDisplayable();
		}
	}
	
    public static SuperDepartmentModel getSuperDepartment(FDUserI user, String departmentId) throws FDResourceException {
				
		GlobalNavigationModel globalNavigationModel = GlobalNavContextUtil.getGlobalNavigationModel(user);

		if(globalNavigationModel!=null){
			for (ContentNodeModel contentNode : globalNavigationModel.getItems()) {
				if (contentNode instanceof SuperDepartmentModel) {
					for (DepartmentModel dept : ((SuperDepartmentModel) contentNode).getDepartments()) {
						if (departmentId.equals(dept.getContentName())) {
							return (SuperDepartmentModel) contentNode;
						}
					}
				}
			}			
		}

		return null;
	}
	
	/**
	 * Checks category is forbidden or not show self. Also checks if the category is not show self and it's selected.
	 * @param user
	 * @param nodeId
	 * @param category
	 * @return true iff the category has to be hidden
	 */
	public static boolean isCategoryHiddenAndSelectedNotShowSelf(FDUserI user, String nodeId, CategoryModel category) {
		return isCategoryHiddenInContext(user, category) && !(!category.isShowSelf() && category.getContentName().equals(nodeId));
	}
}
