package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.ProductImpression;

/**
 * RatingPlusScalePriceTag extends ProductRatingTag.
 * It is primarily used for QuickShop
 * special font requirements that the ProductPriceTag does not handle.
 * This class will display the ProductRating plus ScalePrice on same line with 
 * specified font.
 * 
 * @author asexton
 *
 */
public class RatingPlusScalePriceTag extends ProductRatingTag {
	private static final long serialVersionUID = 1814071642083139881L;

	private static final String quickShopStyleScale = " style=\"line-height:16px; font-size: 11px; font-weight: bold; color: #C94747; font-family: Verdana, Arial, sans-serif;\"";		// bold, red
	
	double savingsPercentage = 0 ; // savings % off
		
	public void setSavingsPercentage(double savingsPercentage) {
		this.savingsPercentage = savingsPercentage;
	}
	
	public int doStartTag() {
		try {
			HttpSession session = pageContext.getSession();
			FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
			
			if ( user == null || !user.isProduceRatingEnabled() || product == null ) {
				return SKIP_BODY;
			}
			
			ProductImpression impression = new ProductImpression(ProductPricingFactory.getInstance().getPricingAdapter(product, user.getPricingContext()));			
			
			String scaleString = impression.getCalculator().getTieredPrice(savingsPercentage);
			
			int tieredPercentage = impression.getCalculator().getTieredDealPercentage();
			
			// TODO include this calculation into the material / tiered pricing model
			if (savingsPercentage > 0.0) {
				tieredPercentage = (int) (((double) tieredPercentage) / (1.0 - savingsPercentage));
			}
			
			String rating = JspMethods.getProductRating(product);
			
			JspWriter out = pageContext.getOut();
			
			StringBuffer buf = new StringBuffer();

			if ( rating != null && rating.trim().length() > 0 ) {
				if (action != null) {
					buf.append("<div" + quickShopStyleScale + ">");
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
				
				if ( scaleString != null ) {
					buf.append("&nbsp;&nbsp;" + "SAVE" + (tieredPercentage > 0 ? " " + tieredPercentage + "%" : "!") + "&nbsp;&nbsp;" + scaleString);
				}
				buf.append( "</div></font>" );
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

