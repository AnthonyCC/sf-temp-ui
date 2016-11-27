package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class ProductSavingTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 3647071760397727769L;

	private final static NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
	private final static DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat("0.##");

	public static String getGroupPrice(FDGroup group, ZoneInfo pricingZone) throws FDResourceException {
		StringBuffer buf = new StringBuffer();
		MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(group, pricingZone);
        GroupScalePricing grpPricing = GroupScaleUtil.lookupGroupPricing(group);
        
		if (matPrice != null) {
			String grpQty = "0";
			String grpTotalPrice = "0";
			boolean isSaleUnitDiff = false;
			double displayPrice = 0.0;
			if (matPrice.getPricingUnit().equals(matPrice.getScaleUnit()))
				displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
			else {
				displayPrice = matPrice.getPrice();
				isSaleUnitDiff = true;
			}
			grpQty = FORMAT_QUANTITY.format(matPrice.getScaleLowerBound());
			// Other than eaches append the /pricing unit for clarity.
			if (matPrice.getScaleUnit().equals("LB"))
				grpQty = grpQty + (matPrice.getScaleUnit().toLowerCase()) + "s";

			grpTotalPrice = FORMAT_CURRENCY.format(displayPrice);
			if (isSaleUnitDiff)
				grpTotalPrice = grpTotalPrice + "/" + (matPrice.getPricingUnit().toLowerCase());

			buf.append("Any ");
			buf.append(grpQty);

			if(grpPricing != null) {
				buf.append(" " +grpPricing.getShortDesc());				
			}
			buf.append(" for ");
			buf.append(grpTotalPrice);
		}
		return buf.toString();
	}

	private ProductModel product;

	/**
	 * [APPDEV-1283] Exclude 6 and 12 bootles deals
	 */
	private boolean excludeCaseDeals = false;
	
	private boolean excludeGroupSavings = false;

	private boolean excludeDeals = false;

	public void setExcludeCaseDeals(boolean excludeCaseDeals) {
		this.excludeCaseDeals = excludeCaseDeals;
	}

	public boolean isExcludeCaseDeals() {
		return excludeCaseDeals;
	}

	public void setExcludeGroupSavings(boolean excludeGroupSavings) {
		this.excludeGroupSavings = excludeGroupSavings;
	}

	public boolean isExcludeGroupSavings() {
		return excludeGroupSavings;
	}

	public void setExcludeDeals(boolean excludeDeals) {
		this.excludeDeals = excludeDeals;
	}
	
	public boolean isExcludeDeals() {
		return excludeDeals;
	}

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

	}
	
	public String getContent() throws javax.servlet.jsp.JspException {
		try {
			ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
			if (availability != null && !availability.isFullyAvailable())
				return "";

			PriceCalculator price = product.getPriceCalculator();

			FDGroup group = price.getFDGroup();

			StringBuffer buf = new StringBuffer();
			MaterialPrice matPrice = null;
			
			if (group != null) {
				matPrice = GroupScaleUtil.getGroupScalePrice(group, price.getPricingContext().getZoneInfo());
			}

			if (!excludeGroupSavings && matPrice != null) {
				buf.append("<span class=\"mixnmatch\">Mix 'n Match</span>");
			} else {
				String scaleString = price.getTieredPrice(0, excludeCaseDeals ? ProductBurstTag.EXCLUDED_WINE_TIERS : null);
				if (scaleString != null) {
					buf.append("Save! ");
					buf.append(scaleString);
				} else if (price.isOnSale() && !this.isExcludeDeals()) {
					buf.append("Save ");
					buf.append(price.getDealPercentage());
					buf.append("%");
				} else {
					// no sales, do nothing
				}
			}

			int i = 0;
			int state = 0;
			// states
			// 0 - not in word
			// 1 - in word not bolded
			// 2 - in word bolded
			// transitions
			// 0 --> 1
			// 0 --> 2
			// 1 --> 0
			// 2 --> 0
			while (i < buf.length()) {
				char c = buf.charAt(i);
				if (state == 0) {
					if (Character.isWhitespace(c)) {
						state = 0;
					} else if (Character.isDigit(c) || c == '$') {
						buf.insert(i, "<b>");
						i += 3;
						state = 2;
					} else {
						state = 1;
					}
				} else if (state == 1) {
					if (Character.isWhitespace(c)) {
						state = 0;
					} else {
						state = 1;
					}
				} else /* state == 2 */{
					if (Character.isWhitespace(c)) {
						buf.insert(i + 1, "</b>");
						i += 4;
						state = 0;
					} else {
						state = 2;
					}
				}
				i++;
			}
			// wrap up
			if (state == 2) {
				buf.append("</b>");
			}
			
			return buf.toString();
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
