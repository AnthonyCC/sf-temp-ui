package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;

public class ProductWasPriceTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -6134261340099468094L;

	private ProductModel product;

	private PriceCalculator price;

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
	
	public StringBuilder getContent() throws javax.servlet.jsp.JspException {

		StringBuilder buf = new StringBuilder();
		
		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
		if (availability != null && !availability.isFullyAvailable())
			return buf;

		if (price == null) {
			if (product == null) {
				throw new RuntimeException("'priceCalculator' or 'product' is mandatory!");
			}
			price = product.getPriceCalculator();
		}

		NumberFormat format = NumberFormat.getCurrencyInstance();
		
		if (price.isOnSale()) {
			buf.append(format.format(price.getWasPrice()));
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
