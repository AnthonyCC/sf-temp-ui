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
public class ComplaintReasonsTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

		String reasonsName = data.getAttributeString("reasons");

		if (reasonsName!=null) {
			return new VariableInfo[] {
				new VariableInfo(reasonsName,
				   "java.util.Collection",
				   true,
				   VariableInfo.NESTED)
			};
		}

		return new VariableInfo[0];

    }

}
