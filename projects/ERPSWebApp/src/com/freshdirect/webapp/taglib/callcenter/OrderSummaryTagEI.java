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
public class OrderSummaryTagEI extends TagExtraInfo {

	/**
	 * Return information about the scripting variables to be created.
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		return new VariableInfo[] {
			new VariableInfo(
				"orderPK", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"total", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"orderStatus", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"deliveryDate", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"createDate", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"lastModifiedDate", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"createBy", "java.lang.String",
				true, VariableInfo.NESTED),

            new VariableInfo(
                "lastModifiedBy", "java.lang.String",
                true, VariableInfo.NESTED),

			new VariableInfo(
				"createSource", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"lastModifiedSource", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"creditIssued", "java.lang.String",
				true, VariableInfo.NESTED)
		};

	}

}
