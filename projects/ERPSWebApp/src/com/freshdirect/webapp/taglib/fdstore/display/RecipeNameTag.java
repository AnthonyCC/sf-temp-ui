package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.Recipe;

public class RecipeNameTag extends BodyTagSupportEx {

	private static final long serialVersionUID = 2032208830336954190L;

	private Recipe recipe;

	@Override
	public int doStartTag() throws JspException {

		try {
			pageContext.getOut().print( recipe.getFullName() );
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	public Recipe getRecipe() {
		return recipe;
	}
	
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
}
