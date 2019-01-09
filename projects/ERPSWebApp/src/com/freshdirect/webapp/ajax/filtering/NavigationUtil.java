package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
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
import com.freshdirect.storeapi.content.ProductFilterGroup;
import com.freshdirect.storeapi.content.ProductFilterGroupModel;
import com.freshdirect.storeapi.content.ProductFilterGroupType;
import com.freshdirect.storeapi.content.ProductFilterModel;
import com.freshdirect.storeapi.content.ProductFilterMultiGroupModel;
import com.freshdirect.storeapi.content.ProductItemFilterI;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.RecipeSearchPage;
import com.freshdirect.storeapi.content.SuperDepartmentModel;
import com.freshdirect.storeapi.content.TagModel;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.cache.EhCacheUtilWrapper;
import com.freshdirect.webapp.globalnav.GlobalNavContextUtil;
import com.google.common.collect.ComparisonChain;

public class NavigationUtil {

	public static final String RECIPE_CATEGORY_FILTER_GROUP = "recipeCategoryFilterGroup";
	public static final String SHOW_ME_ONLY_FILTER_GROUP_ID = "showMeOnlyFilterGroup";
	public static final String BRAND_FILTER_GROUP_ID = "brandFilterGroup";
	public static final String DEPARTMENT_FILTER_GROUP_ID = "departmentFilterGroup";
	public static final String CATEGORY_FILTER_GROUP_ID = "categoryFilterGroup";
	public static final String SUBCATEGORY_FILTER_GROUP_ID = "subCategoryFilterGroup";
	private static final String FILTER_GROUP_ID = "FilterGroup";
	public static final List<String> NO_EXCLUDE_FILTER_GROUP_NAMES = Arrays.asList();
	public static final List<String> EXCLUDE_FILTER_GROUP_NAMES = Arrays.asList("Brand");

    private static final Comparator<ProductFilterGroup> SEARCH_PRODUCT_FILTER_GROUP_ORDER_BY_NAME = new Comparator<ProductFilterGroup>() {

        private PriorityComparator<String> searchFilterMenuPrefixComparator = new PriorityComparator<String>("department", "category", "subcategory", "brand", "popular", "show me only", "nutrition");

        @Override
        public int compare(ProductFilterGroup o1, ProductFilterGroup o2) {
            String left = o1.getName().toLowerCase();
            String right = o2.getName().toLowerCase();
            return ComparisonChain.start().compare(left, right, searchFilterMenuPrefixComparator).compare(left, right).result();
        }

        class PriorityComparator<T> implements Comparator<T> {

            private final List<T> values;

            public PriorityComparator(T... values) {
                this.values = Arrays.asList(values);
            }

            @Override
            public int compare(T o1, T o2) {
                int idx1 = values.indexOf(o1);
                int idx2 = values.indexOf(o2);
                if (idx1 > -1) {
                    return idx2 > -1 ? idx1 - idx2 : -1;
                }
                return idx2 > -1 ? 1 : 0;
            }
        }
    };

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

		// [APPDEV-7601] detect if request came via mobile API (FK web or mobile client)
		if (user.getApplication() != null && user.getApplication().isMobileBound()) {
		    model.setMobileNavigation(true);
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
		}

		// set brand filter location
		if(node instanceof CategoryModel){
			model.setBrandFilterLocation(((CategoryModel)node).getBrandFilterLocation(EnumBrandFilterLocation.BELOW_LOWEST_LEVEL_CATEGROY.toString()));
		}

		// are we showing products?
		model.setProductListing( !model.isSuperDepartment() && model.getNavDepth()!=NavDepth.DEPARTMENT && (
				model.getNavDepth()!=NavDepth.CATEGORY ||
				((CategoryModel)model.getNavigationHierarchy().get(NavDepth.CATEGORY)).getSubcategories().size()==0 ||
				navigator.isAll()));

