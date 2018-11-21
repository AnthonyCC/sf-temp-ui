package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.browse.filter.BrandFilter;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.CategorySectionModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.EnumBrandFilterLocation;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductContainer;
import com.freshdirect.storeapi.content.ProductFilterGroup;
import com.freshdirect.storeapi.content.ProductFilterGroupType;
import com.freshdirect.storeapi.content.ProductItemFilterI;
import com.freshdirect.storeapi.content.SuperDepartmentModel;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.SearchPageType;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContextService;
import com.freshdirect.webapp.ajax.browse.data.CategoryData;
import com.freshdirect.webapp.ajax.browse.data.DataUtil;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData.MenuBoxDisplayType;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData.MenuBoxSelectionType;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData.MenuBoxType;
import com.freshdirect.webapp.ajax.browse.data.MenuItemData;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.browse.data.SectionContext;

public class MenuBuilderFactory {

	private static final MenuItemData MARKER;
	private static final MenuItemData ALL_PRODUCTS_ITEM;

	static{
		MARKER = new MenuItemData();
		MARKER.setName("marker");

		ALL_PRODUCTS_ITEM = new MenuItemData();
		ALL_PRODUCTS_ITEM.setActive(true);
		ALL_PRODUCTS_ITEM.setName("ALL PRODUCTS");
		ALL_PRODUCTS_ITEM.setSelected(false);
		ALL_PRODUCTS_ITEM.setUrlParameter("all");
	}

    private static final MenuBuilderFactory INSTANCE = new MenuBuilderFactory();

    public static MenuBuilderFactory getInstance() {
        return INSTANCE;
    }

    private MenuBuilderFactory() {
    }

	public static MenuBuilderI createBuilderByPageType(NavDepth depth, boolean hasSuperDepartment, SearchPageType searchPageType){

		if (searchPageType != null) {
			if (SearchPageType.PRODUCT == searchPageType) {
				return getInstance().new SearchLikePageMenuBuilder();
			} else if (SearchPageType.RECIPE == searchPageType) {
				return getInstance().new RecipePageMenuBuilder();
			}
		}

		if (hasSuperDepartment) {
			return getInstance().new SuperDeptPageMenuBuilder();
		}

		switch (depth) {

		case DEPARTMENT:{
			return getInstance().new DeptPageMenuBuilder();
		}

		case CATEGORY:{
			return getInstance().new CatPageMenuBuilder();
		}

		case SUB_CATEGORY:{
			return getInstance().new CatPageMenuBuilder();
		}

		case SUB_SUB_CATEGORY:{
			return getInstance().new CatPageMenuBuilder();
		}

		default:{
			return null;
		}
		}
	}

	public class SearchLikePageMenuBuilder implements MenuBuilderI{

		@Override
        public List<MenuBoxData> buildMenu(List<ProductFilterGroup> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav){

			List<MenuBoxData> menu = new ArrayList<MenuBoxData>();
			if (
				nav.getPageType() == FilteringFlowType.PRES_PICKS
				|| nav.getPageType() == FilteringFlowType.STAFF_PICKS //not sure what this workaround does...
			) { //workaround in favor of frontend........
				MenuBoxData domain = new MenuBoxData();
				if (nav.getPageType() == FilteringFlowType.PRES_PICKS) {
					domain.setName("pres_picks_id");
					domain.setId("pres_picks_id");
				} else if (nav.getPageType() == FilteringFlowType.STAFF_PICKS) {
					domain.setName("prod_assort_id"); //by workaround, it means, a ref css can style
					domain.setId("prod_assort_id"); //the ui leftnav gets this as a data-id

				}
				domain.setBoxType(MenuBoxType.CATEGORY);
				domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
				domain.setSelectionType(MenuBoxSelectionType.SINGLE);

				MenuItemData menuItem = new MenuItemData();
				menuItem.setActive(true);
				menuItem.setId(nav.getId());
				menuItem.setFilterId(nav.getId());
				menuItem.setName(nav.getId());
				menuItem.setSelected(true);
				menuItem.setUrlParameter(nav.getId());

				List<MenuItemData> menuItems = new ArrayList<MenuItemData>();
				menuItems.add(menuItem);
				domain.addItems(menuItems);
				menu.add(domain);

			}

			createFilterMenuDomains(filterGroups, navModel.getActiveFilters(), menu, navModel.isProductListing());

			return menu;
		}

	}

	public class RecipePageMenuBuilder implements MenuBuilderI {

		@Override
		public List<MenuBoxData> buildMenu(List<ProductFilterGroup> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav) {
			List<MenuBoxData> menu = new ArrayList<MenuBoxData>();
			createRecipeFilterMenuDomains(filterGroups, navModel.getActiveFilters(), menu);
			return menu;
		}

	}

	public class SuperDeptPageMenuBuilder implements MenuBuilderI{

		@Override
        public List<MenuBoxData> buildMenu(List<ProductFilterGroup> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav){

			List<MenuBoxData> menu = new ArrayList<MenuBoxData>();

			// create superdepartment box

			if (navModel.isSuperDepartment()) {
				menu.add(createSuperDepartmentMenuBox((SuperDepartmentModel)navModel.getSelectedContentNodeModel(), navModel, true));
			}

			return menu;
		}

	}

	public class DeptPageMenuBuilder implements MenuBuilderI{

		@Override
        public List<MenuBoxData> buildMenu(List<ProductFilterGroup> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav){

			List<MenuBoxData> menu = new ArrayList<MenuBoxData>();

			createTopLevelBoxes(menu, navModel);

			return menu;
		}

	}

