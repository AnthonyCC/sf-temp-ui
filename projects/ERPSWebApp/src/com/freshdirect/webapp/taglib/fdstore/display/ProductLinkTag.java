package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.util.FDURLUtil;

public class ProductLinkTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -9101409457297626011L;

	private ProductModel product;

	private String trackingCode;

	private boolean appendWineParams = false;

	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		String url;
		if (appendWineParams) {
			url = FDURLUtil.getWineProductURI(product, trackingCode, (Map<String, String[]>) request.getParameterMap());
		} else {
			url = FDURLUtil.getNewProductURI(product);
		}

		try {
			pageContext.getOut().print("<a href=\"" + url + "\">");
		} catch (IOException e) {
			throw new JspException(e);
		}

		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().print("</a>");
		} catch (IOException e) {
			throw new JspException(e);
		}
		return super.doEndTag();
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public void setAppendWineParams(boolean appendWineParams) {
		this.appendWineParams = appendWineParams;
	}
}
