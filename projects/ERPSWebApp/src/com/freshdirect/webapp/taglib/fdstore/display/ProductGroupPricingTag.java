package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class ProductGroupPricingTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 5597527713397681158L;

	private ProductModel product;

	public int doStartTag() throws javax.servlet.jsp.JspException {
		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
		if (availability != null && !availability.isFullyAvailable())
			return SKIP_BODY;

		String label = getContent();
		
		if (!"".equals(label)) {
			JspWriter out = pageContext.getOut();
			try {
				out.append(label);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}

		return SKIP_BODY;
	}
	
	public String getContent() throws javax.servlet.jsp.JspException {
		try {
			ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
			if (availability != null && !availability.isFullyAvailable())
				return "";

			PriceCalculator price = product.getPriceCalculator();

			FDGroup group = price.getFDGroup();

			String label = null;
			if (group != null)
				label = ProductSavingTag.getGroupPrice(group, price.getPricingContext().getZoneInfo());

			if (label != null && !label.isEmpty()) {
				return label;
			}

			return "";

		} catch (FDResourceException e) {
			throw new JspException(e);
		}
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}
}
