package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupport;

/**
 * Product Image Tag
 * 
 * @author segabor
 *
 */
public class ProductImageTag extends BodyTagSupport {
	ProductModel	product; // product (mandatory)
	String			style; // CSS style modification (optional)
	String			className;	// CSS class name (optional)
	String			action; // URL (optional)
	String			prefix; // For internal use only! (optional)

	public void setProduct(ProductModel prd) {
		this.product = prd;
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

	public void setPrefix(String uriPrefix) {
		this.prefix = uriPrefix;
	}
	

	public int doStartTag() {
		try {
			Image prodImg = product.getProdImage();
			
			JspWriter out = pageContext.getOut();
			
			StringBuffer buf = new StringBuffer();
			
			if (action != null) {
				buf.append("<a href=\"");
				buf.append(action);
				buf.append("\">");
			}

			buf.append("<img src=\"");
			if (this.prefix != null)
				buf.append(this.prefix);
			buf.append(prodImg.getPath());
			buf.append("\"");
			
			buf.append(" width=\"");
			buf.append(prodImg.getWidth());
			buf.append("\"");
			
			buf.append(" height=\"");
			buf.append(prodImg.getHeight());
			buf.append("\"");

			buf.append(" alt=\"");
			buf.append(product.getFullName());
			buf.append("\"");
			
			if (action != null) {
				buf.append(" border=\"0\"");
			}
			
			if (className != null && className.length() > 0) {
				buf.append(" class=\"");
				buf.append(className);
				buf.append("\"");
			}
			
			if (style != null && style.length() > 0) {
				buf.append(" style=\"");
				buf.append(style);
				buf.append("\"");
			}

			buf.append(">");

			if (action != null) {
				buf.append("</a>");
			}

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
