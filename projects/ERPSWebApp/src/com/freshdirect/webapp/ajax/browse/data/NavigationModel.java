package com.freshdirect.webapp.ajax.browse.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategorySectionModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductFilterGroupI;
import com.freshdirect.fdstore.content.ProductItemFilterI;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.customer.FDUserI;

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
	 * All filters
	 */
	private List<ProductFilterGroupI> allFilters = new ArrayList<ProductFilterGroupI>();
	
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
	private List<DepartmentModel> departments;
	private boolean productListing;
	
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
	public List<ProductFilterGroupI> getAllFilters() {
		return allFilters;
	}
	public void setAllFilters(List<ProductFilterGroupI> allFilters) {
		this.allFilters = allFilters;
	}
	public boolean isProductListing() {
		return productListing;
	}
	public void setProductListing(boolean productListing) {
		this.productListing = productListing;
	}
	public boolean hasSuperDepartment() {
		return (selectedContentNodeModel instanceof SuperDepartmentModel);
	}
	public List<CategoryModel> getPopularCategories() {
		return popularCategories;
	}
	public void setPopularCategories(List<CategoryModel> popularCategories) {
		this.popularCategories = popularCategories;
	}
	public List<DepartmentModel> getDepartments() {
		return departments;
	}
	public void setDepartments(List<DepartmentModel> departments) {
		this.departments = departments;
	}
	public List<CategorySectionModel> getCategorySections() {
		return categorySections;
	}
	public void setCategorySections(List<CategorySectionModel> categorySections) {
		this.categorySections = categorySections;
	}
}
