package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class ProductWasPriceTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -6134261340099468094L;

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

		if (price.isOnSale()) {
			StringBuilder buf = new StringBuilder();
			buf.append(format.format(price.getWasPrice()));

			JspWriter out = pageContext.getOut();
			try {
				out.append(buf);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}

		return SKIP_BODY;
	};

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
