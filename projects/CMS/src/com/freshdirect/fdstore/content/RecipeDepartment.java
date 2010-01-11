package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class RecipeDepartment extends ContentNodeModelImpl implements HasRedirectUrl {

	private final List categories = new ArrayList();
	private final List featuredRecipes = new ArrayList();
    private final List featuredProdcuts = new ArrayList();
    private final List departmentNav = new ArrayList();
    private final List featuredSources = new ArrayList();
    
	public RecipeDepartment(ContentKey cKey) {
		super(cKey);
	}

	/**
	 * Get the default recipe department (currently, just picks the first).
	 * 
	 * @return default RecipeDepartment
	 */
	public static RecipeDepartment getDefault() {
		Set deptKeys = CmsManager.getInstance().getContentKeysByType(
				FDContentTypes.RECIPE_DEPARTMENT);

		if (deptKeys.isEmpty()) {
			return null;
		}
		ContentKey deptKey = (ContentKey) deptKeys.iterator().next();
		ContentNodeModel deptNode = ContentFactory.getInstance()
				.getContentNode(deptKey.getId());
		return (RecipeDepartment) deptNode;
	}
	
	public String getName() {
		return getAttribute("name", "");
	}
	
	public String getRedirectUrl() {
            return (String) getCmsAttributeValue("REDIRECT_URL");
	}

	public List getDepartmentNav() {
		ContentNodeModelUtil.refreshModels(this, "departmentNav", departmentNav, false);
		// default to getCategories(), if no dept. nav specified
		return departmentNav.isEmpty() ? getCategories() : Collections
				.unmodifiableList(departmentNav);
	}

	/**
	 *  Get the list of recipe categories.
	 *  
	 *  @return a list of RecipeCategory objects.
	 */
	public List getCategories() {
		ContentNodeModelUtil.refreshModels(this, "categories", categories, true);
		return Collections.unmodifiableList(categories);
	}

	public List getFeaturedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "featuredRecipes", featuredRecipes, false);
		return Collections.unmodifiableList(featuredRecipes);
	}

	/* it's not a typo: the featured recipe category is actually a RecipeSubcategory */
	public RecipeSubcategory getFeaturedRecipeCategory() {
		return FDAttributeFactory.lookup(this, "featuredRecipeCategory", (RecipeSubcategory) null);
	}
	
	public List getFeaturedProducts() {
		ContentNodeModelUtil.refreshModels(this, "featuredProducts", featuredProdcuts, false);
		return Collections.unmodifiableList(featuredProdcuts);
	}

	public List getAvailableFeaturedProducts() {
		List availProducts = new ArrayList();
		for (Iterator itrFp = getFeaturedProducts().iterator(); itrFp.hasNext(); ){
			ProductModel p = (ProductModel)itrFp.next();
			if (!p.isUnavailable()) {
				availProducts.add(p);
			}
		}
		return Collections.unmodifiableList(availProducts);
		
	}
	
	public List getFeaturedSources() {
		ContentNodeModelUtil.refreshModels(this, "featuredSource", featuredSources, false);
		return Collections.unmodifiableList(featuredSources);
	}
	
	public List getAvailableFeaturedSources() {
		List availSources = new ArrayList();
		for (Iterator itrFs = getFeaturedSources().iterator(); itrFs.hasNext(); ){
			RecipeSource s = (RecipeSource)itrFs.next();
			if (s.isAvailable()) {
				availSources.add(s);
			}
		}
		return Collections.unmodifiableList(availSources);
	
	}

	public CategoryModel getFeaturedProductCategory() {
	    return FDAttributeFactory.lookup(this, "featuredProductCategory", (CategoryModel) null);
	}
	
	public String getPath() {
		return "www.freshdirect.com/" + getContentName();
	}
	
        public Html getRecipeEditorial() {
            return FDAttributeFactory.constructHtml(this, "editorial");
        }
	
}
