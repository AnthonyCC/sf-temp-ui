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
public class AlternateAddressInfoTagEI extends TagExtraInfo {

	/**
	 * Return information about the scripting variables to be created.
	 */
	public VariableInfo[] getVariableInfo(TagData data) {

		return new VariableInfo[] {
			new VariableInfo(
				"fldAltFirstName", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"fldAltLastName", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"fldAltApartment", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"fldAltPhone", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"fldDlvInstructions", "java.lang.String",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"leaveWithDoorman", "java.lang.Boolean",
				true, VariableInfo.NESTED),

			new VariableInfo(
				"leaveWithNeighbor", "java.lang.Boolean",
				true, VariableInfo.NESTED)
		};

	}

}
