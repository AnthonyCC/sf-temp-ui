package com.freshdirect.fdstore.lists;

import java.util.Comparator;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Recipe;

public class FDCustomerRecipeListLineItem extends FDCustomerListItem {

	public final static Comparator NAME_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			FDCustomerRecipeListLineItem l1 = (FDCustomerRecipeListLineItem) o1;
			FDCustomerRecipeListLineItem l2 = (FDCustomerRecipeListLineItem) o2;	
			return l1.getRecipe().getName().compareTo(l2.getRecipe().getName());
		}
		
	};
	private String recipeId;

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String skuCode) {
		this.recipeId = skuCode;
	}

	public Recipe getRecipe() {
		return (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
	}

}
