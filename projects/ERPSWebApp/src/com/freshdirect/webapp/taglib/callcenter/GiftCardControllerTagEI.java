package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.*;

public class GiftCardControllerTagEI extends TagExtraInfo{
	
	/**
	  * Return information about the scripting variables to be created.
	  */
	
	public VariableInfo[] getVariableInfo(TagData data) {
		return new VariableInfo[] {
				new VariableInfo(data.getAttributeString("resultName"),
						"com.freshdirect.framework.webapp.ActionResult",
	                    true,
	                    VariableInfo.NESTED)
		};
	}
}