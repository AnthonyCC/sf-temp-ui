package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;
import java.util.Map;

import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.Product;

public class RecipeDetailResponse extends Message {
	public static class Ingredient {
		private String name;
		private double quantity;
		private String uom;
		private String text;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getQuantity() {
			return quantity;
		}
		public void setQuantity(double quantity) {
			this.quantity = quantity;
		}
		public String getUom() {
			return uom;
		}
		public void setUom(String uom) {
			this.uom = uom;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
	}

	private String recipeId;
	private String recipeTitle;
	private String recipeText;
    private Image recipeImage;
    private String recipeUrl;
    private List<String> tags;
    private List<List<Product>> ingredients;
    private Map<String, String> ingredientLoadingErrors;
    private List<Ingredient> foodilyIngredients;
    
    //APPDEV-4238 -- Ingredients you may already have at hand
    private List<List<Product>> ingredientsYmah;

	public String getRecipeTitle() {
		return recipeTitle;
	}
	public void setRecipeTitle(String recipeTitle) {
		this.recipeTitle = recipeTitle;
	}
	public String getRecipeText() {
		return recipeText;
	}
	public void setRecipeText(String recipeText) {
		this.recipeText = recipeText;
	}
	public Image getRecipeImage() {
		return recipeImage;
	}
	public void setRecipeImage(Image recipeImage) {
		this.recipeImage = recipeImage;
	}
	public String getRecipeUrl() {
		return recipeUrl;
	}
	public void setRecipeUrl(String recipeUrl) {
		this.recipeUrl = recipeUrl;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<List<Product>> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<List<Product>> ingredients) {
		this.ingredients = ingredients;
	}
	public String getRecipeId() {
		return recipeId;
	}
	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}
	public Map<String, String> getIngredientLoadingErrors() {
		return ingredientLoadingErrors;
	}
	public void setIngredientLoadingErrors(Map<String, String> ingredientLoadingErrors) {
		this.ingredientLoadingErrors = ingredientLoadingErrors;
	}
	public List<Ingredient> getFoodilyIngredients() {
		return foodilyIngredients;
	}
	public void setFoodilyIngredients(List<Ingredient> foodilyIngredients) {
		this.foodilyIngredients = foodilyIngredients;
	}

	public List<List<Product>> getIngredientsYmah() {
		return ingredientsYmah;
	}
	public void setIngredientsYmah(List<List<Product>> ingredientsYmah) {
		this.ingredientsYmah = ingredientsYmah;
	}
}
