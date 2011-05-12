package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class ProductDefaultPriceTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 5575296457559031765L;

	private ProductModel product;
	
	private PriceCalculator price;

	public int doStartTag() throws javax.servlet.jsp.JspException {
	        if (price == null) {
	            if (product == null) {
	                throw new RuntimeException("'priceCalculator' or 'product' is mandatory!");
	            }
	            price = product.getPriceCalculator();
	        }

		NumberFormat format = NumberFormat.getCurrencyInstance();

		StringBuilder buf = new StringBuilder();
		buf.append("<span class=\"");
		if (price.isOnSale())
			buf.append("save-price");
		else
			buf.append("normal-price");
		buf.append("\">");
		buf.append(format.format(price.getDefaultPriceValue()));
		buf.append("</span>");

		JspWriter out = pageContext.getOut();
		try {
			out.append(buf);
		} catch (IOException e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	};

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}
	
	public void setPriceCalculator(PriceCalculator price) {
            this.price = price;
        }
}
