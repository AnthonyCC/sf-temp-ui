package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.ProductImpression;

public class ProductGroupLinkTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -6697632626950894801L;

	private ProductImpression impression;

	private String trackingCode;

	private String url = null;

	@Override
	public int doStartTag() throws JspException {
		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
		if (availability == null || availability.isFullyAvailable())
			url = FDURLUtil.getProductGroupURI(impression, trackingCode);

		if (url != null)
			try {
				pageContext.getOut().print("<a class=\"product-group-price-link\" href=\"" + url + "\">");
			} catch (IOException e) {
				throw new JspException(e);
			}

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		if (url != null)
			try {
				pageContext.getOut().print("</a>");
			} catch (IOException e) {
				throw new JspException(e);
			}
		return super.doEndTag();
	}

	public ProductImpression getImpression() {
		return impression;
	}

	public void setImpression(ProductImpression impression) {
		this.impression = impression;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}
}
