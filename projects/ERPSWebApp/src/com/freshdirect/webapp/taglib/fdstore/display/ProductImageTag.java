package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.EnumBurstType;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.ProductLabeling;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.BrowserInfo;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.TxProductControlTag;
import com.freshdirect.webapp.taglib.fdstore.TxProductPricingSupportTag;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;
import com.freshdirect.webapp.util.prodconf.SmartStoreConfigurationStrategy;

/**
 * Product Image Tag
 * 
 * @author segabor
 *
 */
public class ProductImageTag extends BodyTagSupport {
	
	private static final long serialVersionUID = 8159061278833068855L;

	private static Random rnd = new Random();

	ProductModel	product; 					// product (mandatory)
	String			style; 						// CSS style modification (optional)
	String			className;					// CSS class name (optional)
	String			action; 					// URL (optional)
	boolean			disabled = false; 			// Image is not clickable
	String			prefix; 					// For internal use only! (optional)

	BrowserInfo		browserInfo = null;
	double			savingsPercentage = 0; 		// savings % off
	boolean			isInCart = false; 			// display savings - in cart
	
	boolean 		showRolloverImage = true;	// rollover image
	boolean 		useAlternateImage = false;	// alternate image
	boolean			enableQuickBuy = false;		// [APPDEV-672] Quick Buy button (optional)
	FDUserI			customer = null;			// Customer (optional - required for QuickBuy feature)
	
	double			opacity = 1; // 1-transparency
	boolean			isNewProductPage = false;
	
	Set<EnumBurstType> hideBursts;

	public static final String 					imageWidthVariableName 		= "productImageWidth";
	public static final String 					imageHeightVariableName 	= "productImageHeight";