	public class CatPageMenuBuilder implements MenuBuilderI{
		/**
		 * Preview mode should handle extraordinary categories
		 *
		 * @ticket APPDEV-4383
		 * @param cat
		 * @return
		 */
		private boolean requiresExtraMenuItem(NavigationModel navModel, CategoryModel cat) {
			if (cat == null || navModel == null)
				return false;

			// preview mode required
			if (!FDStoreProperties.getPreviewMode())
				return false;

			// only top categories are accepted
			if (cat.getParentNode() instanceof CategoryModel)
				return false;

			if (cat.isPreferenceCategory())
				return false;

			// final boolean hasSubcats = cat.getSubcategories() != null && !cat.getSubcategories().isEmpty();

			DepartmentModel dept = cat.getDepartment();

			final boolean hasSectionLayout = !dept.getCategorySections().isEmpty();

			if (!hasSectionLayout)
				return false;

			boolean selCatInSection = false;
			for (CategorySectionModel categorySection : navModel.getCategorySections()) {
				selCatInSection = categorySection.getSelectedCategories().contains(cat);
				if (selCatInSection)
					break;
			}

			return !selCatInSection;
		}

		/**
		 * Create menu entry for hidden categories
		 *
		 * @ticket APPDEV-4383
		 * @param cat
		 * @return
		 */
		private MenuItemData createItemForHiddenCategory(CategoryModel cat) {
			MenuItemData menuItem = new MenuItemData();

			menuItem.setActive(true);
			menuItem.setId(cat.getContentName());
			menuItem.setFilterId(cat.getContentName());
			menuItem.setName(cat.getFullName() + " (hidden)");
			menuItem.setUrlParameter(cat.getContentName());

			menuItem.setSelected(true); // <-- select menu item automatically !!

			return menuItem;
		}



