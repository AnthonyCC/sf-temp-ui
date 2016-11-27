/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.jsp.tagext.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class OrderCountsTagEI extends TagExtraInfo {

	/**
	 * Return information about the scripting variables to be created.
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		return new VariableInfo[] {
			new VariableInfo("totalOrders",
			   "java.lang.Integer",
			   true,
			   VariableInfo.NESTED),
			new VariableInfo("webOrders",
			   "java.lang.Integer",
			   true,
			   VariableInfo.NESTED),
			new VariableInfo("phoneOrders",
			   "java.lang.Integer",
			   true,
			   VariableInfo.NESTED)
		};

	}

}
