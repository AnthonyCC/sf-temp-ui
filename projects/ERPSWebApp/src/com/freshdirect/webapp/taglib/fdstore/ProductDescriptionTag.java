package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.ConfigurationUtil;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

public class ProductDescriptionTag extends BodyTagSupport {
	private static final long serialVersionUID = 5175263850081406757L;

	private ProductImpression impression;

	double savingsPercentage = 0 ; // savings % off
	
	public void setImpression(ProductImpression impression) {
		this.impression = impression;
	}
	


	public void setSavingsPercentage(double savingsPercentage) {
		this.savingsPercentage = savingsPercentage;
	}



	public int doStartTag() {
		JspWriter out = pageContext.getOut();

		StringBuffer buf = new StringBuffer();

		String confDescription = null;

		if (impression instanceof TransactionalProductImpression) {
			TransactionalProductImpression tpi = (TransactionalProductImpression)impression;
			confDescription = ConfigurationUtil.getConfigurationDescription(tpi);
		}

		if (confDescription == null) {
			try {
				confDescription = impression.getProductModel().getSizeDescription();
			} catch (FDResourceException e1) {}
		}

		// display description
		if (confDescription != null) {
			buf.append("<div>" + confDescription + "</div>\n");
		}

		// display price
		FDProductInfo productInfo = impression.getProductInfo();
		if (productInfo != null) {
			if (savingsPercentage > 0) {
				// smart savings price
				double savingsPrice = productInfo.getDefaultPrice() * (1-savingsPercentage);
				buf.append(
						"<div style=\"font-weight: bold; color: red;\">" + 
						JspMethods.currencyFormatter.format(savingsPrice) + "/" + 
						productInfo.getDisplayableDefaultPriceUnit().toLowerCase() + 
						"</div>\n" +
						"<div style=\"font-weight: normal; color: gray\">" + 
						"(was " + JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()) + ")" +
						"</div>\n"
				);
			} else if ( productInfo.isDeal() ) {
				// deal price
				buf.append(
						"<div style=\"font-weight: bold; color: red;\">" + 
						JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()) + "/" + 
						productInfo.getDisplayableDefaultPriceUnit().toLowerCase() + 
						"</div>\n" +
						"<div style=\"font-weight: normal; color: gray\">" + 
						"(was " + JspMethods.currencyFormatter.format(productInfo.getBasePrice()) + ")" +
						"</div>\n"
				);
			} else {
				buf.append(
						"<div style=\"font-weight: bold;\">" + 
						JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()) + "/" + 
						productInfo.getDisplayableDefaultPriceUnit().toLowerCase() + 
						"</div>\n"
				);
			}
		}

		
		// Display "SAVE!" ... label
        FDProduct product = impression.getFDProduct();
        if (product!=null) {
        	String[] ymalScales = null;
        	if (savingsPercentage > 0) 
        		ymalScales = product.getPricing().getScaleDisplay(savingsPercentage);
        	else
        		ymalScales = product.getPricing().getScaleDisplay();
            if (ymalScales.length>0) {
            	buf.append("<div style=\"color: #FF9933; font-weight: bold;\">Save!</div>\n");
                for (int ymalSci = 0; ymalSci < ymalScales.length; ymalSci++) {
        			buf.append("<div style=\"font-weight: bold;\">" + ymalScales[ymalSci] + "</div>\n");
                }
            }
        }

		try {
			// write out
			out.write(buf.toString());
		} catch (IOException e) {
		}


		return EVAL_BODY_INCLUDE;
	}


	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}
