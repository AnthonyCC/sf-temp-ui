package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductFilterGroupI;
import com.freshdirect.fdstore.content.ProductFilterMultiGroupModel;
import com.freshdirect.fdstore.content.ProductItemFilterI;
import com.freshdirect.fdstore.content.TagModel;
import com.freshdirect.fdstore.content.browse.filter.ProductItemFilterFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;

public class NavigationUtil {
	
	public static NavigationModel createNavigationModel(ProductContainer node, CmsFilteringNavigator navigator, FDUserI user) throws InvalidFilteringArgumentException{
		
		NavigationModel model = new NavigationModel();
		
		model.setUser(user);

		LinkedList<ProductContainer> productContainerPath = new LinkedList<ProductContainer>();
		
		model.setSelectedProductContainer(node);
		model.setProductContainerPath(productContainerPath);
		
		// get parents and breadcrumb
		ProductContainer tempNode = node;
		while (true){
			productContainerPath.push((ProductContainer)tempNode);
			if (tempNode instanceof DepartmentModel){
				break;
			}
			tempNode=(ProductContainer)tempNode.getParentNode();
			if (tempNode==null){
				break;
			}
		}
		
		String id = navigator.getId();
		
		// validation for orphan categories
		if(!(productContainerPath.get(0) instanceof DepartmentModel)){
			throw new InvalidFilteringArgumentException("Orphan category: "+id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, id);
		}
		
		int productContainerPathSize = productContainerPath.size();
		if(!navigator.isPdp()){
			if (productContainerPathSize > NavDepth.getMaxLevel()+1){ //validate if level not too deep
				throw new InvalidFilteringArgumentException("Category is too deep in hierarchy: "+ id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, id);
			}			
		}
		
		// determine the depth of the navigation
		Map<NavDepth, ProductContainer> navigationHierarchy = new HashMap<NavDepth, ProductContainer>();
		for(NavDepth navDepth : NavDepth.values()){
			if (productContainerPathSize > navDepth.getLevel()){
				model.setNavDepth(navDepth);
				navigationHierarchy.put(navDepth, productContainerPath.get(navDepth.getLevel()));
			}
		}		
		model.setNavigationHierarchy(navigationHierarchy);

		// search for categories and preference categories - prepare for menu building
		List<CategoryModel> regularCategories = new ArrayList<CategoryModel>();
		List<CategoryModel> prefCategories = new ArrayList<CategoryModel>();
		DepartmentModel department = (DepartmentModel) model.getNavigationHierarchy().get(NavDepth.DEPARTMENT);

		for (CategoryModel cat : department.getCategories()){
			if (cat instanceof CategoryModel) {
				if (NavigationUtil.isCategoryHiddenInContext(user, cat)) {
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
		
		// filters not needed on product page
		if(!navigator.isPdp()){
			
			//-- CREATE FILTERS --
			
			// create actual filters
			model.setAllFilters(createFilterGroups(node, navigator, user));
			
			// select active filters
			model.setActivelFilters(selectActiveFilters(model.getAllFilters(), navigator));
		}
		
		// -- CREATE MENU --
		
		// create menuBuilder based on page type
		MenuBuilderI menuBuilder = MenuBuilderFactory.createBuilderByPageType(model.getNavDepth());

		// are we showing products?
		model.setProductListing( model.getNavDepth()!=NavDepth.DEPARTMENT && (
				model.getNavDepth()!=NavDepth.CATEGORY || 
				model.getNavigationHierarchy().get(NavDepth.CATEGORY).getSubcategories().size()==0 || 
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
	private static List<ProductFilterGroupI> createFilterGroups(ProductContainer productContainer, CmsFilteringNavigator navigator, FDUserI user){
		
		// construct selectionMap from filter parameters
		// check what filters are selected from a multigroup (multigroups can contain tagmodels only)
		Map<String,List<TagModel>> selectionMap = new HashMap<String, List<TagModel>>(); // TODO do we need list of tagmodels here? do we want to support more than two level multi group structure

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
							selection.add((TagModel) ContentFactory.getInstance().getContentNode(paramValue));							
						}
					}
				}
			}
		}
		
		// create a flat group hierarchy, create simple groups from the multigroups and create the x level for the selected multigroup filters (selectionMap)
		return ProductItemFilterFactory.getInstance().getDefaultProductFilterGroups(productContainer, selectionMap, user);
		
	}
	
	/**
	 * @param filterGroups
	 * @param navigator
	 * @return
	 * select active filters
	 */
	private static Set<ProductItemFilterI> selectActiveFilters(List<ProductFilterGroupI> filterGroups, CmsFilteringNavigator navigator){
		
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
	
	public static boolean isCategoryHiddenInContext(FDUserI user, CategoryModel cat) {
		return 	!cat.isShowSelf() ||
				!ContentFactory.getInstance().getPreviewMode() && (
					cat.isHideIfFilteringIsSupported() && isUserContextEligibleForHideFiltering(user) || 
					cat.isHidden() || 
					!isCategoryDisplayableCached(cat)
				);
	}
	
	public static boolean isCategoryDisplayableCached(CategoryModel cat){
		String key = cat.getContentName();
		if (CmsManager.getInstance().isReadOnlyContent()){
			Boolean displayable = EhCacheUtil.getObjectFromCache(EhCacheUtil.BR_CATEGORY_SUB_TREE_HAS_PRODUCTS_CACHE_NAME, key);
			
			if (displayable == null) {
				displayable = cat.isDisplayable(); //do the recursive check
				EhCacheUtil.putObjectToCache(EhCacheUtil.BR_CATEGORY_SUB_TREE_HAS_PRODUCTS_CACHE_NAME, key, displayable);
			}
			
			return displayable;
		} else {
			//CMS DB mode
			return cat.isDisplayable();
		}
	}
}
