package com.freshdirect.webapp.ajax.quickshop.data;

import java.io.Serializable;

public class QuickShopListDetails implements Serializable {
	
	private static final long	serialVersionUID	= 8126444320662373215L;
	
	private String recipeId;
	private String recipeUrl;
	private String recipeName;
	private static final String RECIPE_URL_BASE = "/recipe.jsp?recipeId=";
	
	public QuickShopListDetails(String recipeId, String recipeName, boolean alive) {
		super();
		this.recipeId = recipeId;
		if(alive){
			this.recipeUrl = RECIPE_URL_BASE + recipeId;			
		}
		this.recipeName = recipeName;
	}

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}

	public String getRecipeUrl() {
		return recipeUrl;
	}

	public void setRecipeUrl(String recipeUrl) {
		this.recipeUrl = recipeUrl;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

}
