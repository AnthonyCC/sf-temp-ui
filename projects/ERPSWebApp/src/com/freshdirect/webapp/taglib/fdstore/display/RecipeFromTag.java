package com.freshdirect.webapp.taglib.fdstore.display;

import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.RecipeSource;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class RecipeFromTag extends AbstractGetterTag<String> {

	private static final long serialVersionUID = 3224919182665458205L;

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
		
		if( recipeSource == null ) {
			return null;	
		}
		else {
			return recipeSource.getName();			
		}		
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return String.class.getName();
		}
	}

}
