package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class RecipeCategory extends ContentNodeModelImpl implements HasRedirectUrl {

	private final List subcategories = new ArrayList();
	private final List featuredRecipes = new ArrayList();

	public RecipeCategory(ContentKey cKey) {
		super(cKey);
	}

	public String getName() {
		return getAttribute("name", "");
	}

	public String getRedirectUrl() {
            return (String) getCmsAttributeValue("REDIRECT_URL");
	}

	/**
	 *  Get the subcategories of this category.
	 * 
	 *  @return a list of RecipeSubcategory objects.
	 */
	public List getSubcategories() {
		ContentNodeModelUtil.refreshModels(this, "subcategories", subcategories, true);
		return Collections.unmodifiableList(subcategories);
	}

	public Domain getClassification() {
	    ContentKey key = (ContentKey) getCmsAttributeValue("classification");
	    return key != null ? ContentFactory.getInstance().getDomainById(key.getId()) : null;
	}

	public Image getPhoto() {
            return FDAttributeFactory.constructImage(this, "photo");
	}

	public Image getLabel() {
            return FDAttributeFactory.constructImage(this, "label");
	}

	public Image getZoomLabel() {
            return FDAttributeFactory.constructImage(this, "zoomLabel");
	}
	
	public List getFeaturedRecipes() {
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

	public String getPath() {
		return getParentNode().getPath() + "/" + getContentName();
	}

	public String getFullName() {
		return getName();
	}
	
	public Html getRecipeEditorial() {
	    return FDAttributeFactory.constructHtml(this, "editorial");
	}

}