		return model;
	}

    private static Map<String, List<TagModel>> collectMultigroupTags(ProductContainer productContainer, CmsFilteringNavigator navigator) {
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
        return selectionMap;
    }

    public static void setupAllAndActiveFiltersForBrowse(CmsFilteringNavigator nav, NavigationModel navigationModel) {
        navigationModel.setAllFilters(createBrowseFilterGroups(navigationModel, nav));
        navigationModel.setActiveFilters(selectActiveFilters(navigationModel.getAllFilters(), nav));
    }

    public static void setupAllAndActiveFiltersForSearch(CmsFilteringNavigator nav, NavigationModel navigationModel) {
        navigationModel.setAllFilters(createSearchFilterGroups(navigationModel, nav));
        navigationModel.setActiveFilters(selectActiveFilters(navigationModel.getAllFilters(), nav));
    }

    public static List<ProductFilterGroup> createBrowseFilterGroups(NavigationModel navigationModel, CmsFilteringNavigator navigator) {
        List<ProductFilterGroup> productFilterGroups = new ArrayList<ProductFilterGroup>();

        List<String> excludeFilterGroupNames = NO_EXCLUDE_FILTER_GROUP_NAMES;
        if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.aggregatedfilterimprovement2018, navigationModel.getUser())) {
            excludeFilterGroupNames = EXCLUDE_FILTER_GROUP_NAMES;
            ProductFilterGroup brandFilter = createBrandFilter(navigationModel.getBrandsOfSearchResults());
            brandFilter.setDisplayOnCategoryListingPage(true);
            productFilterGroups.add(brandFilter);
        }

        ProductContainer node = (ProductContainer) navigationModel.getSelectedContentNodeModel();
        productFilterGroups.addAll(createFilterGroups(node, navigator, navigationModel.getUser(), excludeFilterGroupNames));
        return productFilterGroups;
    }

	public static List<ProductFilterGroup> createSearchFilterGroups(NavigationModel navigationModel, CmsFilteringNavigator navigator){
		List<ProductFilterGroup> productFilterGroups = new ArrayList<ProductFilterGroup>();
		if (navigationModel.isProductListing()) {
		    productFilterGroups.addAll(createSearchFilterGroupsForProduct(navigationModel, navigator));
		} else if (navigationModel.isRecipeListing()) {
		    productFilterGroups.add(createSearchFilterGroupsForRecipes(navigationModel, navigator));
		}
		return productFilterGroups;
	}

    public static ProductFilterGroup createBrandFilter(Collection<BrandModel> brandModels) {
        List<ProductItemFilterI> brandFilters = ProductItemFilterFactory.getInstance().getBrandFilters(brandModels, BRAND_FILTER_GROUP_ID);
        return ProductItemFilterFactory.getInstance().createProductFilterGroup(BRAND_FILTER_GROUP_ID, "Brand", ProductFilterGroupType.POPUP.name(), "All Brands", brandFilters, false, false);
    }

	private static ProductFilterGroup createSearchFilterGroupsForRecipes(NavigationModel navigationModel, CmsFilteringNavigator navigator) {
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
		ProductFilterGroup recipeCategoryFilterGroup = new ProductFilterGroup();
		recipeCategoryFilterGroup.setProductFilters(productFilters);
		recipeCategoryFilterGroup.setId(RECIPE_CATEGORY_FILTER_GROUP);
		recipeCategoryFilterGroup.setName("Recipes");
		recipeCategoryFilterGroup.setType("SINGLE");
		recipeCategoryFilterGroup.setAllSelectedLabel("ALL RECIPES");
		recipeCategoryFilterGroup.setDisplayOnCategoryListingPage(false);
		return recipeCategoryFilterGroup;
	}

	private static List<ProductFilterGroup> createSearchFilterGroupsForProduct(NavigationModel navigationModel, CmsFilteringNavigator navigator) {
		List<ProductFilterGroup> results = new ArrayList<ProductFilterGroup>();
		List<ProductItemFilterI> productFilters = new ArrayList<ProductItemFilterI>();

		for (DepartmentModel departmentModel : navigationModel.getDepartmentsOfSearchResults()) {

			productFilters.add(new ContentNodeFilter(departmentModel, DEPARTMENT_FILTER_GROUP_ID));
		}

		ProductFilterGroup departmentFilterGroup = new ProductFilterGroup();
		departmentFilterGroup.setProductFilters(productFilters);
		departmentFilterGroup.setId(DEPARTMENT_FILTER_GROUP_ID);
		departmentFilterGroup.setName("DEPARTMENT");
		departmentFilterGroup.setType("SINGLE");
		departmentFilterGroup.setAllSelectedLabel("ALL DEPARTMENTS");
		departmentFilterGroup.setDisplayOnCategoryListingPage(false);

		results.add(departmentFilterGroup);

		if (isFilterActive(navigator, departmentFilterGroup)) { //show only if department filter is selected

			productFilters = new ArrayList<ProductItemFilterI>();

			for (CategoryModel categoryModel : navigationModel.getCategoriesOfSearchResults()) {
				if (navigator.getRequestFilterParams().get(departmentFilterGroup.getId()).contains(categoryModel.getDepartment().getContentName())) {
					productFilters.add(new ContentNodeFilter(categoryModel, CATEGORY_FILTER_GROUP_ID));
				}
			}

			ProductFilterGroup categoryFilterGroup = new ProductFilterGroup();
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

				for (CategoryModel subCategoryModel : navigationModel.getSubCategoriesOfSearchResults()) {
					if (navigator.getRequestFilterParams().get(categoryFilterGroup.getId()).contains(subCategoryModel.getParentNode().getContentName()) && navigator.getRequestFilterParams().get(departmentFilterGroup.getId()).contains(subCategoryModel.getDepartment().getContentName())) {
						productFilters.add(new ContentNodeFilter(subCategoryModel, SUBCATEGORY_FILTER_GROUP_ID));
					}
				}
				ProductFilterGroup subCategoryFilterGroup = new ProductFilterGroup();
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
            results.add(createBrandFilter(navigationModel.getBrandsOfSearchResults()));

        if (FilteringFlowType.SEARCH == navigator.getPageType() && FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.aggregatedfilterimprovement2018, navigationModel.getUser())) {
            FDUserI user = navigationModel.getUser();
            Collection<CategoryModel> categories = navigationModel.getCategoriesOfSearchResults();
            results.addAll(createAggregatedFilterGroups(categories, user));
            results.addAll(createAggregatedFilterMultiGroups(categories, navigator, user));
            Collections.sort(results, SEARCH_PRODUCT_FILTER_GROUP_ORDER_BY_NAME);
        } else {
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

            ProductFilterGroup showMeOnlyFilterGroup = new ProductFilterGroup();
            showMeOnlyFilterGroup.setProductFilters(productFilters);
            showMeOnlyFilterGroup.setId(SHOW_ME_ONLY_FILTER_GROUP_ID);
            showMeOnlyFilterGroup.setName("SHOW ME ONLY");
            showMeOnlyFilterGroup.setType("MULTI");
            showMeOnlyFilterGroup.setDisplayOnCategoryListingPage(false);

            results.add(showMeOnlyFilterGroup);
        }
        return results;
    }

    private static List<ProductFilterGroup> createFilterGroups(ProductContainer productContainer, CmsFilteringNavigator navigator, FDUserI user, List<String> excludeFilterGroupNames) {
        List<ProductFilterGroup> filterGroups = new ArrayList<ProductFilterGroup>();
        for (ContentNodeModel item : productContainer.getProductFilterGroups()) {
            if (item instanceof ProductFilterGroupModel) {
                ProductFilterGroupModel model = (ProductFilterGroupModel) item;
                if (!excludeProductFilterGroup(model, excludeFilterGroupNames)) {
                    filterGroups.add(ProductItemFilterFactory.getInstance().getProductFilterGroup(model, user));
                }
            } else if (item instanceof ProductFilterMultiGroupModel) {
                ProductFilterMultiGroupModel groupFilter = (ProductFilterMultiGroupModel) item;
                // create a flat group hierarchy, create simple groups from the multigroups and create the x level for the selected multigroup filters (selectionMap)
                Map<String, List<TagModel>> selection = collectMultigroupTags(productContainer, navigator);
                filterGroups.addAll(ProductItemFilterFactory.getInstance().getProductFilterGroups(groupFilter, selection.get(item.getContentName())));
            }
        }
        return filterGroups;
    }

    private static List<ProductFilterGroup> createAggregatedFilterGroups(Collection<? extends ProductContainer> productContainers, FDUserI user) {
        List<ProductFilterGroup> filterGroups = new ArrayList<ProductFilterGroup>();

        Map<String, List<ProductFilterGroupModel>> productFilterGroupByName = collectProductFilterGroupsByName(productContainers, user);
        Set<ProductFilterModel> productFilterModels = collectProductFilterModels(productContainers, user);

        for (Map.Entry<String, List<ProductFilterGroupModel>> entry : productFilterGroupByName.entrySet()) {
            String filterGroupName = entry.getKey();
            List<ProductFilterGroupModel> filterGroupModels = entry.getValue();

            List<ProductItemFilterI> aggregatedItemFilters = new ArrayList<ProductItemFilterI>();
            for (ProductFilterGroupModel filterGroupModel : filterGroupModels) {
                for (ProductFilterModel filter : filterGroupModel.getProductFilterModels()) {
                    if (productFilterModels.contains(filter)) {
                        aggregatedItemFilters.add(ProductItemFilterFactory.getInstance().getProductFilter(filter, filterGroupName + FILTER_GROUP_ID, user));
                        productFilterModels.remove(filter);
                    }
                }
            }

            if (!filterGroupModels.isEmpty() && !aggregatedItemFilters.isEmpty()) {
                ProductFilterGroupModel firstFilterGroupModel = filterGroupModels.get(0);

                boolean isSameType = true;
                String type = firstFilterGroupModel.getType();
                for (ProductFilterGroupModel filterGroupModel : filterGroupModels) {
                    isSameType = filterGroupModel.getType().equals(type) && isSameType;
                }

                filterGroups.add(ProductItemFilterFactory.getInstance().createProductFilterGroup(filterGroupName + FILTER_GROUP_ID, firstFilterGroupModel.getName(),
                        (isSameType) ? firstFilterGroupModel.getType() : ProductFilterGroupType.MULTI.name(), firstFilterGroupModel.getAllSelectedLabel(), aggregatedItemFilters,
                        firstFilterGroupModel.isDisplayOnCategoryListingPage(), false));
            }
        }
        return filterGroups;
    }

    private static Set<ProductFilterGroup> createAggregatedFilterMultiGroups(Collection<? extends ProductContainer> productContainers, CmsFilteringNavigator navigator, FDUserI user) {
        Set<ProductFilterGroup> filtersGroups = new HashSet<ProductFilterGroup>();
        EnumServiceType serviceType = user.getSelectedServiceType();
        EnumEStoreId eStoreId = user.getUserContext().getStoreContext().getEStoreId();
        for (ProductContainer productContainer : productContainers) {
            Map<String, List<TagModel>> selection = collectMultigroupTags(productContainer, navigator);
            for (ContentNodeModel item : productContainer.getProductFilterGroups()) {
                if (item instanceof ProductFilterMultiGroupModel) {
                    ProductFilterMultiGroupModel groupFilter = (ProductFilterMultiGroupModel) item;
                    if (!excludeProductFilterGroup(groupFilter, serviceType, eStoreId)) {
                        filtersGroups.addAll(ProductItemFilterFactory.getInstance().getProductFilterGroups(groupFilter, selection.get(item.getContentName())));
                    }
                }
            }
        }
        return filtersGroups;
    }

    private static Set<ProductFilterModel> collectProductFilterModels(Collection<? extends ProductContainer> productContainers, FDUserI user) {
        Set<ProductFilterModel> productFilterModels = new HashSet<ProductFilterModel>();
        EnumServiceType serviceType = user.getSelectedServiceType();
        EnumEStoreId eStoreId = user.getUserContext().getStoreContext().getEStoreId();
        for (ProductContainer container : productContainers) {
            for (ContentNodeModel item : container.getProductFilterGroups()) {
                if (item instanceof ProductFilterGroupModel) {
                    ProductFilterGroupModel model = (ProductFilterGroupModel) item;
                    if (!excludeProductFilterGroup(model, serviceType, eStoreId)) {
                        productFilterModels.addAll(model.getProductFilterModels());
                    }
                }
            }
        }
        return productFilterModels;
    }

    private static Map<String, List<ProductFilterGroupModel>> collectProductFilterGroupsByName(Collection<? extends ProductContainer> productContainers, FDUserI user) {
        Map<String, List<ProductFilterGroupModel>> productFilterGroupByName = new HashMap<String, List<ProductFilterGroupModel>>();
        EnumServiceType serviceType = user.getSelectedServiceType();
        EnumEStoreId eStoreId = user.getUserContext().getStoreContext().getEStoreId();
        for (ProductContainer container : productContainers) {
            for (ContentNodeModel item : container.getProductFilterGroups()) {
                if (item instanceof ProductFilterGroupModel) {
                    ProductFilterGroupModel productFilterGroup = (ProductFilterGroupModel) item;
                    if (!productFilterGroupByName.containsKey(productFilterGroup.getName())) {
                        productFilterGroupByName.put(productFilterGroup.getName(), new ArrayList<ProductFilterGroupModel>());
                    }
                    List<ProductFilterGroupModel> productFilterGroups = productFilterGroupByName.get(productFilterGroup.getName());
                    if (!excludeProductFilterGroup(productFilterGroup, serviceType, eStoreId)) {
                        productFilterGroups.add(productFilterGroup);
                    }
                }
            }
        }
        return productFilterGroupByName;
    }

    private static boolean excludeProductFilterGroup(ProductFilterGroupModel productFilterGroup, EnumServiceType serviceType, EnumEStoreId eStoreId) {
        return excludeSearchProductFilterGroup(productFilterGroup.isExcludeResidentalSearch(), productFilterGroup.isExcludeCorporateSearch(),
                productFilterGroup.isExcludeFoodKickSearch(), serviceType, eStoreId) || excludeProductFilterGroup(productFilterGroup, EXCLUDE_FILTER_GROUP_NAMES);
    }

    private static boolean excludeProductFilterGroup(ProductFilterMultiGroupModel productFilterGroup, EnumServiceType serviceType, EnumEStoreId eStoreId) {
        return excludeSearchProductFilterGroup(productFilterGroup.isExcludeResidentalSearch(), productFilterGroup.isExcludeCorporateSearch(),
                productFilterGroup.isExcludeFoodKickSearch(), serviceType, eStoreId);
    }

    public static boolean excludeProductFilterGroup(ProductFilterGroupModel productFilterGroup, List<String> excludeFilterGroupNames) {
        return excludeFilterGroupNames != null && excludeFilterGroupNames.contains(productFilterGroup.getName());
    }

    private static boolean excludeSearchProductFilterGroup(boolean isExcludeResidentalSearch, boolean isExcludeCorporateSearch, boolean isExcludeFoodKickSearch,
            EnumServiceType serviceType, EnumEStoreId eStoreId) {
        boolean exclude = false;
        switch (eStoreId) {
            case FD:
                exclude = (EnumServiceType.CORPORATE == serviceType) ? isExcludeCorporateSearch : isExcludeResidentalSearch;
                break;

            case FDX:
                exclude = isExcludeFoodKickSearch;
                break;
        }
        return exclude;
    }

	private static boolean isFilterActive(CmsFilteringNavigator navigator, ProductFilterGroup productFilterGroup) {
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
	public static Set<ProductItemFilterI> selectActiveFilters(List<ProductFilterGroup> filterGroups, CmsFilteringNavigator navigator){

		Set<ProductItemFilterI> activeFilters = new HashSet<ProductItemFilterI>();

		for (ProductFilterGroup filterGroup : filterGroups){

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
		if(null !=user && null !=user.getUserContext() && null !=user.getUserContext().getFulfillmentContext()){
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
		return cat.isDisplayable();
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
