/*
 * DeliveryTimeSlotTagEI.java
 *
 * Created on October 19, 2001, 3:58 PM
 */

package com.freshdirect.webapp.taglib.dlv;

/**
 *
 * @author  rgayle
 * @version
 */

import javax.servlet.jsp.tagext.*;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class DeliveryTimeSlotTagEI extends TagExtraInfo {
	
	/**
	 * Return information about the scripting variables to be created.
	 *
	 */
	public VariableInfo[] getVariableInfo(TagData data) {
		return new VariableInfo[] {
			new VariableInfo(data.getAttributeString("id"),
			"java.util.List",true, VariableInfo.NESTED),
		};
		
	}
	
}
