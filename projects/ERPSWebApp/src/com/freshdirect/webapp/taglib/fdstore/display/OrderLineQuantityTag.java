package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupport;

public class OrderLineQuantityTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;

	private static DecimalFormat quantityFormatter = new DecimalFormat("0.##");

	private ProductModel product;
	private FDCartLineI orderline;
	private FDUserI customer;

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	public void setOrderline(FDCartLineI orderline) {
		this.orderline = orderline;
	}

	public void setCustomer(FDUserI customer) {
		this.customer = customer;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();

		try {
			final FDProduct defaultProduct = product.getDefaultSku()
					.getProduct();
			final boolean isPricedByLB = ("LB".equalsIgnoreCase((defaultProduct
					.getPricing().getZonePrice(
							customer.getPricingContext().getZoneId())
					.getMaterialPrices()[0]).getPricingUnit()));
			final boolean isSoldByLB = isPricedByLB
					&& ("LB"
							.equalsIgnoreCase((defaultProduct.getSalesUnits()[0])
									.getName()));

			final String sell_by = product.getSellBySalesunit();
			final String qtText2nd = product.getQuantityTextSecondary();

			if ((sell_by == null) || "".equals(sell_by)
					|| "QUANTITY".equalsIgnoreCase(sell_by)) {
				// BY QUANTITY
				out.append(quantityFormatter.format(orderline.getQuantity()));
				out.append(" ");

				if (qtText2nd != null) {
					out.append(qtText2nd);
				} else if (isSoldByLB) {
					out.append("lb");
				}
			} else {
				// BY SALES UNIT
				FDSalesUnit fdsu = null;
				for (FDSalesUnit u : product.getSku(orderline.getSkuCode())
						.getProduct().getSalesUnits()) {
					if (u.getName().equals(orderline.getSalesUnit())) {
						fdsu = u;
						break;
					}
				}

				final String suDescr = (fdsu != null) ? fdsu.getDescription()
						: "";
				if ("SALES_UNIT".equals(sell_by)) {
					out.append(suDescr);
				} else if ("BOTH".equals(sell_by)) {
					out.append(quantityFormatter
							.format(orderline.getQuantity()));
					out.append(" ");
					out.append(qtText2nd != null ? qtText2nd : suDescr);
				}
			}
		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (FDSkuNotFoundException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}
