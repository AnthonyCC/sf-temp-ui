package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class RecipeSubcategory extends ContentNodeModelImpl implements HasRedirectUrl {

	private final List groupBy = new ArrayList();

	private final List filterBy = new ArrayList();

	private final List featuredRecipes = new ArrayList();

	private final List featuredProducts = new ArrayList();

	public RecipeSubcategory(ContentKey cKey) {
		super(cKey);
	}

	public String getName() {
		return getAttribute("name", "");
	}

	public String getFullName() {
		return getName();
	}
	
	public String getRedirectUrl() {
		return (String) getCmsAttributeValue("REDIRECT_URL");
	}

	public DomainValue getClassification() {
	    ContentKey key = (ContentKey) getCmsAttributeValue("classification");
	    return key != null ? ContentFactory.getInstance().getDomainValueById(key.getId()) : null;
	}
	
	public List getGroupBy() {
		ContentNodeModelUtil.refreshModels(this, "groupBy", groupBy, false);
		return Collections.unmodifiableList(groupBy);
	}

	public List getFilterBy() {
		ContentNodeModelUtil.refreshModels(this, "filterBy", filterBy, false);
		return Collections.unmodifiableList(filterBy);
	}

	public List getFeaturedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "featuredRecipes", featuredRecipes, false);
		return Collections.unmodifiableList(featuredRecipes);
	}

	public List getFeaturedProducts() {
		ContentNodeModelUtil.refreshModels(this, "featuredProducts", featuredProducts, false);
		return Collections.unmodifiableList(featuredProducts);
	}
	
	public Image getLabel() {
	    return FDAttributeFactory.constructImage(this, "label");
	}
	
        public Html getRecipeEditorial() {
            return FDAttributeFactory.constructHtml(this, "editorial");
        }

	public String getPath() {
		return getParentNode().getPath() + "/" + getContentName();
	}
	
}
