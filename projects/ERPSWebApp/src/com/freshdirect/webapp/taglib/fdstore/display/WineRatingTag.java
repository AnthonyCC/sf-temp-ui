package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.EnumWineRating;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupport;

/**
 * Product Rating Tag
 * 
 * @author cssomogyi
 * 
 */
public class WineRatingTag extends BodyTagSupport {
	private static final long serialVersionUID = 7220112513794940563L;

	private ProductModel product; // product (mandatory)
	private String action; // URL (optional)
	private boolean small; // is small (optional)

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel prd) {
		this.product = prd;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isSmall() {
		return small;
	}

	public void setSmall(boolean small) {
		this.small = small;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			EnumWineRating rating;
			boolean half = false;
			try {
				EnumOrderLineRating origRating = product.getProductRatingEnum();
				rating = EnumWineRating.getEnumByRating(origRating);
				if (origRating.getValue() % 2 == 1)
					half = true;
			} catch (FDResourceException e) {
				rating = EnumWineRating.NOT_RATED;
			}

			int starCount = rating.getStarCount();

			if (starCount == 0)
				return SKIP_BODY;

			JspWriter out = pageContext.getOut();

			StringBuffer buf = new StringBuffer();

			if (action != null) {
				buf.append("<a href=\"");
				buf.append(action);
				buf.append("\" class=\"wine-rating\"");
				buf.append(">");
			} else {
				buf.append("<span class=\"wine-rating\">");
			}
			
			String postfix;
			int width;
			int halfWidth;
			int height;
			if (small) {
				postfix = "sm";
				width = 15;
				halfWidth = 8;
				height = 12;
			} else {
				postfix = "lg";
				width = 18;
				halfWidth = 8;
				height = 15;
			}

			for (int i = 0; i < starCount; i++) {
				buf.append("<span class=\"usq-rating-");
				buf.append(postfix);
				buf.append("\"></span>");
			}
			
			if (half) {
				buf.append("<span class=\"usq-rating-half-");
				buf.append(postfix);
				buf.append("\"></span>");
			}

			if (action != null) {
				buf.append("</a>");
			} else {
				buf.append("</span>");
			}

			out.println(buf.toString());
		} catch (IOException e) {
			throw new JspException("I/O error", e);
		}

		return SKIP_BODY;
	}
}
