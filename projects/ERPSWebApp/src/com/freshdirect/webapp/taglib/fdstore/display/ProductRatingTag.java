package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.EnumWineRating;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsContext;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsDTO;
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
	
	public static enum RatingEnum {
		WINE,
		CUSTOMER,
		SUSTAINABILITY,
		EXPERT,
		ALL
	}
	
	
	private static final Logger LOGGER = LoggerFactory.getInstance( ProductRatingTag.class );

	private static final long serialVersionUID = -5168098436665976237L;

	ProductModel product; // product (mandatory)
	String action; // URL (optional)
	boolean noBr;
	boolean leftAlign;
	String skuCode;
	CustomerRatingsDTO customerRatingsDTO;
	
	RatingEnum showOnly = RatingEnum.ALL;
	

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
	
	public void setShowOnly(RatingEnum showOnly) {
		this.showOnly = showOnly;
	}
	
	public RatingEnum getShowOnly() {
		return showOnly;
	}
	

	@Override
	public int doStartTag() throws JspException {
		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
		if (availability != null && !availability.isFullyAvailable())
			return SKIP_BODY;

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

		if (JspMethods.getWineAssociateId().toLowerCase().equalsIgnoreCase(deptName)) {
			// [A] WINE RATINGS

			if(showOnly == RatingEnum.ALL || showOnly == RatingEnum.WINE) {
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
				
				String postfix = (small) ? "sm" : "lg";

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
			}
		} else if ((customerRatingsDTO =  CustomerRatingsContext.getInstance().getCustomerRatingByProductId(product.getContentKey().getId())) != null) {
			
			if(showOnly == RatingEnum.ALL || showOnly == RatingEnum.CUSTOMER ) {
				// CUSTOMAR RATINGS
				// steal logic from WineRatingTag
				boolean small = true;
				
				// stolen code starts here >>>>

				BigDecimal averageRating = customerRatingsDTO.getAverageOverallRating();
				int starCount = (int)Math.ceil(averageRating.doubleValue());
				int reviewCount = customerRatingsDTO.getTotalReviewCount();

				if (starCount == 0)
					return SKIP_BODY;

				StringBuilder buf = new StringBuilder();


				if (noBr) {
					buf.append("<span class=\"cust-rating hastooltip\" >");
				} else {
					buf.append("<div class=\"cust-rating hastooltip\" >");
				}			
				

				for (int i = 0; i < starCount; i++) {
					buf.append("<span class=\"bv-cust-rating-");
					buf.append((small) ? "sm" : "lg");
					buf.append("\"></span>");
				}
				

				if (noBr)
					buf.append("</span>");
				else
					buf.append("</div>");

				buf.append("<div id=\"" + product.getContentKey().getId() + "_hover\" class=\"cust-rating-hover tooltipcontent\">");
				buf.append("<b class=\"cust-rating-hover-rating\">" + averageRating + "</b><br>");
				buf.append("based on <b style=\"font-size:13px;\">" + reviewCount + "</b> customer reviews");
				buf.append("</div>");
				// <<<<<< stolen code ends here

				try {
					pageContext.getOut().println(buf.toString());
				} catch (IOException e) {
					throw new JspException(e);
				}		
				
			}
			
		} else {
			// [C] SEAFOOD SUSTAINABILITY RATING
			// [B] STANDARD PRODUCE RATING
			
			if (!user.isProduceRatingEnabled() && !FDStoreProperties.isSeafoodSustainEnabled()) {
				return SKIP_BODY;
			}

			
			// [C] SEAFOOD SUSTAINABILITY RATING
			if(showOnly == RatingEnum.ALL || showOnly == RatingEnum.SUSTAINABILITY) {
				if (FDStoreProperties.isSeafoodSustainEnabled()) {
					String ratingSS = JspMethods.getSustainabilityRating(product, skuCode);

					StringBuilder bufSS = new StringBuilder();

					if (ratingSS != null && ratingSS.trim().length() > 0) {
						if (action != null) {
							bufSS.append("<a href=\"");
							bufSS.append(action);
							bufSS.append("\">");
						}
		
						bufSS.append("<img src=\"/media_stat/images/ratings/"
								+ "fish_" + ratingSS + ".gif\"");
		
						bufSS.append(" name=\"ss_rating_" + ratingSS + "\"");

						bufSS.append(" width=\"35\"");

						bufSS.append(" height=\"15\"");

						bufSS.append(" border=\"0\"");
						
						bufSS.append(" alt=\"ss_rating_" + ratingSS + "\"");

						bufSS.append(" />");
		
						if (action != null) {
							bufSS.append("</a>");
						}
		
						if (!noBr)
							bufSS.append("<br>");
					}
		
					try {
						pageContext.getOut().println(bufSS.toString());
					} catch (IOException e) {
						throw new JspException(e);
					}
				
				}else{
					LOGGER.error("fdstore.seafoodsustain.enabled=false! Skipping ...");
				}
				
			}
			
			// [B] STANDARD PRODUCE RATING
			if(showOnly == RatingEnum.ALL || showOnly == RatingEnum.EXPERT) {
				if (user.isProduceRatingEnabled()) {
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
				}else{
					LOGGER.error("user.isProduceRatingEnabled()=false! Skipping ...");
				}				
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