		@Override
        public List<MenuBoxData> buildMenu(List<ProductFilterGroup> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav){

			List<MenuBoxData> menu = new ArrayList<MenuBoxData>();

			final Map<NavDepth, ContentNodeModel> thePath = navModel.getNavigationHierarchy();

			DepartmentModel dept = (DepartmentModel) thePath.get(NavDepth.DEPARTMENT);
			String deptId = dept.getContentName();

			boolean expandSecondLowestNavBox = ((CategoryModel)navModel.getSelectedContentNodeModel()).isExpandSecondLowestNavigationBox();

			// determine the box dipslay and selection type (lowest level of navigation should always remain expanded [APPDEV-3814])
			CategoryModel cat = (CategoryModel) thePath.get( NavDepth.CATEGORY );
			List<CategoryModel> subCategories = cat.getSubcategories();
			String categoryId = cat.getContentName();
			if(!nav.isPdp() && (subCategories==null || subCategories.size()==0)){

				createTopLevelBoxes(menu, navModel);

				for(MenuBoxData domain : menu){
					checkSelected(domain, categoryId);
				}

				if (requiresExtraMenuItem(navModel, cat)) {
					// not member? -> create a new menu item for it
					// and mark with hidden tag
					MenuItemData menuItem = createItemForHiddenCategory(cat);

					for (MenuBoxData domain : menu) {
						if (MenuBoxType.CATEGORY == domain.getBoxType()) {
							domain.getItems().add(0, menuItem);
							break;
						}
					}

				}

			} else {

				// create superdepartment box
				final SuperDepartmentModel superDept = navModel.getSuperDepartmentModel();
				if (superDept != null) {
					MenuBoxData domain = createSuperDepartmentMenuBox(superDept, navModel, false);
					// check which department is selected
					checkSelected(domain, thePath.get(NavDepth.DEPARTMENT).getContentName());
					menu.add(domain);
				}

				// top popup navigation box
				if(!navModel.getRegularCategories().isEmpty() || !navModel.getPreferenceCategories().isEmpty()){

					MenuBoxData domain = new MenuBoxData();
					domain.setName(dept.getRegularCategoriesLeftNavBoxHeader());
					domain.setId(deptId);
					domain.setBoxType(MenuBoxType.CATEGORY);
					domain.setDisplayType(MenuBoxDisplayType.POPUP);
					domain.setSelectionType(MenuBoxSelectionType.LINK);

					List<MenuItemData> menuItems = new ArrayList<MenuItemData>();

					//create categories and preference categories box
					if (!navModel.getCategorySections().isEmpty()) {

						if (requiresExtraMenuItem(navModel, cat)) {
							// not member? -> create a new menu item for it
							// and mark with hidden tag
							MenuItemData menuItem = createItemForHiddenCategory(cat);

							menuItems.add(menuItem);
						}



						CATEGORY_SECTION_LOOP: for (CategorySectionModel categorySection : navModel.getCategorySections()) {

							List<CategoryModel> selectedCategories = categorySection.getSelectedCategories();
							for (CategoryModel selectedCategory : selectedCategories){
								if (determineCategoryLevel(selectedCategory)!=NavDepth.CATEGORY){ //fallback to regular categories box if non-top-level category included in categorySection
									menuItems = new ArrayList<MenuItemData>();
									break CATEGORY_SECTION_LOOP;
								}
							}

							MenuItemData sectionTitle = new MenuItemData();
							sectionTitle.setName(categorySection.getHeadline());
							menuItems.add(sectionTitle);
							createCatMenuItems(selectedCategories, menuItems, navModel.getUser(), categoryId);
						}
					}

					if(menuItems.isEmpty() && !navModel.getRegularCategories().isEmpty()){

						//Shop By Type header
						menuItems.add(new MenuItemData(dept.getRegularCategoriesNavHeader()));

						//regular categories
						createCatMenuItems(navModel.getRegularCategories(), menuItems, navModel.getUser(), categoryId);
					}

					if(!navModel.getPreferenceCategories().isEmpty()){

						//Shop By Preference header
						menuItems.add(new MenuItemData(dept.getPreferenceCategoriesNavHeader()));

						//preference categories
						createCatMenuItems(navModel.getPreferenceCategories(), menuItems, navModel.getUser(), categoryId);
					}

					domain.setItems(menuItems);
					insertMarkersForSpecialBox(domain, dept.getMaxItemsPerColumn());
					checkSelected(domain, categoryId);

					menu.add(domain);
				}
			}


			String subCategoryId = getNodeContentNameByNavDepth(thePath, NavDepth.SUB_CATEGORY);
			// sub categories box on category and sub category page
			if(((CategoryModel)thePath.get(NavDepth.CATEGORY)).getSubcategories()!=null &&
			   ((CategoryModel)thePath.get(NavDepth.CATEGORY)).getSubcategories().size()>0){



				if (!NavigationUtil.isCategoryHiddenAndSelectedNotShowSelf(navModel.getUser(), categoryId, cat)) {
					MenuBoxData domain = new MenuBoxData();
					domain.setName("");//leave empty
					domain.setId(cat.getContentName());
					domain.setBoxType(MenuBoxType.SUB_CATEGORY);
					if(nav.isPdp() || (thePath.get(NavDepth.SUB_CATEGORY)!=null &&
							   ((CategoryModel)thePath.get(NavDepth.SUB_CATEGORY)).getSubcategories()!=null &&
							   ((CategoryModel)thePath.get(NavDepth.SUB_CATEGORY)).getSubcategories().size()>0)){

						domain.setDisplayType(MenuBoxDisplayType.POPUP);
						domain.setSelectionType(MenuBoxSelectionType.LINK);
					}else{
						domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
						domain.setSelectionType(MenuBoxSelectionType.SINGLE);

						if(expandSecondLowestNavBox){ // create expanded top level boxes
							menu.clear();
							createTopLevelBoxes(menu, navModel);

							for(MenuBoxData box : menu){
								checkSelected(box, categoryId);
							}
						}
					}

					List<MenuItemData> items = new ArrayList<MenuItemData>();

					if(!checkSpecial(cat)){
						// add ALL PRODUCTS item in case of no special subcategory
						MenuItemData allProductsItem = ALL_PRODUCTS_ITEM.copy();
						if(nav.isAll()){
							allProductsItem.setSelected(true);
						}
						items.add(allProductsItem);
					}

					domain.setItems(createCatMenuItems(((CategoryModel)thePath.get(NavDepth.CATEGORY)).getSubcategories(), items, navModel.getUser(), subCategoryId));

					if(thePath.get(NavDepth.SUB_CATEGORY)!=null){
						checkSelected(domain, subCategoryId);
					}

					if (items.size() > 1) {
						menu.add(domain);
					}
				} else if(thePath.get(NavDepth.SUB_CATEGORY)==null){
					// in case of hidden category create a header text only box ...
					menu.add(createHeaderOnlyBox(cat, MenuBoxType.CATEGORY));
				}
			}

			// sub sub categories box on sub and sub sub categories page (not on special layout)
			if(thePath.get(NavDepth.SUB_CATEGORY)!=null &&
			   ((CategoryModel)thePath.get(NavDepth.SUB_CATEGORY)).getSubcategories()!=null &&
			   ((CategoryModel)thePath.get(NavDepth.SUB_CATEGORY)).getSubcategories().size()>0 &&
			   !nav.isSpecialPage()){

				CategoryModel subCat = (CategoryModel)thePath.get(NavDepth.SUB_CATEGORY);

				if (!NavigationUtil.isCategoryHiddenAndSelectedNotShowSelf(navModel.getUser(), subCategoryId, subCat)) {
					MenuBoxData domain = new MenuBoxData();
					domain.setName("");//leave empty
					domain.setId(subCat.getContentName());
					domain.setBoxType(MenuBoxType.SUB_SUB_CATEGORY);
					if(nav.isPdp()){
						domain.setDisplayType(MenuBoxDisplayType.POPUP);
						domain.setSelectionType(MenuBoxSelectionType.LINK);
					}else{
						domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
						domain.setSelectionType(MenuBoxSelectionType.SINGLE);

						if(expandSecondLowestNavBox){
							MenuBoxData secondLowestBox = null;
 							if(menu.get(menu.size()-1)!=null){
								secondLowestBox=menu.get(menu.size()-1);
								secondLowestBox.setDisplayType(MenuBoxDisplayType.SIMPLE);
								secondLowestBox.setSelectionType(MenuBoxSelectionType.SINGLE);
							}
						}
					}

					List<MenuItemData> items = new ArrayList<MenuItemData>();
					// handle ALL PRODUCTS item
					MenuItemData allProductsItem = ALL_PRODUCTS_ITEM.copy();
					allProductsItem.setUrlParameter(subCat.getContentName());
					items.add(allProductsItem);

					domain.setItems(createCatMenuItems(subCat.getSubcategories(), items, navModel.getUser(), getNodeContentNameByNavDepth(thePath, NavDepth.SUB_SUB_CATEGORY)));


					if(domain.getItems()!=null && domain.getItems().size()>0){
						CategoryModel subSubCat = (CategoryModel) thePath.get(NavDepth.SUB_SUB_CATEGORY);
						String subCatId = subSubCat == null ? null : subSubCat.getContentName();
						if(!checkSelected(domain, subCatId)){
							allProductsItem.setSelected(true); // select ALL PRODUCTS option if no menu item were selected
						}
					}

					if (items.size() > 1) {
						menu.add(domain);
					}
				} else if(thePath.get(NavDepth.SUB_CATEGORY)!=null) {
					// in case of hidden category create a header text only box ...
					menu.add(createHeaderOnlyBox(subCat, MenuBoxType.SUB_CATEGORY));
				}
			}

			if(!nav.isSpecialPage()){
				// create filter boxes
				createFilterMenuDomains(filterGroups, navModel.getActiveFilters(), menu, navModel.isProductListing());
			}

			return menu;

		}
	}

