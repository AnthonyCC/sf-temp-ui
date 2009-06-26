package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDProduct;
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
	private static final String styleScale = " style=\"font-weight: bold; color: #C94747;\"";		// bold, red

	
	private ProductImpression impression;
	double savingsPercentage = 0 ; // savings % off
	boolean showDescription = true; // show configuration/size description
	
	
	public void setImpression(ProductImpression impression) {
		this.impression = impression;
	}
	
	public void setSavingsPercentage(double savingsPercentage) {
		this.savingsPercentage = savingsPercentage;
	}

	public void setShowDescription( boolean showDescription ) {
		this.showDescription = showDescription;
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
			
			double price = 0;
			double wasPrice = 0;
			
			if ( savingsPercentage > 0 ) {
				price = productInfo.getDefaultPrice() * (1-savingsPercentage);
				wasPrice = productInfo.getDefaultPrice();
			} else if ( productInfo.isDeal() ) {
				price = productInfo.getDefaultPrice();
				wasPrice = productInfo.getBasePrice();
			} else {
				price = productInfo.getDefaultPrice();
			}
			
			String priceString = JspMethods.currencyFormatter.format( price ) + "/" +	
								productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
			
			String scaleString = getScaleDisplay();
			
			String wasString = "(was " + JspMethods.currencyFormatter.format( wasPrice ) + ")";
 
			// style for the real price depends on what kind of deals we have
			String styleRegular;			
			if ( scaleString != null )
				styleRegular = ( wasPrice > 0 ) ? styleRegularWithBoth : styleRegularWithScaled;
			else
				styleRegular = ( wasPrice > 0 ) ? styleRegularWithWas : styleRegularOnly;
			
						
			// regular price
			buf.append(
					"<div" + styleRegular + ">" +
					priceString + 
					"</div>"
			);
			
			// scaled price
			if ( scaleString != null ) buf.append(
					"<div" + styleScale + ">" +
					scaleString + 
					"</div>"
			);
				
			// was price
			if ( wasPrice > 0 ) buf.append(
					"<div" + styleWas + ">" + 
					wasString +
					"</div>"
			);
			
		}	// TODO what if productInfo was null?		

		buf.append( "</font>" );
		
		try {
			// write out
			out.write(buf.toString());
		} catch (IOException e) {
		}

		return EVAL_BODY_INCLUDE;
	}

	private String getScaleDisplay() {
		
		FDProduct product = impression.getFDProduct();
		if ( product != null ) {
			String[] ymalScales = null;
			
			if ( savingsPercentage > 0 )
				ymalScales = product.getPricing().getScaleDisplay( savingsPercentage );
			else
				ymalScales = product.getPricing().getScaleDisplay();			
			
			if ( ymalScales.length > 0 ) {
				return ymalScales[ ymalScales.length-1 ];	//TODO which one is the largest?? first or last? 
			}
		}
		return null;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}
