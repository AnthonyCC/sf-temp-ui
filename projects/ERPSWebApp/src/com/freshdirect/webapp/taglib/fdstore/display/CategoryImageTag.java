package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;


/**
 * Category Image Tag <br/>
 * 
 * Displays a category image, with optional action. <br/>
 * Based on ProductImageTag, without the extra stuff (bursts, rollover, opacity, etc..)
 * 
 * @author treer
 */

public class CategoryImageTag extends BodyTagSupport {

	private static final long serialVersionUID = 8159061278833068855L;
	
	private static final Logger LOGGER = LoggerFactory.getInstance( CategoryImageTag.class );

	CategoryModel	category; 					// category (mandatory)
	String			style; 						// CSS style modification (optional)
	String			className;					// CSS class name (optional)
	String			action; 					// URL (optional)
	boolean			disabled = false; 			// Image is not clickable
	String			prefix; 					// For internal use only! (optional)
	
	public void setCategory(CategoryModel cat) {
		this.category = cat;
	}
	
	public void setStyle(String text) {
		this.style = text;
	}
	
	public void setClassName(String name) {
		this.className = name;
	}
	
	public void setAction(String action) {
		this.action = action;
	}

	public void setDisabled(boolean flag) {
		this.disabled = flag;
	}
	
	public void setPrefix(String uriPrefix) {
		this.prefix = uriPrefix;
	}

	
	public int doStartTag() {
		try {
			Image catImg = category.getCategoryPhoto(); // TODO ez a kep kell?
			
			if ( catImg == null ) {
				LOGGER.warn( "Missing category image: " + category.getContentKey() );
				return SKIP_BODY;
			}
			
			JspWriter out = pageContext.getOut();
			
			StringBuffer buf = new StringBuffer();
			
			
			// not disabled, has action and not in cart (savings) -> add link
			final boolean shouldGenerateAction = !this.disabled && this.action != null;


			String imageStyle = "border: 0; ";
			

			buf.append("<div style=\"padding: 0px; border: 0px; margin: 0px auto; "
					+ "width: " + catImg.getWidth() + "px; "
					+ "height: " + catImg.getHeight() + "px; "
					+ "position: relative;\">");

			
			String imageName = "ro_img_" + category.getContentName();
			

			// product image
			if (shouldGenerateAction) {
				buf.append("<a href=\"");
				buf.append(action);
				buf.append("\">");
			}

			buf.append("<img src=\"");
			if (this.prefix != null)
				buf.append(this.prefix);
			buf.append(catImg.getPath());
			buf.append("\"");
			
			buf.append(" width=\"");
			buf.append(catImg.getWidth());
			buf.append("\"");
			
			buf.append(" height=\"");
			buf.append(catImg.getHeight());
			buf.append("\"");

			buf.append(" alt=\"");
			buf.append(category.getFullName());
			buf.append("\"");
			
			if (className != null && className.length() > 0) {
				buf.append(" class=\"");
				buf.append(className);
				buf.append("\"");
			}
			
			if (style != null && style.length() > 0) {
				buf.append(" style=\"");
				buf.append(imageStyle + " " + style);
				buf.append("\"");
			} else {
				buf.append(" style=\"");
				buf.append(imageStyle);
				buf.append("\"");
			}
			
			buf.append( " name=\"" );
			buf.append( imageName );
			buf.append( "\"" );
			
			buf.append(">");

			if (shouldGenerateAction) {
				buf.append("</a>");
			}
			
			buf.append("</div>");

			out.println(buf.toString());
		} catch (IOException e) {
		}
		
		return EVAL_BODY_INCLUDE;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}
