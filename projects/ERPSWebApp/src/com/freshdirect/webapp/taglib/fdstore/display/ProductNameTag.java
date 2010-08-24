package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.util.Set;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.EnumBurstType;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.ProductLabeling;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


/**
 * 
 * Tag that displays name of product
 * 
 * @author segabor
 */
public class ProductNameTag extends BodyTagSupport {
	
	private static final long	serialVersionUID	= 2202799484204844350L;
	
	ProductModel	product; 								// product (mandatory)
	String			action;									// URL (optional)
	String			style 			= "";					// CSS style modification (optional)
	String			brandStyle 		= "font-weight:bold;";	// CSS style modification for brand name (optional)
	boolean			disabled 		= false;				// Not clickable (optional)
	boolean         showNew     	= false;                // Show NEW! label right after name
	boolean			showFavourite 	= false;
	boolean			showBrandName 	= true;					// Show the brand part of the name
	boolean			noBreak 		= false;				// break after the brand name
	
	Set<EnumBurstType> hideBursts;
	
	public void setHideBursts(Set<EnumBurstType> hideBursts) {
		this.hideBursts = hideBursts;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public void setShowFavourite(boolean showFavourite) {
		this.showFavourite = showFavourite;
	}

	public void setStyle( String style ) {
		this.style = style;
	}

	public void setBrandStyle( String brandStyle ) {
		this.brandStyle = brandStyle;
	}
	
	public void setShowNew(boolean showNew) {
		this.showNew = showNew;
	}
	
	public void setShowBrandName( boolean showBrandName ) {
		this.showBrandName = showBrandName;
	}

	public boolean isNoBreak() {
		return noBreak;
	}

	public void setNoBreak(boolean noBreak) {
		this.noBreak = noBreak;
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
	
		ProductLabeling pl = new ProductLabeling((FDUserI) pageContext.getSession().getAttribute(SessionName.USER), product, hideBursts);
		
		String styleStr = "";
		if ( style != null && !"".equals( style ) ) {
			styleStr = " style=\"" + style + "\"";
		}
		
		// buf.append("<span" + styleStr + ">");
		if ( !this.disabled && action != null )
			buf.append("<a href=\"" + action + "\"" + styleStr + ">");

		if ( shortenedProductName != null ) {
			if ( showBrandName ) {
				buf.append("<span style=\"" );
				buf.append( brandStyle );
				buf.append( "\">");
				buf.append(brandName);
				buf.append("</span>");
				if(!isNoBreak()) {
					buf.append("<br/>");					
				} else {
					buf.append(" ");
				}
			}
			buf.append(shortenedProductName);
		} else {
			buf.append( fullName != null && !"".equalsIgnoreCase(fullName) ? fullName : "(this product)" );
		}

		if ( !this.disabled && action != null )
			buf.append("</a>");
		if (showNew && pl.isDisplayNew())
			buf.append("&nbsp;&nbsp;<span class=\"text10rbold\">NEW!</span>");
		if (showFavourite && pl.isDisplayFave())
			buf.append("&nbsp;&nbsp;<span class=\"text11prpbold\">YOUR FAVORITE</span>");	
		// buf.append("</span>");

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
