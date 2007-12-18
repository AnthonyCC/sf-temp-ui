/*
 * DayTagEI.java
 *
 * Created on November 9, 2001, 8:23 PM
 */

package com.freshdirect.webapp.taglib.dlv;

/**
 *
 * @author  knadeem
 * @version 
 */
import javax.servlet.jsp.tagext.*;

public class DayTagEI extends TagExtraInfo {

	public VariableInfo[] getVariableInfo(TagData data) {
		return new VariableInfo[] {
			new VariableInfo(data.getAttributeString("id"),
			"java.util.Map", true, VariableInfo.NESTED),
		};
		
	}

}
