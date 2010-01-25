package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JspMethods;

/**
 * Product Rating Tag
 * 
 * @author cssomogyi
 *
 */
public class ProductRatingTag extends BodyTagSupport {
	private static final long serialVersionUID = -5168098436665976237L;

	ProductModel	product; // product (mandatory)
	String			action; // URL (optional)
	boolean         noBr;
	boolean         leftAlign;
	String			skuCode;

	public void setProduct(ProductModel prd) {
		this.product = prd;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public void setNoBr(boolean noBr) {
		this.noBr = noBr;
	}
	
	public void setLeftAlign(boolean leftAlign) {
		this.leftAlign = leftAlign;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	public int doStartTag() {
		try {
			HttpSession session = pageContext.getSession();
			FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
			
			if ( user == null || !user.isProduceRatingEnabled() ) {
				return SKIP_BODY;
			}
			
			String rating = JspMethods.getProductRating(product, skuCode);
			
			JspWriter out = pageContext.getOut();
			
			StringBuffer buf = new StringBuffer();

			if ( rating != null && rating.trim().length() > 0 ) {
				if (action != null) {
					buf.append("<a href=\"");
					buf.append(action);
					buf.append("\">");
				}
	
				buf.append("<img src=\"/media_stat/images/ratings/"
						+ (leftAlign ? "left_" : "")
						+ rating + ".gif\"");
				
				buf.append(" name=\"" + rating + "\"");
	
				buf.append(" width=\"59\"");
				
				buf.append(" height=\"11\"");
	
				buf.append(" border=\"0\"");
				
				buf.append(">");
				
				if ( action != null ) {
					buf.append("</a>");
				}
				
				if (!noBr)
					buf.append("<br>");
			}
			
			out.println(buf.toString());
		} catch (JspException e) {
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
