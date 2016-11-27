package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class ProductAboutPriceTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -6134261340099468094L;

	private ProductModel product;

	private PriceCalculator price;

	public int doStartTag() throws javax.servlet.jsp.JspException {
		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
        String aboutPriceString = "";

        if (availability != null && !availability.isFullyAvailable()) {
			return SKIP_BODY;
        }
        
		JspWriter out = pageContext.getOut();
		try {
			out.append(getContent());
		} catch (IOException e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}
	
	public StringBuilder getContent() {
		StringBuilder buf = new StringBuilder();

		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
        	String aboutPriceString = "";

        if (availability != null && !availability.isFullyAvailable())
			return buf;

		if (price == null) {
			if (product == null) {
				throw new RuntimeException("'priceCalculator' or 'product' is mandatory!");
			}
			price = product.getPriceCalculator();
		}

        aboutPriceString = price.getAboutPriceFormatted(0);

        if ((null != aboutPriceString) && !"".equals(aboutPriceString)) {
	            buf.append("<span class=\"about-price\">");
	            buf.append(aboutPriceString);
	            buf.append("</span>");
		}
	
		return buf;
	}

	public void setPriceCalculator(PriceCalculator priceCalculator) {
		this.price = priceCalculator;
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}
}
