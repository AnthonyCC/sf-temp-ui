package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class RecipeDepartment extends ContentNodeModelImpl implements HasRedirectUrl {

	private final List<RecipeCategory> categories = new ArrayList<RecipeCategory>();
	private final List<Recipe> featuredRecipes = new ArrayList<Recipe>();
    private final List<ProductModel> featuredProdcuts = new ArrayList<ProductModel>();
    private final List<RecipeCategory> departmentNav = new ArrayList<RecipeCategory>();
    private final List<RecipeSource> featuredSources = new ArrayList<RecipeSource>();
    
	public RecipeDepartment(ContentKey cKey) {
		super(cKey);
	}

	/**
	 * Get the default recipe department (currently, just picks the first).
	 * 
	 * @return default RecipeDepartment
	 */
	public static RecipeDepartment getDefault() {
		Set<ContentKey> deptKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.RECIPE_DEPARTMENT);

		if (deptKeys.isEmpty()) {
			return null;
		}
		ContentKey deptKey = deptKeys.iterator().next();
		ContentNodeModel deptNode = ContentFactory.getInstance().getContentNode(deptKey.getId());
		return (RecipeDepartment) deptNode;
	}
	
	public String getName() {
		return getAttribute("name", "");
	}
	
	public String getRedirectUrl() {
		return (String) getCmsAttributeValue("REDIRECT_URL");
	}

	public List<RecipeCategory> getDepartmentNav() {
		ContentNodeModelUtil.refreshModels(this, "departmentNav", departmentNav, false);
		// default to getCategories(), if no dept. nav specified
		return departmentNav.isEmpty() ? getCategories() : Collections.unmodifiableList(departmentNav);
	}

	/**
	 *  Get the list of recipe categories.
	 *  
	 *  @return a list of RecipeCategory objects.
	 */
	public List<RecipeCategory> getCategories() {
		ContentNodeModelUtil.refreshModels(this, "categories", categories, true);
		return Collections.unmodifiableList(categories);
	}

	public List<Recipe> getFeaturedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "featuredRecipes", featuredRecipes, false);
		return Collections.unmodifiableList(featuredRecipes);
	}

	/* it's not a typo: the featured recipe category is actually a RecipeSubcategory */
	public RecipeSubcategory getFeaturedRecipeCategory() {
		return FDAttributeFactory.lookup(this, "featuredRecipeCategory", (RecipeSubcategory) null);
	}
	
	public List<ProductModel> getFeaturedProducts() {
		ContentNodeModelUtil.refreshModels(this, "featuredProducts", featuredProdcuts, false);
		return Collections.unmodifiableList(featuredProdcuts);
	}

	public List<ProductModel> getAvailableFeaturedProducts() {
		List<ProductModel> availProducts = new ArrayList<ProductModel>();
		for ( ProductModel p : getFeaturedProducts() ) {
			if (!p.isUnavailable()) {
				availProducts.add(p);
			}
		}
		return Collections.unmodifiableList(availProducts);		
	}
	
	public List<RecipeSource> getFeaturedSources() {
		ContentNodeModelUtil.refreshModels(this, "featuredSource", featuredSources, false);
		return Collections.unmodifiableList(featuredSources);
	}
	
	public List<RecipeSource> getAvailableFeaturedSources() {
		List<RecipeSource> availSources = new ArrayList<RecipeSource>();
		for ( RecipeSource s : getFeaturedSources() ) {
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
