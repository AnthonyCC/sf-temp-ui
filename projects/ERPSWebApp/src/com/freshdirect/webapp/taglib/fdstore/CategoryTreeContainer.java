package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.storeapi.content.CategoryNodeTree;

public interface CategoryTreeContainer {
	public CategoryNodeTree getCategoryTree();
	
	public CategoryNodeTree getFilteredCategoryTree();
}
