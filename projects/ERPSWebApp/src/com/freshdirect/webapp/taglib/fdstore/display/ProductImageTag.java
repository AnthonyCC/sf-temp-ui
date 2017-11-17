package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Category;

import com.freshdirect.WineUtil;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.EnumBurstType;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.util.ProductLabeling;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.BrowserInfo;
import com.freshdirect.webapp.taglib.fdstore.SessionName;



/**
 * Product Image Tag
 *
 * @author segabor
 *
 */
public class ProductImageTag extends BodyTagSupport {
    private static final long serialVersionUID = 8159061278833068855L;
    private static Category		LOGGER				= LoggerFactory.getInstance( ProductImageTag.class );
    public static final String imageWidthVariableName = "productImageWidth";
    public static final String imageHeightVariableName = "productImageHeight";
    ProductModel product; // product (mandatory if calculator is null)
    PriceCalculator calculator; // calculator (mandatory if product is null)
    String style; // CSS style modification (optional)
    String className; // CSS class name (optional)
    String action; // URL (optional)
    boolean disabled = false; // Image is not clickable
    String prefix; // For internal use only! (optional)
    BrowserInfo browserInfo = null;
    double savingsPercentage = 0; // savings % off
    boolean isInCart = false; // display savings - in cart
    boolean showRolloverImage = true; // rollover image
    boolean useAlternateImage = false; // alternate image
    boolean enableQuickBuy = false; // [APPDEV-672] Quick Buy button (optional)
    String quickBuyImage = "/media_stat/images/quickbuy/quickbuy_button_hover.gif"; // quick buy button url 
    String webId = null; // DOM id of generated tags (optional)
    double opacity = 1; // 1-transparency
    boolean isNewProductPage = false;
    FDCustomerCoupon coupon = null; //optional customer coupon to use for badge/logo

    public FDCustomerCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(FDCustomerCoupon coupon) {
		this.coupon = coupon;
	}

	/**
    * [APPDEV-1283] Exclude 6 and 12 bottle deals
    */
    private boolean excludeCaseDeals = false;
    int height = -1; // negative height means height is calculated based on img height
    Set<EnumBurstType> hideBursts;

    /*
     * specifically over-ride the prod image type to show. use the CMS attribute name as the value
     */
    String prodImageType = null;

    /* dynamically size image to a container element's size by specifying container's size.
     *        if this is not passed in, uses the image's actual sizes
     *
     * pass in a String in the form of h=#,w=#
     * either value is optional
     */
    String bindToContainerSize = null;
    private HashMap<String, Integer> bindToContainerSizes = new HashMap<String, Integer>();

    /* allow resizable bursts */
    String burstOptions = null;
    private HashMap<String, String> burstOptionVals = new HashMap<String, String>();

    public void setBurstOptions(String burstOptions) {
        this.burstOptions = burstOptions;
    }

    public void setProduct(ProductModel prd) {
        this.product = prd;
    }

    public void setPriceCalculator(PriceCalculator calculator) {
        this.calculator = calculator;
    }

    public void setNewProductPage(boolean isNewProductPage) {
        this.isNewProductPage = isNewProductPage;
    }

    public void setStyle(String text) {
        this.style = text;
    }