	private String getNodeContentNameByNavDepth(final Map<NavDepth, ContentNodeModel> thePath, NavDepth navDepth) {
		String result = null;
		if (thePath != null && navDepth != null) {
			ContentNodeModel contentNodeModel = thePath.get(navDepth);
			if (contentNodeModel != null) {
				result = contentNodeModel.getContentName();
			}
		}
		return result;
	}

	public void setSecondLowestLevelBoxType(List<MenuBoxData> menu, CategoryModel cat){

		int catCounter=0;
		for(MenuBoxData box : menu){
			if(box.getBoxType()!=MenuBoxType.FILTER){
				catCounter++;
			}else if(catCounter > 1){

			}
		}
	}

	private void createTopLevelBoxes(List<MenuBoxData> menu, NavigationModel navModel){
		final Map<NavDepth, ContentNodeModel> thePath = navModel.getNavigationHierarchy();

		DepartmentModel dept = (DepartmentModel) thePath.get(NavDepth.DEPARTMENT);
		String deptId = dept.getContentName();

		// create superdepartment box
		final SuperDepartmentModel superDept = navModel.getSuperDepartmentModel();
		if (superDept != null) {
			MenuBoxData domain = createSuperDepartmentMenuBox(superDept, navModel, false);
			// check which department is selected
			checkSelected(domain, thePath.get(NavDepth.DEPARTMENT).getContentName());
			menu.add(domain);
		}

		// create categories and preference categories box
		if (!navModel.getCategorySections().isEmpty()) {
			MenuBoxData domain = new MenuBoxData();
			domain.setName(dept.getRegularCategoriesLeftNavBoxHeader());
			domain.setId(deptId + "_regular");
			domain.setBoxType(MenuBoxType.CATEGORY);
			domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
			domain.setSelectionType(MenuBoxSelectionType.SINGLE);

			for (CategorySectionModel categorySection : navModel.getCategorySections()) {
				List<MenuItemData> items = createCatMenuItems(categorySection.getSelectedCategories(), null, navModel.getUser(), getNodeContentNameByNavDepth(thePath, NavDepth.CATEGORY));
				if (!items.isEmpty()) {
					items.add(0, new MenuItemData(categorySection.getHeadline()));
				}
				domain.addItems(items);
			}
			menu.add(domain);
		} else if (!navModel.getRegularCategories().isEmpty()) {
			// regular categories
			MenuBoxData domain = createRegularCategoryMenuBox(navModel, dept, deptId);
			List<MenuItemData> items = createCatMenuItems(navModel.getRegularCategories(), null, navModel.getUser(), getNodeContentNameByNavDepth(thePath, NavDepth.CATEGORY));
			if (!items.isEmpty()) {
				items.add(0, new MenuItemData(dept.getRegularCategoriesNavHeader()));
				domain.setItems(items);
				menu.add(domain);
			}
		}

		if (!navModel.getPreferenceCategories().isEmpty()) {
			// preference categories
			MenuBoxData domain = createPreferenceCategoryMenuBox(dept, deptId);
			List<MenuItemData> items = createCatMenuItems(navModel.getPreferenceCategories(), null, navModel.getUser(), getNodeContentNameByNavDepth(thePath, NavDepth.CATEGORY));
			if (!items.isEmpty()) {
				items.add(0, new MenuItemData(dept.getPreferenceCategoriesNavHeader()));
				domain.setItems(items);
				menu.add(domain);
			}
		}
	}

	private MenuBoxData createRegularCategoryMenuBox(NavigationModel navModel, DepartmentModel dept, String deptId) {
		MenuBoxData domain = new MenuBoxData();
		domain.setName(dept.getRegularCategoriesLeftNavBoxHeader());
		domain.setId(deptId + "_regular");
		if(!navModel.getPreferenceCategories().isEmpty()){
			// mimic the regular categories box type in case of preference categories based on requirement [APPDEV-3814]
			domain.setBoxType(MenuBoxType.DEPARTMENT);
		}else{
			domain.setBoxType(MenuBoxType.CATEGORY);
		}
		domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
		domain.setSelectionType(MenuBoxSelectionType.SINGLE);
		return domain;
	}

	private MenuBoxData createPreferenceCategoryMenuBox(DepartmentModel dept, String deptId) {
		MenuBoxData domain = new MenuBoxData();
		domain.setName(dept.getPreferenceCategoriesLeftNavBoxHeader());
		domain.setId(deptId + "_preference");
		domain.setBoxType(MenuBoxType.CATEGORY);
		domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
		domain.setSelectionType(MenuBoxSelectionType.SINGLE);
		return domain;
	}

	private MenuBoxData createHeaderOnlyBox(CategoryModel node, MenuBoxType type){

		MenuBoxData domain = new MenuBoxData();
		domain.setName(node.getFullName());
		domain.setId(node.getContentName());
		domain.setBoxType(type);
		domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
		domain.setSelectionType(MenuBoxSelectionType.SINGLE);
		domain.setItems(Collections.<MenuItemData>emptyList());

		return domain;
	}

	private void insertMarkersForSpecialBox(MenuBoxData box, int maxItemsPerColumn){

		List<MenuItemData> items = new ArrayList<MenuItemData>();

		int itemCounter = 0;
		for(MenuItemData menuItem : box.getItems()){

			++itemCounter;

			if(itemCounter>maxItemsPerColumn || (itemCounter==maxItemsPerColumn && menuItem.getId()==null)){
				itemCounter=0;
				items.add(MARKER);
			}
			items.add(menuItem);
		}

		box.setItems(items);
	}

