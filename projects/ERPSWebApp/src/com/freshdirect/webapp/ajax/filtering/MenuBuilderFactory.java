package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategorySectionModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.EnumBrandFilterLocation;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.PopulatorUtil;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductFilterGroupI;
import com.freshdirect.fdstore.content.ProductFilterGroupType;
import com.freshdirect.fdstore.content.ProductItemFilterI;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.content.browse.filter.BrandFilter;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
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
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class MenuBuilderFactory {
	private static final Logger LOGGER = LoggerFactory.getInstance( MenuBuilderFactory.class );

	public static final MenuItemData MARKER;
	public static final MenuItemData ALL_PRODUCTS_ITEM;
	public static final MenuItemData ALL_DEPARTMENTS_ITEM;
	public static final MenuItemData ALL_CATEGORIES_ITEM;
	public static final MenuItemData ALL_SUBCATEGORIES_ITEM;
	static{
		
		MARKER = new MenuItemData();
		MARKER.setName("marker");
		
		ALL_PRODUCTS_ITEM = new MenuItemData();
		ALL_PRODUCTS_ITEM.setActive(true);
		ALL_PRODUCTS_ITEM.setName("ALL PRODUCTS");
		ALL_PRODUCTS_ITEM.setSelected(false);
		ALL_PRODUCTS_ITEM.setUrlParameter("all");

		ALL_DEPARTMENTS_ITEM = new MenuItemData();
		ALL_DEPARTMENTS_ITEM.setActive(true);
		ALL_DEPARTMENTS_ITEM.setName("ALL DEPARTMENTS");
		ALL_DEPARTMENTS_ITEM.setSelected(false);
		ALL_DEPARTMENTS_ITEM.setUrlParameter("allDepartments");
		
		ALL_CATEGORIES_ITEM = new MenuItemData();
		ALL_CATEGORIES_ITEM.setActive(true);
		ALL_CATEGORIES_ITEM.setName("ALL CATEGORIES");
		ALL_CATEGORIES_ITEM.setSelected(false);
		ALL_CATEGORIES_ITEM.setUrlParameter("allCategories");

		ALL_SUBCATEGORIES_ITEM = new MenuItemData();
		ALL_SUBCATEGORIES_ITEM.setActive(true);
		ALL_SUBCATEGORIES_ITEM.setName("ALL SUBCATEGORIES");
		ALL_SUBCATEGORIES_ITEM.setSelected(false);
		ALL_SUBCATEGORIES_ITEM.setUrlParameter("allSubCategories");

	}
	
	private static MenuBuilderFactory factory;
	
	public static MenuBuilderFactory getInstance(){
		if(factory == null){
			factory = new MenuBuilderFactory();
		}
		return factory;
	}
	
	public static MenuBuilderI createBuilderByPageType(NavDepth depth, boolean hasSuperDepartment, boolean isSearchPage){
		
		if (isSearchPage) {
			return getInstance().new SearchPageMenuBuilder();
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
	
	public class SearchPageMenuBuilder implements MenuBuilderI{
		
		public List<MenuBoxData> buildMenu(List<ProductFilterGroupI> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav){
			
			List<MenuBoxData> menu = new ArrayList<MenuBoxData>();
			
			if (nav.isSearchRequest()) {
				if (!navModel.getDepartmentsOfSearchResults().isEmpty()) {
					MenuBoxData domain = new MenuBoxData();
					domain.setName("DEPARTMENT");
					domain.setId("search_departments");
					domain.setBoxType(MenuBoxType.DEPARTMENT);
					domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
					domain.setSelectionType(MenuBoxSelectionType.SINGLE);
					
					List<MenuItemData> items = new ArrayList<MenuItemData>();
					
					List<DepartmentModel> departments = new ArrayList<DepartmentModel>();
					departments.addAll(navModel.getDepartmentsOfSearchResults().values());
					MenuItemData allDepartmentsItem = ALL_DEPARTMENTS_ITEM.copy();
					if(nav.isAll()){
						allDepartmentsItem.setSelected(true);
					}
					items.add(allDepartmentsItem);					

					domain.setItems(createDeptMenuItems(departments, items, navModel.getUser()));
	
					menu.add(domain);
				}
				
				if (!navModel.getCategoriesOfSearchResults().isEmpty()) {
					MenuBoxData domain = new MenuBoxData();
					domain.setName("CATEGORY");
					domain.setId("search_categories");
					domain.setBoxType(MenuBoxType.CATEGORY);
					domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
					domain.setSelectionType(MenuBoxSelectionType.SINGLE);
					
					List<MenuItemData> items = new ArrayList<MenuItemData>();
					
					List<CategoryModel> categories = new ArrayList<CategoryModel>();
					categories.addAll(navModel.getCategoriesOfSearchResults().values());
					MenuItemData allCategoriesItem = ALL_CATEGORIES_ITEM.copy();
					if(nav.isAll()){
						allCategoriesItem.setSelected(true);
					}
					items.add(allCategoriesItem);					
					
					domain.setItems(createCatMenuItems(categories, items, navModel.getUser()));
	
					menu.add(domain);
				}
				
				if (!navModel.getSubCategoriesOfSearchResults().isEmpty()) {
					MenuBoxData domain = new MenuBoxData();
					domain.setName("SUBCATEGORY");
					domain.setId("search_subcategories");
					domain.setBoxType(MenuBoxType.SUB_CATEGORY);
					domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
					domain.setSelectionType(MenuBoxSelectionType.SINGLE);
					
					List<MenuItemData> items = new ArrayList<MenuItemData>();
					
					List<CategoryModel> subCategories = new ArrayList<CategoryModel>();
					subCategories.addAll(navModel.getSubCategoriesOfSearchResults().values());
					MenuItemData allSubCategoriesItem = ALL_SUBCATEGORIES_ITEM.copy();
					if(nav.isAll()){
						allSubCategoriesItem.setSelected(true);
					}
					items.add(allSubCategoriesItem);					
					
					domain.setItems(createCatMenuItems(subCategories, items, navModel.getUser()));
	
					menu.add(domain);
				}
				
				createFilterMenuDomains(filterGroups, navModel.getActiveFilters(), menu, navModel.isProductListing());				
			}

			return menu;
		}
		
	}
	
	public class SuperDeptPageMenuBuilder implements MenuBuilderI{
		
		public List<MenuBoxData> buildMenu(List<ProductFilterGroupI> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav){
			
			List<MenuBoxData> menu = new ArrayList<MenuBoxData>();
			
			// create superdepartment box

			if (navModel.isSuperDepartment()) {
				menu.add(createSuperDepartmentMenuBox((SuperDepartmentModel)navModel.getSelectedContentNodeModel(), navModel, true));
			}

			return menu;
		}
		
	}

	public class DeptPageMenuBuilder implements MenuBuilderI{
		
		public List<MenuBoxData> buildMenu(List<ProductFilterGroupI> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav){
			
			List<MenuBoxData> menu = new ArrayList<MenuBoxData>();
			
			createTopLevelBoxes(menu, navModel);
		
			return menu;
		}
		
	}
	
	public class CatPageMenuBuilder implements MenuBuilderI{
		
		public List<MenuBoxData> buildMenu(List<ProductFilterGroupI> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav){
			
			List<MenuBoxData> menu = new ArrayList<MenuBoxData>();
			
			final Map<NavDepth, ContentNodeModel> thePath = navModel.getNavigationHierarchy();

			DepartmentModel dept = (DepartmentModel) thePath.get(NavDepth.DEPARTMENT); 
			String deptId = dept.getContentName();
			
			boolean expandSecondLowestNavBox = ((CategoryModel)navModel.getSelectedContentNodeModel()).isExpandSecondLowestNavigationBox();
			
			// determine the box dipslay and selection type (lowest level of navigation should always remain expanded [APPDEV-3814])
			if(((CategoryModel)thePath.get(NavDepth.CATEGORY)).getSubcategories()==null || 
					   ((CategoryModel)thePath.get(NavDepth.CATEGORY)).getSubcategories().size()==0){
				
				createTopLevelBoxes(menu, navModel);
				
				for(MenuBoxData domain : menu){
					checkSelected(domain, thePath.get(NavDepth.CATEGORY).getContentName());				
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
							createCatMenuItems(selectedCategories, menuItems, navModel.getUser());
						}
						
					}
					
					if(menuItems.isEmpty() && !navModel.getRegularCategories().isEmpty()){
						
						//Shop By Type header
						menuItems.add(new MenuItemData(dept.getRegularCategoriesNavHeader()));
						
						//regular categories
						createCatMenuItems(navModel.getRegularCategories(), menuItems, navModel.getUser());
					}
					
					if(!navModel.getPreferenceCategories().isEmpty()){
						
						//Shop By Preference header
						menuItems.add(new MenuItemData(dept.getPreferenceCategoriesNavHeader()));
						
						//preference categories				
						createCatMenuItems(navModel.getPreferenceCategories(), menuItems, navModel.getUser());
					}
					
					domain.setItems(menuItems);
					insertMarkersForSpecialBox(domain, dept.getMaxItemsPerColumn());
					checkSelected(domain, thePath.get(NavDepth.CATEGORY).getContentName());
					
					menu.add(domain);
				}
			}
			
			
			// sub categories box on category and sub category page
			if(((CategoryModel)thePath.get(NavDepth.CATEGORY)).getSubcategories()!=null && 
			   ((CategoryModel)thePath.get(NavDepth.CATEGORY)).getSubcategories().size()>0){
				
				CategoryModel cat = (CategoryModel)thePath.get(NavDepth.CATEGORY);
				
				if (!NavigationUtil.isCategoryHiddenInContext(navModel.getUser(), (CategoryModel)cat)) {
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
								checkSelected(box, thePath.get(NavDepth.CATEGORY).getContentName());				
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
					
					domain.setItems(createCatMenuItems(((CategoryModel)thePath.get(NavDepth.CATEGORY)).getSubcategories(), items, navModel.getUser()));
					
					if(thePath.get(NavDepth.SUB_CATEGORY)!=null){
						checkSelected(domain, thePath.get(NavDepth.SUB_CATEGORY).getContentName());
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
				
				if (!NavigationUtil.isCategoryHiddenInContext(navModel.getUser(), (CategoryModel)subCat)) {
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
					
					domain.setItems(createCatMenuItems(subCat.getSubcategories(), items, navModel.getUser()));
					
					CategoryModel subSubCat = (CategoryModel) thePath.get(NavDepth.SUB_SUB_CATEGORY);
					
					if(domain.getItems()!=null && domain.getItems().size()>0){
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
	
	public void setSecondLowestLevelBoxType(List<MenuBoxData> menu, CategoryModel cat){
		
		boolean expand = cat.isExpandSecondLowestNavigationBox();
		
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

				List<MenuItemData> items = new ArrayList<MenuItemData>();
				items.add(new MenuItemData(categorySection.getHeadline()));
				domain.addItems(createCatMenuItems(categorySection.getSelectedCategories(), items, navModel.getUser()));

			}

			menu.add(domain);

		} else if (!navModel.getRegularCategories().isEmpty()) {
			
			// regular categories

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

			List<MenuItemData> items = new ArrayList<MenuItemData>();
			items.add(new MenuItemData(dept.getRegularCategoriesNavHeader()));

			domain.setItems(createCatMenuItems(navModel.getRegularCategories(), items, navModel.getUser()));

			menu.add(domain);
		}

		if (!navModel.getPreferenceCategories().isEmpty()) {

			// preference categories

			MenuBoxData domain = new MenuBoxData();
			domain.setName(dept.getPreferenceCategoriesLeftNavBoxHeader());
			domain.setId(deptId + "_preference");
			domain.setBoxType(MenuBoxType.CATEGORY);
			domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
			domain.setSelectionType(MenuBoxSelectionType.SINGLE);

			List<MenuItemData> items = new ArrayList<MenuItemData>();
			items.add(new MenuItemData(dept.getPreferenceCategoriesNavHeader()));

			domain.setItems(createCatMenuItems(navModel.getPreferenceCategories(), items, navModel.getUser()));

			menu.add(domain);
		}
	}
	
	private MenuBoxData createHeaderOnlyBox(CategoryModel node, MenuBoxType type){
		
		MenuBoxData domain = new MenuBoxData();
		domain.setName(node.getFullName());
		domain.setId(node.getContentName());
		domain.setBoxType(type);
		domain.setDisplayType(MenuBoxDisplayType.SIMPLE);
		domain.setSelectionType(MenuBoxSelectionType.SINGLE);
		
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

	/**
	 * Check selected menu item and set explicit label name (instead of the name of particular menu item)
	 * See {@link #checkSelected(MenuBoxData, String)
	 * 
	 * @param box menu box
	 * @param nodeId
	 * @param selectedLabelName
	 * @return
	 */
	private boolean checkSelectedEx(MenuBoxData box, String nodeId, String selectedLabelName){
		
		for(MenuItemData menuItem : box.getItems()){
			if(nodeId!=null && nodeId.equals(menuItem.getId())){
				menuItem.setSelected(true);
				box.setSelectedLabel(selectedLabelName);
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
	
	private void createFilterMenuDomains(List<ProductFilterGroupI> filterGroups, Set<ProductItemFilterI> activeFilters, List<MenuBoxData> menu, boolean productListingPage){
		
		
		
		if (filterGroups != null){
			for (ProductFilterGroupI filterGroup : filterGroups){
				
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
						item.setSelected(false);
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
	
	private boolean isBrandFilterBox(ProductFilterGroupI filterGroup){
		
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
			menuItem.setName(department.getFullName());
			menuItem.setSelected(false);
			menuItem.setUrlParameter(department.getContentName());
			newMenuItems.add(menuItem);
		}
		
		Collections.sort(newMenuItems, DataUtil.NAME_COMPARATOR);
		menuItems.addAll(newMenuItems);
		
		return menuItems;
	}
	
	private List<MenuItemData> createCatMenuItems(List<CategoryModel> categories, List<MenuItemData> menuItems, FDUserI user){
		
		if(menuItems==null){
			menuItems = new ArrayList<MenuItemData>();			
		}
		
		
		List<MenuItemData> newMenuItems = new ArrayList<MenuItemData>();	
		
		for(CategoryModel category : categories){
			if (NavigationUtil.isCategoryHiddenInContext(user, category)) {
				continue;
			}
			
			MenuItemData menuItem = new MenuItemData();
			menuItem.setActive(true);
			menuItem.setId(category.getContentName());
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
	public void checkMenuStatus(BrowseDataContext browseData, NavigationModel navModel, FDSessionUser user){

		List<MenuBoxData> menu = navModel.getLeftNav();
		
		Map<String, ProductItemFilterI> allFilters = ProductItemFilterUtil.prepareFilters(navModel.getAllFilters());
		Map<String, ProductItemFilterI> activeFilters = ProductItemFilterUtil.prepareFilters(navModel.getActiveFilters());
		List<FilteringProductItem> items = new ArrayList<FilteringProductItem>();
		ProductItemFilterUtil.collectAllItems(browseData.getSectionContexts(), items);
		
		boolean clp = false;
		if(isFilterPresentOnPage(menu) && navModel.getNavDepth()==NavDepth.CATEGORY && items.size()==0){
			// if we are on a category page and there are no product items then it should be a CLP. Collect all items from all categories
			
			CategoryModel cat = (CategoryModel) navModel.getNavigationHierarchy().get(NavDepth.CATEGORY);
			
			if(cat!=null){
				
				clp=true;
				
				List<SectionContext> sections = new ArrayList<SectionContext>();
				sections.add(BrowseDataBuilderFactory.getInstance().createProductSection(cat, user, navModel));
				ProductItemFilterUtil.collectAllItems(sections, items);							
			}
		}
		
		int itemCount = 0;
		
		Iterator<MenuBoxData> boxIterator = menu.iterator();
		while(boxIterator.hasNext()){
			
			MenuBoxData box = boxIterator.next();
			boolean emptyBox = true;
			// check if filtering has effect on this box
			if(box.getBoxType().isHasFilterEffect()){

				if (box.getBoxType() == MenuBoxType.FILTER && box.getSelectionType() == MenuBoxSelectionType.MULTI) { // MULTI SELECT FILTER GROUP
					
					boolean popupType = box.getDisplayType() == MenuBoxDisplayType.CENTER_POPUP || box.getDisplayType() == MenuBoxDisplayType.POPUP;
					
					// walk through on menu items in the box
					Iterator<MenuItemData> it = box.getItems().iterator();
					while (it.hasNext()) {
						
						MenuItemData item = it.next();
						
						if(item.getId()==null || "all".equals(item.getId())){ // marker items
							continue;
						}

						itemCount = 0;

						// apply filter if menu item is a filter and the box type is multi select
						itemCount = ProductItemFilterUtil.countItemsForFilter(items, allFilters.get(ProductItemFilterUtil.createCompositeId(box.getId(), item.getId())));

						if (itemCount == 0 && !item.isSelected()) {
								it.remove();
						}else{
							emptyBox=false;
						}
					}

				} else if (box.getBoxType() == MenuBoxType.FILTER) { // SINGLE SELECT FILTER GROUP

					// extract current filter base from active filters
					Set<ProductItemFilterI> currentFiltersBase = ProductItemFilterUtil.removeFiltersByParentId(box.getId(), activeFilters);

					// create the pre filtered item list (filters belongs to this filtergroup will be removed)
					List<FilteringProductItem> preFilteredItems = ProductItemFilterUtil.getFilteredProducts(!clp ? browseData.getUnfilteredItems() : items, currentFiltersBase, false);
					

					final long t0 = System.currentTimeMillis();
					
					// walk through on menu items in the box
					Iterator<MenuItemData> it = box.getItems().iterator();
					while (it.hasNext()) {

						MenuItemData item = it.next();

						if (item.getId() == null || "all".equals(item.getId())) { // marker items
							continue;
						}

						itemCount = 0;

						String filterCompositeId = ProductItemFilterUtil.createCompositeId(box.getId(), item.getId());
						// add filters one by one ...
						final ProductItemFilterI filter = allFilters.get(filterCompositeId);
						if (filter != null) {
							Set<ProductItemFilterI> currentFilters = new HashSet<ProductItemFilterI>();
							currentFilters.add(filter);
							itemCount = ProductItemFilterUtil.getFilteredProducts(preFilteredItems, currentFilters, true).size();
						} else {
							itemCount = preFilteredItems.size();
						}

						// and check how many products passes the current status
						if (itemCount == 0 && !item.isSelected()) {
							it.remove();
						} else {
							emptyBox = false;
						}
					}

					final long t1 = System.currentTimeMillis();
					LOGGER.debug("Filtered " + preFilteredItems.size() + " products for each " + box.getItems().size() + " menu itmes in " + ((t1-t0)/1000) + " secs");

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
							SectionContext section = BrowseDataBuilderFactory.getInstance().createSectionTree(category, navDepth.getLevel(), user);

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
								ProductItemFilterUtil.collectAllItems(section.getSectionContexts(), products);
							}

							products = ProductItemFilterUtil.getFilteredProducts(products, navModel.getActiveFilters(), false);

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
				
				if (!beforeFilterBoxFound) {
					box.setPosition(position++);
				}
				
				if (box.getBoxType() == brandFilterPosition) {
					beforeFilterBoxFound = true;
				}
				
				if (box.isBrandFilter()) {
					
					box.setPosition(position++);
				}
			}
		}
		
		
		Collections.sort(menu, DataUtil.MENUBOX_POSITION_COMPARATOR);
	}
	
	/**
	 * @param products
	 * TODO verify if this is needed in production
	 */
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