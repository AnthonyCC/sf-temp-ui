package com.freshdirect.webapp.ajax.browse.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.CategorySectionModel;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.EnumBrandFilterLocation;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.ProductFilterGroup;
import com.freshdirect.storeapi.content.ProductItemFilterI;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.SuperDepartmentModel;

public class NavigationModel {
	
	/**
	 * Current user navigating	
	 */
	private FDUserI user;

	/**
	 * Depth of the navigation (DEP, CAT, ...)
	 */
	private NavDepth navDepth;
	
	/**
	 * Left side navigation (menu)
	 */
	private List<MenuBoxData> leftNav;
	
	/**
	 * The actual product container
	 */
	private ContentNodeModel selectedContentNodeModel;
	
	/**
	 * Breadcumb
	 */
	private List<ContentNodeModel> contentNodeModelPath;
	
	/**
	 * Navigation tree, store contentNodeModel for each level
	 */
	private Map<NavDepth, ContentNodeModel> navigationHierarchy;
	
	/**
	* The super department of the hierarchy if any
	*/
	private SuperDepartmentModel superDepartmentModel;
	
	/**
	 * All filters
	 */
	private List<ProductFilterGroup> allFilters = new ArrayList<ProductFilterGroup>();
	
	/**
	 * Selected filters
	 */
	private Set<ProductItemFilterI> activelFilters = new HashSet<ProductItemFilterI>();
	
	/**
	 * sort options associated with the actual department
	 */
	
	private List<CategoryModel> regularCategories;
	private List<CategoryModel> preferenceCategories;
	private List<CategoryModel> popularCategories;
	private List<CategorySectionModel> categorySections;
	private List<FilteringSortingItem<ProductModel>> searchResults = new ArrayList<FilteringSortingItem<ProductModel>>();
	private List<Recipe> recipeResults = new ArrayList<Recipe>();
	private Map<String, DepartmentModel> departmentsOfSearchResults = new HashMap<String, DepartmentModel>();
	private Map<String, CategoryModel> categoriesOfSearchResults = new HashMap<String, CategoryModel>();
	private Map<String, CategoryModel> subCategoriesOfSearchResults = new HashMap<String, CategoryModel>();
	private Map<String, BrandModel> brandsOfSearchResults = new HashMap<String, BrandModel>();
	private Set<String> showMeOnlyOfSearchResults = new HashSet<String>();
	private List<DepartmentModel> departmentsOfSuperDepartment;
	private boolean productListing;
	private boolean recipeListing;
	private EnumBrandFilterLocation brandFilterLocation;
	private String pageTitle;
	private String metaDescription;
	