	/**
	 * @param box
	 * @param nodeId
	 * check which node is selected in a box. return true if something is selected.
	 */
	private boolean checkSelected(MenuBoxData box, String nodeId){

		for(MenuItemData menuItem : box.getItems()){
			if(nodeId!=null && nodeId.equals(menuItem.getId())){
				menuItem.setSelected(true);
				box.setSelectedLabel(menuItem.getName());
				return true;
			}
		}

		return false;
	}

	private boolean checkSpecial(ProductContainer cat){

		for(CategoryModel subCat: cat.getSubcategories()){
			if(subCat.getSpecialLayout()!=null){
				return true;
			}
		}

		return false;
	}

	private void createFilterMenuDomains(List<ProductFilterGroup> filterGroups, Set<ProductItemFilterI> activeFilters, List<MenuBoxData> menu, boolean productListingPage){



		if (filterGroups != null){
			for (ProductFilterGroup filterGroup : filterGroups){

				if(filterGroup.isDisplayOnCategoryListingPage() || productListingPage){

					MenuBoxData domain = new MenuBoxData();
					domain.setId(filterGroup.getId());
					domain.setName(filterGroup.getName());
					domain.setBoxType(MenuBoxType.FILTER);
					domain.setMultiGroupBox(filterGroup.isMultiGroupModel());
					domain.setBrandFilter(isBrandFilterBox(filterGroup));
					if(!productListingPage){
						domain.setShouldSetAll(true);
					}

					if (ProductFilterGroupType.POPUP == filterGroup.getType()) {

						domain.setDisplayType(MenuBoxDisplayType.POPUP);
						domain.setSelectionType(MenuBoxSelectionType.SINGLE);

					} else if (ProductFilterGroupType.SINGLE == filterGroup.getType()) {

						domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
						domain.setSelectionType(MenuBoxSelectionType.SINGLE);

					} else if (ProductFilterGroupType.MULTI == filterGroup.getType()) {

						domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
						domain.setSelectionType(MenuBoxSelectionType.MULTI);
					}

					List<MenuItemData> items = null;

					if (filterGroup.getAllSelectedLabel() != null &&
						!"".equals(filterGroup.getAllSelectedLabel()) &&
						filterGroup.getProductFilters()!=null &&
						filterGroup.getProductFilters().size()>0) {

						items = new ArrayList<MenuItemData>();

						MenuItemData item = new MenuItemData();
						item.setActive(true);
						item.setId("all");
						item.setName(filterGroup.getAllSelectedLabel());
						if (productListingPage) {
							boolean containFilterForFilterGroup = false;
							for (ProductItemFilterI activeFilter : activeFilters) {
								if (filterGroup.getId().equals(activeFilter.getParentId())) {
									containFilterForFilterGroup = true;
									break;
								}
							}
							item.setSelected(!containFilterForFilterGroup);
						} else {
							item.setSelected(false);
						}
						item.setUrlParameter("clearall");

						items.add(item);
					}

					domain.setItems(createFilterMenuItems(domain, filterGroup.getProductFilters(), activeFilters, items));

					if(domain.getItems()!=null && domain.getItems().size()>0){
						menu.add(domain);
					}

				}
			}
		}
	}



	private void createRecipeFilterMenuDomains(List<ProductFilterGroup> filterGroups, Set<ProductItemFilterI> activeFilters, List<MenuBoxData> menu){
		if (filterGroups != null){
			for (ProductFilterGroup filterGroup : filterGroups){
				MenuBoxData domain = new MenuBoxData();
				domain.setId(filterGroup.getId());
				domain.setName(filterGroup.getName());
				domain.setBoxType(MenuBoxType.FILTER);
				domain.setMultiGroupBox(filterGroup.isMultiGroupModel());
				domain.setShouldSetAll(true);
				if (ProductFilterGroupType.POPUP == filterGroup.getType()) {
					domain.setDisplayType(MenuBoxDisplayType.POPUP);
					domain.setSelectionType(MenuBoxSelectionType.SINGLE);
				} else if (ProductFilterGroupType.SINGLE == filterGroup.getType()) {
					domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
					domain.setSelectionType(MenuBoxSelectionType.SINGLE);
				} else if (ProductFilterGroupType.MULTI == filterGroup.getType()) {
					domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
					domain.setSelectionType(MenuBoxSelectionType.MULTI);
				}
				List<MenuItemData> items = null;
				if (filterGroup.getAllSelectedLabel() != null &&
						!"".equals(filterGroup.getAllSelectedLabel()) &&
						filterGroup.getProductFilters()!=null &&
						filterGroup.getProductFilters().size()>0) {
					items = new ArrayList<MenuItemData>();
					MenuItemData item = new MenuItemData();
					item.setActive(true);
					item.setId("all");
					item.setName(filterGroup.getAllSelectedLabel());
					boolean containFilterForFilterGroup = false;
					for (ProductItemFilterI activeFilter : activeFilters) {
						if (filterGroup.getId().equals(activeFilter.getParentId())) {
							containFilterForFilterGroup = true;
							break;
						}
					}
					item.setSelected(!containFilterForFilterGroup);
					item.setUrlParameter("clearall");
					items.add(item);
				}
				domain.setItems(createFilterMenuItems(domain, filterGroup.getProductFilters(), activeFilters, items));
				if(domain.getItems()!=null && domain.getItems().size()>0){
					menu.add(domain);
				}
			}
		}
	}

