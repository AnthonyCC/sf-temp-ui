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

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.display.ProductAvailabilityTag;

/**
 * 
 * @version $Revision$
 * @author $Author$
 */
public class ProductCartStatusMessageTag extends BodyTagSupport implements SessionName {
	private static final long serialVersionUID = 8469716879872710487L;

	private final static Logger LOGGER = LoggerFactory.getInstance(ProductCartStatusMessageTag.class);

	private ProductModel product;
	private FDUserI fdUser = null;

	public void setProduct(ProductModel product) {
		this.product = product;
	}
		
	public void setFDUser(FDUserI fdUser) {
		this.fdUser = fdUser;
	}

	@Override
	public int doStartTag() throws JspException {
		ProductAvailabilityTag availability = (ProductAvailabilityTag) findAncestorWithClass(this, ProductAvailabilityTag.class);
		if (availability != null && !availability.isFullyAvailable())
			return SKIP_BODY;

		HttpSession session = pageContext.getSession();
		this.setFDUser((FDUserI) session.getAttribute(USER));
		
		try {
			pageContext.getOut().print(getContent());
		} catch (IOException e) {
			throw new JspException(e);
		}
		
		return SKIP_BODY;
	}
	
	public String getContent() {

		StringBuilder buf = new StringBuilder();
		
		if (this.fdUser != null && this.product != null) {
	
			FDCartModel shoppingCart = this.fdUser.getShoppingCart();
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
				if (quantity != 0) {
					buf.append(Integer.toString(quantity)+" in cart");
				}
			} else {
				LOGGER.error("user does not have cart: " + this.fdUser.getIdentity());
			}
		} else {
			if (this.fdUser == null) {
				LOGGER.error("Required fdUser cannot be null.");
			}
			if (this.product == null) {
				LOGGER.error("Required product cannot be null.");
			}
		}
		
		return buf.toString();
	}
	
}
