package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class RecipeCategory extends ContentNodeModelImpl implements HasRedirectUrl {

	private final List<RecipeSubcategory> subcategories = new ArrayList<RecipeSubcategory>();
	private final List<Recipe> featuredRecipes = new ArrayList<Recipe>();

    public RecipeCategory(ContentKey cKey) {
        super(cKey);
    }

	public String getName() {
		return getAttribute("name", "");
	}

	@Override
    public String getRedirectUrl() {
		return (String) getCmsAttributeValue("REDIRECT_URL");
	}

	/**
	 *  Get the subcategories of this category.
	 *
	 *  @return a list of RecipeSubcategory objects.
	 */
	public List<RecipeSubcategory> getSubcategories() {
		ContentNodeModelUtil.refreshModels(this, "subcategories", subcategories, true);
		return Collections.unmodifiableList(subcategories);
	}

	public Domain getClassification() {
	    ContentKey key = (ContentKey) getCmsAttributeValue("classification");
	    return key != null ? ContentFactory.getInstance().getDomainById(key.id) : null;
	}

	public Image getPhoto() {
		return FDAttributeFactory.constructImage( this, "photo" );
	}

	public Image getLabel() {
		return FDAttributeFactory.constructImage( this, "label" );
	}

	public Image getZoomLabel() {
		return FDAttributeFactory.constructImage( this, "zoomLabel" );
	}

	public List<Recipe> getFeaturedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "featuredRecipes", featuredRecipes, false);
		return Collections.unmodifiableList(featuredRecipes);
	}

	public RecipeSource getFeaturedSource() {
	        ContentKey key = (ContentKey) getCmsAttributeValue("featuredSource");
		return key == null ? null : (RecipeSource) ContentFactory.getInstance().getContentNodeByKey(key);
	}

	public boolean isSecondaryCategory() {
		return getAttribute("SECONDARY_CATEGORY",false);
	}

	@Override
    public String getPath() {
		return getParentNode().getPath() + "/" + getContentName();
	}

	@Override
    public String getFullName() {
		return getName();
	}

	public Html getRecipeEditorial() {
	    return FDAttributeFactory.constructHtml(this, "editorial");
	}

}
