package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
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
	private static final String styleAboutOnly = " style=\"padding-top:2px; line-height:12px; font-size: 11px; font-weight: bold; font-family: Tahoma, Arial, sans-serif; color: #6B6B6B;\"";		// bold, red
	private static final String styleAboutScaled = " style=\"padding-top:4px; line-height:12px; font-weight: bold; color: #C94747;\"";		// bold, red
	
	private static final String quickShopStyleRegularOnly = " style=\"line-height:16px; font-size: 11px; font-weight: bold; font-family: Verdana, Arial, sans-serif; color: #555555;\"";			// bold, dark grey
	private static final String quickShopStyleRegularWithScaled = " style=\"line-height:16px; font-size: 11px; font-weight: bold; font-family: Verdana, Arial, sans-serif; color: #555555;\"";	// normal, dark grey
	private static final String quickShopStyleRegularWithWas = " style=\"line-height:16px; font-size: 11px; font-weight: bold; font-family: Verdana, Arial, sans-serif; color: #C94747;\"";		// bold, red
	private static final String quickShopStyleRegularWithBoth = " style=\"line-height:16px; font-size: 11px; font-weight: bold; font-family: Verdana, Arial, sans-serif; color: #C94747;\"";	// normal, red
	
	private static final String quickShopStyleWas = " style=\"font-weight: normal; color: gray;\"";			// normal, light grey
	private static final String quickShopStyleScale = " style=\"line-height:16px; font-size: 11px; font-weight: bold; color: #C94747; font-family: Verdana, Arial, sans-serif;\"";		// bold, red
	
	private static final String groceryStyleRegularOnly = " style=\"line-height:16px; font-size: 13px; font-weight: bold; font-family: Verdana, Arial, sans-serif; color: #555555;\"";			// bold, dark grey
	private static final String groceryStyleRegularWithScaled = " style=\"line-height:16px; font-size: 13px; font-weight: bold; font-family: Verdana, Arial, sans-serif; color: #555555;\"";	// normal, dark grey
	private static final String groceryStyleRegularWithWas = " style=\"line-height:16px; font-size: 13px; font-weight: bold; font-family: Verdana, Arial, sans-serif; color: #C94747;\"";		// bold, red
	private static final String groceryStyleRegularWithBoth = " style=\"line-height:16px; font-size: 13px; font-weight: bold; font-family: Verdana, Arial, sans-serif; color: #C94747;\"";	// normal, red
	
	private static final String groceryStyleWas = " style=\"font-weight: normal; color: gray;\"";			// normal, light grey
	private static final String groceryStyleScale = " style=\"line-height:16px; font-size: 13px; font-weight: bold; color: #C94747; font-family: Verdana, Arial, sans-serif;\"";		// bold, red
	
	private final static Category LOGGER = LoggerFactory.getInstance("ProductPriceTag.java");
	
	private ProductImpression impression;
	double savingsPercentage = 0 ; // savings % off
	boolean showDescription = true; // show configuration/size description
	boolean showAboutPrice = true; //show about price
	boolean showRegularPrice = true; //show regular pricing
	boolean showWasPrice = true; //show was pricing
	boolean showScalePricing = true; //show scale pricing
	boolean quickShop = false; // special font for quick shop
	boolean grcyProd = false; // special font for grocery product
	
	public void setGrcyProd(boolean grcyProd) {
		this.grcyProd = grcyProd;
	}

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
	
	public void setShowRegularPrice( boolean showRegularPrice ) {
		this.showRegularPrice = showRegularPrice;
	}
	
	public void setShowWasPrice( boolean showWasPrice ) {
		this.showWasPrice = showWasPrice;
	}
	
	public void setShowScalePricing( boolean showScalePricing ) {
		this.showScalePricing = showScalePricing;
	}
	
	public void setQuickShop(boolean quickShop) {
		this.quickShop = quickShop;
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
		//LOGGER.debug("productInfo: "+productInfo);		
		
		if ( productInfo != null ) {	
			String priceString = impression.getProductModel().getPriceFormatted(savingsPercentage);
			//LOGGER.debug("priceString: "+priceString);
			
			String scaleString = impression.getProductModel().getTieredPrice(savingsPercentage);
			//LOGGER.debug("scaleString: "+scaleString);
			
			String wasString = impression.getProductModel().getWasPriceFormatted(savingsPercentage);
			//LOGGER.debug("wasString: "+wasString);
			
			if (wasString != null)
				wasString = "(was " + wasString + ")";

			/* Display Sales Units price-Apple Pricing[AppDev-209].. */
			String aboutPriceString = "";
			String styleAbout = "";
			//LOGGER.debug("styleAbout before aboutPriceString: |"+styleAbout+"|");
			
			try {
				aboutPriceString = JspMethods.getAboutPriceForDisplay(productInfo);
				//LOGGER.debug("showWasPrice: |"+showWasPrice+"|");
				//LOGGER.debug("aboutPriceString: |"+aboutPriceString+"|");
				
				if(null != aboutPriceString && !"".equals(aboutPriceString)){
					showRegularPrice = false; 
					showWasPrice = false; 
					showScalePricing = false; 
					styleAbout = (scaleString!=null) ?  styleAboutScaled : styleAboutOnly;
				}else{
					styleAbout = styleAboutOnly;
				}
			} catch (JspException e) {
			
			}
			//LOGGER.debug("styleAbout after aboutPriceString: |"+styleAbout+"|");
			
			// style for the real price depends on what kind of deals we have
			String styleRegular = "";
			//LOGGER.debug("styleRegular before : |"+styleRegular+"|");			
			if ( scaleString != null && !quickShop && !grcyProd)
				styleRegular = ( wasString != null ) ? styleRegularWithBoth : styleRegularWithScaled;
			else if (!quickShop && !grcyProd)
				styleRegular = ( wasString != null ) ? styleRegularWithWas : styleRegularOnly;
			else if (scaleString != null && quickShop) 
				styleRegular = ( wasString != null ) ? quickShopStyleRegularWithBoth : quickShopStyleRegularWithScaled;
			else if (scaleString != null && grcyProd) 
				styleRegular = ( wasString != null ) ? groceryStyleRegularWithBoth : groceryStyleRegularWithScaled;
			else if (scaleString == null && grcyProd) 
				styleRegular = ( wasString != null ) ? groceryStyleRegularWithWas : groceryStyleRegularOnly;
			else 
				styleRegular = ( wasString != null ) ? quickShopStyleRegularWithWas : quickShopStyleRegularOnly;

			//LOGGER.debug("styleRegular after : |"+styleRegular+"|");	
			
						
			// regular price
			if(showRegularPrice) {
				buf.append(
						"<div" + styleRegular + ">" +
						priceString + 
						"</div>"
				);
			}
			
			// scaled price
			if ( scaleString != null && showScalePricing){
				if(scaleString.indexOf(" or ") >= -1){
					scaleString = scaleString.replaceFirst(" or ", "<br />or ");
				}
				if(quickShop) {
					buf.append(
							"<div" + quickShopStyleScale + ">" +
							scaleString + 
							"</div>"
					);
				}else if(grcyProd) {
					buf.append(
							"<div" + groceryStyleScale + ">" +
							scaleString + 
							"</div>"
					);
				}
				else {
					buf.append(
							"<div" + styleScale + ">" +
							scaleString + 
							"</div>"
					);
				}
			}	
				
			// was price
			if ( showWasPrice && wasString != null ) 
				if(quickShop) {
					buf.append(
						"<div" + quickShopStyleWas + ">" + 
						wasString +
						"</div>"
					);
				} else if(grcyProd) {
					buf.append(
							"<div" + groceryStyleWas + ">" + 
							wasString +
							"</div>"
						);
				} else {
					buf.append(
							"<div" + styleWas + ">" + 
							wasString +
							"</div>"
						);
				}
			
			//about price
			if(showAboutPrice){
				if(null != aboutPriceString && !"".equals(aboutPriceString)){
					//check here if we have a was price - fix for Defect # 409
					if (wasString!=null && wasString!="") { styleAbout = styleRegularWithWas; }
					
					buf.append(
							"<div" + styleAbout + ">about<br />" + 
							aboutPriceString +
							"</div>"
					);
				}
			}
		}

		//LOGGER.debug("end of product price tag");		

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
