package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.DYFUtil;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.taglib.fdstore.BrowserInfo;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ProductBurstTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 1052411080367169416L;

	private ProductModel product;
	
	private PriceCalculator calculator;

	private boolean large;

	private String className;

	private String style;

	/**
	 * [APPDEV-1283] Exclude 6 and 12 wine bottles deals
	 */
	private boolean excludeCaseDeals = false;

	
	public void setExcludeCaseDeals(boolean excludeCaseDeals) {
		this.excludeCaseDeals = excludeCaseDeals;
	}
	
	public void setPriceCalculator(PriceCalculator calculator) {
            this.calculator = calculator;
        }

        PriceCalculator getCalculator() {
            if (calculator == null) {
                calculator = product.getPriceCalculator();
            }
            return calculator;
        }
	
	@Override
	public int doStartTag() throws JspException {
		if (!renderPrecondition()) {
			return SKIP_BODY;
		}
		if (product == null) {
		    if (calculator == null) {
                        throw new RuntimeException("'priceCalculator' or 'product' is mandatory!");
		    }
		    product = calculator.getProductModel();
		}
		
		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		StringBuilder buf = new StringBuilder();
		int highestDeal = getCalculator().getHighestDealPercentage();
		if (highestDeal < FDStoreProperties.getBurstsLowerLimit() || highestDeal > FDStoreProperties.getBurstUpperLimit())
			highestDeal = 0;

		BrowserInfo browser = new BrowserInfo(request);
		boolean supportsPng = !browser.isInternetExplorer();
		String lgsm = large ? "lg" : "sm";
		int size = large ? 55 : 35;

		boolean render = false;
		buf.append("<img src=\"");
		
		if (DYFUtil.isFavorite(product, user)) {
			buf.append("/media_stat/images/bursts/brst_");
			buf.append(lgsm);
			buf.append("_fave");
			render = true;
		} else if (highestDeal > 0) {
			buf.append("/media_stat/images/deals/brst_");
			buf.append(lgsm);
			buf.append('_');
			buf.append(highestDeal);
			render = true;
		} else if (product.isBackInStock()) {
			buf.append("/media_stat/images/bursts/brst_");
			buf.append(lgsm);
			buf.append("_bis");
			render = true;
		} else if (product.isNew()) {
			buf.append("/media_stat/images/bursts/brst_");
			buf.append(lgsm);
			buf.append("_new");
			render = true;
		}

		if (render) {
			buf.append('.');
			buf.append(supportsPng ? "png" : "gif");
			buf.append("\" width=\"");
			buf.append(size);
			buf.append("\" height=\"");
			buf.append(size);
			buf.append("\"");
	
			if (className != null) {
				buf.append(" class=\"");
				buf.append(StringEscapeUtils.escapeHtml(className));
				buf.append("\"");
			}
	
			if (style != null) {
				buf.append(" style=\"");
				buf.append(StringEscapeUtils.escapeHtml(style));
				buf.append("\"");
			}
	
			buf.append('>');
		}

		if (!render)
			return SKIP_BODY;

		try {
			pageContext.getOut().append(buf);
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

	public boolean isLarge() {
		return large;
	}

	public void setLarge(boolean large) {
		this.large = large;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}



	/**
	 * [APPDEV-1283] Exclude case deals
	 */
	public static double[] EXCLUDED_WINE_TIERS = new double[]{6, 12 };

	
	private boolean renderPrecondition() {
		if (!excludeCaseDeals || getCalculator().getHighestDealPercentage() > getCalculator().getTieredDealPercentage())
			return true;
		
		return getCalculator().getTieredPrice(0, EXCLUDED_WINE_TIERS) != null;
	}
}
