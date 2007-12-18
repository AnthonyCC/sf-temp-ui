package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;

public class RecipeCategory extends ContentNodeModelImpl {

	private final List subcategories = new ArrayList();
	private final List featuredRecipes = new ArrayList();

	public RecipeCategory(ContentKey cKey) {
		super(cKey);
	}

	public String getName() {
		return getAttribute("name", "");
	}

	public String getRedirectUrl() {
		return getAttribute("REDIRECT_URL", "");
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
		DomainRef domRef= (DomainRef) getAttribute("classification", (DomainRef) null);
		
		return domRef == null
	     ? null
         : domRef.getDomain();
	}

	public Image getPhoto() {
		return (Image) getAttribute("photo", (Image) null);
	}

	public Image getLabel() {
		return (Image) getAttribute("label", (Image) null);
	}

	public Image getZoomLabel() {
		return (Image) getAttribute("zoomLabel", (Image) null);
	}
	
	public List getFeaturedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "featuredRecipes", featuredRecipes, false);
		return Collections.unmodifiableList(featuredRecipes);
	}

	public RecipeSource getFeaturedSource() {
		AttributeI attrib = getCmsAttribute("featuredSource");
		if (attrib==null) return null;
		ContentKey key = (ContentKey) attrib.getValue();
		return key == null ? null : (RecipeSource) ContentFactory.getInstance().getContentNode(key.getId());
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

}
