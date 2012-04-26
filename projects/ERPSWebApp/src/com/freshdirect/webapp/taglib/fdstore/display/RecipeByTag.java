package com.freshdirect.webapp.taglib.fdstore.display;

import java.util.LinkedList;

import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeSource;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class RecipeByTag extends AbstractGetterTag<String> {

	private static final long serialVersionUID = -455534700932291551L;

	private Recipe recipe;

	public Recipe getRecipe() {
		return recipe;
	}
	
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
	@Override
	protected String getResult() throws Exception {
		RecipeSource recipeSource = recipe.getSource();
		String recipeAuthors;
		
		if( recipeSource == null ) {
			return null;	
		}
		else {
			recipeAuthors = recipeSource.getAuthorNames(); 
			if( recipeAuthors.isEmpty() ) {
				return null;
			}
			return recipeAuthors;
		}		
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return String.class.getName();
		}
	}
	
	
}
