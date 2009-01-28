package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.DYFUtil;

/**
 * Product Image Tag
 * 
 * @author segabor
 *
 */
public class ProductImageTag extends BodyTagSupport {
	private static final long serialVersionUID = 8159061278833068855L;

	ProductModel	product; // product (mandatory)
	String			style; // CSS style modification (optional)
	String			className;	// CSS class name (optional)
	String			action; // URL (optional)
	String			prefix; // For internal use only! (optional)
	boolean			hideDeals = false; // whether display Deals burst (optional)
	boolean			hideNew = false; // whether display New Product burst (optional)
	boolean			hideYourFave = false; // whether display Your Fave burst (optional)
	boolean			hideBurst = false; // whether display any burst (optional)

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
	
	public void setHideDeals(boolean hideDeals) {
		this.hideDeals = hideDeals;
	}

	public void setHideNew(boolean hideNew) {
		this.hideNew = hideNew;
	}

	public void setHideBurst(boolean hideBurst) {
		this.hideBurst = hideBurst;
	}

	public void setHideYourFave(boolean hideYourFave) {
		this.hideYourFave = hideYourFave;
	}

	public int doStartTag() {
		try {
			Image prodImg = product.getProdImage();
			
			JspWriter out = pageContext.getOut();
			
			StringBuffer buf = new StringBuffer();

			int deal = !hideBurst && !hideDeals ? product.getDealPercentage() : 0;
			boolean isNew = !hideBurst && !hideNew && product.isNew();
			boolean isYourFave = !hideBurst && !hideYourFave && DYFUtil.isFavorite(product,
					(FDUserI) pageContext.getSession().getAttribute(SessionName.USER));
			deal = 0; isNew = false; isYourFave = false; // REMOVE IT AFTER TESTING !!!!
			
			if (deal > 0 || isNew || isYourFave) {
				buf.append("<div style=\"padding: 0px; border: 0px; "
						+ "width: " + prodImg.getWidth() + "px; "
						+ "height: " + prodImg.getHeight() + "px; "
						+ "position: relative;\">");
				buf.append("<div style=\"position: absolute; top: 0px; left: 0px\">");
			
				if (action != null) {
					buf.append("<a href=\"");
					buf.append(action);
					buf.append("\">");
				}
	
				if (deal > 0) {
					buf.append("<img alt=\"SAVE " + deal + "\" src=\"/media_stat/images/deals/brst_sm_" + deal + ".gif\" style=\"border: 0px;\">");
				} else if (isYourFave) {
					buf.append("<img alt=\"NEW\" src=\"/media_stat/images/template/search/brst_sm_fave.png\" style=\"border: 0px;\">");
				} else if (isNew) {
					buf.append("<img alt=\"NEW\" src=\"/media_stat/images/template/search/brst_sm_new.png\" style=\"border: 0px;\">");
				}
				
				if (action != null) {
					buf.append("</a>");
				}
	
				buf.append("</div>");
			}

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
			
			if (deal > 0 || isNew || isYourFave) {
				buf.append("</div>");
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
