/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ReturnSummaryTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance( ReturnSummaryTag.class );

	private FDOrderI order = null;

	public void setOrder(FDOrderI o) {
		this.order = o;
	}

	public int doStartTag() throws JspException {

		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance( Locale.US );
		HttpSession session = pageContext.getSession();

		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		FDIdentity identity  = user.getIdentity();

		double perishablesValue = 0;
		double nonPerishablesValue = 0;
		double restockingFeeValue = 0;

		try {
			//
			// Number of previous returns determines how we calculate an order's return value
			//
			int numPreviousReturns = 0;
			Collection orderHistoryInfo = FDCustomerManager.getOrderHistoryInfo(identity).getFDOrderInfos();
			for (Iterator it = orderHistoryInfo.iterator(); it.hasNext(); ) {
				FDOrderInfoI orderInfo = (FDOrderInfoI) it.next();
				if ( EnumSaleStatus.RETURNED.equals(orderInfo.getOrderStatus()) )
					numPreviousReturns++;
			}

			LOGGER.debug("Found " + numPreviousReturns + " previous returns by this user.");

			if (numPreviousReturns == 0) {
				//
				// FIRST EVER RETURN BY THIS CUSTOMER
				//
				perishablesValue = 0;
				nonPerishablesValue = order.getTotal();
				restockingFeeValue = 0;
			} else if (numPreviousReturns == 1) {
				//
				// SECOND OR THIRD RETURN BY THIS CUSTOMER
				//
				for (Iterator it = order.getOrderLines().iterator(); it.hasNext(); ) {
					FDCartLineI line = (FDCartLineI) it.next();
					if (line.isPerishable()) {
						perishablesValue += line.getPrice();
					} else {
						nonPerishablesValue += line.getPrice();
					}
				} 
				restockingFeeValue = nonPerishablesValue * .25;
				nonPerishablesValue *= .75;
			} else {
				//
				// EVERY SUBSEQUENT RETURN BY THIS CUSTOMER
				//
				perishablesValue = order.getTotal();
				nonPerishablesValue = 0;
				restockingFeeValue = perishablesValue * .25;
			}
		} catch (FDResourceException ex) {
			LOGGER.warn("FDResourceException while getting order history", ex);
		}
		pageContext.setAttribute("perishablesValue", currencyFormatter.format(perishablesValue));
		pageContext.setAttribute("nonPerishablesRefund", currencyFormatter.format(nonPerishablesValue));
		pageContext.setAttribute("restockingCharges", currencyFormatter.format(restockingFeeValue));

		return EVAL_BODY_BUFFERED;

	}


}
