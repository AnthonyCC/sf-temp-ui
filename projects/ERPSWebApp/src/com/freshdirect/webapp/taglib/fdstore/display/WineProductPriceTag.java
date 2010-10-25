package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.util.JspMethods;

public class WineProductPriceTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 3647071760397727769L;

	private ProductModel product;

	/**
	 * [APPDEV-1283] Exclude 6 and 12 bootles deals
	 */
	private boolean excludeCaseDeals = true;

	private static double excludedTiers[] = new double[] { 6, 12 };

	public void setExcludeCaseDeals(boolean excludeCaseDeals) {
		this.excludeCaseDeals = excludeCaseDeals;
	}

	public int doStartTag() throws javax.servlet.jsp.JspException {
		try {
			// FIXME: savings % in handled yet
			double savingsPercentage = 0;
			PriceCalculator price = product.getPriceCalculator();

			NumberFormat format = NumberFormat.getCurrencyInstance();

			String scaleString = price.getTieredPrice(savingsPercentage, excludeCaseDeals ? excludedTiers : null);

			FDProduct fdProduct = price.getProduct();
			String priceUnit = null;
			if (fdProduct != null) {
				FDSalesUnit[] salesUnits = fdProduct.getSalesUnits();
				if (salesUnits != null && salesUnits.length > 0)
					priceUnit = salesUnits[0].getDescription();
			}

			StringBuilder buf = new StringBuilder();
			if (price.isOnSale()) {
				buf.append("<span class=\"save-price\">");
				buf.append(format.format(price.getDefaultPriceValue()));
				if (priceUnit != null) {
					buf.append("<span style=\"font-size: 80%;\">");
					buf.append("/");
					buf.append(priceUnit);
					buf.append("</span>");
				}
				buf.append("</span>");

				if (scaleString != null) {
					buf.append("<span class=\"save-price-tiered\">");
					buf.append(scaleString);
					buf.append("</span>");
				}

				buf.append("<span class=\"save-base-price\">");
				buf.append(" &ndash; was ");
				buf.append(format.format(price.getWasPrice()));
				buf.append("</span>");
			} else {
				buf.append("<span class=\"normal-price\">");
				buf.append(format.format(price.getDefaultPriceValue()));
				if (priceUnit != null) {
					buf.append("<span style=\"font-size: 80%;\">");
					buf.append("/");
					buf.append(priceUnit);
					buf.append("</span>");
				}
				buf.append("</span>");

				if (scaleString != null) {
					buf.append("<span class=\"save-price-tiered\" style=\"padding-left: 0.5em;\">(");
					buf.append(scaleString);
					buf.append(")</span>");
				}
			}

			JspWriter out = pageContext.getOut();
			try {
				out.append(buf);
			} catch (IOException e) {
				throw new JspException(e);
			}

			return SKIP_BODY;

		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (FDSkuNotFoundException e) {
			throw new JspException(e);
		}
	};

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}
}
