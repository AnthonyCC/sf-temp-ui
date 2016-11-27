package com.freshdirect.webapp.taglib.fdstore.display;

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
import com.freshdirect.fdstore.content.ProductPromoPreviewPriceCalculator;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;

import com.freshdirect.webapp.util.ConfigurationUtil;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

import org.apache.log4j.Logger;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;


public class ProductPriceTag extends BodyTagSupport {
    private static final long serialVersionUID = 5175263850081406757L;

    /* hard-coded styles moved into global.css - batchley 20110812 */
    private static final String styleRegularOnly = " class=\"styleRegularOnly\"";
    private static final String styleRegularWithScaled = " class=\"styleRegularWithScaled\"";
    private static final String styleRegularWithWas = " class=\"styleRegularWithWas\"";
    private static final String styleRegularWithBoth = " class=\"styleRegularWithBoth\"";
    private static final String styleWas = " class=\"styleWas\"";
    private static final String styleWasText = " class=\"styleWas lineThrough\"";
    private static final String styleScale = " class=\"styleScale\"";
    private static final String styleAboutOnly = " class=\"styleAboutOnly\"";
    private static final String styleAboutScaled = " class=\"styleAboutScaled\"";
    private static final String quickShopStyleRegularOnly = " class=\"quickShopStyleRegularOnly\"";
    private static final String quickShopStyleRegularWithScaled = " class=\"quickShopStyleRegularWithScaled\"";
    private static final String quickShopStyleRegularWithWas = " class=\"quickShopStyleRegularWithWas\"";
    private static final String quickShopStyleRegularWithBoth = " class=\"quickShopStyleRegularWithBoth\"";
    private static final String quickShopStyleWas = " class=\"quickShopStyleWas\"";
    private static final String quickShopStyleWasText = " class=\"quickShopStyleWas lineThrough\"";
    private static final String quickShopStyleScale = " class=\"quickShopStyleScale\"";
    private static final String groceryStyleRegularOnly = " class=\"groceryStyleRegularOnly\"";
    private static final String groceryStyleRegularWithScaled = " class=\"groceryStyleRegularWithScaled\"";
    private static final String groceryStyleRegularWithWas = " class=\"groceryStyleRegularWithWas\"";
    private static final String groceryStyleRegularWithBoth = " class=\"groceryStyleRegularWithBoth\"";
    private static final String groceryStyleWas = " class=\"groceryStyleWas\"";
    private static final String groceryStyleWasText = " class=\"groceryStyleWas lineThrough\"";
    private static final String groceryStyleScale = " class=\"groceryStyleScale\"";
    private final static Logger LOGGER = LoggerFactory.getInstance(ProductPriceTag.class);
    public final static NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
    public final static DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat(
            "0.##");
    private ProductImpression impression;
    double savingsPercentage = 0; // savings % off
    boolean showDescription = true; // show configuration/size description
    boolean showAboutPrice = true; //show about price
    boolean showRegularPrice = true; //show regular pricing
    boolean showWasPrice = true; //show was pricing
    boolean showScalePricing = true; //show scale pricing
    boolean quickShop = false; // special font for quick shop
    boolean grcyProd = false; // special font for grocery product
    String grpDisplayType = null; //change group price display
    boolean showSaveText = false; //show scale pricing
    boolean useTarget = false; //true for quick buy windows
    boolean dataDriven = false; //change entire display for data driven
    boolean tableContent = false; //was price should be handled different in tables

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

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    public void setShowAboutPrice(boolean showAboutPrice) {
        this.showAboutPrice = showAboutPrice;
    }

    public void setShowRegularPrice(boolean showRegularPrice) {
        this.showRegularPrice = showRegularPrice;
    }

    public void setShowWasPrice(boolean showWasPrice) {
        this.showWasPrice = showWasPrice;
    }

    public void setShowScalePricing(boolean showScalePricing) {
        this.showScalePricing = showScalePricing;
    }

    public void setQuickShop(boolean quickShop) {
        this.quickShop = quickShop;
    }

    public void setGrpDisplayType(String grpDisplay) {
        this.grpDisplayType = grpDisplay;
    }

    public void setDataDriven(boolean dataDriven) {
        this.dataDriven = dataDriven;
    }

    public void setExcludeCaseDeals(boolean excludeCaseDeals) {
        this.excludeCaseDeals = excludeCaseDeals;
    }

    public void setTableContent(boolean tableContent) {
		this.tableContent = tableContent;
	}

