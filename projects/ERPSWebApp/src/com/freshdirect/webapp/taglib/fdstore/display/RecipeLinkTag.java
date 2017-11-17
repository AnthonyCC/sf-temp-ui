package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class RecipeLinkTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 6061486441722803755L;

	private Recipe recipe;

	private String trackingCode;


	@Override
	public int doStartTag() throws JspException {
		String url="/recipe.jsp?recipeId=" + recipe.getContentName() + "&amp;trk="+trackingCode;

		try {
			pageContext.getOut().print("<a href=\"" + url + "\">");
		} catch (IOException e) {
			throw new JspException(e);
		}

		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().print("</a>");
		} catch (IOException e) {
			throw new JspException(e);
		}
		return super.doEndTag();
	}

	public Recipe getRecipe() {
		return recipe;
	}
	
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

}
