package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class SimpleRecipeImageTag extends BodyTagSupportEx {

	private static final long serialVersionUID = 2032208830336954190L;

	private Recipe recipe;

	@Override
	public int doStartTag() throws JspException {
		String src = null;
		Image image = recipe.getPhoto();
		if (image != null)
			src = image.getPath();
		else
			src = "/media/images/temp/soon_c.gif";

		try {
			pageContext.getOut().print("<img src=\"" + StringEscapeUtils.escapeHtml(src) + "\">");
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
