/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.taglib.fdstore.display.ProductAvailabilityTag;

/**
 * 
 * @version $Revision$
 * @author $Author$
 */
public class ProductCartStatusTag extends BodyTagSupport implements SessionName {
	private static final long serialVersionUID = 8469716879872710486L;

	private final static Logger LOGGER = LoggerFactory.getInstance(ProductCartStatusTag.class);

	private ProductModel product;

	public void setProduct(ProductModel product) {
		this.product = product;
	}

	@Override
	public int doStartTag() throws JspException {
		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
		if (availability != null && !availability.isFullyAvailable())
			return SKIP_BODY;

		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(USER);
		FDCartModel shoppingCart = user.getShoppingCart();
		if (shoppingCart != null) {
			int quantity = 0;
			List<FDCartLineI> orderLines = shoppingCart.getOrderLines();
			for (FDCartLineI orderLine : orderLines) {
				String skuCode = orderLine.getSkuCode();
				
				boolean found = false;
				List<String> skuCodes = product.getSkuCodes();
				for (String itemSku : skuCodes) {
					if (itemSku.equals(skuCode)) {
						found = true;
						break;
					}
				}
					
				if (!found)
					continue;
				
				if (orderLine.isSoldBySalesUnits()) {
					quantity++;
				} else {
					quantity += orderLine.getQuantity();
				}
			}
			try {
				if (quantity != 0)
					pageContext.getOut().print("in-cart");
			} catch (IOException e) {
				throw new JspException(e);
			}
		} else {
			LOGGER.error("user does not have cart: " + user.getIdentity());
		}
		return SKIP_BODY;
	}
}