	private boolean isBrandFilterBox(ProductFilterGroup filterGroup){

		if(filterGroup.getProductFilters()==null){
			return false;
		}

		for(ProductItemFilterI filter : filterGroup.getProductFilters()){
			if(!(filter instanceof BrandFilter)){
				return false;
			}
		}

		return true;
	}

	private List<MenuItemData> createFilterMenuItems(MenuBoxData box, List<ProductItemFilterI> filters, Set<ProductItemFilterI> activeFilters, List<MenuItemData> menuItems){

		if(menuItems==null){
			menuItems = new ArrayList<MenuItemData>();
		}

		for(ProductItemFilterI filter : filters){
			MenuItemData menuItem = new MenuItemData();
			menuItem.setActive(true);
			menuItem.setId(filter.getId());
			menuItem.setName(filter.getName());

			if(activeFilters.contains(filter)){
				menuItem.setSelected(true);
				if(box.getSelectionType()!=MenuBoxSelectionType.MULTI){
					box.setSelectedLabel(filter.getName());
				}
			} else {
				menuItem.setSelected(false);
			}

			menuItem.setUrlParameter(filter.getId());
			menuItems.add(menuItem);
		}

		return menuItems;
	}

	private MenuBoxData createSuperDepartmentMenuBox(SuperDepartmentModel model, NavigationModel navModel, boolean simpleBox){

		MenuBoxData domain = new MenuBoxData();
		domain.setName("");
		domain.setId(model.getContentName() + "_superdepartment");
		domain.setBoxType(MenuBoxType.SUPERDEPARTMENT);
		if(simpleBox){
			domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
			domain.setSelectionType(MenuBoxSelectionType.SINGLE);
		}else{
			domain.setDisplayType(MenuBoxDisplayType.POPUP);
			domain.setSelectionType(MenuBoxSelectionType.LINK);
		}

		List<MenuItemData> items = new ArrayList<MenuItemData>();

		domain.setItems(createDeptMenuItems(model.getDepartments(), items,
				navModel.getUser()));

		return domain;

	}

	private List<MenuItemData> createDeptMenuItems(List<DepartmentModel> departments, List<MenuItemData> menuItems, FDUserI user){

		if(menuItems==null){
			menuItems = new ArrayList<MenuItemData>();
		}


		List<MenuItemData> newMenuItems = new ArrayList<MenuItemData>();

		for(DepartmentModel department : departments){

			MenuItemData menuItem = new MenuItemData();
			menuItem.setActive(true);
			menuItem.setId(department.getContentName());
			menuItem.setFilterId(department.getContentName());
			menuItem.setName(department.getFullName());
			menuItem.setSelected(false);
			menuItem.setUrlParameter(department.getContentName());
			newMenuItems.add(menuItem);
		}

		Collections.sort(newMenuItems, DataUtil.NAME_COMPARATOR);
		menuItems.addAll(newMenuItems);

		return menuItems;
	}

	private List<MenuItemData> createCatMenuItems(List<CategoryModel> categories, List<MenuItemData> menuItems, FDUserI user, String nodeId){

		if(menuItems==null){
			menuItems = new ArrayList<MenuItemData>();
		}


		List<MenuItemData> newMenuItems = new ArrayList<MenuItemData>();

		for(CategoryModel category : categories){
			if (NavigationUtil.isCategoryHiddenAndSelectedNotShowSelf(user, nodeId, category)) {
				continue;
			}

			MenuItemData menuItem = new MenuItemData();
			menuItem.setActive(true);
			menuItem.setId(category.getContentName());
			menuItem.setFilterId(category.getContentName());
			menuItem.setName(category.getFullName());
			menuItem.setSelected(false);

			//TODO alias check
			menuItem.setUrlParameter(category.getContentName());

			newMenuItems.add(menuItem);
		}

		Collections.sort(newMenuItems, DataUtil.NAME_COMPARATOR);
		menuItems.addAll(newMenuItems);

		return menuItems;
	}

