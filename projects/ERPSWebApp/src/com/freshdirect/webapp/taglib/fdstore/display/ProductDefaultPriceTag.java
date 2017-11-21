package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;

public class ProductDefaultPriceTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 5575296457559031765L;

	private ProductModel product;
	
	private PriceCalculator price;
	private boolean showDescription = false;

	public int doStartTag() throws javax.servlet.jsp.JspException {
		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
		if (availability != null && !availability.isFullyAvailable())
			return SKIP_BODY;

		JspWriter out = pageContext.getOut();
		try {
			out.append(getContent());
		} catch (IOException e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	};
	
	public String getContent() {

		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
		if (availability != null && !availability.isFullyAvailable())
			return "";

		if (price == null) {
	            if (product == null) {
	                throw new RuntimeException("'priceCalculator' or 'product' is mandatory!");
	            }
	            price = product.getPriceCalculator();
	        }

		StringBuilder buf = new StringBuilder();
		buf.append("<span class=\"");
		if (price.isOnSale())
			buf.append("save-price");
		else
			buf.append("normal-price");
		buf.append("\">");
		buf.append(price.getPriceFormatted(0));
		buf.append("</span>");

		return buf.toString();
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}
	
	public void setPriceCalculator(PriceCalculator price) {
		this.price = price;
    }
	
	public void setShowDescription(boolean showDescription) {
		this.showDescription = showDescription;
	}

	public boolean isShowDescription() {
		return showDescription;
	}
}
