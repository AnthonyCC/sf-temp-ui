package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.EnumBurstType;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.ProductLabeling;
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
	
	String			webId = null;				// DOM id of generated tags (optional)
	
	double			opacity = 1; // 1-transparency
	boolean			isNewProductPage = false;
	int				height = -1; // negative height means height is calculated based on img height
	
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
	
	@Override
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
		pageContext.setAttribute( imageHeightVariableName, new Integer( getHeightInternal( prodImg ) ) );

		
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

		String imageStyle = "border: 0; vertical-align: bottom; ";

		if (opacity < 1) {
			imageStyle += TransparentBoxTag.getOpacityStyle(browserInfo, opacity);
		}

		if (this.webId == null)
			webId = GetContentNodeWebIdTag.getWebId(null, product, true);

		buf.append("<div id=\"" + webId + "\" "
				+ "style=\"padding: 0px; border: 0px; margin: 0px auto; "
				+ "width: " + prodImg.getWidth() + "px; "
				+ "height: " + getHeightInternal( prodImg ) + "px; "
				+ "line-height: " + getHeightInternal( prodImg ) + "px; "
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
			buf.append(webId);
			buf.append("\" href=\"");
			buf.append(action);
			buf.append("\" style=\"vertical-align: bottom;\">");
		}

		buf.append("<img src=\"");
		if (this.prefix != null)
			buf.append(this.prefix);
		buf.append(prodImg.getPath());
		buf.append("\"");
		
//		buf.append(" id=\"prdImg_");
//		buf.append(webId);
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
		if (enableQuickBuy) {
			final String prdId = product.getContentKey().getId();
			final String catId = product.getParentId();
			final String deptId = product.getDepartment().getContentKey().getId();
			

			buf.append("<img id=\"qb_"+webId+"\" class=\"qbLaunchButton\" src=\"/media_stat/images/quickbuy/quickbuy_button_hover.gif\">\n");
			/* buf.append("<script type=\"text/javascript\">\n" +
					"FD_QuickBuy.attach('"+webId+"', '" + deptId + "', '" + catId + "', '" + prdId + "');\n" +
					"</script>"); */
			buf.append("<script type=\"text/javascript\">\n" +
					"  FD_QuickBuy.decorate('"+webId+"', ['qb_"+webId+"', 'prdImgAncr_"+webId+"'], {departmentId: '" + deptId + "', categoryId: '" + catId + "', productId: '" + prdId + "'});\n" +
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

	
	private int getHeightInternal( Image prodImg ) {	
		
		// explicit height was set
		if ( height > 0 )
			return height;
		
		// if inside a carousel
		CarouselTag carousel = (CarouselTag) findAncestorWithClass( this, CarouselTag.class );
		if (carousel != null)
			return carousel.getMaxImageHeight();
		
		// else use the image height
		return prodImg.getHeight();
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

		if (deal < FDStoreProperties.getBurstsLowerLimit() || deal > FDStoreProperties.getBurstUpperLimit())
			deal = 0;
		

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