    public void setClassName(String name) {
        this.className = name;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDisabled(boolean flag) {
        this.disabled = flag;
    }

    public void setPrefix(String uriPrefix) {
        this.prefix = uriPrefix;
    }

    public void setExcludeCaseDeals(boolean excludeCaseDeals) {
        this.excludeCaseDeals = excludeCaseDeals;
    }

    public void setBrowserInfo(BrowserInfo browserInfo) {
        this.browserInfo = browserInfo;
    }

    public void setInCart(boolean isInCart) {
        this.isInCart = isInCart;
    }

    public void setSavingsPercentage(double savingsPercentage) {
        this.savingsPercentage = savingsPercentage;
    }

    public void setShowRolloverImage(boolean showRolloverImage) {
        this.showRolloverImage = showRolloverImage;
    }

    public void setUseAlternateImage(boolean useAlternateImage) {
        this.useAlternateImage = useAlternateImage;
    }

    public void setEnableQuickBuy(boolean enableQuickBuy) {
        this.enableQuickBuy = enableQuickBuy;
    }

    public void setQuickBuyImage(String text) {
        this.quickBuyImage = text;
    }

    public void setOpacity(double opacity) {
        if (opacity < 0) {
            this.opacity = 0;
        } else if (opacity > 1) {
            this.opacity = 1;
        } else {
            this.opacity = opacity;
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHideBursts(Set<EnumBurstType> hideBursts) {
        this.hideBursts = hideBursts;
    }

    /**
     * Sets explicitly web ID (or DOM ID) of generated tags.
     * Useful if tags are referred from JavaScript code.
     *
     * @param webId JavaScript safe String
     */
    public void setWebId(String webId) {
        this.webId = webId;
    }

    public void setProdImageType(String text) {
        this.prodImageType = text;
    }

    public void setBindToContainerSize(String text) {
        this.bindToContainerSize = text;
    }

    @Override
    public int doStartTag() {
        if (product == null) {
            if (calculator == null) {
                throw new RuntimeException(
                    "'product' and 'priceCalculator' is null!");
            }

            product = calculator.getProductModel();
        } else if (calculator == null) {
            calculator = product.getPriceCalculator();
        }

        Image prodImg = null;

        // check prodImageType
        if (prodImageType != null) {
            //we've passed in a specific image type we want (switch would be nice here)
            if ("PROD_IMAGE".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getProdImage();
            } else if ("PROD_IMAGE_FEATURE".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getFeatureImage();
            } else if ("RATING_RELATED_IMAGE".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getRatingRelatedImage();
            } else if ("ALTERNATE_IMAGE".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getAlternateImage();
            } else if ("DESCRIPTIVE_IMAGE".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getDescriptiveImage();
            } else if ("PROD_IMAGE_ROLLOVER".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getRolloverImage();
            } else if ("PROD_IMAGE_CONFIRM".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getConfirmImage();
            } else if ("PROD_IMAGE_DETAIL".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getDetailImage();
            } else if ("PROD_IMAGE_ZOOM".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getZoomImage();
            } else if ("PROD_IMAGE_PACKAGE".equalsIgnoreCase(prodImageType)) {
                prodImg = product.getPackageImage();
            }
        }

        //these will still override...
        if (useAlternateImage) {
            prodImg = product.getAlternateImage();
        }

        if (prodImg == null) {
            prodImg = product.getProdImage();
        }

        if (prodImg == null) {
            return SKIP_BODY;
        }

        //set boundContainer sizes
        if (bindToContainerSize != null) {
            setBoundContainerSizes(bindToContainerSize, prodImg);
        }

        pageContext.setAttribute(imageWidthVariableName,
            new Integer(getBoundWidth(prodImg)));
        pageContext.setAttribute(imageHeightVariableName,
            new Integer(getBoundHeight(prodImg)));

        StringBuilder buf = new StringBuilder();

        ProductLabeling pl = new ProductLabeling((FDUserI) pageContext.getSession()
                                                                      .getAttribute(SessionName.USER),
                calculator, hideBursts);

        if (browserInfo == null) {
            browserInfo = new BrowserInfo((HttpServletRequest) pageContext.getRequest());
        }

        final boolean needsOpacityWorkaround = browserInfo.isInternetExplorer() &&
            (browserInfo.getVersionNumber() < 7.0);

        // IE workaround
        if ((this.opacity == 1) && needsOpacityWorkaround) {
            this.opacity = 0.999;
        }

        final boolean supportsPNG = !((opacity < 1) &&
            browserInfo.isInternetExplorer() &&
            (browserInfo.getVersionNumber() < 7.0));

        // not disabled, has action and not in cart (savings) -> add link
        final boolean shouldGenerateAction = !this.disabled &&
            (this.action != null) && !this.isInCart;
        boolean displayBurst = false;
		try {
			displayBurst = savingsPercentage > 0 || pl.isDisplayAny() || (this.product.getFDGroup() != null);
		} catch (FDResourceException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

        String imageStyle = "border: 0; vertical-align: bottom; -moz-force-broken-image-icon: 1; ";

        if (opacity < 1) {
            imageStyle += TransparentBoxTag.getOpacityStyle(browserInfo, opacity);
        }

        if (this.webId == null) {
            webId = GetContentNodeWebIdTag.getWebId(null, product, true);
        }
        
        //add coupon, if not passed in already and in the page attributes
        /* this tends to end up setting a coupon you don't want (from another product)
         * removing for now
         * */
        /* if (coupon == null) {
        	this.setCoupon((FDCustomerCoupon)pageContext.getRequest().getAttribute("custCoupon"));    	
        } */
        
        int couponBufferW = 0; //additional spacing added to image for coupons
        int couponBufferH = 0;

        //if we have a coupon and prod image is null (defaults to prod image) or IS prod image
        // OR we're specifying the class (for rows of product images with/without coupons, mixed)
        if (
        	/*(coupon != null && (prodImageType == null || "PROD_IMAGE".equalsIgnoreCase(prodImageType)))
    		||*/ className != null && className.contains("couponLogo")
        ) {
        	couponBufferH = 25; //height of logo image
        }

        buf.append("<div id=\"" + webId + "\" class=\"product-image-container");
        if ((className != null) && (className.length() > 0)) {
            buf.append(" " + className);
        }
        buf.append("\"");
        buf.append(" style=\"padding: 0px; border: 0px; margin: 0px auto; " +
            "width: " + (getBoundWidth(prodImg)+couponBufferW) + "px; " +
            "height: " + (getBoundHeight(prodImg)+couponBufferH) + "px; " + 
            "line-height: " + (getBoundHeight(prodImg)+couponBufferW) + "px; " +
            "position: relative;\"");


        buf.append(">\n");
        
        String imageName = "ro_img_" + product.getContentName();
        String domains = FDStoreProperties.getSubdomains();
        // ============= prepare rollover image script =============  
        String rolloverStr = "";

        if (showRolloverImage) {
            Image rolloverImage = product.getRolloverImage();

            if (rolloverImage != null) {
            	
                String rolloverImagePath = domains + rolloverImage.getPathWithPublishId();
                String baseImagePath = domains + prodImg.getPathWithPublishId();

                if (!"".equals(rolloverImagePath) && !"".equals(baseImagePath)) {
                    rolloverStr = " onMouseover='swapImage(\"" + imageName +
                        "\",\"" + rolloverImagePath + "\");return true;'" +
                        " onMouseout='swapImage(\"" + imageName + "\",\"" +
                        baseImagePath + "\");return true;'";
                }
            }
        }

        // product image
        if (shouldGenerateAction) {
            buf.append("<a id=\"prdImgAncr_");
            buf.append(webId);
            buf.append("\" href=\"");
            buf.append(action);
            buf.append("\" style=\"vertical-align: bottom;\" class=\"product-image-link\">");
        }

        buf.append("<img src=\"");

        if (this.prefix != null) {
            buf.append(this.prefix);
        }

        buf.append(domains + prodImg.getPathWithPublishId());
        buf.append("\"");

        if (getBoundImgWidth(prodImg) > 0) {
            buf.append(" width=\"");
            //buf.append(prodImg.getWidth());
            buf.append(getBoundImgWidth(prodImg));

            if (bindToContainerSizes.containsKey("IS_PERCENT") &&
                    (bindToContainerSizes.get("IS_PERCENT") == 1)) {
                buf.append("%");
            }

            buf.append("\"");
        }

        if (getBoundImgHeight(prodImg) > 0) {
            buf.append(" height=\"");
            //buf.append(prodImg.getHeight());
            buf.append(getBoundImgHeight(prodImg));

            if (bindToContainerSizes.containsKey("IS_PERCENT") &&
                    (bindToContainerSizes.get("IS_PERCENT") == 1)) {
                buf.append("%");
            }

            buf.append("\"");
        }

        buf.append(" alt=\"");
        buf.append(product.getFullName());
        buf.append("\"");

        //setting a style will override coupon style
        if ((style != null) && (style.length() > 0)) {
            buf.append(" style=\"");
            buf.append(imageStyle + " " + style);
            buf.append("\"");
        } else {
            buf.append(" style=\"");
            buf.append(imageStyle);
            buf.append(" position: absolute; top: "+couponBufferH+"px; left: "+couponBufferW+"px;");
            buf.append("\"");
        }

        buf.append(" name=\"");
        buf.append(imageName);
        buf.append("\"");

        buf.append(rolloverStr);

        buf.append(">");
		try {
			if ( ContentNodeModelUtil.hasWineDepartment(product.getContentKey()) && (product.getSku(0).getProduct() != null && !"".equals(product.getSku(0).getProduct().getMaterial().getAlcoholicContent().getCode())) ) {
				if ((pageContext.getRequest().getParameter("catId") == null || !pageContext.getRequest().getParameter("catId").startsWith(WineUtil.getWineAssociateId().toLowerCase())) && !WineUtil.getWineAssociateId().toLowerCase().equals(pageContext.getRequest().getParameter("deptId")) && !((HttpServletRequest)pageContext.getRequest()).getServletPath().contains("wine")) {
					 /* check for usq wine for usq badge */
					//generic css class (switch css image instead)
					buf.append("<span class=\"burst-wine\"></span>");
				}
			}
		} catch (FDResourceException e1) {
		} catch (FDSkuNotFoundException e1) {
		}
		


        //add coupon now that container exists
        /*if (coupon != null && "PROD_IMAGE_ZOOM".equalsIgnoreCase(prodImageType)) {
        	buf.append("<div class=\"fdCoupon_prodBadge\" style=\"position: absolute; top: 0; left: " + (getBoundImgWidth(prodImg) - 35) + "px;\">");
				buf.append("<img src=\"/media/images/ecoupon/badge-small.png\" alt=\"FDCoupon Badge\" />");
			buf.append("</div>");
        }*/
        if (
    		(coupon != null || (className != null && className.contains("couponLogo")) )
    		&& (prodImageType == null || "PROD_IMAGE".equalsIgnoreCase(prodImageType))
        ) {
        	buf.append("<div class=\"fdCoupon_prodLogo\" style=\"position: absolute; top: 0; left: 0; height: "+couponBufferH+"px; width: 100%;");
        		if (coupon != null) { //add logo
        			buf.append(" background: url('/media/images/ecoupon/logo-med.gif') no-repeat center center;");
        		}
        		buf.append("\">");
			buf.append("</div>");
        }


        if (shouldGenerateAction) {
            buf.append("</a>");
        }

        if (displayBurst) {
            try {
				appendBurst(buf, pl, supportsPNG, shouldGenerateAction);
			} catch (FDResourceException e) {
			}
        }

        // [APPDEV-672] QUICK BUY button
        if (enableQuickBuy) {
            final String prdId = product.getContentKey().getId();
            final String catId = product.getParentId();
            final String deptId = product.getDepartment().getContentKey().getId();

            TrackingCodes t = new TrackingCodes(action);

            
            
            buf.append("<img id=\"qb_" + webId +
                "\" class=\"qbLaunchButton\" src=\"" + quickBuyImage + "\">\n");

            buf.append("<script type=\"text/javascript\">\n" +
                "  FD_QuickBuy.decorate('" + webId + "', ['qb_" + webId +
                "', 'prdImgAncr_" + webId + "'], {");
            buf.append(
            		  "departmentId: '" + deptId + "', "
            		+ "categoryId: '" + catId + "', "
            		+ "productId: '" + prdId + "', "
            		+ "wineDeptId: '" + WineUtil.getWineAssociateId() + "'"
            		);
            buf.append("}, 'QUICKBUY'");
            if (t.isValid()) {
            	buf.append(", ");
            	t.appendQuickBuyParamsTo(buf);
            }
            buf.append(");\n" + "</script>");
        }

        // close outer frame
        buf.append("</div>\n");

        try {
            JspWriter out = pageContext.getOut();
            out.println(buf.toString());
        } catch (IOException e) {
        }

        return EVAL_BODY_INCLUDE;
    }

    private int getHeightInternal(Image prodImg) {
        // explicit height was set
        if (height > 0) {
            return height;
        }

        // if inside a carousel
        CarouselTag carousel = (CarouselTag) findAncestorWithClass(this,
                CarouselTag.class);

        if (carousel != null) {
            return carousel.getMaxImageHeight();
        }

        // else use the image height
        return prodImg.getHeight();
    }

    private void setBoundContainerSizes(String bindToSizeStr, Image prodImgVar) {
        String[] sizeParse = new String[0];

        if (bindToSizeStr != null) {
            sizeParse = bindToSizeStr.split(",");

            for (int m = 0; m < sizeParse.length; m++) {
                StringTokenizer keyValues = new StringTokenizer(sizeParse[m],
                        "=");

                while (keyValues.hasMoreTokens()) {
                    String key = keyValues.nextToken();
                    String val = "";

                    if (keyValues.hasMoreTokens()) {
                        val = keyValues.nextToken().trim();
                    }

                    try {
                        if ("h".equalsIgnoreCase(key)) {
                            bindToContainerSizes.put("CONT_HEIGHT",
                                Integer.parseInt(val));
                        }
                    } catch (NumberFormatException nfe) {
                        bindToContainerSizes.put("CONT_HEIGHT", -1);
                    }

                    try {
                        if ("w".equalsIgnoreCase(key)) {
                            bindToContainerSizes.put("CONT_WIDTH",
                                Integer.parseInt(val));
                        }
                    } catch (NumberFormatException nfe) {
                        bindToContainerSizes.put("CONT_WIDTH", -1);
                    }
                }
            }

            //turn on use of percentages
            bindToContainerSizes.put("IS_PERCENT", 1);
        } else {
            //set image sizes themselves since we didn't get them from boundContainer
            bindToContainerSizes.put("IMAGE_HEIGHT",
                getBoundImgHeight(prodImgVar));
            bindToContainerSizes.put("IMAGE_WIDTH", getBoundImgWidth(prodImgVar));
            bindToContainerSizes.put("CONT_HEIGHT",
                getBoundImgHeight(prodImgVar));
            bindToContainerSizes.put("CONT_WIDTH", getBoundImgWidth(prodImgVar));

            //and make sure percent usage is off
            bindToContainerSizes.put("IS_PERCENT", 0);
        }

        /* estimate image size to percentages */
        if (bindToContainerSizes.get("IS_PERCENT") == 1) {
            //use the biggest edge of the container
            if ((getBoundHeight(prodImgVar) > getBoundWidth(prodImgVar)) ||
                    (prodImgVar.getHeight() > prodImgVar.getHeight())) {
                bindToContainerSizes.put("IMAGE_HEIGHT", 100);
                bindToContainerSizes.put("IMAGE_WIDTH", -1);
            } else {
                bindToContainerSizes.put("IMAGE_HEIGHT", -1);
                bindToContainerSizes.put("IMAGE_WIDTH", 100);
            }
        }
    }

    private int getBoundImgHeight(Image prodImgVar) {
        if (bindToContainerSizes.containsKey("IMAGE_HEIGHT")) {
            return bindToContainerSizes.get("IMAGE_HEIGHT");
        }

        //if no bound height set, fall back to the default
        if (prodImgVar != null) {
            return getBoundHeight(prodImgVar);
        }

        return -1;
    }

    private int getBoundImgWidth(Image prodImgVar) {
        if (bindToContainerSizes.containsKey("IMAGE_WIDTH")) {
            return bindToContainerSizes.get("IMAGE_WIDTH");
        }

        //if no bound height set, fall back to the default
        if (prodImgVar != null) {
            return getBoundWidth(prodImgVar);
        }

        return -1;
    }

    private int getBoundHeight(Image prodImgVar) {
        if ((prodImgVar != null) &&
                bindToContainerSizes.containsKey("CONT_HEIGHT")) {
            return bindToContainerSizes.get("CONT_HEIGHT");
        }

        //if no bound height set, fall back to the default
        if (prodImgVar != null) {
            return prodImgVar.getHeight();
        }

        return -1;
    }

    private int getBoundWidth(Image prodImgVar) {
        if ((prodImgVar != null) &&
                bindToContainerSizes.containsKey("CONT_WIDTH")) {
            return bindToContainerSizes.get("CONT_WIDTH");
        }

        if (prodImgVar != null) {
            //if no bound width set, fall back to the default
            return prodImgVar.getWidth();
        }

        return -1;
    }

    /**
     * Appends burst image
     *
     * @param buf output
     * @param pl Product Labeling
     * @param supportsPNG Is PNG supported?
     * @param shouldGenerateAction Should add link to image
     * @throws FDResourceException 
     */
    private void appendBurst(StringBuilder buf, ProductLabeling pl,
        final boolean supportsPNG, final boolean shouldGenerateAction) throws FDResourceException {
        // burst image
        String burstImageStyle = "border: 0;";

        //temp vars without defaults (defaults set after overrides, if needed)
        String cHeight = null;
        String cWidth = null;
        String cStyle = null;
        String cCssClass = null;

        String iHeight = null;
        String iWidth = null;
        String iSize = null;
        String iSrc = null;
        String iSizeToken = "%%iSize%%"; //replacement token
        String iAlt = null;
        String iStyle = null;
        String iCssClass = null;

        String aStyle = null;

        if (this.opacity < 1) {
            burstImageStyle += TransparentBoxTag.getOpacityStyle(browserInfo,
                this.opacity);
        }

        // get deal
        int deal = 0;

        if (savingsPercentage > 0) {
            deal = (int) (savingsPercentage * 100);
        } else if (pl.isDisplayDeal()) {
        	//APPDEV-2414
        	try {
				if(product.getFDGroup() != null) {
					deal = calculator.getHighestDealPercentage();
				} else {
					deal = (int) calculator.getBurstDealsPercentage(excludeCaseDeals
				        ? ProductBurstTag.EXCLUDED_WINE_TIERS : null);
				}
			} catch (FDResourceException e) {
				LOGGER.error("FDResourceException",e);
			}
        } else {
        	try {
				if(product.getFDGroup() != null) {
					deal = calculator.getGroupDealPercentage();
				}
			} catch (FDResourceException e) {
				LOGGER.error("FDResourceException",e);
			}
        }

        if ((deal < FDStoreProperties.getBurstsLowerLimit()) ||
                (deal > FDStoreProperties.getBurstUpperLimit())) {
        	deal = 0;
        }

        if (this.isInCart) {
            // Smart Savings - display "In Cart" burst
            // No opacity needed since burst image is already faded
            iSrc = ((this.prefix != null) ? this.prefix : "") +
                "/media_stat/images/bursts/in_cart" +
                (supportsPNG ? ".png" : ".gif");
            iAlt = "IN CART";
            iStyle = "border: 0;";

        } else if (deal > 0) {
            iSrc = ((this.prefix != null) ? this.prefix : "") +
                "/media_stat/images/deals/brst_" + iSizeToken + "_" + deal +
                (supportsPNG ? ".png" : ".gif");
            iAlt = "SAVE";
            iStyle = burstImageStyle;

        } else if (pl.isDisplayFave()) {
            iSrc = ((this.prefix != null) ? this.prefix : "") +
                "/media_stat/images/bursts/brst_" + iSizeToken + "_fave" +
                (supportsPNG ? ".png" : ".gif");
            iAlt = "FAVE";
            iStyle = burstImageStyle;

        } else if (pl.isDisplayNew() && !this.isNewProductPage) {
            iSrc = ((this.prefix != null) ? this.prefix : "") +
                "/media_stat/images/bursts/brst_" + iSizeToken + "_new" +
                (supportsPNG ? ".png" : ".gif");
            iAlt = "NEW";
            iStyle = burstImageStyle;

        } else if (pl.isDisplayBackinStock()) {
            iSrc = ((this.prefix != null) ? this.prefix : "") +
                "/media_stat/images/bursts/brst_" + iSizeToken + "_bis" +
                (supportsPNG ? ".png" : ".gif");
            iAlt = "BACK";
            iStyle = burstImageStyle;

        }

        //override values (may or may not be used, depending on the burst that ends up being generated)
        if (burstOptions != null) {
            String[] burstOptionsParse = new String[0];
            burstOptionsParse = burstOptions.split(",");

            for (int m = 0; m < burstOptionsParse.length; m++) {
                StringTokenizer keyValues = new StringTokenizer(burstOptionsParse[m],
                        "=");

                while (keyValues.hasMoreTokens()) {
                    String key = keyValues.nextToken();
                    String val = "";

                    if (keyValues.hasMoreTokens()) {
                        val = keyValues.nextToken().trim();
                    }

                    //image-level options
                    if ("h".equalsIgnoreCase(key)) {
                        iHeight = val;
                    }

                    if ("w".equalsIgnoreCase(key)) {
                        iWidth = val;
                    }

                    if ("alt".equalsIgnoreCase(key)) {
                        iAlt = val;
                    }

                    if ("style".equalsIgnoreCase(key)) {
                        iStyle = burstImageStyle + val;
                    }

                    if ("cssClass".equalsIgnoreCase(key)) {
                        iCssClass = val;
                    }

                    if ("src".equalsIgnoreCase(key)) {
                        iSrc = val;
                    }

                    if ("size".equalsIgnoreCase(key)) {
                        //this is "sm" or "lg", invalid items will invalidate the burst (if it has sizes)
                        iSize = val;
                    }

                    //anchor level options
                    if ("a_style".equalsIgnoreCase(key)) {
                        aStyle = val;
                    }

                    //container-level options
                    if ("c_h".equalsIgnoreCase(key)) {
                        cHeight = val;
                    }

                    if ("c_w".equalsIgnoreCase(key)) {
                        cWidth = val;
                    }

                    if ("c_style".equalsIgnoreCase(key)) {
                        cStyle = val;
                    }

                    if ("c_cssClass".equalsIgnoreCase(key)) {
                        cCssClass = val;
                    }
                }
            }
        }

        // we need to set the default size BEFORE trying to get the burst (because it uses it in the src)
        if (iSize == null) {
            iSize = "sm";
        }

        //check for invalids and default them
        if (cHeight == null) {
            if ("lg".equalsIgnoreCase(iSize)) {
                cHeight = "55";
            } else {
                cHeight = "35";
            }
        }

        if (cWidth == null) {
            if ("lg".equalsIgnoreCase(iSize)) {
                cWidth = "55";
            } else {
                cWidth = "35";
            }
        }

        if (cStyle == null) {
            cStyle = "position: absolute; top: 0px; left: 0px; width: " +
                cWidth + "px; height: " + cHeight + "px;";
        }

        if (cCssClass == null) {
            cCssClass = "productImageBurst";
        }

        if (aStyle == null) {
            aStyle = "display: inline-block; width: " + cWidth +
                "px; height: " + cHeight + "px; line-height: " + cHeight +
                "px;";
        }

        if (iHeight == null) {
            if ("lg".equalsIgnoreCase(iSize)) {
                iHeight = "55";
            } else {
                iHeight = "35";
            }
        }

        if (iWidth == null) {
            if ("lg".equalsIgnoreCase(iSize)) {
                iWidth = "55";
            } else {
                iWidth = "35";
            }
        }


        if (iSrc != null) { //if iSrc is null, we didn't have a burst set
            String path = iSrc.replace(iSizeToken, iSize);
            int h = Integer.parseInt(iHeight);
            int w = Integer.parseInt(iWidth);

            buf.append("<div class=\"" + cCssClass + "\" style=\"" + cStyle +
                "\">\n");

            if (shouldGenerateAction) {
                buf.append("<a href=\"" + action + "\" style=\"" + aStyle +
                    "\" class=\"product-image-burst-link\">");
            }

            buf.append(Image.toHtml(path, w, h, iAlt, iCssClass));

            if (shouldGenerateAction) {
                buf.append("</a>\n");
            }

            buf.append("</div>\n");
        }
    }

    public static class TagEI extends TagExtraInfo {/*
        public VariableInfo[] getVariableInfo(TagData data) {
            return new VariableInfo[] {
                new VariableInfo(imageWidthVariableName, "java.lang.Integer",
                    true, VariableInfo.AT_END),
                new VariableInfo(imageHeightVariableName, "java.lang.Integer",
                    true, VariableInfo.AT_END)
            };
        }
    */}
}



/**
 * Utility class that extracts tracking codes from action URI
 * 
 * @author segabor
 */
class TrackingCodes {
	String trk;
	String trkd;
	String rank;
	
	boolean valid = false;
	
	public boolean isValid() {
		return valid;
	}

	public TrackingCodes(String action) {
        if (action != null) {
        	/*
        	 * Ugly hack to extract tracking codes from product link :/
        	 */
        	try {
				URI i = new URI(action);
				// split up parameters
				// final String unescaped = URLDecoder.decode(i.getQuery(), "UTF-8");
				final String unescaped = StringEscapeUtils.unescapeHtml(i.getQuery());
				
				// String[] p =  i.getQuery().split("&amp;");
				String[] p =  unescaped.split("&");

				for (final String ap : p) {
					if (ap.startsWith("trk=")) {
						trk = ap.substring(4);
					} else if (ap.startsWith("trkd=")) {
						trkd = ap.substring(5);
					} else if (ap.startsWith("rank=")) {
						trkd = ap.substring(5);
					}
				}

				if (trk != null)
					valid = true;
			} catch (URISyntaxException e) {
			}
        }
	}

	/* 
	 * Create javascript expression from extracted tracking codes
	 * and append it as a javascript expression for QuickBuy decorate() function
	 * 
	 * Expression:
	 * "{ trk: '...', trkd: '...', rank: '...'}"
	 * 
	 */
	public void appendQuickBuyParamsTo(Appendable buf) {
		if (valid) {
			try {
				buf.append("{");
    			buf.append("trk: '").append(this.trk).append("'");
    			if (this.trkd != null) { buf.append("trkd: '").append(this.trkd).append("'"); }
    			if (this.rank != null) { buf.append("rank: '").append(this.rank).append("'"); }
    			buf.append("}");
			} catch (IOException e) {
			}
		}
	}
}
