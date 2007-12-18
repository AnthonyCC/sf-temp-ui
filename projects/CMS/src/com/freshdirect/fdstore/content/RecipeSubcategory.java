package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.ContentKey;

public class RecipeSubcategory extends ContentNodeModelImpl {

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
		return getAttribute("REDIRECT_URL", "");
	}

	public DomainValue getClassification() {
		DomainValueRef domvalRef = (DomainValueRef) getAttribute("classification",(DomainValueRef) null);
		return domvalRef==null
		? null
		:domvalRef.getDomainValue();
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
		return (Image) getAttribute("label", (Image) null);
	}

	public String getPath() {
		return getParentNode().getPath() + "/" + getContentName();
	}
	
}