	/**
	 * @param filters
	 * @param menu
	 * @param browseData
	 * walk through on menu items and check which one is available
	 */
	public void checkMenuStatus(BrowseDataContext browseData, NavigationModel navModel, FDUserI user){
		checkMenuStatus(browseData, navModel, user, false);
	}
	/**
	 * @param filters
	 * @param menu
	 * @param browseData
	 * @param setHitCount when true, hitCount is added to the menu filter (SINGLE SELECT FILTER GROUP)
	 * walk through on menu items and check which one is available
	 */
	public void checkMenuStatus(BrowseDataContext browseData, NavigationModel navModel, FDUserI user, boolean setHitCount){

		List<MenuBoxData> menu = navModel.getLeftNav();

		Map<String, ProductItemFilterI> allFilters = ProductItemFilterUtil.prepareFilters(navModel.getAllFilters());
		Map<String, ProductItemFilterI> activeFilters = ProductItemFilterUtil.prepareFilters(navModel.getActiveFilters());
		List<FilteringProductItem> items = BrowseDataContextService.getDefaultBrowseDataContextService().collectAllItems(browseData);

		boolean clp = false;
		if(isFilterPresentOnPage(menu) && navModel.getNavDepth()==NavDepth.CATEGORY && items.size()==0){
			// if we are on a category page and there are no product items then it should be a CLP. Collect all items from all categories

			CategoryModel cat = (CategoryModel) navModel.getNavigationHierarchy().get(NavDepth.CATEGORY);

			if(cat!=null){

				clp=true;

				List<SectionContext> sections = new ArrayList<SectionContext>();
				sections.add(BrowseDataBuilderFactory.getInstance().createProductSection(cat, user, navModel));
				ProductItemFilterUtil.collectAllItems(sections, items, navModel);
			}
		}

		int itemCount = 0;

		Iterator<MenuBoxData> boxIterator = menu.iterator();
		while(boxIterator.hasNext()){
			MenuBoxData box = boxIterator.next();
			boolean emptyBox = true;
			// check if filtering has effect on this box
			if(box.getBoxType().isHasFilterEffect()){
				final String boxId = box.getId();
				if (box.getBoxType() == MenuBoxType.FILTER && box.getSelectionType() == MenuBoxSelectionType.MULTI) { // MULTI SELECT FILTER GROUP
					// walk through on menu items in the box
					Iterator<MenuItemData> it = box.getItems().iterator();
					while (it.hasNext()) {
						MenuItemData item = it.next();

						if(item.getId()==null || "all".equals(item.getId())){ // marker items
							continue;
						}

						itemCount = 0;

						// apply filter if menu item is a filter and the box type is multi select
						List<FilteringProductItem>  availableItems = new ArrayList<FilteringProductItem>(items);
						checkDefaultSkuAvailability(availableItems);
						itemCount = ProductItemFilterUtil.countItemsForFilter(availableItems, allFilters.get(ProductItemFilterUtil.createCompositeId(boxId, item.getId())));
						if (!item.isSelected() && (itemCount == 0 || itemCount == availableItems.size())) {
								it.remove();
						}else{
							emptyBox=false;
						}
					}

				} else if (box.getBoxType() == MenuBoxType.FILTER) { // SINGLE SELECT FILTER GROUP
					final boolean isProductListing = navModel.isProductListing();

					// extract current filter base from active filters
					Set<ProductItemFilterI> currentFiltersBase = ProductItemFilterUtil.removeFiltersByParentId(boxId, activeFilters);

					// create the pre filtered item list (filters belongs to this filtergroup will be removed)
					List<FilteringProductItem> preFilteredItems = ProductItemFilterUtil.getFilteredProducts(!clp ? browseData.getUnfilteredItems() : items, currentFiltersBase, !FilteringFlowType.BROWSE.equals(browseData.getPageType()), isProductListing);

					// walk through on menu items in the box
					Iterator<MenuItemData> it = box.getItems().iterator();
					while (it.hasNext()) {

						MenuItemData item = it.next();

						if(item.getId()==null || "all".equals(item.getId())){ // marker items
							continue;
						}
						boolean shouldRemoved = shouldSingleFilterMenuGroupBeRemoved(item, preFilteredItems, box, allFilters, isProductListing, setHitCount);

						if (!shouldRemoved) {
							emptyBox = false;
						} else {
							it.remove();
						}

					}

				} else { // NAVIGATION BOX (CATEGORIES)
					if(box.getItems()!=null) {
						// walk through on menu items in the box
						for (MenuItemData item : box.getItems()) {
							if (item.getId() == null || "all".equals(item.getId()) || item.isSpecial()) { // marker or special items
								continue;
							}

							itemCount = 0;
							// if the menu item is a category then check how
							// many products we have with the selected filters

							CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode(item.getId());
							NavDepth navDepth = determineCategoryLevel(category);
							// create a mock section contains all products we
							// need
							SectionContext section = BrowseDataBuilderFactory.getInstance().createSectionTree(category, navDepth.getLevel(), user,  navModel);

							if (section.isSpecial()) {
								continue;
							}

							List<FilteringProductItem> products = new ArrayList<FilteringProductItem>();
							if (section.getSectionContexts() == null || section.getSectionContexts().size() == 0) {
								// if the category doesn't have sub categories
								// ...
								products = ProductItemFilterUtil.createFilteringProductItems(category.getProducts());
							} else {
								// collect all products from section
								ProductItemFilterUtil.collectAllItems(section.getSectionContexts(), products, navModel);
							}

							products = ProductItemFilterUtil.getFilteredProducts(products, navModel.getActiveFilters(), false, navModel.isProductListing());

							checkDefaultSkuAvailability(products);

							itemCount = products.size();

							if (itemCount == 0 && !item.isSelected()) {
								item.setActive(false);
								setBrowseSectionAvailability(browseData.getSectionContexts(), item.getId());
							} else {
								emptyBox = false;
							}
						}
					}else{
						//special header only case
						emptyBox=false;
					}
				}

				if(emptyBox){
					boxIterator.remove();
				}
			}
		}
		if (!FilteringFlowType.BROWSE.equals(browseData.getPageType())) {
			MenuBoxData menuBoxData = MenuBoxDataService.getDefaultMenuBoxDataService().getMenuBoxById(NavigationUtil.SUBCATEGORY_FILTER_GROUP_ID, menu);
			if (menuBoxData != null) {
				if (menuBoxData.getItems().size() == 2) {
					int menuIndex = menu.indexOf(menuBoxData);
					menu.get(menuIndex - 1).setDisplayType(MenuBoxDisplayType.SIMPLE);
					menu.remove(menuBoxData);
				}
			} else {
				menuBoxData = MenuBoxDataService.getDefaultMenuBoxDataService().getMenuBoxById(NavigationUtil.CATEGORY_FILTER_GROUP_ID, menu);
				if (menuBoxData != null) {
					if (menuBoxData.getItems().size() == 2) {
						int menuIndex = menu.indexOf(menuBoxData);
						menu.get(menuIndex - 1).setDisplayType(MenuBoxDisplayType.SIMPLE);
						menu.remove(menuBoxData);
					}
				}
			}
		}
		if (FilteringFlowType.BROWSE.equals(browseData.getPageType())) {
			removeEmptyMenuBox(menu);
		}
	}

