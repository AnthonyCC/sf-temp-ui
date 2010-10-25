package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.content.EnumWinePrice;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupport;

/**
 * Product Rating Tag
 * 
 * @author cssomogyi
 * 
 */
public class WinePriceTag extends BodyTagSupport {
	private static final long serialVersionUID = 1346658288638801654L;

	ProductModel product;
	String action;

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel prd) {
		this.product = prd;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int doStartTag() throws JspException {
		try {
			EnumWinePrice price = EnumWinePrice.getEnumByPrice(product.getPrice(0.));

			JspWriter out = pageContext.getOut();

			StringBuffer buf = new StringBuffer();

			if (action != null) {
				buf.append("<a href=\"");
				buf.append(action);
				buf.append("\" class=\"wine-price text12\">");
			} else {
				buf.append("<span class=\"wine-price text12\">");
			}
			
			buf.append('-');
			
			for (int i = 0; i < price.getDollarCount(); i++)
				buf.append('$');
			
			buf.append('-');

			if (action != null) {
				buf.append("</a>");
			} else {
				buf.append("</span>");
			}

			out.println(buf.toString());
		} catch (IOException e) {
			throw new JspException("I/O error", e);
		}

		return SKIP_BODY;
	}
}