	public void setProduct(ProductModel prd) {
		this.product = prd;
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
	

	public void setBrowserInfo(BrowserInfo browserInfo) {
		this.browserInfo = browserInfo;
	}


	public void setInCart(boolean isInCart) {
		this.isInCart = isInCart;
	}

	public void setSavingsPercentage(double savingsPercentage) {
		this.savingsPercentage = savingsPercentage;
	}

	public void setShowRolloverImage( boolean showRolloverImage ) {
		this.showRolloverImage = showRolloverImage;
	}
	
	public void setUseAlternateImage( boolean useAlternateImage ) {
		this.useAlternateImage = useAlternateImage;
	}

	public void setEnableQuickBuy(boolean enableQuickBuy) {
		this.enableQuickBuy = enableQuickBuy;
	}

	public void setOpacity(double opacity) {
		if (opacity < 0)
			this.opacity = 0;
		else if (opacity > 1)
			this.opacity = 1;
		else
			this.opacity = opacity;
	}
	


	
	public void setHideBursts(Set<EnumBurstType> hideBursts) {
		this.hideBursts = hideBursts;
	}

	
	public void setCustomer(FDUserI customer) {
		this.customer = customer;
	}
	

	public int doStartTag() {
		
		Image prodImg = null;
		
		if ( useAlternateImage ) {
			prodImg = product.getAlternateImage();
		}			
		if ( prodImg == null ) {
			prodImg = product.getProdImage();
		}			
		if ( prodImg == null ) {
			return SKIP_BODY;
		}

		pageContext.setAttribute( imageWidthVariableName, new Integer( prodImg.getWidth() ) );
		pageContext.setAttribute( imageHeightVariableName, new Integer( prodImg.getHeight() ) );

		
		StringBuilder buf = new StringBuilder();

		ProductLabeling pl = new ProductLabeling((FDUserI) pageContext.getSession().getAttribute(SessionName.USER), product, hideBursts);
		
		if (browserInfo == null)
			browserInfo = new BrowserInfo((HttpServletRequest) pageContext.getRequest());


		final boolean needsOpacityWorkaround = browserInfo.isInternetExplorer() && browserInfo.getVersionNumber() < 7.0;
		
		// IE workaround

		if (this.opacity == 1 && needsOpacityWorkaround)
			this.opacity = 0.999;

		final boolean supportsPNG = !(opacity < 1 && browserInfo.isInternetExplorer() && browserInfo.getVersionNumber() < 7.0);
		// not disabled, has action and not in cart (savings) -> add link
		final boolean shouldGenerateAction = !this.disabled && this.action != null && !this.isInCart;
		final boolean displayBurst = savingsPercentage > 0 || pl.isDisplayAny();

		String imageStyle = "border: 0; ";
		
		if (opacity < 1) {
			imageStyle += TransparentBoxTag.getOpacityStyle(browserInfo, opacity);
		}


		final String frameId = "PIT_"+product.getContentName()+"_"+Long.toHexString(rnd.nextLong());
		buf.append("<div id=\"" + frameId + "\" "
				+ "style=\"padding: 0px; border: 0px; margin: 0px auto; "
				+ "width: " + prodImg.getWidth() + "px; "
				+ "height: " + prodImg.getHeight() + "px; "
				+ "position: relative;\"");

		if (className != null && className.length() > 0) {
			buf.append(" class=\"");
			buf.append(className);
			buf.append("\"");
		}
		
		buf.append(">\n");
		
		
		String imageName = "ro_img_" + product.getContentName();
		
		// ============= prepare rollover image script =============  
		
		String rolloverStr = "";	
		if ( showRolloverImage ) {
			Image rolloverImage = product.getRolloverImage();
			
			if ( rolloverImage != null ) {					
				String rolloverImagePath = rolloverImage.getPath();
				String baseImagePath = prodImg.getPath();
				
				if ( ! "".equals( rolloverImagePath ) && ! "".equals( baseImagePath ) ) {
					rolloverStr = 
						" onMouseover='swapImage(\"" 	+ imageName + "\",\"" + rolloverImagePath 	+ "\");return true;'" + 
						" onMouseout='swapImage(\"" 	+ imageName + "\",\"" + baseImagePath 		+ "\");return true;'";
				}
			}
		}
		

		// product image
		if (shouldGenerateAction) {
			buf.append("<a id=\"prdImgAncr_");
			buf.append(frameId);
			buf.append("\" href=\"");
			buf.append(action);
			buf.append("\">");
		}

		buf.append("<img src=\"");
		if (this.prefix != null)
			buf.append(this.prefix);
		buf.append(prodImg.getPath());
		buf.append("\"");
		
//		buf.append(" id=\"prdImg_");
//		buf.append(frameId);
//		buf.append("\"");

		buf.append(" width=\"");
		buf.append(prodImg.getWidth());
		buf.append("\"");
		
		buf.append(" height=\"");
		buf.append(prodImg.getHeight());
		buf.append("\"");

		buf.append(" alt=\"");
		buf.append(product.getFullName());
		buf.append("\"");
		
		if (style != null && style.length() > 0) {
			buf.append(" style=\"");
			buf.append(imageStyle + " " + style);
			buf.append("\"");
		} else {
			buf.append(" style=\"");
			buf.append(imageStyle);
			buf.append("\"");
		}
		
		buf.append( " name=\"" );
		buf.append( imageName );
		buf.append( "\"" );
		
		buf.append( rolloverStr );			

		buf.append(">");

		if (shouldGenerateAction) {
			buf.append("</a>");
		}



		if (displayBurst) {
			appendBurst(buf, pl, supportsPNG, shouldGenerateAction);
		}

		// [APPDEV-672] QUICK BUY button
		if (enableQuickBuy && customer != null) {
			final String prdId = product.getContentKey().getId();
			final String catId = product.getParentId();
			final Image catImg = product.getCategoryImage();
			final String jsNs = JspMethods.safeJavaScriptVariable(frameId);

			buf.append("<div id=\"" + frameId + "_ctnt\" style=\"display: none;\">\n");
			try {
				buf.append(createQuickBuyPanelInterior(frameId, customer));
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
			buf.append("</div>\n");
			
			buf.append("<img id=\"qb_"+frameId+"\" class=\"qbLaunchButton\" src=\"/media_stat/images/quickbuy/quickbuy_button_hover.gif\">\n");
			buf.append("<script type=\"text/javascript\">\n" +
					"FD_QuickBuy.attach('"+frameId+"', '"+jsNs+"', '" + catId + "', '" + prdId + "', '"+ catImg.getPath() +"', "+ catImg.getWidth() +", "+ catImg.getWidth() +");\n" +
					"</script>");
			
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



	/**
	 * Appends burst image
	 * 
	 * @param buf output
	 * @param pl Product Labelling
	 * @param supportsPNG Is PNG supported?
	 * @param shouldGenerateAction Should add link to image
	 */
	private void appendBurst(StringBuilder buf, ProductLabeling pl, final boolean supportsPNG, final boolean shouldGenerateAction) {
		
		// burst image
		String burstImageStyle = "border: 0;";
		
		if (this.opacity < 1) {
			burstImageStyle += TransparentBoxTag.getOpacityStyle(browserInfo, this.opacity);
		}
		
		// get deal
		int deal = 0;
		if (savingsPercentage > 0) {
			deal = (int)(savingsPercentage*100);
		} else if (pl.isDisplayDeal()) {
			deal = product.getHighestDealPercentage();
		}

		buf.append("<div class=\"productImageBurst\" style=\"position: absolute; top: 0px; left: 0px\">\n");
		if (shouldGenerateAction) {
			buf.append("<a href=\"");
			buf.append(action);
			buf.append("\">");
		}

		if (this.isInCart) {
			// Smart Savings - display "In Cart" burst
			// No opacity needed since burst image is already faded
			buf.append("<img alt=\"IN CART\" src=\"");
				if (this.prefix != null)
					buf.append(this.prefix);
			buf.append("/media_stat/images/bursts/in_cart" + (supportsPNG ? ".png" : ".gif") + "\" width=\"35px\" height=\"35px\" style=\"border:0;\">\n");
		} else if (deal > 0) {
			String burstImage = "/media_stat/images/deals/brst_sm_" + deal + (supportsPNG ? ".png" : ".gif");
			buf.append("<img alt=\"SAVE " + deal + "\" src=\"");
				if (this.prefix != null)
					buf.append(this.prefix);
			buf.append(burstImage+"\" width=\"35px\" height=\"35px\" style=\""+ burstImageStyle +"\">\n");
		} else if (pl.isDisplayFave()) {
			buf.append("<img alt=\"FAVE\" src=\"");
				if (this.prefix != null)
					buf.append(this.prefix);
			buf.append("/media_stat/images/bursts/brst_sm_fave"+(supportsPNG ? ".png" : ".gif")+"\" width=\"35px\" height=\"35px\" style=\""+ burstImageStyle +"\">\n");
		} else if (pl.isDisplayNew() && !this.isNewProductPage) {
			buf.append("<img alt=\"NEW\" src=\"");
				if (this.prefix != null)
					buf.append(this.prefix);
			buf.append("/media_stat/images/bursts/brst_sm_new"+(supportsPNG ? ".png" : ".gif")+"\" width=\"35px\" height=\"35px\" style=\""+ burstImageStyle +"\">\n");
		}else if (pl.isDisplayBackinStock()) {
			buf.append("<img alt=\"BACK\" src=\"");
				if (this.prefix != null)
					buf.append(this.prefix);
			buf.append("/media_stat/images/bursts/brst_sm_bis"+(supportsPNG ? ".png" : ".gif")+"\" width=\"35px\" height=\"35px\" style=\""+ burstImageStyle +"\">\n");
		}
		if (shouldGenerateAction) {
			buf.append("</a>\n");
		}

		buf.append("</div>\n");
	}

	
	@SuppressWarnings("unchecked")
	private String createQuickBuyPanelInterior(String frameId, FDUserI customer) throws FDResourceException {
		if (customer == null)
			return "";

		ProductImpression pi = configureProduct(product, customer);
		if (! (pi instanceof TransactionalProductImpression) ) {
			return "Product is not transactional!";
		}


		final TransactionalProductImpression txImp = (TransactionalProductImpression) pi;
		final SkuModel sku = txImp.getSku();
		FDProduct fdProduct = null;

		try {
			fdProduct = sku.getProduct();
		} catch (FDSkuNotFoundException e) {
			throw new FDResourceException(e);
		}
		

		
		// "delivery restriction"
		final String earliestAvailability = sku.getEarliestAvailabilityMessage();
		final boolean displayShortTermUnavailability = fdProduct.getMaterial().getBlockedDays().isEmpty();
		final boolean showAvailMsg = displayShortTermUnavailability && earliestAvailability != null;

		// min-max limits
		final boolean hasMin = product.getQuantityMinimum() != 1;
		final boolean hasMax = customer.getQuantityMaximum(product)<99;
		final boolean showMinMaxWarning = hasMin || hasMax;





		final String jsNamespace = JspMethods.safeJavaScriptVariable(frameId);
		
		final Collection<TransactionalProductImpression> txList = new ArrayList<TransactionalProductImpression>();
		txList.add(txImp);




		StringBuilder buf = new StringBuilder();

		
		buf.append("<div id=\""+frameId+"_overbox\" class=\"overbox\">\n");


		/* NEW FEATURE BUBBLE - placeholder */
		buf.append("  <div id=\""+frameId+"_nfeat\" class=\"nfeat roundedbox\"></div>\n");

		/* Form Errors BUBBLE - placeholder  */
		buf.append("  <div id=\""+frameId+"_errors\" class=\"alerts roundedbox\"></div>\n");

		buf.append("</div>\n");
		
		// LEFT COLUMN
		buf.append( TxProductPricingSupportTag.getHTMLFragment(customer, txList, false, jsNamespace, frameId, null, true) );
		
		buf.append("<input type=\"hidden\" name=\"frameId\" value=\""+frameId+"\">\n");
		buf.append("<input type=\"hidden\" name=\"itemCount\" value=\"1\">\n");

		
		// duplicate parameters and override TRKD
		final Map<String,String[]> duppedParams = new HashMap<String,String[]>();
		for (Map.Entry<String, String[]> pair : (Set<Map.Entry<String, String[]>>) pageContext.getRequest().getParameterMap().entrySet() ) {
			duppedParams.put(pair.getKey(), pair.getValue());
		}
		duppedParams.put("trkd", new String[]{"qb"});
		FDURLUtil.appendCommonParameters(buf, duppedParams, "_0");

		String trk = "qbuy"; // default value
		if (duppedParams.keySet().contains("trk") && duppedParams.get("trk").length > 0 ) {
			trk = duppedParams.get("trk")[0];
		}
		
		buf.append("<div id=\""+frameId+"_inner\">"); // <-- INNER BOX START
		buf.append("<div class=\"left\">\n"); // <-- INNER>LEFT BOX START
		buf.append("  <div class=\"title16\">" + product.getFullName() + "</div>\n");
		buf.append("  <div class=\"title14\"><a style=\"font-weight:normal\" href=\"" + FDURLUtil.getProductURI(product, trk, "qb", -1) + "\">Learn More</a></div>\n");
		// FIXME: adjust boolean flags show... in second line
		buf.append("  <div class=\"price\" style=\"padding: 1em 0em\">\n");
		buf.append(ProductPriceTag.getHTMLFragment(txImp, savingsPercentage,
				false, false, true, false, false,
				true, false, sku.getSkuCode()));
		buf.append("  </div>\n");
		

		buf.append("  <div class=\"qb-separator\"></div>\n"); // separator
		
		buf.append("  <div class=\"text11 qbTxControl\">\n");
		buf.append( TxProductControlTag.getHTMLFragment( txImp, null, 0, jsNamespace, false, true) );
		buf.append("    <div class=\"text11bold qbTxName\">Quantity&nbsp;" + (showMinMaxWarning ? "*" : "") + "</div>\n");
		buf.append("  </div>\n");

		buf.append("  <div class=\"text11bold qbTxTotal\">\n");
		buf.append("    <input class=\"text11bold\" type=\"text\" name=\"total\" value=\"\">\n");
		buf.append("    <div class=\"text11bold qbTxName2\">Price&nbsp;</div>\n");
		buf.append("  </div>\n");

		buf.append("  <div class=\"title14\">\n");
		buf.append("    <input type=\"image\" src=\"/media_stat/images/buttons/add_to_cart.gif\" style=\"margin-top:10px\" name=\"image\" width=\"93\" height=\"20\" onclick=\"FD_QuickBuy.postForm(this.form, '"+frameId+"'); return false;\">\n");
		buf.append("  </div>\n");

		// Notifications / warnings
		{
			final DecimalFormat QUANTITY_FORMATTER = new DecimalFormat("0.##");

			if (showAvailMsg || showMinMaxWarning) {
				buf.append("  <div class=\"qb-separator\" style=\"margin-top: 8px;\"></div>\n"); // separator
			}
			
			if (showAvailMsg) {
				buf.append("  <div class=\"text10rbold\">Earliest Delivery " + earliestAvailability + "</div>\n");
			}

			if (showMinMaxWarning) {
				buf.append("  <div class=\"text10\">* Note: ");
				if (hasMin) {
					buf.append(QUANTITY_FORMATTER.format(product.getQuantityMinimum()));
					buf.append(" minimum");
					if (hasMax)
						buf.append(", ");
				}
				if (hasMax) {
					buf.append("limit ");
					buf.append(QUANTITY_FORMATTER.format(customer.getQuantityMaximum(product)));
				}
				buf.append("</div>\n");
			}
		}

		buf.append("</div>\n"); // <-- INNER>LEFT BOX END

		// RIGHT COLUMN - image
		buf.append("<div class=\"right\" style=\"\">\n"); // <-- INNER>RIGHT BOX START
		
		{
			Image catImg = product.getDetailImage();
			buf.append("  <img src=\"");
			if (this.prefix != null)
				buf.append(this.prefix);
			buf.append(catImg.getPath());
			buf.append("\"");
			
			buf.append(" width=\"");
			buf.append(catImg.getWidth());
			buf.append("\"");
			
			buf.append(" height=\"");
			buf.append(catImg.getHeight());
			buf.append("\"");

			buf.append(">\n");
		}

		buf.append("</div>\n"); // <-- INNER>RIGHT BOX END
		buf.append("</div>\n"); // <-- INNER BOX END
		
		return buf.toString();
	}
	
	
	/**
	 * Configures a product if possible.
	 * 
	 * @param prd
	 * @param u
	 * @return Instance of {@link TransactionalProductImpression} if product is transactional,
	 * otherwise returns {@link ProductImpression}
	 */
	private ProductImpression configureProduct(ProductModel prd, FDUserI u) {
		// try to configure product
		ConfigurationContext confContext = new ConfigurationContext();
		confContext.setFDUser( u );

		ConfigurationStrategy cUtil = SmartStoreConfigurationStrategy.getInstance();
		ProductImpression pi = cUtil.configure(product, confContext);
		
		return pi;
	}

	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
	        return new VariableInfo[] {
		            new VariableInfo(
		            		imageWidthVariableName,
		            		"java.lang.Integer",
		            		true, 
		            		VariableInfo.AT_END ),
		            new VariableInfo(
		            		imageHeightVariableName,
		            		"java.lang.Integer",
		            		true, 
		            		VariableInfo.AT_END )
		        };
			
		}
	}
}