	public NavDepth getNavDepth() {
		return navDepth;
	}
	public void setNavDepth(NavDepth navDepth) {
		this.navDepth = navDepth;
	}
	public List<MenuBoxData> getLeftNav() {
		return leftNav;
	}
	public void setLeftNav(List<MenuBoxData> leftNav) {
		this.leftNav = leftNav;
	}
	public ContentNodeModel getSelectedContentNodeModel() {
		return selectedContentNodeModel;
	}
	public void setSelectedContentNodeModel(ContentNodeModel selectedContentNodeModel) {
		this.selectedContentNodeModel = selectedContentNodeModel;
	}
	public List<ContentNodeModel> getContentNodeModelPath() {
		return contentNodeModelPath;
	}
	public void setContentNodeModelPath(List<ContentNodeModel> ContentNodeModelPath) {
		this.contentNodeModelPath = ContentNodeModelPath;
	}
	public Map<NavDepth, ContentNodeModel> getNavigationHierarchy() {
		return navigationHierarchy;
	}
	public void setNavigationHierarchy(Map<NavDepth, ContentNodeModel> navigationHierarchy) {
		this.navigationHierarchy = navigationHierarchy;
	}
	public List<CategoryModel> getRegularCategories() {
		return regularCategories;
	}
	public void setRegularCategories(List<CategoryModel> regularCategories) {
		this.regularCategories = regularCategories;
	}
	public List<CategoryModel> getPreferenceCategories() {
		return preferenceCategories;
	}
	public void setPreferenceCategories(List<CategoryModel> preferenceCategories) {
		this.preferenceCategories = preferenceCategories;
	}
	public Set<ProductItemFilterI> getActiveFilters() {
		return activelFilters;
	}
	public void setActiveFilters(Set<ProductItemFilterI> activelFilters) {
		this.activelFilters = activelFilters;
	}
	public FDUserI getUser() {
		return user;
	}
	public void setUser(FDUserI user) {
		this.user = user;
	}
	public List<ProductFilterGroup> getAllFilters() {
		return allFilters;
	}
	public void setAllFilters(List<ProductFilterGroup> allFilters) {
		this.allFilters = allFilters;
	}
	public boolean isProductListing() {
		return productListing;
	}
	public void setProductListing(boolean productListing) {
		this.productListing = productListing;
	}
	public boolean isSuperDepartment() {
		return (selectedContentNodeModel instanceof SuperDepartmentModel);
	}
	public SuperDepartmentModel getSuperDepartmentModel() {
		return superDepartmentModel;
	}
	public void setSuperDepartmentModel(SuperDepartmentModel superDepartmentModel) {
		this.superDepartmentModel = superDepartmentModel;
	}
	public List<CategoryModel> getPopularCategories() {
		return popularCategories;
	}
	public void setPopularCategories(List<CategoryModel> popularCategories) {
		this.popularCategories = popularCategories;
	}
	public List<DepartmentModel> getDepartmentsOfSuperDepartment() {
		return departmentsOfSuperDepartment;
	}
	public void setDepartmentsOfSuperDepartment(List<DepartmentModel> departmentsOfSuperDepartment) {
		this.departmentsOfSuperDepartment = departmentsOfSuperDepartment;
	}
	public List<CategorySectionModel> getCategorySections() {
		return categorySections;
	}
	public void setCategorySections(List<CategorySectionModel> categorySections) {
		this.categorySections = categorySections;
	}
	public List<FilteringSortingItem<ProductModel>> getSearchResults() {
		return searchResults;
	}
	public Map<String, DepartmentModel> getDepartmentsOfSearchResults() {
		return departmentsOfSearchResults;
	}
	public void setDepartmentsOfSearchResults(Map<String, DepartmentModel> departmentsOfSearchResults) {
		this.departmentsOfSearchResults = departmentsOfSearchResults;
	}
	public Map<String, CategoryModel> getCategoriesOfSearchResults() {
		return categoriesOfSearchResults;
	}
	public void setCategoriesOfSearchResults(Map<String, CategoryModel> categoriesOfSearchResults) {
		this.categoriesOfSearchResults = categoriesOfSearchResults;
	}
	public Map<String, CategoryModel> getSubCategoriesOfSearchResults() {
		return subCategoriesOfSearchResults;
	}
	public void setSubCategoriesOfSearchResults(Map<String, CategoryModel> subCategoriesOfSearchResults) {
		this.subCategoriesOfSearchResults = subCategoriesOfSearchResults;
	}
	public void setSearchResults(List<FilteringSortingItem<ProductModel>> searchResults) {
		this.searchResults = searchResults;
	}
	public Map<String, BrandModel> getBrandsOfSearchResults() {
		return brandsOfSearchResults;
	}
	public void setBrandsOfSearchResults(Map<String, BrandModel> brandsOfSearchResults) {
		this.brandsOfSearchResults = brandsOfSearchResults;
	}
	public Set<String> getShowMeOnlyOfSearchResults() {
		return showMeOnlyOfSearchResults;
	}
	public void setShowMeOnlyOfSearchResults(Set<String> showMeOnlyOfSearchResults) {
		this.showMeOnlyOfSearchResults = showMeOnlyOfSearchResults;
	}
	
	public boolean isRecipeListing() {
		return recipeListing;
	}
	
	public void setRecipeListing(boolean recipeListing) {
		this.recipeListing = recipeListing;
	}
	public List<Recipe> getRecipeResults() {
		return recipeResults;
	}
	public void setRecipeResults(List<Recipe> recipeResults) {
		this.recipeResults = recipeResults;
	}
	public EnumBrandFilterLocation getBrandFilterLocation() {
		return brandFilterLocation;
	}
	public void setBrandFilterLocation(EnumBrandFilterLocation brandFilterLocation) {
		this.brandFilterLocation = brandFilterLocation;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
}
