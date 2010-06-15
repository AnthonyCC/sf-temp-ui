package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

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

	double			opacity = 1; // 1-transparency
	boolean			isNewProductPage = false;
	
	Set<EnumBurstType> hideBursts;

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


	public int doStartTag() {
		try {
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
			
			JspWriter out = pageContext.getOut();
			
			StringBuffer buf = new StringBuffer();

			ProductLabeling pl = new ProductLabeling((FDUserI) pageContext.getSession().getAttribute(SessionName.USER), product, hideBursts);
			
			if (browserInfo == null)
				browserInfo = new BrowserInfo((HttpServletRequest) pageContext.getRequest());


			final boolean needsOpacityWorkaround = browserInfo.isInternetExplorer() && browserInfo.getVersionNumber() < 8.0;
			
			// IE workaround
			if (this.opacity == 1 && needsOpacityWorkaround)
				this.opacity = 0.999;

			final boolean supportsPNG = !(opacity < 1 && browserInfo.isInternetExplorer() && browserInfo.getVersionNumber() < 8.0);
			// not disabled, has action and not in cart (savings) -> add link
			final boolean shouldGenerateAction = !this.disabled && this.action != null && !this.isInCart;

			



			String imageStyle = "border: 0; ";
			
			if (opacity < 1) {
				imageStyle += TransparentBoxTag.getOpacityStyle(browserInfo, opacity);
			}


			buf.append("<div style=\"padding: 0px; border: 0px; margin: 0px auto; "
					+ "width: " + prodImg.getWidth() + "px; "
					+ "height: " + prodImg.getHeight() + "px; "
					+ "position: relative;\">\n");

			
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
				buf.append("<a href=\"");
				buf.append(action);
				buf.append("\">");
			}

			buf.append("<img src=\"");
			if (this.prefix != null)
				buf.append(this.prefix);
			buf.append(prodImg.getPath());
			buf.append("\"");
			
			buf.append(" width=\"");
			buf.append(prodImg.getWidth());
			buf.append("\"");
			
			buf.append(" height=\"");
			buf.append(prodImg.getHeight());
			buf.append("\"");

			buf.append(" alt=\"");
			buf.append(product.getFullName());
			buf.append("\"");
			
			if (className != null && className.length() > 0) {
				buf.append(" class=\"");
				buf.append(className);
				buf.append("\"");
			}
			
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



			appendBurst(buf, pl, supportsPNG, shouldGenerateAction);



			buf.append("</div>\n");

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
	private void appendBurst(StringBuffer buf, ProductLabeling pl, final boolean supportsPNG, final boolean shouldGenerateAction) {
		
		// burst image
		final boolean displayBurst = savingsPercentage > 0 || pl.isDisplayAny();
		if (displayBurst) {
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


			buf.append("<div style=\"position: absolute; top: 0px; left: 0px\">\n");
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
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}