	private boolean shouldSingleFilterMenuGroupBeRemoved(MenuItemData item, List<FilteringProductItem> preFilteredItems,
			MenuBoxData box, Map<String, ProductItemFilterI> allFilters, boolean isProductListing,
			boolean setHitCount) {
		boolean shouldBeRemoved = false;
		String boxId = box.getId();
		int itemCount = 0;
		if (setHitCount) {
			int pfSize = preFilteredItems.size();
			String filterCompositeId = ProductItemFilterUtil.createCompositeId(boxId, item.getId());
			// add filters one by one ...
			final ProductItemFilterI filter = allFilters.get(filterCompositeId);
			if (filter != null) {
				Set<ProductItemFilterI> currentFilters = new HashSet<ProductItemFilterI>();
				currentFilters.add(filter);
				itemCount = ProductItemFilterUtil
						.getFilteredProducts(preFilteredItems, currentFilters, true, isProductListing).size();
				item.setHitCount(itemCount);
			} else {
				itemCount = pfSize;
			}

			if (!item.isSelected()
					&& (itemCount == 0 || (box.isBrandFilter() && !box.isMultiGroupBox() && itemCount == pfSize))) {
				shouldBeRemoved = true;
			}
		} else {

			if (!item.isSelected()) {

				String filterCompositeId = ProductItemFilterUtil.createCompositeId(boxId, item.getId());
				// add filters one by one ...
				final ProductItemFilterI filter = allFilters.get(filterCompositeId);
				if (filter != null) {
					Set<ProductItemFilterI> currentFilters = new HashSet<ProductItemFilterI>();
					currentFilters.add(filter);
					boolean hasFilteredProducts = ProductItemFilterUtil.hasFilteredProducts(preFilteredItems, filter,
							true, isProductListing);
					if (!hasFilteredProducts) {
						shouldBeRemoved = true;
					} else {
						if (box.isBrandFilter() && !box.isMultiGroupBox()) {
							itemCount = ProductItemFilterUtil
									.getFilteredProducts(preFilteredItems, currentFilters, true, isProductListing)
									.size();
							if (itemCount == preFilteredItems.size()) {
								shouldBeRemoved = true;
							}

						}
					}
				} else {
					if (box.isBrandFilter() && !box.isMultiGroupBox()) {
						shouldBeRemoved = true;
					}
				}

			}
		}
		return shouldBeRemoved;
	}
	private void removeEmptyMenuBox(List<MenuBoxData> menu) {
		Iterator<MenuBoxData> menuBoxIterator = menu.iterator();
		while (menuBoxIterator.hasNext()) {
			MenuBoxData menuBox = menuBoxIterator.next();
			List<MenuItemData> menuItems = menuBox.getItems();
			if (menuItems == null || menuItems.isEmpty()) {
				menuBoxIterator.remove();
			}
		}
	}

	private boolean isFilterPresentOnPage(List<MenuBoxData> menu){

		for(MenuBoxData box : menu){

			if (box.getBoxType() == MenuBoxType.FILTER){
				return true;
			}
		}

		return false;
	}

	public void checkNullSelection(List<MenuBoxData> menu){
		if (null == menu) {
			return;
		}
		Iterator<MenuBoxData> it = menu.iterator();

		while(it.hasNext()){

			MenuBoxData box = it.next();

			if(MenuBoxDisplayType.POPUP==box.getDisplayType() && box.getBoxType()!=MenuBoxType.FILTER && (box.getSelectedLabel()==null || "".equals(box.getSelectedLabel()))){
				it.remove();
			}
		}
	}

	public void relocateBrandFilter(List<MenuBoxData> menu, EnumBrandFilterLocation brandFilterLocation){

		Iterator<MenuBoxData> it = menu.iterator();

		boolean beforeFilterBoxFound = false;
		MenuBoxType brandFilterPosition = null;

		if(brandFilterLocation == EnumBrandFilterLocation.BELOW_DEPARTMENT){
			brandFilterPosition = MenuBoxType.CATEGORY;
		}else if(brandFilterLocation == EnumBrandFilterLocation.BELOW_LOWEST_LEVEL_CATEGROY){
			brandFilterPosition = MenuBoxType.FILTER;
		}

		if(brandFilterPosition==null){ // remove brand filter
			while (it.hasNext()) {

				MenuBoxData box = it.next();

				if(box.isBrandFilter()){
					it.remove();
				}
			}

		}else{

			int position = 0;
			while (it.hasNext()) {

				MenuBoxData box = it.next();

				if (box.getBoxType() == brandFilterPosition) {
				    beforeFilterBoxFound = true;
				}

				if (!beforeFilterBoxFound) {
					box.setPosition(position++);
				}

				if (box.isBrandFilter()) {

					box.setPosition(position++);
				}
			}
		}


		Collections.sort(menu, DataUtil.MENUBOX_POSITION_COMPARATOR);
	}

	private void checkDefaultSkuAvailability(List<FilteringProductItem> products){
		Iterator<FilteringProductItem> it = products.iterator();

		while(it.hasNext()){
			FilteringProductItem item = it.next();
			if(PopulatorUtil.getDefSku(item.getProductModel())==null){
				it.remove();
			}
		}
	}

	private void setBrowseSectionAvailability(List<SectionContext> sections, String catId){

		for(SectionContext section : sections){

			if(section.getCategories()!=null){
				for(CategoryData category : section.getCategories()){
					if(category.getId().equals(catId)){
						category.setAvailable(false);
					}
				}
			}

			if(section.getSectionContexts()!=null){
				setBrowseSectionAvailability(section.getSectionContexts(), catId);
			}
		}
	}

	public NavDepth determineCategoryLevel(ProductContainer node){

		int level = 0;
		while (true){
			if (node instanceof DepartmentModel){
				break;
			}
			node=(ProductContainer)node.getParentNode();
			if (node==null){
				break;
			}
			level++;
		}

		return NavDepth.getNavDepthByLevel(level);
	}

}