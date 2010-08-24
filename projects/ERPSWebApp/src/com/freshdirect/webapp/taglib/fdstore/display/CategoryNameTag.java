package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.framework.webapp.BodyTagSupport;

/**
 * Category Name Tag <br/>
 * 
 * Displays a category name, with optional action. <br/>
 * Based on ProductNameTag.
 * 
 * @author treer
 */

public class CategoryNameTag extends BodyTagSupport {

	private static final long	serialVersionUID	= 6930552128020277946L;
	
	CategoryModel	category;							// category (mandatory)
	String			action;								// URL (optional)
	String			style		= "font-weight:bold";	// CSS style modification (optional)
	boolean			disabled	= false;				// Not clickable (optional)

	public void setCategory( CategoryModel cat ) {
		this.category = cat;
	}

	public void setAction( String action ) {
		this.action = action;
	}

	public void setDisabled( boolean disabled ) {
		this.disabled = disabled;
	}

	public void setStyle( String style ) {
		this.style = style;
	}

	public int doStartTag() {
		JspWriter out = pageContext.getOut();

		String fullName = category.getFullName();
		String styleString = ( style == null || style.trim().equals( "" ) ) ? " " : " style=\"" + style + "\"" ;

		StringBuffer buf = new StringBuffer();

		if ( !this.disabled && action != null ) {
			buf.append( "<a href=\"" + action + "\"" + styleString + ">" );
		} else {
			buf.append( "<span" + styleString + ">" );
		}

		buf.append( fullName != null && !"".equalsIgnoreCase( fullName ) ? fullName : "(this category)" );

		if ( !this.disabled && action != null ) {
			buf.append( "</a>" );
		} else {
			buf.append( "</span>" );
		}

		try {
			// write out
			out.write( buf.toString() );
		} catch ( IOException e ) {
		}

		return EVAL_BODY_INCLUDE;
	}

	public static class TagEI extends TagExtraInfo {

		public VariableInfo[] getVariableInfo( TagData data ) {
			return new VariableInfo[] {};
		}
	}
}
