/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class OrderHistoryInfoTag extends AbstractGetterTag {

	/** Sorts orders by dlv. start time, descending */
	private final static Comparator ORDER_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((FDOrderInfoI) o2).getRequestedDate().compareTo(((FDOrderInfoI) o1).getRequestedDate());
		}
	};

	protected Object getResult() throws FDResourceException {

		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

		FDOrderHistory history = user.getOrderHistory();
		List orderHistoryInfo = new ArrayList(history.getFDOrderInfos());

		Collections.sort(orderHistoryInfo, ORDER_COMPARATOR);

		return orderHistoryInfo;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}
