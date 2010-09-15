/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.util.FDEventUtil;

/**
 * 
 * 
 * @version $Revision$
 * @author $Author$
 */
public class AdjustAvailabilityTag extends
		com.freshdirect.framework.webapp.BodyTagSupport {

	private String sourcePage = null;

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}

	public int doStartTag() throws JspException {
		//
		// perform any actions requested by the user if the request was a POST
		//
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		FDCartModel cart = user.getShoppingCart();

		Map unavMap = cart.getUnavailabilityMap();
		for (Iterator i = unavMap.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			Integer key = (Integer) entry.getKey();
			FDAvailabilityInfo info = (FDAvailabilityInfo) entry.getValue();
			FDCartLineI cartline = cart.getOrderLineById(key.intValue());
			int cartIndex = cart.getOrderLineIndex(key.intValue());
			if (info instanceof FDStockAvailabilityInfo) {
				FDStockAvailabilityInfo sInfo = (FDStockAvailabilityInfo) info;

				if (sInfo.getQuantity() == 0) {
					// remove
					cart.removeOrderLine(cartIndex);
					// Create FD remove cart event.
					FDEventUtil.logRemoveCartEvent(cartline, request);

				} else {
					cartline.setQuantity(sInfo.getQuantity());
					// Create FD Modify cart event.
					FDEventUtil.logEditCartEvent(cartline, request);
				}

			} else {
				// remove
				cart.removeOrderLine(cartIndex);
				FDEventUtil.logRemoveCartEvent(cartline, request);
			}
		}
		//Remove unavailable delivery passes.
		List unavailPasses = cart.getUnavailablePasses();
		if(unavailPasses != null && unavailPasses.size() > 0){
			for (Iterator i = unavailPasses.iterator(); i.hasNext();) {
				DlvPassAvailabilityInfo info = (DlvPassAvailabilityInfo)i.next();
				Integer key = info.getKey();
				FDCartLineI cartline = cart.getOrderLineById(key.intValue());
				int cartIndex = cart.getOrderLineIndex(key.intValue());
				cart.removeOrderLine(cartIndex);
				FDEventUtil.logRemoveCartEvent(cartline, request);
			}
			unavailPasses.clear();
		}
		
		try {
			cart.refreshAll();
			//This method retains all product keys that are in the cart in the dcpd promo product info.
			user.getDCPDPromoProductCache().retainAll(cart.getProductKeysForLineItems());
		} catch (FDException e) {
			// !!! improve error handling
			throw new FDRuntimeException(e);
		}
		//Revalidate the cart for deliverypass.
		cart.handleDeliveryPass();
		user.updateUserState();
		
		cart.setAvailability(NullAvailability.AVAILABLE);
		return SKIP_BODY;
	}
}
