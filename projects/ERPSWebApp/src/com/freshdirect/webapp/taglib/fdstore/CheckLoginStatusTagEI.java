/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.tagext.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class CheckLoginStatusTagEI extends TagExtraInfo {

	/**
	 * Return information about the scripting variables to be created.
	 */
	public VariableInfo[] getVariableInfo(TagData data) {
		String id = data.getAttributeString("id");
		if (id!=null) {
			return new VariableInfo[] {
				new VariableInfo( id,
				   "com.freshdirect.webapp.taglib.fdstore.FDSessionUser",
				   true,
				   VariableInfo.AT_BEGIN )
			};
		} else {
			return new VariableInfo[0];
		}
	}

}