	@Override
    public int doStartTag() {
        StringBuffer buf = new StringBuffer();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String catId = request.getParameter("catId");
        String ppPreviewId = request.getParameter("ppPreviewId");

        if (!quickShop) {
            buf.append("<div class=\"price\" style=\"margin-right: 5px;\">");
            buf.append(ProductPriceTag.getHTMLFragment(impression,
                    savingsPercentage, showDescription, showAboutPrice,
                    showRegularPrice, showWasPrice, showScalePricing,
                    quickShop, grcyProd, excludeCaseDeals, grpDisplayType,
                    showSaveText, catId, useTarget, dataDriven,ppPreviewId, tableContent));
            buf.append("</div>\n");
        } else {
            buf.append(ProductPriceTag.getHTMLFragment(impression,
                    savingsPercentage, showDescription, showAboutPrice,
                    showRegularPrice, showWasPrice, showScalePricing,
                    quickShop, grcyProd, excludeCaseDeals, grpDisplayType,
                    showSaveText, catId, useTarget, dataDriven,ppPreviewId, tableContent));
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
    private static String getHTMLFragment(ProductImpression impression,
        double savingsPercentage, boolean showDescription,
        boolean showAboutPrice, boolean showRegularPrice, boolean showWasPrice,
        boolean showScalePricing, boolean quickShop, boolean grcyProd,
        boolean excludeCaseDeals, String grpDisplayType, boolean showSaveText,
        String catId, boolean useTarget, boolean dataDriven,String ppPreviewId, boolean tableContent) {
        StringBuffer buf = new StringBuffer();

        String confDescription = null;
        
        ProductModel prodModel = impression.getProductModel();
        PriceCalculator priceCalculator = impression.getCalculator();
        if(null!=ppPreviewId){
        	priceCalculator = prodModel.getPriceCalculator();
//        	impression.setCalculator(priceCalculator);
        }

        if (impression instanceof TransactionalProductImpression) {
            TransactionalProductImpression tpi = (TransactionalProductImpression) impression;
            confDescription = ConfigurationUtil.getConfigurationDescription(tpi);
        }

       

        if (confDescription == null) {
            try {
                confDescription = priceCalculator.getSizeDescription();
            } catch (FDResourceException e1) {
            }
        }

        /// buf.append( "<span class=\"price\">" );

        // display description
        if (showDescription && (confDescription != null)) {
            buf.append("<div class=\"confDescription\">" + confDescription +
                "</div>\n");
        }

        FDProductInfo productInfo = null;

        try {
            productInfo = priceCalculator.getProductInfo();
        } catch (FDSkuNotFoundException e) {
            LOGGER.info("sku not found :" + e, e);
        } catch (FDResourceException e) {
            LOGGER.info("resource error :" + e, e);
        }

        if (productInfo != null) {
            //SkuCode is null initialize to default skucode.
            String skuCode = productInfo.getSkuCode();
            String priceString = priceCalculator.getPriceFormatted(savingsPercentage);
            String scaleString = null;
            String salesOrg=priceCalculator.getPricingContext().getZoneInfo().getSalesOrg();
    		String distributionChannel=priceCalculator.getPricingContext().getZoneInfo().getDistributionChanel();
            FDGroup group = productInfo.getGroup(salesOrg,distributionChannel);

            if (group == null) {
                //Try getting the group from Product Impression which loops through all skus
                //if product has multiple skus. So in case product's non default sku is associated
                //with a group then it will display that group.
                group = impression.getFDGroup();
            }

            if (group != null) {
                try {
                    MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(group,
                            impression.getPricingZone());

                    GroupScalePricing gsPricing = GroupScaleUtil.lookupGroupPricing(group);
                    if(null !=gsPricing){
	                    List<String> gsSkus =gsPricing.getSkuList();
	                    List<String> prodSkus = prodModel.getSkuCodes();
	                    //[APPDEV-2269]-If the product is part of the group but the current sku is not part of group scale, then get the actual sku from the product which is part the groupscale.
	                    if(null != gsSkus && !gsSkus.contains(skuCode)){
		                    for (Iterator iterator = prodSkus.iterator(); iterator
									.hasNext();) {
								String sku = (String) iterator.next();
								if(gsSkus.contains(sku)){
									skuCode=sku;
									break;
								}						
							}
	                    }
                    }
                    if (matPrice != null) {
                        //catId=pr&prodCatId=pr&productId=pr_bartlett&skuCode=FRU0005348&grpId=ORG_PEARS&version=10443&trk=trkCode
                        String prodCatId = prodModel.getParentNode()
                                                    .getContentName();
                        String productId = prodModel.getContentName();

                        if (prodCatId == null) {
                            if ((prodModel != null) &&
                                    prodModel.getParentNode() instanceof CategoryModel) {
                                prodCatId = prodModel.getParentNode().toString();
                                LOGGER.debug("prodCatId " + catId);
                            }
                        }

                        if ((catId == null) || (catId.length() == 0)) {
                            catId = prodModel.getParentNode().toString();
                        }

                        StringBuffer buffer = new StringBuffer();

                        if ((catId != null) && (catId.length() > 0) &&
                                (productId != null) &&
                                (productId.length() > 0) && (skuCode != null) &&
                                (skuCode.length() > 0)) {
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

                        if (matPrice.getPricingUnit()
                                        .equals(matPrice.getScaleUnit())) {
                            if (matPrice.getPricingUnit().equals("EA")) {
                                displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
                            } else {
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

                        if (quickShop) {
                            buf1.append("SAVE!").append(" ");
                        }

                        if ("LARGE".equalsIgnoreCase(grpDisplayType)) {
                            //if(impression.isGroupExists(skuCode)) {
                            buf1.append(
                                " <span class=\"text12rbold\">Buy More &amp; Save!</span><br />");
                            buf1.append(
                                "<span class=\"text12bold\" style=\"color:black\">Any " +
                                grpPricing.getLongDesc() + "</span><br />");
                            buf1.append("<span class=\"text14rbold\">");
                            buf1.append(FORMAT_QUANTITY.format(
                                    matPrice.getScaleLowerBound()));

                            if (matPrice.getScaleUnit().equals("LB")) { //Other than eaches append the /pricing unit for clarity.
                                buf1.append(matPrice.getScaleUnit().toLowerCase())
                                    .append("s");
                            }

                            buf1.append(" for ");
                            buf1.append(FORMAT_CURRENCY.format(displayPrice));

                            if (isSaleUnitDiff) {
                                buf1.append("/")
                                    .append(matPrice.getPricingUnit()
                                                    .toLowerCase());
                            }

                            buf1.append("</span><br />");

                            if (useTarget) {
                                buf1.append("<a href=\"/group.jsp?grpId=" +
                                    group.getGroupId() + "&version=" +
                                    group.getVersion() + buffer.toString() +
                                    "\" target=\"_top\">");
                            } else {
                                buf1.append("<a href=\"/group.jsp?grpId=" +
                                    group.getGroupId() + "&version=" +
                                    group.getVersion() + buffer.toString() +
                                    "\">");
                            }

                            buf1.append("All ");
                            buf1.append(grpPricing.getShortDesc());
                            buf1.append(" - click here");
                            buf1.append("</a>");

                            //}
                        } else if ("LARGE_NOLINK".equalsIgnoreCase(
                                    grpDisplayType)) { //obsolete, no replacement
                                                       //if(impression.isGroupExists(skuCode)) {
                            buf1.append(
                                " <span class=\"titleor14\">SAVE!</span> <span class=\"title14\">");
                            buf1.append(FORMAT_QUANTITY.format(
                                    matPrice.getScaleLowerBound()));

                            if (matPrice.getScaleUnit().equals("LB")) { //Other than eaches append the /pricing unit for clarity.
                                buf1.append(matPrice.getScaleUnit().toLowerCase())
                                    .append("s");
                            }

                            buf1.append(" for ");
                            buf1.append(FORMAT_CURRENCY.format(displayPrice));

                            if (isSaleUnitDiff) {
                                buf1.append("/")
                                    .append(matPrice.getPricingUnit()
                                                    .toLowerCase());
                            }

                            buf1.append("</span>");

                            //}
                        } else if ("LARGE_RED".equalsIgnoreCase(grpDisplayType)) {
                            //if(impression.isGroupExists(skuCode)) {
                            buf1.append(" <span class=\"text14rbold\">Any ");
                            buf1.append(FORMAT_QUANTITY.format(
                                    matPrice.getScaleLowerBound()));

                            if (matPrice.getScaleUnit().equals("LB")) { //Other than eaches append the /pricing unit for clarity.
                                buf1.append(matPrice.getScaleUnit().toLowerCase())
                                    .append("s");
                            }

                            ;
                            buf1.append(" ");
                            buf1.append(grpPricing.getShortDesc());
                            buf1.append(" for ");
                            buf1.append(FORMAT_CURRENCY.format(displayPrice));

                            if (isSaleUnitDiff) {
                                buf1.append("/")
                                    .append(matPrice.getPricingUnit()
                                                    .toLowerCase());
                            }

                            buf1.append("</span>");

                            //}
                        } else if ("SMALL_NOLINK".equalsIgnoreCase(
                                    grpDisplayType)) { //obsolete, use small_red
                                                       //if(impression.isGroupExists(skuCode)) {
                            buf1.append(
                                " <span class=\"text12orbold\">SAVE!</span> <span class=\"title12\">");
                            buf1.append(FORMAT_QUANTITY.format(
                                    matPrice.getScaleLowerBound()));

                            if (matPrice.getScaleUnit().equals("LB")) { //Other than eaches append the /pricing unit for clarity.
                                buf1.append(matPrice.getScaleUnit().toLowerCase())
                                    .append("s");
                            }

                            buf1.append(" for ");
                            buf1.append(FORMAT_CURRENCY.format(displayPrice));

                            if (isSaleUnitDiff) {
                                buf1.append("/")
                                    .append(matPrice.getPricingUnit()
                                                    .toLowerCase());
                            }

                            buf1.append("</span>");

                            //}
                        } else if ("SMALL_RED".equalsIgnoreCase(grpDisplayType)) {
                            //if(impression.isGroupExists(skuCode)) {
                            buf1.append(" <span class=\"text10rbold\">Any ");
                            buf1.append(FORMAT_QUANTITY.format(
                                    matPrice.getScaleLowerBound()));

                            if (matPrice.getScaleUnit().equals("LB")) { //Other than eaches append the /pricing unit for clarity.
                                buf1.append(matPrice.getScaleUnit().toLowerCase())
                                    .append("s");
                            }

                            buf1.append(" ");
                            buf1.append(grpPricing.getShortDesc());
                            buf1.append(" for ");
                            buf1.append(FORMAT_CURRENCY.format(displayPrice));

                            if (isSaleUnitDiff) {
                                buf1.append("/")
                                    .append(matPrice.getPricingUnit()
                                                    .toLowerCase());
                            }

                            buf1.append("</span>");

                            //}
                        } else {
                            //we have a group, but no type. check dataDriven, otherwise fall back to default display
                            if (dataDriven) {
                                if ("FEAT".equalsIgnoreCase(grpDisplayType)) {
                                	//TODO: ADD THIS DISPLAY
                                	buf1.append("<span class=\"mixnmatch\">Mix <span class=\"lor\">'n</span><br />Match</span>");
                                }

                                if ("NONFEAT".equalsIgnoreCase(grpDisplayType)) {
                                	//TODO: ADD THIS DISPLAY 
                                	buf1.append("<span class=\"mixnmatch\">Mix <span class=\"lor\">'n</span> Match</span>");
                                }
                            } else {
                                //default to "SMALL".equalsIgnoreCase(grpDisplayType) a short, linked, description
                                if (matPrice.getScaleUnit().equals("LB")) { //Other than eaches append the /pricing unit for clarity.

                                    if (useTarget) {
                                        buf1.append(
                                            "<a href=\"/group.jsp?grpId=" +
                                            group.getGroupId() + "&version=" +
                                            group.getVersion() +
                                            buffer.toString() +
                                            "\" target=\"_top\" class=\"text10rbold\" style=\"color: #CC0000;\">");
                                    } else {
                                        buf1.append(
                                            "<a href=\"/group.jsp?grpId=" +
                                            group.getGroupId() + "&version=" +
                                            group.getVersion() +
                                            buffer.toString() +
                                            "\" class=\"text10rbold\" style=\"color: #CC0000;\">");
                                    }

                                    buf1.append(FORMAT_QUANTITY.format(
                                            matPrice.getScaleLowerBound()));
                                    buf1.append(matPrice.getScaleUnit()
                                                        .toLowerCase())
                                        .append("s");
                                    buf1.append(" ");
                                    buf1.append("of any ");
                                    buf1.append(" ");
                                    buf1.append(grpPricing.getShortDesc());
                                    buf1.append(" for ");
                                    buf1.append(FORMAT_CURRENCY.format(
                                            displayPrice));
                                    buf1.append("/")
                                        .append(matPrice.getPricingUnit()
                                                        .toLowerCase());
                                    buf1.append("</a>");
                                } else {
                                    if (useTarget) {
                                        buf1.append(
                                            "<a href=\"/group.jsp?grpId=" +
                                            group.getGroupId() + "&version=" +
                                            group.getVersion() +
                                            buffer.toString() +
                                            "\" target=\"_top\" class=\"text10rbold\" style=\"color: #CC0000;\">Any ");
                                    } else {
                                        buf1.append(
                                            "<a href=\"/group.jsp?grpId=" +
                                            group.getGroupId() + "&version=" +
                                            group.getVersion() +
                                            buffer.toString() +
                                            "\" class=\"text10rbold\" style=\"color: #CC0000;\">Any ");
                                    }

                                    buf1.append(FORMAT_QUANTITY.format(
                                            matPrice.getScaleLowerBound()));
                                    buf1.append(" ");
                                    buf1.append(grpPricing.getShortDesc());
                                    buf1.append(" for ");
                                    buf1.append(FORMAT_CURRENCY.format(
                                            displayPrice));

                                    if (isSaleUnitDiff) {
                                        buf1.append("/")
                                            .append(matPrice.getPricingUnit()
                                                            .toLowerCase());
                                    }

                                    buf1.append("</a>");
                                }
                            }
                        }

                        scaleString = buf1.toString();
                    }
                } catch (FDResourceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (scaleString == null) {
                //no group, do the normal scale string fetch
                scaleString = priceCalculator.getTieredPrice(savingsPercentage,
                        excludeCaseDeals ? ProductBurstTag.EXCLUDED_WINE_TIERS
                                         : null);

                //n for $#.## does not enter this if
                if ((scaleString != null) && showSaveText) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("SAVE");

                    if (quickShop) {
                        int tieredPercentage = priceCalculator.getTieredDealPercentage();
                        buffer.append((tieredPercentage > 0)
                            ? (" " + tieredPercentage + "%") : "!");
                        buffer.append("&nbsp;&nbsp;");
                    } else {
                        buffer.append("!");
                        buffer.append("&nbsp;");
                    }

                    //buffer.append(priceCalculator.getTieredPrice(savingsPercentage));
                    scaleString = buffer.toString() + scaleString;
                } else if ((scaleString != null) && dataDriven) {
                    //this should be n for $#.##
                    //reuse grpDisplayType
                    if (grpDisplayType != null) {
                    	//this would be the "top" price display in ddpp
                        if ("NONFEAT".equalsIgnoreCase(grpDisplayType)) {
                            scaleString = "<span class=\"lor\">SAVE!</span> " +
                                scaleString.toUpperCase()
                                           .replace("FOR",
                                    "<span class=\"lor\">FOR</span>");
                        } else if ("FEAT".equalsIgnoreCase(grpDisplayType)) {
                            scaleString = scaleString.toUpperCase()
                                                     .replace("FOR",
                                    "<span class=\"lor\">FOR</span><br /><span class=\"featScalePrice\">");
                            scaleString += "</span>";
                        }
                    } else {
                    	//this would be the "bottom" price display in ddpp
                        scaleString = null;
                    }
                }

                //LOGGER.debug("scaleString: "+scaleString);
            }

            String wasString = priceCalculator.getWasPriceFormatted(savingsPercentage);
            
            if (wasString != null) {
                if (dataDriven) { //return savingsPercentage text

                    //reuse grpDisplayType
                    if (grpDisplayType != null) {
                        if ("NONFEAT".equalsIgnoreCase(grpDisplayType)) {
                            wasString = "<span class=\"lor\">SAVE</span> " +
                                priceCalculator.getHighestDealPercentage() +
                                "%";
                        } else if ("FEAT".equalsIgnoreCase(grpDisplayType)) {
                            wasString = "<span class=\"lor\">SAVE</span><br /><span class=\"featSavingsPerc\">" +
                                priceCalculator.getHighestDealPercentage() +
                                "%</span>";
                        }
                    } else {
                    	//return (was $#.##) text
                    	//wasString = " (was " + wasString + ")";
                    }
                } else {
                    //return (was $#.##) text
                    //wasString = " (was " + wasString + ")";
                }
            }

            /* Display Sales Units price-Apple Pricing[AppDev-209].. */
            String aboutPriceString = "";
            String styleAbout = "";

            aboutPriceString = priceCalculator.getAboutPriceFormatted(0);

            if ((null != aboutPriceString) && !"".equals(aboutPriceString)) {
                showRegularPrice = false;
                showWasPrice = false;
                showScalePricing = false;
                styleAbout = (scaleString != null) ? styleAboutScaled
                                                   : styleAboutOnly;
            } else {
                styleAbout = styleAboutOnly;
            }

            // style for the real price depends on what kind of deals we have
            String styleRegular = "";

            if ((scaleString != null) && !quickShop && !grcyProd) {
            	
                styleRegular = (wasString != null) ? styleRegularWithBoth : styleRegularWithScaled;
                
            } else if (!quickShop && !grcyProd) {
            	
                styleRegular = (wasString != null) ? styleRegularWithWas : styleRegularOnly;
                
            } else if ((scaleString != null) && quickShop) {
            	
                styleRegular = (wasString != null) ? quickShopStyleRegularWithBoth : quickShopStyleRegularWithScaled;
                
            } else if ((scaleString != null) && grcyProd) {
            	
                styleRegular = (wasString != null) ? groceryStyleRegularWithBoth : groceryStyleRegularWithScaled;
                
            } else if ((scaleString == null) && grcyProd) {
            	
                styleRegular = (wasString != null) ? groceryStyleRegularWithWas : groceryStyleRegularOnly;
                
            } else {
            	
                styleRegular = (wasString != null) ? quickShopStyleRegularWithWas : quickShopStyleRegularOnly;
                
            }
            
            String wasClass = "";
            String wasTextStyle = "";
            if (quickShop) {
            	wasClass = quickShopStyleWas;
            	wasTextStyle = quickShopStyleWasText;
            } else if (grcyProd) {
            	wasClass = groceryStyleWas;
            	wasTextStyle = groceryStyleWasText;
            } else {
            	wasClass = styleWas;
            	wasTextStyle = styleWasText;
            }

            boolean wasUsed = false;
            // regular price
        	/* we need this for DDPP, NOT in TOP_TEXT */
            if (showRegularPrice || (dataDriven && grpDisplayType == null) ) {
            	
            	if (showWasPrice && wasString != null && !grcyProd && !quickShop && !tableContent){
            		buf.append("<div" + styleRegular + ">" + priceString + "<font" + wasClass + "> (<font" + wasTextStyle +">" + wasString + "</font>)</font></div> "); 
            		wasUsed = true;
            	} else {
            		buf.append("<div" + styleRegular + ">" + priceString + "</div> ");            		
            	}
            	
            }

            
            // scaled price
            if ((scaleString != null) && showScalePricing) {
            	
                if (scaleString.indexOf(" or ") >= -1) {
                    scaleString = scaleString.replaceFirst(" or ", "<br />or ");
                }

                if (quickShop) {
                    buf.append("<span" + quickShopStyleScale + ">" + scaleString + "</span>");
                } else if (grcyProd) {
                    buf.append("<div" + groceryStyleScale + ">" + scaleString + "</div>");
                } else {
                	//if we have scale pricing AND promo price, in ddpp we don't want BOTH to display
                	if (!dataDriven || (dataDriven && (wasString == null || "".equals(wasString)))) {
                		buf.append("<div" + styleScale + ">" + scaleString + "</div>");
                	}
                }
            }

            // was price
            if (showWasPrice && (wasString != null) && !wasUsed) {
            	buf.append("<div" + wasClass + ">(<font" + wasTextStyle +">" + wasString + "</font>)</div>");
            }

            //about price
            if (showAboutPrice) {
                if ((null != aboutPriceString) && !"".equals(aboutPriceString)) {
                    //check here if we have a was price - fix for Defect # 409
                    if ((wasString != null) && (wasString != "")) {
                        styleAbout = styleRegularWithWas;
                    }

                    buf.append(//remove initial "about"
                    //"<div" + styleAbout + ">about<br />" + 
                    "<div" + styleAbout + ">" + aboutPriceString + "</div>");
                }
            }
        }

        /// buf.append( "</span>" );
        return buf.toString();
    }

    public void setShowSaveText(boolean showSaveText) {
        this.showSaveText = showSaveText;
    }

    public void setUseTarget(boolean useTarget) {
        this.useTarget = useTarget;
    }

    public static class TagEI extends TagExtraInfo {
        public VariableInfo[] getVariableInfo(TagData data) {
            return new VariableInfo[] {  };
        }
    }
}
