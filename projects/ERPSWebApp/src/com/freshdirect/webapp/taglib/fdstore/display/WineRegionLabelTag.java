package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupport;

/**
 * Displays wine region in "country >> region >> city" form
 * 
 * @author segabor
 * 
 */
public class WineRegionLabelTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;

	public static final String SEP_DEFAULT = "&rsaquo;";
	public static final String SEP_QUICKBUY = "&gt;";

	private ProductModel product;
	private String separator = SEP_DEFAULT;
	private boolean excludeCountry = false;
	private boolean excludeVintage = false;

	
	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setExcludeCountry(boolean excludeCountry) {
		this.excludeCountry = excludeCountry;
	}

	public void setExcludeVintage(boolean excludeVintage) {
		this.excludeVintage = excludeVintage;
	}
	
	@Override
	public int doStartTag() throws JspException {
		StringBuilder buf = new StringBuilder();

		DomainValue wineCountry = product.getWineCountry();
		List<DomainValue> wineRegion = product.getNewWineRegion();
		String wineCity = product.getWineCity();
		if (wineCity.trim().length() == 0)
			wineCity = null;
		List<DomainValue> wineVintageList = product.getWineVintage();
		DomainValue wineVintage = wineVintageList.size() > 0 ? wineVintageList.get(0) : null;
		if (wineVintage != null && "vintage_nv".equals(wineVintage.getContentKey().getId()))
			wineVintage = null;

		if (!excludeCountry) {
			// exclude country from quickbuy panel
			//
			if (wineCountry != null)
				buf.append(wineCountry.getLabel());

			if (wineCountry != null && !wineRegion.isEmpty()) {
				buf.append(' ');
				buf.append(separator);
				buf.append(' ');
			}
		}

		if (!wineRegion.isEmpty())
			buf.append(wineRegion.get(0).getLabel());

		if (((wineCountry != null && !excludeCountry) || !wineRegion.isEmpty()) && wineCity != null) {
			buf.append(' ');
			buf.append(separator);
			buf.append(' ');
		}

		if (wineCity != null)
			buf.append(wineCity);

		final boolean showVintage = !excludeVintage && wineVintage != null;

		if ((wineCountry != null || !wineRegion.isEmpty() || wineCity != null) && showVintage)
			buf.append(", ");

		if (showVintage)
			buf.append(wineVintage.getLabel());

		try {
			pageContext.getOut().write(buf.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}
}
