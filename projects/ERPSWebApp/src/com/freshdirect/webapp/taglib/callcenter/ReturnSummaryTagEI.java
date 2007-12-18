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
public class ReturnSummaryTagEI extends TagExtraInfo {

	/**
	 * Return information about the scripting variables to be created.
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		return new VariableInfo[] {
			new VariableInfo("perishablesValue",
			   "java.lang.String",
			   true,
			   VariableInfo.NESTED),
			new VariableInfo("nonPerishablesRefund",
			   "java.lang.String",
			   true,
			   VariableInfo.NESTED),
			new VariableInfo("restockingCharges",
			   "java.lang.String",
			   true,
			   VariableInfo.NESTED)
		};

	}

}
