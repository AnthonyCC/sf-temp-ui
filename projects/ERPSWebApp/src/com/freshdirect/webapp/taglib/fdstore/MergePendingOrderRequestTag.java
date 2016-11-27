package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class MergePendingOrderRequestTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 3738435294425402302L;

	private FDUserI user;

	/*
	 * Status codes:
	 * 0 - OK (everything processed properly)
	 * 1 - Not logged in (session timed out)
	 * 2 - Invalid request (pendingOrderId is null)
	 * 3 - No such pending order (maybe cut-off passed)
	 * 4 - Unknown error
	 * 
	 */
	@Override
	public int doStartTag() throws JspException {
		FDUserI user = getFDUser();
		
		int status = 4;
		if (user == null) {
			status = 1;
		} else {
			String pendingOrderId = request.getParameter("pendingOrderId");
			if (pendingOrderId == null) {
				status = 2;
			} else {
				try {
					List<FDOrderInfoI> pendingOrders = user.getPendingOrders();
					FDOrderInfoI pendingOrder = null;
					for (FDOrderInfoI order : pendingOrders)
						if (order.getErpSalesId().equals(pendingOrderId)) {
							pendingOrder = order;
							break;
						}
					if (pendingOrder == null) {
						status = 3;
					} else {
						List<String> cartlineIds = new ArrayList<String>();
						@SuppressWarnings("unchecked")
						Enumeration<String> parameterNames = request.getParameterNames();
						while (parameterNames.hasMoreElements()) {
							String parameterName = parameterNames.nextElement();
							if (parameterName.startsWith("userCLID_")) {
								String cartlineId = parameterName.substring("userCLID_".length());
								cartlineIds.add(cartlineId);
							}
						}
						
						// merge existing cartlines into the pending cartline
						FDCartModel mergePendCart = user.getMergePendCart();
						FDCartModel shoppingCart = user.getShoppingCart();
						List<FDCartLineI> shoppingCartLines = shoppingCart.getOrderLines();
						List<FDCartLineI> mergedLines = new ArrayList<FDCartLineI>();
						for (FDCartLineI cartline : shoppingCartLines) {
							if (cartlineIds.contains(cartline.getCartlineId()))
								mergedLines.add(cartline);
						}
						
						if (!mergedLines.isEmpty()) {
							mergePendCart.addOrderLines(mergedLines);
							mergePendCart.sortOrderLines();
							// sure what is sure:
							mergePendCart.setUserContextToOrderLines(user.getUserContext());

							for (FDCartLineI cartline : mergedLines) {
								shoppingCart.removeOrderLineById(cartline.getRandomId()); 
							}
							shoppingCart.sortOrderLines();
							shoppingCart.setUserContextToOrderLines(user.getUserContext());
							user.setShoppingCart(shoppingCart);
						}

						user.setMergePendCart(mergePendCart);
						status = 0; // everything went ok so far
					}
				} catch (FDResourceException e) {
					status = 4;
				}
			}
		}
		
		JspWriter out = pageContext.getOut();
		try {
			out.append(Integer.toString(status));
		} catch (IOException e) {
			throw new JspException();
			// cannot happen
		}
		
		return SKIP_BODY;
	}
	
	public FDUserI getFDUser() {
		if (user == null) {
			user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		}
		return user;
	}	
}
