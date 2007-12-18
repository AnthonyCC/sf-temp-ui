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
public class MergeCartControllerTagEI extends TagExtraInfo {

	/**
	 * Return information about the scripting variables to be created.
	 */
	public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {

				new VariableInfo( "cartCurrent",
				   "com.freshdirect.fdstore.customer.FDCartModel",  
				   true,
				   VariableInfo.NESTED ),

				new VariableInfo( "cartSaved",
				   "com.freshdirect.fdstore.customer.FDCartModel",  
				   true,
				   VariableInfo.NESTED ),

				new VariableInfo( "cartMerged",
				   "com.freshdirect.fdstore.customer.FDCartModel",  
				   true,
				   VariableInfo.NESTED )
			};
	}

}
