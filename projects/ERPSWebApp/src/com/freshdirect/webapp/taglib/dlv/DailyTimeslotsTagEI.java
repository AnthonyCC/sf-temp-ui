/*
 * DailyTimeslotsTagEI.java
 *
 * Created on November 10, 2001, 7:07 PM
 */

package com.freshdirect.webapp.taglib.dlv;

/**
 *
 * @author  knadeem
 * @version 
 */
import javax.servlet.jsp.tagext.*;

public class DailyTimeslotsTagEI extends TagExtraInfo {

	public VariableInfo[] getVariableInfo(TagData data) {
		return new VariableInfo[] {
			new VariableInfo(data.getAttributeString("id"),
			"java.util.Map", true, VariableInfo.NESTED),
		};
		
	}
}
