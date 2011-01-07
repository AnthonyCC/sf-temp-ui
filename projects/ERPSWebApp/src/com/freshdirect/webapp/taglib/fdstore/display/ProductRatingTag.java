package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.EnumWineRating;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getInstance( ProductRatingTag.class );

	private static final long serialVersionUID = -5168098436665976237L;

	ProductModel product; // product (mandatory)
	String action; // URL (optional)
	boolean noBr;
	boolean leftAlign;
	String skuCode;

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

	@Override
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		if (user == null) {
			LOGGER.error("User is null! Skipping ...");
			return SKIP_BODY;
		}

		if (product == null) {
			LOGGER.error("Product is null! Skipping ...");
			return SKIP_BODY;
		}

		if (product.getDepartment() == null) {
			LOGGER.warn("Product "+ product.getContentName() +" has no department!");
		}


		final String deptName = product.getDepartment() != null ?
				product.getDepartment().getContentName() : "";

		if ("usq".equalsIgnoreCase(deptName)) {
			// [A] WINE RATINGS
			if (!product.isShowWineRatings()) {
				return SKIP_BODY;
			}

			// steal logic from WineRatingTag
			boolean small = true;
			
			// stolen code starts here >>>>
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

			StringBuilder buf = new StringBuilder();

			if (action != null) {
				buf.append("<a href=\"");
				buf.append(action);
				buf.append("\" class=\"wine-rating\"");
				buf.append(">");
			} else {
				if (noBr)
					buf.append("<span class=\"wine-rating\">");
				else
					buf.append("<div class=\"wine-rating\">");
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
				if (noBr)
					buf.append("</span>");
				else
					buf.append("</div>");
			}
			// <<<<<< stolen code ends here

			try {
				pageContext.getOut().println(buf.toString());
			} catch (IOException e) {
				throw new JspException(e);
			}
		} else {
			// [B] STANDARD PRODUCE RATIN
			
			if (!user.isProduceRatingEnabled()) {
				return SKIP_BODY;
			}

			String rating = JspMethods.getProductRating(product, skuCode);

			StringBuilder buf = new StringBuilder();

			if (rating != null && rating.trim().length() > 0) {
				if (action != null) {
					buf.append("<a href=\"");
					buf.append(action);
					buf.append("\">");
				}

				buf.append("<img src=\"/media_stat/images/ratings/"
						+ (leftAlign ? "left_" : "") + rating + ".gif\"");

				buf.append(" name=\"" + rating + "\"");

				buf.append(" width=\"59\"");

				buf.append(" height=\"11\"");

				buf.append(" border=\"0\"");

				buf.append(">");

				if (action != null) {
					buf.append("</a>");
				}

				if (!noBr)
					buf.append("<br>");
			}

			try {
				pageContext.getOut().println(buf.toString());
			} catch (IOException e) {
				throw new JspException(e);
			}
		}


		return EVAL_BODY_INCLUDE;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}
