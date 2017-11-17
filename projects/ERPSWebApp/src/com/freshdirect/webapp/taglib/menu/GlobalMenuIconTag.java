package com.freshdirect.webapp.taglib.menu;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.ProductContainer;

/**
 * Displays product container image for Global Menu Item.
 * 
 * @author tgelesz
 * 
**/
public class GlobalMenuIconTag extends SimpleTagSupport {
	
	private static final Logger LOGGER = LoggerFactory.getInstance( GlobalMenuIconTag.class );
	
	private ProductContainer productContainer;
	private boolean large;

	
	@Override
	public void doTag() throws JspException, IOException {
		try {
			Image photo = productContainer.getPhoto(); 
			
			if ( photo == null ) {
				LOGGER.warn( "Missing productContainer image: " + productContainer.getContentKey() );
				return;
			}
			
			JspWriter out = getJspContext().getOut();
			
			StringBuffer buf = new StringBuffer();
			
//			buf.append("<div style=\"padding: 0px; border: 0px; margin: 0px auto; "
//					+ "width: " + catImg.getWidth() + "px; "
//					+ "height: " + catImg.getHeight() + "px; "
//					+ "position: relative;\">");

			double ratio = large ? 1 : 40d / photo.getWidth();
			
			String imageName = "ro_img_" + productContainer.getContentName();
			

			buf.append("<img src=\"");
			buf.append(photo.getPathWithPublishId());
			buf.append("\"");
			
			buf.append(" width=\"");
			buf.append((int)(photo.getWidth() * ratio));
			buf.append("\"");
			
			buf.append(" height=\"");
			buf.append((int)(photo.getHeight() * ratio));
			buf.append("\"");

			buf.append(" alt=\"");
			buf.append(productContainer.getFullName());
			buf.append("\"");
			
			buf.append( " name=\"" );
			buf.append( imageName );
			buf.append( "\"" );
			
			buf.append(">");

		
//			buf.append("</div>");

			out.println(buf.toString());
		} catch (IOException e) {
		}
	
	
	}


	public ProductContainer getProductContainer() {
		return productContainer;
	}


	public void setProductContainer(ProductContainer productContainer) {
		this.productContainer = productContainer;
	}


	public boolean isLarge() {
		return large;
	}


	public void setLarge(boolean large) {
		this.large = large;
	}
	
}
