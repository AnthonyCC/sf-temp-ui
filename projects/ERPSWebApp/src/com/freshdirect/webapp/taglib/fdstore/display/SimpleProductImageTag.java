package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.ProductModel;

public class SimpleProductImageTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 9221482591997718613L;

	private ProductModel product;

	private String size; // regular (default), detail, zoom, feature

	@Override
	public int doStartTag() throws JspException {
		String src = null;
		if (size.startsWith("feature")) {
			Image image = product.getFeatureImage();
			if (image != null)
				src = image.getPathWithPublishId();
			else
				src = "/media/images/temp/soon_f.gif";
		} else if (size.startsWith("detail")) {
			Image image = product.getDetailImage();
			if (image != null)
				src = image.getPathWithPublishId();
			else
				src = "/media/images/temp/soon_p.gif";
		} else if (size.startsWith("zoom")) {
			Image image = product.getZoomImage();
			if (image != null)
				src = image.getPathWithPublishId();
			else
				src = "/media/images/temp/soon_z.gif";
		} else {
			// defaulting to regular
			// and unrecognized also defaults to regular
			Image image = product.getProdImage();
			if (image != null)
				src = image.getPathWithPublishId();
			else
				src = "/media/images/temp/soon_c.gif";
		}
		try {
			pageContext.getOut().print("<img src=\"" + StringEscapeUtils.escapeHtml(src) + "\">");
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
