package com.freshdirect.fdstore.content;

import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.FDSku;

public interface YmalSource extends ContentNodeModel {
	
	public YmalSet getActiveYmalSet();

	/**
	 * !!! HACK !!!
	 * 
	 * This is a nasty hack to reset the active ymal set in the current ymal
	 * source. This is necessary because the active ymal set should be the same
	 * during the entire life-time of the request and at the first time it
	 * should be randomly chosen among the active ymal sets associated with the
	 * current source
	 */
	public void resetActiveYmalSetSession();

	public List<ProductModel> getYmalProducts();

	public List<ProductModel> getYmalProducts(Set<FDSku> removeSkus);

	public List<CategoryModel> getYmalCategories();

	public List<Recipe> getYmalRecipes();

	public List<ProductModel> getRelatedProducts();

	public String getYmalHeader();
}
