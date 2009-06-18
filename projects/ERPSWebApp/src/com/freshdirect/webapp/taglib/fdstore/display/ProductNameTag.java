package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupport;


/**
 * 
 * Tag that displays name of product
 * 
 * @author segabor
 */
public class ProductNameTag extends BodyTagSupport {
	
	ProductModel	product; 							// product (mandatory)
	String			action;								// URL (optional)
	String			style 		= "";					// CSS style modification (optional)
	String			brandStyle 	= "font-weight:bold;";	// CSS style modification for brand name (optional)
	boolean			disabled 	= false;				// Not clickable (optional)
	
	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public void setStyle( String style ) {
		this.style = style;
	}

	public void setBrandStyle( String brandStyle ) {
		this.brandStyle = brandStyle;
	}
	
	public int doStartTag() {
		JspWriter out = pageContext.getOut();

		String fullName = product.getFullName();
		String brandName = product.getPrimaryBrandName();
		String shortenedProductName = null;

		if (brandName != null
			&& brandName.length() > 0
			&& (fullName.length() >= brandName.length())
			&& fullName.substring(0, brandName.length()).equalsIgnoreCase(brandName)) {

			shortenedProductName = fullName.substring(brandName.length()).trim();
		}
		
		StringBuffer buf = new StringBuffer();
		
		String styleStr = "";
		if ( style != null && !"".equals( style ) ) {
			styleStr = " style=\"" + style + "\"";
		}
		
		if ( !this.disabled && action != null ) {
			buf.append("<a href=\"" + action + "\"" + styleStr + ">");
		} else {
			buf.append("<span" + styleStr + ">");
		}

		if ( shortenedProductName != null ) {
			buf.append("<span style=\"" );
			buf.append( brandStyle );
			buf.append( "\">");
			buf.append(brandName);
			buf.append("</span><br/>");
			buf.append(shortenedProductName);
		} else {
			buf.append( fullName != null && !"".equalsIgnoreCase(fullName) ? fullName : "(this product)" );
		}

		if ( !this.disabled && action != null ) {
			buf.append("</a>");
		} else {
			buf.append("</span>");
		}

		try {
			// write out
			out.write(buf.toString());
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
