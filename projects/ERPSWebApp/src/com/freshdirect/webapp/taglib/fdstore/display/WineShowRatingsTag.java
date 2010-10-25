package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class WineShowRatingsTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 6069948866473640124L;

	private ProductModel product;
	private boolean hideOnly = false;

	private boolean globalFlag;
	private boolean productFlag;

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public boolean isHideOnly() {
		return hideOnly;
	}

	public void setHideOnly(boolean hideOnly) {
		this.hideOnly = hideOnly;
	}

	@Override
	public int doStartTag() throws JspException {
		globalFlag = FDStoreProperties.isWineShowRatings();
		productFlag = !product.isHideWineRatingPricing();

		if (globalFlag && productFlag) {
			return EVAL_BODY_INCLUDE;
		} else {
			if (hideOnly) {
				try {
					JspWriter out = pageContext.getOut();
					out.print("<div style=\"visibility: hidden;\">");
				} catch (IOException e) {
					throw new JspException(e);
				}
				return EVAL_BODY_INCLUDE;
			} else {
				return SKIP_BODY;
			}
		}
	}
	
	@Override
	public int doEndTag() throws JspException {
		if (!(globalFlag && productFlag) && hideOnly) {
			try {
				JspWriter out = pageContext.getOut();
				out.print("</div>");
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		return super.doEndTag();
	}
}
