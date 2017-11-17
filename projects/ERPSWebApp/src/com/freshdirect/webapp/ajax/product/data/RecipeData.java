package com.freshdirect.webapp.ajax.product.data;

import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.RecipeSource;


public class RecipeData {

	private String contentName;
	private String imagePath;
	private String fullName;
	private String recipeSourceName;
	private String recipeAuthors;

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(Image image) {
		if (image != null) {
			this.imagePath = image.getPath();
		} else {
			this.imagePath = "/media/images/temp/soon_c.gif";
		}
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRecipeSourceName() {
		return recipeSourceName;
	}

	public void setRecipeSourceName(RecipeSource recipeSource) {
		if (recipeSource != null) {
			this.recipeSourceName = recipeSource.getName();
		} else {
			this.recipeSourceName = null;
		}
	}

	public String getRecipeAuthors() {
		return recipeAuthors;
	}

	public void setRecipeAuthors(RecipeSource recipeSource) {
		if (recipeSource != null && !recipeSource.getAuthorNames().isEmpty()) {
			this.recipeAuthors = recipeSource.getAuthorNames();
		} else {
			this.recipeAuthors = null;
		}
	}
}
