package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.util.ConfigurationUtil;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

public class ProductPriceDescriptionTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 5575296457559031765L;

	private ProductImpression impression;
	
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
	
	public StringBuilder getContent() {

		StringBuilder buf = new StringBuilder();

        String confDescription = null;
		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);

		if (availability != null && !availability.isFullyAvailable())
			return buf;

        PriceCalculator priceCalculator = impression.getCalculator();

        if (impression instanceof TransactionalProductImpression) {
            TransactionalProductImpression tpi = (TransactionalProductImpression) impression;
            confDescription = ConfigurationUtil.getConfigurationDescription(tpi);
        }

        if (confDescription == null) {
            try {
                confDescription = priceCalculator.getSizeDescription();
            } catch (FDResourceException e1) {
            }
        }
		
		buf.append("<span class=\"price-description\">");
		
        if(confDescription != null) {
    		buf.append(confDescription);
        }
		buf.append("</span>");

		return buf;
	}

	public void setImpression(ProductImpression impression) {
		this.impression = impression;
	}
	
	public ProductImpression getImpression() {
		return impression;
	}
}
