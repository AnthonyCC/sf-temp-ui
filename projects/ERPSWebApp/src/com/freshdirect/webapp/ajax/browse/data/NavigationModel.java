package com.freshdirect.webapp.ajax.browse.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductFilterGroupI;
import com.freshdirect.fdstore.content.ProductItemFilterI;
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
	private ProductContainer selectedProductContainer;
	
	/**
	 * Breadcumb
	 */
	private List<ProductContainer> productContainerPath;
	
	/**
	 * Navigation tree, store productContainer for each level
	 */
	private Map<NavDepth, ProductContainer> navigationHierarchy;
	
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
	public ProductContainer getSelectedProductContainer() {
		return selectedProductContainer;
	}
	public void setSelectedProductContainer(ProductContainer selectedProductContainer) {
		this.selectedProductContainer = selectedProductContainer;
	}
	public List<ProductContainer> getProductContainerPath() {
		return productContainerPath;
	}
	public void setProductContainerPath(List<ProductContainer> productContainerPath) {
		this.productContainerPath = productContainerPath;
	}
	public Map<NavDepth, ProductContainer> getNavigationHierarchy() {
		return navigationHierarchy;
	}
	public void setNavigationHierarchy(Map<NavDepth, ProductContainer> navigationHierarchy) {
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
	public Set<ProductItemFilterI> getActivelFilters() {
		return activelFilters;
	}
	public void setActivelFilters(Set<ProductItemFilterI> activelFilters) {
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
}
