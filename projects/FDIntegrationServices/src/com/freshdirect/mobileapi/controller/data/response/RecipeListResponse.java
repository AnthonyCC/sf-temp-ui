package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class RecipeListResponse extends Message {
	private List<RecipeDetailResponse> recipes;

	public List<RecipeDetailResponse> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<RecipeDetailResponse> recipes) {
		this.recipes = recipes;
	}
}
