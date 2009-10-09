package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.ConfigurationUtil;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

public class ProductPriceTag extends BodyTagSupport {
	
	private static final long serialVersionUID = 5175263850081406757L;
	
	private static final String styleRegularOnly = " style=\"font-weight: bold; color: #555555;\"";			// bold, dark grey
	private static final String styleRegularWithScaled = " style=\"font-weight: normal; color: #555555;\"";	// normal, dark grey
	private static final String styleRegularWithWas = " style=\"font-weight: bold; color: #C94747;\"";		// bold, red
	private static final String styleRegularWithBoth = " style=\"font-weight: normal; color: #C94747;\"";	// normal, red
	
	private static final String styleWas = " style=\"font-weight: normal; color: gray;\"";			// normal, light grey
	private static final String styleScale = " style=\"line-height:12px; font-weight: bold; color: #C94747;\"";		// bold, red

	
	private ProductImpression impression;
	double savingsPercentage = 0 ; // savings % off
	boolean showDescription = true; // show configuration/size description
	boolean showAboutPrice = true; //show about price
	
	
	public void setImpression(ProductImpression impression) {
		this.impression = impression;
	}
	
	public void setSavingsPercentage(double savingsPercentage) {
		this.savingsPercentage = savingsPercentage;
	}

	public void setShowDescription( boolean showDescription ) {
		this.showDescription = showDescription;
	}

	public void setShowAboutPrice( boolean showAboutPrice ) {
		this.showAboutPrice = showAboutPrice;
	}
	
	public int doStartTag() {
		JspWriter out = pageContext.getOut();

		StringBuffer buf = new StringBuffer();

		String confDescription = null;

		if (impression instanceof TransactionalProductImpression) {
			TransactionalProductImpression tpi = (TransactionalProductImpression)impression;
			confDescription = ConfigurationUtil.getConfigurationDescription(tpi);
		}

		if (confDescription == null) {
			try {
				confDescription = impression.getProductModel().getSizeDescription();
			} catch (FDResourceException e1) {}
		}
		
		buf.append( "<font class=\"price\">" );

		// display description
		if ( showDescription && confDescription != null ) {
			buf.append("<div>" + confDescription + "</div>\n");
		}

		// display price
		FDProductInfo productInfo = impression.getProductInfo();		
		
		if ( productInfo != null ) {	
			String priceString = impression.getProductModel().getPriceFormatted(savingsPercentage);
			
			String scaleString = impression.getProductModel().getTieredPrice(savingsPercentage);
			
			String wasString = impression.getProductModel().getWasPriceFormatted(savingsPercentage);
			if (wasString != null)
				wasString = "(was " + wasString + ")";

			/* Display Sales Units price-Apple Pricing[AppDev-209].. */
			String aboutPriceString = "";
			
			try {
				aboutPriceString = JspMethods.getAboutPriceForDisplay(productInfo);
			} catch (JspException e) {
			
			}
			
			// style for the real price depends on what kind of deals we have
			String styleRegular;			
			if ( scaleString != null )
				styleRegular = ( wasString != null ) ? styleRegularWithBoth : styleRegularWithScaled;
			else
				styleRegular = ( wasString != null ) ? styleRegularWithWas : styleRegularOnly;
			
						
			// regular price
			buf.append(
					"<div" + styleRegular + ">" +
					priceString + 
					"</div>"
			);
			
			// scaled price
			if ( scaleString != null ){
				if(scaleString.indexOf(" or ") >= -1){
					scaleString = scaleString.replaceFirst(" or ", "<br>or ");
				}
				buf.append(
						"<div" + styleScale + ">" +
						scaleString + 
						"</div>"
				);
			}	
				
			// was price
			if ( wasString != null ) buf.append(
					"<div" + styleWas + ">" + 
					wasString +
					"</div>"
			);
			
			//about price
			if(showAboutPrice){
				if(null != aboutPriceString && !"".equals(aboutPriceString)){
					buf.append(
							"<div class=\"aboutDisplaySalesUnitCat\">about<br>" + 
							aboutPriceString +
							"</div>"
					);
				}
			}
		}

		buf.append( "</font>" );
		
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
