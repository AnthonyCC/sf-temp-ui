package com.freshdirect.fdstore.content;

import java.util.List;
import java.util.Set;

public interface YmalSource extends ContentNodeModel {
	public YmalSet getActiveYmalSet();

	public List getYmalProducts();

	public List getYmalProducts(Set removeSkus);

	public List getYmalCategories();

	public List getYmalRecipes();
	
	public List getRelatedProducts();
	
	public String getYmalHeader();
}
