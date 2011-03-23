package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.ConfigurationUtil;
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
	
	private final static Logger LOGGER = LoggerFactory.getInstance( ProductPriceTag.class );
	
	private ProductImpression impression;
	double savingsPercentage = 0 ; // savings % off
	boolean showDescription = true; // show configuration/size description
	boolean showAboutPrice = true; //show about price
	boolean showRegularPrice = true; //show regular pricing
	boolean showWasPrice = true; //show was pricing
	boolean showScalePricing = true; //show scale pricing
	boolean quickShop = false; // special font for quick shop
	boolean grcyProd = false; // special font for grocery product
	String grpDisplayType=null; //change group price display
	boolean showSaveText = false; //show scale pricing
	boolean useTarget = false; //true for quick buy windows
	
	private final static NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
	private final static DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat("0.##");
	
	/**
	 * [APPDEV-1283] Exclude 6 and 12 wine bottles deals
	 */
	private boolean excludeCaseDeals = false;

	
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

	public void setGrpDisplayType(String grpDisplay){
	  this.grpDisplayType=grpDisplay;	
	}
	private static double excludedTiers[] = new double[]{6, 12 };
	
	public void setExcludeCaseDeals(boolean excludeCaseDeals) {
		this.excludeCaseDeals = excludeCaseDeals;
	}

	@Override
	public int doStartTag() {
		StringBuffer buf = new StringBuffer();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String catId = request.getParameter("catId");
		if(!quickShop){
			buf.append( "<span class=\"price\">" );
			buf.append(ProductPriceTag.getHTMLFragment(
				impression,
				savingsPercentage, showDescription, showAboutPrice, showRegularPrice, showWasPrice, showScalePricing,
				quickShop, grcyProd, excludeCaseDeals, grpDisplayType, showSaveText, catId, useTarget
			));
			buf.append("</span>\n");
		}else {
			buf.append(ProductPriceTag.getHTMLFragment(
					impression,
					savingsPercentage, showDescription, showAboutPrice, showRegularPrice, showWasPrice, showScalePricing,
					quickShop, grcyProd, excludeCaseDeals, grpDisplayType, showSaveText, catId, useTarget
				));
			buf.append("\n");
		}

		try {
			// write out
			pageContext.getOut().write(buf.toString());
		} catch (IOException e) {
		}

		return EVAL_BODY_INCLUDE;
	}

	
	/**
	 * Generates price HTML fragment without the outer 'price' frame 
	 * 
	 * @param impression
	 * @param savingsPercentage
	 * @param showDescription
	 * @param showAboutPrice
	 * @param showRegularPrice
	 * @param showWasPrice
	 * @param showScalePricing
	 * @param quickShop
	 * @param grcyProd
	 * @param skuCode
	 * 
	 * @return HTML piece
	 */
	private static String getHTMLFragment(ProductImpression impression, double savingsPercentage, boolean showDescription, boolean showAboutPrice, boolean showRegularPrice, boolean showWasPrice, boolean showScalePricing, boolean quickShop, boolean grcyProd,  
			boolean excludeCaseDeals, String grpDisplayType, boolean showSaveText,String catId, boolean useTarget) {
		StringBuffer buf = new StringBuffer();

		String confDescription = null;

		if (impression instanceof TransactionalProductImpression) {
			TransactionalProductImpression tpi = (TransactionalProductImpression)impression;
			confDescription = ConfigurationUtil.getConfigurationDescription(tpi);
		}
		ProductModel prodModel = impression.getProductModel();
		PriceCalculator priceCalculator = impression.getCalculator();
		
		if (confDescription == null) {
			try {
				confDescription = priceCalculator.getSizeDescription();
			} catch (FDResourceException e1) {}
		}
		
		/// buf.append( "<span class=\"price\">" );

		// display description
		if ( showDescription && confDescription != null ) {
			buf.append("<div>" + confDescription + "</div>\n");
		}
		FDProductInfo productInfo = null;
		try {
		    productInfo = priceCalculator.getProductInfo();
		} catch (FDSkuNotFoundException e) {
                    LOGGER.info("sku not found :" + e, e);
		} catch (FDResourceException e) {
		    LOGGER.info("resource error :" + e, e);
	        }

		// display price
//		if(skuCode != null) {
//			//SkuCode will be not null when coming from product page
//			try{
//				//Load the productInfo based on sku.
//				productInfo = FDCachedFactory.getProductInfo(skuCode);
//			}catch(Exception e){
//				//load default sku 
//				productInfo = impression.getProductInfo();
//			}
//		} else {
//			//SkuCode is null initialize to default sku.
//			productInfo = impression.getProductInfo();
//		}
 		
		if ( productInfo != null ) {
			//SkuCode is null initialize to default skucode.
			String skuCode = productInfo.getSkuCode();
			String priceString = priceCalculator.getPriceFormatted(savingsPercentage);
			String scaleString=null;
			FDGroup group = productInfo.getGroup();
			if(group == null) {
				//Try getting the group from Product Impression which loops through all skus
				//if product has multiple skus. So in case product's non default sku is associated
				//with a group then it will display that group.
				group = impression.getFDGroup();
			}
			if(group != null){
				try {
					MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(group, impression.getPricingZoneId());
					if(matPrice != null) {
						//catId=pr&prodCatId=pr&productId=pr_bartlett&skuCode=FRU0005348&grpId=ORG_PEARS&version=10443&trk=trkCode
						String prodCatId=prodModel.getParentNode().getContentName(); 
                        String productId=prodModel.getContentName();

						if (prodCatId == null) {
							if (prodModel != null && prodModel.getParentNode() instanceof CategoryModel) {
								prodCatId = prodModel.getParentNode().toString();
								LOGGER.debug("prodCatId "+catId);
							}
						}
						StringBuffer buffer = new StringBuffer();
						if(catId!=null && catId.length() > 0 && productId != null && productId.length() > 0 && skuCode != null
								&& skuCode.length() > 0){
							buffer.append("&catId=");
							buffer.append(catId);
							buffer.append("&productId=");
							buffer.append(productId);
							buffer.append("&prodCatId=");
							buffer.append(prodCatId);
							buffer.append("&skuCode=");
							buffer.append(skuCode);
						}
						double displayPrice = 0.0;
						boolean isSaleUnitDiff = false;
						if(matPrice.getPricingUnit().equals(matPrice.getScaleUnit())){
							if(matPrice.getPricingUnit().equals("EA"))
								displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
							else {
									//other than eaches.
									displayPrice = matPrice.getPrice();
									isSaleUnitDiff = true;
							}
						} else {
							displayPrice = matPrice.getPrice();
							isSaleUnitDiff = true;
						}
						GroupScalePricing grpPricing = GroupScaleUtil.lookupGroupPricing(group);
						StringBuffer buf1 = new StringBuffer();
						if(quickShop) buf1.append("SAVE!").append(" ");
						if("LARGE".equalsIgnoreCase(grpDisplayType) ){
							//if(impression.isGroupExists(skuCode)) {
								buf1.append( " <span class=\"text12rbold\">Buy More &amp; Save!</span><br />" );
								buf1.append( "<span class=\"text12bold\" style=\"color:black\">Any "+grpPricing.getLongDesc()+"</span><br />" );
								buf1.append( "<span class=\"text14rbold\">" );
								buf1.append( FORMAT_QUANTITY.format( matPrice.getScaleLowerBound() ) );
								if(matPrice.getScaleUnit().equals("LB"))//Other than eaches append the /pricing unit for clarity.
									buf1.append(matPrice.getScaleUnit().toLowerCase()).append("s");
								buf1.append( " for " );
								buf1.append( FORMAT_CURRENCY.format(displayPrice ) );
								if(isSaleUnitDiff)
									buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</span><br />" );
								if(useTarget) {
									buf1.append( "<a href=\"/group.jsp?grpId="+group.getGroupId()+"&version="+group.getVersion()+buffer.toString()+"\" target=\"_top\">" );
								} else {								
									buf1.append( "<a href=\"/group.jsp?grpId="+group.getGroupId()+"&version="+group.getVersion()+buffer.toString()+"\">" );
								}
								buf1.append( "All " );
								buf1.append( grpPricing.getShortDesc() );
								buf1.append( " - click here" );
								buf1.append( "</a>" );
							//}
						}else if("LARGE_NOLINK".equalsIgnoreCase(grpDisplayType)){ //obsolete, no replacement
							//if(impression.isGroupExists(skuCode)) {
								buf1.append( " <span class=\"titleor14\">SAVE!</span> <span class=\"title14\">" );
								buf1.append( FORMAT_QUANTITY.format( matPrice.getScaleLowerBound() ) );
								if(matPrice.getScaleUnit().equals("LB"))//Other than eaches append the /pricing unit for clarity.
									buf1.append(matPrice.getScaleUnit().toLowerCase()).append("s");
								buf1.append( " for " );
								buf1.append( FORMAT_CURRENCY.format(displayPrice) );
								if(isSaleUnitDiff)
									buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</span>" );
							//}
						}else if("LARGE_RED".equalsIgnoreCase(grpDisplayType)){
							//if(impression.isGroupExists(skuCode)) {
								buf1.append( " <span class=\"text14rbold\">Any " );
								buf1.append( FORMAT_QUANTITY.format( matPrice.getScaleLowerBound() ) );
								if(matPrice.getScaleUnit().equals("LB"))//Other than eaches append the /pricing unit for clarity.
									buf1.append(matPrice.getScaleUnit().toLowerCase()).append("s");;
								buf1.append( " " );
								buf1.append( grpPricing.getShortDesc() );
								buf1.append( " for " );
								buf1.append( FORMAT_CURRENCY.format( displayPrice ) );
								if(isSaleUnitDiff)
									buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</span>" );
							//}
						}else if("SMALL_NOLINK".equalsIgnoreCase(grpDisplayType)){ //obsolete, use small_red
							//if(impression.isGroupExists(skuCode)) {
								buf1.append( " <span class=\"text12orbold\">SAVE!</span> <span class=\"title12\">" );
								buf1.append( FORMAT_QUANTITY.format( matPrice.getScaleLowerBound() ) );
								if(matPrice.getScaleUnit().equals("LB"))//Other than eaches append the /pricing unit for clarity.
									buf1.append(matPrice.getScaleUnit().toLowerCase()).append("s");
								buf1.append( " for " );
								buf1.append( FORMAT_CURRENCY.format( displayPrice) );
								if(isSaleUnitDiff)
									buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</span>" );
							//}
						}else if("SMALL_RED".equalsIgnoreCase(grpDisplayType)){
							//if(impression.isGroupExists(skuCode)) {
								buf1.append( " <span class=\"text10rbold\">Any " );
								buf1.append( FORMAT_QUANTITY.format( matPrice.getScaleLowerBound() ) );
								if(matPrice.getScaleUnit().equals("LB"))//Other than eaches append the /pricing unit for clarity.
									buf1.append(matPrice.getScaleUnit().toLowerCase()).append("s");
								buf1.append( " " );
								buf1.append( grpPricing.getShortDesc() );
								buf1.append( " for " );
								buf1.append( FORMAT_CURRENCY.format(displayPrice) );
								if(isSaleUnitDiff)
									buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</span>" );
							//} 
						}else{
							//default to "SMALL".equalsIgnoreCase(grpDisplayType) a short, linked, description
							if(matPrice.getScaleUnit().equals("LB")) {//Other than eaches append the /pricing unit for clarity.
								if(useTarget) {
									buf1.append("<a href=\"/group.jsp?grpId="+group.getGroupId()+"&version="+group.getVersion()+buffer.toString()+"\" target=\"_top\" class=\"text10rbold\" style=\"color: #CC0000;\">");
								} else {
									buf1.append("<a href=\"/group.jsp?grpId="+group.getGroupId()+"&version="+group.getVersion()+buffer.toString()+"\" class=\"text10rbold\" style=\"color: #CC0000;\">");
								}
								buf1.append( FORMAT_QUANTITY.format( matPrice.getScaleLowerBound() ) );
								buf1.append(matPrice.getScaleUnit().toLowerCase()).append("s");
								buf1.append( " " );
								buf1.append( "of any " );
								buf1.append( " " );
								buf1.append( grpPricing.getShortDesc() );
								buf1.append( " for " );
								buf1.append( FORMAT_CURRENCY.format( displayPrice) );
								buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</a>" );

							} else {
								if(useTarget) {
									buf1.append( "<a href=\"/group.jsp?grpId="+group.getGroupId()+"&version="+group.getVersion()+buffer.toString()+"\" target=\"_top\" class=\"text10rbold\" style=\"color: #CC0000;\">Any " );
								} else {
									buf1.append( "<a href=\"/group.jsp?grpId="+group.getGroupId()+"&version="+group.getVersion()+buffer.toString()+"\" class=\"text10rbold\" style=\"color: #CC0000;\">Any " );
								}
								buf1.append( FORMAT_QUANTITY.format( matPrice.getScaleLowerBound() ) );
								buf1.append( " " );
								buf1.append( grpPricing.getShortDesc() );
								buf1.append( " for " );
								buf1.append( FORMAT_CURRENCY.format( displayPrice) );
								if(isSaleUnitDiff)
									buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</a>" );

							}
						}
						
						scaleString= buf1.toString();
					}
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(scaleString == null) {
				//no group, do the normal scale string fetch
				
				scaleString = priceCalculator.getTieredPrice(savingsPercentage, excludeCaseDeals ? excludedTiers : null);
				if(scaleString != null && showSaveText){
					StringBuffer buffer  = new StringBuffer();
					buffer.append("SAVE");
					if(quickShop){
						int tieredPercentage = impression.getProductModel().getTieredDealPercentage();
						buffer.append(tieredPercentage > 0 ? " " + tieredPercentage + "%" : "!");
						buffer.append("&nbsp;&nbsp;");
					}else {
						buffer.append("!");
						buffer.append("&nbsp;");
					}
					
					//buffer.append(priceCalculator.getTieredPrice(savingsPercentage));
					scaleString = buffer.toString() + scaleString;
				}
				
				
				
				//LOGGER.debug("scaleString: "+scaleString);
			
			}
			String wasString = priceCalculator.getWasPriceFormatted(savingsPercentage);
			
			if (wasString != null)
				wasString = "(was " + wasString + ")";

			/* Display Sales Units price-Apple Pricing[AppDev-209].. */
			String aboutPriceString = "";
			String styleAbout = "";

			aboutPriceString = priceCalculator.getAboutPriceFormatted(0);

            
	        if (null != aboutPriceString && !"".equals(aboutPriceString)) {
	            showRegularPrice = false;
	            showWasPrice = false;
	            showScalePricing = false;

	            styleAbout = (scaleString != null) ? styleAboutScaled : styleAboutOnly;
	        } else {
	            styleAbout = styleAboutOnly;
	        }

			
			// style for the real price depends on what kind of deals we have
			String styleRegular = "";

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
							"<span" + quickShopStyleScale + ">" +
							scaleString + 
							"</span>"
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
							//remove initial "about"
							//"<div" + styleAbout + ">about<br />" + 
							"<div" + styleAbout + ">" + 
							aboutPriceString +
							"</div>"
					);
				}
			}
		}

		/// buf.append( "</span>" );

		return buf.toString();
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}

	public void setShowSaveText(boolean showSaveText) {
		this.showSaveText = showSaveText;
	}
	
	public void setUseTarget(boolean useTarget) {
		this.useTarget = useTarget;
	}
}
